package me.zpp0196.qqsimple;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by zpp0196 on 2018/3/11.
 */

public class Common {

    public static final String PACKAGE_NAME_QQ = "com.tencent.mobileqq";
    public static final String PACKAGE_NAME_VXP = "io.va.exposed";
    public static ClassLoader qqClassLoader;

    public static boolean isModuleActive() {
        return false;
    }

    public static String getQQVersion(Context context) {
        if (context != null) {
            try {
                return context.getPackageManager().getPackageInfo(PACKAGE_NAME_QQ, 0).versionName;
            } catch (Throwable e) {
                Log.e("Common", "Can't get the QQ version!");
            }
        }
        return "0.0";
    }

    public static boolean isInstalled(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return false;
            }
            packageManager.getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
