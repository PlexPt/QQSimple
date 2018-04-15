package me.zpp0196.qqsimple.fragment;

import android.annotation.SuppressLint;
import android.preference.Preference;

import com.afollestad.materialdialogs.MaterialDialog;

import me.zpp0196.qqsimple.BuildConfig;
import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.fragment.base.BaseFragment;

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
        findPreference("instructions").setOnPreferenceClickListener(this);
        findPreference("donate").setOnPreferenceClickListener(this);
        findPreference("feedback").setOnPreferenceClickListener(this);
        findPreference("github").setOnPreferenceClickListener(this);
        findPreference("license").setOnPreferenceClickListener(this);
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "instructions":
                showInstructions();
                return true;
            case "donate":
                getSettingActivity().openAlipay();
                return true;
            case "feedback":
                getSettingActivity().openCoolApk();
            case "github":
                getSettingActivity().openUrl("https://github.com/zpp0196/QQSimple");
                return true;
            case "license":
                getSettingActivity().openUrl("https://github.com/afollestad/material-dialogs");
                return true;
        }
        return true;
    }

    public void showInstructions() {
        new MaterialDialog.Builder(getActivity())
                .cancelable(false)
                .title(String.format("QQ 精简模块 %s", BuildConfig.VERSION_NAME))
                .content(R.string.instructions)
                .positiveText("捐赠")
                .negativeText("反馈")
                .neutralText("关闭")
                .onPositive(((dialog, which) -> getSettingActivity().openAlipay()))
                .onNegative((dialog, which) -> getSettingActivity().openCoolApk()).build().show();
    }
}
