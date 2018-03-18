package me.zpp0196.qqsimple.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import me.zpp0196.qqsimple.BuildConfig;
import me.zpp0196.qqsimple.R;

import static me.zpp0196.qqsimple.Common.KEY_DESKTOP_ICON;
import static me.zpp0196.qqsimple.Common.KEY_SETTING_QQ;
import static me.zpp0196.qqsimple.Common.KEY_TEST_HELP;
import static me.zpp0196.qqsimple.Common.PACKAGE_NAME_QQ;

/**
 * Created by zpp0196 on 2018/3/15.
 */

public class MoreFragment extends BaseFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    @Override
    protected int setPrefs() {
        return R.xml.prefs_more;
    }

    @Override
    protected void initData() {
        CheckBoxPreference desktopIcon = (CheckBoxPreference) findPreference(KEY_DESKTOP_ICON);
        desktopIcon.setChecked(!getEnable());
        desktopIcon.setOnPreferenceChangeListener(this);
        findPreference(KEY_SETTING_QQ).setOnPreferenceClickListener(this);
        findPreference(KEY_TEST_HELP).setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(KEY_DESKTOP_ICON)) {
            Activity activity = getActivity();
            if (activity != null) {
                activity.getPackageManager().setComponentEnabledSetting(getAlias(),
                        getEnable() ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED : PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            } else {
                showToast("设置失败！");
            }
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(KEY_SETTING_QQ)) {
            try {
                startActivity(new Intent().setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + PACKAGE_NAME_QQ)));
            } catch (Exception e) {
                showToast(e.getMessage());
            }
        }
        if (preference.getKey().equals(KEY_TEST_HELP)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("测试功能介绍")
                    .setMessage(R.string.test_help)
                    .setPositiveButton("关闭", (dialog, which) -> {
                    })
                    .show();
        }
        return false;
    }

    private ComponentName getAlias() {
        return new ComponentName(getActivity(), String.format("%s.activity.SettingActivityAlias", BuildConfig.APPLICATION_ID));
    }

    private boolean getEnable() {
        PackageManager packageManager = getPackageManager();
        if (packageManager != null) {
            int state = packageManager.getComponentEnabledSetting(getAlias());
            return state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED || state == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
        }
        return true;
    }

    private PackageManager getPackageManager() {
        Activity activity = getActivity();
        if (activity == null) {
            return null;
        }
        return activity.getPackageManager();
    }
}