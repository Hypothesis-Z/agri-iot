package com.zzq.app.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzq.app.app.App;
import com.zzq.app.http.service.OneNETService;
import com.zzq.app.util.NetworkUtil;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Http {
    private static OkHttpClient client;
    private static OneNETService service;
    private static volatile Retrofit retrofit;

    public static OneNETService getOneNETService(){
        if (service == null){
            service = getRetrofit().create(OneNETService.class);
        }
        return service;
    }

    private static Interceptor addQueryParameterInterceptor(){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request request;
                HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                        .addQueryParameter("api-key", "LO79mBj364HBhD1ZuPNPEDiMMAw=")
                        .build();
                request = originalRequest.newBuilder().url(modifiedUrl).build();
                return chain.proceed(request);
            }
        };
    }

    private static Interceptor addHeaderInterceptor(){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .header("api-key", "LO79mBj364HBhD1ZuPNPEDiMMAw=")
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
    }

    private static Interceptor addCacheInterceptor(){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetworkUtil.isNetworkAvailable(App.getInstance())){
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (NetworkUtil.isNetworkAvailable(App.getInstance())){
                    int maxAge = 0;
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age" + maxAge)
                            .removeHeader("Retrofit")
                            .build();
                } else {
                    int maxSatle = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control",
                                    "public, only-if-cached, max-stale=" + maxSatle)
                            .removeHeader("nyn")
                            .build();
                }
                return response;
            }
        };
    }

    public static Retrofit getRetrofit(){
        if (retrofit == null){
            synchronized (Http.class){
                if (retrofit == null){
                    //添加一个log拦截器,打印所有的log
                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                    //可以设置请求过滤的水平,body,basic,headers
                    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    //设置 请求的缓存的大小跟位置
                    File cacheFile = new File(App.getInstance().getCacheDir(), "cache");
                    Cache cache = new Cache(cacheFile, 1024 * 1024 * 50); //50Mb 缓存的大小

                    client = new OkHttpClient
                            .Builder()
//                            .addInterceptor(addQueryParameterInterceptor())  //参数添加
                            .addInterceptor(addHeaderInterceptor()) // token过滤
                            .addInterceptor(httpLoggingInterceptor) //日志,所有的请求响应度看到
//                            .cache(cache)  //添加缓存
                            .connectTimeout(60L, TimeUnit.SECONDS)
                            .readTimeout(60L, TimeUnit.SECONDS)
                            .writeTimeout(60L, TimeUnit.SECONDS)
                            .build();

                    // 获取retrofit的实例
                    retrofit = new Retrofit
                            .Builder()
                            .baseUrl(OneNETService.API_ONENET)  //自己配置
                            .client(client)
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }
}
