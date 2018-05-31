package me.zpp0196.qqsimple.hook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.zpp0196.qqsimple.hook.util.XPrefs;

import static me.zpp0196.qqsimple.Common.PACKAGE_NAME_QQ;

/**
 * Created by zpp0196 on 2018/4/27 0027.
 */

public class InitHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        new CheckActive().handleLoadPackage(lpparam);
        if (!lpparam.packageName.equals(PACKAGE_NAME_QQ) || !XPrefs.isEnableModule()) {
            return;
        }
        new MainHook().handleLoadPackage(lpparam);
    }
}
