package me.zpp0196.qqsimple.hook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import me.zpp0196.qqsimple.hook.base.BaseHook;

/**
 * Created by zpp0196 on 2018/3/11.
 */

class OtherHook extends BaseHook {

    OtherHook() {
        closeAllAnimation();
        hideSidebarApollo();
    }

    /**
     * 完全关闭动画
     */
    private void closeAllAnimation() {
        if (!getBool("close_all_animation")) return;
        findAndHookMethod(Activity.class, "startActivityForResult", Intent.class, int.class, Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                ((Intent) param.args[0]).putExtra("open_chatfragment_withanim", false);
            }
        });
        findAndHookMethod(Activity.class, "startActivity", Intent.class, Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                ((Intent) param.args[0]).putExtra("open_chatfragment_withanim", false);
            }
        });
    }

    /**
     * 隐藏侧滑栏厘米秀
     */
    private void hideSidebarApollo() {
        if (!getBool("hide_sidebar_apollo")) return;
        Class<?> QQSettingMe = findClassInQQ("com.tencent.mobileqq.activity.QQSettingMe");
        Class<?> CheckApolloInfoResult = findClassInQQ("com.tencent.mobileqq.apollo.ApolloManager$CheckApolloInfoResult");
        findAndHookMethod(QQSettingMe, "a", CheckApolloInfoResult, XC_MethodReplacement.returnConstant(null));
    }
}
