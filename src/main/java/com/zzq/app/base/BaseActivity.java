package com.zzq.app.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.zzq.app.rx.LifeDisposable;
import com.zzq.app.util.ToastUtil;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.subscriptions.ArrayCompositeSubscription;

/*
 * Activity基类，具有返回确认功能
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView, LifeDisposable {
    protected CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initView();
    }

    protected abstract int getLayoutId();
    protected abstract void initView();

    @Override
    public void showLoading(){

    }

    @Override
    public void hideLoading(){

    }

    @Override
    public void showError(){

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mCompositeDisposable!=null) {
            mCompositeDisposable.clear();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        return checkBackAction() || super.onKeyDown(keyCode, event);
    }

    private boolean mFlag = false;
    private long mTimeout = -1;
    private boolean checkBackAction(){
        long time = 3000L;
        boolean flag = mFlag;
        mFlag = true;
        boolean timeout = (mTimeout == -1 || (System.currentTimeMillis() - mTimeout) > time);
        if (mFlag && (mFlag != flag || timeout)){
            mTimeout = System.currentTimeMillis();
            ToastUtil.showToast("再点击一次回到桌面");
            return true;
        }
        return !mFlag;
    }

    @Override
    public void bindDisposable(Disposable disposable) {
        if (this.mCompositeDisposable == null) {
            this.mCompositeDisposable = new CompositeDisposable();
        }
        this.mCompositeDisposable.add(disposable);
    }

    public CompositeDisposable getCompositeDisposable(){
        return mCompositeDisposable == null ? new CompositeDisposable() : mCompositeDisposable;
    }
}
