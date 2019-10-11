package com.zzq.app.contract;

import com.zzq.app.bean.OneNETBean;
import com.zzq.app.base.BaseModel;
import com.zzq.app.base.BasePresenter;
import com.zzq.app.base.BaseView;
import com.zzq.app.base.MvpListener;

public interface AgriIotContract {
    interface AgriIotModel extends BaseModel{
        //void loadAgriIot(String url, MvpListener<OneNETBean.DataBean> listener);
        //void loadAgriIotHistory(String url, MvpListener<OneNETBean.DataBean> listener);
        //void loadAgriIotDownload(MvpListener<OneNETBean> listener);
    }

    interface AgriIotView extends BaseView {
        //void setData(OneNETBean.DataBean bean);
        //void setDataHistory(OneNETBean.DataBean bean);
    }

    abstract class AgriIotPresenter extends BasePresenter<AgriIotModel, AgriIotView>{
        //protected abstract void loadData(String url);
        //protected abstract void loadDataHistory(String url);
        //protected abstract void loadDataDownload();

    }
}
