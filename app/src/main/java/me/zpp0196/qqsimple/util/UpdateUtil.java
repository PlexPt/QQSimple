package me.zpp0196.qqsimple.util;

import android.os.Handler;
import android.os.Message;
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
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    UpdateInfo updateInfo = new UpdateInfo();
                    updateInfo.setUpdate(getVersionCode() > VERSION_CODE);
                    updateInfo.setVersionName(getVersionName());
                    updateInfo.setVersionCode(getVersionCode());
                    msg.obj = updateInfo;
                    msg.what = FINISHED;
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

    private static ArrayList<String> getThisVersionLogList(MainActivity mainActivity) {
        String[] log = mainActivity.getResources()
                .getStringArray(R.array.UpdateLog);
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, log);
        return list;
    }

    private static String getNewVersionLogHtml() throws JSONException, IOException {
        return getLogHtml(getNewVersionLogList(), getVersionName(), getVersionCode());
    }

    private static String getThisVersionLogHtml(MainActivity mainActivity) {
        return getLogHtml(getThisVersionLogList(mainActivity), VERSION_NAME, VERSION_CODE);
    }

    private static String getLogString(ArrayList<String> logList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < logList.size(); i++) {
            sb.append(String.format("· %s\n", logList.get(i)));
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

    public static void showUpdateDialog(MainActivity mainActivity, UpdateInfo updateInfo){
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
            if(webView != null){
                builder.customView(webView, true);
            }else {
                builder.content(getLogString(getNewVersionLogList()));
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
                .onNegative((dialog, which) -> mainActivity.openUrl("https://github.com/zpp0196/QQSimple/blob/master/Log.md"));
        WebView webView = getWebView(mainActivity, getThisVersionLogHtml(mainActivity));
        if(webView != null){
            builder.title(R.string.title_update_log);
            builder.customView(webView, true);
        }else {
            builder.title(String.format("%s_v%s(%s)", mainActivity.getString(R.string.app_name), VERSION_NAME, VERSION_CODE));
            builder.content(getLogString(getThisVersionLogList(mainActivity)));
        }
        builder.show();
    }

    private static WebView getWebView(MainActivity mainActivity, String html){
        try {
            WebView webView = new WebView(mainActivity);
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
            return webView;
        }catch (Exception e){
            return null;
        }
    }

    public static class UpdateInfo {
        private boolean isUpdate;
        private String versionName;
        private int versionCode;

        public boolean isUpdate() {
            return isUpdate;
        }

        public void setUpdate(boolean update) {
            isUpdate = update;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }
    }
}
