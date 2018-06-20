package me.zpp0196.qqsimple.util;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ArrayRes;
import android.util.Log;
import android.webkit.WebView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.activity.MainActivity;

import static me.zpp0196.qqsimple.BuildConfig.VERSION_CODE;
import static me.zpp0196.qqsimple.BuildConfig.VERSION_NAME;
import static me.zpp0196.qqsimple.Common.PREFS_KEY_IS_SHOW_MATTERS;

/**
 * Created by zpp0196 on 2018/5/11 0011.
 */

public class UpdateUtil {

    public static final int FINISHED = 1, ERR = -1;
    private static final String JSON_URL = "https://raw.githubusercontent.com/zpp0196/QQSimple/master/update.json";
    private final static String KEY_VERSION_NAME = "versionName";
    private final static String KEY_VERSION_CODE = "versionCode";
    private final static String KEY_UPDATE_LOG = "updateLog";
    private final static String KEY_UPDATE_LOG_CONTENT = "content";
    private static JSONObject jsonObject = null;

    public static void CheckUpdate(Handler handler) {
        new Thread() {
            @SuppressLint ("LogConditional")
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    boolean isUpdate = getVersionCode() > VERSION_CODE;
                    msg.obj = isUpdate;
                    msg.what = FINISHED;
                    Log.d(UpdateUtil.class.getName(), String.format("isUpdate: %s, versionName: %s, versionCode: %s", isUpdate, getVersionName(), getVersionCode()));
                } catch (Exception e) {
                    msg.obj = e;
                    msg.what = ERR;
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    private static JSONObject getJson() throws IOException, JSONException {
        if (jsonObject == null) {
            URL url = new URL(JSON_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.connect();
            StringBuilder builder = new StringBuilder();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    int len;
                    char buf[] = new char[4096];
                    while ((len = br.read(buf)) != -1) {
                        builder.append(buf, 0, len);
                    }
                }
            }
            conn.disconnect();
            jsonObject = new JSONObject(builder.toString());
        }
        return jsonObject;
    }

    private static String getVersionName() throws JSONException, IOException {
        return getJson().getString(KEY_VERSION_NAME);
    }


    private static int getVersionCode() throws JSONException, IOException {
        return getJson().getInt(KEY_VERSION_CODE);
    }

    private static ArrayList<String> getNewVersionLogList() throws JSONException, IOException {
        JSONArray array = getJson().getJSONArray(KEY_UPDATE_LOG);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getJSONObject(i)
                    .getString(KEY_UPDATE_LOG_CONTENT));
        }
        return list;
    }

    private static ArrayList<String> getList(MainActivity mainActivity, @ArrayRes int arrayId) {
        String[] log = mainActivity.getResources()
                .getStringArray(arrayId);
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, log);
        return list;
    }

    private static String getNewVersionLogHtml() throws JSONException, IOException {
        return getLogHtml(getNewVersionLogList(), getVersionName(), getVersionCode());
    }

    private static String getThisVersionLogHtml(MainActivity mainActivity) {
        return getLogHtml(getList(mainActivity, R.array.UpdateLog), VERSION_NAME, VERSION_CODE);
    }

    private static String list2String(ArrayList<String> logList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < logList.size(); i++) {
            sb.append(String.format("· %s%s", logList.get(i), i == logList.size() - 1 ? "" : "\n"));
        }
        return sb.toString();
    }

    private static String getLogHtml(ArrayList<String> logList, String versionName, int versionCode) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < logList.size(); i++) {
            sb.append(String.format("<li>%s</li>", logList.get(i)));
        }
        return String.format("<h3 style=\"padding-left:16px\">v%s(%s)</h3><ul>%s</ul>", versionName, versionCode, sb.toString());
    }

    public static void showUpdateDialog(MainActivity mainActivity) throws Exception {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(mainActivity).cancelable(false)
                .title(R.string.item_card_module_update_check_new)
                .positiveText(R.string.item_card_module_update_check_update)
                .negativeText(R.string.button_releases)
                .neutralText(R.string.button_close)
                .onPositive((dialog, which) -> mainActivity.openCoolApk())
                .onNegative((dialog, which) -> mainActivity.openReleases());
        WebView webView;
        try {
            webView = getWebView(mainActivity, getNewVersionLogHtml());
            if (webView != null) {
                builder.customView(webView, true);
            } else {
                builder.content(list2String(getNewVersionLogList()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        builder.show();
    }

    public static void showUpdateLog(MainActivity mainActivity) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(mainActivity).cancelable(false)
                .positiveText(R.string.button_close)
                .negativeText(R.string.button_more)
                .onPositive((dialog, which) -> showMatters(mainActivity))
                .onNegative((dialog, which) -> mainActivity.openUrl("https://github.com/zpp0196/QQSimple/blob/master/Log.md"));
        WebView webView = getWebView(mainActivity, getThisVersionLogHtml(mainActivity));
        if (webView != null) {
            builder.title(R.string.title_update_log);
            builder.customView(webView, true);
        } else {
            builder.title(String.format("%s_v%s(%s)", mainActivity.getString(R.string.app_name), VERSION_NAME, VERSION_CODE));
            builder.content(list2String(getList(mainActivity, R.array.UpdateLog)));
        }
        builder.show();
    }

    private static void showMatters(MainActivity mainActivity) {
        ArrayList<String> matters = getList(mainActivity, R.array.Matters);
        boolean isShowMatters = mainActivity.getPrefs()
                .getBoolean(PREFS_KEY_IS_SHOW_MATTERS, true);
        if (!matters.isEmpty() && isShowMatters) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < matters.size(); i++) {
                sb.append(String.format("%s、%s。%s",
                        i + 1, matters.get(i), i == matters.size() - 1 ? "" : "\n"));
            }
            new MaterialDialog.Builder(mainActivity).title(R.string.title_matters)
                    .content(sb.toString())
                    .positiveText(R.string.button_close)
                    .checkBoxPromptRes(R.string.switch_not_prompt, false, (buttonView, isChecked) -> mainActivity.getEditor()
                            .putBoolean(PREFS_KEY_IS_SHOW_MATTERS, !isChecked)
                            .apply())
                    .show();
        }
    }

    private static WebView getWebView(MainActivity mainActivity, String html) {
        try {
            WebView webView = new WebView(mainActivity);
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
            return webView;
        } catch (Exception e) {
            return null;
        }
    }
}
