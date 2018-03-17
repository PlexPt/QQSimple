package me.zpp0196.qqsimple.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import java.io.File;

import me.zpp0196.qqsimple.util.ToastUtils;

import static me.zpp0196.qqsimple.Common.KEY_DESKTOP_ICON;

/**
 * Created by zpp0196 on 2018/3/16.
 */

public abstract class BaseFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    protected ToastUtils toastUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(setPrefs());
        setWorldReadable();
        initData();
    }

    protected abstract int setPrefs();

    protected void initData() {}

    @Override
    public void onPause() {
        super.onPause();
        setWorldReadable();
        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(toastUtils == null){
            toastUtils = ToastUtils.getInstance(getActivity());
        }
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (!key.equals(KEY_DESKTOP_ICON)) {
            showToast("设置已保存，重启 QQ 生效！");
        }
    }

    @SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored"})
    @SuppressLint({"SetWorldReadable", "WorldReadableFiles"})
    private void setWorldReadable() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        File dataDir = new File(activity.getApplicationInfo().dataDir);
        File prefsDir = new File(dataDir, "shared_prefs");
        File prefsFile = new File(prefsDir, getPreferenceManager().getSharedPreferencesName() + ".xml");
        if (prefsFile.exists()) {
            for (File file : new File[]{dataDir, prefsDir, prefsFile}) {
                file.setReadable(true, false);
                file.setExecutable(true, false);
            }
        }
    }

    protected void showToast(String msg) {
        if(toastUtils != null) {
            toastUtils.showToast(msg);
        }
    }
}
