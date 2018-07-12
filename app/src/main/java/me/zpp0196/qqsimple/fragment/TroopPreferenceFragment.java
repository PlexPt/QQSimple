package me.zpp0196.qqsimple.fragment;

import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.fragment.base.BasePreferenceFragment;

/**
 * Created by zpp0196 on 2018/3/15.
 */

public class TroopPreferenceFragment extends BasePreferenceFragment {

    @Override
    protected int setPrefsId() {
        return R.xml.prefs_troop;
    }

    @Override
    public int getTitleId() {
        return R.string.nav_item_group;
    }

    @Override
    protected void initData() {
        findPreference("hide_troopTip_all").setOnPreferenceChangeListener((preference, newValue) -> {
            if ((boolean) newValue) {
                getMainActivity().showMsgDialog(R.string.title_explain, R.string.tip_troop_tip);
            }
            return true;
        });
    }
}
