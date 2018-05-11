package me.zpp0196.qqsimple.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import me.zpp0196.qqsimple.BuildConfig;
import me.zpp0196.qqsimple.Common;
import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.fragment.AboutFragment;
import me.zpp0196.qqsimple.fragment.ChatFragment;
import me.zpp0196.qqsimple.fragment.GroupFragment;
import me.zpp0196.qqsimple.fragment.MainUIFragment;
import me.zpp0196.qqsimple.fragment.MoreFragment;
import me.zpp0196.qqsimple.fragment.OtherFragment;
import me.zpp0196.qqsimple.fragment.QZoneFragment;
import me.zpp0196.qqsimple.fragment.SidebarFragment;
import me.zpp0196.qqsimple.util.UpdateUtil;

import me.zpp0196.qqsimple.util.UpdateUtil.UpdateInfo;

public class SettingActivity extends AppCompatPreferenceActivity {

    private boolean isModuleActive() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkState();
        initShortCut();
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
                || QZoneFragment.class.getName().equals(fragmentName)
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
     * Util method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private void initShortCut() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) return;
        List<ShortcutInfo> list = new ArrayList<>();
        String label = getPrefs().getBoolean("auto_start", false) ? "重启 QQ" : "停止 QQ";
        list.add(new ShortcutInfo.Builder(this, "forceStopQQ")
                .setShortLabel(label)
                .setLongLabel(label)
                .setIcon(getIcon(Common.PACKAGE_NAME_QQ))
                .setIntent(new Intent(Intent.ACTION_VIEW).setClass(this, CommandActivity.class).putExtra("packageName", Common.PACKAGE_NAME_QQ).putExtra("progressName", " QQ "))
                .build());
        list.add(new ShortcutInfo.Builder(this, "openCoolapk")
                .setShortLabel("酷安")
                .setLongLabel("酷安")
                .setIcon(getIcon("com.coolapk.market"))
                .setIntent(getCoolapkIntent(BuildConfig.APPLICATION_ID))
                .build());
        if (isInstalled(Common.PACKAGE_NAME_VXP)) {
            list.add(new ShortcutInfo.Builder(this, "updateModuleInVxp")
                    .setShortLabel("更新模块")
                    .setLongLabel("更新 Vxp 中的模块")
                    .setIcon(getIcon(Common.PACKAGE_NAME_VXP))
                    .setIntent(new Intent(Intent.ACTION_VIEW).setClass(this, CommandActivity.class).putExtra("packageName", BuildConfig.APPLICATION_ID).putExtra("progressName", "模块").putExtra("vxpCmdType", "update"))
                    .build());
            list.add(new ShortcutInfo.Builder(this, "updateQQInVxp")
                    .setShortLabel(label)
                    .setLongLabel("重启 Vxp 中的 QQ")
                    .setIcon(getIcon(Common.PACKAGE_NAME_VXP))
                    .setIntent(new Intent(Intent.ACTION_VIEW).setClass(this, CommandActivity.class).putExtra("packageName", Common.PACKAGE_NAME_QQ).putExtra("progressName", " QQ ").putExtra("vxpCmdType", "reboot"))
                    .build());
        }
        try{
            getSystemService(ShortcutManager.class).setDynamicShortcuts(list);
        }catch (Exception e){
            //
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private Icon getIcon(String packageName) {
        Icon icon = Icon.createWithResource(this, R.mipmap.ic_launcher);
        try {
            Drawable drawable = getPackageManager().getApplicationIcon(packageName);
            if (drawable instanceof BitmapDrawable) {
                icon = Icon.createWithBitmap(((BitmapDrawable) drawable).getBitmap());
            } else if (drawable instanceof AdaptiveIconDrawable) {
                Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bmp);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                icon = Icon.createWithBitmap(bmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return icon;
    }

    private void checkState() {
        if (!isModuleActive() && !BuildConfig.DEBUG) {
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

            boolean isAutoUpdate = getPrefs().getBoolean("checkUpdate_auto", true);
            long lastCheckUpdate = getPrefs().getLong("last_check_update", 0);
            if(isAutoUpdate && System.currentTimeMillis() - lastCheckUpdate > 216000000){
                checkUpdate(false);
            }
        }
    }

    public SharedPreferences getPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void openXposed() {
        if (isInstalled("de.robv.android.xposed.installer")) {
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
            showXposedTip();
        }
    }

    private void showXposedTip(){
        new MaterialDialog.Builder(this)
                .cancelable(false)
                .title("提示")
                .content("该设备未安装 Xposed 框架所以该模块将无法使用！")
                .positiveText("退出")
                .onPositive(((dialog, which) -> finish())).build().show();
    }

    public void showInstructions() {
        new MaterialDialog.Builder(this)
                .cancelable(false)
                .title(String.format("QQ 精简模块 %s", BuildConfig.VERSION_NAME))
                .content(R.string.instructions)
                .positiveText("更多")
                .negativeText("捐赠")
                .neutralText("关闭")
                .onPositive(((dialog, which) -> openReadme()))
                .onNegative((dialog, which) -> openAlipay())
                .onNeutral((dialog, which) -> getPrefs().edit().putInt("app_version_code", BuildConfig.VERSION_CODE).apply()).build().show();
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

    public void openCoolApk() {
        String packageName = BuildConfig.APPLICATION_ID;
        try {
            startActivity(getCoolapkIntent(packageName));
        } catch (Exception e) {
            openUrl("http://www.coolapk.com/apk/" + packageName);
        }
    }

    @SuppressLint("WrongConstant")
    private Intent getCoolapkIntent(String packageName) {
        String str = "market://details?id=" + packageName;
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(str));
        intent.setPackage("com.coolapk.market");
        intent.setFlags(0x10000000);
        return intent;
    }

    public void openGitHub(){
        openUrl("https://github.com/zpp0196/QQSimple");
    }

    public void openReleases(){
        openUrl("https://github.com/zpp0196/QQSimple/releases");
    }

    public void openReadme(){
        openUrl("https://github.com/zpp0196/QQSimple/blob/master/README.md");
    }

    public void openIssues(){
        openUrl("https://github.com/zpp0196/QQSimple/issues/new");
    }

    @SuppressLint ("HandlerLeak")
    public void checkUpdate(boolean isShowToast){
        Toast.makeText(this, "正在检查更新...", Toast.LENGTH_SHORT).show();
        UpdateUtil.CheckUpdate(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case UpdateUtil.FINISHED:
                        getPrefs().edit().putLong("last_check_update", System.currentTimeMillis()).apply();
                        UpdateInfo updateInfo = (UpdateInfo) msg.obj;
                        if(updateInfo.isUpdate()) {
                            showUpdateDialog(updateInfo);
                        }else if(isShowToast) {
                            Toast.makeText(SettingActivity.this, "当前已是最新版本", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        break;
                    case UpdateUtil.ERR:
                        Exception exception = (Exception) msg.obj;
                        if(isShowToast) {
                            Toast.makeText(SettingActivity.this, exception.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                        break;
                }
            }
        });
    }

    private void showUpdateDialog(UpdateInfo updateInfo){
        new MaterialDialog.Builder(this)
                .cancelable(false)
                .title(String.format("有新版可用：%s(%s)", updateInfo.getVersionName(), updateInfo.getVersionCode()))
                .items(updateInfo.getUpdateLog())
                .positiveText("更新")
                .negativeText("历史")
                .neutralText("关闭")
                .onPositive((dialog, which) -> openCoolApk())
                .onNegative((dialog, which) -> openReleases()).build().show();
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

    public boolean isInstalled(String packageName) {
        try {
            PackageManager packageManager = getPackageManager();
            if (packageManager == null) {
                return false;
            }
            packageManager.getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}