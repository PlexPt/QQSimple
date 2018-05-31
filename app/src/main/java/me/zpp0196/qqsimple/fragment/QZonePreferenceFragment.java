package me.zpp0196.qqsimple.fragment;

import android.preference.CheckBoxPreference;
import android.preference.Preference;

import java.util.ArrayList;

import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.fragment.base.BasePreferenceFragment;

/**
 * Created by zpp0196 on 2018/4/1.
 */

public class QZonePreferenceFragment extends BasePreferenceFragment implements Preference.OnPreferenceChangeListener {

    private CheckBoxPreference mood;
    private CheckBoxPreference album;
    private CheckBoxPreference shoot;
    private CheckBoxPreference signIn;
    private CheckBoxPreference redPacket;
    private CheckBoxPreference live;
    private CheckBoxPreference hideAd;

    @Override
    protected int setPrefsId() {
        return R.xml.prefs_qzone;
    }

    @Override
    public int getTitleId() {
        return R.string.nav_item_qzone;
    }

    @Override
    protected void initData() {
        mood = findPreference("hide_qzone_plus_mood");
        album = findPreference("hide_qzone_plus_album");
        shoot = findPreference("hide_qzone_plus_shoot");
        signIn = findPreference("hide_qzone_plus_signIn");
        redPacket = findPreference("hide_qzone_plus_redPacket");
        live = findPreference("hide_qzone_plus_live");
        hideAd = findPreference("hide_qzone_AD");
        mood.setOnPreferenceChangeListener(this);
        album.setOnPreferenceChangeListener(this);
        shoot.setOnPreferenceChangeListener(this);
        signIn.setOnPreferenceChangeListener(this);
        redPacket.setOnPreferenceChangeListener(this);
        live.setOnPreferenceChangeListener(this);
        hideAd.setOnPreferenceChangeListener(this);
    }

    private boolean isMoreThree() {
        ArrayList<Boolean> list = new ArrayList<>();
        list.add(mood.isChecked());
        list.add(album.isChecked());
        list.add(shoot.isChecked());
        list.add(signIn.isChecked());
        list.add(redPacket.isChecked());
        list.add(live.isChecked());
        int count = 0;
        for (boolean b : list) {
            if (b) {
                count++;
            }
            if (count == 3) {
                getMainActivity().showMsgDialog(R.string.title_explain, R.string.tip_qzone_menu);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean nValue = (boolean) newValue;
        if (preference == hideAd && nValue) {
            getMainActivity().showMsgDialog(R.string.title_explain, R.string.tip_qzone_ad);
            return true;
        }
        return !nValue || !(preference == mood || preference == album || preference == shoot ||
                            preference == signIn || preference == redPacket ||
                            preference == live) || isMoreThree();
    }
}
