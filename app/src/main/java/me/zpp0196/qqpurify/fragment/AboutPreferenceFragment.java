package me.zpp0196.qqpurify.fragment;

import de.psdev.licensesdialog.LicensesDialogFragment;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;
import me.zpp0196.qqpurify.BuildConfig;
import me.zpp0196.qqpurify.R;
import me.zpp0196.qqpurify.fragment.base.AbstractPreferenceFragment;

import static me.zpp0196.qqpurify.utils.Utils.getAppVersionName;

/**
 * Created by zpp0196 on 2019/2/9.
 */
public class AboutPreferenceFragment extends AbstractPreferenceFragment {

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void initPreferences() {
        super.initPreferences();
        String mBuildNum = BuildConfig.TEST ? "" : BuildConfig.BUILDNUM;
        String qBuildNum = mActivity.getIntent().getStringExtra(INTENT_BUILD_NUM);
        qBuildNum = qBuildNum == null ? "" : "." + qBuildNum;
        String moduleVersion = BuildConfig.VERSION_NAME + mBuildNum;
        String qqVersion = getAppVersionName(mActivity, PACKAGE_NAME_QQ) + qBuildNum;

        findPreference("version_module").setSummary(moduleVersion);
        findPreference("version_qq").setSummary(qqVersion);
        findPreference("licenses").setOnPreferenceClickListener(preference -> {
            showLicensesDialog();
            return false;
        });
    }

    private void showLicensesDialog() {
        Notices notices = new Notices();
        Notice commonsIo = new Notice("Apache Commons IO", "https://github.com/apache/commons-io", "Copyright 2002-2019 The Apache Software Foundation", new ApacheSoftwareLicense20());
        Notice flycoTabLayout = new Notice("FlycoTabLayout", "https://github.com/H07000223/FlycoTabLayout", "Copyright (c) 2015 H07000223", new MITLicense());
        Notice markwon = new Notice("Markwon", "https://github.com/noties/Markwon", "Copyright 2017 Dimitry Ivanov (mail@dimitryivanov.ru)", new ApacheSoftwareLicense20());
        Notice colorPicker = new Notice("ColorPicker", "https://github.com/jaredrummler/ColorPicker", "Copyright 2016 Jared Rummler\nCopyright 2015 Daniel Nilsson", new ApacheSoftwareLicense20());
        notices.addNotice(commonsIo);
        notices.addNotice(flycoTabLayout);
        notices.addNotice(markwon);
        notices.addNotice(colorPicker);
        new LicensesDialogFragment.Builder(mActivity)
                .setNotices(notices)
                .build()
                .showNow(mActivity.getSupportFragmentManager(), "licenses-dialog");
    }

    @Override
    protected int getPrefRes() {
        return R.xml.pref_about;
    }

    @Override
    public String getTabTitle() {
        return "关于";
    }

    @Override
    public String getToolbarTitle() {
        return "关于模块";
    }

    @Override
    public String getSettingGroup() {
        return SETTING_ABOUT;
    }
}
