package me.zpp0196.qqpurify.hook.utils;

import android.app.Activity;
import android.os.Process;

import java.util.ArrayList;
import java.util.List;

import me.zpp0196.library.xposed.XMethod;
import me.zpp0196.library.xposed.XMethodHook;
import me.zpp0196.qqpurify.BuildConfig;
import me.zpp0196.qqpurify.hook.callback.XC_LogMethodHook;
import me.zpp0196.qqpurify.task.CheckUpdateTask;
import me.zpp0196.qqpurify.utils.Constants;
import me.zpp0196.qqpurify.utils.Setting;
import me.zpp0196.qqpurify.utils.SettingUtils;

/**
 * Created by zpp0196 on 2019/5/19.
 */
public class QQDialogUtils implements Constants, QQClasses {

    private static final String KEY_VERSION_CODE = "versionCode";
    private static final String KEY_LAST_CHECK = "lastCheck";
    private static List<Throwable> mInitErrors;

    public static void init(ClassLoader classLoader) {
        XMethodHook.create(QQConfigUtils.findClass(MainFragment), classLoader).method("onViewCreated").hook(new XC_LogMethodHook() {
            @Override
            protected void after(XMethodHook.MethodParam param) {
                super.after(param);
                Activity activity = XMethod.create(param).name("getActivity").invoke();
                if (mInitErrors == null || mInitErrors.isEmpty()) {
                    if (showDebugReadme(activity)) {
                        return;
                    }
                    checkUpdate(activity);
                    return;
                }
                showInitError(activity, 0);
            }
        });
    }

    public static void addError(Throwable th) {
        if (mInitErrors == null) {
            mInitErrors = new ArrayList<>();
        }
        if (mInitErrors.contains(th)) {
            return;
        }
        mInitErrors.add(th);
    }

    private static void showInitError(Activity activity, int index) {
        if (index >= mInitErrors.size()) {
            mInitErrors.clear();
            return;
        }
        Throwable th = mInitErrors.get(index);
        new QQCustomDialog.Builder(activity)
                .cancel(false)
                .title("发生错误")
                .message(th.getMessage())
                .positive("关闭", (dialog, which) -> {
                    dialog.dismiss();
                    showInitError(activity, index + 1);
                })
                .build()
                .show();
    }

    public static void showRestartDialog(Activity activity) {
        new QQCustomDialog.Builder(activity)
                .cancel(false)
                .title("提示")
                .message("新的设置需要重启QQ才能生效")
                .positive("重启", (dialog, which) -> Process.killProcess(Process.myPid()))
                .negative("忽略", (dialog, which) -> dialog.dismiss())
                .build()
                .show();
    }

    private static boolean showDebugReadme(Activity activity) {
        long version = QQSharedPreferences.getLong(activity, KEY_VERSION_CODE);
        if (!BuildConfig.TEST || version >= BuildConfig.VERSION_CODE) {
            return false;
        }

        String message = "        作为测试人员，您将可以体验尚未公开发布的测试版本，" +
                "该测试版会在日后向公众推出正式版，在您测试的版本公开发布之前，请不要宣传或分享该版本。" +
                "\n\n        当前版本为测试版本，不代表最终版本！";
        new QQCustomDialog.Builder(activity)
                .cancel(false)
                .title("测试版使用须知")
                .message(message)
                .positive("我知道了", (dialog, which) -> {
                    dialog.dismiss();
                    QQSharedPreferences.putLong(activity, KEY_VERSION_CODE, BuildConfig.VERSION_CODE);
                })
                .build()
                .show();
        return true;
    }

    private static void checkUpdate(Activity activity) {
        long lastCheckUpdate = QQSharedPreferences.getLong(activity, KEY_LAST_CHECK);
        Setting update = Setting.getInstance(SettingUtils.ISetting.SettingGroup.update);
        if (!update.get(KEY_UPD_AUTO_CHECK, true)) {
            return;
        }
        if (System.currentTimeMillis() - lastCheckUpdate > update.get(KEY_UPD_INTERVAL, 1000) * 60000) {
            new CheckUpdateTask(activity, update.get(KEY_UPD_AUTO_OPEN, false)).execute();
            QQSharedPreferences.putLong(activity, KEY_LAST_CHECK, System.currentTimeMillis());
        }
    }
}
