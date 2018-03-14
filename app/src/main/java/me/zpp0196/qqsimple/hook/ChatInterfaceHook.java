package me.zpp0196.qqsimple.hook;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.File;
import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import me.zpp0196.qqsimple.util.SettingUtils;

import static me.zpp0196.qqsimple.hook.RemoveImagine.remove;

/**
 * Created by zpp0196 on 2018/3/11.
 */

public class ChatInterfaceHook {

    private ClassLoader classLoader;
    private Class<?> id;

    public ChatInterfaceHook(ClassLoader classLoader, Class id) {
        this.classLoader = classLoader;
        this.id = id;
    }

    /**
     * 隐藏头像挂件
     */
    public void hideAvatarPendant() {
        Class<?> PendantInfo = getClass("com.tencent.mobileqq.vas.PendantInfo");
        findAndHookMethod(PendantInfo, "a", int.class, Object.class, int.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        param.setResult(null);
                    }
                });
    }

    /**
     * 隐藏个性气泡
     */
    public void hideChatBubble() {
        Class<?> BubbleManager = getClass("com.tencent.mobileqq.bubble.BubbleManager");
        findAndHookMethod(BubbleManager, "a", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                return new File("data/data/com.tencent.mobileqq/files/imei");
            }
        });
    }

    /**
     * 隐藏字体特效
     */
    public void hideFontEffects() {
        Class<?> TextItemBuilder = getClass("com.tencent.mobileqq.activity.aio.item.TextItemBuilder");
        Class<?> BaseBubbleBuilder$ViewHolder = getClass("com.tencent.mobileqq.activity.aio.BaseBubbleBuilder$ViewHolder");
        Class<?> ChatMessage = getClass("com.tencent.mobileqq.data.ChatMessage");
        findAndHookMethod(TextItemBuilder, "a", BaseBubbleBuilder$ViewHolder, ChatMessage, XC_MethodReplacement.returnConstant(null));
        Class<?> TextPreviewActivity = getClass("com.tencent.mobileqq.activity.TextPreviewActivity");
        findAndHookMethod(TextPreviewActivity, "a", int.class, XC_MethodReplacement.returnConstant(null));
    }

    /**
     * 隐藏表情联想
     */
    public void hideExpressionAssociation() {
        Class<?> EmoticonManager = getClass("com.tencent.mobileqq.model.EmoticonManager");
        findAndHookMethod(EmoticonManager, "i", XC_MethodReplacement.returnConstant(null));
    }

    /**
     * 隐藏推荐表情
     */
    public void hideRecommendedExpression() {
        Class<?> EmoticonHandler = getClass("com.tencent.mobileqq.app.EmoticonHandler");
        findAndHookMethod(EmoticonHandler, "c", int.class, XC_MethodReplacement.returnConstant(null));
    }

    /**
     * 隐藏聊天图片旁的笑脸按钮
     */
    public void hidePicSmile() {
        remove(getId("pic_light_emoj"));
    }

    /**
     * 隐藏聊天界面右上角的 QQ 电话
     */
    public void hideChatCall() {
        remove(getId("ivTitleBtnRightCall"));
    }

    /**
     * 隐藏好友获得了新徽章
     */
    public void hideGetNewBadge() {
        hideItemBuilder("MedalNewsItemBuilder");
    }

    /**
     * 隐藏好友新动态
     */
    public void hideNewFeed() {
        hideItemBuilder("QzoneFeedItemBuilder");
    }

    private void hideItemBuilder(String className) {
        Class<?> ItemBuilder = getClass("com.tencent.mobileqq.activity.aio.item." + className);
        Class<?> MessageRecord = getClass("com.tencent.mobileqq.data.MessageRecord");
        Class<?> AbstractChatItemBuilder$ViewHolder = getClass("com.tencent.mobileqq.activity.aio.AbstractChatItemBuilder$ViewHolder");
        Class<?> OnLongClickAndTouchListener = getClass("com.tencent.mobileqq.activity.aio.OnLongClickAndTouchListener");
        findAndHookMethod(ItemBuilder, "a", MessageRecord, AbstractChatItemBuilder$ViewHolder, View.class, LinearLayout.class, OnLongClickAndTouchListener, XC_MethodReplacement.returnConstant(null));
    }

    /**
     * 隐藏取消隐藏不常用联系人提示
     */
    public void hideChatUnusualContacts() {
        hideGrayTipsItem(-1026);
    }

    /**
     * 隐藏搜狗输入法广告
     */
    public void hideChatSougouAd() {
        hideGrayTipsItem(-1043);
    }

    public void hideGrayTipsItem(int type) {
        hideTimeStamp();
        Class<?> GrayTipsItemBuilder = getClass("com.tencent.mobileqq.activity.aio.item.GrayTipsItemBuilder");
        Class<?> MessageRecord = getClass("com.tencent.mobileqq.data.MessageRecord");
        Class<?> AbstractChatItemBuilder$ViewHolder = getClass("com.tencent.mobileqq.activity.aio.AbstractChatItemBuilder$ViewHolder");
        Class<?> OnLongClickAndTouchListener = getClass("com.tencent.mobileqq.activity.aio.OnLongClickAndTouchListener");
        findAndHookMethod(GrayTipsItemBuilder, "a", MessageRecord, AbstractChatItemBuilder$ViewHolder, View.class, LinearLayout.class, OnLongClickAndTouchListener, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Field field = XposedHelpers.findField(MessageRecord, "msgtype");
                int msgtype = (int) field.get(param.args[0]);
                if (msgtype == type) {
                    param.setResult(null);
                }
            }
        });
    }

    private void hideTimeStamp() {
        Class<?> BaseChatItemLayout = getClass("com.tencent.mobileqq.activity.aio.AbstractChatItemBuilder");
        Class<?> ChatMessage = getClass("com.tencent.mobileqq.data.ChatMessage");
        Class<?> OnLongClickAndTouchListener = getClass("com.tencent.mobileqq.activity.aio.OnLongClickAndTouchListener");
        findAndHookMethod(BaseChatItemLayout, "a", int.class, int.class, ChatMessage, View.class, ViewGroup.class, OnLongClickAndTouchListener, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                XposedBridge.log(String.format("args[0]: %s, args[1]: %s", param.args[0], param.args[1]));
                Field field = XposedHelpers.findField(ChatMessage, "mNeedTimeStamp");
                if (field != null) {
                    boolean mNeedTimeStamp = (boolean) field.get(param.args[2]);
                    if (mNeedTimeStamp) {
                        XposedHelpers.setBooleanField(param.args[2], "mNeedTimeStamp", false);
                    }
                }
            }
        });
    }

    /**
     * 聊天界面底部工具栏
     */
    public void hideChatToolbar() {
        if (SettingUtils.getValueHideChatToolbarVoice()) {
            remove(getId("qq_aio_panel_ptt"));
        }
        if (SettingUtils.getValueHideChatToolbarCamera()) {
            remove(getId("qq_aio_panel_ptv"));
        }
        if (SettingUtils.getValueHideChatToolbarGif()) {
            remove(getId("qq_aio_panel_hot_pic"));
        }
        if (SettingUtils.getValueHideChatToolbarRedEnvelope()) {
            remove(getId("qq_aio_panel_hongbao"));
        }
        if (SettingUtils.getValueHideChatToolbarPoke()) {
            remove(getId("qq_aio_panel_poke"));
        }
    }

    /**
     * 隐藏群头衔
     */
    public void hideGroupMemberLevel() {
        remove(getId("chat_item_troop_member_level"));
    }

    /**
     * 隐藏魅力等级
     */
    public void hideGroupMemberGlamourLevel() {
        remove(getId("chat_item_troop_member_glamour_level"));
    }

    /**
     * 隐藏礼物动画
     */
    public void hideGroupGiftAnima() {
        Class<?> TroopGiftAnimationController = getClass("com.tencent.mobileqq.troopgift.TroopGiftAnimationController");
        Class<?> MessageForDeliverGiftTips = getClass("com.tencent.mobileqq.data.MessageForDeliverGiftTips");
        XposedHelpers.findAndHookMethod(TroopGiftAnimationController, "a", MessageForDeliverGiftTips, XC_MethodReplacement.returnConstant(null));
    }

    /**
     * 隐藏群聊入场动画
     */
    public void hideGroupChatAdmissions() {
        Class<?> TroopEnterEffectController = getClass("com.tencent.mobileqq.troop.enterEffect.TroopEnterEffectController");
        if (TroopEnterEffectController != null) {
            Field[] fields = TroopEnterEffectController.getDeclaredFields();
            for (Field field : fields) {
                if (field.toString().contains("final")) {
                    field.setAccessible(true);
                    try {
                        field.set(null, "");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 隐藏群消息里的小视频
     */
    public void hideGroupSmallVideo() {
        remove(getId("troop_assistant_feeds_title_small_video"));
        remove(getId("troop_assistant_feeds_title_super_owner"));
    }

    /**
     * 隐藏移出群助手提示
     */
    public void hideGroupHelperRemoveTips() {
        remove(getId("chat_top_bar"));
        remove(getId("chat_top_bar_confirm_btn"));
        remove(getId("chat_top_bar_text"));
        remove(getId("chat_top_bar_btn"));
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
