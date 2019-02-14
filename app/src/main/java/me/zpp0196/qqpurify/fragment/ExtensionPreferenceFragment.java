package me.zpp0196.qqpurify.fragment;

import android.os.Bundle;

import me.zpp0196.qqpurify.R;

/**
 * Created by zpp0196 on 2019/2/9.
 */

public class ExtensionPreferenceFragment extends AbstractPreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindPreferenceSummaryToValue(findPreference("chat_imgBg_alphaPercent"));
        bindPreferenceSummaryToValue(findPreference("troop_rename_format"));
        bindPreferenceSummaryToValue(findPreference("extension_redirect_path"));
        bindPreferenceSummaryToValue(findPreference("extension_global_fontSize"));
    }

    @Override
    protected int getPrefRes() {
        return R.xml.pref_extension;
    }
}
