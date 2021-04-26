package me.zpp0196.qqpurify.hook;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.zpp0196.library.xposed.XC_MemberHook;
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
 * Created by zpp0196 on 2018/4/27.
 */
public class InitializeHook implements Constants, IXposedHookLoadPackage, SettingUtils.ISetting {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {

        if (!lpparam.packageName.equals(PACKAGE_NAME_QQ) || !lpparam.isFirstApplication) {
            return;
        }

        XMethodHook.create(Application.class).method("attach").hook(new XC_MemberHook() {
            @Override
            protected void onAfterHooked(@NonNull XC_MemberHook.MemberHookParam param) {
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
        long versionCode = Utils.getAppVersionCode(context, PACKAGE_NAME_QQ);
        log("Loading processName: " + processName + ", versionCode: " + versionCode);

        QQConfigUtils.init(context, versionCode);
        XReflect.setConvertCallback(new XReflect.XConvertClassCallback() {
            @Override
            protected Class<?> findClass(String className, ClassLoader classLoader) {
                return super.findClass(QQConfigUtils.findClass(className), classLoader);
            }
        });

        QQDialogUtils.init(context.getClassLoader());
        try {
            Context mContext = Utils.createContext(context);
            Setting.init(mContext);
            XLog.setLogCallback(XLogUtils.getInstance(mContext));
        } catch (Throwable th) {
            QQDialogUtils.addError(th);
        }
    }

    private boolean isHookProcess(String processName) {
        boolean isPeak = processName.endsWith("peak");
        if (!PACKAGE_NAME_QQ.equals(processName) && !isPeak) {
            return false;
        }

        Setting setting = Setting.getInstance(this);
        if (setting.get(KEY_DISABLE_MODULE, false)) {
            log("module disabled");
            return false;
        }

        Setting extension = Setting.getInstance(SETTING_EXTENSION);
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
            if (simpleClassName.toLowerCase().contains(getSettingGroup())) {
                continue;
            }

            Class<?> hookClass;
            try {
                hookClass = Class.forName(getPackage() + simpleClassName);
            } catch (ClassNotFoundException e) {
                XLog.e(e);
                QQDialogUtils.addError(e);
                continue;
            }

            long hookInitStartTime = System.currentTimeMillis();
            log("================================================");
            log(String.format("Loading %s start", simpleClassName));

            try {
                ((BaseHook) hookClass.getConstructor(Context.class).newInstance(context)).init();
            } catch (Exception e) {
                XLog.e(e);
                log(String.format("Loading %s failure", simpleClassName));
                QQDialogUtils.addError(e);
            }

            log(String.format("Loading %s cost: %s ms", simpleClassName,
                    System.currentTimeMillis() - hookInitStartTime));
            log("================================================");
        }
        new EarlierSupport(context).init();
        log(String.format("Loading all the hooks cost: %s ms", System.currentTimeMillis() - allHooksInitStartTime));
    }

    private void log(String message) {
        XLogUtils.log(this, message);
    }

    private String getPackage() {
        Class<?> clazz = getClass();
        return clazz.getName().replace(clazz.getSimpleName(), "");
    }

    @Override
    public String getSettingGroup() {
        return SETTING_SETTING;
    }
}
