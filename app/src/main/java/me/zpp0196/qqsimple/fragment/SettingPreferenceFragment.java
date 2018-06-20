package me.zpp0196.qqsimple.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.support.annotation.StringRes;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.activity.MainActivity;
import me.zpp0196.qqsimple.fragment.base.BasePreferenceFragment;

import static me.zpp0196.qqsimple.BuildConfig.APPLICATION_ID;
import static me.zpp0196.qqsimple.Common.PREFS_KEY_IS_SHOW_BACKUP_TIP;
import static me.zpp0196.qqsimple.util.CommUtil.getBackupPrefsFile;
import static me.zpp0196.qqsimple.util.CommUtil.getPrefsFile;

/**
 * Created by zpp0196 on 2018/3/15.
 */

public class SettingPreferenceFragment extends BasePreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    @Override
    protected int setPrefsId() {
        return R.xml.prefs_setting;
    }

    @Override
    public int getTitleId() {
        return R.string.nav_item_setting;
    }

    @Override
    protected void initData() {
        CheckBoxPreference desktopIcon = findPreference("desktop_icon");
        desktopIcon.setChecked(!getEnable());
        desktopIcon.setOnPreferenceChangeListener(this);
        findPreference("setting_backup").setOnPreferenceClickListener(this);
        findPreference("setting_restore").setOnPreferenceClickListener(this);
        findPreference("setting_restore_default").setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case "desktop_icon":
                changeDesktopIcon();
                return true;
        }
        return true;
    }

    private void changeDesktopIcon() {
        MainActivity activity = getMainActivity();
        if (activity != null) {
            activity.getPackageManager()
                    .setComponentEnabledSetting(getAlias(),
                            getEnable() ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED :
                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        } else {
            showToast(R.string.tip_setting_failure);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "setting_backup":
                boolean isShowBackupTip = getPrefs().getBoolean(PREFS_KEY_IS_SHOW_BACKUP_TIP, true);
                if (isShowBackupTip) {
                    new MaterialDialog.Builder(getMainActivity()).title(R.string.title_tip)
                            .content(String.format(getString(R.string.tip_setting_backup), Environment.getExternalStorageDirectory()
                                    .getAbsoluteFile(), APPLICATION_ID, APPLICATION_ID))
                            .neutralText(R.string.button_get_permission)
                            .onNeutral((dialog, which) -> getPermission())
                            .negativeText(R.string.button_close)
                            .positiveText(R.string.button_backup)
                            .onPositive((dialog, which) -> backupPrefs())
                            .checkBoxPromptRes(R.string.switch_not_prompt, false, (buttonView, isChecked) -> getEditor().putBoolean(PREFS_KEY_IS_SHOW_BACKUP_TIP, !isChecked)
                                    .apply())
                            .show();
                } else {
                    backupPrefs();
                }
                return true;
            case "setting_restore":
                restorePrefs();
                return true;
            case "setting_restore_default":
                restoreDefault();
                return true;
        }
        return false;
    }

    private ComponentName getAlias() {
        return new ComponentName(getMainActivity(), MainActivity.class.getName() + "Alias");
    }

    private boolean getEnable() {
        try {
            PackageManager packageManager = getMainActivity().getPackageManager();
            int state = packageManager.getComponentEnabledSetting(getAlias());
            return state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED ||
                   state == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
        } catch (Exception e) {
            return false;
        }
    }

    private void getPermission() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", APPLICATION_ID, null));
        startActivity(intent);
    }

    /**
     * 备份配置文件
     */
    private void backupPrefs() {
        try {
            if (getBackupPrefsFile().exists()) {
                getBackupPrefsFile().delete();
            }
            getBackupPrefsFile().createNewFile();
            FileInputStream ins = new FileInputStream(getPrefsFile(getMainActivity()));
            FileOutputStream out = new FileOutputStream(getBackupPrefsFile());
            byte[] b = new byte[1024];
            int n;
            while ((n = ins.read(b)) != -1) {
                out.write(b, 0, n);
            }
            ins.close();
            out.close();
            showToast(String.format(getString(R.string.tip_setting_backup_finish), getBackupPrefsFile().getAbsolutePath()));
        } catch (Exception e) {
            getMainActivity().showThrowableDialog(e);
        }
    }

    /**
     * 恢复配置文件
     */
    private void restorePrefs() {
        try {
            if (!getBackupPrefsFile().exists()) {
                showToast(R.string.tip_setting_backup_not_find_file);
                return;
            }
            FileInputStream ins = new FileInputStream(getBackupPrefsFile());
            FileOutputStream out = new FileOutputStream(getPrefsFile(getMainActivity()));
            byte[] b = new byte[1024];
            int n;
            while ((n = ins.read(b)) != -1) {
                out.write(b, 0, n);
            }
            ins.close();
            out.close();
            getMainActivity().setWorldReadable();
            showRestartDialog(R.string.tip_setting_restore_reboot_tip);
        } catch (Exception e) {
            getMainActivity().showThrowableDialog(e);
        }
    }

    /**
     * 清空所有设置
     */
    private void restoreDefault() {
        new MaterialDialog.Builder(getMainActivity()).cancelable(false)
                .title(R.string.title_tip)
                .content(R.string.tip_setting_restore_default_reboot_confirm)
                .positiveText(R.string.button_ok)
                .negativeText(R.string.button_cancel)
                .onPositive((dialog, which) -> {
                    getPrefsFile(getMainActivity()).delete();
                    showRestartDialog(R.string.tip_setting_restore_default_reboot_tip);
                })
                .show();
    }

    private void showRestartDialog(@StringRes int msg) {
        new MaterialDialog.Builder(getMainActivity()).cancelable(false)
                .title(R.string.title_tip)
                .content(msg)
                .positiveText(R.string.button_reboot)
                .onPositive((dialog, which) -> {
                    Intent intent = new Intent(getMainActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getMainActivity().startActivity(intent);
                    android.os.Process.killProcess(android.os.Process.myPid());
                })
                .show();
    }
}