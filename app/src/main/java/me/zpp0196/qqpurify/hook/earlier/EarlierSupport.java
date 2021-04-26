package me.zpp0196.qqpurify.hook.earlier;

import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import me.zpp0196.library.xposed.XC_MemberHook;
import me.zpp0196.library.xposed.XConstructorHook;
import me.zpp0196.library.xposed.XField;
import me.zpp0196.library.xposed.XMethodHook;
import me.zpp0196.qqpurify.hook.annotation.MethodHook;
import me.zpp0196.qqpurify.hook.annotation.VersionSupport;
import me.zpp0196.qqpurify.hook.base.BaseHook;

/**
 * Created by zpp0196 on 2019/5/28.
 */
@SuppressWarnings("unused")
public class EarlierSupport extends BaseHook {

    public EarlierSupport(Context context) {
        super(context);
    }

    // region mainui
    @MethodHook(desc = "隐藏会员铭牌")
    @VersionSupport(min = QQ_795, max = QQ_800)
    public void hideVipNameplate() {
        XField.create($(VipUtils)).exact(int.class, "a").set(4);
    }

    @MethodHook(desc = "隐藏动态入口")
    @VersionSupport(group = SETTING_MAINUI, max = QQ_800)
    public void hideLebaList(final List<String> list) {
        XMethodHook.create($(Leba)).method("onInflate").hook(new XC_MemberHook() {
            @Override
            protected void onAfterHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                if (list.contains("动态")) {
                    hideView(XField.create(param).exact(View.class, "c").get());
                }
                if (list.contains("附近")) {
                    hideView(XField.create(param).exact(View.class, "d").get());
                }
                if (list.contains("com.tx.xingqubuluo.android")) {
                    hideView(XField.create(param).exact(View.class, "f").get());
                }
            }
        });
    }

    @MethodHook(desc = "隐藏坦白说入口")
    @VersionSupport(group = SETTING_MAINUI, max = QQ_795)
    public void hideHonestSay() {
        XMethodHook honestSayHook = XMethodHook.create($(CardController)).method("a")
                .beforeHooked(param -> param.setResult(null));
        honestSayHook.params(int.class, CommonCardEntry, int.class).hook();
        honestSayHook.params(int.class, CommonCardEntry).hook();
    }

    @MethodHook(desc = "隐藏创建群聊")
    @VersionSupport(group = SETTING_MAINUI, max = QQ_800)
    public void hideCreateTroop() {
        hideViewAfterCreateTab(Contacts, View.class, "b");
    }

    @MethodHook(desc = "隐藏底部分组")
    @VersionSupport(group = SETTING_MAINUI)
    public void hideTabs(final List<String> list) {
        XMethodHook.create($(MainFragment)).method(View[].class, "a")
                .params(View.class).hook(new XC_MemberHook() {
            @Override
            protected void onAfterHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                View[] views = XField.create(param).exact(View[].class, "a").get();
                for (String str : list) {
                    int index = Integer.parseInt(str);
                    if (index == 0) {
                        log("不支持单独隐藏消息分组");
                        continue;
                    }
                    hideView(views[index]);
                }
            }
        });
    }
    // endregion

    // region sidebar
    @MethodHook(desc = "隐藏城市天气")
    @VersionSupport(group = SETTING_SIDEBAR, max = QQ_800)
    public void hideCityWeather() {
        XConstructorHook.create($(QQSettingMe)).hook(new XC_MemberHook() {
            @Override
            protected void onAfterHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                hideView(XField.create(param).exact(LinearLayout.class, "b").get());
            }
        });
    }

    @MethodHook(desc = "隐藏厘米秀")
    @VersionSupport(group = SETTING_SIDEBAR, max = QQ_805)
    public void hideApollo() {
        XMethodHook.create($(QQSettingMe)).method("a").params(ApolloManager$CheckApolloInfoResult)
                .intercept();
    }
    // endregion

    // region chat
    @MethodHook(desc = "隐藏表情联想")
    @VersionSupport(group = SETTING_CHAT, max = QQ_800)
    public void hideSticker() {
        XMethodHook.create($(BaseChatPie)).method("a").params(Editable.class).intercept();
    }

    @MethodHook(desc = "隐藏字体特效")
    @VersionSupport(group = SETTING_CHAT)
    public void hideFont() {
        XMethodHook.create($(TextItemBuilder)).method("a")
                .params(BaseBubbleBuilder$ViewHolder, ChatMessage).intercept();
        XMethodHook.create($(TextPreviewActivity)).method("a")
                .params(int.class).intercept();
        XMethodHook.create($(FontManager)).method(boolean.class, "b")
                .params(ChatMessage).replace(false);
    }
    // endregion

    // region troop
    @MethodHook(desc = "隐藏在线人数")
    @VersionSupport(group = SETTING_TROOP, max = QQ_800)
    public void hideOnlineNumber() {
        XMethodHook.create($(BaseTroopChatPie)).method("b").params(boolean.class)
                .hook(new XC_MemberHook() {
                    @Override
                    protected void onAfterHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                        TextView e = XField.create(param).exact(TextView.class, "e").get();
                        ((ViewGroup) e.getParent()).setVisibility(View.GONE);
                    }
                });
    }
    // endregion

    @MethodHook(desc = "隐藏资料卡浮屏")
    @VersionSupport(max = QQ_798)
    public void hideColorScreen() {
        XMethodHook.create($(FriendProfileCardActivity)).method("k").intercept();
    }

    @Override
    public String getSettingGroup() {
        return SETTING_EARLIER;
    }
}
