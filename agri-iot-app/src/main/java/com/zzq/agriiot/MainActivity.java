package com.zzq.agriiot;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorLong;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.dashboard.trc.AngleBean;
import com.dashboard.trc.DashBoardManager;
import com.dashboard.trc.DashboardView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.apache.log4j.chainsaw.Main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = "MainActivity";

    /**
     * Request code
     */
    private static final int REQUEST_LOGIN = 1;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private static final int MENU_OPEN = 1;
    private static final int MENU_RENAME = 2;
    private static final int MENU_DELETE = 3;

    /**
     * Account
     */
    private String[] strAccount = null;
    private TCPConnectionTask mTask = null;
    private Handler taskHandler = new Handler();
    private Runnable taskRunnable = null;
    private View mContent;
    private View mProgress;
    private MenuItem mMenuItem;
    private boolean ifOnline = true;
    private ShowDownloadListRunnable mShowDownloadListRunnable;
    private Map<String, String> deviceMap = new HashMap<>();
    private String device = "esp8266";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mContent = findViewById(R.id.content);
        mProgress = findViewById(R.id.main_progress);
        mMenuItem = (MenuItem) findViewById(R.id.nav_realTime);

        ImageButton refreshImgBtn = (ImageButton) findViewById(R.id.refresh);
        refreshImgBtn.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v){
                if (mMenuItem == (MenuItem) findViewById(R.id.nav_realTime)) {
                    taskHandler.removeCallbacks(taskRunnable);
                    afterLogin(true);
                }
            }
        });

        final ImageButton ifOnlineImgBtn = (ImageButton) findViewById(R.id.ifOnline);
        ifOnlineImgBtn.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v){
                String stringIfOnline = ifOnline ? "服务器连接正常" : "服务器连接失败";
                // 底部消息
                Snackbar.make(v, stringIfOnline, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });


        // 判断是否连接正常
        final Handler handlerIfOnline = new Handler();
        Runnable runnableIfOnline = new Runnable() {
            @Override
            public void run() {
                if (ifOnline){
                    ifOnlineImgBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_cloud_queue_black_24dp));
                }
                else{
                    ifOnlineImgBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_cloud_off_black_24dp));
                }
                ifOnlineImgBtn.refreshDrawableState();
                // 定时30秒重复执行
                handlerIfOnline.postDelayed(this, 30000);
            }
        };
        // 开始执行
        handlerIfOnline.postDelayed(runnableIfOnline, 10000);

        deviceMap.put("esp8266", "503698659");
        deviceMap.put("esp8266_2", "524629255");
        // Login Activity
        Login();
        //afterLogin();
    }

    private void afterLogin(boolean firstCall){

        if(strAccount==null)
            return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = (TextView)findViewById(R.id.textView);
                textView.setText(strAccount[0]);
            }
        });

        Log.i(TAG, "account set");

        taskRunnable = new Runnable() {
            @Override
            public void run() {
                ImageButton refreshImgBtn = (ImageButton) findViewById(R.id.refresh);
                ImageButton ifOnlineImgBtn = (ImageButton) findViewById(R.id.ifOnline);
                ImageButton downloadImgBtn = (ImageButton) findViewById(R.id.download);
                refreshImgBtn.setVisibility(View.VISIBLE);
                ifOnlineImgBtn.setVisibility(View.VISIBLE);
                downloadImgBtn.setVisibility(View.GONE);

                try {
                    int port = 80;
                    InetAddress address;
                    String data = "GET http://api.heclouds.com/devices/" + deviceMap.get(device) + "/datapoints HTTP/1.1\r\n"
                            + "api-key:LO79mBj364HBhD1ZuPNPEDiMMAw=\r\n"
                            + "Host:api.heclouds.com\r\n\r\n";
                    address = InetAddress.getByName("183.230.40.33");
                    mTask = new TCPConnectionTask(address, port, data);
                    showProgress(true);
                    Log.i(TAG, "mTask.execute() called.");
                    mTask.execute((Void) null);
                } catch (Exception e) {
                    Log.e(TAG, "mTask error.");
                }
            }
        };
        taskHandler.postDelayed(taskRunnable, firstCall? 1:300000);
    }

    private void navHistory(){

        // ToolBar visibility
        ImageButton refreshImgBtn = (ImageButton) findViewById(R.id.refresh);
        ImageButton ifOnlineImgBtn = (ImageButton) findViewById(R.id.ifOnline);
        ImageButton downloadImgBtn = (ImageButton) findViewById(R.id.download);
        refreshImgBtn.setVisibility(View.GONE);
        ifOnlineImgBtn.setVisibility(View.GONE);
        downloadImgBtn.setVisibility(View.GONE);

        if(strAccount == null)
            return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ConstraintLayout content = (ConstraintLayout) mContent;
                LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
                inflater.inflate(R.layout.content_main_request_history_parameters, content);
                // 设置按钮
                final Button btnHour = (Button)findViewById(R.id.btn_recent_hour);
                final Button btnDay = (Button)findViewById(R.id.btn_recent_day);
                final Button btnWeek = (Button)findViewById(R.id.btn_recent_week);
                final Button btnCustom = (Button)findViewById(R.id.btn_custom);
                final ImageButton imgBtnStart = (ImageButton) findViewById(R.id.imgBtn_start_time);
                final ImageButton imgBtnEnd = (ImageButton) findViewById(R.id.imgBtn_end_time);
                final EditText textStart = (EditText)findViewById(R.id.text_start_time);
                final EditText textEnd = (EditText)findViewById(R.id.text_end_time);
                final Button btnQuery = (Button)findViewById(R.id.btn_query);
                final List<Button> btnList = new ArrayList<>(4);
                btnList.add(btnHour);btnList.add(btnDay);btnList.add(btnWeek);btnList.add(btnCustom);
                Button.OnClickListener btnListListener = new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(Button button : btnList){
                            // 修改按钮及其字体颜色
                            if (v == button){
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    //Android系统大于等于API16，使用setBackground
                                    button.setBackground(getResources().getDrawable(R.drawable.btn_request_history_selected));
                                } else {
                                    //Android系统小于API16，使用setBackgroundDrawable
                                    button.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_request_history_selected));
                                }
                                button.setTextColor(getResources().getColor(R.color.colorWhite));
                            }
                            else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    //Android系统大于等于API16，使用setBackground
                                    button.setBackground(getResources().getDrawable(R.drawable.btn_request_history));
                                } else {
                                    //Android系统小于API16，使用setBackgroundDrawable
                                    button.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_request_history));
                                }
                                button.setTextColor(getResources().getColor(R.color.colorPrimary));
                            }
                        }
                        long end = System.currentTimeMillis();
                        long start;
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        // 获取时间
                        switch(v.getId()){
                            case R.id.btn_recent_hour:
                                start = end - 3600000;
                                textEnd.setText(formatter.format(new Date(end)));
                                textEnd.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                textStart.setText(formatter.format(new Date(start)));
                                textStart.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                imgBtnStart.setEnabled(false);
                                imgBtnEnd.setEnabled(false);
                                break;
                            case R.id.btn_recent_day:
                                start = end - 86400000;
                                textEnd.setText(formatter.format(new Date(end)));
                                textEnd.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                textStart.setText(formatter.format(new Date(start)));
                                textStart.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                imgBtnStart.setEnabled(false);
                                imgBtnEnd.setEnabled(false);
                                break;
                            case R.id.btn_recent_week:
                                start = end - 604800000;
                                textEnd.setText(formatter.format(new Date(end)));
                                textEnd.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                textStart.setText(formatter.format(new Date(start)));
                                textStart.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                imgBtnStart.setEnabled(false);
                                imgBtnEnd.setEnabled(false);
                                break;
                            default:
                                textEnd.setText(getResources().getString(R.string.end_time));
                                textEnd.setTextColor(getResources().getColor(R.color.colorGray));
                                textStart.setText(getResources().getString(R.string.start_time));
                                textStart.setTextColor(getResources().getColor(R.color.colorGray));
                                imgBtnStart.setEnabled(true);
                                imgBtnEnd.setEnabled(true);
                                break;
                        }
                    }
                };
                for (Button button : btnList)
                    button.setOnClickListener(btnListListener);
                // 获取自定义时间
                ImageButton.OnClickListener imgBtnListener = new ImageButton.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        final EditText editText;
                        if (v.getId() == R.id.imgBtn_start_time)
                            editText = textStart;
                        else
                            editText = textEnd;
                        // 时间选择器
                        TimePickerView timePicker = new TimePickerBuilder(MainActivity.this, new OnTimeSelectListener() {
                            @Override
                            public void onTimeSelect(Date date, View v) {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                editText.setText(formatter.format(date));
                                editText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                            }
                        })
                                .setType(new boolean[]{true,true,true,true,true,false})
                                .setCancelText("取消")
                                .setSubmitText("确认")
                                .isCyclic(true)
                                .setOutSideCancelable(true)
                                .setLabel("年","月","日","时","分","秒")
                                .isCenterLabel(true)
                                .build();
                        timePicker.show();
                    }
                };
                imgBtnStart.setOnClickListener(imgBtnListener);
                imgBtnEnd.setOnClickListener(imgBtnListener);
                btnQuery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            int port = 80;
                            InetAddress address;
                            String data = "GET http://api.heclouds.com/devices/" + deviceMap.get(device) + "/datapoints?start="
                                    + textStart.getText().toString()
                                    + "&end=" + textEnd.getText().toString()
                                    + "&limit=" + "4000"
                                    + " HTTP/1.1\r\n"
                                    + "api-key:LO79mBj364HBhD1ZuPNPEDiMMAw=\r\n"
                                    + "Host:api.heclouds.com\r\n\r\n";
                            address = InetAddress.getByName("183.230.40.33");
                            mTask = new TCPConnectionHistoryTask(address, port, data);
                            showProgress(true);
                            mTask.execute((Void) null);
                        } catch (Exception e){
                            Log.e(TAG, "mTask error.");
                        }
                    }
                });
            }
        });
    }

    private void showDownloadList() {
        if(strAccount == null)
            return;
        mShowDownloadListRunnable = new ShowDownloadListRunnable();
        runOnUiThread(mShowDownloadListRunnable);
    }

    private void showSetup(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ConstraintLayout content = (ConstraintLayout) mContent;
                content.removeAllViews();

                ImageButton refreshImgBtn = (ImageButton) findViewById(R.id.refresh);
                ImageButton ifOnlineImgBtn = (ImageButton) findViewById(R.id.ifOnline);
                ImageButton downloadImgBtn = (ImageButton) findViewById(R.id.download);
                refreshImgBtn.setVisibility(View.GONE);
                ifOnlineImgBtn.setVisibility(View.GONE);
                downloadImgBtn.setVisibility(View.GONE);

                LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
                inflater.inflate(R.layout.setup_main, content);

                Spinner deviceSpinner = (Spinner) findViewById(R.id.choose_device_spinner);
                int count = deviceSpinner.getAdapter().getCount();
                for (int i = 0; i < count; i++){
                    if (device.equals(deviceSpinner.getItemAtPosition(i).toString())){
                        deviceSpinner.setSelection(i, true);
                        break;
                    }
                }
                deviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        device = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }

    private class ShowDownloadListRunnable implements Runnable{

        private View mListItemView;
        private ListView mListView;

        ShowDownloadListRunnable(){
            super();
        }

        private class FileAdapter extends ArrayAdapter{
            private final int resourceId;

            public FileAdapter (Context context, int layoutId, List<DownloadItem> objects){
                super(context, layoutId, objects);
                resourceId = layoutId;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                DownloadItem downloadItem = (DownloadItem) getItem(position);
                View view = LayoutInflater.from(getBaseContext()).inflate(resourceId, parent, false);
                ImageView imageView = view.findViewById(R.id.downloadItemImage);
                TextView textView = view.findViewById(R.id.fileName);
                textView.setText(downloadItem.getFileName());
                imageView.setImageResource(downloadItem.getImageId());
                return view;
            }
        }

        private class DownloadItem{
            private int imageId;
            private String fileName;

            public DownloadItem(int imageId, String fileName){
                this.imageId = imageId;
                this.fileName = fileName;
            }
            public String getFileName(){
                return fileName;
            }
            public int getImageId(){
                return imageId;
            }
        }

        private class MenuItemAction{
            private View view;
            private String local_file;
            private String fileName;

            MenuItemAction(){
                view = mListItemView;
                local_file = Environment.getExternalStorageDirectory().getAbsolutePath()+"/down/";
                fileName = (String) ((TextView)view.findViewById(R.id.fileName)).getText();
            }

            public void openFile(){
                try{
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Intent.ACTION_VIEW);
                    String type = "application/vnd.ms-excel";
                    File file = new File(local_file + "/" + fileName);
                    if(!file.exists())
                        return;
                    Uri uri;
                    if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(MainActivity.this,getPackageName() + ".provider", file);
                        intent.setDataAndType(uri, type);
                        grantUriPermission(MainActivity.this, uri, intent);
                    } else {
                        uri = Uri.fromFile(file);
                        intent.setDataAndType(uri, type);
                    }
                    startActivity(intent);
                    // Intent.createChooser(intent, "选择打开文件的应用：");
                } catch (ActivityNotFoundException e){
                    Toast.makeText(MainActivity.this, "未找到可以打开文件的应用", Toast.LENGTH_SHORT).show();
                }
            }

            public void renameFile(){
                final EditText input = new EditText(MainActivity.this);
                input.setText(fileName);
                input.setSingleLine(true);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("请输入新文件名").setIcon(android.R.drawable.ic_dialog_info).setView(input)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newFileName = input.getText().toString();
                        File file = new File(local_file + "/" + fileName);
                        if (!newFileName.endsWith(".xls") || !file.renameTo(new File(local_file + "/" + newFileName)))
                            Toast.makeText(MainActivity.this, "重命名失败", Toast.LENGTH_SHORT).show();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showFiles(new File(local_file));
                            }
                        });
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        input.post(new Runnable() {
                            @Override
                            public void run() {
                                input.setSelection(0, fileName.lastIndexOf('.'));
                                input.requestFocus();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                            }
                        });
                    }
                });
                alertDialog.show();

            }

            public void deleteFile(){
                File file = new File(local_file + "/" + fileName);
                if (!file.delete())
                    Toast.makeText(MainActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showFiles(new File(local_file));
                    }
                });
            }
        }

        @Override
        public void run(){
            // ToolBar
            ImageButton refreshImgBtn = (ImageButton) findViewById(R.id.refresh);
            ImageButton ifOnlineImgBtn = (ImageButton) findViewById(R.id.ifOnline);
            ImageButton downloadImgBtn = (ImageButton) findViewById(R.id.download);
            refreshImgBtn.setVisibility(View.GONE);
            ifOnlineImgBtn.setVisibility(View.GONE);
            downloadImgBtn.setVisibility(View.GONE);

            final ConstraintLayout content = (ConstraintLayout) mContent;
            content.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
            inflater.inflate(R.layout.content_main_download_list, content);

            String local_file = Environment.getExternalStorageDirectory().getAbsolutePath()+"/down/";
            mListView = (ListView) findViewById(R.id.downloadList);
            File f = new File(local_file);
            if(!f.exists()){
                return;
            }
            showFiles(f);
            // 点击打开
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mListItemView = view;
                    new MenuItemAction().openFile();
                }
            });
            // 长按
            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    mListItemView = view;
                    return false;
                }
            });
            // 长按选择
            mListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.add(0, MENU_OPEN, 0, R.string.context_menu_open);
                    menu.add(0, MENU_RENAME, 1, R.string.context_menu_rename);
                    menu.add(0, MENU_DELETE, 2, R.string.context_menu_delete);
                }
            });
        }

        private void showFiles(File folder){
            File[] files = folder.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(".xls");
                }
            });
            final ArrayList<DownloadItem> downloadItems = new ArrayList<>();
            for (File file : files){
                downloadItems.add(new DownloadItem(R.drawable.ic_insert_drive_file_black_48dp, file.getName()));
            }
            FileAdapter fileAdapter = new FileAdapter(MainActivity.this, R.layout.content_main_download_item, downloadItems);
            mListView.setAdapter(fileAdapter);
        }

        private void grantUriPermission(Context context, Uri fileUri, Intent intent) {
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
    }

    @Override
    public void onClick(View v){

    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        ShowDownloadListRunnable.MenuItemAction menuItemAction = mShowDownloadListRunnable.new MenuItemAction();
        switch (item.getItemId()){
            case MENU_OPEN:
                menuItemAction.openFile();
                break;
            case MENU_RENAME:
                menuItemAction.renameFile();
                break;
            case MENU_DELETE:
                menuItemAction.deleteFile();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        mMenuItem = (MenuItem) findViewById(id);
        if (id == R.id.nav_realTime) {
            setTitle(getResources().getString(R.string.title_activity_main));
            ((ViewGroup) findViewById(R.id.content)).removeAllViews();
            taskHandler.removeCallbacks(taskRunnable);
            afterLogin(true);
        } else if (id == R.id.nav_history) {
            setTitle(getResources().getString(R.string.title_activity_main_history));
            ((ViewGroup) findViewById(R.id.content)).removeAllViews();
            taskHandler.removeCallbacks(taskRunnable);
            navHistory();
        } else if (id == R.id.nav_downloadList) {
            setTitle(getResources().getString(R.string.title_activity_main_download_list));
            taskHandler.removeCallbacks((taskRunnable));
            showDownloadList();
        } else if (id == R.id.nav_setup) {
            setTitle(getResources().getString(R.string.title_activity_main_setup));
            taskHandler.removeCallbacks((taskRunnable));
            showSetup();
        } else if (id == R.id.nav_logout) {
            SharedPreferences sp = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("AUTO_LOGIN", false);
            editor.apply();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(getApplication().getPackageName());
                    LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(LaunchIntent);
                }
            }, 100);

        } else if (id == R.id.nav_exit) {
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgress.setVisibility(show ? View.GONE : View.VISIBLE);
            mContent.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mContent.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            mContent.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void Login(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_LOGIN:
                if(resultCode != Activity.RESULT_OK) {
                    finish();
                    return;
                }
                Bundle bundle = data.getExtras();
                try {
                    strAccount = bundle.getStringArray("account");
                } catch (NullPointerException e){
                    //TODO
                }
                Log.i(TAG, "afterLogin() called.");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        afterLogin(true);
                    }
                }).start();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ((TCPConnectionHistoryTask) mTask).download();
                }
                else{
                    Toast.makeText(MainActivity.this,"权限申请失败", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public class TCPConnectionTask extends AsyncTask<Void, Void, Boolean> {
        private Socket socket = null;
        private final InetAddress socketAddress;
        private final int socketPort;
        private final String socketData;
        JSONData jsonData;
        HashMap<String, List<Object>> idMap_;
        TCPConnectionTask(InetAddress address, int port, String data ){
            socketAddress = address;
            socketPort = port;
            socketData = data;
            idMap_ = new HashMap<>();
            idMap_.put("temp", new ArrayList<Object>(){{add("温度");add("℃");add(0);add(100);}});
            idMap_.put("humi", new ArrayList<Object>(){{add("湿度");add("%");add(0);add(100);}});
            idMap_.put("light", new ArrayList<Object>(){{add("光照强度");add("lx");add(0);add(10000);}});
            idMap_.put("CO2", new ArrayList<Object>(){{add("二氧化碳浓度");add("ppm");add(0);add(1000);}});
        }
        private void send() throws IOException{
            //try {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(socketData.getBytes());
                outputStream.flush();
                Log.i(TAG, "outputStream flushed.\n"
                        +"address:"+socketAddress.toString()+"\n"
                        +"port:"+String.valueOf(socketPort)+"\n"
                        +"data:"+socketData);
            //} catch (IOException e){
            //    throw e;
            //}
        }
        private String receive() throws IOException{
            //try{
                InputStream inputStream = socket.getInputStream();
//                BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
//                StringBuilder response = new StringBuilder();
//                String line;
//                while((line = input.readLine()) != null){
//                    response.append(line);
//                    response.append("\r\n");
//                }
//                input.close();
                byte buffer[] = new byte[1000000];
                StringBuilder sb = new StringBuilder();
                int count;
                while ((count = inputStream.read(buffer)) != -1) {
                    String part = new String(buffer, 0, count);
                    sb.append(part);
                    Log.d(TAG, "stringBuilder = " + sb.toString());
                    if (part.contains("\"succ\"}"))
                        break;
                }
                String response = sb.toString();
//                byte buffer[] = new byte[1000000];
//                String response = null;
//                int readBytes = 0;
//                int len = buffer.length;
//                while(readBytes < len){
//                    int read = inputStream.read(buffer, readBytes, len - readBytes);
//                    if (read == -1)
//                        break;
//                    readBytes += read;
//                    response = new String(buffer, 0, readBytes);
//                    // TODO: To solve the problem of "limit" in request "GET".
//                    String lines[] = response.split("\r\n");
//                    String contentLength = lines[3];
//                    String content = lines[lines.length - 1];
//                    if (Integer.valueOf(contentLength.split(":")[1].trim()) == content.length())
//                        break;
//                }
//                //String response = new String(buffer, 0, readBytes);
                //if (response != null)
                    Log.d(TAG, "response's length: " + response.length());
                return response;
            //} catch (IOException e){
            //    throw e;
            //}
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                socket = new Socket(socketAddress, socketPort);
                Log.i(TAG, "socket created.");
                send();
                Log.i(TAG, "socket data sent.");
                //Thread.sleep(2000);
                String response = receive();
                Log.i(TAG, "response got");
                jsonData = new JSONData(response);
                Log.i(TAG, "jsonData created");
                return jsonData.msg.equals("succ");
            } catch (IOException e){
                Log.e(TAG, "doInBackground IOException.");
                return false;
            }
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);
            ifOnline = success;
            if (success){

                int count = jsonData.data.count;
                ConstraintLayout content = (ConstraintLayout) mContent;
                content.removeAllViews();
                ScrollView scrollView = new ScrollView(MainActivity.this);
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                LinearLayout container = new LinearLayout(MainActivity.this);
                container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                container.setOrientation(LinearLayout.VERTICAL);
                scrollView.addView(container);
                content.addView(scrollView);
                //LinearLayout container = (LinearLayout) findViewById(R.id.content_layout);

                for(int i = 0; i < count; i++) {
                    LinearLayout llRow_1 = new LinearLayout(MainActivity.this);
                    {
                        llRow_1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        llRow_1.setGravity(Gravity.CENTER_HORIZONTAL);
                        if (i % 2 == 0)
                            llRow_1.setBackgroundColor(getResources().getColor(R.color.colorGray));
                        LinearLayout llRow = new LinearLayout(MainActivity.this);
                        {
                            llRow.setOrientation(LinearLayout.HORIZONTAL);
                            llRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                            llRow.setPadding(20, 30, 20, 30);
                            //String[] idMapValue = idMap.get(jsonData.data.datastreams.get(i).id);
                            List<Object> idMap_Value = idMap_.get(jsonData.data.datastreams.get(i).id);
                            LinearLayout llText = new LinearLayout(MainActivity.this);
                            {
                                llText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                llText.setOrientation(LinearLayout.VERTICAL);
                                llText.setPadding(0, 0, 20, 0);
                                llText.setGravity(Gravity.CENTER_VERTICAL);
                                TextView tvUpdateTime = new TextView(MainActivity.this);
                                {
                                    tvUpdateTime.setText(getResources().getString(R.string.content_update_time));
                                    tvUpdateTime.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                    tvUpdateTime.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                    tvUpdateTime.setGravity(Gravity.CENTER);
                                }
                                TextView tvUpdateTimeValue = new TextView(MainActivity.this);
                                {
                                    tvUpdateTimeValue.setText(jsonData.data.datastreams.get(i).datapoints.get(0).at.split("\\.")[0]);
                                    tvUpdateTimeValue.setGravity((Gravity.CENTER));
                                }
                                TextView tvParameter = new TextView(MainActivity.this);
                                {
                                    tvParameter.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                    tvParameter.setGravity(Gravity.CENTER);
                                    tvParameter.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                    if (idMap_Value != null)
                                        tvParameter.setText(String.valueOf(idMap_Value.get(0)));
                                    else
                                        tvParameter.setText(getResources().getString(R.string.content_parameter));
                                }
                                TextView tvParameterValue = new TextView(MainActivity.this);
                                {
                                    tvParameterValue.setGravity(Gravity.CENTER);
                                    if (idMap_Value != null)
                                        tvParameterValue.setText(String.valueOf(jsonData.data.datastreams.get(i).datapoints.get(0).value) + String.valueOf(idMap_Value.get(1)));
                                    else
                                        tvParameterValue.setText(getResources().getString(R.string.content_null));
                                }
                                llText.addView(tvUpdateTime);
                                llText.addView(tvUpdateTimeValue);
                                llText.addView(tvParameter);
                                llText.addView(tvParameterValue);
                            }
                            DashboardView dashboardView = new DashboardView(MainActivity.this);
                            {
                                dashboardView.setTextColor(getResources().getColor(R.color.colorPrimary));
                                dashboardView.setArcColor(getResources().getColor(R.color.colorPrimaryDark));
                                dashboardView.setPointerColor(getResources().getColor(R.color.colorPrimaryDark));
                                dashboardView.setBigSliceCount(10);
                                dashboardView.setHeaderRadius(20);
                                dashboardView.setMeasureTextSize(12);
                                dashboardView.setRadius(100);
                                dashboardView.setStartAngle(135);
                                dashboardView.setStripeMode(DashboardView.StripeMode.INNER);
                                dashboardView.setStripeWidth(16);
                                dashboardView.setSweepAngle(270);
                                if (idMap_Value != null) {
                                    dashboardView.setHeaderTitle(String.valueOf(idMap_Value.get(1)));
                                    dashboardView.setMinValue(Integer.valueOf(String.valueOf(idMap_Value.get(2))));
                                    dashboardView.setMaxValue(Integer.valueOf(String.valueOf(idMap_Value.get(3))));
                                }
                                dashboardView.setRealTimeValue(jsonData.data.datastreams.get(i).datapoints.get(0).value);
                            }
                            llRow.addView(llText);
                            llRow.addView(dashboardView);
                        }
                        llRow_1.addView(llRow);
                    }
                    container.addView(llRow_1);
                }



                /*
                LayoutInflater inflater = getLayoutInflater();
                LinearLayout[] element = new LinearLayout[count];
                {
                    for (int i = 0; i < count; i++) {
                        Log.i(TAG, "Initializing element[" + i + "]...");
                        element[i] = (LinearLayout) inflater.inflate(R.layout.content_element_main, container, true);
                        Log.i(TAG, "element[" + i + "] got.");

                        TextView content_update_time_value = (TextView) element[i].findViewById(R.id.content_update_time_value);
                        TextView content_parameter = (TextView) element[i].findViewById(R.id.content_parameter);
                        TextView content_parameter_value = (TextView) element[i].findViewById(R.id.content_parameter_value);
                        DashboardView dashboardView = (DashboardView) element[i].findViewById(R.id.dashboardView);
                        Log.i(TAG, "views found.");
                        String[] idMapValue = idMap.get(jsonData.data.datastreams.get(i).id);
                        content_update_time_value.setText(jsonData.data.datastreams.get(i).datapoints.get(0).at);
                        dashboardView.setRealTimeValue(jsonData.data.datastreams.get(i).datapoints.get(0).value);

                        if (idMapValue!=null) {
                            content_parameter.setText(idMapValue[0]);
                            content_parameter_value.setText(String.valueOf(jsonData.data.datastreams.get(i).datapoints.get(0).value) + idMapValue[1]);
                            dashboardView.setHeaderTitle(idMapValue[1]);
                        }
                        else{
                            content_parameter.setText(jsonData.data.datastreams.get(i).id);
                            content_parameter_value.setText(String.valueOf(jsonData.data.datastreams.get(i).datapoints.get(0).value));
                            dashboardView.setHeaderTitle(jsonData.data.datastreams.get(i).id);
                        }
                    }
                }*/

                mTask = null;

                afterLogin(false);
            }
        }
        @Override
        protected void onCancelled() {
            mTask = null;
            showProgress(false);
        }
    }

    private class JSONData{
        private int code;
        private Data data;
        private String msg;

        JSONData(String JSONdata){
            Log.d(TAG, JSONdata);
            JSONdata = debug(JSONdata);
            Log.d(TAG, "String after debug: " + JSONdata);
            // code
            Matcher matcherCode = Pattern.compile("\"errno\":\\D*(.*?)\\D").matcher(JSONdata);
            if (matcherCode.find())
                code = Integer.valueOf(matcherCode.group(1));
            Log.i(TAG, "code generated:"+String.valueOf(code));

            // data
            Matcher matcherData = Pattern.compile("\"data\":\\s*(\\{[\\s\\S]*)").matcher(JSONdata);
            if (matcherData.find())
                data = new Data(normalize(matcherData.group(1)));
            Log.i(TAG, "data generated:"+"...");
            // msg
            Matcher matcherMsg = Pattern.compile("\"error\":\\s*\"(.*?)\"").matcher(JSONdata);
            if (matcherMsg.find())
                msg = matcherMsg.group(1);
            Log.i(TAG, "msg generated:"+msg);

        }

        private String debug(String s){
            StringBuilder debugString = new StringBuilder();
            String[] strings = s.split("\r\n");
            int startRow = 0;
            for (int i = 0; i < strings.length; i++){
                if (startRow == 0){
                    if (!strings[i].equals("") && strings[i].charAt(0) == '{')
                        startRow = i;
                }
                else
                    break;
            }
            for (int i = startRow; i < strings.length; i+=2){
                debugString.append(strings[i]);
            }
            return debugString.toString();
        }

        private String normalize(String s){
            int count = 0;
            char[] charArray = s.toCharArray();
            char first = charArray[0];
            String normalizedString = null;
            Log.d(TAG, "String to be normalized: " + s);
            if (first != '{' && first != '[') {
                Log.w(TAG, "normalize() returns null.");
                return null;
            }
            for (int i = 0; i < s.length(); i++){
                char c = charArray[i];
                if (first == c) {
                    count++;
                    //Log.d(TAG, "count++: " + count);
                }
                else if (first == c - 2) {
                    count--;
                    //Log.d(TAG, "count-- :" + count);
                }
                if (count == 0) {
                    normalizedString = s.substring(0, i + 1);
                    break;
                }
            }
            Log.d(TAG, "normalizedString: "+normalizedString);
            return normalizedString;
        }

        private class Data{
            private int count;
            private List<DataStreams> datastreams = new ArrayList<>();

            Data(String data){
                // count
                Log.d(TAG, data);
                Matcher matcherCount = Pattern.compile("\"count\":\\D*(.*?)\\D").matcher(data);
                if (matcherCount.find()){
                    Log.d(TAG, matcherCount.group(1));
                    count = Integer.valueOf(matcherCount.group(1));
                }
                Log.i(TAG, "count generated:"+String.valueOf(count));

                // datastreams
                List<String> stringList;
                Matcher matcherDatastreams = Pattern.compile("\"datastreams\":\\s*(\\[[\\s\\S]*)").matcher(data);
                if (matcherDatastreams.find()) {
                    stringList = getListOfStrings(normalize(matcherDatastreams.group(1)));
                    if (stringList != null)
                        for (String item : stringList)
                            datastreams.add(new DataStreams(item));
                }
                Log.i(TAG, "datastreams generated:"+"...");

                Collections.sort(datastreams, new Comparator<DataStreams>() {
                    @Override
                    public int compare(DataStreams o1, DataStreams o2) {
                        Map<String, Integer> idMap = new HashMap<>();
                        idMap.put("temp",1);
                        idMap.put("humi", 2);
                        idMap.put("light", 3);
                        idMap.put("CO2", 4);
                        if (idMap.containsKey(o1.id) && idMap.containsKey(o2.id)) {
                            return idMap.get(o1.id) > idMap.get(o2.id) ? 1 : -1;
                        }
                        else
                            return 0;
                    }
                });
            }

            private List<String> getListOfStrings(String s) {
                if (s == null)
                    return null;
                if (s.charAt(0) != '[')
                    return null;
                List<String> stringList = new ArrayList<>();
                int count = 0;
                int beginIndex = 0;
                int endIndex;
                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    if (c == '{') {
                        beginIndex = (beginIndex == 0) ? i : beginIndex;
                        count++;
                    }
                    else if (c == '}') {
                        count--;
                    }
                    if (count == 0 && beginIndex != 0) {
                        endIndex = i + 1;
                        stringList.add(s.substring(beginIndex, endIndex));
                        beginIndex = 0;
                    }
                }
                return stringList;
            }

            private class DataStreams{
                private List<DataPoints> datapoints = new ArrayList<>();
                private String id;

                DataStreams(String datastreams) {
                    // datapoints
                    List<String> stringList;
                    Matcher matcherDataPoints = Pattern.compile("\"datapoints\":\\s*(\\[[\\s\\S]*)").matcher(datastreams);
                    if (matcherDataPoints.find()) {
                        stringList = getListOfStrings(normalize(matcherDataPoints.group(1)));
                        if (stringList!=null)
                            for (String item : stringList)
                                datapoints.add(new DataPoints(item));
                    }
                    Log.i(TAG, "datapoints generated:"+"...");

                    // id
                    Matcher matcherId = Pattern.compile("\"id\":\\s*\"(.*?)\"").matcher(datastreams);
                    if (matcherId.find())
                        id = matcherId.group(1);
                    Log.i(TAG, "id generated:"+id);
                }

                private class DataPoints{
                    private String at;
                    private float value;

                    DataPoints(String datapoints) {
                        Log.d(TAG, "datapoint: " + datapoints);
                        // at
                        Matcher matcherAt = Pattern.compile("\"at\":\\s*\"(.*?)\"").matcher(datapoints);
                        if (matcherAt.find())
                            at = matcherAt.group(1);
                        Log.i(TAG, "at generated:"+at);

                        // value
                        Matcher matcherValue = Pattern.compile("\"value\":\\s*(-?\\d*\\.?\\d*)").matcher(datapoints);
                        if(matcherValue.find())
                            value = Float.valueOf(matcherValue.group(1));
                        Log.i(TAG, "value generated:"+String.valueOf(value));
                    }
                }
            }
        }
    }

    public class TCPConnectionHistoryTask extends TCPConnectionTask{
        TCPConnectionHistoryTask(InetAddress address, int port, String data){
            super(address, port, data);
        }
        @Override
        protected void onPostExecute(final Boolean success){
            showProgress(false);
            if (success) {
                // ToolBar
                ImageButton downloadImgBtn = (ImageButton) findViewById(R.id.download);
                downloadImgBtn.setVisibility(View.VISIBLE);
                downloadImgBtn.setOnClickListener(new downloadListener());

                int count = jsonData.data.datastreams.size();
                ConstraintLayout content = (ConstraintLayout) mContent;
                content.removeAllViews();
                if (count == 0){
                    TextView textViewNoData = new TextView(MainActivity.this);
                    textViewNoData.setText("暂无数据");
                    textViewNoData.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    textViewNoData.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    RelativeLayout relativeLayoutNoData = new RelativeLayout(MainActivity.this);
                    RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
                    relativeLayoutNoData.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                    relativeLayoutNoData.addView(textViewNoData, rlp);
                    content.addView(relativeLayoutNoData);
                    mTask = null;
                    return;
                }
                ScrollView scrollView = new ScrollView(MainActivity.this);
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                LinearLayout container = new LinearLayout(MainActivity.this);
                container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                container.setOrientation(LinearLayout.VERTICAL);
                container.setGravity(Gravity.CENTER);
                scrollView.addView(container);
                content.addView(scrollView);
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final SimpleDateFormat sdfXAxis = new SimpleDateFormat("MM-dd HH:mm");
                // MPAndroid chart
                LineChart chart;
                List<Entry> entries;
                LineDataSet dataSet;
                LineData lineData;
                for(int i = 0; i < count; i++){
                    chart = new LineChart(MainActivity.this);
                    entries = new ArrayList<>();
                    final long firstDate;
                    try {
                        firstDate = sdf.parse(jsonData.data.datastreams.get(i).datapoints.get(0).at.split("\\.")[0]).getTime();
                        for (JSONData.Data.DataStreams.DataPoints dataPoint : jsonData.data.datastreams.get(i).datapoints) {
                            entries.add(new Entry(sdf.parse(dataPoint.at.split("\\.")[0]).getTime() - firstDate, dataPoint.value));
                            Log.d(TAG, "Entry X: " + (sdf.parse(dataPoint.at.split("\\.")[0]).getTime() - firstDate));
                        }

                        // set data set
                        dataSet = new LineDataSet(entries, idMap_.get(jsonData.data.datastreams.get(i).id).get(0).toString());
                        dataSet.setColor(getResources().getColor(R.color.colorPrimary));
                        dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimary));
                        dataSet.setDrawFilled(true);
                        dataSet.setFillDrawable(getResources().getDrawable(R.drawable.linechart_fade_blue));
                        dataSet.setLineWidth(2);
                        dataSet.setDrawValues(false);
                        dataSet.setDrawCircles(false);

                        // setChart
                        lineData = new LineData(dataSet);
                        chart.setData(lineData);
                        chart.setMinimumHeight(500);
                        chart.setDescription(null);
                        chart.setPadding(20, 30, 20, 30);
                        //if (i % 2 == 0) chart.setBackgroundColor(getResources().getColor(R.color.colorGray));
                        chart.setDrawBorders(false);
                        chart.setDrawGridBackground(false);


                        // set X axis
                        XAxis xAxis = chart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setAxisMinimum(0);
                        Log.d(TAG, "XAxisMinimum: 0");
                        xAxis.setAxisMaximum(entries.get(entries.size() - 1).getX());
                        Log.d(TAG, "XAxisMaximum:" + entries.get(entries.size() - 1).getX());
                        xAxis.setDrawGridLines(false);
                        xAxis.setValueFormatter(new IAxisValueFormatter(){
                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
                                String stringXAxis = sdfXAxis.format(new Date((long)value + firstDate));
                                Log.v(TAG, "String of XAxis: " + stringXAxis);
                                return stringXAxis;
                            }
                        });
                       // xAxis.setTextSize(20);

                        // set Y axis
                        YAxis yAxisLeft = chart.getAxisLeft();
                        YAxis yAxisRight = chart.getAxisRight();
                        yAxisLeft.setDrawGridLines(true);
                        yAxisRight.setDrawGridLines(false);
                        yAxisLeft.enableGridDashedLine(10f, 10f, 0f);
                        yAxisRight.setEnabled(false);

                        // set legend
                        Legend legend = chart.getLegend();
                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                        legend.setTextSize(15);


                        chart.invalidate();
                        container.addView(chart);

                        mTask = null;
                    } catch (ParseException e) {
                        Log.e(TAG, "Parse error.");
                    }
                }
            }
        }

        private void download(){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            long currentTime = System.currentTimeMillis();
            Date date = new Date(currentTime);
            String fileName = "历史数据" + sdf.format(date) + ".xls";
            try {
                String local_file = Environment.getExternalStorageDirectory().getAbsolutePath()+"/down/";
                File f = new File(local_file);
                if(!f.exists()){
                    if (!f.mkdirs())
                        return;
                }
                File file = new File(f.getAbsolutePath() + "/" + fileName);
                if (!file.exists()) {
                    if (!file.createNewFile())
                        return;
                }
                WritableWorkbook workbook = Workbook.createWorkbook(file);
                WritableSheet sheet = workbook.createSheet("sheet1", 0);
                WritableFont wf = new WritableFont(WritableFont.ARIAL, 11,
                        WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
                        jxl.format.Colour.BLACK);
                WritableCellFormat wcf = new WritableCellFormat(wf);
                wcf.setBackground(jxl.format.Colour.WHITE);
                wcf.setAlignment(Alignment.CENTRE);
                wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
                wcf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK);
                sheet.addCell(new Label(0, 0, "数据流", wcf));
                sheet.addCell(new Label(1, 0, "时间", wcf));
                sheet.addCell(new Label(2, 0, "值", wcf));
                int count = 1;
                for (int i = 0; i < jsonData.data.datastreams.size(); i++) {
                    int from = count;
                    List<Object> list = idMap_.get(jsonData.data.datastreams.get(i).id);
                    sheet.addCell(new Label(0, count, ((String)list.get(0)) + "/" + ((String)list.get(1)), wcf));
                    for (int j = 0; j < jsonData.data.datastreams.get(i).datapoints.size(); j++) {
                        sheet.addCell(new Label(1, count, jsonData.data.datastreams.get(i).datapoints.get(j).at, wcf));
                        sheet.addCell(new Label(2, count, String.valueOf(jsonData.data.datastreams.get(i).datapoints.get(j).value), wcf));
                        count++;
                    }
                    sheet.mergeCells(0, from, 0, count - 1);
                }
                workbook.write();
                workbook.close();
                Toast.makeText(MainActivity.this, "文件 " + fileName + " 下载成功。", Toast.LENGTH_SHORT).show();
            }catch(Exception e){
                Toast.makeText(MainActivity.this, "文件下载失败。", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Saving excel error.");
            }
        }

        public class downloadListener implements View.OnClickListener{
            @Override
            public void onClick(View v){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                    else {
                        download();
                    }
                }
                else {
                    download();
                }
            }
        }
    }
}
