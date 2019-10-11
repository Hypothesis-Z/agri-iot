package com.zzq.app.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zzq.app.util.ReflectUtil;

import java.util.Observable;

public abstract class BaseMvpFragment<T extends BasePresenter, M extends BaseModel>
        extends BaseFragment {
    protected T mPresenter;
    protected M mModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mPresenter = ReflectUtil.getT(this, 0);
        mModel = ReflectUtil.getT(this, 1);
        mPresenter.onAttach(mModel, this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mPresenter.onDetach();
    }
}
