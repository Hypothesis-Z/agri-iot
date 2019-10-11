package com.zzq.app.presenter;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zzq.app.bean.OneNETBean;
import com.zzq.app.contract.AgriIotContract;
import com.zzq.app.base.MvpListener;
import com.zzq.app.util.ToastUtil;

import static com.zzq.app.ui.activity.MainActivity.TAG;

public class AgriIotPresenterImpl extends AgriIotContract.AgriIotPresenter {
//    @Override
//    public void loadData(String url){
//        final AgriIotContract.AgriIotView mView = getView();
//        if (mView == null){
//            return;
//        }
//
//        mView.showLoading();
//        mModel.loadAgriIot(url, new MvpListener<OneNETBean.DataBean>() {
//            @Override
//            public void onSuccess(OneNETBean.DataBean result) {
//                mView.hideLoading();
//                mView.setData(result);
//            }
//
//            @Override
//            public void onError(String errorMsg) {
//                mView.hideLoading();
//                mView.showError();
//            }
//        });
//    }
//
//    @Override
//    public void loadDataHistory(String url){
//        final AgriIotContract.AgriIotView mView = getView();
//        if (mView == null){
//            return;
//        }
//
//        mView.showLoading();
//        mModel.loadAgriIotHistory(url, new MvpListener<OneNETBean.DataBean>() {
//            @Override
//            public void onSuccess(OneNETBean.DataBean result) {
//                Log.d(TAG, "MvpListener.onSuccess() called.");
//                mView.hideLoading();
//                mView.setDataHistory(result);
//            }
//
//            @Override
//            public void onError(String errorMsg) {
//                Log.e(TAG, "MvpListener.onError() called. Error message is " + errorMsg);
//                mView.hideLoading();
//                mView.showError();
//            }
//        });
//    }
//
//    @Override
//    public void loadDataDownload() {
//        final AgriIotContract.AgriIotView mView = getView();
//        if (mView == null) {
//            return;
//        }
//
//        ToastUtil.showToast(((AppCompatActivity) mView)
//                .getResources().getString(com.zzq.app.R.string.downloading));
//        mModel.loadAgriIotDownload(new MvpListener<OneNETBean>(){
//            @Override
//            public void onSuccess(OneNETBean result){
//                ToastUtil.showToast(((AppCompatActivity) mView)
//                        .getResources().getString(com.zzq.app.R.string.downloadOnSuccess));
//            }
//
//            @Override
//            public void onError(String errorMsg){
//                ToastUtil.showToast(((AppCompatActivity) mView)
//                        .getResources().getString(com.zzq.app.R.string.downloadOnError));
//            }
//        });
//    }

}
