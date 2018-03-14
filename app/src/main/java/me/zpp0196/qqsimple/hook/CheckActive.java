package me.zpp0196.qqsimple.hook;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.zpp0196.qqsimple.BuildConfig;
import me.zpp0196.qqsimple.SettingActivity;

/**
 * Created by Deng on 2018/2/12.
 */

public class CheckActive {
    public static void isActive(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (!loadPackageParam.packageName.equals(BuildConfig.APPLICATION_ID)) return;
        XposedHelpers.findAndHookMethod(SettingActivity.class.getName(), loadPackageParam.classLoader, "isModuleActive", XC_MethodReplacement.returnConstant(true));
    }
}
