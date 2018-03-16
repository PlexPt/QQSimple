package me.zpp0196.qqsimple.fragment;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.preference.Preference;
import android.support.v7.app.AlertDialog;

import me.zpp0196.qqsimple.BuildConfig;
import me.zpp0196.qqsimple.R;

import static me.zpp0196.qqsimple.Common.KEY_GITHUB_SOURCE;
import static me.zpp0196.qqsimple.Common.KEY_INSTRUCTIONS;
import static me.zpp0196.qqsimple.Common.getQQVersion;

/**
 * Created by zpp0196 on 2018/3/15.
 */

public class AboutFragment extends BaseFragment implements Preference.OnPreferenceClickListener {
    @Override
    protected int setPrefs() {
        return R.xml.prefs_about;
    }

    @Override
    protected void initData() {
        findPreference(KEY_INSTRUCTIONS).setOnPreferenceClickListener(this);
        findPreference(KEY_GITHUB_SOURCE).setOnPreferenceClickListener(this);
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case KEY_INSTRUCTIONS:
                new AlertDialog.Builder(getActivity())
                        .setTitle("帮助")
                        .setMessage(R.string.instructions)
                        .setPositiveButton("关闭", (dialog, which) -> {
                        })
                        .setNeutralButton("反馈", (dialog, which) -> {
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
                        })
                        .show();
                return true;
            case KEY_GITHUB_SOURCE:
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/zpp0196/QQSimple")));
                return true;
        }
        return true;
    }

    private void copyDeviceInfo(Context context) {
        String sb = String.format("手机型号：%s\n", Build.MODEL) +
                String.format("Android 版本：Android %s (API %s)\n", Build.VERSION.RELEASE, Build.VERSION.SDK_INT) +
                String.format("QQ 版本：%s\n", getQQVersion(context)) +
                String.format("模块版本：%s\n", BuildConfig.VERSION_NAME + "Bug：");
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text", sb);
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(clip);
        }
    }
}
