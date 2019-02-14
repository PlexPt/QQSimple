package me.zpp0196.qqpurify.fragment;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import me.zpp0196.qqpurify.activity.SettingsActivity;
import me.zpp0196.qqpurify.fragment.custom.SeekBarPreferences;

/**
 * Created by zpp0196 on 2019/2/9.
 */

public abstract class AbstractPreferenceFragment extends PreferenceFragment {

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private final Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = (preference, value) -> {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            // Set the summary to reflect the new value.
            preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

        } else {
            // For all other preferences, set the summary to the value's
            // simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    };
    protected SettingsActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (SettingsActivity) getActivity();
        addPreferencesFromResource(getPrefRes());
    }

    protected abstract int getPrefRes();

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    protected void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        Object newValue;
        if (preference instanceof SeekBarPreferences) {
            newValue = PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                    .getInt(preference.getKey(), ((SeekBarPreferences) preference).getDefaultValue());
        } else {
            newValue = PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                    .getString(preference.getKey(), "");
        }
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, newValue);
    }
}
