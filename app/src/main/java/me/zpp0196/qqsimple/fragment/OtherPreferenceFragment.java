package me.zpp0196.qqsimple.fragment;

import android.preference.EditTextPreference;
import android.widget.Toast;

import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.fragment.base.BasePreferenceFragment;

import static me.zpp0196.qqsimple.Common.PREFS_KEY_FONT_SIZE;
import static me.zpp0196.qqsimple.Common.PREFS_VALUE_FONT_SIZE;

/**
 * Created by zpp0196 on 2018/3/15.
 */

public class OtherPreferenceFragment extends BasePreferenceFragment {

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
        fontSize.setOnPreferenceChangeListener((preference, newValue) -> {
            try {
                float size = Float.parseFloat((String) newValue);
                if (size < 13.92 || size > 18.0) {
                    throw new Exception("");
                }
                fontSize.setSummary((CharSequence) newValue);
            } catch (Exception e) {
                Toast.makeText(getMainActivity(), R.string.tip_other_fontSize, Toast.LENGTH_LONG)
                        .show();
                return false;
            }
            return true;
        });
    }
}
