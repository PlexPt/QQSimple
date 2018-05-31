package me.zpp0196.qqsimple.hook;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.zpp0196.qqsimple.BuildConfig;
import me.zpp0196.qqsimple.activity.MainActivity;

/**
 * Created by Deng on 2018/2/12.
 */

class CheckActive {

    @SuppressWarnings ("all")
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!loadPackageParam.packageName.equals(BuildConfig.APPLICATION_ID)) {
            return;
        }
        XposedHelpers.findAndHookMethod(MainActivity.class.getName(), loadPackageParam.classLoader, "isModuleActive", XC_MethodReplacement.returnConstant(true));
    }
}
