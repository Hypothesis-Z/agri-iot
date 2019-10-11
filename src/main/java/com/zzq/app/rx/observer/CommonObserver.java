package com.zzq.app.rx.observer;

import org.reactivestreams.Subscriber;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CommonObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable disposable){

    }
    @Override
    public void onComplete(){

    }
    @Override
    public void onNext(T t){

    }
    @Override
    public void onError(Throwable e){

    }
}
