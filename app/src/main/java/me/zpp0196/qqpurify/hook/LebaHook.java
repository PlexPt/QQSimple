package me.zpp0196.qqpurify.hook;

import android.view.View;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;

/**
 * Created by zpp0196 on 2019/2/8.
 */

public class LebaHook extends AbstractHook {
    @Override
    public void init() throws Throwable {
        if (getBool("leba_display_tab", true) && getBool("leba_use_older", true)) {
            // 使用旧版动态
            findAndHookMethod(LebaGridManager, boolean.class, "a", XC_MethodReplacement.returnConstant(false));
            // 隐藏热搜
            if (getBool("leba_hide_hotSearch", true)) {
                findAndHookMethod(Leba, "b", List.class, XC_MethodReplacement.returnConstant(null));
            }
            findAndHookMethod(Leba, "w", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    final Object thisObject = param.thisObject;

                    // 隐藏好友动态入口
                    if (getBool("leba_hide_qzoneEntry")) {
                        hideView(getObjectIfExists(thisObject, View.class, "c"));
                    }
                    // 隐藏附近的人入口
                    if (getBool("leba_hide_nearEntry")) {
                        hideView(getObjectIfExists(thisObject, View.class, "d"));
                    }
                    // 隐藏兴趣部落入口
                    if (getBool("leba_hide_tribalEntry")) {
                        hideView(getObjectIfExists(thisObject, View.class, "f"));
                    }
                }
            });
        }
    }
}
