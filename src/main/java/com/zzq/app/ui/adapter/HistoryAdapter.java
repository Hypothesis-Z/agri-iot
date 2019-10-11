package com.zzq.app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.zzq.app.R;
import com.zzq.app.api.Datapoint;
import com.zzq.app.bean.OneNETBean;
import com.zzq.app.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;

import static com.zzq.app.ui.activity.MainActivity.TAG;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {
    private Context context;
    private List<OneNETBean.DataBean.DatastreamsBean> datastreamsBeanList;

    public HistoryAdapter(Context context){
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
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new HistoryHolder(LayoutInflater.from(context)
        .inflate(R.layout.content_main_history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position){
        OneNETBean.DataBean.DatastreamsBean bean = datastreamsBeanList.get(position);
        List<Entry> entries = new ArrayList<>();
        final long firstTime;
        Date firstDate = DateUtil.parse(bean.getDatapoints().get(0).getAt().split("\\.")[0], "yyyy-MM-dd HH:mm:ss");
        if (firstDate == null)
            return;
        firstTime = firstDate.getTime();
        for (OneNETBean.DataBean.DatastreamsBean.DatapointsBean datapoint : bean.getDatapoints()) {
            Date date = DateUtil.parse(datapoint.getAt().split("\\.")[0], "yyyy-MM-dd HH:mm:ss");
            if (date == null)
                return;
            entries.add(new Entry(date.getTime() - firstTime, (float) datapoint.getValue()));
        }

        // set data set
        Datapoint.DatapointParameter dp = Datapoint.getDatapointMap().get(bean.getId());
        if (dp == null)
            return;
        LineDataSet dataSet = new LineDataSet(entries,dp.getName());
        dataSet.setColor(context.getResources().getColor(R.color.colorPrimary));
        dataSet.setValueTextColor(context.getResources().getColor(R.color.colorPrimary));
        dataSet.setDrawFilled(true);
        dataSet.setFillDrawable(context.getResources().getDrawable(R.drawable.linechart_fade_blue));
        dataSet.setLineWidth(2);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);

        // setChart
        LineData lineData = new LineData(dataSet);
        holder.lineChart.setData(lineData);
        holder.lineChart.setMinimumHeight(500);
        holder.lineChart.setDescription(null);
        holder.lineChart.setPadding(20, 30, 20, 30);
        //if (i % 2 == 0) chart.setBackgroundColor(getResources().getColor(R.color.colorGray));
        holder.lineChart.setDrawBorders(false);
        holder.lineChart.setDrawGridBackground(false);

        // set X axis
        XAxis xAxis = holder.lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0);
        Log.d(TAG, "XAxisMinimum: 0");
        xAxis.setAxisMaximum(entries.get(entries.size() - 1).getX());
        Log.d(TAG, "XAxisMaximum:" + entries.get(entries.size() - 1).getX());
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter(){
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String stringXAxis = DateUtil.format(new Date((long)value + firstTime), "MM-dd HH:mm");
                Log.v(TAG, "String of XAxis: " + stringXAxis);
                return stringXAxis;
            }
        });

        // set Y axis
        YAxis yAxisLeft = holder.lineChart.getAxisLeft();
        YAxis yAxisRight = holder.lineChart.getAxisRight();
        yAxisLeft.setDrawGridLines(true);
        yAxisRight.setDrawGridLines(false);
        yAxisLeft.enableGridDashedLine(10f, 10f, 0f);
        yAxisRight.setEnabled(false);

        // set legend
        Legend legend = holder.lineChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setTextSize(15);

        holder.lineChart.invalidate();
    }

    static class HistoryHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.main_history_lineChart)
        LineChart lineChart;

        HistoryHolder(View itemView){
            super(itemView);
        }
    }
}
