package me.zpp0196.qqsimple.hook;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import me.zpp0196.qqsimple.Common;
import me.zpp0196.qqsimple.hook.base.BaseHook;

import static me.zpp0196.qqsimple.hook.comm.Classes.Leba;
import static me.zpp0196.qqsimple.hook.comm.Classes.LebaQZoneFacePlayHelper;
import static me.zpp0196.qqsimple.hook.comm.Classes.QQSettingSettingActivity;
import static me.zpp0196.qqsimple.hook.util.HookUtil.isMoreThan735;
import static me.zpp0196.qqsimple.hook.util.HookUtil.isMoreThan758;

/**
 * Created by Deng on 2018/2/16.
 */

class RemoveImagine extends BaseHook {

    @Override
    public void init() {
        hideView();
        hideRedDot();
        hideQzone();
    }

    private void hideView() {
        // 隐藏消息列表底部未读消息数量
        hideView("unchecked_msg_num", "hide_tab_msg_num");
        // 隐藏消息列表界面横幅广告
        hideView("adviewlayout", "hide_chat_list_head_ad");
        // 隐藏联系人界面新朋友
        hideView("newFriendEntry", "hide_new_friend_entry");
        // 隐藏联系人界面创建群聊
        hideView("createTroopEntry", "hide_create_troop_entry");
        // 隐藏表情联想
        hideView("emotionLayout", "hide_expression_association");
        // 隐藏表情商城
        hideView("btn_more_emoticon", "hide_btn_more_emoticon");
        // 隐藏联系人界面不常用联系人
        hideView("unusual_contacts_footerview", "hide_unusual_contacts");
        // 隐藏动态界面搜索框
        hideView("leb_search_entry", "hide_leb_search_entry");
        // 隐藏动态界面空间入口
        hideView("qzone_feed_entry", "hide_qzone_entry");
        // 隐藏动态界面附近的人入口
        hideView("near_people_entry", "hide_near_entry");
        // 隐藏动态界面兴趣部落入口
        hideView("xingqu_buluo_entry", "hide_tribal_entry");
        // 隐藏动态界面空间头像提醒
        if(isMoreThan758()){
            hideQzoneFacePlayHelper();
        }else {
            hideView("qzone_feed_entry_sub_iv", "hide_qzone_avatar_remind");
        }
        // 隐藏动态界面附近的人头像提醒
        hideView("nearby_people_entry_sub_iv", "hide_near_avatar_remind");
        // 隐藏动态界面兴趣部落头像提醒
        hideView("buluo_entry_sub_iv", "hide_tribal_avatar_remind");
        // 隐藏侧滑栏我的二维码
        hideView("qr_code_icon", "hide_sidebar_my_qr_code");
        // 隐藏侧滑栏打卡/每日任务
        hideView("mydaily", "hide_sidebar_my_daily");
        // 隐藏侧滑栏 QQ信息
        hideView("nickname_area", "hide_sidebar_qq_info");
        hideView("sig_layout", "hide_sidebar_qq_info");
        // 隐藏侧滑栏会员栏
        hideView("myvip", "hide_sidebar_vip");
        // 隐藏侧滑栏 QQ钱包
        hideView("mypocket", "hide_sidebar_pocket");
        // 隐藏侧滑栏个性装扮
        hideView("myDressup", "hide_sidebar_dress_up");
        // 隐藏侧滑栏我的收藏
        hideView("myfavorites", "hide_sidebar_my_favorites");
        // 隐藏侧滑栏我的相册
        hideView("myphotos", "hide_sidebar_my_photos");
        // 隐藏侧滑栏我的文件
        hideView("myfiles", "hide_sidebar_my_files");
        // 隐藏侧滑栏我的视频
        hideView("myvideos", "hide_sidebar_my_videos");
        // 隐藏侧滑栏我的名片夹
        hideView("mycards", "hide_sidebar_my_cards");
        // 隐藏侧滑栏免流量特权
        hideView("cuKingCard", "hide_sidebar_free_flow");
        // 隐藏侧滑栏夜间模式
        hideView("nightmode", "hide_sidebar_theme_night");
        // 隐藏侧滑栏城市天气
        hideView("weather_layout", "hide_sidebar_city_weather");
        // 隐藏侧滑栏我的城市
        hideView("weather_area", "hide_sidebar_my_city");
        // 隐藏头像挂件
        hideView("chat_item_pendant_image", "hide_avatar_pendant");
        // 隐藏聊天界面右上角的 QQ 电话
        hideView("ivTitleBtnRightCall", "hide_chat_right_call");
        // 隐藏聊天界面底部工具栏语音按钮
        hideView("qq_aio_panel_ptt", "hide_chat_toolbar_voice");
        hideView("qq_aio_panel_ptt_gold_msg", "hide_chat_toolbar_voice");
        // 隐藏聊天界面底部工具栏图片按钮
        hideView("qq_aio_panel_image", "hide_chat_toolbar_pic");
        hideView("qq_aio_panel_image_gold_msg", "hide_chat_toolbar_pic");
        // 隐藏聊天界面底部工具栏视频按钮
        hideView("qq_aio_panel_camera", "hide_chat_toolbar_camera");
        hideView("qq_aio_panel_ptv", "hide_chat_toolbar_camera");
        hideView("qq_aio_panel_ptv_gold_msg", "hide_chat_toolbar_camera");
        // 隐藏聊天界面底部工具栏红包按钮
        hideView("qq_aio_panel_hongbao", "hide_chat_toolbar_red_envelope");
        // 隐藏聊天界面底部工具栏戳一戳按钮
        hideView("qq_aio_panel_poke", "hide_chat_toolbar_poke");
        // 隐藏聊天界面底部工具栏 GIF 按钮
        hideView("qq_aio_panel_hot_pic", "hide_chat_toolbar_gif");
        hideView("qq_aio_panel_hot_pic_gold_msg", "hide_chat_toolbar_gif");
        // 隐藏群头衔
        hideView("chat_item_troop_member_level", "hide_group_member_level");
        // 隐藏魅力等级
        hideView("chat_item_troop_member_glamour_level", "hide_group_member_glamour_level");
        // 隐藏群消息里的小视频
        hideView("troop_assistant_feeds_title_small_video", "hide_group_small_video");
        // 隐藏我的文件里的 TIM 推广
        hideView("timtips", "hide_tim_in_my_file");
        // 隐藏设置电话号码
        hideView("qqsetting2_phone_unity_info", "hide_setting_phone_number");
        // 隐藏设置 QQ达人
        hideView("qqsetting2_newXmanLayout", "hide_setting_qq_expert");
        // 隐藏设置消息通知
        hideView("qqsetting2_msg_notify", "hide_setting_msg_notify");
        // 隐藏设置聊天记录
        hideView("qqsetting2_msg_history", "hide_setting_msg_history");
        // 隐藏设置空间清理
        hideView("qqsetting2_msg_qqclean", "hide_setting_msg_qqclean");
        // 隐藏设置账号、设备安全
        hideView("qqsetting2_device_security", "hide_setting_device_security");
        // 隐藏设置联系人、隐私
        hideView("qqsetting2_permission_privacy", "hide_setting_permission_privacy");
        // 隐藏设置辅助功能
        hideView("qqsetting2_assist", "hide_setting_assist");
        // 隐藏设置免流量特权
        hideView("cu_open_card_guide_entry", "hide_setting_free_flow");
        // 隐藏设置关于QQ与帮助
        hideView("about", "hide_setting_about");
        if (isMoreThan735()) {
            // 隐藏贴表情
            hideView("pic_light_emoj", "hide_group_stick_face");
        }
    }

    /**
     * 隐藏动态界面空间头像提醒
     */
    private void hideQzoneFacePlayHelper(){
        findAndHookMethod(Leba, "u", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if(!getBool("hide_qzone_avatar_remind")){
                    return;
                }
                Field field = findField(Leba, LebaQZoneFacePlayHelper, "a");
                field.set(param.thisObject, null);
            }
        });
    }

    /**
     * 隐藏小红点
     */
    private void hideRedDot() {
        if (!getBool(Common.PREFS_KEY_HIDE_SOME_RED_DOT)) {
            return;
        }
        findAndHookMethod(QQSettingSettingActivity, "a", XC_MethodReplacement.returnConstant(false));
        // hideView("qzone_super_font_tab_reddot");
        // hideView("qzone_uploadphoto_item_reddot");
        hideView("qzone_feed_reddot");
        hideView("xingqu_buluo_reddot");
        hideDrawable("skin_tips_dot");
        // hideDrawable("skin_tips_dot_small");
        hideDrawable("skin_tips_new");
        if (isMoreThan735()) {
            hideDrawable("shortvideo_redbag_outicon");
        }
    }

    /**
     * 隐藏空间内容
     */
    private void hideQzone() {
        // 隐藏头像装扮
        hideView("qzone_cover_avatar_facade", "hide_qzone_facadeDecorator");
        // 隐藏我的黄钻
        hideView("qzone_cover_avatar_vip", "hide_qzone_vipDecorator");
        hideView("qzone_cover_avatar_qboss", "hide_qzone_vipDecorator");
        // 隐藏机型
        hideView("feed_attach_view", "hide_qzone_mood_attach");
        // 隐藏点赞按钮
        hideView("operation_like_container", "hide_qzone_btn_like");
        hideView("operation_like_container2", "hide_qzone_btn_like");
        // 隐藏点赞列表
        hideView("feed_praise_avatars_view", "hide_qzone_likeList");
        // 隐藏评论框
        hideView("feed_guide_comment_bar", "hide_qzone_et_comment");
        // 隐藏评论内容
        hideView("feed_canvas_comment_area_stub", "hide_qzone_et_comment_content");
        if (!getBool("hide_qzone_AD")) {
            return;
        }
        // 隐藏空间绿厂广告
        hideView("shuoshuo_ad_upload_quality");
        hideView("quality_hd_ad");
        hideView("quality_ad");
        hideView("quality_normal_ad");
        hideView("quality_original_ad");
        // 隐藏顶部广告
        hideView("qzone_feed_commwidget_container");
        hideView("qzone_feed_commwidget_image");
        hideView("qzone_feed_commwidget_text");
        hideView("qzone_feed_commwidget_count");
        hideView("qzone_feed_commwidget_hide_btn");
        hideView("qzone_feed_commwidget_stub");
        hideView("qz_feed_head_container");
    }
}
