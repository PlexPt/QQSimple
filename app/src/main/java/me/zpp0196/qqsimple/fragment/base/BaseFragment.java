package me.zpp0196.qqsimple.fragment.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;

import me.zpp0196.qqsimple.BuildConfig;
import me.zpp0196.qqsimple.activity.SettingActivity;

/**
 * Created by zpp0196 on 2018/3/16.
 */

public abstract class BaseFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(setPrefs());
        setWorldReadable();
        initData();
    }

    protected abstract int setPrefs();

    protected void initData() {
    }

    @Override
    public void onPause() {
        super.onPause();
        setWorldReadable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setWorldReadable();
    }

    @SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored"})
    @SuppressLint({"SetWorldReadable", "WorldReadableFiles"})
    public void setWorldReadable() {
        File dataDir = new File(getActivity().getApplicationInfo().dataDir);
        File prefsDir = new File(dataDir, "shared_prefs");
        File prefsFile = new File(prefsDir, BuildConfig.APPLICATION_ID + "_preferences.xml");
        if (prefsFile.exists()) {
            for (File file : new File[]{dataDir, prefsDir, prefsFile}) {
                file.setReadable(true, false);
                file.setExecutable(true, false);
            }
        }
    }

    protected void showToast(@NonNull Object msg) {
        Activity activity = getActivity();
        if (activity != null) {
            Toast.makeText(getActivity(), msg.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void showDialog(String msg) {
        new MaterialDialog.Builder(getActivity()).title("说明").content(msg).positiveText("关闭").build().show();
    }

    protected SettingActivity getSettingActivity() {
        return (SettingActivity) getActivity();
    }
}