package me.zpp0196.qqsimple.hook;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.File;
import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;

import static me.zpp0196.qqsimple.Common.KEY_HIDE_AVATAR_PENDANT;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_BUBBLE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_SOUGOU_AD;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_UNUSUAL_CONTACTS;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_EXPRESSION_ASSOCIATION;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_FONT_EFFECTS;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_GET_NEW_BADGE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_GROUP_CHAT_ADMISSIONS;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_GROUP_GIFT_ANIM;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_NEW_FEED;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_NEW_SIGNATURE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_RECOMMENDED_EXPRESSION;

/**
 * Created by zpp0196 on 2018/3/11.
 */

class ChatInterfaceHook extends BaseHook {

    ChatInterfaceHook(ClassLoader classLoader) {
        setClassLoader(classLoader);
        hideAvatarPendant();
        hideChatBubble();
        hideFontEffects();
        hideExpressionAssociation();
        hideRecommendedExpression();
        hideChatUnusualContacts();
        hideNewFeed();
        hideNewSignNature();
        hideGetNewBadge();
        hideChatSougouAd();
        hideGroupGiftAnima();
        hideGroupChatAdmissions();
    }

    /**
     * 隐藏头像挂件
     */
    private void hideAvatarPendant() {
        if (getBool(KEY_HIDE_AVATAR_PENDANT)) {
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
    }

    /**
     * 隐藏个性气泡
     */
    private void hideChatBubble() {
        if (getBool(KEY_HIDE_CHAT_BUBBLE)) {
            Class<?> BubbleManager = getClass("com.tencent.mobileqq.bubble.BubbleManager");
            findAndHookMethod(BubbleManager, "a", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                    return new File("data/data/com.tencent.mobileqq/files/imei");
                }
            });
        }
    }

    /**
     * 隐藏字体特效
     */
    private void hideFontEffects() {
        if (getBool(KEY_HIDE_FONT_EFFECTS)) {
            Class<?> TextItemBuilder = getClass("com.tencent.mobileqq.activity.aio.item.TextItemBuilder");
            Class<?> BaseBubbleBuilder$ViewHolder = getClass("com.tencent.mobileqq.activity.aio.BaseBubbleBuilder$ViewHolder");
            Class<?> ChatMessage = getClass("com.tencent.mobileqq.data.ChatMessage");
            findAndHookMethod(TextItemBuilder, "a", BaseBubbleBuilder$ViewHolder, ChatMessage, XC_MethodReplacement.returnConstant(null));
            Class<?> TextPreviewActivity = getClass("com.tencent.mobileqq.activity.TextPreviewActivity");
            findAndHookMethod(TextPreviewActivity, "a", int.class, XC_MethodReplacement.returnConstant(null));
        }
    }

    /**
     * 隐藏表情联想
     */
    private void hideExpressionAssociation() {
        if (getBool(KEY_HIDE_EXPRESSION_ASSOCIATION)) {
            Class<?> EmoticonManager = getClass("com.tencent.mobileqq.model.EmoticonManager");
            findAndHookMethod(EmoticonManager, "i", XC_MethodReplacement.returnConstant(null));
        }
    }

    /**
     * 隐藏推荐表情
     */
    private void hideRecommendedExpression() {
        if (isMoreThan732() && getBool(KEY_HIDE_RECOMMENDED_EXPRESSION)) {
            Class<?> EmoticonHandler = getClass("com.tencent.mobileqq.app.EmoticonHandler");
            findAndHookMethod(EmoticonHandler, "c", int.class, XC_MethodReplacement.returnConstant(null));
        }
    }

    /**
     * 隐藏好友获得了新徽章
     */
    private void hideGetNewBadge() {
        if (isMoreThan732() && getBool(KEY_HIDE_GET_NEW_BADGE)) {
            hideItemBuilder("MedalNewsItemBuilder");
        }
    }

    /**
     * 隐藏好友新动态
     */
    private void hideNewFeed() {
        if (getBool(KEY_HIDE_NEW_FEED)) {
            hideItemBuilder("QzoneFeedItemBuilder");
        }
    }

    /**
     * 隐藏好友新签名
     */
    private void hideNewSignNature() {
        if (getBool(KEY_HIDE_NEW_SIGNATURE)) {
            hideItemBuilder("RichStatItemBuilder");
        }
    }

    /**
     * 隐藏取消隐藏不常用联系人提示
     */
    private void hideChatUnusualContacts() {
        if (getBool(KEY_HIDE_CHAT_UNUSUAL_CONTACTS)) {
            hideGrayTipsItem(-1026);
        }
    }

    /**
     * 隐藏搜狗输入法广告
     */
    private void hideChatSougouAd() {
        if (getBool(KEY_HIDE_CHAT_SOUGOU_AD)) {
            hideGrayTipsItem(-1043);
        }
    }

    /**
     * 隐藏礼物动画
     */
    private void hideGroupGiftAnima() {
        if (getBool(KEY_HIDE_GROUP_GIFT_ANIM)) {
            Class<?> TroopGiftAnimationController = getClass("com.tencent.mobileqq.troopgift.TroopGiftAnimationController");
            Class<?> MessageForDeliverGiftTips = getClass("com.tencent.mobileqq.data.MessageForDeliverGiftTips");
            XposedHelpers.findAndHookMethod(TroopGiftAnimationController, "a", MessageForDeliverGiftTips, XC_MethodReplacement.returnConstant(null));
        }
    }

    /**
     * 隐藏群聊入场动画
     */
    private void hideGroupChatAdmissions() {
        if (getBool(KEY_HIDE_GROUP_CHAT_ADMISSIONS)) {
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

    private void hideItemBuilder(String className) {
        Class<?> ItemBuilder = getClass("com.tencent.mobileqq.activity.aio.item." + className);
        Class<?> MessageRecord = getClass("com.tencent.mobileqq.data.MessageRecord");
        Class<?> AbstractChatItemBuilder$ViewHolder = getClass("com.tencent.mobileqq.activity.aio.AbstractChatItemBuilder$ViewHolder");
        Class<?> OnLongClickAndTouchListener = getClass("com.tencent.mobileqq.activity.aio.OnLongClickAndTouchListener");
        findAndHookMethod(ItemBuilder, "a", MessageRecord, AbstractChatItemBuilder$ViewHolder, View.class, LinearLayout.class, OnLongClickAndTouchListener, XC_MethodReplacement.returnConstant(null));
    }

    private void hideGrayTipsItem(int type) {
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
}
