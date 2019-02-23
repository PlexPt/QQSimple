package me.zpp0196.qqpurify.hook;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import me.zpp0196.qqpurify.utils.ReflectionUtils;
import me.zpp0196.qqpurify.utils.XPrefUtils;

/**
 * Created by zpp0196 on 2019/2/8.
 */

public class ChatHook extends AbstractHook {
    @Override
    public void init() throws Throwable {
        hideItemLayout();
        hideFeedAndAd();
        hideEmotion();
        // 隐藏底部工具栏
        hidePanel();
        // 屏蔽提示
        if (getBool("chat_hide_grayTips", true)) {
            hideGrayTipsItem();
        }
    }

    private void hideItemLayout() {
        // 隐藏头像挂件
        if (getBool("chat_hide_avatarPendant", true)) {
            findAndHookMethod(BaseChatItemLayout, "setPendantImage", Drawable.class, XC_MethodReplacement.returnConstant(null));
        }
        // 隐藏个性气泡
        if (getBool("chat_hide_bubble", true)) {
            findAndHookMethod(BubbleManager, "a", String.class, BitmapFactory.Options.class, XC_MethodReplacement.returnConstant(null));
        }
        // 隐藏字体特效
        if (getBool("chat_hide_fontEffects", true)) {
            findAndHookMethod(TextItemBuilder, "a", BaseBubbleBuilder$ViewHolder, ChatMessage, XC_MethodReplacement.returnConstant(null));
            findAndHookMethod(TextPreviewActivity, "a", int.class, XC_MethodReplacement.returnConstant(null));
            findAndHookMethod(FontManager, boolean.class, "b", new Class[]{findClass(ChatMessage)}, XC_MethodReplacement.returnConstant(false));
        }
    }

    private void hideFeedAndAd() {
        // 隐藏取消隐藏不常用联系人提示
        hideGrayTips(GatherContactsTips);
        // 隐藏设置特别消息提示音
        hideGrayTips(VipSpecialCareGrayTips);
        // 隐藏搜狗输入法广告
        hideGrayTips(SougouInputGrayTips);
    }

    private void hideEmotion() {
        // 隐藏表情掉落
        if (getBool("chat_hide_dropExpression", true)) {
            findAndHookMethod(AioAnimationConfigHelper, "a", XC_MethodReplacement.returnConstant(null));
        }
        // 隐藏表情联想
        if (getBool("chat_hide_associatedExpression", true)) {
            findAndHookMethod(BaseChatPie, "a", Editable.class, XC_MethodReplacement.returnConstant(null));
        }
        // 隐藏推荐表情
        if (getBool("chat_hide_hotExpression", true)) {
            findAndHookMethod(EmoticonManager, "a", boolean.class, int.class, boolean.class, XC_MethodReplacement.returnConstant(new ArrayList<>()));
        }
    }

    private void hidePanel() {
        List<String> list = XPrefUtils.getStringList("chat_hide_panel");
        Class<?> AIOPanelUtiles = findClass(super.AIOPanelUtiles);
        findAndHookMethod(PanelIconLinearLayout, void.class, "a", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                ArrayList arrayList = getObjectIfExists(param.thisObject, ArrayList.class, "a");
                if (arrayList == null || arrayList.isEmpty())
                    return;
                for (String temp : list) {
                    char[] fields = temp.toCharArray();
                    for (char field : fields) {
                        arrayList.remove(ReflectionUtils.getStaticObjectIfExists(AIOPanelUtiles, int[].class, String.valueOf(field)));
                    }
                }
            }
        });
    }

    private void hideGrayTips(String className) {
        findAndHookMethod(className, "a", Object[].class, XC_MethodReplacement.returnConstant(null));
    }

    private void hideGrayTipsItem() {
        String value = XPrefUtils.getPref()
                .getString("chat_grayTips_keywords", "会员 礼物 送给 豪气 魅力 进场");
        String[] keywords = value.split(" ");
        Class<?> MessageRecord = findClass(super.MessageRecord);
        findAndHookMethod(GrayTipsItemBuilder, "a", MessageRecord, AbstractChatItemBuilder$ViewHolder, View.class, LinearLayout.class, OnLongClickAndTouchListener, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String msg = ReflectionUtils.getObjectIfExists(MessageRecord, String.class, "msg", param.args[0]);
                for (String keyword : keywords) {
                    if (msg != null && !TextUtils.isEmpty(keyword) && msg.contains(keyword)) {
                        param.setResult(null);
                    }
                }
            }
        });
    }
}

