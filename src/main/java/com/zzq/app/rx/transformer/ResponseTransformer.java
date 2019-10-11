package com.zzq.app.rx.transformer;

import com.zzq.app.bean.OneNETBean;
import com.zzq.app.rx.exception.ApiException;
import com.zzq.app.rx.exception.CustomException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import retrofit2.Response;

public class ResponseTransformer {

    public static ObservableTransformer<OneNETBean, OneNETBean.DataBean> handleResult() {
        return new ObservableTransformer<OneNETBean, OneNETBean.DataBean>() {
            @Override
            public ObservableSource<OneNETBean.DataBean> apply(Observable<OneNETBean> upstream) {
                return upstream.onErrorResumeNext(new ErrorResumeFunction())
                        .flatMap(new ResponseFunction());
            }
        };
    }


    /**
     * 非服务器产生的异常，比如本地无无网络请求，Json数据解析错误等等。
     *
     */
    private static class ErrorResumeFunction implements Function<Throwable, ObservableSource<? extends OneNETBean>> {

        @Override
        public ObservableSource<? extends OneNETBean> apply(Throwable throwable) throws Exception {
            return Observable.error(CustomException.handleException(throwable));
        }
    }

    /**
     * 服务其返回的数据解析
     * 正常服务器返回数据和服务器可能返回的exception
     *
     */
    private static class ResponseFunction implements Function<OneNETBean, ObservableSource<OneNETBean.DataBean>> {

        @Override
        public ObservableSource<OneNETBean.DataBean> apply(OneNETBean tResponse) throws Exception {
            int code = tResponse.getErrno();
            String message = tResponse.getError();
            if (code == 0) {
                return Observable.just(tResponse.getData());
            } else {
                return Observable.error(new ApiException(code, message));
            }
        }
    }
}