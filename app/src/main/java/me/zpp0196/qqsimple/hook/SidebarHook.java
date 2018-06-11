package me.zpp0196.qqsimple.hook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;

import de.robv.android.xposed.XC_MethodHook;
import me.zpp0196.qqsimple.activity.MainActivity;
import me.zpp0196.qqsimple.hook.base.BaseHook;

import static me.zpp0196.qqsimple.BuildConfig.APPLICATION_ID;
import static me.zpp0196.qqsimple.hook.comm.Classes.ApolloManager$CheckApolloInfoResult;
import static me.zpp0196.qqsimple.hook.comm.Classes.BaseActivity;
import static me.zpp0196.qqsimple.hook.comm.Classes.FrameHelperActivity;
import static me.zpp0196.qqsimple.hook.comm.Classes.QQAppInterface;
import static me.zpp0196.qqsimple.hook.comm.Classes.QQSettingMe;
import static me.zpp0196.qqsimple.hook.comm.Ids.isSupport;
import static me.zpp0196.qqsimple.hook.util.HookUtil.getQQVersionCode;

/**
 * Created by zpp0196 on 2018/5/11 0011.
 */

public class SidebarHook extends BaseHook {

    @Override
    public void init() {
        // 隐藏侧滑栏厘米秀
        findAndHookMethod(QQSettingMe, "a", ApolloManager$CheckApolloInfoResult, replaceNull("hide_sidebar_apollo"));
        addEntryInSidebar();
    }

    /**
     * 在侧滑栏添加进入模块入口
     */
    @SuppressLint ("ResourceType")
    private void addEntryInSidebar() {
        findAndHookConstructor(QQSettingMe, BaseActivity, QQAppInterface, FrameHelperActivity, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if (!getBool("sidebar_add_entry") || !isSupport(getQQVersionCode())) {
                    return;
                }
                Activity baseActivity = (Activity) param.args[0];
                ViewGroup viewGroup = getObject(param.thisObject, ViewGroup.class, "a");
                ViewGroup settingLayout = viewGroup.findViewById(getIdInQQ("settings"));
                settingLayout.setOnLongClickListener(v -> {
                    enterModule(baseActivity);
                    return false;
                });
            }
        });
    }

    private void enterModule(Context context) {
        Intent intent = new Intent(APPLICATION_ID);
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.setComponent(new ComponentName(APPLICATION_ID, MainActivity.class.getName()));
        context.startActivity(intent);
    }

}
