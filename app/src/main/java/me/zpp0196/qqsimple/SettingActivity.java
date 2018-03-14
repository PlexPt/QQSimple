package me.zpp0196.qqsimple;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.TwoStatePreference;

import java.io.File;

import me.zpp0196.qqsimple.util.ToastUtils;

import static me.zpp0196.qqsimple.Common.KEY_ADAPTATION_VERSION;
import static me.zpp0196.qqsimple.Common.KEY_DESKTOP_ICON;
import static me.zpp0196.qqsimple.Common.KEY_GITHUB_SOURCE;
import static me.zpp0196.qqsimple.Common.KEY_MARKET_COOLAPK_FEEDBACK;
import static me.zpp0196.qqsimple.Common.getQQVersion;

public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(getResources().getIdentifier("content", "id", "android"), new SettingFragment()).commit();
    }

    public boolean isModuleActive() {
        return false;
    }

    @SuppressLint("ValidFragment")
    public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

        private ToastUtils toastUtils;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setWorldReadable();
            addPreferencesFromResource(R.xml.setting);
            checkState();
            initData();
            toastUtils = ToastUtils.getInstance(getActivity());
        }

        private void initData() {
            TwoStatePreference desktopIcon = (TwoStatePreference) getPreferenceScreen().findPreference(KEY_DESKTOP_ICON);
            desktopIcon.setChecked(getEnable());
            desktopIcon.setOnPreferenceChangeListener(this);
            findPreference(KEY_MARKET_COOLAPK_FEEDBACK).setOnPreferenceClickListener(this);
            findPreference(KEY_GITHUB_SOURCE).setOnPreferenceClickListener(this);
            Preference isSupport = findPreference(KEY_ADAPTATION_VERSION);
            try {
                PackageManager manager = getActivity().getPackageManager();
                PackageInfo info = manager.getPackageInfo("com.tencent.mobileqq", 0);
                String version = info.versionName;
                isSupport.setSummary(String.format("当前 QQ版本：%s（%s的版本）", version, (version.compareTo("7.3.2") > 0 ? "支持" : "不支持")));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                isSupport.setSummary("未安装 QQ！");
            }
            findPreference(KEY_MARKET_COOLAPK_FEEDBACK).setSummary(String.format("当前版本：v%s", BuildConfig.VERSION_NAME));
        }

        @Override
        public void onPause() {
            super.onPause();
            setWorldReadable();
            PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onResume() {
            super.onResume();
            PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (!key.equals(KEY_DESKTOP_ICON)) {
                toastUtils.showToast("设置已保存，重启 QQ 生效！");
            }
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference.getKey().equals(KEY_DESKTOP_ICON)) {
                getPackageManager().setComponentEnabledSetting(getAlias(), getEnable() ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED : PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            }
            return true;
        }

        @SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored"})
        @SuppressLint({"SetWorldReadable", "WorldReadableFiles"})
        private void setWorldReadable() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                File dataDir = new File(getApplicationInfo().dataDir);
                File prefsDir = new File(dataDir, "shared_prefs");
                File prefsFile = new File(prefsDir, getPreferenceManager().getSharedPreferencesName() + ".xml");
                if (prefsFile.exists()) {
                    for (File file : new File[]{dataDir, prefsDir, prefsFile}) {
                        file.setReadable(true, false);
                        file.setExecutable(true, false);
                    }
                }
            } else {
                getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
            }
        }

        @SuppressLint("WrongConstant")
        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case KEY_MARKET_COOLAPK_FEEDBACK:
                    copyDeviceInfo(getActivity());
                    try {
                        String str = "market://details?id=" + BuildConfig.APPLICATION_ID;
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.setData(Uri.parse(str));
                        intent.setPackage("com.coolapk.market");
                        intent.setFlags(0x10000000);
                        startActivity(intent);
                    } catch (Exception e) {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://www.coolapk.com/apk/" + BuildConfig.APPLICATION_ID)));
                    }
                    break;
                case KEY_GITHUB_SOURCE:
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/zpp0196/QQSimple")));
                    break;
            }
            return true;
        }

        private void copyDeviceInfo(Context context) {
            String sb = String.format("手机型号：%s\n", Build.MODEL) +
                    String.format("Android 版本：Android %s (API %s)\n", Build.VERSION.RELEASE, Build.VERSION.SDK_INT) +
                    String.format("Xposed 状态：%s\n", (isModuleActive() ? "已启用" : "未启用")) +
                    String.format("QQ 版本：%s\n", getQQVersion(context)) +
                    "Bug：";
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("simple text", sb);
            if (clipboardManager != null) {
                clipboardManager.setPrimaryClip(clip);
            }
        }

        private void checkState() {
            if (!isModuleActive()) {
                new AlertDialog.Builder(getActivity())
                        .setCancelable(false)
                        .setMessage("模块未激活，请先激活模块并重启手机！")
                        .setPositiveButton("激活", (dialog, id) -> openXposed())
                        .setNegativeButton("取消", null)
                        .show();
            }
        }

        private void openXposed() {
            if (isXposedInstalled()) {
                Intent intent = new Intent("de.robv.android.xposed.installer.OPEN_SECTION");
                if (getPackageManager().queryIntentActivities(intent, 0).isEmpty()) {
                    intent = getPackageManager().getLaunchIntentForPackage("de.robv.android.xposed.installer");
                }
                if (intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("section", "modules")
                            .putExtra("fragment", 1)
                            .putExtra("module", BuildConfig.APPLICATION_ID);
                    startActivity(intent);
                }
            } else {
                toastUtils.showToast("未安装 XposedInstaller !");
            }
        }

        public boolean isXposedInstalled() {
            try {
                getPackageManager().getApplicationInfo("de.robv.android.xposed.installer", 0);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                return false;
            }
        }

        private ComponentName getAlias() {
            return new ComponentName(getActivity(), String.format("%s.SettingsActivityAlias", BuildConfig.APPLICATION_ID));
        }

        private boolean getEnable() {
            return getPackageManager().getComponentEnabledSetting(getAlias()) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
        }
    }
}
