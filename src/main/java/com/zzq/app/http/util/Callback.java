package com.zzq.app.http.util;

import com.zzq.app.api.AppConstant;
import com.zzq.app.base.BaseView;
import com.zzq.app.http.Stateful;

import org.reactivestreams.Subscriber;


//public class Callback<T> extends Subscriber<T> {
//    private Stateful target;
//
//    public void setTarget(Stateful target){
//        this.target = target;
//    }
//
//    @Override
//    public void onComplete(){
//
//    }
//
//    @Override
//    public void onError(Throwable e){
//        e.printStackTrace();
//        onFail();
//    }
//
//    @Override
//    public void onNext(T data){
//        target.setState(AppConstant.STATE_SUCCESS);
//        onResponse();
//        onResponse(data);
//    }
//
//    public void onResponse(){
//
//    }
//
//    public void onResponse(T data){
//        ((BaseView) target).refreshView(data);
//    };
//}

