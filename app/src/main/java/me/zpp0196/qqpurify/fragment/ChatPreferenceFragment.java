package me.zpp0196.qqpurify.fragment;

import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceGroup;

import java.util.Set;

import me.zpp0196.qqpurify.R;

/**
 * Created by zpp0196 on 2019/2/9.
 */

public class ChatPreferenceFragment extends AbstractPreferenceFragment {

    private MultiSelectListPreference tailListPref;
    private Set<String> tailSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindPreferenceSummaryToValue(findPreference("chat_grayTips_keywords"));
        tailListPref = (MultiSelectListPreference) findPreference("chat_tail_list");
        tailSet = tailListPref.getValues();
        findPreference("chat_tail_add").setOnPreferenceChangeListener((preference, newValue) -> {
            String tail = String.valueOf(newValue);
            if (!tailSet.contains(tail)) {
                tailSet.add(tail);
                getPreferenceManager().getSharedPreferences()
                        .edit()
                        .putStringSet(tailListPref.getKey(), tailSet)
                        .apply();
            }
            setTailListPreference();
            return false;
        });
        setTailListPreference();
    }

    private void setTailListPreference() {
        PreferenceGroup otherCategory = (PreferenceGroup) findPreference("chat_category_other");
        if (tailSet.isEmpty()) {
            otherCategory.removePreference(tailListPref);
            return;
        }
        String[] tailKeywordsEntries = tailSet.toArray(new String[tailSet.size()]);
        tailListPref.setEntries(tailKeywordsEntries);
        tailListPref.setEntryValues(tailKeywordsEntries);
        otherCategory.addPreference(tailListPref);
    }

    @Override
    protected int getPrefRes() {
        return R.xml.pref_chat;
    }
}

