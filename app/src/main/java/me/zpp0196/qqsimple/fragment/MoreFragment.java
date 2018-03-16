package me.zpp0196.qqsimple.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.preference.Preference;
import android.preference.TwoStatePreference;

import me.zpp0196.qqsimple.BuildConfig;
import me.zpp0196.qqsimple.R;

import static me.zpp0196.qqsimple.Common.KEY_DESKTOP_ICON;

/**
 * Created by zpp0196 on 2018/3/15.
 */

public class MoreFragment extends BaseFragment implements Preference.OnPreferenceChangeListener {
    @Override
    protected int setPrefs() {
        return R.xml.prefs_more;
    }

    @Override
    protected void initData() {
        TwoStatePreference desktopIcon = (TwoStatePreference) getPreferenceScreen().findPreference(KEY_DESKTOP_ICON);
        desktopIcon.setChecked(!getEnable());
        desktopIcon.setOnPreferenceChangeListener(this);
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
