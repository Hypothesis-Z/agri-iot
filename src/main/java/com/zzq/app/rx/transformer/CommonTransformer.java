package com.zzq.app.rx.transformer;

import com.zzq.app.bean.OneNETBean;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CommonTransformer<T extends OneNETBean.DataBean> implements ObservableTransformer<OneNETBean, T> {

    @Override
    public Observable<T> apply(Observable<OneNETBean> tansFormerObservable) {
        return tansFormerObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(ErrorTransformer.<T>getInstance());
    }
}

