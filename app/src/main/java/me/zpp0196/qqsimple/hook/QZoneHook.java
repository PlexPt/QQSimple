package me.zpp0196.qqsimple.hook;

import android.content.Context;
import android.widget.ImageView;

import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import me.zpp0196.qqsimple.Common;
import me.zpp0196.qqsimple.hook.base.BaseHook;

import static me.zpp0196.qqsimple.hook.comm.Maps.qzoneMenuItem;
import static me.zpp0196.qqsimple.hook.comm.Maps.qzoneNavItem;
import static me.zpp0196.qqsimple.hook.util.HookUtil.getQQVersionName;
import static me.zpp0196.qqsimple.hook.util.HookUtil.isMoreThan760;

/**
 * Created by zpp0196 on 2018/4/1.
 */

class QZoneHook extends BaseHook {

    private ClassLoader qqClassLoader;
    private ClassLoader qzoneClassLoader;

    private Class<?> DynamicPhotoAdapter;
    private Class<?> entrance_cfg;
    private Class<?> LocalConfig;
    private Class<?> NavigatorBar;
    private Class<?> NavigatorItem;
    private Class<?> OperationItem;
    private Class<?> PlusMenuContainer;
    private Class<?> QzoneAvatarDecorator;
    private Class<?> QZoneFeedsHeader;
    private Class<?> QZoneFriendFeedFragment;


    QZoneHook(ClassLoader qqClassLoader, ClassLoader qzoneClassLoader) {
        this.qqClassLoader = qqClassLoader;
        this.qzoneClassLoader = qzoneClassLoader;
    }

    @Override
    public void init() {
        initClass();
        hidePlusMenuConstants();
        hideNavConstants();
        hideOther();
    }

    private void initClass() {
        if (DynamicPhotoAdapter == null) {
            DynamicPhotoAdapter = findClassInQzone("com.qzone.publish.ui.adapter.DynamicPhotoAdapter");
        }
        if (entrance_cfg == null) {
            entrance_cfg = findClassInQQ("NS_UNDEAL_COUNT.entrance_cfg");
        }
        if (LocalConfig == null) {
            LocalConfig = findClassInQzone("com.qzone.config.LocalConfig");
        }
        if (NavigatorBar == null) {
            NavigatorBar = findClassInQzone("com.qzone.navigationbar.NavigatorBar");
        }
        if (NavigatorItem == null) {
            NavigatorItem = findClassInQzone("com.qzone.navigationbar.NavigatorItem");
        }
        if (OperationItem == null) {
            OperationItem = findClassInQzone("com.qzone.plusoperation.OperationItem");
        }
        if (PlusMenuContainer == null) {
            PlusMenuContainer = findClassInQzone("com.qzone.plusoperation.PlusMenuContainer");
        }
        if (QzoneAvatarDecorator == null) {
            QzoneAvatarDecorator = findClassInQzone("com.qzone.cover.ui.QzoneAvatarDecorator");
        }
        if (QZoneFeedsHeader == null) {
            QZoneFeedsHeader = findClassInQzone("com.qzone.widget.QZoneFeedsHeader");
        }
        if (QZoneFriendFeedFragment == null) {
            QZoneFriendFeedFragment = findClassInQzone("com.qzone.feed.ui.activity.QZoneFriendFeedFragment");
        }
    }

    /**
     * 隐藏加号菜单内容
     */
    @SuppressWarnings ("unchecked")
    private void hidePlusMenuConstants() {
        findAndHookMethod(PlusMenuContainer, "b", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                ArrayList arrayList = getObject(param.thisObject, ArrayList.class, "a");
                ArrayList needRemove = new ArrayList();
                for (Object item : arrayList) {
                    String title = getObject(OperationItem, String.class, "a", item);
                    for (String key : qzoneMenuItem.keySet()) {
                        String value = qzoneMenuItem.get(key);
                        if (title.equals(value) && getBool(key)) {
                            needRemove.add(item);
                        }
                    }
                }
                arrayList.removeAll(needRemove);
            }
        });
    }

    /**
     * 隐藏导航栏
     */
    @SuppressWarnings ("unchecked")
    private void hideNavConstants() {
        // 隐藏小红点
        findAndHookMethod(NavigatorItem, "b", Context.class, replaceNull("hide_qzone_nav_redDot"));

        // 隐藏导航栏
        findAndHookMethod(NavigatorBar, "b", Context.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                ArrayList arrayList = getObject(param.thisObject, ArrayList.class, "a");
                ArrayList needRemove = new ArrayList();
                for (Object item : arrayList) {
                    String title = getObject(entrance_cfg, String.class, "sEntranceName", item);
                    for (String key : qzoneNavItem.keySet()) {
                        String value = qzoneNavItem.get(key);
                        if (title.equals(value) && getBool(key)) {
                            needRemove.add(item);
                        }
                    }
                }
                arrayList.removeAll(needRemove);
            }
        });
        // 隐藏消息
        findAndHookMethod(QZoneFriendFeedFragment, isMoreThan760() ? "n_" :
                "t_", hideView(ImageView.class, "d", "hide_qzone_nav_news"));
    }

    private void hideOther() {
        // hideView("qzone_super_font_tab_reddot");
        findAndHookMethod(LocalConfig, "a", String.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (getBool(Common.PREFS_KEY_HIDE_SOME_RED_DOT) && param.args[0].toString()
                        .contains("SuperFontRedIcon")) {
                    param.setResult(0);
                }
            }
        });
        // hideView("qzone_uploadphoto_item_reddot");
        findAndHookMethod(DynamicPhotoAdapter, "c", boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (getBool(Common.PREFS_KEY_HIDE_SOME_RED_DOT)) {
                    param.args[0] = false;
                }
            }
        });
    }

    private Class<?> findClassInQQ(String className) {
        try {
            return qqClassLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            log("%s Can't find the Class of name: %s!", getQQVersionName(), className);
        }
        return null;
    }

    private Class<?> findClassInQzone(String className) {
        try {
            return qzoneClassLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            log("%s Can't find the Class of name: %s in QzonePlugin!", getQQVersionName(), className);
        }
        return null;
    }
}
