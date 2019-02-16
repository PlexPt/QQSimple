package me.zpp0196.qqpurify.hook;

import android.content.Context;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import me.zpp0196.qqpurify.utils.ReflectionUtils;

/**
 * Created by zpp0196 on 2019/2/8.
 */

public class TroopHook extends AbstractHook {
    @Override
    public void init() throws Throwable {
        hideTopContent();
        hookChat();
        hideChatItemLayout();
        hideTroopAnim();
        hideTroopAssistantContent();
        hideTroopNoticeContent();
    }

    private void hideTopContent() {
        // 隐藏顶部总人数
        if (getBool("troop_hide_totalNumber")) {
            findAndHookMethod(TroopChatPie, "bq", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    TextView d = getObjectIfExists(param.thisObject, TextView.class, "d");
                    hideView(d);
                }
            });
        }
        // 隐藏顶部在线人数
        if (getBool("troop_hide_onlineNumber")) {
            findAndHookMethod(BaseTroopChatPie, "c", boolean.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    TextView f = getObjectIfExists(param.thisObject, TextView.class, "f");
                    ((ViewGroup) f.getParent()).setVisibility(View.GONE);
                    TextView e = getObjectIfExists(param.thisObject, TextView.class, "e");
                    e.setTextSize(2, 19f);
                    TextView d = getObjectIfExists(param.thisObject, TextView.class, "d");
                    d.setTextSize(2, 19f);
                }
            });
        }
    }

    private void hookChat() {
        // 防止被@
        if (getBool("troop_prevent_at")) {
            findAndHookMethod(MessageInfo, "a", QQAppInterface, String.class, MessageInfo, Object.class, MessageRecord, boolean.class, XC_MethodReplacement.returnConstant(null));
        }
        // 签到文本化
        if (getBool("troop_simple_sign", true)) {
            findAndHookMethod(ItemBuilderFactory, "a", ChatMessage, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    int result = (int) param.getResult();
                    if (result == 71 || result == 84) {
                        param.setResult(-1);
                    }
                }
            });
        }
    }

    private void hideChatItemLayout() {
        // 隐藏群头衔
        if (getBool("troop_hide_level")) {
            findAndHookMethod(BaseChatItemLayout, "a", QQAppInterface, boolean.class, String.class, boolean.class, int.class, int.class, hideViewAfterMethod(TextView.class, "c"));
        }
        // 隐藏魅力等级
        if (getBool("troop_hide_glamourLevel", true)) {
            findAndHookMethod(BaseChatItemLayout, "a", QQAppInterface, boolean.class, int.class, boolean.class, hideViewAfterMethod(TextView.class, "d"));
        }
        // 隐藏炫彩昵称 FIXME taichi
        if (getBool("troop_hide_colorNick", true)) {
            findAndHookMethod(ColorNickManager, "a", QQAppInterface, TextView.class, Spannable.class, XC_MethodReplacement.returnConstant(null));
        }
    }

    private void hideTroopAnim() throws Throwable {
        // 隐藏礼物动画
        if (getBool("troop_hide_giftAnim", true)) {
            findAndHookMethod(TroopGiftAnimationController, "a", MessageForDeliverGiftTips, XC_MethodReplacement.returnConstant(null));
        }
        // 隐藏入场动画
        if (getBool("troop_hide_enterAnim", true)) {
            ReflectionUtils.setStaticObjectField(findClass(TroopEnterEffectController), String.class, "a", "");
        }
    }

    private void hideTroopAssistantContent() {
        // 隐藏群助手里面的小视频
        if (getBool("troopAssistant_hide_smallVideo", true)) {
            findAndHookMethod(TroopAssistantActivity, "h", hideViewAfterMethod(View.class, "c"));
        }
        // 隐藏移出群助手提示
        if (getBool("troopAssistant_hide_removeTips", true)) {
            findAndHookMethod(ChatActivityUtils, "a", Context.class, String.class, View.OnClickListener.class, View.OnClickListener.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    String str = param.args[1].toString();
                    if (str.contains("移出群助手")) {
                        param.setResult(null);
                    }
                }
            });
        }
    }

    private void hideTroopNoticeContent() {
        // 隐藏群通知里面的群推荐
        if (getBool("troopNotice_hide_recommend", true)) {
            findAndHookMethod(NotifyAndRecAdapter, "getView", int.class, View.class, ViewGroup.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    int itemViewType = (int) XposedHelpers.callMethod(param.thisObject, "getItemViewType", param.args[0]);
                    if (itemViewType == 1 || itemViewType == 6) {
                        param.setResult(new View(((View) param.args[1]).getContext()));
                    }
                }
            });
        }
    }
}