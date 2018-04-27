package me.zpp0196.qqsimple.hook.util;

import android.content.Context;
import android.util.Log;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static me.zpp0196.qqsimple.Common.PACKAGE_NAME_QQ;

/**
 * Created by zpp0196 on 2018/4/27 0027.
 */

public class Util {

    @SuppressWarnings("all")
    public static String getQQVersion() {
        Context context = (Context) XposedHelpers.callMethod(XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread", new Object[0]), "getSystemContext", new Object[0]);
        if (context != null) {
            try {
                return context.getPackageManager().getPackageInfo(PACKAGE_NAME_QQ, 0).versionName;
            } catch (Throwable e) {
                Log.e("Common", "Can't get the QQ version!");
            }
        }
        return "";
    }

    public static boolean isMoreThan732() {
        return getQQVersion().compareTo("7.3.2") >= 0;
    }

    public static boolean isMoreThan735() {
        return getQQVersion().compareTo("7.3.5") >= 0;
    }

    public static void log(Throwable throwable) {
        if (!XPrefs.isPrintLog()) XposedBridge.log(throwable);
    }

    public static void log(Class clazz, String msg) {
        if (XPrefs.isPrintLog()) log(clazz.getSimpleName(), msg);
    }

    public static void log(String TAG, String msg){
        if (XPrefs.isPrintLog()) XposedBridge.log(String.format("%s -> %s", TAG, msg));
    }

    @SuppressWarnings("all")
    public static void log(String TAG, String format, Object... args) {
        log(TAG,  String.format(format, args));
    }

    public static void log(Class clazz, String format, Object... args) {
        log(clazz, String.format(format, args));
    }
}
