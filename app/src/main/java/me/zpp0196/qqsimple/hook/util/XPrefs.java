package me.zpp0196.qqsimple.hook.util;

import java.lang.ref.WeakReference;

import de.robv.android.xposed.XSharedPreferences;
import me.zpp0196.qqsimple.BuildConfig;

/**
 * Created by zpp0196 on 2018/4/11.
 */

public class XPrefs {

    private static WeakReference<XSharedPreferences> xSharedPreferences = new WeakReference<>(null);

    public static XSharedPreferences getPref() {
        XSharedPreferences preferences = xSharedPreferences.get();
        if (preferences == null) {
            preferences = new XSharedPreferences(BuildConfig.APPLICATION_ID);
            preferences.makeWorldReadable();
            xSharedPreferences = new WeakReference<>(preferences);
        } else {
            preferences.reload();
        }
        return preferences;
    }

    public static boolean isPrintLog() {
        return getPref().getBoolean("switch_log", false);
    }
}
