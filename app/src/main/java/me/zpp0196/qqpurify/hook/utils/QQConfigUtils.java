package me.zpp0196.qqpurify.hook.utils;

import android.content.Context;
import android.text.TextUtils;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.StandardCharsets;

import me.zpp0196.library.xposed.XLog;
import me.zpp0196.qqpurify.utils.Constants;
import me.zpp0196.qqpurify.utils.Utils;

/**
 * Created by zpp0196 on 2019/6/17.
 */
public class QQConfigUtils implements Constants {

    private static File mConfigFile;
    private static JSONObject mClasses;
    private static JSONObject mFields;
    private static JSONObject mMethods;

    public static void init(Context context, long versionCode) {
        try {
            String fileName = versionCode + ".cfg";
            if (mConfigFile == null) {
                mConfigFile = new File(Utils.getLocalPath(), fileName);
            }
            String res;
            if (!mConfigFile.exists()) {
                res = Utils.getTextFromAssets(Utils.createContext(context), fileName);
            } else {
                res = FileUtils.readFileToString(mConfigFile, StandardCharsets.UTF_8).trim();
            }
            if (TextUtils.isEmpty(res)) {
                throw new Exception();
            }
            JSONObject config = new JSONObject(res);
            if (config.has("classes")) {
                mClasses = config.getJSONObject("classes");
            }
            if (config.has("fields")) {
                mFields = config.getJSONObject("fields");
            }
            if (config.has("methods")) {
                mMethods = config.getJSONObject("methods");
            }
        } catch (Exception e) {
            XLog.wtf(e);
        }
    }

    public static String findClass(String className) {
        try {
            String simpleName = className.substring(className.lastIndexOf('.') + 1);
            return mClasses.getString(simpleName);
        } catch (Exception e) {
            return className;
        }
    }

    public static String getField(String key, String def) {
        try {
            return mFields.getString(key);
        } catch (Exception e) {
            return def;
        }
    }

    public static String getMethod(String key) {
        try {
            return mMethods.getString(key);
        } catch (Exception e) {
            return "a";
        }
    }
}
