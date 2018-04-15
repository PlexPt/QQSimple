package me.zpp0196.qqsimple.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.provider.Settings;

import me.zpp0196.qqsimple.BuildConfig;
import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.fragment.base.BaseFragment;

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
        CheckBoxPreference desktopIcon = (CheckBoxPreference) findPreference("desktop_icon");
        desktopIcon.setChecked(!getEnable());
        desktopIcon.setOnPreferenceChangeListener(this);
        findPreference("setting_qq").setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals("desktop_icon")) {
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
        if (preference.getKey().equals("setting_qq")) {
            try {
                startActivity(new Intent().setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + PACKAGE_NAME_QQ)));
            } catch (Exception e) {
                showToast(e.getMessage());
            }
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