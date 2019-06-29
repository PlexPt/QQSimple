package me.zpp0196.qqpurify.hook;

import android.app.Activity;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.Context;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.zpp0196.library.xposed.XC_MemberHook;
import me.zpp0196.library.xposed.XField;
import me.zpp0196.library.xposed.XMethod;
import me.zpp0196.library.xposed.XMethodHook;
import me.zpp0196.qqpurify.hook.annotation.MethodHook;
import me.zpp0196.qqpurify.hook.annotation.VersionSupport;
import me.zpp0196.qqpurify.hook.base.BaseHook;
import me.zpp0196.qqpurify.hook.utils.QQConfigUtils;

/**
 * Created by zpp0196 on 2018/5/15.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class MainuiHook extends BaseHook {

    private List<String> mHideFriendGroups = new ArrayList<>();
    private List<String> mHideTabNum = new ArrayList<>();

    public MainuiHook(Context context) {
        super(context);
    }

    @Override
    public void init() {
        super.init();
        hideTabsNum();
    }

    // region 消息
    @MethodHook(desc = "隐藏SVIP铭牌")
    @VersionSupport(min = QQ_800)
    public void hideSvipNameplate() {
        // SVIP铭牌
        XMethodHook.create($(VipUtils)).method(short.class, "a").params(AppRuntime,
                String.class).replace((short) 0);
        // 会员红名
        XMethodHook.create($(VipUtils)).method(int.class, "a").params(QQAppInterface,
                String.class, boolean.class).replace(4);
    }

    @MethodHook(desc = "隐藏小程序入口")
    @VersionSupport(min = QQ_800)
    public void hideMiniAppEntry() {
        String F = QQConfigUtils.getMethod("mainui_imael", "F");
        // initMicroAppEntryLayout
        XMethodHook.create($(Conversation)).method(F).intercept();
    }

    @MethodHook(desc = "隐藏快捷入口")
    public void hidePopupMenu(final List<String> list) {
        if (list.contains("隐藏所有")) {
            hideViewAfterCreateTab(Conversation, ImageView.class, "a");
            return;
        }

        XMethodHook hook = XMethodHook.create($(PopupMenuDialog)).callback(new XC_MemberHook() {
            @Override
            protected void onBeforeHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                if (param.args.length < 2 || !(param.args[1] instanceof List)) {
                    return;
                }
                List<Object> items = param.args(1);
                Object addItem = null;
                String addTitle = "加好友/群";
                Iterator iterable = items.iterator();
                while (iterable.hasNext()) {
                    Object item = iterable.next();
                    String title = XField.create(item).exact(String.class, "a").get();
                    log("快捷入口: " + title);
                    if (title.equals(addTitle)) {
                        addItem = item;
                    }
                    if (list.contains(title)) {
                        iterable.remove();
                    }
                }
                if (items.isEmpty()) {
                    log("请至少保留一个入口，否则会导致QQ闪退");
                    items.add(addItem);
                }
            }
        });
        hook.method("a").hookAll();
        hook.method("b").hookAll();
    }

    // endregion

    // region 联系人
    @MethodHook(key = KEY_HIDE_NEW_FRIEND, desc = "隐藏新朋友")
    public void hideNewFriend() {
        mHideTabNum.add("33");
        hideViewAfterCreateTab(Contacts, View.class, "a");
    }

    @MethodHook(desc = "隐藏滑动分组")
    public void hideSlidingIndicator(final List<String> list) {
        XMethodHook.create($(SimpleSlidingIndicator)).method(void.class, "a")
                .hook(new XC_MemberHook() {
                    @Override
                    protected void onAfterHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                        LinearLayout layout = XField.create(param.thisObject)
                                .exact(LinearLayout.class, "a").get();
                        for (int i = 0; i < layout.getChildCount(); i++) {
                            if (list.contains(String.valueOf(i))) {
                                hideView(layout.getChildAt(i));
                            }
                        }
                    }
                });

        // 左右滑动(一开始只有我和上帝知道这些代码怎么写出来的，现在只有上帝知道了)
        XMethodHook.create($(SimpleSlidingIndicator)).params(int.class, boolean.class, boolean.class)
                .method("a").hook(new XC_MemberHook() {

            private XC_MemberHook.MemberHookParam param;
            private int i;
            private int b;

            @Override
            protected void onBeforeHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                this.i = param.args(0);
                this.b = XField.create(param).exact(int.class, "b").get();
                this.param = param;
                boolean isHideDevice = list.contains("2");
                boolean isHidePhone = list.contains("3");
                boolean isHidePubAccount = list.contains("4");

                if (isHideDevice && !isHidePhone && !isHidePubAccount) {
                    f(2);
                }
                if (isHideDevice && isHidePhone && !isHidePubAccount) {
                    f(2, 3);
                }
                if (isHideDevice && !isHidePhone && isHidePubAccount) {
                    param.setArgs(0, (b == 1 && i == 2) ? 3 : (b == 3 && i == 4) ? 0 : param.args[0]);
                }
                if (isHideDevice && isHidePhone && isHidePubAccount) {
                    if (i != 2 && i != 3 && i != 4) {
                        return;
                    }
                    m(1, 0);
                }
                if (!isHideDevice && isHidePhone && !isHidePubAccount) {
                    f(3);
                }
                if (!isHideDevice && isHidePhone && isHidePubAccount) {
                    f(3, 4);
                }
                if (!isHideDevice && !isHidePhone && isHidePubAccount) {
                    f(4);
                }
            }

            private void f(int pos) {
                if (i != pos) {
                    return;
                }
                m(pos - 1, pos != 4 ? pos + 1 : 0);
            }

            private void f(int pos1, int pos2) {
                if (i != pos1 && i != pos2) {
                    return;
                }
                m(pos1 - 1, pos2 != 4 ? pos2 + 1 : 0);
            }

            private void m(int left, int right) {
                param.setArgs(0, b == left ? right : b == right ? left : param.args[0]);
            }
        });
    }

    @MethodHook(desc = "隐藏特别关心分组")
    public void hideSpecialCare() {
        mHideFriendGroups.add("特别关心");
        hideFriendGroups(null);
    }

    @MethodHook(desc = "隐藏好友分组")
    public void hideFriendGroups(final List<String> list) {
        if (list != null) {
            mHideFriendGroups.addAll(list);
        }
        XMethodHook.create($(BuddyListAdapter)).method("a").params(ArrayList.class,
                SparseArray.class, SparseIntArray.class).hook(new XC_MemberHook() {
            @Override
            protected void onAfterHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                ArrayList groupsList = param.args(0);
                Iterator iterator = groupsList.iterator();
                while (iterator.hasNext()) {
                    Object groups = iterator.next();
                    String groupName = XField.create(groups).name("group_name").get();
                    if (mHideFriendGroups.contains(groupName)) {
                        iterator.remove();
                    }
                }
            }
        });
    }

    @MethodHook(desc = "隐藏不常用联系人")
    @SuppressWarnings("UnnecessaryLocalVariable")
    public void hideUnusualContacts() {
        String listClassName = ContactsFPSPinnedHeaderExpandableListView;
        XMethodHook.create($(listClassName)).method("addFooterView").hook(new XC_MemberHook() {
            @Override
            protected void onBeforeHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                View view = param.args(0);
                if (!(view instanceof FrameLayout)) {
                    return;
                }
                TextView textView = (TextView) ((FrameLayout) view).getChildAt(0);
                if (textView.getText().equals("不常用联系人")) {
                    param.setResult(null);
                }
            }
        });
    }
    // endregion

    // region 动态
    @MethodHook(desc = "隐藏热搜")
    public void hideHotSearch() {
        XMethodHook.create($(Leba)).method("a").hookAll(new XC_MemberHook() {
            @Override
            protected void onBeforeHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                if (param.args.length != 1 || !(param.args(0) instanceof List)) {
                    return;
                }
                List list = param.args(0);
                if (list == null || list.isEmpty()) {
                    return;
                }
                Object item = list.get(0);
                if (item != null && item.getClass().getName().contains("HotSearchItem")) {
                    param.setResult(null);
                }
            }
        });
    }

    @MethodHook(desc = "隐藏动态入口")
    @VersionSupport(min = QQ_800)
    public void hideLebaList(final List<String> list) {
        if (list.contains("动态")) {
            XMethodHook.create($(Leba)).method("onInflate").hook(new XC_MemberHook() {
                @Override
                protected void onAfterHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                    View c = XField.create(param).exact(View.class, "c").get();
                    hideView((ViewGroup) c.getParent());
                }
            });
        }
        XMethodHook.create($(LebaShowListManager)).method("a")
                .params(QQAppInterface).hook(new XC_MemberHook() {
            @Override
            protected void onAfterHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                List itemList = param.getResult();
                Iterator iterator = itemList.iterator();
                while (iterator.hasNext()) {
                    Object item = iterator.next();
                    Object rpi = XField.create(item).type(ResourcePluginInfo).get();
                    String strPkgName = XField.create(rpi).name("strPkgName").get();
                    log("动态列表: " + strPkgName);
                    if (list.contains(strPkgName)) {
                        iterator.remove();
                    }
                }
            }
        });
    }
    // endregion

    // region 其他
    @MethodHook(desc = "隐藏菜单")
    public void hideMenu(final List<String> list) {
        XMethodHook.create($(MainFragment)).method("a").params(int.class, KeyEvent.class)
                .hook(new XC_MemberHook() {
                    @Override
                    protected void onAfterHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                        if (param.args(0, Integer.class) != KeyEvent.KEYCODE_MENU) {
                            return;
                        }
                        Dialog dialog = XField.create(param).exact(Dialog.class, "b").get();
                        LinearLayout dl = XField.create(dialog).exact(LinearLayout.class, "a").get();
                        for (int i = 0; i < dl.getChildCount(); i++) {
                            ViewGroup child = (ViewGroup) dl.getChildAt(i);
                            View title = child.getChildAt(0);
                            if (!(title instanceof TextView)) {
                                continue;
                            }
                            if (list.contains(((TextView) title).getText().toString())) {
                                hideView(child);
                            }
                        }
                    }
                });
    }

    @MethodHook(desc = "隐藏底部分组")
    @VersionSupport(min = QQ_800)
    public void hideTabs(final List<String> list) {
        if (list.contains("-1")) {
            XMethodHook.create($(MainFragment)).method("onViewCreated").hook(new XC_MemberHook() {
                @Override
                protected void onAfterHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                    Activity activity = XMethod.create(param).name("getActivity").invoke();
                    hideView(activity.findViewById(android.R.id.tabs));
                    hideView(XField.create(param).type(QQBlurView).get());
                }
            });
        }
    }

    @MethodHook(desc = "隐藏分组消息数量")
    public void hideTabsNum(List<String> list) {
        mHideTabNum.addAll(list);
    }

    private void hideTabsNum() {
        // 联系人和动态
        XMethodHook.create($(MainFragment)).params(int.class, BusinessInfoCheckUpdate$RedTypeInfo)
                .method("a").hook(new XC_MemberHook() {
            @Override
            protected void onBeforeHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                int i = param.args(0);
                switch (i) {
                    case 33:
                        if (mHideTabNum.contains("33") || mSetting.get(KEY_HIDE_NEW_FRIEND, false)) {
                            param.setResult(null);
                        }
                        return;
                    case 34:
                        if (mHideTabNum.contains("34")) {
                            param.setResult(null);
                        }
                }
            }
        });
        // 消息
        XMethodHook.create($(MainFragment)).method("a").params(int.class, int.class, Object.class)
                .hook(new XC_MemberHook() {
                    @Override
                    protected void onBeforeHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                        int i = param.args(0);
                        if (!mHideTabNum.contains(String.valueOf(i))) {
                            return;
                        }
                        if (i == 32) {
                            param.setResult(null);
                        }
                    }
                });
    }

    @MethodHook(desc = "模拟菜单")
    public void simulateMenu() {
        doSthAfterTabCreated(Conversation, frame -> {
            RelativeLayout b = XField.create(frame).exact(RelativeLayout.class, "b").get();
            b.setOnClickListener(v -> new Thread(() -> {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start());
        });
    }

    @Override
    public String getSettingGroup() {
        return SETTING_MAINUI;
    }
    // endregion
}
