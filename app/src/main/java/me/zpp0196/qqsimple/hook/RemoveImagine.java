package me.zpp0196.qqsimple.hook;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import me.zpp0196.qqsimple.util.SettingUtils;

import static me.zpp0196.qqsimple.hook.MainHook.getQQ_Version;

/**
 * Created by Deng on 2018/2/16.
 */

public class RemoveImagine {

    private Class<?> id;
    private Class<?> drawable;

    public RemoveImagine(Class<?> id, Class<?> drawable) {
        this.id = id;
        this.drawable = drawable;
    }

    public void remove() {
        XposedHelpers.findAndHookMethod(View.class, "setLayoutParams", ViewGroup.LayoutParams.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                View view = (View) param.thisObject;
                hideView(view, param);
            }
        });
    }

    private void hideView(View view, XC_MethodHook.MethodHookParam param) {
        int id = view.getId();
        boolean isHide = false;
        if (id == getId("adviewlayout")) {
            // 隐藏消息列表界面横幅广告
            isHide = SettingUtils.getValueHideChatListHeadAd();
        } else if (id == getId("search_container") || id == getId("search_box")) {
            // 隐藏搜索框
            isHide = SettingUtils.getValueHideSearchBox();
        } else if (id == getId("unchecked_msg_num")) {
            // 隐藏消息列表未读消息数量
            isHide = SettingUtils.getValueHideUncheckedMsgNum();
        } else if (id == getId("newFriendEntry")) {
            // 隐藏联系人界面新朋友
            isHide = SettingUtils.getValueHideNewFriendEntry();
        } else if (id == getId("createTroopEntry")) {
            // 隐藏联系人界面创建群聊
            isHide = SettingUtils.getValueHideCreateTroopEntry();
        } else if (id == getId("unusual_contacts_footerview")) {
            // 隐藏联系人界面不常用联系人
            isHide = SettingUtils.getValueHideUnusualContacts();
        } else if (id == getId("qzone_feed_entry")) {
            // 隐藏动态界面空间入口
            isHide = SettingUtils.getValueHideQzoneEntry();
        } else if (id == getId("near_people_entry")) {
            // 隐藏动态界面附近的人入口
            isHide = SettingUtils.getValueHideNearEntry();
        } else if (id == getId("xingqu_buluo_entry")) {
            // 隐藏动态界面兴趣部落入口
            isHide = SettingUtils.getValueHideTribalEntry();
        } else if (id == getId("qzone_feed_entry_sub_iv")) {
            // 隐藏动态界面空间头像提醒
            isHide = SettingUtils.getValueHideQzoneAvatarRemind();
        } else if (id == getId("nearby_people_entry_sub_iv")) {
            // 隐藏动态界面附近的人头像提醒
            isHide = SettingUtils.getValueHideNearAvatarRemind();
        } else if (id == getId("buluo_entry_sub_iv")) {
            // 隐藏动态界面兴趣部落头像提醒
            isHide = SettingUtils.getValueHideTribalAvatarRemind();
        } else if (id == getId("ivTitleBtnRightCall")) {
            // 隐藏聊天界面右上角的 QQ 电话
            isHide = SettingUtils.getValueHideChatCall();
        } else if (id == getId("mydaily")) {
            // 隐藏侧滑栏打卡
            isHide = SettingUtils.getValueHideSidebarMyDaily();
        } else if (id == getId("qr_code_icon")) {
            // 隐藏侧滑栏我的二维码
            isHide = SettingUtils.getValueHideSidebarMyQrCode();
        } else if (id == getId("nickname_area") || id == getId("richstatus_txt") || id == getId("sig_layout")) {
            // 隐藏侧滑栏 QQ 信息
            isHide = SettingUtils.getValueHideSidebarQQInfo();
        } else if (id == getId("myvip")) {
            // 隐藏侧滑栏会员栏
            isHide = SettingUtils.getValueHideSidebarVip();
        } else if (id == getId("mypocket")) {
            // 隐藏侧滑栏 QQ 钱包
            isHide = SettingUtils.getValueHideSidebarPocket();
        } else if (id == getId("myDressup")) {
            // 隐藏侧滑栏个性装扮
            isHide = SettingUtils.getValueHideSidebarDressUp();
        } else if (id == getId("myfavorites")) {
            // 隐藏侧滑栏我的收藏
            isHide = SettingUtils.getValueHideSidebarMyFavorites();
        } else if (id == getId("myphotos")) {
            // 隐藏侧滑栏我的相册
            isHide = SettingUtils.getValueHideSidebarMyPhotos();
        } else if (id == getId("myfiles")) {
            // 隐藏侧滑栏我的文件
            isHide = SettingUtils.getValueHideSidebarMyFiles();
        } else if (id == getId("myvideos")) {
            // 隐藏侧滑栏我的视频
            isHide = SettingUtils.getValueHideSidebarMyVideos();
        } else if (id == getId("mycards")) {
            // 隐藏侧滑栏我的名片夹
            isHide = SettingUtils.getValueHideSidebarMyCards();
        } else if (id == getId("cuKingCard")) {
            // 隐藏侧滑栏免流量特权
            isHide = SettingUtils.getValueHideSidebarFreeFlow();
        } else if (id == getId("nightmode")) {
            // 隐藏侧滑栏夜间模式
            isHide = SettingUtils.getValueHideSidebarThemeNight();
        } else if (id == getId("weather_layout")) {
            // 隐藏侧滑栏城市天气
            isHide = SettingUtils.getValueHideSidebarCityWeather();
        } else if (id == getId("weather_area")) {
            // 隐藏侧滑栏我的城市
            isHide = SettingUtils.getValueHideSidebarMyCity();
        } else if (id == getId("qq_aio_panel_ptt")) {
            // 隐藏聊天界面底部工具栏语音按钮
            isHide = SettingUtils.getValueHideChatToolbarVoice();
        } else if (id == getId("qq_aio_panel_ptv")) {
            // 隐藏聊天界面底部工具栏视频按钮
            isHide = SettingUtils.getValueHideChatToolbarCamera();
        } else if (id == getId("qq_aio_panel_hot_pic")) {
            // 隐藏聊天界面底部工具栏红包按钮
            isHide = SettingUtils.getValueHideChatToolbarGif();
        } else if (id == getId("qq_aio_panel_hongbao")) {
            // 隐藏聊天界面底部工具栏戳一戳按钮
            isHide = SettingUtils.getValueHideChatToolbarRedEnvelope();
        } else if (id == getId("qq_aio_panel_poke")) {
            // 隐藏聊天界面底部工具栏 GIF 按钮
            isHide = SettingUtils.getValueHideChatToolbarPoke();
        } else if (id == getId("chat_item_troop_member_level")) {
            // 隐藏群头衔
            isHide = SettingUtils.getValueHideGroupMemberLevel();
        } else if (id == getId("chat_item_troop_member_glamour_level")) {
            // 隐藏魅力等级
            isHide = SettingUtils.getValueHideGroupMemberGlamourLevel();
        } else if (id == getId("troop_assistant_feeds_title_small_video") || id == getId("troop_assistant_feeds_title_super_owner")) {
            // 隐藏群消息里的小视频
            isHide = SettingUtils.getValueHideGroupSmallVideo();
        } else if (id == getId("chat_top_bar")) {
            // 隐藏移出群助手提示
            isHide = SettingUtils.getValueHideGroupHelperRemoveTips();
        } else if (id == getId("find_reddot") || id == getId("item_right_reddot") || id == getId("iv_reddot") || id == getId("qzone_feed_reddot") || id == getId("qzone_mood_reddot") || id == getId("qzone_super_font_tab_reddot") || id == getId("qzone_uploadphoto_item_reddot") || id == getId("tv_reddot") || id == getId("xingqu_buluo_reddot")) {
            // 隐藏部分小红点
            isHide = SettingUtils.getValueHideSomeRedDot();
        } else if (id == getId("timtips")) {
            // 隐藏我的文件里的 TIM 推广
            isHide = SettingUtils.getValueHideTimInMyFile();
        } else if (id == getId("qqsetting2_phone_unity_info")) {
            // 隐藏设置电话号码
            isHide = SettingUtils.getValueHideSettingPhoneNumber();
        } else if (id == getId("qqsetting2_newXmanLayout")) {
            // 隐藏设置 QQ 达人
            isHide = SettingUtils.getValueHideSettingQQExpert();
        } else if (id == getId("qqsetting2_msg_qqclean")) {
            // 隐藏设置空间清理
            isHide = SettingUtils.getValueHideSettingClear();
        } else if (id == getId("shuoshuo_ad_upload_quality") || id == getId("quality_hd_ad") || id == getId("quality_ad")) {
            // 隐藏空间绿厂广告
            isHide = SettingUtils.getValueHideQzoneAd();
        }
        if (getQQ_Version().compareTo("7.3.5") >= 0) {
            // 隐藏聊天图片旁的笑脸按钮
            if (id == getId("pic_light_emoj")) {
                isHide = SettingUtils.getValueHidePicSmile();
            }
        }
        hideView(view, param, isHide);
    }

    private void hideView(View view, XC_MethodHook.MethodHookParam param, boolean isHide) {
        if (isHide) {
            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) param.args[0];
            layoutParams.height = 1;
            layoutParams.width = 0;
            view.setVisibility(View.GONE);
        }
    }

    public void removeDrawable() {
        XposedHelpers.findAndHookMethod(Resources.class, "getDrawable", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                hideDrawable((int) param.args[0], param);
            }
        });
        XposedHelpers.findAndHookMethod(ImageView.class, "setImageResource", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                hideDrawable((int) param.args[0], param);
            }
        });
    }

    private void hideDrawable(int i, XC_MethodHook.MethodHookParam param) {
        if (i == getDrawableId("skin_tips_dot") || i == getDrawableId("skin_tips_dot_small") || i == getDrawableId("skin_tips_new") || (getQQ_Version().compareTo("7.3.5") > 0) && i == getDrawableId("shortvideo_redbag_outicon")) {
            // 去除部分小红点
            if (SettingUtils.getValueHideSomeRedDot()) {
                param.args[0] = getDrawableId("skin_searchbar_button_pressed_theme_version2");
            }
        }
    }

    private int getId(String idName) {
        if (id == null || idName.equals("")) {
            return 0;
        }
        try {
            return XposedHelpers.getStaticIntField(id, idName);
        } catch (Exception e) {
            XposedBridge.log(String.format("%s not found field: %s", getQQ_Version(), idName));
            return 0;
        }
    }

    private int getDrawableId(String drawableName) {
        if (drawable == null || drawableName.equals("")) {
            return 0;
        }
        try {
            return XposedHelpers.getStaticIntField(drawable, drawableName);
        } catch (Exception e) {
            XposedBridge.log(String.format("%s not found field: %s", getQQ_Version(), drawableName));
            return 0;
        }
    }
}
