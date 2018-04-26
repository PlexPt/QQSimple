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
import de.robv.android.xposed.XC_MethodReplacement;
import me.zpp0196.qqsimple.hook.base.BaseHook;

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
        if (!getBool("hide_menu_uaf")) return;
        Class<?> MainFragment = findClassInQQ("com.tencent.mobileqq.activity.MainFragment");
        Class<?> ActionSheet = findClassInQQ("com.tencent.widget.ActionSheet");
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
        if (!getBool("hide_popup_menu_entry")) return;
        Class<?> Conversation = findClassInQQ("com.tencent.mobileqq.activity.Conversation");
        findAndHookMethod(Conversation, "D", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                ImageView view = (ImageView) findField(Conversation, ImageView.class, "a").get(param.thisObject);
                if (view == null) return;
                view.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 隐藏消息列表右上角快捷入口内容
     */
    private void hidePopupMenuContacts() {
        if (!getBool("hide_popup_multiChat", "hide_popup_add", "hide_popup_sweep", "hide_popup_face2face", "hide_popup_pay", "hide_popup_shoot", "hide_popup_videoDance"))
            return;
        Class<?> PopupMenuDialog = findClassInQQ("com.tencent.widget.PopupMenuDialog");
        Class<?> MenuItem = findClassInQQ("com.tencent.widget.PopupMenuDialog$MenuItem");
        Class<?> OnClickActionListener = findClassInQQ("com.tencent.widget.PopupMenuDialog$OnClickActionListener");
        findAndHookMethod(PopupMenuDialog, "a", Activity.class, List.class, OnClickActionListener, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                List list = (List) param.args[1];
                List needRemove = new ArrayList();
                if (MenuItem == null) return;
                for (Object item : list) {
                    String title = (String) findField(MenuItem, String.class, "a").get(item);
                    if (title.equals("创建群聊") && getBool("hide_popup_multiChat"))
                        needRemove.add(item);
                    if (title.equals("加好友/群") && getBool("hide_popup_add")) needRemove.add(item);
                    if (title.equals("扫一扫") && getBool("hide_popup_sweep")) needRemove.add(item);
                    if (title.equals("面对面快传") && getBool("hide_popup_face2face"))
                        needRemove.add(item);
                    if (title.equals("付款") && getBool("hide_popup_pay")) needRemove.add(item);
                    if (title.equals("拍摄") && getBool("hide_popup_shoot")) needRemove.add(item);
                    if (title.equals("高能舞室") && getBool("hide_popup_videoDance"))
                        needRemove.add(item);
                }
                list.removeAll(needRemove);
            }
        });
    }

    /**
     * 隐藏全民闯关入口
     */
    private void hideNationalEntrance() {
        if (!getBool("hide_national_entrance")) return;
        Class<?> ConversationNowController = findClassInQQ("com.tencent.mobileqq.now.enter.ConversationNowController"); // 2
        findAndHookMethod(ConversationNowController, "a", String.class, XC_MethodReplacement.returnConstant(null));
    }

    /**
     * 隐藏隐藏动态界面大家都在搜
     */
    private void hideEveryoneSearching() {
        if (!isMoreThan735() || !getBool("hide_everyone_searching")) return;
        Class<?> Leba = findClassInQQ("com.tencent.mobileqq.activity.Leba");
        findAndHookMethod(Leba, "a", List.class, XC_MethodReplacement.returnConstant(null));
    }

    /**
     * 隐藏底部分组
     */
    private void hideMainFragmentTab() {
        if (getBool("hide_tab_contact", "hide_tab_dynamic")) {
            Class<?> MainFragment = findClassInQQ("com.tencent.mobileqq.activity.MainFragment");
            findAndHookMethod(MainFragment, "a", View.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Field field = findField(MainFragment, View[].class, "a");
                    if (field == null) return;
                    View[] views = (View[]) field.get(param.thisObject);
                    if (views == null) return;
                    if (getBool("hide_tab_contact")) views[2].setVisibility(View.GONE);
                    if (getBool("hide_tab_dynamic")) views[3].setVisibility(View.GONE);
                    param.setResult(views);
                }
            });
        }
        if (!getBool("hide_tab_readinjoy")) return;
        Class<?> ReadInJoyHelper = findClassInQQ("cooperation.readinjoy.ReadInJoyHelper");
        findAndHookMethod(ReadInJoyHelper, "d", XC_MethodReplacement.returnConstant(false));
    }

    /**
     * 隐藏联系人分组
     */
    private void hideSlidingIndicator() {
        if (!getBool("hide_contacts_tab_device", "hide_contacts_tab_phone", "hide_contacts_tab_pub_account"))
            return;
        Class<?> ContactsViewController = findClassInQQ("com.tencent.mobileqq.activity.contacts.base.ContactsViewController");
        Class<?> SimpleSlidingIndicator = findClassInQQ("com.tencent.mobileqq.activity.contacts.view.SimpleSlidingIndicator");
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
