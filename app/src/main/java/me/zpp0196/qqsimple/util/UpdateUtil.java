package me.zpp0196.qqsimple.util;

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
import java.util.List;

import me.zpp0196.qqsimple.BuildConfig;

/**
 * Created by zpp0196 on 2018/5/11 0011.
 */

public class UpdateUtil {

    private static JSONObject jsonObject = null;
    private static final String JSON_URL = "https://raw.githubusercontent.com/zpp0196/QQSimple/master/update.json";
    private static List<String> thisVersionUpdateLog;

    public static final int FINISHED = 1, ERR = -1;

    public static void CheckUpdate(Handler handler) {
        new Thread(){
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    UpdateInfo updateInfo = new UpdateInfo();
                    updateInfo.setUpdate(getVersionCode() > BuildConfig.VERSION_CODE);
                    updateInfo.setVersionName(getVersionName());
                    updateInfo.setVersionCode(getVersionCode());
                    updateInfo.setUpdateLog(getUpdateLog());
                    msg.obj = updateInfo;
                    msg.what = FINISHED;
                } catch (Exception e) {
                    msg.obj = e;
                    msg.what = ERR;
                }finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    private static JSONObject getJson() throws IOException, JSONException {
        if(jsonObject == null) {
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
        return getJson().getString("versionName");
    }


    private static int getVersionCode() throws JSONException, IOException {
        return getJson().getInt("versionCode");
    }

    private static List<String> getUpdateLog() throws JSONException, IOException {
        JSONArray array = getJson().getJSONArray("updateLog");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            list.add(String.format("%s、%s", i + 1, obj.getString("content")));
        }
        return list;
    }

    public static List<String> getThisVersionUpdateLog(){
        if(thisVersionUpdateLog == null) {
            thisVersionUpdateLog = new ArrayList<>();
        }
        thisVersionUpdateLog.add("1、修复了不能隐藏移出群助手提示的 bug");
        thisVersionUpdateLog.add("2、修复了隐藏联系人分组后显示异常的 bug");
        thisVersionUpdateLog.add("3、移除了关闭动画");
        return thisVersionUpdateLog;
    }

    public static class UpdateInfo{
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

        public List<String> getUpdateLog() {
            return updateLog;
        }

        public void setUpdateLog(List<String> updateLog) {
            this.updateLog = updateLog;
        }

        private List<String> updateLog;
    }
}
