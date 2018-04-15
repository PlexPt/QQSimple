package me.zpp0196.qqsimple.hook;

import de.robv.android.xposed.XC_MethodHook;
import me.zpp0196.qqsimple.hook.base.BaseHook;

/**
 * Created by Deng on 2018/2/16.
 */

class RemoveImagine extends BaseHook {

    private boolean isHideReddot = false;
    private boolean isHideSidebarQQInfo = false;
    private boolean isHideQzoneAd = false;

    RemoveImagine() {
        isHideReddot = getBool("hide_some_red_dot");
        isHideSidebarQQInfo = getBool("hide_sidebar_qq_info");
        isHideQzoneAd = getBool("hide_qzone_ad");
        hideView();
        hideDrawable();
    }

    private void hideView() {
        // 隐藏消息和联系人界面搜索框
        removeView(getId("search_container"), getBool("hide_search_container"));
        // 隐藏消息列表底部未读消息数量
        removeView(getId("unchecked_msg_num"), getBool("hide_tab_msg_num"));
        // 隐藏消息列表界面横幅广告
        removeView(getId("adviewlayout"), getBool("hide_chat_list_head_ad"));
        // 隐藏消息列表右侧未读消息数量
        removeView(getId("unreadmsg"), getBool("hide_unreadmsg"));
        // 隐藏联系人界面新朋友
        removeView(getId("newFriendEntry"), getBool("hide_new_friend_entry"));
        // 隐藏联系人界面创建群聊
        removeView(getId("createTroopEntry"), getBool("hide_create_troop_entry"));
        // 隐藏表情联想
        removeView(getId("emotionLayout"), getBool("hide_expression_association"));
        // 隐藏表情商城
        removeView(getId("btn_more_emoticon"), getBool("hide_btn_more_emoticon"));
        // 隐藏联系人界面不常用联系人
        removeView(getId("unusual_contacts_footerview"), getBool("hide_unusual_contacts"));
        // 隐藏动态界面顶部
        removeView(getId("laba_entrys_layout"), getBool("hide_laba_entrys_layout"));
        // 隐藏动态界面搜索框
        removeView(getId("leb_search_entry"), getBool("hide_leb_search_entry"));
        // 隐藏动态界面空间入口
        removeView(getId("qzone_feed_entry"), getBool("hide_qzone_entry"));
        // 隐藏动态界面附近的人入口
        removeView(getId("near_people_entry"), getBool("hide_near_entry"));
        // 隐藏动态界面兴趣部落入口
        removeView(getId("xingqu_buluo_entry"), getBool("hide_tribal_entry"));
        // 隐藏动态界面空间头像提醒
        removeView(getId("qzone_feed_entry_sub_iv"), getBool("hide_qzone_avatar_remind"));
        // 隐藏动态界面附近的人头像提醒
        removeView(getId("nearby_people_entry_sub_iv"), getBool("hide_near_avatar_remind"));
        // 隐藏动态界面兴趣部落头像提醒
        removeView(getId("buluo_entry_sub_iv"), getBool("hide_tribal_avatar_remind"));
        // 隐藏侧滑栏打卡
        removeView(getId("mydaily"), getBool("hide_sidebar_my_daily"));
        // 隐藏侧滑栏我的二维码
        removeView(getId("qr_code_icon"), getBool("hide_sidebar_my_qr_code"));
        // 隐藏侧滑栏 QQ 信息
        removeView(getId("nickname_area"), isHideSidebarQQInfo);
        removeView(getId("sig_layout"), isHideSidebarQQInfo);
        // 隐藏侧滑栏会员栏
        removeView(getId("myvip"), getBool("hide_sidebar_vip"));
        // 隐藏侧滑栏 QQ 钱包
        removeView(getId("mypocket"), getBool("hide_sidebar_pocket"));
        // 隐藏侧滑栏个性装扮
        removeView(getId("myDressup"), getBool("hide_sidebar_dress_up"));
        // 隐藏侧滑栏我的收藏
        removeView(getId("myfavorites"), getBool("hide_sidebar_my_favorites"));
        // 隐藏侧滑栏我的相册
        removeView(getId("myphotos"), getBool("hide_sidebar_my_photos"));
        // 隐藏侧滑栏我的文件
        removeView(getId("myfiles"), getBool("hide_sidebar_my_files"));
        // 隐藏侧滑栏我的视频
        removeView(getId("myvideos"), getBool("hide_sidebar_my_videos"));
        // 隐藏侧滑栏我的名片夹
        removeView(getId("mycards"), getBool("hide_sidebar_my_cards"));
        // 隐藏侧滑栏免流量特权
        removeView(getId("cuKingCard"), getBool("hide_sidebar_free_flow"));
        // 隐藏侧滑栏夜间模式
        removeView(getId("nightmode"), getBool("hide_sidebar_theme_night"));
        // 隐藏侧滑栏城市天气
        removeView(getId("weather_layout"), getBool("hide_sidebar_city_weather"));
        // 隐藏侧滑栏我的城市
        removeView(getId("weather_area"), getBool("hide_sidebar_my_city"));
        // 隐藏头像挂件
        removeView(getId("chat_item_pendant_image"), getBool("hide_avatar_pendant"));
        // 隐藏聊天界面左上角的未读消息数量
        removeView(getId("tvDefaultTitleBtnLeft"), getBool("hide_chat_left_num"));
        // 隐藏聊天界面右上角的 QQ 电话
        removeView(getId("ivTitleBtnRightCall"), getBool("hide_chat_right_call"));
        // 隐藏聊天界面底部工具栏语音按钮
        removeView(getId("qq_aio_panel_ptt"), getBool("hide_chat_toolbar_voice"));
        removeView(getId("qq_aio_panel_ptt_gold_msg"), getBool("hide_chat_toolbar_voice"));
        // 隐藏聊天界面底部工具栏图片按钮
        removeView(getId("qq_aio_panel_image"), getBool("hide_chat_toolbar_pic"));
        removeView(getId("qq_aio_panel_image_gold_msg"), getBool("hide_chat_toolbar_pic"));
        // 隐藏聊天界面底部工具栏视频按钮
        removeView(getId("qq_aio_panel_camera"), getBool("hide_chat_toolbar_camera"));
        removeView(getId("qq_aio_panel_ptv"), getBool("hide_chat_toolbar_camera"));
        removeView(getId("qq_aio_panel_ptv_gold_msg"), getBool("hide_chat_toolbar_camera"));
        // 隐藏聊天界面底部工具栏红包按钮
        removeView(getId("qq_aio_panel_hongbao"), getBool("hide_chat_toolbar_red_envelope"));
        // 隐藏聊天界面底部工具栏戳一戳按钮
        removeView(getId("qq_aio_panel_poke"), getBool("hide_chat_toolbar_poke"));
        // 隐藏聊天界面底部工具栏 GIF 按钮
        removeView(getId("qq_aio_panel_hot_pic"), getBool("hide_chat_toolbar_gif"));
        removeView(getId("qq_aio_panel_hot_pic_gold_msg"), getBool("hide_chat_toolbar_gif"));
        // 隐藏群头衔
        removeView(getId("chat_item_troop_member_level"), getBool("hide_group_member_level"));
        // 隐藏魅力等级
        removeView(getId("chat_item_troop_member_glamour_level"), getBool("hide_group_member_glamour_level"));
        // 隐藏群消息里的小视频
        removeView(getId("troop_assistant_feeds_title_small_video"), getBool("hide_group_small_video"));
        // 隐藏移出群助手提示
        removeView(getId("chat_top_bar"), getBool("hide_group_helper_remove_tips"));
        // 隐藏贴表情
        removeView(getId("pic_light_emoj"), isMoreThan735() && getBool("hide_group_stick_face"));
        // 隐藏我的文件里的 TIM 推广
        removeView(getId("timtips"), getBool("hide_tim_in_my_file"));
        // 隐藏设置电话号码
        removeView(getId("qqsetting2_phone_unity_info"), getBool("hide_setting_phone_number"));
        // 隐藏设置 QQ 达人
        removeView(getId("qqsetting2_newXmanLayout"), getBool("hide_setting_qq_expert"));
        // 隐藏设置消息通知
        removeView(getId("qqsetting2_msg_notify"), getBool("hide_setting_msg_notify"));
        // 隐藏设置聊天记录
        removeView(getId("qqsetting2_msg_history"), getBool("hide_setting_msg_history"));
        // 隐藏设置空间清理
        removeView(getId("qqsetting2_msg_qqclean"), getBool("hide_setting_msg_qqclean"));
        // 隐藏设置账号、设备安全
        removeView(getId("qqsetting2_device_security"), getBool("hide_setting_device_security"));
        // 隐藏设置联系人、隐私
        removeView(getId("qqsetting2_permission_privacy"), getBool("hide_setting_permission_privacy"));
        // 隐藏设置辅助功能
        removeView(getId("qqsetting2_assist"), getBool("hide_setting_assist"));
        // 隐藏设置免流量特权
        removeView(getId("cu_open_card_guide_entry"), getBool("hide_setting_free_flow"));
        // 隐藏设置关于QQ与帮助
        removeView(getId("about"), getBool("hide_setting_about"));
        // 隐藏空间绿厂广告
        removeView(getId("shuoshuo_ad_upload_quality"), isHideQzoneAd);
        removeView(getId("quality_hd_ad"), isHideQzoneAd);
        removeView(getId("quality_ad"), isHideQzoneAd);
        // 隐藏部分小红点
        removeView(getId("find_reddot"), isHideReddot);
        removeView(getId("item_right_reddot"), isHideReddot);
        removeView(getId("iv_reddot"), isHideReddot);
        removeView(getId("qzone_feed_reddot"), isHideReddot || getBool("hide_qzone_entry") || getBool("hide_qzone_avatar_remind"));
        removeView(getId("qzone_mood_reddot"), isHideReddot);
        removeView(getId("qzone_super_font_tab_reddot"), isHideReddot);
        removeView(getId("qzone_uploadphoto_item_reddot"), isHideReddot);
        removeView(getId("tv_reddot"), isHideReddot);
        removeView(getId("xingqu_buluo_reddot"), isHideReddot || getBool("hide_tribal_entry") || getBool("hide_tribal_avatar_remind"));
        hideRedTouchViewNum();
    }

    private void hideDrawable() {
        removeDrawable(getDrawableId("skin_tips_dot"), isHideReddot);
        removeDrawable(getDrawableId("skin_tips_dot_small"), isHideReddot);
        removeDrawable(getDrawableId("skin_tips_new"), isHideReddot);
        removeDrawable(getDrawableId("shortvideo_redbag_outicon"), isHideReddot && isMoreThan735());
    }

    /**
     * 隐藏底部消息数量
     */
    private void hideRedTouchViewNum() {
        Class<?> MainFragment = findClassInQQ("com.tencent.mobileqq.activity.MainFragment");
        Class<?> RedTypeInfo = findClassInQQ("com.tencent.pb.getbusiinfo.BusinessInfoCheckUpdate$RedTypeInfo");
        findAndHookMethod(MainFragment, "a", int.class, RedTypeInfo, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                int i = (int) param.args[0];
                if ((i == 33 && getBool("hide_new_friend_entry")) || (i == 34 && (getBool("hide_qzone_avatar_remind") || getBool("hide_tribal_entry") || getBool("hide_tribal_avatar_remind")))) {
                    param.setResult(null);
                }
            }
        });
    }
}
