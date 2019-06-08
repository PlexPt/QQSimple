package me.zpp0196.qqpurify.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import me.zpp0196.qqpurify.BuildConfig;
import me.zpp0196.qqpurify.utils.Utils;

public class CheckUpdateTask extends AsyncTask<Void, Void, CheckUpdateTask.Result> {

    private static final String TAG = "CheckUpdateTask";
    private static final String RELEASE_OUTPUT_JSON = "https://raw.githubusercontent.com/zpp0196/QQPurify/master/app/release/output.json";

    private WeakReference<Activity> mActivity;
    private boolean mAutoOpen;

    public CheckUpdateTask(Activity activity) {
        this.mActivity = new WeakReference<>(activity);
    }

    public CheckUpdateTask(Activity activity, boolean autoOpen) {
        this(activity);
        this.mAutoOpen = autoOpen;
    }

    @Override
    protected Result doInBackground(Void... voids) {
        Result result = new Result();
        try {
            JSONArray jsonArray = new JSONArray(getOutputJson());
            JSONObject output = jsonArray.optJSONObject(0);
            Log.d(TAG, "output: " + output);
            JSONObject apkInfo = output.optJSONObject("apkInfo");
            result.versionCode = apkInfo.optLong("versionCode");
            result.outputFile = output.optString("path");
            result.downloadUrl = "https://github.com/zpp0196/QQPurify/releases/latest";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String getOutputJson() {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        InputStreamReader is = null;
        try {
            URL realUrl = new URL(RELEASE_OUTPUT_JSON);
            URLConnection connection = realUrl.openConnection();
            connection.setConnectTimeout(1000 * 60);
            connection.connect();
            is = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
            in = new BufferedReader(is);
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (in != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (result.versionCode <= BuildConfig.VERSION_CODE) {
            return;
        }
        Activity activity = mActivity.get();
        Toast.makeText(activity, "发现新版本: " + result.outputFile, Toast.LENGTH_SHORT).show();
        if (mAutoOpen) {
            Utils.openUrl(activity, result.downloadUrl);
        }
    }

    static class Result {
        long versionCode;
        String outputFile;
        String downloadUrl;
    }
}
