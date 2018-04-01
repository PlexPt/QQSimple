package me.zpp0196.qqsimple.hook;

import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.regex.Pattern;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;

/**
 * Created by zpp0196 on 2018/3/11.
 */

class ChatInterfaceHook extends BaseHook {

    ChatInterfaceHook(ClassLoader classLoader) {
        super(classLoader);
        hideChatBubble();
        hideFontEffects();
        hideRecommendedExpression();
        hideTAI();
        hideGroupGiftAnim();
        hideGroupChatAdmissions();
    }

    /**
     * 隐藏个性气泡
     */
    private void hideChatBubble() {
        if (getBool("hide_chat_bubble")) {
            Class<?> BubbleManager = findClassInQQ("com.tencent.mobileqq.bubble.BubbleManager");
            findAndHookMethod(BubbleManager, "a", XC_MethodReplacement.returnConstant(null));
        }
    }

    /**
     * 隐藏字体特效
     */
    private void hideFontEffects() {
        if (getBool("hide_font_effects")) {
            Class<?> TextItemBuilder = findClassInQQ("com.tencent.mobileqq.activity.aio.item.TextItemBuilder");
            Class<?> BaseBubbleBuilder$ViewHolder = findClassInQQ("com.tencent.mobileqq.activity.aio.BaseBubbleBuilder$ViewHolder");
            Class<?> ChatMessage = findClassInQQ("com.tencent.mobileqq.data.ChatMessage");
            findAndHookMethod(TextItemBuilder, "a", BaseBubbleBuilder$ViewHolder, ChatMessage, XC_MethodReplacement.returnConstant(null));
            Class<?> TextPreviewActivity = findClassInQQ("com.tencent.mobileqq.activity.TextPreviewActivity");
            findAndHookMethod(TextPreviewActivity, "a", int.class, XC_MethodReplacement.returnConstant(null));
        }
    }

    /**
     * 隐藏推荐表情
     */
    private void hideRecommendedExpression() {
        if (isMoreThan732() && getBool("hide_recommended_expression")) {
            Class<?> EmoticonManager = findClassInQQ("com.tencent.mobileqq.model.EmoticonManager");
            findAndHookMethod(EmoticonManager, "a", boolean.class, int.class, boolean.class, XC_MethodReplacement.returnConstant(new ArrayList<>()));
        }
    }

    private void hideTAI() {
        // 隐藏好友获得了新徽章
        hideItemBuilder("MedalNewsItemBuilder", isMoreThan732() && getBool("hide_get_new_badge"));
        // 隐藏好友新动态
        hideItemBuilder("QzoneFeedItemBuilder", getBool("hide_new_feed"));
        // 隐藏好友新签名
        hideItemBuilder("RichStatItemBuilder", getBool("hide_new_signature"));
        // 隐藏取消隐藏不常用联系人提示
        hideGrayTips("GatherContactsTips", getBool("hide_chat_unusual_contacts"));
        // 隐藏设置特别消息提示音
        hideGrayTips("VipSpecialCareGrayTips", getBool("hide_chat_special_care"));
        // 隐藏搜狗输入法广告
        hideGrayTips("SougouInputGrayTips", getBool("hide_chat_sougou_ad"));
        // 隐藏会员相关广告
        hideGrayTipsItem(getBool("hide_chat_vip_ad"), ".+会员.+");
        // 签到文本化
        simpleItem(getBool("simple_group_sign"), 71, 84);
        // 隐藏加入群提示
        hideGrayTipsItem(getBool("hide_group_join_tips"), ".+加入了本群.+", ".+邀请.+加入.+");
        // 隐藏获得新头衔
        hideGrayTipsItem(getBool("hide_group_member_level_tips"), ".+获得.+头衔.+");
        // 隐藏礼物相关提示
        hideGrayTipsItem(getBool("hide_group_gift_tips"), ".+礼物.+成为.+守护.+", ".+成为.+魅力.+", ".+成为.+豪气.+", ".+送给.+朵.+");
    }

    /**
     * 隐藏礼物动画
     */
    private void hideGroupGiftAnim() {
        if (getBool("hide_group_gift_anim")) {
            Class<?> TroopGiftAnimationController = findClassInQQ("com.tencent.mobileqq.troopgift.TroopGiftAnimationController");
            Class<?> MessageForDeliverGiftTips = findClassInQQ("com.tencent.mobileqq.data.MessageForDeliverGiftTips");
            findAndHookMethod(TroopGiftAnimationController, "a", MessageForDeliverGiftTips, XC_MethodReplacement.returnConstant(null));
        }
    }

    /**
     * 隐藏群聊入场动画
     */
    private void hideGroupChatAdmissions() {
        if (getBool("hide_group_chat_admissions")) {
            Class<?> TroopEnterEffectController = findClassInQQ("com.tencent.mobileqq.troop.enterEffect.TroopEnterEffectController");
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

    private void simpleItem(boolean isSimple, int... rs) {
        if (isSimple) {
            Class<?> ItemBuilderFactory = findClassInQQ("com.tencent.mobileqq.activity.aio.item.ItemBuilderFactory");
            Class<?> ChatMessage = findClassInQQ("com.tencent.mobileqq.data.ChatMessage");
            findAndHookMethod(ItemBuilderFactory, "a", ChatMessage, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    int result = (int) param.getResult();
                    for (int i : rs) {
                        if (i == result) {
                            param.setResult(-1);
                        }
                    }
                }
            });
        }
    }

    private void hideItemBuilder(String className, boolean isHide) {
        if (isHide) {
            Class<?> ItemBuilder = findClassInQQ("com.tencent.mobileqq.activity.aio.item." + className);
            Class<?> MessageRecord = findClassInQQ("com.tencent.mobileqq.data.MessageRecord");
            Class<?> AbstractChatItemBuilder$ViewHolder = findClassInQQ("com.tencent.mobileqq.activity.aio.AbstractChatItemBuilder$ViewHolder");
            Class<?> OnLongClickAndTouchListener = findClassInQQ("com.tencent.mobileqq.activity.aio.OnLongClickAndTouchListener");
            findAndHookMethod(ItemBuilder, "a", MessageRecord, AbstractChatItemBuilder$ViewHolder, View.class, LinearLayout.class, OnLongClickAndTouchListener, XC_MethodReplacement.returnConstant(null));
        }
    }

    private void hideGrayTips(String className, boolean isHide) {
        if (isHide) {
            Class<?> GatherContactsTips = findClassInQQ("com.tencent.mobileqq.activity.aio.tips." + className);
            findAndHookMethod(GatherContactsTips, "a", Object[].class, XC_MethodReplacement.returnConstant(null));
        }
    }

    private void hideGrayTipsItem(boolean isHide, String... regex) {
        if (isHide) {
            Class<?> GrayTipsItemBuilder = findClassInQQ("com.tencent.mobileqq.activity.aio.item.GrayTipsItemBuilder");
            Class<?> MessageRecord = findClassInQQ("com.tencent.mobileqq.data.MessageRecord");
            Class<?> AbstractChatItemBuilder$ViewHolder = findClassInQQ("com.tencent.mobileqq.activity.aio.AbstractChatItemBuilder$ViewHolder");
            Class<?> OnLongClickAndTouchListener = findClassInQQ("com.tencent.mobileqq.activity.aio.OnLongClickAndTouchListener");
            findAndHookMethod(GrayTipsItemBuilder, "a", MessageRecord, AbstractChatItemBuilder$ViewHolder, View.class, LinearLayout.class, OnLongClickAndTouchListener, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Field field = findField(MessageRecord, "msg");
                    if (field != null) {
                        String msg = (String) field.get(param.args[0]);
                        if (msg != null) {
                            for (String str : regex) {
                                if (Pattern.matches(str, msg)) {
                                    param.setResult(null);
                                }
                            }
                        }
                    }
                }
            });
        }
    }
}
