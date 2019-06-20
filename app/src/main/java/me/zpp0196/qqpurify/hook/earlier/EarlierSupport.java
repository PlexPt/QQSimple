package me.zpp0196.qqpurify.hook.earlier;

import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import me.zpp0196.library.xposed.XConstructorHook;
import me.zpp0196.library.xposed.XField;
import me.zpp0196.library.xposed.XLog;
import me.zpp0196.library.xposed.XMethodHook;
import me.zpp0196.qqpurify.hook.annotation.MethodHook;
import me.zpp0196.qqpurify.hook.annotation.VersionSupport;
import me.zpp0196.qqpurify.hook.base.BaseHook;
import me.zpp0196.qqpurify.hook.callback.XC_LogMethodHook;

import static me.zpp0196.qqpurify.hook.callback.XC_LogMethodHook.intercept;

/**
 * Created by zpp0196 on 2019/5/28.
 */
@SuppressWarnings("unused")
public class EarlierSupport extends BaseHook {

    public EarlierSupport(Context context) {
        super(context);
    }

    // region mainui
    @MethodHook(desc = "隐藏SVIP铭牌")
    @VersionSupport(min = 980, max = 1024)
    public void hideSvipNameplate() {
        XField.create($(VipUtils)).exact(int.class, "a").set(4);
    }

    @MethodHook(desc = "隐藏动态入口")
    @VersionSupport(group = SettingGroup.mainui, max = 1024)
    public void hideLebaList(final List<String> list) {
        XMethodHook.create($(Leba)).method("onInflate").hook(new XMethodHook.Callback() {
            @Override
            protected void after(XMethodHook.MethodParam param) {
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
    @VersionSupport(group = SettingGroup.mainui, max = 900)
    public void hideHonestSay() {
        XMethodHook honestSayHook = XMethodHook.create($(CardController)).method("a")
                .callback(intercept());
        honestSayHook.params(int.class, CommonCardEntry, int.class).hook();
        honestSayHook.params(int.class, CommonCardEntry).hook();
    }

    @MethodHook(desc = "隐藏创建群聊")
    @VersionSupport(group = SettingGroup.mainui, max = 1024)
    public void hideCreateTroop() {
        hideViewAfterCreateTab(Contacts, View.class, "b");
    }

    @MethodHook(desc = "隐藏底部分组")
    @VersionSupport(group = SettingGroup.mainui)
    public void hideTabs(final List<String> list) {
        XMethodHook.create($(MainFragment)).method(View[].class, "a")
                .params(View.class).hook(new XMethodHook.Callback() {
            @Override
            protected void after(XMethodHook.MethodParam param) {
                super.after(param);
                View[] views = XField.create(param).exact(View[].class, "a").get();
                for (String str : list) {
                    int index = Integer.parseInt(str);
                    if (index == 0) {
                        XLog.w(getTAG(), "不支持单独隐藏消息分组");
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
    @VersionSupport(group = SettingGroup.sidebar, max = 1024)
    public void hideCityWeather() {
        XConstructorHook.create($(QQSettingMe)).hook(new XMethodHook.Callback() {
            @Override
            protected void after(XMethodHook.MethodParam param) {
                hideView(XField.create(param).exact(LinearLayout.class, "b").get());
            }
        });
    }

    @MethodHook(desc = "隐藏厘米秀")
    @VersionSupport(group = SettingGroup.sidebar, max = 1186)
    public void hideApollo() {
        XMethodHook.create($(QQSettingMe)).method("a").params(ApolloManager$CheckApolloInfoResult)
                .hook(XC_LogMethodHook.intercept());
    }
    // endregion

    // region chat
    @MethodHook(desc = "隐藏表情联想")
    @VersionSupport(group = SettingGroup.chat, max = 1024)
    public void hideSticker() {
        XMethodHook.create($(BaseChatPie)).method("a").params(Editable.class).hook(intercept());
    }

    @MethodHook(desc = "隐藏字体特效")
    @VersionSupport(group = SettingGroup.chat)
    public void hideFont() {
        XMethodHook.create($(TextItemBuilder)).method("a")
                .params(BaseBubbleBuilder$ViewHolder, ChatMessage).hook(intercept());
        XMethodHook.create($(TextPreviewActivity)).method("a")
                .params(int.class).hook(intercept());
        XMethodHook.create($(FontManager)).method(boolean.class, "b")
                .params(ChatMessage).callback(XC_LogMethodHook.replace(false));
    }
    // endregion

    // region troop
    @MethodHook(desc = "隐藏在线人数")
    @VersionSupport(group = SettingGroup.troop, max = 1024)
    public void hideOnlineNumber() {
        XMethodHook.create($(BaseTroopChatPie)).method("b").params(boolean.class)
                .hook(new XC_LogMethodHook() {
                    @Override
                    protected void after(XMethodHook.MethodParam param) {
                        super.after(param);
                        TextView e = XField.create(param).exact(TextView.class, "e").get();
                        ((ViewGroup) e.getParent()).setVisibility(View.GONE);
                    }
                });
    }

    @MethodHook(desc = "隐藏群头衔")
    @VersionSupport(group = SettingGroup.troop, max = 1186)
    public void hideLevel() {
        XMethodHook.create($(BaseBubbleBuilder)).method("f").params(ChatMessage, BaseChatItemLayout)
                .hook(intercept());
        XMethodHook.create($(BaseChatItemLayout)).method("a").params(QQAppInterface, boolean.class,
                String.class, boolean.class, int.class, int.class).hook(intercept());
    }

    @MethodHook(desc = "隐藏魅力等级")
    @VersionSupport(group = SettingGroup.troop, max = 1186)
    public void hideGlamourLevel() {
        XMethodHook.create($(BaseBubbleBuilder)).method("g").params(ChatMessage, BaseChatItemLayout)
                .hook(intercept());
        XMethodHook.create($(BaseChatItemLayout)).method("a").params(QQAppInterface, boolean.class,
                int.class, boolean.class).hook(intercept());
    }
    // endregion

    @Override
    public SettingGroup getSettingGroup() {
        return SettingGroup.earlier;
    }
}
