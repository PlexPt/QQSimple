package me.zpp0196.qqsimple.hook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;

import static me.zpp0196.qqsimple.Common.KEY_CLOSE_ALL_ANIMATION;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SETTING_FREE_FLOW;

/**
 * Created by zpp0196 on 2018/3/11.
 */

class OtherHook extends BaseHook {

    OtherHook(ClassLoader classLoader, Class<?> id) {
        setClassLoader(classLoader);
        setId(id);
        closeAllAnimation();
        hideSettingFreeFlow();
    }

    /**
     * 完全关闭动画
     */
    private void closeAllAnimation() {
        if (getBool(KEY_CLOSE_ALL_ANIMATION)) {
            XposedHelpers.findAndHookMethod(Activity.class, "startActivityForResult", Intent.class, int.class, Bundle.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    ((Intent) param.args[0]).putExtra("open_chatfragment_withanim", false);
                }
            });
            XposedHelpers.findAndHookMethod(Activity.class, "startActivity", Intent.class, Bundle.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    ((Intent) param.args[0]).putExtra("open_chatfragment_withanim", false);
                }
            });
        }
    }

    /**
     * 隐藏设置免流量特权
     */
    private void hideSettingFreeFlow() {
        if (isMoreThan732() && getBool(KEY_HIDE_SETTING_FREE_FLOW)) {
            Class<?> QQSettingSettingActivity = getClass("com.tencent.mobileqq.activity.QQSettingSettingActivity");
            XposedHelpers.findAndHookMethod(QQSettingSettingActivity, "a", XC_MethodReplacement.returnConstant(null));
        }
    }
}
