package me.zpp0196.qqpurify.hook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import me.zpp0196.qqpurify.activity.SettingsActivity;
import me.zpp0196.qqpurify.utils.XPrefUtils;

import static me.zpp0196.qqpurify.BuildConfig.APPLICATION_ID;

/**
 * Created by zpp0196 on 2019/2/8.
 */

public class SidebarHook extends AbstractHook {
    @Override
    public void init() {
        XposedHelpers.findAndHookConstructor(findClass(QQSettingMe), BaseActivity, QQAppInterface, FrameHelperActivity, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                hideSidebarItemAndAddEntry(param);
            }
        });
        // 隐藏侧滑栏厘米秀
        if (getBool("sidebar_hide_apollo")) {
            findAndHookMethod(QQSettingMe, "a", ApolloManager$CheckApolloInfoResult, XC_MethodReplacement.returnConstant(null));
        }
        hideSettingItem();
    }

    private void hideSidebarItemAndAddEntry(XC_MethodHook.MethodHookParam param) {
        final Object obj = param.thisObject;

        // 隐藏打卡
        if (getBool("sidebar_hide_myDaily", true)) {
            hideView(getObjectIfExists(obj, LinearLayout.class, "a"));
        }
        // 隐藏二维码
        findAndHideView(obj, ImageView.class, "d", "sidebar_hide_myQrCode");
        // 隐藏侧滑栏内容
        View[] items = getObjectIfExists(obj, View[].class, "a");
        List<Integer> list = XPrefUtils.getIntegerList("sidebar_hide_items");
        for (Integer integer : list) {
            hideView(items[integer]);
        }
        // 隐藏夜间模式
        View nightTheme = getObjectIfExists(obj, View.class, "d");
        if (getBool("sidebar_hide_nightTheme", true)) {
            hideView(nightTheme);
        }
        // 隐藏天气
        findAndHideView(obj, LinearLayout.class, "b", "sidebar_hide_cityWeather");

        Activity activity = (Activity) param.args[0];
        addModuleEntry(activity, (ViewGroup) nightTheme.getParent());
    }

    @SuppressLint ("ResourceType")
    private void addModuleEntry(Activity activity, ViewGroup bottomBtn) {
        // 侧滑栏添加模块入口
        if (getBool("sidebar_add_moduleEntry", true)) {
            View settingLayout = bottomBtn.getChildAt(0);
            settingLayout.setOnLongClickListener(v -> {
                Intent intent = new Intent(APPLICATION_ID);
                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                intent.setComponent(new ComponentName(APPLICATION_ID, SettingsActivity.class.getName()));
                activity.startActivity(intent);
                return false;
            });
        }
    }

    private void hideSettingItem() {
        // 隐藏设置界面内容
        List<String> list = XPrefUtils.getStringList("setting_hide_items");
        findAndHookMethod(QQSettingSettingActivity, "a", int.class, int.class, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Activity activity = (Activity) param.thisObject;
                int viewId = Integer.parseInt(param.args[0].toString());
                int strId = Integer.parseInt(param.args[1].toString());
                View view = activity.findViewById(viewId);
                String str = activity.getString(strId);
                if (list.contains(str)) {
                    hideView(view);
                }
            }
        });
        // 隐藏QQ达人
        if (list.contains("QQ达人")) {
            findAndHookMethod(QQSettingSettingActivity, "c", Card, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    param.args[0] = null;
                }
            });
        }
        // 隐藏设置免流量特权
        if (list.contains("免流量特权")) {
            findAndHookMethod(QQSettingSettingActivity, "a", XC_MethodReplacement.returnConstant(null));
        }
    }
}
