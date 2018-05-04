package me.zpp0196.qqsimple.hook;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import me.zpp0196.qqsimple.BuildConfig;
import me.zpp0196.qqsimple.activity.SettingActivity;
import me.zpp0196.qqsimple.hook.base.BaseHook;
import me.zpp0196.qqsimple.hook.comm.Ids;

import static me.zpp0196.qqsimple.hook.comm.Classes.ApolloManager$CheckApolloInfoResult;
import static me.zpp0196.qqsimple.hook.comm.Classes.BaseActivity;
import static me.zpp0196.qqsimple.hook.comm.Classes.FrameHelperActivity;
import static me.zpp0196.qqsimple.hook.comm.Classes.QQAppInterface;
import static me.zpp0196.qqsimple.hook.comm.Classes.QQSettingMe;

/**
 * Created by zpp0196 on 2018/3/11.
 */

class OtherHook extends BaseHook {

    OtherHook() {
        closeAllAnimation();
        hideSidebarApollo();
        addEntryInSidebar();
    }

    /**
     * 完全关闭动画
     */
    private void closeAllAnimation() {
        findAndHookMethod(Activity.class, "startActivityForResult", Intent.class, int.class, Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (!getBool("close_all_animation")) return;
                ((Intent) param.args[0]).putExtra("open_chatfragment_withanim", false);
            }
        });
        findAndHookMethod(Activity.class, "startActivity", Intent.class, Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (!getBool("close_all_animation")) return;
                ((Intent) param.args[0]).putExtra("open_chatfragment_withanim", false);
            }
        });
    }

    /**
     * 隐藏侧滑栏厘米秀
     */
    private void hideSidebarApollo() {
        findAndHookMethod(QQSettingMe, "a", ApolloManager$CheckApolloInfoResult, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (getBool("hide_sidebar_apollo")) param.setResult(null);
            }
        });
    }

    /**
     * 在侧滑栏添加进入模块入口
     */
    private void addEntryInSidebar(){
        XposedHelpers.findAndHookConstructor(QQSettingMe, BaseActivity, QQAppInterface, FrameHelperActivity, new XC_MethodHook() {
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
