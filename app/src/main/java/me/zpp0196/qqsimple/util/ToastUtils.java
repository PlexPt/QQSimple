package me.zpp0196.qqsimple.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by zpp0196 on 2018/3/13.
 */

public class ToastUtils {

    private static ToastUtils toastUtils;
    private Toast toast;

    @SuppressLint("ShowToast")
    private ToastUtils(Context context) {
        toast = Toast.makeText(context.getApplicationContext(), null, Toast.LENGTH_SHORT);
    }

    public static synchronized ToastUtils getInstance(Context context) {
        if (toastUtils == null) {
            toastUtils = new ToastUtils(context);
        }
        return toastUtils;
    }

    public void showToast(int toastMsg) {
        toast.setText(toastMsg);
        toast.show();
    }

    public void showToast(String toastMsg) {
        toast.setText(toastMsg);
        toast.show();
    }

    public void destory() {
        if (null != toast) {
            toast.cancel();
            toast = null;
        }
        toastUtils = null;
    }
}
