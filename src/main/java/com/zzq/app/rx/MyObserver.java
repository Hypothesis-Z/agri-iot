package com.zzq.app.rx;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public abstract class MyObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(@NonNull Disposable d){

    }

    @Override
    public void onError(@NonNull Throwable e){

    }

    @Override
    public void onComplete(){

    }

    @Override
    public abstract void onNext(T t);
}
