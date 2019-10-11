package com.zzq.app.contract;

import com.zzq.app.base.BaseModel;
import com.zzq.app.base.BaseMvpFragment;
import com.zzq.app.base.BasePresenter;
import com.zzq.app.base.BaseView;
import com.zzq.app.base.MvpListener;
import com.zzq.app.bean.OneNETBean;

import io.reactivex.Observable;

public interface RealtimeContract {
    interface RealtimeModel extends BaseModel{
        void loadRealtime(String url, MvpListener<OneNETBean.DataBean> listener);
        Observable<OneNETBean> loadRealtime();
    }

    interface RealtimeView extends BaseView {
        void setRealtimeData(OneNETBean.DataBean bean);
    }

    abstract class RealtimePresenter extends BasePresenter<RealtimeModel, RealtimeView>{
        protected abstract void loadRealtimeData(String url);
        protected abstract void loadRealtimeData();
    }
}

