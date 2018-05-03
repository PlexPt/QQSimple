package me.zpp0196.qqsimple.hook;

import android.util.SparseArray;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static me.zpp0196.qqsimple.Common.PACKAGE_NAME_QQ;

/**
 * Created by zpp0196 on 2018/4/27 0027.
 */

public class InitHook implements IXposedHookLoadPackage {

    private SparseArray<MainHook> hookSparseArray;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        new CheckActive().handleLoadPackage(lpparam);
        if (!lpparam.packageName.equals(PACKAGE_NAME_QQ)) return;
        if (hookSparseArray == null) hookSparseArray = new SparseArray<>();
        int uid = lpparam.appInfo.uid;
        MainHook mainHook = hookSparseArray.indexOfKey(uid) != -1 ? hookSparseArray.get(uid) : null;
        if(mainHook == null) hookSparseArray.put(uid, new MainHook());
        hookSparseArray.get(uid).handleLoadPackage(lpparam);
    }
}
