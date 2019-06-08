package me.zpp0196.qqpurify.hook;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import me.zpp0196.library.xposed.XField;
import me.zpp0196.library.xposed.XLog;
import me.zpp0196.library.xposed.XMethodHook;
import me.zpp0196.qqpurify.hook.annotation.MethodHook;
import me.zpp0196.qqpurify.hook.annotation.VersionSupport;
import me.zpp0196.qqpurify.hook.base.BaseHook;
import me.zpp0196.qqpurify.hook.callback.XC_LogMethodHook;

import static me.zpp0196.qqpurify.hook.callback.XC_LogMethodHook.intercept;

/**
 * Created by zpp0196 on 2019/2/8.
 */
@SuppressWarnings("unused")
public class ChatHook extends BaseHook {

    public ChatHook(Context context) {
        super(context);
    }

    @Override
    public void init() {
        super.init();
        // 取消隐藏不常用联系人提示
        hideGrayTips(GatherContactsTips);
        // 设置特别消息提示音
        hideGrayTips(VipSpecialCareGrayTips);
        // 搜狗输入法广告
        hideGrayTips(SougouInputGrayTips);
    }

    private void hideGrayTips(String className) {
        XMethodHook.create($(className)).method("a").params(Object[].class).hook(intercept());
    }

    // region 装扮
    @MethodHook(desc = "隐藏头像挂件")
    public void hideAvatar() {
        XMethodHook.create($(BaseChatItemLayout)).method("setPendantImage")
                .params(Drawable.class).hook(intercept());
    }

    @MethodHook(desc = "隐藏个性气泡")
    public void hideBubble() {
        XMethodHook bubbleManagerHook = XMethodHook.create($(BubbleManager));
        bubbleManagerHook.method("a").params(String.class, BitmapFactory.Options.class).hook(intercept());
        bubbleManagerHook.method(boolean.class, "a").params(int.class, boolean.class)
                .hook(XC_LogMethodHook.replace(false));
    }

    @MethodHook(desc = "隐藏字体特效")
    @VersionSupport(min = 980)
    public void hideFont() {
        XMethodHook fontManagerHook = XMethodHook.create($(FontManager)).callback(intercept());
        fontManagerHook.method(void.class, "a").params(String.class).hook();
        fontManagerHook.method(void.class, "a").params(File.class).hook();
        fontManagerHook.method(FontInfo, "a").params(ChatMessage).hook();
    }
    // endregion

    // region 表情
    @MethodHook(desc = "隐藏表情掉落")
    public void hideAioEggs() {
        XMethodHook.create($(AioAnimationConfigHelper)).method(void.class, "a")
                .params(Context.class).hook(XC_LogMethodHook.intercept());
    }

    @MethodHook(desc = "隐藏表情联想")
    @VersionSupport(min = 1024)
    public void hideSticker() {
        XMethodHook.create($(StickerRecHelper)).method("a").params(Editable.class).hook(intercept());
    }

    @MethodHook(desc = "隐藏推荐表情")
    public void hideProEmoticon() {
        XMethodHook.create($(EmoticonManager)).method("a")
                .params(boolean.class, int.class, boolean.class)
                .hook(XC_LogMethodHook.replace(new ArrayList<>()));
    }
    // endregion

    // region 其他
    @MethodHook(desc = "隐藏工具栏")
    public void hidePanelIcon(final List<String> list) {
        XMethodHook.create($(PanelIconLinearLayout)).method(void.class, "a")
                .hook(new XC_LogMethodHook() {
                    @Override
                    protected void before(XMethodHook.MethodParam param) {
                        super.before(param);
                        ArrayList arrayList = XField.create(param)
                                .exact(ArrayList.class, "a")
                                .get();
                        if (arrayList == null || arrayList.isEmpty()) {
                            return;
                        }

                        LinearLayout layout = param.thisObject();
                        Context context = XField.create(param).type(Context.class).get();
                        Field[] fields = $(AIOPanelUtiles).getFields();
                        for (Field field : fields) {
                            if (field.getType() != int[].class) {
                                continue;
                            }
                            try {
                                int[] ids = (int[]) field.get(null);
                                int titleId = ids[1];
                                String title = context.getResources().getString(titleId);
                                if (list.contains(title)) {
                                    arrayList.remove(ids);
                                }
                                XLog.d(getTAG(), "聊天界面底部工具栏: " + title);
                            } catch (IllegalAccessException e) {
                                onBeforeError(param, e);
                            }
                        }
                    }
                });
    }

    @MethodHook(desc = "屏蔽提示")
    public void hideGrayTipItem() {
        String value = getString(KEY_GRAY_TIP_KEYWORDS);
        String[] keywords = value.split(" ");
        XMethodHook.create($(GrayTipsItemBuilder)).method("a").params(MessageRecord,
                AbstractChatItemBuilder$ViewHolder, View.class, LinearLayout.class,
                OnLongClickAndTouchListener).hook(new XC_LogMethodHook() {
            @Override
            protected void before(XMethodHook.MethodParam param) {
                super.before(param);
                String msg = XField.create(param.args[0]).exact(String.class, "msg").get();
                for (String keyword : keywords) {
                    if (msg != null && !TextUtils.isEmpty(keyword) && msg.contains(keyword)) {
                        param.setResult(null);
                    }
                }
            }
        });
    }
    // endregion

    @Override
    public SettingGroup getSettingGroup() {
        return SettingGroup.chat;
    }
}

