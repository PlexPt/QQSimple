package me.zpp0196.qqsimple.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import me.zpp0196.qqsimple.BuildConfig;
import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.fragment.AboutFragment;
import me.zpp0196.qqsimple.fragment.ChatFragment;
import me.zpp0196.qqsimple.fragment.GroupFragment;
import me.zpp0196.qqsimple.fragment.MainUIFragment;
import me.zpp0196.qqsimple.fragment.MoreFragment;
import me.zpp0196.qqsimple.fragment.OtherFragment;
import me.zpp0196.qqsimple.fragment.SidebarFragment;

import static me.zpp0196.qqsimple.Common.isModuleActive;

public class SettingActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.prefs_header, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || MainUIFragment.class.getName().equals(fragmentName)
                || SidebarFragment.class.getName().equals(fragmentName)
                || ChatFragment.class.getName().equals(fragmentName)
                || GroupFragment.class.getName().equals(fragmentName)
                || OtherFragment.class.getName().equals(fragmentName)
                || MoreFragment.class.getName().equals(fragmentName)
                || AboutFragment.class.getName().equals(fragmentName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private void checkState() {
        if (!isModuleActive()) {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("模块未激活，请先激活模块并重启手机！")
                    .setPositiveButton("激活", (dialog, id) -> openXposed())
                    .setNegativeButton("取消", (dialog, id) -> finish())
                    .show();
        } else {
            boolean isUpdate = getPrefs().getInt("app_version_code", 0) < BuildConfig.VERSION_CODE;
            if (isUpdate) {
                showInstructions();
            }
        }
    }

    public SharedPreferences getPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void openXposed() {
        if (isXposedInstalled()) {
            Intent intent = new Intent("de.robv.android.xposed.installer.OPEN_SECTION");
            PackageManager packageManager = getPackageManager();
            if (packageManager == null) {
                return;
            }
            if (packageManager.queryIntentActivities(intent, 0).isEmpty()) {
                intent = packageManager.getLaunchIntentForPackage("de.robv.android.xposed.installer");
            }
            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("section", "modules")
                        .putExtra("fragment", 1)
                        .putExtra("module", BuildConfig.APPLICATION_ID);
                startActivity(intent);
            }
        } else {
            Toast.makeText(this, "未安装 XposedInstaller !", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isXposedInstalled() {
        try {
            PackageManager packageManager = getPackageManager();
            if (packageManager == null) {
                return false;
            }
            packageManager.getApplicationInfo("de.robv.android.xposed.installer", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void showInstructions() {
        new MaterialDialog.Builder(this)
                .cancelable(false)
                .title(String.format("QQ 精简模块 %s", BuildConfig.VERSION_NAME))
                .content(R.string.instructions)
                .positiveText("捐赠")
                .negativeText("反馈")
                .neutralText("关闭")
                .onAny((dialog, which) -> {
                    getPrefs().edit().putInt("app_version_code", BuildConfig.VERSION_CODE).apply();
                    switch (which) {
                        case POSITIVE:
                            openAlipay();
                            break;
                        case NEGATIVE:
                            openCoolApk();
                            break;
                    }
                }).build().show();
    }

    public void openAlipay() {
        String qrcode = "FKX03149H8YOUWESHOCEC6";
        try {
            getPackageManager().getPackageInfo("com.eg.android.AlipayGphone", PackageManager.GET_ACTIVITIES);
            openUrl("alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=https://qr.alipay.com/" + qrcode + "%3F_s%3Dweb-other&_t=");
        } catch (PackageManager.NameNotFoundException e) {
            openUrl("https://mobilecodec.alipay.com/client_download.htm?qrcode=" + qrcode);
        }
    }

    @SuppressLint("WrongConstant")
    public void openCoolApk() {
        try {
            String str = "market://details?id=" + BuildConfig.APPLICATION_ID;
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            intent.setPackage("com.coolapk.market");
            intent.setFlags(0x10000000);
            startActivity(intent);
        } catch (Exception e) {
            openUrl("http://www.coolapk.com/apk/" + BuildConfig.APPLICATION_ID);
        }
    }

    public void openUrl(String url) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}