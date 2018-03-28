package me.zpp0196.qqsimple.hook;

import android.app.Application;
import android.os.Build;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static me.zpp0196.qqsimple.Common.PACKAGE_NAME_QQ;
import static me.zpp0196.qqsimple.hook.BaseHook.getQQ_Version;

/**
 * Created by Deng on 2018/1/6.
 */

public class MainHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        new CheckActive().handleLoadPackage(loadPackageParam);
        if (!loadPackageParam.packageName.equals(PACKAGE_NAME_QQ)) return;
        findAndHookMethod("com.tencent.common.app.QFixApplicationImpl", loadPackageParam.classLoader, "isAndroidNPatchEnable", XC_MethodReplacement.returnConstant(false));
        findAndHookMethod("com.tencent.mobileqq.app.InjectUtils", loadPackageParam.classLoader, "injectExtraDexes",
                Application.class, boolean.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        final Application application = (Application) param.args[0];
                        if (Build.VERSION.SDK_INT < 21) {
                            findAndHookMethod("com.tencent.common.app.BaseApplicationImpl", application.getClassLoader(), "onCreate", new XC_MethodHook() {
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

    private void startHook(ClassLoader classLoader) {
        if (classLoader == null) {
            XposedBridge.log(String.format("%s can't get classloader", getQQ_Version()));
            return;
        }
        new RemoveImagine(classLoader);
        new MainUIHook(classLoader);
        new ChatInterfaceHook(classLoader);
        new OtherHook(classLoader);
        new MoreHook(classLoader);
    }
}
