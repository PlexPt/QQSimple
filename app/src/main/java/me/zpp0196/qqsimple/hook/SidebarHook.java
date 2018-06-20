package me.zpp0196.qqsimple.hook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.robv.android.xposed.XC_MethodHook;
import me.zpp0196.qqsimple.activity.MainActivity;
import me.zpp0196.qqsimple.hook.base.BaseHook;

import static me.zpp0196.qqsimple.BuildConfig.APPLICATION_ID;
import static me.zpp0196.qqsimple.hook.comm.Classes.ApolloManager$CheckApolloInfoResult;
import static me.zpp0196.qqsimple.hook.comm.Classes.BaseActivity;
import static me.zpp0196.qqsimple.hook.comm.Classes.FileManagerUtil;
import static me.zpp0196.qqsimple.hook.comm.Classes.FrameHelperActivity;
import static me.zpp0196.qqsimple.hook.comm.Classes.QQAppInterface;
import static me.zpp0196.qqsimple.hook.comm.Classes.QQSettingMe;
import static me.zpp0196.qqsimple.hook.comm.Maps.sidebarItem;

/**
 * Created by zpp0196 on 2018/5/11 0011.
 */

public class SidebarHook extends BaseHook {

    @Override
    public void init() {
        findAndHookConstructor(QQSettingMe, BaseActivity, QQAppInterface, FrameHelperActivity, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                hideViews(param);
            }
        });
        hideSidebarItems();
        hookOther();
    }

    private void hideSidebarItems() {
    }

    private void hideViews(XC_MethodHook.MethodHookParam param) {
        final Object obj = param.thisObject;
        ImageView qrCode = getObject(obj, ImageView.class, "d");
        View qqInfo = getObject(obj, View.class, "a");
        View nightTheme = getObject(obj, View.class, "d");

        // 隐藏二维码
        hideView(qrCode, "hide_sidebar_myQrCode");
        // 隐藏QQ信息
        if (getBool("hide_sidebar_qqInfo")) {
            qqInfo.setVisibility(View.INVISIBLE);
        }

        // 隐藏侧滑栏
        View[] items = getObject(obj, View[].class, "a");
        for (View item : items) {
            ViewGroup viewGroup = (ViewGroup) item;
            TextView textView = (TextView) viewGroup.getChildAt(1);
            for (String key : sidebarItem.keySet()) {
                String value = sidebarItem.get(key);
                if (textView.getText()
                        .toString()
                        .contains(value)) {
                    hideView(viewGroup, key);
                }
            }
        }

        // 隐藏夜间模式
        hideView(nightTheme, "hide_sidebar_nightTheme");
        addEntryInSidebar((Activity) param.args[0], (ViewGroup) nightTheme.getParent());
    }

    private void hookOther() {
        // 隐藏侧滑栏厘米秀
        findAndHookMethod(QQSettingMe, "a", ApolloManager$CheckApolloInfoResult, replaceNull("hide_sidebar_apollo"));
        // 隐藏我的文件里面的TIM推广
        findAndHookMethod(FileManagerUtil, "a", QQAppInterface, boolean.class, replaceFalse("hide_tim_in_myFile"));
    }

    /**
     * 在侧滑栏添加进入模块入口
     */
    @SuppressLint ("ResourceType")
    private void addEntryInSidebar(Activity activity, ViewGroup bottomBtn) {
        if (!getBool("sidebar_add_entry")) {
            return;
        }
        View settingLayout = bottomBtn.getChildAt(0);
        settingLayout.setOnLongClickListener(v -> {
            Intent intent = new Intent(APPLICATION_ID);
            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            intent.setComponent(new ComponentName(APPLICATION_ID, MainActivity.class.getName()));
            activity.startActivity(intent);
            return false;
        });
    }

}
