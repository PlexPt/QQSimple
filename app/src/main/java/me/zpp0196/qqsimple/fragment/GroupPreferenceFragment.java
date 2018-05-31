package me.zpp0196.qqsimple.fragment;

import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.fragment.base.BasePreferenceFragment;

/**
 * Created by zpp0196 on 2018/3/15.
 */

public class GroupPreferenceFragment extends BasePreferenceFragment {

    @Override
    protected int setPrefsId() {
        return R.xml.prefs_group;
    }

    @Override
    public int getTitleId() {
        return R.string.nav_item_group;
    }
}
