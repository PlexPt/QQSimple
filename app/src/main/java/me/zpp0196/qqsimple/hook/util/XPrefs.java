package me.zpp0196.qqsimple.hook.util;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import de.robv.android.xposed.XSharedPreferences;
import me.zpp0196.qqsimple.BuildConfig;

import static me.zpp0196.qqsimple.Common.PREFS_KEY_SWITCH_ALL;
import static me.zpp0196.qqsimple.Common.PREFS_KEY_SWITCH_LOG;

/**
 * Created by zpp0196 on 2018/4/11.
 */

public class XPrefs {

    private static WeakReference<XSharedPreferences> xSharedPreferences = new WeakReference<>(null);
    private static HashMap<String, Boolean> map = new HashMap<>();


    public static XSharedPreferences getPref() {
        XSharedPreferences preferences = xSharedPreferences.get();
        if (preferences == null) {
            preferences = new XSharedPreferences(BuildConfig.APPLICATION_ID);
            preferences.makeWorldReadable();
            preferences.reload();
            xSharedPreferences = new WeakReference<>(preferences);
        } else {
            preferences.reload();
        }
        return preferences;
    }

    public static boolean getBoolean(String key) {
        Boolean value = map.get(key);
        if (value != null) {
            return value;
        }
        value = getPref().getBoolean(key, false);
        map.put(key, value);
        return value;
    }

    static boolean isPrintLog() {
        return getBoolean(PREFS_KEY_SWITCH_LOG);
    }

    public static boolean isEnableModule() {
        return getPref().getBoolean(PREFS_KEY_SWITCH_ALL, true);
    }
}
