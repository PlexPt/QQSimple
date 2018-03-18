package me.zpp0196.qqsimple.hook;

import android.app.Application;
import android.os.Build;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static me.zpp0196.qqsimple.Common.KEY_CLOSE_ALL;
import static me.zpp0196.qqsimple.Common.PACKAGE_NAME_QQ;

/**
 * Created by Deng on 2018/1/6.
 */

public class MainHook extends BaseHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        new CheckActive().handleLoadPackage(loadPackageParam);
        if (!loadPackageParam.packageName.equals(PACKAGE_NAME_QQ)) return;
        if (getBool(KEY_CLOSE_ALL)) return;
        XposedHelpers.findAndHookMethod("com.tencent.mobileqq.app.InjectUtils", loadPackageParam.classLoader, "injectExtraDexes",
                Application.class, boolean.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        final Application application = (Application) param.args[0];
                        if (Build.VERSION.SDK_INT < 21) {
                            XposedHelpers.findAndHookMethod("com.tencent.common.app.BaseApplicationImpl", application.getClassLoader(), "onCreate", new XC_MethodHook() {
                                @Override
                                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                    startHook(application.getClassLoader());
                                }
                            });
                        } else {
                            startHook(application.getClassLoader());
                        }
                    }
                });
    }

    private void startHook(final ClassLoader classLoader) {
        if (classLoader == null) {
            XposedBridge.log(String.format("%s can not get classloader", getQQ_Version()));
            return;
        }

        Class drawable = XposedHelpers.findClass("com.tencent.mobileqq.R$drawable", classLoader);
        Class id = XposedHelpers.findClass("com.tencent.mobileqq.R$id", classLoader);

        if (drawable == null || id == null) {
            return;
        }

        new RemoveImagine(id, drawable);
        new MainUIHook(classLoader, id);
        new ChatInterfaceHook(classLoader);
        new OtherHook(classLoader, id);
        new MoreHook(classLoader);
    }
}
