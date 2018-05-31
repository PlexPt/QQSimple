package me.zpp0196.qqsimple.hook;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.zpp0196.qqsimple.hook.base.BaseHook;
import me.zpp0196.qqsimple.hook.comm.Classes;
import me.zpp0196.qqsimple.hook.comm.Ids;
import me.zpp0196.qqsimple.hook.util.HookUtil;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static me.zpp0196.qqsimple.Common.PACKAGE_NAME_QQ;
import static me.zpp0196.qqsimple.hook.comm.Classes.QzonePluginProxyActivity;
import static me.zpp0196.qqsimple.hook.util.HookUtil.log;

/**
 * Created by Deng on 2018/1/6.
 */

public class MainHook implements IXposedHookLoadPackage {

    private XC_LoadPackage.LoadPackageParam lpparam;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam)
            throws Throwable {
        this.lpparam = loadPackageParam;
        findAndHookMethod("com.tencent.common.app.QFixApplicationImpl", lpparam.classLoader, "isAndroidNPatchEnable", XC_MethodReplacement.returnConstant(false));
        findAndHookMethod(Application.class.getName(), loadPackageParam.classLoader, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                final ClassLoader classLoader = ((Context) param.args[0]).getClassLoader();
                if (Build.VERSION.SDK_INT < 21) {
                    findAndHookMethod("com.tencent.common.app.BaseApplicationImpl", classLoader, "onCreate", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            startHook(classLoader);
                        }
                    });
                } else {
                    startHook(classLoader);
                }
            }
        });
    }

    private void startHook(ClassLoader classLoader) {
        if (classLoader == null) {
            log(getClass(), String.format("%s Can't get ClassLoader!", HookUtil.getQQVersionName()));
            return;
        }

        Classes.initClass(classLoader);
        Ids.init();

        log(getClass(), String.format("loading %s(%s)", lpparam.processName, lpparam.appInfo.uid));

        if (lpparam.processName.contains("qzone")) {
            startQzoneHook(classLoader);
        } else if (!lpparam.processName.equals(PACKAGE_NAME_QQ)) {
            return;
        }

        List<BaseHook> hooks = new ArrayList<>();
        hooks.add(new RemoveImagine());
        hooks.add(new MainUIHook());
        hooks.add(new SidebarHook());
        hooks.add(new ChatInterfaceHook());
        hooks.add(new OtherHook());
        for (BaseHook hook : hooks) {
            hook.init();
        }
    }

    private void startQzoneHook(ClassLoader classLoader) {
        try {
            findAndHookMethod(QzonePluginProxyActivity, "a", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    new QZoneHook(classLoader, (ClassLoader) param.getResult()).init();
                }
            });
        } catch (Throwable t) {
            log(t);
        }
    }
}
