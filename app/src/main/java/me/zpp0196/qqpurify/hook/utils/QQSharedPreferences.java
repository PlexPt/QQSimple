package me.zpp0196.qqpurify.hook.utils;

import android.content.Context;
import android.content.SharedPreferences;

import me.zpp0196.qqpurify.utils.Utils;

/**
 * Created by zpp0196 on 2019/5/19.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class QQSharedPreferences {

    public static long getLong(Context context, String key) {
        SharedPreferences sp = Utils.getSp(context);
        return sp.getLong(key, -1);
    }

    public static boolean getBool(Context context, String key) {
        SharedPreferences sp = Utils.getSp(context);
        return sp.getBoolean(key, false);
    }

    public static void putLong(Context context, String key, long value) {
        SharedPreferences sp = Utils.getSp(context);
        sp.edit().putLong(key, value).apply();
    }

    public static void putBool(Context context, String key, boolean value) {
        SharedPreferences sp = Utils.getSp(context);
        sp.edit().putBoolean(key, value).apply();
    }
}
