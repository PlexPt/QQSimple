package me.zpp0196.qqsimple.hook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;

/**
 * Created by zpp0196 on 2018/3/11.
 */

public class OtherHook extends BaseHook{

    public OtherHook(ClassLoader classLoader) {
        setClassLoader(classLoader);
    }

    /**
     * 完全关闭动画
     */
    public void closeAllAnimation() {
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

    /**
     * 隐藏设置免流量特权
     */
    public void hideSettingFreeFlow() {
        Class<?> QQSettingSettingActivity = getClass("com.tencent.mobileqq.activity.QQSettingSettingActivity");
        XposedHelpers.findAndHookMethod(QQSettingSettingActivity, "a", XC_MethodReplacement.returnConstant(null));
    }
}
