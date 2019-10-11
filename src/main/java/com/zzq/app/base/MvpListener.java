package com.zzq.app.base;

public interface MvpListener<T> {
    void onSuccess(T result);
    void onError(String errorMsg);
}
