package me.zpp0196.qqsimple.hook;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;

import static me.zpp0196.qqsimple.hook.comm.Classes.BaseChatItemLayout;
import static me.zpp0196.qqsimple.hook.comm.Classes.BaseTroopChatPie;
import static me.zpp0196.qqsimple.hook.comm.Classes.ChatActivityUtils;
import static me.zpp0196.qqsimple.hook.comm.Classes.ChatMessage;
import static me.zpp0196.qqsimple.hook.comm.Classes.ItemBuilderFactory;
import static me.zpp0196.qqsimple.hook.comm.Classes.MessageForDeliverGiftTips;
import static me.zpp0196.qqsimple.hook.comm.Classes.MessageForPic;
import static me.zpp0196.qqsimple.hook.comm.Classes.MessageInfo;
import static me.zpp0196.qqsimple.hook.comm.Classes.MessageRecord;
import static me.zpp0196.qqsimple.hook.comm.Classes.OnLongClickAndTouchListener;
import static me.zpp0196.qqsimple.hook.comm.Classes.PicItemBuilder;
import static me.zpp0196.qqsimple.hook.comm.Classes.PicItemBuilder$Holder;
import static me.zpp0196.qqsimple.hook.comm.Classes.QQAppInterface;
import static me.zpp0196.qqsimple.hook.comm.Classes.TroopAssistantActivity;
import static me.zpp0196.qqsimple.hook.comm.Classes.TroopEnterEffectController;
import static me.zpp0196.qqsimple.hook.comm.Classes.TroopGiftAnimationController;
import static me.zpp0196.qqsimple.hook.util.HookUtil.isMoreThan735;

/**
 * Created by zpp0196 on 2018/6/14 0014.
 */

public class TroopHook extends ChatHook {
    @Override
    public void init() {
        simpleSignItem();
        hideTroopAssistantContent();
        hideTroopAnim();
        hideOther();
        // 隐藏进场动画提示
        hideGrayTipsItem("hide_troop_enterAnim", ".+进场.+");
        // 隐藏加入群提示
        hideGrayTipsItem("hide_chatTip_joinTroop", ".+加入了本群.+", ".+邀请.+加入.+");
        // 隐藏获得新头衔
        hideGrayTipsItem("hide_chatTip_newLevel", ".+获得.+头衔.+");
        // 隐藏礼物相关提示
        hideGrayTipsItem("hide_chatTip_troopGift", ".+礼物.+成为.+守护.+", ".+成为.+魅力.+", ".+成为.+豪气.+", ".+送给.+朵.+");
        // 防止被@
        findAndHookMethod(MessageInfo, "a", QQAppInterface, String.class, MessageInfo, Object.class, MessageRecord, boolean.class, replaceNull("hide_troopTip_all"));
        // 隐藏群头衔
        findAndHookMethod(BaseChatItemLayout, "a", QQAppInterface, boolean.class, String.class, boolean.class, int.class, int.class, hideView(TextView.class, "c", "hide_troop_memberLevel"));
        // 隐藏魅力等级
        findAndHookMethod(BaseChatItemLayout, "a", QQAppInterface, boolean.class, int.class, boolean.class, hideView(TextView.class, "d", "hide_troop_memberGlamourLevel"));
        // 隐藏群消息里的小视频
        findAndHookMethod(TroopAssistantActivity, "h", hideView(View.class, "c", "hide_troopAssistant_smallVideo"));
        // 隐藏贴表情按钮
        if (isMoreThan735()) {
            findAndHookMethod(PicItemBuilder, "a", BaseChatItemLayout, MessageForPic, OnLongClickAndTouchListener, PicItemBuilder$Holder, boolean.class, replaceNull("hide_troop_lightEmoji"));
        }
    }

    /**
     * 签到文本化
     */
    private void simpleSignItem() {
        findAndHookMethod(ItemBuilderFactory, "a", ChatMessage, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (!getBool("simple_troop_sign")) {
                    return;
                }
                int result = (int) param.getResult();
                if (result == 71 || result == 84) {
                    param.setResult(-1);
                }
            }
        });
    }

    private void hideTroopAssistantContent() {
        // 隐藏移出群助手提示
        findAndHookMethod(ChatActivityUtils, "a", Context.class, String.class, View.OnClickListener.class, View.OnClickListener.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String str = param.args[1].toString();
                if (!getBool("hide_troopAssistant_removeTips")) {
                    return;
                }
                if (str.contains("常") || str.contains("是否")) {
                    param.setResult(null);
                }
            }
        });
    }

    /**
     * 隐藏群聊动画
     */
    private void hideTroopAnim() {
        // 隐藏礼物动画
        findAndHookMethod(TroopGiftAnimationController, "a", MessageForDeliverGiftTips, replaceNull("hide_troop_giftAnim"));

        // 隐藏入场动画
        if (!getBool("hide_troop_enterAnim")) {
            return;
        }
        Field field = findField(TroopEnterEffectController, String.class, "a");
        try {
            field.set(null, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideOther() {
        // 隐藏在线人数
        if (getBool("hide_online_number")) {
            findAndHookMethod(BaseTroopChatPie, "b", boolean.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    TextView d = getObject(param.thisObject, TextView.class, "d");
                    TextView e = getObject(param.thisObject, TextView.class, "e");
                    ((ViewGroup) e.getParent()).setVisibility(View.GONE);
                    d.setTextSize(2, 19f);
                }
            });
        }
    }
}
