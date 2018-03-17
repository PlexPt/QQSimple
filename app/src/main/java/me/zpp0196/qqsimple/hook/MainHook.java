package me.zpp0196.qqsimple.hook;

import android.app.Application;
import android.os.Build;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.zpp0196.qqsimple.util.SettingUtils;

import static me.zpp0196.qqsimple.Common.PACKAGE_NAME_QQ;

/**
 * Created by Deng on 2018/1/6.
 */

public class MainHook extends BaseHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        new CheckActive().handleLoadPackage(loadPackageParam);
        if (!loadPackageParam.packageName.equals(PACKAGE_NAME_QQ)) return;
        if (SettingUtils.getValueCloseAll()) return;
        XposedHelpers.findAndHookMethod("com.tencent.mobileqq.app.InjectUtils", loadPackageParam.classLoader, "injectExtraDexes",
                Application.class, boolean.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        final Application application = (Application) param.args[0];
                        if (Build.VERSION.SDK_INT < 21) {
                            XposedHelpers.findAndHookMethod("com.tencent.common.app.BaseApplicationImpl", application.getClassLoader(), "onCreate", new XC_MethodHook() {
                                @Override
                                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                    startHook(application.getClassLoader());
                                }
                            });
                        } else {
                            startHook(application.getClassLoader());
                        }
                    }
                });
    }

    private void startHook(final ClassLoader classLoader) {
        if (classLoader == null) {
            XposedBridge.log(String.format("%s can not get classloader", getQQ_Version()));
            return;
        }

        Class drawable = XposedHelpers.findClass("com.tencent.mobileqq.R$drawable", classLoader);
        Class id = XposedHelpers.findClass("com.tencent.mobileqq.R$id", classLoader);

        if(drawable == null || id == null){
            return;
        }

        RemoveImagine removeImagine = new RemoveImagine(id, drawable);
        removeImagine.hideView();
        removeImagine.hideDrawable();

        MainUIHook uiHook = new MainUIHook(classLoader, id);
        ChatInterfaceHook chatInterfaceHook = new ChatInterfaceHook(classLoader);
        OtherHook otherHook = new OtherHook(classLoader);
        MoreHook moreHook = new MoreHook(classLoader);

        // 隐藏更新提醒
        if (SettingUtils.getValueHideUpdateReminder()) {
            uiHook.hideUpdateReminder();
        }

        // 隐藏全民闯关入口
        if (SettingUtils.getValueHideNationalEntrance()) {
            uiHook.hideNationalEntrance();
        }

        // 隐藏底部分组
        uiHook.hideMainFragmentTab();

        // 隐藏隐藏联系人分组
        uiHook.hideSlidingIndicator();

        // 隐藏动态界面更多按钮
        if (SettingUtils.getValueHideDynamicMore()) {
            uiHook.hideDynamicMore();
        }

        // 隐藏头像挂件
        if (SettingUtils.getValueHideAvatarPendant()) {
            chatInterfaceHook.hideAvatarPendant();
        }

        // 隐藏个性气泡
        if (SettingUtils.getValueHideChatBubble()) {
            chatInterfaceHook.hideChatBubble();
        }

        // 隐藏字体特效
        if (SettingUtils.getValueHideFontEffects()) {
            chatInterfaceHook.hideFontEffects();
        }

        // 隐藏表情联想
        if (SettingUtils.getValueHideExpressionAssociation()) {
            chatInterfaceHook.hideExpressionAssociation();
        }

        // 隐藏好友新发的说说
        if (SettingUtils.getValueHideNewFeed()) {
            chatInterfaceHook.hideNewFeed();
        }

        // 隐藏取消隐藏不常用联系人提示
        if (SettingUtils.getValueHideChatUnusualContacts()) {
            chatInterfaceHook.hideChatUnusualContacts();
        }

        // 隐藏搜狗输入法广告
        if (SettingUtils.getValueHideChatSougouAd()) {
            chatInterfaceHook.hideChatSougouAd();
        }

        // 隐藏礼物动画
        if (SettingUtils.getValueHideGroupGiftAnim()) {
            chatInterfaceHook.hideGroupGiftAnima();
        }

        // 隐藏群聊入场动画
        if (SettingUtils.getValueHideGroupChatAdmissions()) {
            chatInterfaceHook.hideGroupChatAdmissions();
        }

        // 完全关闭动画
        if (SettingUtils.getValueCloseAllAnimation()) {
            otherHook.closeAllAnimation();
        }

        // 防止闪照消失
        if (SettingUtils.getValuePreventFlashDisappear()) {
            moreHook.preventFlashDisappear();
        }

        // 防止消息撤回
        if (SettingUtils.getValuePreventMessagesWithdrawn()) {
            moreHook.preventMessagesWithdrawn();
        }

        // 模拟菜单
        if(SettingUtils.getValueSimulateMenu()){
            moreHook.simulateMenu();
        }

        if (getQQ_Version().compareTo("7.3.2") >= 0) {
            // 隐藏推荐表情
            if (SettingUtils.getValueHideRecommendedExpression()) {
                chatInterfaceHook.hideRecommendedExpression();
            }

            // 隐藏好友获得了新徽章
            if (SettingUtils.getValueHideGetNewBadge()) {
                chatInterfaceHook.hideGetNewBadge();
            }

            // 隐藏设置免流量特权
            if (SettingUtils.getValueHideSettingFreeFlow()) {
                otherHook.hideSettingFreeFlow();
            }
            if (getQQ_Version().compareTo("7.3.5") >= 0) {
                // 隐藏动态界面大家都在搜
                if (SettingUtils.getValueHideEveryoneSearching()) {
                    uiHook.hideEveryoneSearching();
                }
            }
        }
    }
}
