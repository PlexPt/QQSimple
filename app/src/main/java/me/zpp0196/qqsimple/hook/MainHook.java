package me.zpp0196.qqsimple.hook;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.zpp0196.qqsimple.hook.base.BaseHook;
import me.zpp0196.qqsimple.hook.comm.Classes;
import me.zpp0196.qqsimple.hook.comm.Ids;
import me.zpp0196.qqsimple.hook.util.Util;
import me.zpp0196.qqsimple.hook.util.XPrefs;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static me.zpp0196.qqsimple.Common.PACKAGE_NAME_QQ;
import static me.zpp0196.qqsimple.hook.comm.Classes.QzonePluginProxyActivity;

/**
 * Created by Deng on 2018/1/6.
 */

public class MainHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        Util.log(getClass(), String.format("loading %s(%s)", PACKAGE_NAME_QQ, loadPackageParam.appInfo.uid));
        hookHotPatch(loadPackageParam);
        findAndHookMethod(Application.class.getName(), loadPackageParam.classLoader, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                final ClassLoader classLoader = ((Context)param.args[0]).getClassLoader();
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

    /**
     * 拦截热修复
     */
    private void hookHotPatch(XC_LoadPackage.LoadPackageParam loadPackageParam){
        findAndHookMethod("com.tencent.common.app.QFixApplicationImpl", loadPackageParam.classLoader, "isAndroidNPatchEnable", new XC_MethodHook() {
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

        List<BaseHook> hooks = new ArrayList<>();
        hooks.add(new RemoveImagine());
        hooks.add(new MainUIHook());
        hooks.add(new SidebarHook());
        hooks.add(new ChatInterfaceHook());
        hooks.add(new MoreHook());
        for (BaseHook hook : hooks) {
            hook.init();
        }

        startQzoneHook(classLoader);
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
            Util.log(t);
        }
    }
}
