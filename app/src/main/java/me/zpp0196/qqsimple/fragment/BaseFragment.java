package me.zpp0196.qqsimple.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import java.io.File;

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
        Activity activity = getActivity();
        if (activity != null) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}
