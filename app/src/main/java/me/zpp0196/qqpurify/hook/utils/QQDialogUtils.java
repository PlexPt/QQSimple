package me.zpp0196.qqpurify.hook.utils;

import android.app.Activity;
import android.os.Process;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import me.zpp0196.library.xposed.XC_MemberHook;
import me.zpp0196.library.xposed.XMethod;
import me.zpp0196.library.xposed.XMethodHook;
import me.zpp0196.qqpurify.utils.Constants;

/**
 * Created by zpp0196 on 2019/5/19.
 */
public class QQDialogUtils implements Constants, QQClasses {

    private static List<Throwable> mInitErrors;

    public static void init(ClassLoader classLoader) {
        XMethodHook.create(QQConfigUtils.findClass(MainFragment), classLoader).method("onViewCreated")
                .hook(new XC_MemberHook() {
            @Override
            protected void onAfterHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                Activity activity = XMethod.create(param).name("getActivity").invoke();
                if (mInitErrors == null || mInitErrors.isEmpty()) {
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
}
