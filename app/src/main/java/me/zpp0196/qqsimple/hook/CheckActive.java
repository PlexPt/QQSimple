package me.zpp0196.qqsimple.hook;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.zpp0196.qqsimple.BuildConfig;
import me.zpp0196.qqsimple.Common;

/**
 * Created by Deng on 2018/2/12.
 */

class CheckActive {
    void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!loadPackageParam.packageName.equals(BuildConfig.APPLICATION_ID)) return;
        XposedHelpers.findAndHookMethod(Common.class.getName(), loadPackageParam.classLoader, "isModuleActive", XC_MethodReplacement.returnConstant(true));
    }
}
