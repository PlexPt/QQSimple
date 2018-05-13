package me.zpp0196.qqsimple.hook;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import de.robv.android.xposed.XC_MethodHook;
import me.zpp0196.qqsimple.BuildConfig;
import me.zpp0196.qqsimple.activity.SettingActivity;
import me.zpp0196.qqsimple.hook.base.BaseHook;
import me.zpp0196.qqsimple.hook.comm.Ids;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static me.zpp0196.qqsimple.hook.comm.Classes.ApolloManager$CheckApolloInfoResult;
import static me.zpp0196.qqsimple.hook.comm.Classes.BaseActivity;
import static me.zpp0196.qqsimple.hook.comm.Classes.FrameHelperActivity;
import static me.zpp0196.qqsimple.hook.comm.Classes.QQAppInterface;
import static me.zpp0196.qqsimple.hook.comm.Classes.QQSettingMe;

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
    private void addEntryInSidebar(){
        findAndHookConstructor(QQSettingMe, BaseActivity, QQAppInterface, FrameHelperActivity, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Activity BaseActivity = (Activity) param.args[0];
                ViewGroup viewGroup = (ViewGroup) findField(QQSettingMe, ViewGroup.class, "a").get(param.thisObject);
                View view = viewGroup.findViewById(Ids.getId("settings"));
                view.setOnLongClickListener(v -> {
                    Intent intent = new Intent(BuildConfig.APPLICATION_ID);
                    intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    intent.setComponent(new ComponentName(BuildConfig.APPLICATION_ID, SettingActivity.class.getName()));
                    BaseActivity.startActivity(intent);
                    return false;
                });
            }
        });
    }
}
