package com.zzq.app.rx;

import io.reactivex.disposables.Disposable;

public interface LifeDisposable {
    void bindDisposable(Disposable disposable);
}
