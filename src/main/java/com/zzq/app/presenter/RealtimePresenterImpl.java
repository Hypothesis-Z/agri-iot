package com.zzq.app.presenter;

import com.zzq.app.base.MvpListener;
import com.zzq.app.bean.OneNETBean;
import com.zzq.app.contract.AgriIotContract;
import com.zzq.app.contract.RealtimeContract;
import com.zzq.app.rx.LifeDisposable;
import com.zzq.app.rx.scheduler.SchedulerProvider;
import com.zzq.app.rx.transformer.ResponseTransformer;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RealtimePresenterImpl extends RealtimeContract.RealtimePresenter {

    @Override
    public void loadRealtimeData(){
        final RealtimeContract.RealtimeView mView = getView();
        if (mView == null){
            return;
        }

        mView.showLoading();
        Observable<OneNETBean> observable = mModel.loadRealtime();
        Observable<OneNETBean.DataBean> observable2 = observable.compose(ResponseTransformer.handleResult());
        Observable<Object> observable3 = observable2.compose(SchedulerProvider.getInstance().applySchedulers());
        Disposable disposable = observable3.subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object beans) throws Exception {
                        // 处理数据
                        mView.setRealtimeData((OneNETBean.DataBean) beans);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // 处理异常
                        throwable.printStackTrace();
                    }
                });
//        Disposable disposable = mModel.loadRealtime()
//                .compose(ResponseTransformer.handleResult())
//                .compose(SchedulerProvider.getInstance().applySchedulers())
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(Object beans) throws Exception {
//                        // 处理数据
//                        mView.setRealtimeData((OneNETBean.DataBean) beans);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        // 处理异常
//                        throwable.printStackTrace();
//                    }
//                });
        LifeDisposable lifeDisposable = (LifeDisposable) mView;
        lifeDisposable.bindDisposable(disposable);
    }

    @Override
    public void loadRealtimeData(String url) {
        final RealtimeContract.RealtimeView mView = getView();
        if (mView == null){
            return;
        }

        mView.showLoading();
        mModel.loadRealtime(url, new MvpListener<OneNETBean.DataBean>() {
            @Override
            public void onSuccess(OneNETBean.DataBean result) {
                mView.hideLoading();
                mView.setRealtimeData(result);
            }

            @Override
            public void onError(String errorMsg) {
                mView.hideLoading();
                mView.showError();
            }
        });
    }
}
