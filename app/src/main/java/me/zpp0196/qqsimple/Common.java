package me.zpp0196.qqsimple;

import android.content.Context;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by zpp0196 on 2018/3/11.
 */

public class Common {

    public static final String PACKAGE_NAME_QQ = "com.tencent.mobileqq";

    public static boolean isModuleActive() {
        return false;
    }

    public static String getQQVersion(Context context) {
        if (context != null) {
            try {
                return context.getPackageManager().getPackageInfo(PACKAGE_NAME_QQ, 0).versionName;
            } catch (Exception e) {
                XposedBridge.log(e);
            }
        }
        return "0.0";
    }
}
