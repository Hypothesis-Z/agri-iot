package com.zzq.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import com.zzq.app.R;
import com.zzq.app.api.Api;
import com.zzq.app.api.Device;
import com.zzq.app.base.BaseMvpFragment;
import com.zzq.app.bean.OneNETBean;
import com.zzq.app.contract.RealtimeContract;
import com.zzq.app.model.RealtimeModelImpl;
import com.zzq.app.presenter.RealtimePresenterImpl;
import com.zzq.app.ui.adapter.RealTimeAdapter;

import butterknife.BindView;

import static com.zzq.app.ui.activity.MainActivity.TAG;

public class RealtimeFragment extends BaseMvpFragment<RealtimePresenterImpl, RealtimeModelImpl>
        implements RealtimeContract.RealtimeView {
    private RealTimeAdapter realTimeAdapter;

    @BindView(R.id.main_recycler)
    RecyclerView rcv;

    @Override
    protected void initView() {
        realTimeAdapter = new RealTimeAdapter(mContext);
        rcv.setLayoutManager(new LinearLayoutManager(mContext));
        rcv.setHasFixedSize(true);
        rcv.setAdapter(realTimeAdapter);
    }

    @Override
    protected void initData() {
        mPresenter.loadRealtimeData();
    }


    @Override
    public void setRealtimeData(OneNETBean.DataBean bean) {
        realTimeAdapter.setDatastreamsBeanList(bean.getDatastreams());
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.content_main_recycler;
    }

}
