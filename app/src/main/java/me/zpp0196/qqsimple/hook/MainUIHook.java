package me.zpp0196.qqsimple.hook;

import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import me.zpp0196.qqsimple.util.SettingUtils;

import static me.zpp0196.qqsimple.hook.MainHook.getQQ_Version;

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
     * 隐藏隐藏动态界面大家都在搜
     */
    public void hideEveryoneSearching() {
        Class<?> Leba = getClass("com.tencent.mobileqq.activity.Leba");
        findAndHookMethod(Leba, "a", List.class, XC_MethodReplacement.returnConstant(null));
    }

    /**
     * 隐藏动态界面更多按钮
     */
    public void hideDynamicMore() {
        findAndHookMethod(TextView.class, "setText", CharSequence.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                CharSequence sequence = (CharSequence) param.args[0];
                TextView ivTitleBtnRightText = (TextView) param.thisObject;
                if (ivTitleBtnRightText.getId() == getId("ivTitleBtnRightText") && sequence.equals("更多")) {
                    ViewGroup viewGroup = (ViewGroup) ivTitleBtnRightText.getParent();
                    if(viewGroup.getClass().getName().contains("RelativeLayout")){
                        ivTitleBtnRightText.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    /**
     * 隐藏底部分组
     */
    public void hideMainFragmentTab() {
        Class<?> MainFragment = getClass("com.tencent.mobileqq.activity.MainFragment");
        findAndHookMethod(MainFragment, "a", View.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if (MainFragment != null) {
                    Field[] fields = MainFragment.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        if (field.getGenericType().toString().contains("[Landroid.view.View") && field.getName().equals("a")) {
                            View[] views = (View[]) field.get(param.thisObject);
                            if (SettingUtils.getValueHideTabContact()) {
                                views[2].setVisibility(View.GONE);
                            }
                            if (SettingUtils.getValueHideTabDynamic()) {
                                views[3].setVisibility(View.GONE);
                            }
                            param.setResult(views);
                        }
                    }
                }
            }
        });
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
                if (ContactsViewController != null) {
                    Field[] fields = ContactsViewController.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        if (field.getGenericType().toString().contains("SimpleSlidingIndicator") && field.getName().equals("a")) {
                            HorizontalScrollView slidingIndicator = (HorizontalScrollView) field.get(param.thisObject);
                            if (slidingIndicator != null) {
                                LinearLayout linearLayout = (LinearLayout) slidingIndicator.getChildAt(0);
                                if (SettingUtils.getValueHideContactsTabDevice()) {
                                    linearLayout.getChildAt(2).setVisibility(View.GONE);
                                }
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
            }
        });
    }

    private int getId(String idName) {
        if (id == null || idName.equals("")) {
            return 0;
        }
        try {
            return XposedHelpers.getStaticIntField(id, idName);
        } catch (Exception e) {
            XposedBridge.log(String.format("%s not found field: %s", getQQ_Version(), idName));
            return 0;
        }
    }

    private Class<?> getClass(String className) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            XposedBridge.log(String.format("%s can not get className: %s", getQQ_Version(), className));
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
}
