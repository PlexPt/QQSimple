package me.zpp0196.qqsimple.hook;

import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;

import static me.zpp0196.qqsimple.Common.KEY_HIDE_CONTACTS_TAB_DEVICE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CONTACTS_TAB_PHONE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CONTACTS_TAB_PUB_ACCOUNT;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_EVERYONE_SEARCHING;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_NATIONAL_ENTRANCE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_TAB_CONTACT;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_TAB_DYNAMIC;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_UPDATE_REMINDER;

/**
 * Created by zpp0196 on 2018/3/12.
 */

class MainUIHook extends BaseHook {

    MainUIHook(ClassLoader classLoader, Class<?> id) {
        setClassLoader(classLoader);
        setId(id);
        hideMainFragmentTab();
        hideRedTouchTextView();
        hideUpdateReminder();
        hideNationalEntrance();
        hideEveryoneSearching();
        hideSlidingIndicator();
    }

    /**
     * 隐藏更新提醒
     */
    private void hideUpdateReminder() {
        if (getBool(KEY_HIDE_UPDATE_REMINDER)) {
            Class<?> AfterSyncMsg = getClass("com.tencent.mobileqq.app.automator.step.AfterSyncMsg");
            findAndHookMethod(AfterSyncMsg, "e", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedHelpers.findAndHookMethod("com.tencent.mobileqq.app.MessageHandler",
                            param.thisObject.getClass().getClassLoader(), "c",
                            "com.tencent.qphone.base.remote.ToServiceMsg",
                            "com.tencent.qphone.base.remote.FromServiceMsg",
                            Object.class,
                            XC_MethodReplacement.returnConstant(null));
                }
            });
        }
    }

    /**
     * 隐藏全民闯关入口
     */
    private void hideNationalEntrance() {
        if (getBool(KEY_HIDE_NATIONAL_ENTRANCE)) {
            Class<?> ConversationNowController = getClass("com.tencent.mobileqq.now.enter.ConversationNowController");
            findAndHookMethod(ConversationNowController, "a", String.class, XC_MethodReplacement.returnConstant(null));
            findAndHookMethod(ConversationNowController, "e", XC_MethodReplacement.returnConstant(null));
        }
    }

    /**
     * 隐藏隐藏动态界面大家都在搜
     */
    private void hideEveryoneSearching() {
        if (isMoreThan735() && getBool(KEY_HIDE_EVERYONE_SEARCHING)) {
            Class<?> Leba = getClass("com.tencent.mobileqq.activity.Leba");
            findAndHookMethod(Leba, "a", List.class, XC_MethodReplacement.returnConstant(null));
        }
    }

    /**
     * 隐藏底部分组
     */
    private void hideMainFragmentTab() {
        Class<?> MainFragment = getClass("com.tencent.mobileqq.activity.MainFragment");
        findAndHookMethod(MainFragment, "a", View.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                View[] views = findField(MainFragment, "[Landroid.view.View", "a", param);
                if (views != null) {
                    if (getBool(KEY_HIDE_TAB_CONTACT)) {
                        views[2].setVisibility(View.GONE);
                    }
                    if (getBool(KEY_HIDE_TAB_DYNAMIC)) {
                        views[3].setVisibility(View.GONE);
                    }
                    param.setResult(views);
                }
            }
        });
    }

    /**
     * 隐藏底部消息数量
     */
    private void hideRedTouchTextView() {
        Class<?> MainFragment = getClass("com.tencent.mobileqq.redtouch.RedTouchTab");
        findAndHookMethod(MainFragment, "a", String.class, int.class, int.class, int.class, XC_MethodReplacement.returnConstant(null));
    }

    /**
     * 隐藏联系人分组
     */
    private void hideSlidingIndicator() {
        Class<?> ContactsViewController = getClass("com.tencent.mobileqq.activity.contacts.base.ContactsViewController");
        XposedHelpers.findAndHookMethod(ContactsViewController, "a", View.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                HorizontalScrollView slidingIndicator = findField(ContactsViewController, "SimpleSlidingIndicator", "a", param);
                if (slidingIndicator != null) {
                    LinearLayout linearLayout = (LinearLayout) slidingIndicator.getChildAt(0);
                    if (getBool(KEY_HIDE_CONTACTS_TAB_DEVICE)) {
                        linearLayout.getChildAt(2).setVisibility(View.GONE);
                    }
                    if (getBool(KEY_HIDE_CONTACTS_TAB_PHONE)) {
                        linearLayout.getChildAt(3).setVisibility(View.GONE);
                    }
                    if (getBool(KEY_HIDE_CONTACTS_TAB_PUB_ACCOUNT)) {
                        linearLayout.getChildAt(4).setVisibility(View.GONE);
                    }
                }
            }
        });
    }
}
