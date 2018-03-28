package me.zpp0196.qqsimple.hook;

import android.app.Dialog;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
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
