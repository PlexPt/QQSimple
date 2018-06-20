package me.zpp0196.qqsimple.hook;

import android.content.Context;
import android.view.View;

import de.robv.android.xposed.XC_MethodHook;
import me.zpp0196.qqsimple.hook.base.BaseHook;

import static me.zpp0196.qqsimple.hook.comm.Classes.ChatActivityUtils;
import static me.zpp0196.qqsimple.hook.comm.Classes.ChatMessage;
import static me.zpp0196.qqsimple.hook.comm.Classes.ItemBuilderFactory;
import static me.zpp0196.qqsimple.hook.comm.Classes.MessageForDeliverGiftTips;
import static me.zpp0196.qqsimple.hook.comm.Classes.TroopGiftAnimationController;

/**
 * Created by zpp0196 on 2018/6/14 0014.
 */

public class TroopHook extends BaseHook {
    @Override
    public void init() {
        simpleSignItem();
        hideTroopAssistantContent();
        hideTroopAnim();
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

        //        // 隐藏入场动画
        //        if (!getBool("hide_troop_enterAnim")) {
        //            return;
        //        }
        //        if (TroopEnterEffectController == null) {
        //            return;
        //        }
        //        Field[] fields = TroopEnterEffectController.getDeclaredFields();
        //        for (Field field : fields) {
        //            if (field.toString()
        //                    .contains("final")) {
        //                field.setAccessible(true);
        //                try {
        //                    field.set(null, "");
        //                } catch (IllegalAccessException e) {
        //                    e.printStackTrace();
        //                }
        //            }
        //        }
    }
}
