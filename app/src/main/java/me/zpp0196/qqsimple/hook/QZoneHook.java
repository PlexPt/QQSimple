package me.zpp0196.qqsimple.hook;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import me.zpp0196.qqsimple.Common;
import me.zpp0196.qqsimple.hook.base.BaseHook;
import me.zpp0196.qqsimple.hook.util.Util;

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
                if (OperationItem == null) {
                    return;
                }
                ArrayList arrayList = getObject(param.thisObject, ArrayList.class, "a");
                if (arrayList == null || arrayList.isEmpty()) {
                    return;
                }
                ArrayList needRemove = new ArrayList();
                for (Object item : arrayList) {
                    String title = (String) findField(OperationItem, String.class, "a").get(item);
                    if (title.equals("说说") && getBool("hide_qzone_plus_mood")) {
                        needRemove.add(item);
                    }
                    if (title.equals("相册") && getBool("hide_qzone_plus_album")) {
                        needRemove.add(item);
                    }
                    if (title.equals("拍摄") && getBool("hide_qzone_plus_shoot")) {
                        needRemove.add(item);
                    }
                    if (title.equals("签到") && getBool("hide_qzone_plus_signIn")) {
                        needRemove.add(item);
                    }
                    if (title.equals("小红包") && getBool("hide_qzone_plus_redPacket")) {
                        needRemove.add(item);
                    }
                    if (title.equals("直播") && getBool("hide_qzone_plus_live")) {
                        needRemove.add(item);
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
                if (entrance_cfg == null) {
                    return;
                }
                ArrayList arrayList = (ArrayList) findField(NavigatorBar, ArrayList.class, "a").get(param.thisObject);
                if (arrayList == null || arrayList.isEmpty()) {
                    return;
                }
                ArrayList needRemove = new ArrayList();
                for (Object item : arrayList) {
                    String title = (String) findField(entrance_cfg, String.class, "sEntranceName").get(item);
                    if (title.equals("相册") && getBool("hide_qzone_nav_album")) {
                        needRemove.add(item);
                    }
                    if (title.equals("说说") && getBool("hide_qzone_nav_mood")) {
                        needRemove.add(item);
                    }
                    if (title.equals("个性化") && getBool("hide_qzone_nav_dress")) {
                        needRemove.add(item);
                    }
                    if (title.equals("小游戏") && getBool("hide_qzone_nav_game")) {
                        needRemove.add(item);
                    }
                    if (title.equals("小视频") && getBool("hide_qzone_nav_video")) {
                        needRemove.add(item);
                    }
                }
                arrayList.removeAll(needRemove);
            }
        });
        // 隐藏消息
        findAndHookMethod(QZoneFriendFeedFragment,
                Util.isMoreThan760() ? "n_" : "t_", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        if (getBool("hide_qzone_nav_news")) {
                            ImageView imageView = getObject(param.thisObject, ImageView.class, "d");
                            if (imageView != null) {
                                imageView.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    private void hideOther() {
        // hideView("qzone_super_font_tab_reddot");
        findAndHookMethod(LocalConfig, "a", String.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
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
                super.beforeHookedMethod(param);
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
            log("%s Can't find the Class of name: %s!", Util.getQQVersion(), className);
        }
        return null;
    }

    private Class<?> findClassInQzone(String className) {
        try {
            return qzoneClassLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            log("%s Can't find the Class of name: %s in QzonePlugin!", Util.getQQVersion(), className);
        }
        return null;
    }
}
