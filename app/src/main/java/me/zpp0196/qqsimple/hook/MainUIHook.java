package me.zpp0196.qqsimple.hook;

import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import me.zpp0196.qqsimple.util.SettingUtils;

import static me.zpp0196.qqsimple.hook.RemoveImagine.remove;

/**
 * Created by zpp0196 on 2018/3/12.
 */

public class MainUIHook {

    private ClassLoader classLoader;
    private Class<?> id;

    public MainUIHook(ClassLoader classLoader, Class<?> id) {
        this.classLoader = classLoader;
        this.id = id;
    }

    /**
     * 隐藏消息界面横幅广告
     */
    public void hideChatListHeadAd() {
        remove(getId("close"));
        remove(getId("adview1"));
    }

    /**
     * 隐藏更新提醒
     */
    public void hideUpdateReminder() {
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

    /**
     * 隐藏全民闯关入口
     */
    public void hideNationalEntrance() {
        Class<?> ConversationNowController = getClass("com.tencent.mobileqq.now.enter.ConversationNowController");
        findAndHookMethod(ConversationNowController, "a", String.class, XC_MethodReplacement.returnConstant(null));
        findAndHookMethod(ConversationNowController, "e", XC_MethodReplacement.returnConstant(null));
    }

    /**
     * 隐藏消息列表未读消息数量
     */
    public void hideUncheckedMsgNum() {
        remove(getId("unchecked_msg_num"));
    }

    /**
     * 隐藏大家都在搜
     */
    public void hideEveryoneSearching() {
        Class<?> Leba = getClass("com.tencent.mobileqq.activity.Leba");
        findAndHookMethod(Leba, "a", List.class, XC_MethodReplacement.returnConstant(null));
    }

    /**
     * 隐藏联系人分组
     */
    public void hideSlidingIndicator() {
        Class<?> ContactsViewController = getClass("com.tencent.mobileqq.activity.contacts.base.ContactsViewController");
        XposedHelpers.findAndHookMethod(ContactsViewController, "a", View.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Field[] fields = new Field[0];
                if (ContactsViewController != null) {
                    fields = ContactsViewController.getDeclaredFields();
                }
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.getGenericType().toString().contains("SimpleSlidingIndicator") && field.getName().equals("a")) {
                        HorizontalScrollView slidingIndicator = (HorizontalScrollView) field.get(param.thisObject);
                        if (slidingIndicator != null) {
                            LinearLayout linearLayout = (LinearLayout) slidingIndicator.getChildAt(0);
                            if (SettingUtils.getValueHideContactsTabPhone()) {
                                linearLayout.getChildAt(3).setVisibility(View.GONE);
                            }
                            if (SettingUtils.getValueHideContactsTabPubAccount()) {
                                linearLayout.getChildAt(4).setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * 隐藏入口
     */
    public void hideEntry() {
        if (SettingUtils.getValueHideNewFriendEntry()) {
            remove(getId("newFriendEntry"));
        }
        if (SettingUtils.getValueHideCreateTroopEntry()) {
            remove(getId("createTroopEntry"));
        }
        if (SettingUtils.getValueHideUnusualContacts()) {
            remove(getId("unusual_contacts_footerview"));
        }
        if (SettingUtils.getValueHideQzoneEntry()) {
            remove(getId("qzone_feed_entry"));
        }
        if (SettingUtils.getValueHideNearEntry()) {
            remove(getId("near_people_entry"));
        }
        if (SettingUtils.getValueHideTribalEntry()) {
            remove(getId("xingqu_buluo_entry"));
        }
    }

    /**
     * 隐藏头像提示
     */
    public void hideAvatarRemind() {
        if (SettingUtils.getValueHideQzoneAvatarRemind()) {
            remove(getId("qzone_feed_reddot"));
            remove(getId("qzone_feed_entry_sub_iv"));
        }
        if (SettingUtils.getValueHideNearAvatarRemind()) {
            remove(getId("nearby_people_entry_sub_iv"));
        }
        if (SettingUtils.getValueHideTribalAvatarRemind()) {
            remove(getId("buluo_entry_sub_iv"));
        }
    }

    private Class<?> getClass(String className) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            XposedBridge.log(e);
        }
        return null;
    }

    private void findAndHookMethod(Class<?> clazz, String methodName, Object... parameterTypesAndCallback) {
        if (clazz == null) {
            return;
        }
        try {
            XposedHelpers.findAndHookMethod(clazz, methodName, parameterTypesAndCallback);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }

    private int getId(String idName) {
        return XposedHelpers.getStaticIntField(id, idName);
    }
}
