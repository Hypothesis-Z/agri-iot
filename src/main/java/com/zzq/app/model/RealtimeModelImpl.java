package com.zzq.app.model;

import com.zzq.app.base.MvpListener;
import com.zzq.app.bean.OneNETBean;
import com.zzq.app.contract.RealtimeContract;
import com.zzq.app.http.Http;
import com.zzq.app.http.service.OneNETService;
import com.zzq.app.network.MyListener;
import com.zzq.app.network.RequestManager;
import com.zzq.app.rx.observer.CommonObserver;
import com.zzq.app.rx.scheduler.SchedulerProvider;
import com.zzq.app.rx.transformer.CommonTransformer;
import com.zzq.app.rx.transformer.ResponseTransformer;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;

public class RealtimeModelImpl implements RealtimeContract.RealtimeModel {

    protected static OneNETService service;

    @Override
    public Observable<OneNETBean> loadRealtime(){
        service = Http.getOneNETService();
        return service.fetchRealtimeData();
    }

    @Override
    public void loadRealtime(String url, final MvpListener<OneNETBean.DataBean> listener) {
//        RequestManager.getInstance().sendGet(url, OneNETBean.class, new MyListener<OneNETBean>() {
//            @Override
//            public void onSuccess(OneNETBean result) {
//                listener.onSuccess(result.getData());
//            }
//
//            @Override
//            public void onError(String errorMsg) {
//                listener.onError(errorMsg);
//            }
//        });
    }


}
