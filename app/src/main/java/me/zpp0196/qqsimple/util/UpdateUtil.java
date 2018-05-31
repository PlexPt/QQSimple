package me.zpp0196.qqsimple.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

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
                    updateInfo.setUpdateLog(getNewVersionLog());
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

    private static String getNewVersionLog() throws JSONException, IOException {
        JSONArray array = getJson().getJSONArray(KEY_UPDATE_LOG);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getJSONObject(i)
                    .getString(KEY_UPDATE_LOG_CONTENT));
        }
        return getLogHtml(list, getVersionName(), getVersionCode());
    }

    public static String getThisVersionLog(Context context) {
        String[] log = context.getResources()
                .getStringArray(R.array.UpdateLog);
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, log);
        return getLogHtml(list, VERSION_NAME, VERSION_CODE);
    }

    private static String getLogHtml(ArrayList<String> logList, String versionName, int versionCode) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < logList.size(); i++) {
            sb.append(String.format("<li>%s</li>", logList.get(i)));
        }
        return String.format("<h3 style=\"padding-left:16px\">v%s(%s)</h3><ul>%s</ul>", versionName, versionCode, sb.toString());
    }

    public static class UpdateInfo {
        private boolean isUpdate;
        private String versionName;
        private int versionCode;
        private String updateLog;

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

        public String getUpdateLog() {
            return updateLog;
        }

        public void setUpdateLog(String updateLog) {
            this.updateLog = updateLog;
        }
    }
}
