package com.zzq.app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dashboard.trc.DashboardView;
import com.zzq.app.R;
import com.zzq.app.api.Datapoint;
import com.zzq.app.bean.OneNETBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;


public class RealTimeAdapter extends RecyclerView.Adapter<RealTimeAdapter.RealTimeHolder> {
    private Context context;
    private List<OneNETBean.DataBean.DatastreamsBean> datastreamsBeanList;

    public RealTimeAdapter(Context context){
        this.context = context;
        datastreamsBeanList = new ArrayList<>();
    }

    public void setDatastreamsBeanList(List<OneNETBean.DataBean.DatastreamsBean> list){
        this.datastreamsBeanList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return datastreamsBeanList.size();
    }

    @NonNull
    @Override
    public RealTimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new RealTimeHolder(LayoutInflater.from(context)
                .inflate(R.layout.content_main_realtime_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RealTimeHolder holder, int position){
        OneNETBean.DataBean.DatastreamsBean bean = datastreamsBeanList.get(position);
        holder.tv_update_time_value.setText(bean.getDatapoints().get(0).getAt()
                .split("\\.")[0]);
        Datapoint.DatapointParameter dp = Datapoint.getDatapointMap().get(bean.getId());
        if (dp == null)
            return;
        String str = dp.getName() + "/" + dp.getUnit();
        holder.tv_parameter.setText(str);
        holder.tv_parameter_value.setText(new DecimalFormat("0.00")
                .format(bean.getDatapoints().get(0).getValue()));
        holder.dashboardView.setMeasureTextSize(12);
        holder.dashboardView.setHeaderTitle(dp.getUnit());
        holder.dashboardView.setRealTimeValue(bean.getDatapoints().get(0).getValue());
        holder.dashboardView.setMinValue(dp.getLowerLimitation());
        holder.dashboardView.setMaxValue(dp.getUpperLimitation());
        holder.dashboardView.setPointerColor(context.getResources().getColor(R.color.colorPrimaryDark));
    }

    static class RealTimeHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_update_time)
        TextView tv_update_time;

        @BindView(R.id.tv_update_time_value)
        TextView tv_update_time_value;

        @BindView(R.id.tv_parameter)
        TextView tv_parameter;

        @BindView(R.id.tv_parameter_value)
        TextView tv_parameter_value;

        @BindView(R.id.dashboardView)
        DashboardView dashboardView;

        RealTimeHolder(View itemView){
            super(itemView);
        }
    }
}
