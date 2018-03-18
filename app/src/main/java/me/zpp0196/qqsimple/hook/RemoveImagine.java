package me.zpp0196.qqsimple.hook;

import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_CALL;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_LIST_HEAD_AD;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_TOOLBAR_CAMERA;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_TOOLBAR_GIF;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_TOOLBAR_PIC;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_TOOLBAR_POKE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_TOOLBAR_RED_ENVELOPE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_TOOLBAR_VOICE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CREATE_TROOP_ENTRY;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_GROUP_HELPER_REMOVE_TIPS;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_GROUP_MEMBER_GLAMOUR_LEVEL;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_GROUP_MEMBER_LEVEL;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_GROUP_SMALL_VIDEO;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_GROUP_STICK_FACE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_LEB_SEARCH_ENTRY;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_NEAR_AVATAR_REMIND;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_NEAR_ENTRY;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_NEW_FRIEND_ENTRY;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_QZONE_AD;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_QZONE_AVATAR_REMIND;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_QZONE_ENTRY;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SEARCH_CONTAINER;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SETTING_CLEAR;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SETTING_PHONE_NUMBER;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SETTING_QQ_EXPERT;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SIDEBAR_CITY_WEATHER;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SIDEBAR_DRESS_UP;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SIDEBAR_FREE_FLOW;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SIDEBAR_MY_CARDS;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SIDEBAR_MY_CITY;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SIDEBAR_MY_DAILY;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SIDEBAR_MY_FAVORITES;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SIDEBAR_MY_FILES;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SIDEBAR_MY_PHOTOS;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SIDEBAR_MY_QR_CODE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SIDEBAR_MY_VIDEOS;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SIDEBAR_POCKET;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SIDEBAR_QQ_INFO;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SIDEBAR_THEME_NIGHT;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SIDEBAR_VIP;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SOME_RED_DOT;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_TAB_MSG_NUM;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_TIM_IN_MY_FILE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_TRIBAL_AVATAR_REMIND;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_TRIBAL_ENTRY;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_UNREADMSG;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_UNUSUAL_CONTACTS;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_VIEW_DIY;

/**
 * Created by Deng on 2018/2/16.
 */

class RemoveImagine extends BaseHook {

    private boolean isHideUnreadmsg = false;
    private boolean isHideReddot = false;
    private boolean isHideSidebarQQInfo = false;
    private boolean isHideQzneAd = false;

    RemoveImagine(Class<?> id, Class<?> drawable) {
        setId(id);
        setDrawable(drawable);
        isHideUnreadmsg = getBool(KEY_HIDE_UNREADMSG);
        isHideReddot = getBool(KEY_HIDE_SOME_RED_DOT);
        isHideSidebarQQInfo = getBool(KEY_HIDE_SIDEBAR_QQ_INFO);
        isHideQzneAd = getBool(KEY_HIDE_QZONE_AD);
        hideView();
        hideDrawable();
        hideDiyView();
    }

    private void hideDiyView() {
        String value = getString(KEY_HIDE_VIEW_DIY, "");
        String[] views = value.split(" ");
        for (String str : views) {
            str = str.toLowerCase();
            if (str.startsWith("0x") || str.startsWith("ox")) {
                str = str.substring(2);
            }
            int id;
            try {
                id = Integer.parseInt(str);
            } catch (Exception e) {
                try {
                    id = Integer.parseInt(str, 16);
                } catch (Exception ex) {
                    continue;
                }
            }
            if (id > 2131361792 && id < 2131427324) {
                remove(id, true);
            } else if (id > 2130837504 && id < 2130903039) {
                removeDrawable(id, true);
            }
        }
    }

    private void hideView() {
        // 隐藏消息和联系人界面搜索框
        remove(getId("search_container"), getBool(KEY_HIDE_SEARCH_CONTAINER));
        // 隐藏消息列表底部未读消息数量
        remove(getId("unchecked_msg_num"), getBool(KEY_HIDE_TAB_MSG_NUM));
        // 隐藏消息列表界面横幅广告
        remove(getId("adviewlayout"), getBool(KEY_HIDE_CHAT_LIST_HEAD_AD));
        // 隐藏消息列表右侧未读消息数量
        remove(getId("unreadmsg"), isHideUnreadmsg);
        remove(getId("tvDefaultTitleBtnLeft"), isHideUnreadmsg);
        // 隐藏联系人界面新朋友
        remove(getId("newFriendEntry"), getBool(KEY_HIDE_NEW_FRIEND_ENTRY));
        // 隐藏联系人界面创建群聊
        remove(getId("createTroopEntry"), getBool(KEY_HIDE_CREATE_TROOP_ENTRY));
        // 隐藏联系人界面不常用联系人
        remove(getId("unusual_contacts_footerview"), getBool(KEY_HIDE_UNUSUAL_CONTACTS));
        // 隐藏动态界面搜索框
        remove(getId("leb_search_entry"), getBool(KEY_HIDE_LEB_SEARCH_ENTRY));
        // 隐藏动态界面空间入口
        remove(getId("qzone_feed_entry"), getBool(KEY_HIDE_QZONE_ENTRY));
        // 隐藏动态界面附近的人入口
        remove(getId("near_people_entry"), getBool(KEY_HIDE_NEAR_ENTRY));
        // 隐藏动态界面兴趣部落入口
        remove(getId("xingqu_buluo_entry"), getBool(KEY_HIDE_TRIBAL_ENTRY));
        // 隐藏动态界面空间头像提醒
        remove(getId("qzone_feed_entry_sub_iv"), getBool(KEY_HIDE_QZONE_AVATAR_REMIND));
        // 隐藏动态界面附近的人头像提醒
        remove(getId("nearby_people_entry_sub_iv"), getBool(KEY_HIDE_NEAR_AVATAR_REMIND));
        // 隐藏动态界面兴趣部落头像提醒
        remove(getId("buluo_entry_sub_iv"), getBool(KEY_HIDE_TRIBAL_AVATAR_REMIND));
        // 隐藏侧滑栏打卡
        remove(getId("mydaily"), getBool(KEY_HIDE_SIDEBAR_MY_DAILY));
        // 隐藏侧滑栏我的二维码
        remove(getId("qr_code_icon"), getBool(KEY_HIDE_SIDEBAR_MY_QR_CODE));
        // 隐藏侧滑栏 QQ 信息
        remove(getId("nickname_area"), isHideSidebarQQInfo);
        remove(getId("richstatus_txt"), isHideSidebarQQInfo);
        remove(getId("sig_layout"), isHideSidebarQQInfo);
        // 隐藏侧滑栏会员栏
        remove(getId("myvip"), getBool(KEY_HIDE_SIDEBAR_VIP));
        // 隐藏侧滑栏 QQ 钱包
        remove(getId("mypocket"), getBool(KEY_HIDE_SIDEBAR_POCKET));
        // 隐藏侧滑栏个性装扮
        remove(getId("myDressup"), getBool(KEY_HIDE_SIDEBAR_DRESS_UP));
        // 隐藏侧滑栏我的收藏
        remove(getId("myfavorites"), getBool(KEY_HIDE_SIDEBAR_MY_FAVORITES));
        // 隐藏侧滑栏我的相册
        remove(getId("myphotos"), getBool(KEY_HIDE_SIDEBAR_MY_PHOTOS));
        // 隐藏侧滑栏我的文件
        remove(getId("myfiles"), getBool(KEY_HIDE_SIDEBAR_MY_FILES));
        // 隐藏侧滑栏我的视频
        remove(getId("myvideos"), getBool(KEY_HIDE_SIDEBAR_MY_VIDEOS));
        // 隐藏侧滑栏我的名片夹
        remove(getId("mycards"), getBool(KEY_HIDE_SIDEBAR_MY_CARDS));
        // 隐藏侧滑栏免流量特权
        remove(getId("cuKingCard"), getBool(KEY_HIDE_SIDEBAR_FREE_FLOW));
        // 隐藏侧滑栏夜间模式
        remove(getId("nightmode"), getBool(KEY_HIDE_SIDEBAR_THEME_NIGHT));
        // 隐藏侧滑栏城市天气
        remove(getId("weather_layout"), getBool(KEY_HIDE_SIDEBAR_CITY_WEATHER));
        // 隐藏侧滑栏我的城市
        remove(getId("weather_area"), getBool(KEY_HIDE_SIDEBAR_MY_CITY));
        // 隐藏聊天界面右上角的 QQ 电话
        remove(getId("ivTitleBtnRightCall"), getBool(KEY_HIDE_CHAT_CALL));
        // 隐藏聊天界面底部工具栏语音按钮
        remove(getId("qq_aio_panel_ptt"), getBool(KEY_HIDE_CHAT_TOOLBAR_VOICE));
        // 隐藏聊天界面底部工具栏图片按钮
        remove(getId("qq_aio_panel_image"), getBool(KEY_HIDE_CHAT_TOOLBAR_PIC));
        // 隐藏聊天界面底部工具栏视频按钮
        remove(getId("qq_aio_panel_ptv"), getBool(KEY_HIDE_CHAT_TOOLBAR_CAMERA));
        // 隐藏聊天界面底部工具栏红包按钮
        remove(getId("qq_aio_panel_hongbao"), getBool(KEY_HIDE_CHAT_TOOLBAR_RED_ENVELOPE));
        // 隐藏聊天界面底部工具栏戳一戳按钮
        remove(getId("qq_aio_panel_poke"), getBool(KEY_HIDE_CHAT_TOOLBAR_POKE));
        // 隐藏聊天界面底部工具栏 GIF 按钮
        remove(getId("qq_aio_panel_hot_pic"), getBool(KEY_HIDE_CHAT_TOOLBAR_GIF));
        // 隐藏群头衔
        remove(getId("chat_item_troop_member_level"), getBool(KEY_HIDE_GROUP_MEMBER_LEVEL));
        // 隐藏魅力等级
        remove(getId("chat_item_troop_member_glamour_level"), getBool(KEY_HIDE_GROUP_MEMBER_GLAMOUR_LEVEL));
        // 隐藏群消息里的小视频
        remove(getId("troop_assistant_feeds_title_small_video"), getBool(KEY_HIDE_GROUP_SMALL_VIDEO));
        remove(getId("troop_assistant_feeds_title_super_owner"), getBool(KEY_HIDE_GROUP_SMALL_VIDEO));
        // 隐藏移出群助手提示
        remove(getId("chat_top_bar"), getBool(KEY_HIDE_GROUP_HELPER_REMOVE_TIPS));
        // 隐藏部分小红点
        remove(getId("find_reddot"), isHideReddot);
        remove(getId("item_right_reddot"), isHideReddot);
        remove(getId("iv_reddot"), isHideReddot);
        remove(getId("qzone_feed_reddot"), isHideReddot);
        remove(getId("qzone_mood_reddot"), isHideReddot);
        remove(getId("qzone_super_font_tab_reddot"), isHideReddot);
        remove(getId("qzone_uploadphoto_item_reddot"), isHideReddot);
        remove(getId("tv_reddot"), isHideReddot);
        remove(getId("xingqu_buluo_reddot"), isHideReddot);
        // 隐藏我的文件里的 TIM 推广
        remove(getId("timtips"), getBool(KEY_HIDE_TIM_IN_MY_FILE));
        // 隐藏设置电话号码
        remove(getId("qqsetting2_phone_unity_info"), getBool(KEY_HIDE_SETTING_PHONE_NUMBER));
        // 隐藏设置 QQ 达人
        remove(getId("qqsetting2_newXmanLayout"), getBool(KEY_HIDE_SETTING_QQ_EXPERT));
        // 隐藏设置空间清理
        remove(getId("qqsetting2_msg_qqclean"), getBool(KEY_HIDE_SETTING_CLEAR));
        // 隐藏空间绿厂广告
        remove(getId("shuoshuo_ad_upload_quality"), isHideQzneAd);
        remove(getId("quality_hd_ad"), isHideQzneAd);
        remove(getId("quality_ad"), isHideQzneAd);
        if (isMoreThan735()) {
            // 隐藏聊天图片旁的笑脸按钮
            remove(getId("pic_light_emoj"), getBool(KEY_HIDE_GROUP_STICK_FACE));
        }
    }

    private void hideDrawable() {
        removeDrawable(getDrawableId("skin_tips_dot"), isHideReddot);
        removeDrawable(getDrawableId("skin_tips_dot_small"), isHideReddot);
        removeDrawable(getDrawableId("skin_tips_new"), isHideReddot);
        if (isMoreThan735()) {
            removeDrawable(getDrawableId("shortvideo_redbag_outicon"), isHideReddot);
        }
    }
}
