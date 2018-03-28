package me.zpp0196.qqsimple.hook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import de.robv.android.xposed.XC_MethodHook;

/**
 * Created by zpp0196 on 2018/3/11.
 */

class OtherHook extends BaseHook {

    OtherHook(ClassLoader classLoader) {
        super(classLoader);
        closeAllAnimation();
    }

    /**
     * 完全关闭动画
     */
    private void closeAllAnimation() {
        if (getBool("close_all_animation")) {
            findAndHookMethod(Activity.class, "startActivityForResult", Intent.class, int.class, Bundle.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    ((Intent) param.args[0]).putExtra("open_chatfragment_withanim", false);
                }
            });
            findAndHookMethod(Activity.class, "startActivity", Intent.class, Bundle.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    ((Intent) param.args[0]).putExtra("open_chatfragment_withanim", false);
                }
            });
        }
    }
}
