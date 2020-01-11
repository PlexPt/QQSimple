package me.zpp0196.qqpurify.demo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import reflectx.Reflectx;
import reflectx.ReflectxException;

public class ReflectxInitializer {
    public static void init(Context context) {
        Reflectx.setProxyClassLoader(context.getClassLoader());
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            long versionCode;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                versionCode = pi.getLongVersionCode();
            } else {
                versionCode = pi.versionCode;
            }
            Reflectx.setProguardVersion(versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            throw new ReflectxException(e);
        }
    }
}
