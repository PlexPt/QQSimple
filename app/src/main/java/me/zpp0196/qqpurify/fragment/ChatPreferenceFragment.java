package me.zpp0196.qqpurify.fragment;

import android.os.Bundle;

import me.zpp0196.qqpurify.R;

/**
 * Created by zpp0196 on 2019/2/9.
 */

public class ChatPreferenceFragment extends AbstractPreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindPreferenceSummaryToValue(findPreference("chat_grayTips_keywords"));
    }

    @Override
    protected int getPrefRes() {
        return R.xml.pref_chat;
    }
}

