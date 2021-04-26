package me.zpp0196.qqpurify.hook;

import android.content.Context;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import me.zpp0196.library.xposed.XC_MemberHook;
import me.zpp0196.library.xposed.XField;
import me.zpp0196.library.xposed.XMethod;
import me.zpp0196.library.xposed.XMethodHook;
import me.zpp0196.qqpurify.hook.annotation.MethodHook;
import me.zpp0196.qqpurify.hook.annotation.VersionSupport;
import me.zpp0196.qqpurify.hook.base.BaseHook;
import me.zpp0196.qqpurify.hook.utils.QQConfigUtils;

/**
 * Created by zpp0196 on 2019/2/8.
 */
@SuppressWarnings("unused")
public class TroopHook extends BaseHook {

    public TroopHook(Context context) {
        super(context);
    }

    @MethodHook(desc = "隐藏总人数")
    @VersionSupport(min = QQ_800)
    public void hideTotalNumber() {
        XMethodHook.create($(TroopChatPie)).method("a").params(String.class, boolean.class).
                hook(new XC_MemberHook() {
                    @Override
                    protected void onAfterHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                        TextView d = XField.create(param).exact(TextView.class, "d").get();
                        d.setVisibility(View.INVISIBLE);
                    }
                });
    }

    @MethodHook(desc = "隐藏在线人数")
    @VersionSupport(min = QQ_800)
    public void hideOnlineNumber() {
        XMethodHook.create($(BaseTroopChatPie)).method("c").params(boolean.class)
                .hook(new XC_MemberHook() {
                    @Override
                    protected void onAfterHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                        TextView f = XField.create(param).exact(TextView.class, "f").get();
                        ((ViewGroup) f.getParent()).setVisibility(View.GONE);
                    }
                });
    }

    @MethodHook(desc = "隐藏群头衔")
    @VersionSupport(min = QQ_805)
    public void hideLevel() {
        String bb = QQConfigUtils.getMethod("troop_level_bb", mQQVersionCode < QQ_805 ? "f" : "e");
        String ci = QQConfigUtils.getMethod("troop_level_ci", mQQVersionCode < QQ_805 ? "a" : "setTroopMemberLevel");
        XMethodHook.create($(BaseBubbleBuilder)).method(bb).params(ChatMessage, BaseChatItemLayout).intercept();
        XMethodHook.create($(BaseChatItemLayout)).method(ci).params(QQAppInterface, boolean.class,
                String.class, boolean.class, int.class, int.class).intercept();
    }

    @MethodHook(desc = "隐藏魅力等级")
    @VersionSupport(min = QQ_805)
    public void hideGlamourLevel() {
        String bb = QQConfigUtils.getMethod("troop_glamour_bb", mQQVersionCode < QQ_805 ? "g" : "f");
        String ci = QQConfigUtils.getMethod("troop_glamour_ci");
        XMethodHook.create($(BaseBubbleBuilder)).method(bb).params(ChatMessage, BaseChatItemLayout).intercept();
        XMethodHook.create($(BaseChatItemLayout)).method(ci).params(QQAppInterface, boolean.class,
                int.class, boolean.class).intercept();
    }

    @MethodHook(desc = "隐藏炫彩昵称")
    @VersionSupport(min = QQ_795)
    public void hideColorNick() {
        XMethodHook.create($(ColorNickManager)).method("a").params(QQAppInterface, TextView.class,
                Spannable.class).beforeHooked(param -> param.setResult(null)).hook();
    }

    @MethodHook(desc = "隐藏礼物动画")
    public void hideGiftAnim() {
        XMethodHook.create($(TroopGiftAnimationController)).method("a")
                .params(MessageForDeliverGiftTips).intercept();
    }

    @MethodHook(desc = "隐藏入场动画")
    public void hideEnterEffect() {
        XField.create($(TroopEnterEffectController)).exact(String.class, "a").set("");
    }

    @MethodHook(desc = "隐藏群助手顶部动态")
    public void hideAssistantDynamic() {
        XMethodHook.create($(TroopDynamicConfig)).method("a").params(String.class).intercept();
    }

    @MethodHook(desc = "隐藏移出群助手提示")
    public void hideAssistantRemoveTips() {
        XMethodHook.create($(ChatActivityUtils)).params(Context.class, String.class,
                View.OnClickListener.class, View.OnClickListener.class).method("a")
                .hook(new XC_MemberHook() {
                    @Override
                    protected void onBeforeHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                        String str = param.args(1);
                        if (str.contains("移出群助手")) {
                            param.setResult(null);
                        }
                    }
                });
    }

    @MethodHook(desc = "隐藏群通知里面的群推荐")
    public void hideNoticeRecommend() {
        XMethodHook.create($(NotifyAndRecAdapter)).method("getView").hook(new XC_MemberHook() {
            @Override
            protected void onAfterHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                int type = XMethod.create(param).name("getItemViewType").invoke(param.args[0]);
                if (type == 1 || type == 6) {
                    param.setResult(new View((param.args(1, View.class)).getContext()));
                }
            }
        });
    }

    @Override
    public String getSettingGroup() {
        return SETTING_TROOP;
    }
}