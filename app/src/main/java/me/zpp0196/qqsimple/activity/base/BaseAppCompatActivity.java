package me.zpp0196.qqsimple.activity.base;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;

import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.util.CommUtil;

import static me.zpp0196.qqsimple.BuildConfig.APPLICATION_ID;
import static me.zpp0196.qqsimple.Common.PACKAGE_NAME_ALIPAY;
import static me.zpp0196.qqsimple.util.CommUtil.getPrefsDir;
import static me.zpp0196.qqsimple.util.CommUtil.getPrefsFile;
import static me.zpp0196.qqsimple.util.UpdateUtil.getThisVersionLog;

/**
 * Created by zpp0196 on 2018/5/30 0030.
 */

@SuppressLint ("Registered")
public abstract class BaseAppCompatActivity extends AppCompatActivity {

    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentViewId());
        initRootView();
        initData(savedInstanceState);
    }

    @LayoutRes
    protected abstract int setContentViewId();

    @IdRes
    protected abstract int setRootViewId();

    protected void initRootView() {
        rootView = findViewById(setRootViewId());
    }

    public View getRootView() {
        return rootView;
    }

    protected void initData(Bundle savedInstanceState) {}

    public void openAlipay() {
        String qrcode = "FKX03149H8YOUWESHOCEC6";
        try {
            getPackageManager().getPackageInfo(PACKAGE_NAME_ALIPAY, PackageManager.GET_ACTIVITIES);
            openUrl("alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=https://qr.alipay.com/" +
                    qrcode + "%3F_s%3Dweb-other&_t=");
        } catch (PackageManager.NameNotFoundException e) {
            openUrl("https://mobilecodec.alipay.com/client_download.htm?qrcode=" + qrcode);
        }
    }

    public void openCoolApk() {
        try {
            startActivity(getCoolapkIntent(APPLICATION_ID));
        } catch (Exception e) {
            openUrl("http://www.coolapk.com/apk/" + APPLICATION_ID);
        }
    }

    @SuppressLint ("WrongConstant")
    private Intent getCoolapkIntent(String packageName) {
        String str = "market://details?id=" + packageName;
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(str));
        intent.setPackage("com.coolapk.market");
        intent.setFlags(0x10000000);
        return intent;
    }

    public void openGitHub() {
        openUrl(getString(R.string.about_github_url));
    }

    public void openReleases() {
        openUrl(getString(R.string.about_github_url) + "/releases");
    }

    public void openMyCoolapk() {
        openUrl("https://www.coolapk.com/u/1158829");
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
            showExceptionTip(e);
        }
    }

    public void launchApp(String packageName) {
        try {
            startActivity(getPackageManager().getLaunchIntentForPackage(packageName));
        } catch (Exception e) {
            showExceptionTip(e);
        }
    }

    public void showExceptionTip(Exception e) {
        e.printStackTrace();
        if (getRootView() == null) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
        } else {
            Snackbar.make(getRootView(), R.string.title_appear_exception, Snackbar.LENGTH_LONG)
                    .setAction(R.string.title_action_copy_log, v -> {
                        ClipData clip = ClipData.newPlainText("text", e.getMessage());
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        if (clipboardManager != null) {
                            clipboardManager.setPrimaryClip(clip);
                        }
                    });
        }
    }

    public void showMsgDialog(@StringRes int title, @StringRes int msg) {
        new MaterialDialog.Builder(this).cancelable(false)
                .title(title)
                .content(msg)
                .positiveText(R.string.button_close)
                .build()
                .show();
    }

    public void showDialogView(@StringRes int title, View view) {
        new MaterialDialog.Builder(this).cancelable(false)
                .title(title)
                .customView(view, true)
                .positiveText(R.string.button_close)
                .build()
                .show();
    }

    public void showUpdateLog() {
        showDialogView(R.string.title_update_log, getWebView(getThisVersionLog(this)));
    }

    public WebView getWebView(String html) {
        WebView webView = new WebView(this);
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        return webView;
    }

    public SharedPreferences getPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    public SharedPreferences.Editor getEditor() {
        return getPrefs().edit();
    }

    @SuppressWarnings ({"deprecation", "ResultOfMethodCallIgnored"})
    @SuppressLint ({"SetWorldReadable", "WorldReadableFiles"})
    public void setWorldReadable() {
        if (getPrefsFile(this).exists()) {
            for (File file : new File[]{CommUtil.getDataDir(this), getPrefsDir(this), getPrefsFile(this)}) {
                file.setReadable(true, false);
                file.setExecutable(true, false);
            }
        }
    }
}
