package me.zpp0196.qqsimple.fragment;

import android.preference.CheckBoxPreference;
import android.preference.Preference;

import java.util.ArrayList;

import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.fragment.base.BaseFragment;

/**
 * Created by zpp0196 on 2018/4/1.
 */

public class QZoneFragment extends BaseFragment implements Preference.OnPreferenceChangeListener {

    CheckBoxPreference mood;
    CheckBoxPreference album;
    CheckBoxPreference shoot;
    CheckBoxPreference signIn;
    CheckBoxPreference redPacket;
    CheckBoxPreference live;

    @Override
    protected int setPrefs() {
        return R.xml.prefs_qzone;
    }

    @Override
    protected void initData() {
        mood = (CheckBoxPreference) findPreference("hide_qzone_plus_mood");
        album = (CheckBoxPreference) findPreference("hide_qzone_plus_album");
        shoot = (CheckBoxPreference) findPreference("hide_qzone_plus_shoot");
        signIn = (CheckBoxPreference) findPreference("hide_qzone_plus_signIn");
        redPacket = (CheckBoxPreference) findPreference("hide_qzone_plus_redPacket");
        live = (CheckBoxPreference) findPreference("hide_qzone_plus_live");
        mood.setOnPreferenceChangeListener(this);
        album.setOnPreferenceChangeListener(this);
        shoot.setOnPreferenceChangeListener(this);
        signIn.setOnPreferenceChangeListener(this);
        redPacket.setOnPreferenceChangeListener(this);
        live.setOnPreferenceChangeListener(this);
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
                showDialog("该分组里面虽然提供了 6 个开关，但却只能开启 3 个，多开一个都会导致点击菜单时空间闪退，请根据自己实际情况选择性开启！");
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return !(boolean) newValue || !(preference == mood || preference == album || preference == shoot || preference == signIn || preference == redPacket || preference == live) || isMoreThree();
    }
}
