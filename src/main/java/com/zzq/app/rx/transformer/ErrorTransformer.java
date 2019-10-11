package com.zzq.app.rx.transformer;

import android.util.Log;

import com.zzq.app.bean.OneNETBean;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

public class ErrorTransformer<T extends OneNETBean.DataBean> implements ObservableTransformer<OneNETBean, T> {

    private static ErrorTransformer errorTransformer = null;
    private static final String TAG = "ErrorTransformer";

    @Override
    public Observable<T> apply(Observable<OneNETBean> responseObservable) {

        return responseObservable.map(new Function<OneNETBean, T>() {
            @Override
            public T apply(OneNETBean bean) {
//                if (bean == null)
//                    throw new ServerException(ErrorType.EMPTY_BEAN, "解析对象为空");
//
//                Log.e(TAG, httpResult.toString());
//
//                if (httpResult.getStatus() != ErrorType.SUCCESS)
//                    throw new ServerException(httpResult.getStatus(), httpResult.getMessage());
                return (T) bean.getData();
            }
        });
//                .onErrorResumeNext(new Function<Throwable, Observable<? extends T>>() {
//            @Override
//            public Observable<? extends T> call(Throwable throwable) {
//                //ExceptionEngine为处理异常的驱动器throwable
//                throwable.printStackTrace();
//                return Observable.error(ExceptionEngine.handleException(throwable));
//            }
//        });

    }

    /**
     * @return 线程安全, 双层校验
     */
    public static <T extends OneNETBean.DataBean> ErrorTransformer<T> getInstance() {

        if (errorTransformer == null) {
            synchronized (ErrorTransformer.class) {
                if (errorTransformer == null) {
                    errorTransformer = new ErrorTransformer<>();
                }
            }
        }
        return errorTransformer;

    }
}
