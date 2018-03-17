package me.zpp0196.qqsimple.hook;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.File;
import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;

/**
 * Created by zpp0196 on 2018/3/11.
 */

public class ChatInterfaceHook extends BaseHook{

    public ChatInterfaceHook(ClassLoader classLoader) {
        setClassLoader(classLoader);
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
}
