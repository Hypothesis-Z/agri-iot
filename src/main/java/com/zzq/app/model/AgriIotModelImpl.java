package com.zzq.app.model;

import android.util.Log;

import com.zzq.app.bean.OneNETBean;
import com.zzq.app.contract.AgriIotContract;
import com.zzq.app.base.MvpListener;
import com.zzq.app.network.MyListener;
import com.zzq.app.network.RequestManager;

import java.io.File;

import static com.zzq.app.ui.activity.MainActivity.TAG;

public class AgriIotModelImpl implements AgriIotContract.AgriIotModel {
//    @Override
//    public void loadAgriIot(String url, final MvpListener<OneNETBean.DataBean> listener) {
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
//    }
//
//    @Override
//    public void loadAgriIotHistory(String url, final MvpListener<OneNETBean.DataBean> listener) {
//        RequestManager.getInstance().sendGet(url, OneNETBean.class, new MyListener<OneNETBean>() {
//            @Override
//            public void onSuccess(OneNETBean result) {
//                Log.d(TAG, "MyListener.onSuccess() called.");
//                listener.onSuccess(result.getData());
//            }
//
//            @Override
//            public void onError(String errorMsg) {
//                Log.e(TAG, "MyListener.onError() called.");
//                listener.onError(errorMsg);
//            }
//        });
//    }
//
//    @Override
//    public void loadAgriIotDownload(final MvpListener<OneNETBean> listener){
//        RxBus.getInstance().toObservable(Code.rxCode, File.class).subscribe(new MyObserver<File>() {
//            @Override
//            public void onNext(@NonNull File file){
//
//            }
//        });


//    }
}
