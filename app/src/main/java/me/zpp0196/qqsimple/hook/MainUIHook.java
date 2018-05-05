package me.zpp0196.qqsimple.hook;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import me.zpp0196.qqsimple.hook.base.BaseHook;
import me.zpp0196.qqsimple.hook.util.Util;

import static me.zpp0196.qqsimple.hook.comm.Classes.ActionSheet;
import static me.zpp0196.qqsimple.hook.comm.Classes.BusinessInfoCheckUpdate$RedTypeInfo;
import static me.zpp0196.qqsimple.hook.comm.Classes.ContactsViewController;
import static me.zpp0196.qqsimple.hook.comm.Classes.Conversation;
import static me.zpp0196.qqsimple.hook.comm.Classes.ConversationNowController;
import static me.zpp0196.qqsimple.hook.comm.Classes.Leba;
import static me.zpp0196.qqsimple.hook.comm.Classes.MainFragment;
import static me.zpp0196.qqsimple.hook.comm.Classes.PopupMenuDialog;
import static me.zpp0196.qqsimple.hook.comm.Classes.PopupMenuDialog$MenuItem;
import static me.zpp0196.qqsimple.hook.comm.Classes.PopupMenuDialog$OnClickActionListener;
import static me.zpp0196.qqsimple.hook.comm.Classes.ReadInJoyHelper;
import static me.zpp0196.qqsimple.hook.comm.Classes.SimpleSlidingIndicator;

/**
 * Created by zpp0196 on 2018/3/12.
 */

class MainUIHook extends BaseHook {

    MainUIHook() {
        hideMenuUAF();
        hidePopupMenuEntry();
        hidePopupMenuContacts();
        hideMainFragmentTab();
        hideNationalEntrance();
        hideEveryoneSearching();
        hideSlidingIndicator();
    }

    /**
     * 隐藏菜单更新和反馈
     */
    private void hideMenuUAF() {
        findAndHookMethod(MainFragment, "r", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Field field = findField(MainFragment, Dialog.class, "b");
                if (field == null) return;
                Dialog dialog = (Dialog) field.get(param.thisObject);
                if (dialog == null) return;
                Field field1 = findFirstFieldByExactType(ActionSheet, LinearLayout.class);
                if (field1 == null) return;
                if (!getBool("hide_menu_uaf")) return;
                LinearLayout layout = (LinearLayout) field1.get(dialog);
                layout.getChildAt(0).setVisibility(View.GONE);
                layout.getChildAt(1).setVisibility(View.GONE);
            }
        });
    }

    /**
     * 隐藏消息列表右上角快捷入口
     */
    private void hidePopupMenuEntry() {
        findAndHookMethod(Conversation, "D", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                ImageView view = (ImageView) findField(Conversation, ImageView.class, "a").get(param.thisObject);
                if (!getBool("hide_popup_menu_entry")) return;
                if(view != null) view.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 隐藏消息列表右上角快捷入口内容
     */
    @SuppressWarnings("unchecked")
    private void hidePopupMenuContacts() {
        findAndHookMethod(PopupMenuDialog, "a", Activity.class, List.class, PopupMenuDialog$OnClickActionListener, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                List list = (List) param.args[1];
                if(list == null || list.isEmpty()) return;
                List needRemove = new ArrayList();
                if (PopupMenuDialog$MenuItem == null) return;
                for (Object item : list) {
                    String title = (String) findField(PopupMenuDialog$MenuItem, String.class, "a").get(item);
                    if (title.equals("创建群聊") && getBool("hide_popup_multiChat")) needRemove.add(item);
                    if (title.equals("加好友/群") && getBool("hide_popup_add")) needRemove.add(item);
                    if (title.equals("扫一扫") && getBool("hide_popup_sweep")) needRemove.add(item);
                    if (title.equals("面对面快传") && getBool("hide_popup_face2face")) needRemove.add(item);
                    if (title.equals("付款") && getBool("hide_popup_pay")) needRemove.add(item);
                    if (title.equals("拍摄") && getBool("hide_popup_shoot")) needRemove.add(item);
                    if (title.equals("高能舞室") && getBool("hide_popup_videoDance")) needRemove.add(item);
                }
                list.removeAll(needRemove);
            }
        });
    }

    /**
     * 隐藏全民闯关入口
     */
    private void hideNationalEntrance() {
        findAndHookMethod(ConversationNowController, "a", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (getBool("hide_national_entrance")) param.setResult(null);
            }
        });
    }

    /**
     * 隐藏隐藏动态界面大家都在搜
     */
    private void hideEveryoneSearching() {
        findAndHookMethod(Leba, "a", List.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (Util.isMoreThan735() && getBool("hide_everyone_searching")) param.setResult(null);
            }
        });
    }

    /**
     * 隐藏底部分组
     */
    private void hideMainFragmentTab() {
        findAndHookMethod(MainFragment, "a", View.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                View[] views = getObject(param.thisObject, View[].class, "a");
                if (views == null) return;
                if (getBool("hide_tab_contact")) views[2].setVisibility(View.GONE);
                if (getBool("hide_tab_dynamic")) views[3].setVisibility(View.GONE);
            }
        });

        // 隐藏看点
        findAndHookMethod(ReadInJoyHelper, "d", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (getBool("hide_tab_readinjoy")) param.setResult(false);
            }
        });

        // 隐藏底部消息数量
        findAndHookMethod(MainFragment, "a", int.class, BusinessInfoCheckUpdate$RedTypeInfo, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                int i = (int) param.args[0];
                if ((i == 33 && getBool("hide_tab_contact_num")) || (i == 34 && (getBool("hide_tab_dynamic_num")))) {
                    param.setResult(null);
                }
            }
        });
    }

    /**
     * 隐藏联系人分组
     */
    private void hideSlidingIndicator() {
        findAndHookMethod(ContactsViewController, "a", View.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if (SimpleSlidingIndicator == null) return;
                Field field = findFirstFieldByExactType(ContactsViewController, SimpleSlidingIndicator);
                if (field == null) return;
                HorizontalScrollView slidingIndicator = (HorizontalScrollView) field.get(param.thisObject);
                if (slidingIndicator == null) return;
                LinearLayout linearLayout = (LinearLayout) slidingIndicator.getChildAt(0);
                if (getBool("hide_contacts_tab_device"))
                    linearLayout.getChildAt(2).setVisibility(View.GONE);
                if (getBool("hide_contacts_tab_phone"))
                    linearLayout.getChildAt(3).setVisibility(View.GONE);
                if (getBool("hide_contacts_tab_pub_account"))
                    linearLayout.getChildAt(4).setVisibility(View.GONE);
            }
        });
    }
}
