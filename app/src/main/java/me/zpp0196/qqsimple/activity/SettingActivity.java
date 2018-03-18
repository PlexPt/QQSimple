package me.zpp0196.qqsimple.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Toast;

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
        }
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
}