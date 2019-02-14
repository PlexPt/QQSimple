package me.zpp0196.qqpurify.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;

import me.zpp0196.qqpurify.BuildConfig;
import me.zpp0196.qqpurify.R;

import static me.zpp0196.qqpurify.utils.Constants.PACKAGE_NAME_QQ;

/**
 * Created by zpp0196 on 2019/2/9.
 */

public class AboutPreferenceFragment extends AbstractPreferenceFragment implements Preference.OnPreferenceClickListener {
    @Override
    protected int getPrefRes() {
        return R.xml.pref_about;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSummaryAndIcon();
        findPreference("about_version_module").setOnPreferenceClickListener(this);
        findPreference("about_github").setOnPreferenceClickListener(this);
        findPreference("about_alipay").setOnPreferenceClickListener(this);
        findPreference("about_email").setOnPreferenceClickListener(this);
        findPreference("about_wiki").setOnPreferenceClickListener(this);
    }

    public void initSummaryAndIcon() {
        // 模块状态
        Preference moduleActive = findPreference("about_module_active");
        // 模块环境
        Preference moduleEnvironment = findPreference("about_module_environment");
        if (activity.isModuleActive()) {
            moduleActive.setSummary("已激活");
            if (System.getProperty("vxp") != null) {
                moduleEnvironment.setSummary("VirtualXposed");
            } else {
                moduleEnvironment.setSummary("Xposed");
            }
        } else if (activity.isExpModuleActive()) {
            moduleActive.setSummary("已激活");
            moduleEnvironment.setSummary("太极");
            moduleEnvironment.setIcon(R.drawable.ic_about_module_envi_taichi);
        } else {
            moduleActive.setSummary("未激活");
            moduleActive.setIcon(R.drawable.ic_about_module_active_error);
            getPreferenceScreen().removePreference(moduleEnvironment);
        }
        // 模块版本
        findPreference("about_version_module").setSummary(String.format("%s(%s)", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
        // QQ版本
        findPreference("about_version_qq").setSummary(getQQVersion());
    }

    public String getQQVersion() {
        try {
            PackageInfo qqPackageInfo = activity.getPackageManager()
                    .getPackageInfo(PACKAGE_NAME_QQ, 0);
            String versionName = qqPackageInfo.versionName;
            int versionCode = qqPackageInfo.versionCode;
            return String.format("%s(%s)", versionName, versionCode);
        } catch (Exception e) {
            e.printStackTrace();
            return "unknown";
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        switch (key) {
            case "about_version_module":
                activity.openUrl("https://github.com/zpp0196/QQPurify/releases");
                break;
            case "about_github":
                activity.openUrl("https://github.com/zpp0196/QQPurify");
                break;
            case "about_alipay":
                activity.openAlipay();
                break;
            case "about_email":
                mailContact("zpp0196@gmail.com");
                break;
            case "about_wiki":
                activity.openUrl("https://github.com/zpp0196/QQPurify/wiki");
                break;
        }
        return false;
    }

    public void mailContact(String mailAddress) {
        Uri uri = Uri.parse("mailto:" + mailAddress);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        try {
            activity.startActivity(Intent.createChooser(intent, "请选择发送邮件的应用"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
