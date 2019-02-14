package me.zpp0196.qqpurify.hook;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import me.zpp0196.qqpurify.utils.ReflectionUtils;

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
                    if (!getBool("leba_display_qzoneEntry", true)) {
                        hideView(getObjectIfExists(thisObject, View.class, "c"));
                    }
                    // 隐藏附近的人入口
                    if (!getBool("leba_display_nearEntry", true)) {
                        hideView(getObjectIfExists(thisObject, View.class, "d"));
                    }
                    // 隐藏兴趣部落入口
                    if (!getBool("leba_display_tribalEntry", true)) {
                        hideView(getObjectIfExists(thisObject, View.class, "f"));
                    }
                    // 隐藏动态头像提醒
                    if (getBool("leba_hide_qzoneRemind")) {
                        ReflectionUtils.setObjectField(thisObject, TextView.class, "b", null);
                    }
                    // 隐藏附近头像提醒
                    if (getBool("leba_hide_nearRemind")) {
                        View nearSub = getObjectIfExists(thisObject, ImageView.class, "d");
                        hideView(nearSub);
                    }
                    // 隐藏部落头像提醒
                    if (getBool("leba_hide_tribalRemind")) {
                        View tribalSub = getObjectIfExists(thisObject, URLImageView, "a");
                        hideView(tribalSub);
                        ReflectionUtils.setObjectField(thisObject, TextView.class, "d", null);
                    }
                }
            });
            // 隐藏动态头像提醒
            if (getBool("leba_hide_qzoneRemind")) {
                findAndHookMethod(Leba, "w", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        ReflectionUtils.setObjectField(param.thisObject, findClass(LebaQZoneFacePlayHelper), "a", null);
                    }
                });
            }
        }
    }
}
