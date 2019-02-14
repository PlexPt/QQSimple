package me.zpp0196.qqpurify.hook;

import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import me.zpp0196.qqpurify.utils.XPrefUtils;

/**
 * Created by zpp0196 on 2019/2/8.
 */

public class ContactsHook extends AbstractHook {
    @Override
    public void init() throws Throwable {
        if(getBool("contacts_display_tab", true)) {
            hideTopContent();
            hideContactsTabs();
            // 隐藏不常用联系人
            if (getBool("contacts_hide_unusualContacts")) {
                findAndHookMethod(FriendFragment, "i", hideViewAfterMethod(View.class, "b"));
                findAndHookMethod(FriendFragment, "j", hideViewAfterMethod(View.class, "b"));
            }
        }
    }

    private void hideTopContent() {
        findAndHookMethod(Contacts, "o", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // 隐藏新朋友
                findAndHideView(param.thisObject, View.class, "a", "contacts_hide_newFriend");
                // 隐藏创建群聊
                findAndHideView(param.thisObject, View.class, "b", "contacts_hide_createTroop");
            }
        });
    }

    private void hideContactsTabs() {
        // 隐藏联系人分组
        List<Integer> list = XPrefUtils.getIntegerList("contacts_hide_tabs");
        findAndHookMethod(SimpleSlidingIndicator, "a", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LinearLayout layout = getObjectIfExists(param.thisObject, LinearLayout.class, "a");
                for (int i = 0; i < layout.getChildCount(); i++) {
                    if (list.contains(i)) {
                        hideView(layout.getChildAt(i));
                    }
                }
            }
        });

        // 左右滑动(一开始只有我和上帝知道这些代码怎么写出来的，现在只有上帝知道了)
        findAndHookMethod(SimpleSlidingIndicator, "a", int.class, boolean.class, boolean.class, new XC_MethodHook() {

            private MethodHookParam param;
            private int i;
            private int b;

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                this.i = (int) param.args[0];
                this.b = getObjectIfExists(param.thisObject, int.class, "b");
                this.param = param;
                boolean isHideDevice = list.contains(2);
                boolean isHidePhone = list.contains(3);
                boolean isHidePubAccount = list.contains(4);
                if (isHideDevice && !isHidePhone && !isHidePubAccount) {
                    f(2);
                }
                if (isHideDevice && isHidePhone && !isHidePubAccount) {
                    f(2, 3);
                }
                if (isHideDevice && !isHidePhone && isHidePubAccount) {
                    param.args[0] = (b == 1 && i == 2) ? 3 : (b == 3 && i == 4) ? 0 : param.args[0];
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
                param.args[0] = b == left ? right : b == right ? left : param.args[0];
            }
        });
    }
}
