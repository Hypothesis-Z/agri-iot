package com.zzq.app.util;

import android.widget.Toast;

import com.zzq.app.app.App;

public class ToastUtil {
    private static Toast toast;
    public static void showToast(String text) {
        if(toast == null){
            toast = Toast.makeText(App.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }
}
