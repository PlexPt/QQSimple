package me.zpp0196.qqsimple.fragment;

import android.preference.EditTextPreference;
import android.preference.Preference;

import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.fragment.base.BasePreferenceFragment;

import static me.zpp0196.qqsimple.Common.PREFS_KEY_CHAT_TAIL;
import static me.zpp0196.qqsimple.Common.PREFS_KEY_FONT_SIZE;
import static me.zpp0196.qqsimple.Common.PREFS_VALUE_CHAT_TAIL;
import static me.zpp0196.qqsimple.Common.PREFS_VALUE_FONT_SIZE;

/**
 * Created by zpp0196 on 2018/3/15.
 */

public class OtherPreferenceFragment extends BasePreferenceFragment implements Preference.OnPreferenceChangeListener {

    @Override
    protected int setPrefsId() {
        return R.xml.prefs_other;
    }

    @Override
    public int getTitleId() {
        return R.string.nav_item_other;
    }

    @Override
    protected void initData() {
        EditTextPreference fontSize = findPreference(PREFS_KEY_FONT_SIZE);
        fontSize.setSummary(getPrefs().getString(PREFS_KEY_FONT_SIZE, PREFS_VALUE_FONT_SIZE));
        fontSize.setOnPreferenceChangeListener(this);
        EditTextPreference tail = findPreference(PREFS_KEY_CHAT_TAIL);
        tail.setSummary(getPrefs().getString(PREFS_KEY_CHAT_TAIL, PREFS_VALUE_CHAT_TAIL));
        tail.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey()
                .equals(PREFS_KEY_FONT_SIZE)) {
            float size = Float.parseFloat((String) newValue);
            if (size < 13.92 || size > 18.0) {
                showToast(R.string.tip_other_fontSize);
                return false;
            }
        }
        preference.setSummary((CharSequence) newValue);
        return true;
    }
}
