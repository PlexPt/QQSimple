package me.zpp0196.qqpurify.hook;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import me.zpp0196.qqpurify.utils.ReflectionUtils;
import me.zpp0196.qqpurify.utils.XPrefUtils;

/**
 * Created by zpp0196 on 2019/2/8.
 */

public class ConversationHook extends AbstractHook {
    @Override
    public void init() throws Throwable {
        // 隐藏消息列表顶部横幅广告
        if (getBool("conversation_hide_headAd", true)) {
            findAndHookMethod(BannerManager, "A", XC_MethodReplacement.returnConstant(null));
        }
        // 是否显示快捷入口
        if (getBool("conversation_display_popupMenu", true)) {
            // 隐藏快捷入口内容
            hidePopupMenuItem();
        } else {
            // 隐藏快捷入口
            findAndHookMethod(Conversation, "C", hideViewAfterMethod(ImageView.class, "a"));
        }
        // 隐藏分割线
        if (getBool("conversation_hide_divider")) {
            hideDivider();
        }
        // 隐藏SVIP铭牌
        if (getBool("conversation_hide_svipNameplate", true)) {
            XposedHelpers.setStaticIntField(findClass(VipUtils), "a", 4);
        }
    }

    private void hidePopupMenuItem() {
        List<String> itemList = XPrefUtils.getStringList("conversation_hide_popupMenuItem");
        Class<?> PopupMenuDialog$MenuItem = findClass(super.PopupMenuDialog$MenuItem);
        findAndHookMethod(PopupMenuDialog, "a", Activity.class, List.class, PopupMenuDialog$OnClickActionListener, PopupMenuDialog$OnDismissListener, new XC_MethodHook() {
            @Override
            @SuppressWarnings ("unchecked")
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                List list = (List) param.args[1];
                List needRemove = new ArrayList<>();
                for (Object item : list) {
                    String title = ReflectionUtils.getObjectIfExists(PopupMenuDialog$MenuItem, String.class, "a", item);
                    if (itemList.contains(title)) {
                        needRemove.add(item);
                    }
                }
                if (needRemove.size() == list.size()) {
                    needRemove.remove(0);
                }
                list.removeAll(needRemove);
            }
        });

        // 隐藏轻游戏
        if (itemList.contains("轻游戏")) {
            findAndHookMethod(RecentOptPopBar, View.class, "a", XC_MethodReplacement.returnConstant(null));
        }
    }

    private void hideDivider() {
        findAndHookMethod(RecentEfficientItemBuilder, "a", View.class, RecentBaseData, Context.class, Drawable.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                View args0 = (View) param.args[0];
                Object tag = args0.getTag();
                if (tag.getClass()
                        .getName()
                        .contains("RecentEfficientItemBuilder$RecentEfficientItemBuilderHolder")) {
                    View view = getObjectIfExists(tag, View.class, "f");
                    view.setBackground(null);
                }
            }
        });
    }
}
