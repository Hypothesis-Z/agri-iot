package com.zzq.app.rx;


import io.reactivex.functions.Consumer;

public interface MyConsumer<T> extends Consumer<T> {
    void accept(T t) throws Exception;
}
