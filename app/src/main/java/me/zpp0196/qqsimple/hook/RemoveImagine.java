package me.zpp0196.qqsimple.hook;

import me.zpp0196.qqsimple.hook.base.BaseHook;

import static me.zpp0196.qqsimple.Common.PREFS_KEY_HIDE_SOME_RED_DOT;
import static me.zpp0196.qqsimple.hook.util.HookUtil.isMoreThan735;

/**
 * Created by Deng on 2018/2/16.
 */

class RemoveImagine extends BaseHook {

    @Override
    public void init() {
        hideRedDot();
    }

    /**
     * 隐藏小红点
     */
    private void hideRedDot() {
        if (!getBool(PREFS_KEY_HIDE_SOME_RED_DOT)) {
            return;
        }
        hideDrawable("skin_tips_dot");
        hideDrawable("skin_tips_new");
        if (isMoreThan735()) {
            hideDrawable("shortvideo_redbag_outicon");
        }
    }
}
