package me.zpp0196.qqpurify.demo;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import proxy.android.app.IApplication;
import proxy.com.tencent.mobileqq.activity.IConversation;
import proxy.com.tencent.mobileqq.app.IQQAppInterface;
import proxy.com.tencent.mobileqq.troop.honor.ITroopHonorConfig;
import proxy.com.tencent.mobileqq.troop.honor.ITroopHonorManager;
import proxy.mqq.IManager;
import proxy.mqq.app.IAppRuntime;
import reflectx.android.compat.Function;
import reflectx.mapping.ProxyClassMapping;

import static reflectx.ProxyFactory.proxy;

public class DemoEntry implements IXposedHookLoadPackage {

    private IQQAppInterface mAppInterface;
    private AtomicBoolean mTroopHonorConfigFlag = new AtomicBoolean(false);

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!lpparam.processName.equals("com.tencent.mobileqq")) {
            return;
        }
        ProxyClassMapping.init();

        XposedBridge.hookMethod(proxy(IApplication.class).attach(), new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Context context = (Context) param.args[0];
                ReflectxInitializer.init(context);

                XposedBridge.hookMethod(proxy(IAppRuntime.class).init(), new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        mAppInterface = proxy(IAppRuntime.class, param.thisObject)
                                .cast(IQQAppInterface.class);
                        if (mAppInterface == null) {
                            return;
                        }
                        closeMiniAppSwitch();
                        closeTroopHonor();
                    }
                });
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void closeMiniAppSwitch() {
        XposedBridge.hookMethod(proxy(IConversation.class).initMiniAppEntryLayout(),
                XC_MethodReplacement.returnConstant(null));
    }

    private void closeTroopHonor() {
        Function<ITroopHonorConfig, Void> resetConfig = config -> {
            Log.i("TroopHonor.manager", "config: " + config);
            config.setSupport(false).setHonorMap(new SparseArray());
            return null;
        };
        IManager manager = mAppInterface.getManager(346);
        if (manager == null) {
            return;
        }
        ITroopHonorManager honorManager = manager.cast(ITroopHonorManager.class);
        if (honorManager == null) {
            return;
        }
        ITroopHonorConfig config = honorManager.config();
        if (config == null) {
            return;
        }
        resetConfig.apply(config);
        if (mTroopHonorConfigFlag.compareAndSet(false, true)) {
            Method method = config.parseConfig();
            if (method == null) {
                return;
            }
            XposedBridge.hookMethod(method, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object result = param.getResult();
                    if (result == null) {
                        return;
                    }
                    resetConfig.apply(proxy(ITroopHonorConfig.class, result));
                }
            });
        }
    }
}
