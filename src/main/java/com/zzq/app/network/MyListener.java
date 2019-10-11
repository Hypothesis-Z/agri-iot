package com.zzq.app.network;

public interface MyListener<T> {
    void onSuccess(T result);
    void onError(String errorMsg);
}
