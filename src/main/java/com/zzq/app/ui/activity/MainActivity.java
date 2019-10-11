package com.zzq.app.ui.activity;

import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.zzq.app.R;
import com.zzq.app.api.Api;
import com.zzq.app.api.Device;
import com.zzq.app.app.App;
import com.zzq.app.bean.OneNETBean;
import com.zzq.app.contract.AgriIotContract;
import com.zzq.app.contract.RealtimeContract;
import com.zzq.app.model.AgriIotModelImpl;
import com.zzq.app.presenter.AgriIotPresenterImpl;
import com.zzq.app.ui.adapter.HistoryAdapter;
import com.zzq.app.ui.adapter.RealTimeAdapter;
import com.zzq.app.base.BaseMvpActivity;
import com.zzq.app.ui.fragment.RealtimeFragment;
import com.zzq.app.util.DateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseMvpActivity<AgriIotPresenterImpl, AgriIotModelImpl>
        implements AgriIotContract.AgriIotView, NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener{

    public static final String TAG = "MainActivity";
    private RealTimeAdapter realTimeAdapter;
    private HistoryAdapter historyAdapter;
    private MenuItem mMenuItem;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
//    @BindView(R.id.content) ConstraintLayout content;
//    @BindView(R.id.refresh) ImageButton refreshImgBtn;
//    @BindView(R.id.ifOnline) ImageButton ifOnlineImgBtn;
//    @BindView(R.id.download) ImageButton downloadImgBtn;
//    @BindView(R.id.main_recycler) RecyclerView rcv;
//    @BindView(R.id.text_start_time) EditText textStart;
//    @BindView(R.id.text_end_time) EditText textEnd;
//    @BindView(R.id.imgBtn_start_time) ImageButton imgBtnStart;
//    @BindView(R.id.imgBtn_end_time) ImageButton imgBtnEnd;

//    @OnClick({R.id.btn_recent_hour, R.id.btn_recent_day, R.id.btn_recent_week, R.id.btn_custom})
//    public void onBtnsClicked(View view){
//        int id = view.getId();
//        // Set Button OnClickListener
//        Integer[] integers = new Integer[]{R.id.btn_recent_hour, R.id.btn_recent_day,
//                R.id.btn_recent_week, R.id.btn_custom};
//        List<Integer> list = Arrays.asList(integers);
//        if (list.contains(id)){
//            new ArrayList<Integer>(list){
//
//                void setButtonSelected(int id){
//                    for (Integer integer:this){
//                        Button button = findViewById(integer);
//                        if (id == integer){
//                            // 修改按钮及其字体颜色
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                //Android系统大于等于API16，使用setBackground
//                                button.setBackground(getResources().getDrawable(R.drawable.btn_request_history_selected));
//                            } else {
//                                //Android系统小于API16，使用setBackgroundDrawable
//                                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_request_history_selected));
//                            }
//                            button.setTextColor(getResources().getColor(R.color.colorWhite));
//                        }
//                        else {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                //Android系统大于等于API16，使用setBackground
//                                button.setBackground(getResources().getDrawable(R.drawable.btn_request_history));
//                            } else {
//                                //Android系统小于API16，使用setBackgroundDrawable
//                                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_request_history));
//                            }
//                            button.setTextColor(getResources().getColor(R.color.colorPrimary));
//                        }
//                    }
//
//                    // Set ImageButtons and EditTexts
//                    long end = System.currentTimeMillis();
//                    long start = end;
//                    if (id == R.id.btn_recent_hour){
//                        start = end - 3600000;
//                    }
//                    else if (id == R.id.btn_recent_day){
//                        start = end - 86400000;
//                    }
//                    else if (id == R.id.btn_recent_week){
//                        start = end - 604800000;
//                    }
//                    textEnd.setText(DateUtil.format(new Date(end), "yyyy-MM-dd'T'HH:mm:ss"));
//                    textEnd.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//                    textStart.setText(DateUtil.format(new Date(start), "yyyy-MM-dd'T'HH:mm:ss"));
//                    textStart.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//                    imgBtnStart.setEnabled(false);
//                    imgBtnEnd.setEnabled(false);
//                    if (id == R.id.btn_custom) {
//                        textEnd.setText(getResources().getString(R.string.end_time));
//                        textEnd.setTextColor(getResources().getColor(R.color.colorGray));
//                        textStart.setText(getResources().getString(R.string.start_time));
//                        textStart.setTextColor(getResources().getColor(R.color.colorGray));
//                        imgBtnStart.setEnabled(true);
//                        imgBtnEnd.setEnabled(true);
//                    }
//                }
//            }.setButtonSelected(id);
//        }
//    }
//
//    @OnClick({R.id.imgBtn_start_time, R.id.imgBtn_end_time})
//    public void onImgBtnsClicked(View view){
//        int id = view.getId();
//        final EditText editText;
//        if (id == R.id.imgBtn_start_time)
//            editText = textStart;
//        else
//            editText = textEnd;
//        // 时间选择器
//        TimePickerView timePicker = new TimePickerBuilder(MainActivity.this, new OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date, View v) {
//                editText.setText(DateUtil.format(date, "yyyy-MM-dd'T'HH:mm:ss"));
//                editText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//            }
//        })
//                .setType(new boolean[]{true,true,true,true,true,false})
//                .setCancelText("取消")
//                .setSubmitText("确认")
//                .isCyclic(true)
//                .setOutSideCancelable(true)
//                .setLabel("年","月","日","时","分","秒")
//                .isCenterLabel(true)
//                .build();
//        timePicker.show();
//    }
//
//    @OnClick(R.id.btn_query)
//    public void onQueryClicked(){
//        String startTime = textStart.getText().toString();
//        String endTime = textEnd.getText().toString();
//        String url = Api.ONENET + Device.DEVICE_ESP8266 + "/" + "datapoints" +
//                "?start="+ startTime + "&end=" + endTime + "&limit=4000";
//        Log.i(TAG, "url = " + url);
//        mPresenter.loadDataHistory(url);
//    }

//    @OnClick(R.id.refresh)
//    public void onRefreshClicked(){
//        loadData();
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView() {

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager= getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        final RealtimeFragment realtimeFragment = new RealtimeFragment();
        fragmentTransaction.add(R.id.content, realtimeFragment, "realtime");
        fragmentTransaction.commit();

//        // Load Layout
//        content.removeAllViews();
//        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
//        inflater.inflate(R.layout.content_main_recycler, content);


//        // ToolBar Visibility
//        refreshImgBtn.setVisibility(View.VISIBLE);
//        ifOnlineImgBtn.setVisibility(View.VISIBLE);
//        downloadImgBtn.setVisibility(View.GONE);

//        // Set RecyclerView
//        realTimeAdapter = new RealTimeAdapter(this);
//        rcv.setLayoutManager(new LinearLayoutManager(this));
//        rcv.setHasFixedSize(true);
//        rcv.setAdapter(realTimeAdapter);
    }

    @Override
    protected void loadData() {
//        String url = Api.ONENET + Device.DEVICE_ESP8266 + "/" + "datapoints";
//        Log.i(TAG, "url = " + url);
//        mPresenter.loadData(url);
    }

//    @Override
//    public void setData(OneNETBean.DataBean bean) {
//        realTimeAdapter.setDatastreamsBeanList(bean.getDatastreams());
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        mMenuItem = (MenuItem) findViewById(id);
        if (id == R.id.nav_realTime) {
//            initView();
//            loadData();
        } else if (id == R.id.nav_history) {
            // initViewHistory();
        } else if (id == R.id.nav_downloadList) {

        } else if (id == R.id.nav_setup) {

        } else if (id == R.id.nav_logout) {

        } else if (id == R.id.nav_exit) {
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view){
        int id = view.getId();

        if (id == R.id.download){

        }


    }

//    public void initViewHistory(){
//        // Load layout
//        content.removeAllViews();
//        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
//        inflater.inflate(R.layout.content_main_request_history_parameters, content);
//
//        // ToolBar initialization
//        refreshImgBtn.setVisibility(View.GONE);
//        ifOnlineImgBtn.setVisibility(View.GONE);
//        downloadImgBtn.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void setDataHistory(OneNETBean.DataBean bean) {
//        // Load Layout
//        content.removeAllViews();
//        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
//        inflater.inflate(R.layout.content_main_recycler, content);
//
//        // Set adapter
//        historyAdapter = new HistoryAdapter(this);
//        rcv.setLayoutManager(new LinearLayoutManager(this));
//        rcv.setHasFixedSize(true);
//        rcv.setAdapter(historyAdapter);
//
//        // Set adapter data
//        Log.d(TAG, "Setting datastreams bean list.");
//        historyAdapter.setDatastreamsBeanList(bean.getDatastreams());
//    }

}
