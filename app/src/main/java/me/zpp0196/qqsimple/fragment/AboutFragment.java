package me.zpp0196.qqsimple.fragment;

import android.annotation.SuppressLint;
import android.preference.Preference;

import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.fragment.base.BaseFragment;

/**
 * Created by zpp0196 on 2018/3/15.
 */

public class AboutFragment extends BaseFragment implements Preference.OnPreferenceClickListener {
    @Override
    protected int setPrefs() {
        return R.xml.prefs_about;
    }

    @Override
    protected void initData() {
        findPreference("instructions").setOnPreferenceClickListener(this);
        findPreference("donate").setOnPreferenceClickListener(this);
        findPreference("feedback").setOnPreferenceClickListener(this);
        findPreference("github").setOnPreferenceClickListener(this);
        findPreference("license").setOnPreferenceClickListener(this);
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "instructions":
                getSettingActivity().showInstructions();
                return true;
            case "donate":
                getSettingActivity().openAlipay();
                return true;
            case "feedback":
                getSettingActivity().openCoolApk();
            case "github":
                getSettingActivity().openGitHub();
                return true;
            case "license":
                getSettingActivity().openUrl("https://github.com/afollestad/material-dialogs");
                return true;
        }
        return true;
    }
}
