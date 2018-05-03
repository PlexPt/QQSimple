package me.zpp0196.qqsimple.hook;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.zpp0196.qqsimple.hook.comm.Classes;
import me.zpp0196.qqsimple.hook.comm.Ids;
import me.zpp0196.qqsimple.hook.util.Util;
import me.zpp0196.qqsimple.hook.util.XPrefs;

import static me.zpp0196.qqsimple.Common.PACKAGE_NAME_QQ;
import static me.zpp0196.qqsimple.hook.comm.Classes.PluginStatic;

/**
 * Created by Deng on 2018/1/6.
 */

public class MainHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        Util.log(getClass(), "loading: " + PACKAGE_NAME_QQ);
        if(XPrefs.isCloseAll()) return;
        hookHotPatch(loadPackageParam);
        XposedHelpers.findAndHookMethod("com.tencent.mobileqq.app.InjectUtils", loadPackageParam.classLoader, "injectExtraDexes",
                Application.class, boolean.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        final ClassLoader classLoader = ((Application) param.args[0]).getClassLoader();
                        if (Build.VERSION.SDK_INT < 21) {
                            XposedHelpers.findAndHookMethod("com.tencent.common.app.BaseApplicationImpl", classLoader, "onCreate", new XC_MethodHook() {
                                @Override
                                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                    startHook(classLoader);
                                }
                            });
                        } else {
                            startHook(classLoader);
                        }
                        startQzoneHook(classLoader);
                    }
                });
    }

    /**
     * 拦截热修复
     */
    private void hookHotPatch(XC_LoadPackage.LoadPackageParam loadPackageParam){
        XposedHelpers.findAndHookMethod("com.tencent.common.app.QFixApplicationImpl", loadPackageParam.classLoader, "isAndroidNPatchEnable", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if(XPrefs.getBoolean("hook_hotpatch", false)){
                    param.setResult(false);
                }
            }
        });
    }

    private void startHook(ClassLoader classLoader) {
        if (classLoader == null) {
            Util.log(getClass(), String.format("%s Can't get ClassLoader!", Util.getQQVersion()));
            return;
        }
        Classes.initClass(classLoader);
        Ids.init();
        new RemoveImagine();
        new MainUIHook();
        new ChatInterfaceHook();
        new OtherHook();
        new MoreHook();
    }

    private void startQzoneHook(ClassLoader classLoader) {
        try {
            XposedHelpers.findAndHookMethod(PluginStatic, "a", Context.class, String.class, String.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    if (param.args[1].toString().contains("qzone_plugin")) {
                        new QZoneHook(classLoader, (ClassLoader) param.getResult());
                    }
                }
            });
        } catch (Throwable t) {
            Util.log(t);
        }
    }
}
