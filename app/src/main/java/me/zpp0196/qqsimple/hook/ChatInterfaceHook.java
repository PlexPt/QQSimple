package me.zpp0196.qqsimple.hook;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Pattern;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import me.zpp0196.qqsimple.hook.base.BaseHook;

import static de.robv.android.xposed.XposedBridge.hookMethod;
import static de.robv.android.xposed.XposedHelpers.findMethodsByExactParameters;
import static me.zpp0196.qqsimple.hook.comm.Classes.AbstractChatItemBuilder$ViewHolder;
import static me.zpp0196.qqsimple.hook.comm.Classes.AioAnimationConfigHelper;
import static me.zpp0196.qqsimple.hook.comm.Classes.BaseBubbleBuilder$ViewHolder;
import static me.zpp0196.qqsimple.hook.comm.Classes.BubbleManager;
import static me.zpp0196.qqsimple.hook.comm.Classes.ChatActivityUtils;
import static me.zpp0196.qqsimple.hook.comm.Classes.ChatMessage;
import static me.zpp0196.qqsimple.hook.comm.Classes.EmoticonManager;
import static me.zpp0196.qqsimple.hook.comm.Classes.FontManager;
import static me.zpp0196.qqsimple.hook.comm.Classes.GatherContactsTips;
import static me.zpp0196.qqsimple.hook.comm.Classes.GrayTipsItemBuilder;
import static me.zpp0196.qqsimple.hook.comm.Classes.ItemBuilderFactory;
import static me.zpp0196.qqsimple.hook.comm.Classes.MedalNewsItemBuilder;
import static me.zpp0196.qqsimple.hook.comm.Classes.MessageForDeliverGiftTips;
import static me.zpp0196.qqsimple.hook.comm.Classes.MessageRecord;
import static me.zpp0196.qqsimple.hook.comm.Classes.OnLongClickAndTouchListener;
import static me.zpp0196.qqsimple.hook.comm.Classes.QzoneFeedItemBuilder;
import static me.zpp0196.qqsimple.hook.comm.Classes.RichStatItemBuilder;
import static me.zpp0196.qqsimple.hook.comm.Classes.SougouInputGrayTips;
import static me.zpp0196.qqsimple.hook.comm.Classes.TextItemBuilder;
import static me.zpp0196.qqsimple.hook.comm.Classes.TextPreviewActivity;
import static me.zpp0196.qqsimple.hook.comm.Classes.TroopEnterEffectController;
import static me.zpp0196.qqsimple.hook.comm.Classes.TroopGiftAnimationController;
import static me.zpp0196.qqsimple.hook.comm.Classes.VipSpecialCareGrayTips;
import static me.zpp0196.qqsimple.hook.util.HookUtil.isMoreThan732;
import static me.zpp0196.qqsimple.hook.util.HookUtil.isMoreThan760;

/**
 * Created by zpp0196 on 2018/3/11.
 */

class ChatInterfaceHook extends BaseHook {

    @Override
    public void init() {
        // 隐藏个性气泡
        findAndHookMethod(BubbleManager, "a", int.class, boolean.class, replaceNull("hide_chat_bubble"));
        // 隐藏表情掉落
        findAndHookMethod(AioAnimationConfigHelper, "a", replaceNull("hide_expression_drop"));
        // 隐藏礼物动画
        findAndHookMethod(TroopGiftAnimationController, "a", MessageForDeliverGiftTips, replaceNull("hide_group_gift_anim"));
        // 隐藏好友新动态
        hideItemBuilder(QzoneFeedItemBuilder, "hide_new_feed");
        // 隐藏好友新签名
        hideItemBuilder(RichStatItemBuilder, "hide_new_signature");
        // 隐藏取消隐藏不常用联系人提示
        hideGrayTips(GatherContactsTips, "hide_chat_unusual_contacts");
        // 隐藏设置特别消息提示音
        hideGrayTips(VipSpecialCareGrayTips, "hide_chat_special_care");
        // 隐藏搜狗输入法广告
        hideGrayTips(SougouInputGrayTips, "hide_chat_sougou_input");
        // 隐藏会员相关广告
        hideGrayTipsItem("hide_chat_vip_ad", ".+会员.+");
        // 隐藏进场动画提示
        hideGrayTipsItem("hide_group_chat_admissions", ".+进场.+");
        // 签到文本化
        simpleItem("simple_group_sign", 71, 84);
        // 隐藏加入群提示
        hideGrayTipsItem("hide_group_join_tips", ".+加入了本群.+", ".+邀请.+加入.+");
        // 隐藏获得新头衔
        hideGrayTipsItem("hide_group_member_level_tips", ".+获得.+头衔.+");
        // 隐藏礼物相关提示
        hideGrayTipsItem("hide_group_gift_tips", ".+礼物.+成为.+守护.+", ".+成为.+魅力.+", ".+成为.+豪气.+", ".+送给.+朵.+");
        // 隐藏移出群助手提示
        hideTopBar("hide_group_helper_remove_tips", "常", "是否");
        hideFontEffect();
        hideGroupChatAdmissions();
        if (isMoreThan732()) {
            // 隐藏推荐表情
            findAndHookMethod(EmoticonManager, "a", boolean.class, int.class, boolean.class, replaceObj(new ArrayList<>(), "hide_recommended_expression"));
            // 隐藏好友获得了新徽章
            hideItemBuilder(MedalNewsItemBuilder, "hide_get_new_badge");
        }
    }

    /**
     * 隐藏字体特效
     */
    private void hideFontEffect() {
        findAndHookMethod(TextItemBuilder, "a", BaseBubbleBuilder$ViewHolder, ChatMessage, replaceNull("hide_font_effects"));
        findAndHookMethod(TextPreviewActivity, "a", int.class, replaceNull("hide_font_effects"));

        if (isMoreThan760()) {
            // 隐藏大字体特效
            Method[] methods = findMethodsByExactParameters(FontManager, boolean.class, ChatMessage);
            for (Method method : methods) {
                if (method.getName()
                        .equals("b")) {
                    hookMethod(method, replaceFalse("hide_font_effects"));
                }
            }
        }
    }

    /**
     * 隐藏群聊入场动画
     */
    private void hideGroupChatAdmissions() {
        if (!getBool("hide_group_chat_admissions")) {
            return;
        }
        if (TroopEnterEffectController == null) {
            return;
        }
        Field[] fields = TroopEnterEffectController.getDeclaredFields();
        for (Field field : fields) {
            if (field.toString()
                    .contains("final")) {
                field.setAccessible(true);
                try {
                    field.set(null, "");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 隐藏顶部提示
     */
    private void hideTopBar(String key, String... strs) {
        findAndHookMethod(ChatActivityUtils, "a", Context.class, String.class, View.OnClickListener.class, View.OnClickListener.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                String str = param.args[1].toString();
                for (String s : strs) {
                    if (str.contains(s) && getBool(key)) {
                        param.setResult(null);
                    }
                }
            }
        });
    }

    private void simpleItem(String key, int... rs) {
        findAndHookMethod(ItemBuilderFactory, "a", ChatMessage, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if (!getBool(key)) {
                    return;
                }
                int result = (int) param.getResult();
                for (int i : rs) {
                    if (i == result) {
                        param.setResult(-1);
                    }
                }
            }
        });
    }

    private void hideItemBuilder(Class<?> ItemBuilder, String key) {
        findAndHookMethod(ItemBuilder, "a", MessageRecord, AbstractChatItemBuilder$ViewHolder, View.class, LinearLayout.class, OnLongClickAndTouchListener, replaceNull(key));
    }

    private void hideGrayTips(Class<?> Tips, String key) {
        findAndHookMethod(Tips, "a", Object[].class, replaceNull(key));
    }

    private void hideGrayTipsItem(String key, String... regex) {
        findAndHookMethod(GrayTipsItemBuilder, "a", MessageRecord, AbstractChatItemBuilder$ViewHolder, View.class, LinearLayout.class, OnLongClickAndTouchListener, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (!getBool(key)) {
                    return;
                }
                Field field = XposedHelpers.findFieldIfExists(MessageRecord, "msg");
                if (field == null) {
                    return;
                }
                String msg = (String) field.get(param.args[0]);
                if (msg == null) {
                    return;
                }
                for (String str : regex) {
                    if (Pattern.matches(str, msg)) {
                        param.setResult(null);
                    }
                }
            }
        });
    }
}
