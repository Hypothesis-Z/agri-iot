package com.zzq.app.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzq.app.R;
import com.zzq.app.rx.LifeDisposable;

import org.reactivestreams.Subscription;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment extends Fragment implements BaseView, LifeDisposable {
    protected Context mContext;
    public View mView;
    private Unbinder mUnBinder;
    private boolean isViewCreated = false;
    protected boolean isInitData = false;
    private boolean mIsVisible = false;     // fragment是否显示了

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        if (mView == null) {
            mView = inflater.inflate(setLayoutResourceID(), container, false);
            try {
                mUnBinder = ButterKnife.bind(this, mView);
            }catch (Exception e){
                e.printStackTrace();
            }
            isViewCreated = true;
        } else {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (null != parent) {
                parent.removeView(mView);
            }
        }
        initView();
        return mView;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.mContext = context;
    }

    /**
     * 在这里实现Fragment数据的缓加载.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {//fragment可见
            mIsVisible = true;
            onVisible();
        } else {//fragment不可见
            mIsVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getUserVisibleHint()) {
            initData();
        }
    }

    protected abstract int setLayoutResourceID();

    protected abstract void initView();

    protected abstract void initData();

    protected void onVisible() {
        isInitData = true;
        initData();
    }

    protected void onInvisible() {

    }

    protected CompositeDisposable mCompositeDisposable;

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

    @Override
    public void onDetach() {
        super.onDetach();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        if (this.mCompositeDisposable != null) {
            this.mCompositeDisposable.clear();
        }
    }

    @Override
    public void showLoading(){

    }

    @Override
    public void hideLoading(){

    }

    @Override
    public void showError(){

    }
}
