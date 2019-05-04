package me.zpp0196.qqpurify.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.SwitchPreference;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import me.zpp0196.qqpurify.R;
import me.zpp0196.qqpurify.activity.SettingsActivity;

/**
 * Created by zpp0196 on 2019/2/9.
 */

public class SettingPreferenceFragment extends AbstractPreferenceFragment {

    @Override
    protected int getPrefRes() {
        return R.xml.pref_setting;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwitchPreference hideDesktopIcon = (SwitchPreference) findPreference("module_hide_desktopIcon");
        hideDesktopIcon.setChecked(!getEnable());
        hideDesktopIcon.setOnPreferenceChangeListener((preference, newValue) -> {
            // 设置桌面图标
            activity.getPackageManager()
                    .setComponentEnabledSetting(getAlias(),
                            getEnable() ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED :
                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

            return true;
        });
        findPreference("module_backupData").setOnPreferenceClickListener(preference -> {
            if (checkPermission(activity.REQUEST_BACKUP)) {
                activity.backupPref();
            }
            return false;
        });
        findPreference("module_restoreBackup").setOnPreferenceClickListener(preference -> {
            if (checkPermission(activity.REQUEST_RESTORE)) {
                activity.restoreBackup();
            }
            return false;
        });
        findPreference("module_restoreDefault").setOnPreferenceClickListener(preference -> {
            showRestoreDefaultDialog();
            return false;
        });
    }

    private ComponentName getAlias() {
        return new ComponentName(activity, SettingsActivity.class.getName() + "Alias");
    }

    private boolean getEnable() {
        try {
            PackageManager packageManager = activity.getPackageManager();
            int state = packageManager.getComponentEnabledSetting(getAlias());
            return state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED ||
                   state == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkPermission(int requestCode) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
            return false;
        }
        return true;
    }

    private void showRestoreDefaultDialog() {
        new AlertDialog.Builder(activity).setCancelable(false)
                .setTitle(R.string.dialog_title_tip)
                .setMessage(R.string.tip_setting_restore_default_confirm)
                .setPositiveButton(R.string.dialog_button_confirm, (dialog, which) -> {
                    activity.defaultPref.delete();
                    activity.showRestartDialog();
                })
                .setNegativeButton(R.string.dialog_button_cancel, null)
                .show();
    }
}
