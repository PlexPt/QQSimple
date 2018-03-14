package me.zpp0196.qqsimple.hook;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.zpp0196.qqsimple.util.DeleteFile;
import me.zpp0196.qqsimple.util.SettingUtils;

import static me.zpp0196.qqsimple.Common.PACKAGE_NAME_QQ;
import static me.zpp0196.qqsimple.Common.getQQVersion;

/**
 * Created by Deng on 2018/1/6.
 */

public class MainHook implements IXposedHookLoadPackage {

    /**
     * 获取 QQ 版本
     *
     * @return
     */
    public static String getQQ_Version() {
        Context context = (Context) XposedHelpers.callMethod(XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread", new Object[0]), "getSystemContext", new Object[0]);
        return getQQVersion(context);
    }

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        CheckActive.isActive(loadPackageParam);
        if (!loadPackageParam.packageName.equals(PACKAGE_NAME_QQ)) return;
        if (!SettingUtils.getValueMasterSwitch()) return;
        findAndHookMethod("com.tencent.mobileqq.app.InjectUtils", loadPackageParam.classLoader, "injectExtraDexes",
                Application.class, boolean.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        final Application application = (Application) param.args[0];
                        if (Build.VERSION.SDK_INT < 21) {
                            findAndHookMethod("com.tencent.common.app.BaseApplicationImpl", application.getClassLoader(), "onCreate", new XC_MethodHook() {
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
            XposedBridge.log("can't get classloader");
            return;
        }
        Class drawable = XposedHelpers.findClass("com.tencent.mobileqq.R$drawable", classLoader);
        Class id = XposedHelpers.findClass("com.tencent.mobileqq.R$id", classLoader);

        MainUIHook uiHook = new MainUIHook(classLoader, id);
        ChatInterfaceHook chatInterfaceHook = new ChatInterfaceHook(classLoader, id);
        SideBarHook sideBarHook = new SideBarHook(id);
        OtherHook otherHook = new OtherHook(classLoader, drawable, id);
        PreventHook preventHook = new PreventHook(classLoader);

        // 隐藏消息界面横幅广告
        if (SettingUtils.getValueHideChatListHeadAd()) {
            uiHook.hideChatListHeadAd();
        }

        // 隐藏更新提醒
        if (SettingUtils.getValueHideUpdateReminder()) {
            uiHook.hideUpdateReminder();
        }

        // 隐藏全民闯关入口
        if (SettingUtils.getValueHideNationalEntrance()) {
            uiHook.hideNationalEntrance();
        }

        // 隐藏消息列表未读消息数量
        if (SettingUtils.getValueHideUncheckedMsgNum()) {
            uiHook.hideUncheckedMsgNum();
        }

        // 隐藏隐藏联系人分组
        uiHook.hideSlidingIndicator();

        // 隐藏入口
        uiHook.hideEntry();

        // 隐藏头像提示
        uiHook.hideAvatarRemind();

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

        // 隐藏聊天界面右上角的 QQ 电话
        if (SettingUtils.getValueHideChatCall()) {
            chatInterfaceHook.hideChatCall();
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

        // 聊天界面底部工具栏
        chatInterfaceHook.hideChatToolbar();

        // 隐藏群头衔
        if (SettingUtils.getValueHideGroupMemberLevel()) {
            chatInterfaceHook.hideGroupMemberLevel();
        }

        // 隐藏魅力等级
        if (SettingUtils.getValueHideGroupMemberGlamourLevel()) {
            chatInterfaceHook.hideGroupMemberGlamourLevel();
        }

        // 隐藏礼物动画
        if (SettingUtils.getValueHideGroupGiftAnim()) {
            chatInterfaceHook.hideGroupGiftAnima();
        }

        // 隐藏群聊入场动画
        if (SettingUtils.getValueHideGroupChatAdmissions()) {
            chatInterfaceHook.hideGroupChatAdmissions();
        }

        // 隐藏群消息里的小视频
        if (SettingUtils.getValueHideGroupSmallVideo()) {
            chatInterfaceHook.hideGroupSmallVideo();
        }

        // 隐藏移出群助手提示
        if (SettingUtils.getValueHideGroupHelperRemoveTips()) {
            chatInterfaceHook.hideGroupHelperRemoveTips();
        }

        // 侧滑栏
        sideBarHook.hideSidebar();

        // 完全关闭动画
        if (SettingUtils.getValueCloseAllAnimation()) {
            otherHook.closeAllAnimation();
        }

        // 隐藏启动图广告
        if (SettingUtils.getValueHideSplashAd()) {
            DeleteFile.delete("data/data/com.tencent.mobileqq/files/splashpic", "");
        }

        // 隐藏部分小红点
        if (SettingUtils.getValueHideSomeRedDot()) {
            otherHook.hideRedDot();
        }

        // 隐藏我的文件里的 TIM 推广
        if (SettingUtils.getValueHideTimInMyFile()) {
            otherHook.hideTimInMyFile();
        }

        // 隐藏空间绿厂广告
        if (SettingUtils.getValueHideQzoneAd()) {
            otherHook.hideQzoneAd();
        }

        // 防止闪照消失
        if (SettingUtils.getValuePreventFlashDisappear()) {
            preventHook.preventFlashDisappear();
        }

        // 防止消息撤回
        if (SettingUtils.getValuePreventMessagesWithdrawn()) {
            preventHook.preventMessagesWithdrawn();
        }

        if (getQQ_Version().compareTo("7.3.2") >= 0) {
            // 隐藏推荐表情
            if (SettingUtils.getValueHideRecommendedExpression()) {
                chatInterfaceHook.hideRecommendedExpression();
                DeleteFile.delete("data/data/com.tencent.mobileqq/files", "recommemd_emotion_file_");
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
                // 隐藏聊天图片旁的笑脸按钮
                if (SettingUtils.getValueHidePicSmile()) {
                    chatInterfaceHook.hidePicSmile();
                }

                // 隐藏大家都在搜
                if (SettingUtils.getValueHideEveryoneSearching()) {
                    uiHook.hideEveryoneSearching();
                }
            }
        }
    }

    private void findAndHookMethod(String className, ClassLoader classLoader, String methodName, Object... parameterTypesAndCallback) {
        try {
            XposedHelpers.findAndHookMethod(className, classLoader, methodName, parameterTypesAndCallback);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }
}
