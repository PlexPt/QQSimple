package me.zpp0196.qqsimple.fragment;

import android.preference.CheckBoxPreference;
import android.preference.Preference;

import java.util.ArrayList;

import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.fragment.base.BaseFragment;

/**
 * Created by zpp0196 on 2018/3/15.
 */

public class MainUIFragment extends BaseFragment implements Preference.OnPreferenceChangeListener {

    CheckBoxPreference multiChat;
    CheckBoxPreference add;
    CheckBoxPreference sweep;
    CheckBoxPreference face2face;
    CheckBoxPreference pay;
    CheckBoxPreference shoot;
    CheckBoxPreference videoDance;

    @Override
    protected int setPrefs() {
        return R.xml.prefs_mainui;
    }

    @Override
    protected void initData() {
        multiChat = (CheckBoxPreference) findPreference("hide_popup_multiChat");
        add = (CheckBoxPreference) findPreference("hide_popup_add");
        sweep = (CheckBoxPreference) findPreference("hide_popup_sweep");
        face2face = (CheckBoxPreference) findPreference("hide_popup_face2face");
        pay = (CheckBoxPreference) findPreference("hide_popup_pay");
        shoot = (CheckBoxPreference) findPreference("hide_popup_shoot");
        videoDance = (CheckBoxPreference) findPreference("hide_popup_videoDance");
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
                showDialog("该分组里面虽然提供了 7 个开关，但至少需要关闭 1 个，如果全部开启会导致点击菜单时 QQ 闪退，如果你想全部隐藏可以直接开启上面的「隐藏快捷入口」，请根据自己实际情况选择性开启！");
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return !(boolean) newValue || !(preference == multiChat || preference == add || preference == sweep || preference == face2face || preference == pay || preference == shoot || preference == videoDance) || isMoreThree();
    }
}
