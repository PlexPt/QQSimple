package me.zpp0196.qqsimple.fragment;

import android.preference.EditTextPreference;
import android.preference.Preference;

import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.fragment.base.BasePreferenceFragment;

import static me.zpp0196.qqsimple.Common.PREFS_KEY_CHAT_TAIL;
import static me.zpp0196.qqsimple.Common.PREFS_KEY_FONT_SIZE;
import static me.zpp0196.qqsimple.Common.PREFS_KEY_IMG_ALPHA;
import static me.zpp0196.qqsimple.Common.PREFS_VALUE_CHAT_IMG_ALPHA;
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
        EditTextPreference alpha = findPreference(PREFS_KEY_IMG_ALPHA);
        alpha.setSummary(getPrefs().getString(PREFS_KEY_IMG_ALPHA, PREFS_VALUE_CHAT_IMG_ALPHA));
        alpha.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey()
                .equals(PREFS_KEY_FONT_SIZE)) {
            float size = Float.parseFloat((String) newValue);
            if (size < 12 || size > 20) {
                showToast(R.string.tip_other_size);
            }
        }
        if (preference.getKey()
                .equals(PREFS_KEY_IMG_ALPHA)) {
            float alpha = Float.parseFloat((String) newValue);
            if (alpha < 0 || alpha > 1) {
                showToast(R.string.tip_other_alpha);
                return false;
            }
        }
        preference.setSummary((CharSequence) newValue);
        return true;
    }
}
