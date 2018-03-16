package me.zpp0196.qqsimple.hook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static me.zpp0196.qqsimple.hook.MainHook.getQQ_Version;

/**
 * Created by zpp0196 on 2018/3/11.
 */

public class OtherHook {

    private ClassLoader classLoader;

    public OtherHook(ClassLoader classLoader) {
        this.classLoader = classLoader;
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
     * 隐藏启动图广告
     */
    public void hideSplashAd() {
        XposedHelpers.findAndHookConstructor(File.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (param.args[0].toString().contains("com.tencent.mobileqq") && param.args[0].toString().contains("splashpic")) {
                    param.args[0] = new File(Environment.getExternalStorageDirectory(), "tencent").getAbsolutePath();
                }
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

    private Class<?> getClass(String className) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            XposedBridge.log(String.format("%s can not get className: %s", getQQ_Version(), className));
        }
        return null;
    }
}
