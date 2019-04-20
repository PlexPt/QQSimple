package me.zpp0196.qqpurify.hook;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;

/**
 * Created by zpp0196 on 2019/2/8.
 */

public class LebaHook extends AbstractHook {
    @Override
    public void init() throws Throwable {
        if (getBool("leba_display_tab", true)) {
            // 隐藏热搜
            if (getBool("leba_hide_hotSearch", true)) {
                findAndHookMethod(Leba, "a", List.class, XC_MethodReplacement.returnConstant(null));
            }
            findAndHookMethod(Leba, "z", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    // 隐藏好友动态入口
                    if (getBool("leba_hide_qzoneEntry")) {
                        hideView((ViewGroup) ((View) getObjectIfExists(param.thisObject, View.class, "c")).getParent());
                    }
                }
            });
            findAndHookMethod(LebaShowListManager, "a", QQAppInterface, new XC_MethodHook() {
                @Override
                @SuppressWarnings("unchecked")
                protected void afterHookedMethod(MethodHookParam param) {
                    List itemList = (List) param.getResult();
                    List needRemoveList = new ArrayList();
                    for (Object item : itemList) {
                        Object rpi = getObjectIfExists(item, ResourcePluginInfo, "a");
                        String strPkgName = getObjectIfExists(rpi, String.class, "strPkgName");
                        // 附近的人
                        if (strPkgName.equals("附近") && getBool("leba_hide_nearEntry")) {
                            needRemoveList.add(item);
                        }
                        // 兴趣部落
                        if (strPkgName.equals("com.tx.xingqubuluo.android") && getBool("leba_hide_tribalEntry")) {
                            needRemoveList.add(item);
                        }
                    }
                    itemList.removeAll(needRemoveList);
                }
            });
        }
    }
}
