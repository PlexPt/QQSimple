package me.zpp0196.qqsimple.hook.util;

import android.content.Context;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import me.zpp0196.qqsimple.util.CommUtil;

/**
 * Created by zpp0196 on 2018/4/27 0027.
 */

public class HookUtil {

    @SuppressWarnings ("all")
    public static Context getSystemContext() {
        return (Context) XposedHelpers.callMethod(XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread", new Object[0]), "getSystemContext", new Object[0]);
    }

    public static String getQQVersionName() {
        return CommUtil.getQQVersionName(getSystemContext());
    }

    public static int getQQVersionCode() {
        return CommUtil.getQQVersionCode(getSystemContext());
    }

    public static boolean isMoreThan732() {
        return getQQVersionName().compareTo("7.3.2") >= 0;
    }

    public static boolean isMoreThan735() {
        return getQQVersionName().compareTo("7.3.5") >= 0;
    }

    public static boolean isMoreThan758() {
        return getQQVersionName().compareTo("7.5.8") >= 0;
    }

    public static boolean isMoreThan760() {
        return getQQVersionName().compareTo("7.6.0") >= 0;
    }

    public static void log(Throwable throwable) {
        if (!XPrefs.isPrintLog()) {
            XposedBridge.log(throwable);
        }
    }

    public static void log(Class clazz, String msg) {
        if (XPrefs.isPrintLog()) {
            log(clazz.getSimpleName(), msg);
        }
    }

    public static void log(String TAG, String msg) {
        if (XPrefs.isPrintLog()) {
            XposedBridge.log(String.format("%s -> %s", TAG, msg));
        }
    }

    @SuppressWarnings ("all")
    public static void log(String TAG, String format, Object... args) {
        log(TAG, String.format(format, args));
    }

    public static void log(Class clazz, String format, Object... args) {
        log(clazz, String.format(format, args));
    }

    public static boolean isCallingFrom(String className) {
        StackTraceElement[] stackTraceElements = Thread.currentThread()
                .getStackTrace();
        for (StackTraceElement element : stackTraceElements) {
            if (element.getClassName()
                    .contains(className)) {
                return true;
            }
        }
        return false;
    }
}
