package me.zpp0196.qqpurify.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import me.zpp0196.qqpurify.BuildConfig;

import static android.content.Context.MODE_PRIVATE;
import static me.zpp0196.qqpurify.BuildConfig.APPLICATION_ID;

/**
 * Created by zpp0196 on 2018/3/11.
 */
@SuppressWarnings("WeakerAccess")
public class Utils {

    public static Context createContext(Context context) throws PackageManager.NameNotFoundException {
        return context.createPackageContext(BuildConfig.APPLICATION_ID, Context.CONTEXT_IGNORE_SECURITY);
    }

    public static File getLocalPath() {
        return new File(Environment.getExternalStorageDirectory(), Constants.APP_NAME);
    }

    public static SharedPreferences getSp(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences(APPLICATION_ID + ".xml", MODE_PRIVATE);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String getTextFromAssets(Context context, String fileName) {
        try (InputStream is = context.getAssets().open(fileName)) {
            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }

    @SuppressWarnings("deprecation")
    public static long getAppVersionCode(Context context, String packageName) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return pi.getLongVersionCode();
            } else {
                return pi.versionCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getAppVersionName(Context context, String packageName) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, 0);
            return pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "unknown";
        }
    }

    public static void mailTo(Context context, String mailAddress) {
        Uri uri = Uri.parse("mailto:" + mailAddress);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        try {
            context.startActivity(Intent.createChooser(intent, "请选择发送邮件的应用"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openUrl(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getEnable(Activity activity) {
        try {
            PackageManager packageManager = activity.getPackageManager();
            int state = packageManager.getComponentEnabledSetting(getAlias(activity));
            return state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED ||
                    state == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
        } catch (Exception e) {
            return false;
        }
    }

    public static void updateDesktopIcon(Activity activity) {
        activity.getPackageManager().setComponentEnabledSetting(getAlias(activity),
                getEnable(activity) ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED :
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private static ComponentName getAlias(Activity activity) {
        return new ComponentName(activity, activity.getClass().getName() + "Alias");
    }

    public static String getProcessName() {
        try {
            return FileUtils.readFileToString(new File("/proc/"
                    + android.os.Process.myPid() + "/" + "cmdline"), Charset.defaultCharset()).trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String initialCapital(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    public static List<String> jArray2SList(JSONArray array) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                Object value = array.get(i);
                if (value == null) {
                    continue;
                }
                list.add(String.valueOf(value));
            } catch (JSONException ignore) {
            }
        }
        return list;
    }

    public static boolean isCallingFrom(String className) {
        boolean result = false;
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTraceElements) {
            if (element.getClassName().contains(className)) {
                result = true;
            }
        }
        return result;
    }

    @SuppressWarnings("unused")
    public static void printViewTree(View view) {
        recursiveView("\t", view);
    }

    private static void recursiveView(String space, View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            printView(space, view);
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                recursiveView(space + "\t", viewGroup.getChildAt(i));
            }
        } else {
            printView(space, view);
        }
    }

    @SuppressLint("ResourceType")
    private static void printView(String space, View view) {
        StringBuilder sb = new StringBuilder(space);
        sb.append(view.getClass().getCanonicalName());
        if (view.getId() > 0) {
            sb.append(", id: 0x").append(Integer.toHexString(view.getId()));
        }
        if (view instanceof TextView) {
            sb.append(", text: ").append(((TextView) view).getText());
        }
        if (!TextUtils.isEmpty(view.getContentDescription())) {
            sb.append(", description: ").append(view.getContentDescription());
        }
        sb.append(", visibility: ").append(getVisibilityString(view));
        Log.v("ViewTree", "printView: " + sb.toString());
    }

    public static String getVisibilityString(View view) {
        int visibility = view.getVisibility();
        switch (visibility) {
            case View.VISIBLE:
                return "VISIBLE";
            case View.INVISIBLE:
                return "INVISIBLE";
            case View.GONE:
                return "GONE";
            default:
                return "";
        }
    }
}
