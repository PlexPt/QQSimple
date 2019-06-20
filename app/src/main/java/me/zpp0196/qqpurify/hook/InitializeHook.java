package me.zpp0196.qqpurify.hook;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.zpp0196.library.xposed.XLog;
import me.zpp0196.library.xposed.XMethodHook;
import me.zpp0196.library.xposed.XReflect;
import me.zpp0196.qqpurify.hook.base.BaseHook;
import me.zpp0196.qqpurify.hook.earlier.EarlierSupport;
import me.zpp0196.qqpurify.hook.utils.QQConfigUtils;
import me.zpp0196.qqpurify.hook.utils.QQDialogUtils;
import me.zpp0196.qqpurify.hook.utils.XLogUtils;
import me.zpp0196.qqpurify.utils.Constants;
import me.zpp0196.qqpurify.utils.Setting;
import me.zpp0196.qqpurify.utils.SettingUtils;
import me.zpp0196.qqpurify.utils.Utils;

/**
 * Created by zpp0196 on 2018/4/27 0027.
 */
public class InitializeHook implements Constants, IXposedHookLoadPackage, SettingUtils.ISetting {

    private static final String TAG = APP_NAME;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {

        if (!lpparam.packageName.equals(PACKAGE_NAME_QQ) || !lpparam.isFirstApplication) {
            return;
        }

        XMethodHook.create(Application.class).method("attach").hook(new XMethodHook.Callback() {
            @Override
            protected void after(XMethodHook.MethodParam param) {
                Context context = param.args(0);
                String processName = Utils.getProcessName();
                init(context, processName);
                if (!isHookProcess(processName)) {
                    return;
                }
                initHook(context, processName);
            }
        });
    }

    private void init(Context context, String processName) {
        XLog.setTAG(TAG);
        long versionCode = Utils.getAppVersionCode(context, PACKAGE_NAME_QQ);
        QQConfigUtils.init(context, versionCode);
        XReflect.setConvertCallback(new XReflect.XConvertClassCallback() {
            @Override
            protected Class<?> findClass(String className, ClassLoader classLoader) {
                return super.findClass(QQConfigUtils.findClass(className), classLoader);
            }
        });

        QQDialogUtils.init(context.getClassLoader());

        try {
            Setting.init();
        } catch (Throwable th) {
            QQDialogUtils.addError(th);
        }
        Setting setting = Setting.getInstance(this);
        XLog.LogLevel logLevel = XLog.LogLevel.valueOf(setting.get(KEY_LOG_LEVEL, XLog.LogLevel.NONE.name()));
        XLog.setLevel(logLevel);
        if (logLevel != XLog.LogLevel.NONE) {
            XLog.setLogCallback(XLogUtils.getInstance(setting.get(KEY_LOG_COUNT, 10)));
        }
        Log.d(TAG, "logLevel: " + logLevel.name());
        XLog.wtf(TAG, "logLevel: " + logLevel.name());
        XLog.d(TAG, "Loading processName: " + processName + ", versionCode: " + versionCode);
    }

    private boolean isHookProcess(String processName) {
        boolean isPeak = processName.endsWith("peak");
        if (!PACKAGE_NAME_QQ.equals(processName) && !isPeak) {
            return false;
        }

        Setting setting = Setting.getInstance(this);
        if (setting.get(KEY_DISABLE_MODULE, false)) {
            XLog.d(TAG, "module disabled");
            return false;
        }

        Setting extension = Setting.getInstance(SettingGroup.extension);
        return !isPeak || extension.get(KEY_TRANSPARENT_IMG_BG, false);
    }

    private void initHook(Context context, String processName) {
        if (processName.endsWith("peak")) {
            new ExtensionHook(context).transparentImgBg();
            return;
        }
        List<String> hooksClassName = Setting.getGroups("Hook");
        long allHooksInitStartTime = System.currentTimeMillis();
        for (String simpleClassName : hooksClassName) {
            // 排除setting
            if (simpleClassName.toLowerCase().contains(getSettingGroup().name())) {
                continue;
            }
            // 排除update
            if (simpleClassName.toLowerCase().contains(SettingGroup.update.name())) {
                continue;
            }

            Class<?> hookClass;
            try {
                hookClass = Class.forName(getPackage() + simpleClassName);
            } catch (ClassNotFoundException e) {
                XLog.e(TAG, e);
                QQDialogUtils.addError(e);
                continue;
            }

            long hookInitStartTime = System.currentTimeMillis();
            XLog.d(TAG, "================================================");
            XLog.d(TAG, String.format("Loading %s start", simpleClassName));

            try {
                ((BaseHook) hookClass.getConstructor(Context.class).newInstance(context)).init();
            } catch (Exception e) {
                XLog.e(TAG, e);
                XLog.d(TAG, String.format("Loading %s failure", simpleClassName));
                QQDialogUtils.addError(e);
            }

            XLog.d(TAG, String.format("Loading %s cost: %s ms", simpleClassName,
                    System.currentTimeMillis() - hookInitStartTime));
            XLog.d(TAG, "================================================");
        }
        new EarlierSupport(context).init();
        XLog.d(TAG, String.format("Loading all the hooks cost: %s ms",
                System.currentTimeMillis() - allHooksInitStartTime));
    }

    private String getPackage() {
        Class<?> clazz = getClass();
        return clazz.getName().replace(clazz.getSimpleName(), "");
    }

    @Override
    public SettingGroup getSettingGroup() {
        return SettingGroup.setting;
    }
}
