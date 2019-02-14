package me.zpp0196.qqpurify.hook;

import android.app.Application;
import android.content.Context;
import android.os.Process;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.zpp0196.qqpurify.BuildConfig;
import me.zpp0196.qqpurify.activity.SettingsActivity;
import me.zpp0196.qqpurify.utils.Constants;
import me.zpp0196.qqpurify.utils.XPrefUtils;

/**
 * Created by zpp0196 on 2018/4/27 0027.
 */

public class InitHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        makeQQDebuggable(lpparam);
        hookModuleActive(lpparam);
        if (XPrefUtils.getPref()
                    .getBoolean("module_switch", true) &&
            lpparam.packageName.equals(Constants.PACKAGE_NAME_QQ) &&
            !lpparam.processName.endsWith(":qzone")) {
            XposedHelpers.findAndHookMethod("com.tencent.common.app.QFixApplicationImpl", lpparam.classLoader, "isAndroidNPatchEnable", XC_MethodReplacement.returnConstant(false));
            XposedHelpers.findAndHookMethod("com.tencent.mobileqq.app.InjectUtils", lpparam.classLoader, "injectExtraDexes", Application.class, boolean.class, new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    startHookQQ((Context) param.args[0]);
                }
            });
        }
    }

    private void makeQQDebuggable(XC_LoadPackage.LoadPackageParam lpparam) {
        if (BuildConfig.DEBUG) {
            if (lpparam.packageName.equals("android") && lpparam.processName.equals("android")) {
                XposedBridge.hookAllMethods(Process.class, "start", new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (param.args[11].toString()
                                .contains(Constants.PACKAGE_NAME_QQ)) {
                            param.args[5] = 1;
                        }
                    }
                });
            }
        }
    }

    private void hookModuleActive(XC_LoadPackage.LoadPackageParam lpparam) {
        if (lpparam.packageName.equals(BuildConfig.APPLICATION_ID)) {
            XposedHelpers.findAndHookMethod(SettingsActivity.class.getName(), lpparam.classLoader, "isModuleActive", XC_MethodReplacement.returnConstant(true));
        }
    }

    private void startHookQQ(Context context) throws Throwable {
        AbstractHook.setClassLoader(context.getClassLoader());
        List<AbstractHook> hooks = new ArrayList<>();
        hooks.add(new SidebarHook());
        hooks.add(new ConversationHook());
        hooks.add(new ContactsHook());
        hooks.add(new LebaHook());
        hooks.add(new ChatHook());
        hooks.add(new TroopHook());
        hooks.add(new ExtensionHook(context));
        for (AbstractHook hook : hooks) {
            hook.init();
        }
    }
}
