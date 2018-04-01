package me.zpp0196.qqsimple.hook;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;

/**
 * Created by zpp0196 on 2018/3/12.
 */

class MainUIHook extends BaseHook {

    MainUIHook(ClassLoader classLoader) {
        super(classLoader);
        hideMenuUAF();
        hidePopupMenu();
        hideMainFragmentTab();
        hideNationalEntrance();
        hideEveryoneSearching();
        hideSlidingIndicator();
    }

    /**
     * 隐藏菜单更新和反馈
     */
    private void hideMenuUAF() {
        if (getBool("hide_menu_uaf")) {
            Class<?> MainFragment = findClassInQQ("com.tencent.mobileqq.activity.MainFragment");
            Class<?> ActionSheet = findClassInQQ("com.tencent.widget.ActionSheet");
            findAndHookMethod(MainFragment, "r", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Field field = findField(MainFragment, Dialog.class, "b");
                    if (field != null) {
                        Dialog dialog = (Dialog) field.get(param.thisObject);
                        if (dialog != null) {
                            Field field1 = findFirstFieldByExactType(ActionSheet, LinearLayout.class);
                            if (field1 != null) {
                                LinearLayout layout = (LinearLayout) field1.get(dialog);
                                layout.getChildAt(0).setVisibility(View.GONE);
                                layout.getChildAt(1).setVisibility(View.GONE);
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * 隐藏消息列表右上角快捷入口部分内容
     */
    private void hidePopupMenu() {
        Class<?> PopupMenuDialog = findClassInQQ("com.tencent.widget.PopupMenuDialog");
        Class<?> MenuItem = findClassInQQ("com.tencent.widget.PopupMenuDialog$MenuItem");
        Class<?> OnClickActionListener = findClassInQQ("com.tencent.widget.PopupMenuDialog$OnClickActionListener");
        findAndHookMethod(PopupMenuDialog, "a", Activity.class, List.class, OnClickActionListener, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                List list = (List) param.args[1];
                List needRemove = new ArrayList();
                for (Object item : list) {
                    String title = (String) findField(MenuItem, String.class, "a").get(item);
                    if (title.equals("创建群聊") && getBool("hide_popup_multichat")) {
                        needRemove.add(item);
                    }
                    if (title.equals("付款") && getBool("hide_popup_charge")) {
                        needRemove.add(item);
                    }
                    if (title.equals("高能舞室") && getBool("hide_popup_video_dance")) {
                        needRemove.add(item);
                    }
                }
                list.removeAll(needRemove);
            }
        });
    }

    /**
     * 隐藏全民闯关入口
     */
    private void hideNationalEntrance() {
        if (getBool("hide_national_entrance")) {
            Class<?> ConversationNowController = findClassInQQ("com.tencent.mobileqq.now.enter.ConversationNowController");
            findAndHookMethod(ConversationNowController, "a", String.class, XC_MethodReplacement.returnConstant(null));
            findAndHookMethod(ConversationNowController, "e", XC_MethodReplacement.returnConstant(null));
        }
    }

    /**
     * 隐藏隐藏动态界面大家都在搜
     */
    private void hideEveryoneSearching() {
        if (isMoreThan735() && getBool("hide_everyone_searching")) {
            Class<?> Leba = findClassInQQ("com.tencent.mobileqq.activity.Leba");
            findAndHookMethod(Leba, "a", List.class, XC_MethodReplacement.returnConstant(null));
        }
    }

    /**
     * 隐藏底部分组
     */
    private void hideMainFragmentTab() {
        Class<?> MainFragment = findClassInQQ("com.tencent.mobileqq.activity.MainFragment");
        findAndHookMethod(MainFragment, "a", View.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Field field = findField(MainFragment, View[].class, "a");
                if (field != null) {
                    View[] views = (View[]) field.get(param.thisObject);
                    if (views != null) {
                        if (getBool("hide_tab_contact")) {
                            views[2].setVisibility(View.GONE);
                        }
                        if (getBool("hide_tab_dynamic")) {
                            views[3].setVisibility(View.GONE);
                        }
                        param.setResult(views);
                    }
                }
            }
        });
        if (getBool("hide_tab_readinjoy")) {
            Class<?> ReadInJoyHelper = findClassInQQ("cooperation.readinjoy.ReadInJoyHelper");
            findAndHookMethod(ReadInJoyHelper, "d", XC_MethodReplacement.returnConstant(false));
        }
    }

    /**
     * 隐藏联系人分组
     */
    private void hideSlidingIndicator() {
        Class<?> ContactsViewController = findClassInQQ("com.tencent.mobileqq.activity.contacts.base.ContactsViewController");
        Class<?> SimpleSlidingIndicator = findClassInQQ("com.tencent.mobileqq.activity.contacts.view.SimpleSlidingIndicator");
        findAndHookMethod(ContactsViewController, "a", View.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Field field = findFirstFieldByExactType(ContactsViewController, SimpleSlidingIndicator);
                if (field != null) {
                    HorizontalScrollView slidingIndicator = (HorizontalScrollView) field.get(param.thisObject);
                    if (slidingIndicator != null) {
                        LinearLayout linearLayout = (LinearLayout) slidingIndicator.getChildAt(0);
                        if (getBool("hide_contacts_tab_device")) {
                            linearLayout.getChildAt(2).setVisibility(View.GONE);
                        }
                        if (getBool("hide_contacts_tab_phone")) {
                            linearLayout.getChildAt(3).setVisibility(View.GONE);
                        }
                        if (getBool("hide_contacts_tab_pub_account")) {
                            linearLayout.getChildAt(4).setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }
}
