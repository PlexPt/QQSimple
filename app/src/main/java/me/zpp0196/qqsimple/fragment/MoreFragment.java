package me.zpp0196.qqsimple.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import me.zpp0196.qqsimple.BuildConfig;
import me.zpp0196.qqsimple.Common;
import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.activity.CommandActivity;
import me.zpp0196.qqsimple.activity.SettingActivity;
import me.zpp0196.qqsimple.fragment.base.BaseFragment;

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
        findPreference("setting_bor").setOnPreferenceClickListener(this);
        String filesDir = getActivity().getFilesDir().getAbsolutePath();
        if (!filesDir.contains("virtual")) {
            addPrefs();
        }
    }

    private void addPrefs() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen == null) return;
        PreferenceCategory category = new PreferenceCategory(getActivity());
        category.setTitle("ShortCut");
        preferenceScreen.addPreference(category);
        CheckBoxPreference autoStart = new CheckBoxPreference(getActivity());
        autoStart.setDefaultValue(false);
        autoStart.setKey("auto_start");
        autoStart.setTitle("重启模式");
        autoStart.setSummary("开启后强行停止/更新后直接自动进入应用");
        preferenceScreen.addPreference(autoStart);
        Preference forceStopQQ = new Preference(getActivity());
        forceStopQQ.setTitle("强行停止 QQ");
        forceStopQQ.setKey("forceStopQQ");
        forceStopQQ.setOnPreferenceClickListener(this);
        preferenceScreen.addPreference(forceStopQQ);
        if (!Common.isInstalled(getActivity(), Common.PACKAGE_NAME_VXP)) return;
        Preference updateModuleInVxp = new Preference(getActivity());
        updateModuleInVxp.setTitle("更新 Vxp 中的模块");
        updateModuleInVxp.setKey("updateModuleInVxp");
        updateModuleInVxp.setOnPreferenceClickListener(this);
        preferenceScreen.addPreference(updateModuleInVxp);
        Preference rebootQQInVxp = new Preference(getActivity());
        rebootQQInVxp.setTitle("重启 Vxp 中的 QQ");
        rebootQQInVxp.setKey("rebootQQInVxp");
        rebootQQInVxp.setOnPreferenceClickListener(this);
        preferenceScreen.addPreference(rebootQQInVxp);
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
        Intent intent = new Intent();
        switch (preference.getKey()) {
            case "forceStopQQ":
                intent.setAction(Intent.ACTION_VIEW);
                intent.setClass(getActivity(), CommandActivity.class);
                intent.putExtra("packageName", Common.PACKAGE_NAME_QQ);
                intent.putExtra("progressName", " QQ ");
                startActivity(intent);
                return true;
            case "updateModuleInVxp":
                intent.setAction(Intent.ACTION_VIEW);
                intent.setClass(getActivity(), CommandActivity.class);
                intent.putExtra("vxpCmdType", "update");
                intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
                intent.putExtra("progressName", "模块");
                startActivity(intent);
                return true;
            case "rebootQQInVxp":
                intent.setAction(Intent.ACTION_VIEW);
                intent.setClass(getActivity(), CommandActivity.class);
                intent.putExtra("vxpCmdType", "reboot");
                intent.putExtra("packageName", Common.PACKAGE_NAME_QQ);
                intent.putExtra("progressName", " QQ ");
                startActivity(intent);
                return true;
            case "setting_bor":
                new MaterialDialog.Builder(getActivity()).title("提示").content(R.string.bor_help).neutralText("存储空间授权").onNeutral((dialog, which) -> getPermission()).negativeText("备份").onNegative((dialog, which) -> backupPrefs()).positiveText("恢复").onPositive((dialog, which) -> restorePrefs()).build().show();
                return true;
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

    private void getPermission() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", BuildConfig.APPLICATION_ID, null));
        startActivity(intent);
    }

    /**
     * 备份配置文件
     */
    private void backupPrefs() {
        File dataDir = new File(getActivity().getApplicationInfo().dataDir);
        File prefsDir = new File(dataDir, "shared_prefs");
        File prefsFile = new File(prefsDir, BuildConfig.APPLICATION_ID + "_preferences.xml");
        File sdDataDir = new File(Environment.getExternalStorageDirectory(), "Android/data/" + BuildConfig.APPLICATION_ID);
        File destFile = new File(sdDataDir, BuildConfig.APPLICATION_ID + "_preferences.xml");
        try {
            if (!sdDataDir.exists()) {
                sdDataDir.mkdirs();
            }
            if (destFile.exists()) {
                destFile.delete();
            }
            destFile.createNewFile();
            FileInputStream ins = new FileInputStream(prefsFile);
            FileOutputStream out = new FileOutputStream(destFile);
            byte[] b = new byte[1024];
            int n;
            while ((n = ins.read(b)) != -1) {
                out.write(b, 0, n);
            }
            ins.close();
            out.close();
            showToast("已备份到 " + destFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            showToast(e.getMessage());
        }
    }

    /**
     * 恢复配置文件
     */
    private void restorePrefs() {
        File dataDir = new File(getActivity().getApplicationInfo().dataDir);
        File prefsDir = new File(dataDir, "shared_prefs");
        File prefsFile = new File(prefsDir, BuildConfig.APPLICATION_ID + "_preferences.xml");
        File sdDataDir = new File(Environment.getExternalStorageDirectory(), "Android/data/" + BuildConfig.APPLICATION_ID);
        File destFile = new File(sdDataDir, BuildConfig.APPLICATION_ID + "_preferences.xml");
        try {
            if (!destFile.exists()) {
                showToast("未找到备份文件！");
                return;
            }
            FileInputStream ins = new FileInputStream(destFile);
            FileOutputStream out = new FileOutputStream(prefsFile);
            byte[] b = new byte[1024];
            int n;
            while ((n = ins.read(b)) != -1) {
                out.write(b, 0, n);
            }
            ins.close();
            out.close();
            setWorldReadable();
            showRestartDialog();
        } catch (Exception e) {
            e.printStackTrace();
            showToast(e.getMessage());
        }
    }

    private void showRestartDialog() {
        new MaterialDialog.Builder(getActivity()).cancelable(false).title("提示").content("已恢复备份配置需要重启应用生效！").positiveText("重启").onPositive((dialog, which) -> {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }).build().show();
    }

    private PackageManager getPackageManager() {
        Activity activity = getActivity();
        if (activity == null) {
            return null;
        }
        return activity.getPackageManager();
    }
}