package me.zpp0196.qqsimple.hook;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.lang.reflect.Method;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import me.zpp0196.qqsimple.Common;
import me.zpp0196.qqsimple.hook.base.BaseHook;

import static me.zpp0196.qqsimple.hook.comm.Maps.qzoneMenuItem;
import static me.zpp0196.qqsimple.hook.comm.Maps.qzoneNavItem;
import static me.zpp0196.qqsimple.hook.util.HookUtil.getQQVersionName;
import static me.zpp0196.qqsimple.hook.util.HookUtil.isMoreThan758;
import static me.zpp0196.qqsimple.hook.util.HookUtil.isMoreThan760;
import static me.zpp0196.qqsimple.hook.util.HookUtil.isMoreThan765;

/**
 * Created by zpp0196 on 2018/4/1.
 */

class QZoneHook extends BaseHook {

    private ClassLoader qqClassLoader;
    private ClassLoader qzoneClassLoader;

    private Class<?> AsyncImageView;
    private Class<?> BusinessFeedData;
    private Class<?> CanvasCellCommentView;
    private Class<?> DynamicPhotoAdapter;
    private Class<?> entrance_cfg;
    private Class<?> FeedComment;
    private Class<?> FeedOperation;
    private Class<?> FeedView;
    private Class<?> GuideCommentBar;
    private Class<?> HotBannerManager;
    private Class<?> LocalConfig;
    private Class<?> NavigatorBar;
    private Class<?> NavigatorItem;
    private Class<?> OperationItem;
    private Class<?> PlusMenuContainer;
    private Class<?> PraiseListView;
    private Class<?> QzoneAvatarDecorator;
    private Class<?> QZoneCommWidget;
    private Class<?> QZoneFeedsHeader;
    private Class<?> QzoneFacadeDecorator;
    private Class<?> QZoneFriendFeedFragment;
    private Class<?> QZonePublishMoodActivity;
    private Class<?> QzoneQbossDecorator;
    private Class<?> QZoneUploadPhotoActivity;
    private Class<?> QZoneUploadQualityActivity;
    private Class<?> QzoneVipDecorator;
    private Class<?> SuperLikeView;
    private Class<?> VisitAndPraiseAvatarsView;


    QZoneHook(ClassLoader qqClassLoader, ClassLoader qzoneClassLoader) {
        this.qqClassLoader = qqClassLoader;
        this.qzoneClassLoader = qzoneClassLoader;
    }

    @Override
    public void init() {
        initClass();
        hidePlusMenuConstants();
        hideDecorator();
        hideFeedContent();
        hideNavConstants();
        hideAd();
        hideOther();
    }

    private void initClass() {
        if (AsyncImageView == null) {
            AsyncImageView = findClassInQzone("com.qzone.widget.AsyncImageView");
        }
        if (BusinessFeedData == null) {
            BusinessFeedData = findClassInQzone("com.qzone.proxy.feedcomponent.model.BusinessFeedData");
        }
        if (CanvasCellCommentView == null) {
            CanvasCellCommentView = findClassInQzone("com.qzone.module.feedcomponent.ui.canvasui.CanvasCellCommentView");
        }
        if (DynamicPhotoAdapter == null) {
            DynamicPhotoAdapter = findClassInQzone("com.qzone.publish.ui.adapter.DynamicPhotoAdapter");
        }
        if (entrance_cfg == null) {
            entrance_cfg = findClassInQQ("NS_UNDEAL_COUNT.entrance_cfg");
        }
        if (FeedComment == null) {
            FeedComment = findClassInQzone("com.qzone.module.feedcomponent.ui.FeedComment");
        }
        if (FeedOperation == null) {
            FeedOperation = findClassInQzone("com.qzone.module.feedcomponent.ui.FeedOperation");
        }
        if (FeedView == null) {
            FeedView = findClassInQzone("com.qzone.module.feedcomponent.ui.FeedView");
        }
        if (GuideCommentBar == null) {
            GuideCommentBar = findClassInQzone("com.qzone.module.feedcomponent.ui.GuideCommentBar");
        }
        if (HotBannerManager == null) {
            HotBannerManager = findClassInQzone("com.qzone.component.banner.manager.HotBannerManager");
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
        if (PraiseListView == null) {
            PraiseListView = findClassInQzone("com.qzone.module.feedcomponent.ui.PraiseListView");
        }
        if (QzoneAvatarDecorator == null) {
            QzoneAvatarDecorator = findClassInQzone("com.qzone.cover.ui.QzoneAvatarDecorator");
        }
        if (QZoneCommWidget == null) {
            QZoneCommWidget = findClassInQzone("com.qzone.commwidget.QZoneCommWidget");
        }
        if (QZoneFeedsHeader == null) {
            QZoneFeedsHeader = findClassInQzone("com.qzone.widget.QZoneFeedsHeader");
        }
        if (QzoneFacadeDecorator == null) {
            QzoneFacadeDecorator = findClassInQzone("com.qzone.cover.ui.QzoneFacadeDecorator");
        }
        if (QZoneFriendFeedFragment == null) {
            QZoneFriendFeedFragment = findClassInQzone("com.qzone.feed.ui.activity.QZoneFriendFeedFragment");
        }
        if (QZonePublishMoodActivity == null) {
            QZonePublishMoodActivity = findClassInQzone("com.qzone.publish.ui.activity.QZonePublishMoodActivity");
        }
        if (QzoneQbossDecorator == null) {
            QzoneQbossDecorator = findClassInQzone("com.qzone.cover.ui.QzoneQbossDecorator");
        }
        if (QZoneUploadPhotoActivity == null) {
            QZoneUploadPhotoActivity = findClassInQzone("com.qzone.publish.ui.activity.QZoneUploadPhotoActivity");
        }
        if (QZoneUploadQualityActivity == null) {
            QZoneUploadQualityActivity = findClassInQzone("com.qzone.publish.ui.activity.QZoneUploadQualityActivity");
        }
        if (QzoneVipDecorator == null) {
            QzoneVipDecorator = findClassInQzone("com.qzone.cover.ui.QzoneVipDecorator");
        }
        if (SuperLikeView == null) {
            SuperLikeView = findClassInQzone("com.qzone.module.feedcomponent.ui.common.SuperLikeView");
        }
        if (VisitAndPraiseAvatarsView == null) {
            VisitAndPraiseAvatarsView = findClassInQzone("com.qzone.module.feedcomponent.ui.VisitAndPraiseAvatarsView");
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

    private void hideDecorator() {
        Method method = findMethodIfExists(QZoneFeedsHeader, void.class, "a");
        // 隐藏头像装扮
        hookMethod(method, hideView(QzoneFacadeDecorator, "a", "hide_qzone_facadeDecorator"));
        // 隐藏我的黄钻
        hookMethod(method, hideView(QzoneQbossDecorator, "a", "hide_qzone_vipDecorator"));
        hookMethod(method, hideView(QzoneVipDecorator, "a", "hide_qzone_vipDecorator"));
        Method method1 = findMethodIfExists(QzoneQbossDecorator, void.class, "a");
        hookMethod(method1, replaceNull("hide_qzone_vipDecorator"));
    }

    private void hideFeedContent() {
        // 隐藏点赞按钮
        Method method = findMethodIfExists(FeedOperation, boolean.class, "a", BusinessFeedData);
        hookMethod(method, hideView(SuperLikeView, "a", "hide_qzone_btn_like"));
        findAndHookMethod(FeedOperation, "d", hideView(SuperLikeView, "a", "hide_qzone_btn_like"));

        findAndHookMethod(FeedComment, "b", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // 隐藏点赞列表
                hideView(param.thisObject, VisitAndPraiseAvatarsView, "a", "hide_qzone_likeList");
                hideView(param.thisObject, PraiseListView, "a", "hide_qzone_likeList");
                // 隐藏评论框
                hideView(param.thisObject, GuideCommentBar, "a", "hide_qzone_et_comment");
                // 隐藏评论内容
                hideView(param.thisObject, CanvasCellCommentView, "a", "hide_qzone_et_comment_content");
            }
        });

        findAndHookMethod(FeedComment, "a", BusinessFeedData, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                // 隐藏点赞列表
                hideView(param.thisObject, VisitAndPraiseAvatarsView, "a", "hide_qzone_likeList");
                hideView(param.thisObject, PraiseListView, "a", "hide_qzone_likeList");
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

    /**
     * 隐藏广告
     */
    private void hideAd() {
        String key = "hide_qzone_AD";
        XC_MethodHook hideQuality = hideView(AsyncImageView, "c", key);
        String methodName = isMoreThan758() ? "aU" : "aX";
        methodName = isMoreThan765() ? "aY" : methodName;
        findAndHookMethod(QZonePublishMoodActivity, methodName, hideQuality);
        findAndHookMethod(QZonePublishMoodActivity, "e", Bundle.class, hideQuality);
        findAndHookMethod(QZoneUploadQualityActivity, "a", String.class, String.class, String.class, String.class, String.class, String.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                hideView(param.thisObject, AsyncImageView, "a");
                hideView(param.thisObject, AsyncImageView, "b");
                hideView(param.thisObject, AsyncImageView, "c");
            }
        });
        findAndHookMethod(QZoneUploadPhotoActivity, "a", String.class, String.class, hideView(AsyncImageView, "c", key));
        findAndHookMethod(HotBannerManager, "i", replaceNull(key));
        findAndHookMethod(QZoneCommWidget, "a", Drawable.class, replaceNull(key));
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
