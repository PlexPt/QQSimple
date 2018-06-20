package me.zpp0196.qqsimple.hook;

import de.robv.android.xposed.XC_MethodReplacement;
import me.zpp0196.qqsimple.Common;
import me.zpp0196.qqsimple.hook.base.BaseHook;

import static me.zpp0196.qqsimple.hook.comm.Classes.QQSettingSettingActivity;
import static me.zpp0196.qqsimple.hook.comm.Ids.isSupport;
import static me.zpp0196.qqsimple.hook.util.HookUtil.getQQVersionCode;
import static me.zpp0196.qqsimple.hook.util.HookUtil.isMoreThan735;

/**
 * Created by Deng on 2018/2/16.
 */

class RemoveImagine extends BaseHook {

    @Override
    public void init() {
        if (!isSupport(getQQVersionCode())) {
            return;
        }
        hideView();
        hideRedDot();
        hideQzone();
    }

    private void hideView() {
        // 隐藏消息列表底部未读消息数量
        hideView("unchecked_msg_num", "hide_tab_num_conversation");
        // 隐藏消息列表界面横幅广告
        hideView("adviewlayout", "hide_conversation_headAd");
        // 隐藏表情联想
        hideView("emotionLayout", "hide_chat_associatedExpression");
        // 隐藏表情商城
        hideView("btn_more_emoticon", "hide_btn_more_emoticon");
        // 隐藏联系人界面不常用联系人
        hideView("unusual_contacts_footerview", "hide_contacts_unusualContacts");
        // 隐藏动态界面搜索框
        hideView("leb_search_entry", "hide_leba_search");
        // 隐藏打卡/每日任务
        hideView("mydaily", "hide_sidebar_myDaily");
        // 隐藏侧滑栏会员栏
        hideView("myvip", "hide_sidebar_vip");
        // 隐藏侧滑栏我的视频
        hideView("myvideos", "hide_sidebar_myVideos");
        // 隐藏侧滑栏我的名片夹
        hideView("mycards", "hide_sidebar_myCards");
        // 隐藏侧滑栏免流量特权
        hideView("cuKingCard", "hide_sidebar_kingCard");
        // 隐藏侧滑栏城市天气
        hideView("weather_layout", "hide_sidebar_cityWeather");
        // 隐藏侧滑栏我的城市
        hideView("weather_area", "hide_sidebar_myCity");
        // 隐藏头像挂件
        hideView("chat_item_pendant_image", "hide_chat_avatarPendant");
        // 隐藏聊天界面右上角的 QQ 电话
        hideView("ivTitleBtnRightCall", "hide_chat_rightCall");
        // 隐藏群头衔
        hideView("chat_item_troop_member_level", "hide_troop_memberLevel");
        // 隐藏魅力等级
        hideView("chat_item_troop_member_glamour_level", "hide_troop_memberGlamourLevel");
        // 隐藏群消息里的小视频
        hideView("troop_assistant_feeds_title_small_video", "hide_troopAssistant_smallVideo");
        // 隐藏设置免流量特权
        hideView("cu_open_card_guide_entry", "hide_setting_kingCard");
        if (isMoreThan735()) {
            // 隐藏贴表情
            hideView("pic_light_emoj", "hide_troop_lightEmoji");
        }
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
