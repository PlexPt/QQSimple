package me.zpp0196.qqsimple.fragment.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.StringRes;
import android.support.annotation.XmlRes;
import android.widget.Toast;

import me.zpp0196.qqsimple.activity.MainActivity;

/**
 * Created by zpp0196 on 2018/3/16.
 */

public abstract class BasePreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(setPrefsId());
        getMainActivity().setWorldReadable();
        initData();
    }

    @XmlRes
    protected abstract int setPrefsId();

    @StringRes
    public abstract int getTitleId();

    protected void initData() {}

    protected SharedPreferences getPrefs() {
        return getMainActivity().getPrefs();
    }

    protected SharedPreferences.Editor getEditor() {
        return getPrefs().edit();
    }

    @SuppressWarnings ("unchecked")
    protected <T extends Preference> T findPreference(String key) {
        return (T) getPreferenceScreen().findPreference(key);
    }

    @Override
    public void onPause() {
        super.onPause();
        getMainActivity().setWorldReadable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getMainActivity().setWorldReadable();
    }

    protected void showToast(@StringRes int strId) {
        showToast(getString(strId));
    }

    protected void showToast(CharSequence msg) {
        MainActivity mainActivity = getMainActivity();
        if (mainActivity != null) {
            Toast.makeText(mainActivity, msg, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    protected MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

}