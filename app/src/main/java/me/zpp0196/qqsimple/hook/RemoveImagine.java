package me.zpp0196.qqsimple.hook;

import me.zpp0196.qqsimple.util.SettingUtils;

/**
 * Created by Deng on 2018/2/16.
 */

public class RemoveImagine extends BaseHook{

    public RemoveImagine(Class<?> id, Class<?> drawable) {
        setId(id);
        setDrawable(drawable);
    }

    public void hideView() {
        // 隐藏消息列表界面横幅广告
        remove(getId("adviewlayout"), SettingUtils.getValueHideChatListHeadAd());
        // 隐藏消息和联系人界面搜索框
        remove(getId("search_container"), SettingUtils.getValueHideSearchContainer());
        // 隐藏消息列表未读消息数量
        remove(getId("unchecked_msg_num"), SettingUtils.getValueHideUncheckedMsgNum());
        // 隐藏联系人界面新朋友
        remove(getId("newFriendEntry"), SettingUtils.getValueHideNewFriendEntry());
        // 隐藏联系人界面创建群聊
        remove(getId("createTroopEntry"), SettingUtils.getValueHideCreateTroopEntry());
        // 隐藏联系人界面不常用联系人
        remove(getId("unusual_contacts_footerview"), SettingUtils.getValueHideUnusualContacts());
        // 隐藏动态界面搜索框
        remove(getId("leb_search_entry"), SettingUtils.getValueHideLebSearchEntry());
        // 隐藏动态界面空间入口
        remove(getId("qzone_feed_entry"), SettingUtils.getValueHideQzoneEntry());
        // 隐藏动态界面附近的人入口
        remove(getId("near_people_entry"), SettingUtils.getValueHideNearEntry());
        // 隐藏动态界面兴趣部落入口
        remove(getId("xingqu_buluo_entry"), SettingUtils.getValueHideTribalEntry());
        // 隐藏动态界面空间头像提醒
        remove(getId("qzone_feed_entry_sub_iv"), SettingUtils.getValueHideQzoneAvatarRemind());
        // 隐藏动态界面附近的人头像提醒
        remove(getId("nearby_people_entry_sub_iv"), SettingUtils.getValueHideNearAvatarRemind());
        // 隐藏动态界面兴趣部落头像提醒
        remove(getId("buluo_entry_sub_iv"), SettingUtils.getValueHideTribalAvatarRemind());
        // 隐藏聊天界面右上角的 QQ 电话
        remove(getId("ivTitleBtnRightCall"), SettingUtils.getValueHideChatCall());
        // 隐藏侧滑栏打卡
        remove(getId("mydaily"), SettingUtils.getValueHideSidebarMyDaily());
        // 隐藏侧滑栏我的二维码
        remove(getId("qr_code_icon"), SettingUtils.getValueHideSidebarMyQrCode());
        // 隐藏侧滑栏 QQ 信息
        remove(getId("nickname_area"), SettingUtils.getValueHideSidebarQQInfo());
        remove(getId("richstatus_txt"), SettingUtils.getValueHideSidebarQQInfo());
        remove(getId("sig_layout"), SettingUtils.getValueHideSidebarQQInfo());
        // 隐藏侧滑栏会员栏
        remove(getId("myvip"), SettingUtils.getValueHideSidebarVip());
        // 隐藏侧滑栏 QQ 钱包
        remove(getId("mypocket"), SettingUtils.getValueHideSidebarPocket());
        // 隐藏侧滑栏个性装扮
        remove(getId("myDressup"), SettingUtils.getValueHideSidebarDressUp());
        // 隐藏侧滑栏我的收藏
        remove(getId("myfavorites"), SettingUtils.getValueHideSidebarMyFavorites());
        // 隐藏侧滑栏我的相册
        remove(getId("myphotos"), SettingUtils.getValueHideSidebarMyPhotos());
        // 隐藏侧滑栏我的文件
        remove(getId("myfiles"), SettingUtils.getValueHideSidebarMyFiles());
        // 隐藏侧滑栏我的视频
        remove(getId("myvideos"), SettingUtils.getValueHideSidebarMyVideos());
        // 隐藏侧滑栏我的名片夹
        remove(getId("mycards"), SettingUtils.getValueHideSidebarMyCards());
        // 隐藏侧滑栏免流量特权
        remove(getId("cuKingCard"), SettingUtils.getValueHideSidebarFreeFlow());
        // 隐藏侧滑栏夜间模式
        remove(getId("nightmode"), SettingUtils.getValueHideSidebarThemeNight());
        // 隐藏侧滑栏城市天气
        remove(getId("weather_layout"), SettingUtils.getValueHideSidebarCityWeather());
        // 隐藏侧滑栏我的城市
        remove(getId("weather_area"), SettingUtils.getValueHideSidebarMyCity());
        // 隐藏聊天界面底部工具栏语音按钮
        remove(getId("qq_aio_panel_ptt"), SettingUtils.getValueHideChatToolbarVoice());
        // 隐藏聊天界面底部工具栏图片按钮
        remove(getId("qq_aio_panel_image"), SettingUtils.getValueHideChatToolbarPic());
        // 隐藏聊天界面底部工具栏视频按钮
        remove(getId("qq_aio_panel_ptv"), SettingUtils.getValueHideChatToolbarCamera());
        // 隐藏聊天界面底部工具栏红包按钮
        remove(getId("qq_aio_panel_hongbao"), SettingUtils.getValueHideChatToolbarRedEnvelope());
        // 隐藏聊天界面底部工具栏戳一戳按钮
        remove(getId("qq_aio_panel_poke"), SettingUtils.getValueHideChatToolbarPoke());
        // 隐藏聊天界面底部工具栏 GIF 按钮
        remove(getId("qq_aio_panel_hot_pic"), SettingUtils.getValueHideChatToolbarGif());
        // 隐藏群头衔
        remove(getId("chat_item_troop_member_level"), SettingUtils.getValueHideGroupMemberLevel());
        // 隐藏魅力等级
        remove(getId("chat_item_troop_member_glamour_level"), SettingUtils.getValueHideGroupMemberGlamourLevel());
        // 隐藏群消息里的小视频
        remove(getId("troop_assistant_feeds_title_small_video"), SettingUtils.getValueHideGroupSmallVideo());
        remove(getId("troop_assistant_feeds_title_super_owner"), SettingUtils.getValueHideGroupSmallVideo());
        // 隐藏移出群助手提示
        remove(getId("chat_top_bar"), SettingUtils.getValueHideGroupHelperRemoveTips());
        // 隐藏部分小红点
        boolean hideRedDot = SettingUtils.getValueHideSomeRedDot();
        remove(getId("find_reddot"), hideRedDot);
        remove(getId("item_right_reddot"), hideRedDot);
        remove(getId("iv_reddot"), hideRedDot);
        remove(getId("qzone_feed_reddot"), hideRedDot);
        remove(getId("qzone_mood_reddot"), hideRedDot);
        remove(getId("qzone_super_font_tab_reddot"), hideRedDot);
        remove(getId("qzone_uploadphoto_item_reddot"), hideRedDot);
        remove(getId("tv_reddot"), hideRedDot);
        remove(getId("xingqu_buluo_reddot"), hideRedDot);
        // 隐藏我的文件里的 TIM 推广
        remove(getId("timtips"), SettingUtils.getValueHideTimInMyFile());
        // 隐藏设置电话号码
        remove(getId("qqsetting2_phone_unity_info"), SettingUtils.getValueHideSettingPhoneNumber());
        // 隐藏设置 QQ 达人
        remove(getId("qqsetting2_newXmanLayout"), SettingUtils.getValueHideSettingQQExpert());
        // 隐藏设置空间清理
        remove(getId("qqsetting2_msg_qqclean"), SettingUtils.getValueHideSettingClear());
        // 隐藏空间绿厂广告
        remove(getId("shuoshuo_ad_upload_quality"), SettingUtils.getValueHideQzoneAd());
        remove(getId("quality_hd_ad"), SettingUtils.getValueHideQzoneAd());
        remove(getId("quality_ad"), SettingUtils.getValueHideQzoneAd());
        if (getQQ_Version().compareTo("7.3.5") >= 0) {
            // 隐藏聊天图片旁的笑脸按钮
            remove(getId("pic_light_emoj"), SettingUtils.getValueHidePicSmile());
        }
    }

    public void hideDrawable() {
        boolean hideRedDot = SettingUtils.getValueHideSomeRedDot();
        removeDrawable(getDrawableId("skin_tips_dot"), hideRedDot);
        removeDrawable(getDrawableId("skin_tips_dot_small"), hideRedDot);
        removeDrawable(getDrawableId("skin_tips_new"), hideRedDot);
        if(getQQ_Version().compareTo("7.3.5") > 0){
            removeDrawable(getDrawableId("shortvideo_redbag_outicon"), hideRedDot);
        }
    }
}
