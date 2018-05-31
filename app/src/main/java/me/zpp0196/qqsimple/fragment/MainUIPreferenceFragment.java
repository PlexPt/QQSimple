package me.zpp0196.qqsimple.fragment;

import android.preference.CheckBoxPreference;
import android.preference.Preference;

import java.util.ArrayList;

import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.fragment.base.BasePreferenceFragment;

/**
 * Created by zpp0196 on 2018/3/15.
 */

public class MainUIPreferenceFragment extends BasePreferenceFragment implements Preference.OnPreferenceChangeListener {

    private CheckBoxPreference multiChat;
    private CheckBoxPreference add;
    private CheckBoxPreference sweep;
    private CheckBoxPreference face2face;
    private CheckBoxPreference pay;
    private CheckBoxPreference shoot;
    private CheckBoxPreference videoDance;

    @Override
    protected int setPrefsId() {
        return R.xml.prefs_mainui;
    }

    @Override
    public int getTitleId() {
        return R.string.nav_item_mainui;
    }

    @Override
    protected void initData() {
        multiChat = findPreference("hide_popup_multiChat");
        add = findPreference("hide_popup_add");
        sweep = findPreference("hide_popup_sweep");
        face2face = findPreference("hide_popup_face2face");
        pay = findPreference("hide_popup_pay");
        shoot = findPreference("hide_popup_shoot");
        videoDance = findPreference("hide_popup_videoDance");
        multiChat.setOnPreferenceChangeListener(this);
        add.setOnPreferenceChangeListener(this);
        sweep.setOnPreferenceChangeListener(this);
        face2face.setOnPreferenceChangeListener(this);
        pay.setOnPreferenceChangeListener(this);
        shoot.setOnPreferenceChangeListener(this);
        videoDance.setOnPreferenceChangeListener(this);
    }

    private boolean isMoreThree() {
        ArrayList<Boolean> list = new ArrayList<>();
        list.add(multiChat.isChecked());
        list.add(add.isChecked());
        list.add(sweep.isChecked());
        list.add(face2face.isChecked());
        list.add(pay.isChecked());
        list.add(shoot.isChecked());
        list.add(videoDance.isChecked());
        int count = 0;
        for (boolean b : list) {
            if (b) {
                count++;
            }
            if (count == list.size() - 1) {
                getMainActivity().showMsgDialog(R.string.title_explain, R.string.tip_popup_menu);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return !(boolean) newValue ||
               !(preference == multiChat || preference == add || preference == sweep ||
                 preference == face2face || preference == pay || preference == shoot ||
                 preference == videoDance) || isMoreThree();
    }
}
