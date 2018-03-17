package me.zpp0196.qqsimple;

import android.content.Context;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by zpp0196 on 2018/3/11.
 */

public class Common {

    public static final String PACKAGE_NAME_QQ = "com.tencent.mobileqq";

    public static final String KEY_HIDE_SEARCH_CONTAINER = "hide_search_container";
    public static final String KEY_HIDE_TAB_CONTACT = "hide_tab_contact";
    public static final String KEY_HIDE_TAB_DYNAMIC = "hide_tab_dynamic";
    public static final String KEY_HIDE_CHAT_LIST_HEAD_AD = "hide_chat_list_head_ad";
    public static final String KEY_HIDE_UPDATE_REMINDER = "hide_update_reminder";
    public static final String KEY_HIDE_NATIONAL_ENTRANCE = "hide_national_entrance";
    public static final String KEY_HIDE_UNCHECKED_MSG_NUM = "hide_unchecked_msg_num";
    public static final String KEY_HIDE_NEW_FRIEND_ENTRY = "hide_new_friend_entry";
    public static final String KEY_HIDE_CREATE_TROOP_ENTRY = "hide_create_troop_entry";
    public static final String KEY_HIDE_CONTACTS_TAB_DEVICE = "hide_contacts_tab_device";
    public static final String KEY_HIDE_CONTACTS_TAB_PHONE = "hide_contacts_tab_phone";
    public static final String KEY_HIDE_CONTACTS_TAB_PUB_ACCOUNT = "hide_contacts_tab_pub_account";
    public static final String KEY_HIDE_UNUSUAL_CONTACTS = "hide_unusual_contacts";
    public static final String KEY_HIDE_EVERYONE_SEARCHING = "hide_everyone_searching";
    public static final String KEY_HIDE_LEB_SEARCH_ENTRY = "hide_leb_search_entry";
    public static final String KEY_HIDE_DYNAMIC_MORE = "hide_dynamic_more";
    public static final String KEY_HIDE_QZONE_ENTRY = "hide_qzone_entry";
    public static final String KEY_HIDE_NEAR_ENTRY = "hide_near_entry";
    public static final String KEY_HIDE_TRIBAL_ENTRY = "hide_tribal_entry";
    public static final String KEY_HIDE_QZONE_AVATAR_REMIND = "hide_qzone_avatar_remind";
    public static final String KEY_HIDE_NEAR_AVATAR_REMIND = "hide_near_avatar_remind";
    public static final String KEY_HIDE_TRIBAL_AVATAR_REMIND = "hide_tribal_avatar_remind";

    public static final String KEY_HIDE_SIDEBAR_MY_DAILY = "hide_sidebar_my_daily";
    public static final String KEY_HIDE_SIDEBAR_MY_QR_CODE = "hide_sidebar_my_qr_code";
    public static final String KEY_HIDE_SIDEBAR_QQ_INFO = "hide_sidebar_qq_info";
    public static final String KEY_HIDE_SIDEBAR_VIP = "hide_sidebar_vip";
    public static final String KEY_HIDE_SIDEBAR_POCKET = "hide_sidebar_pocket";
    public static final String KEY_HIDE_SIDEBAR_DRESS_UP = "hide_sidebar_dress_up";
    public static final String KEY_HIDE_SIDEBAR_MY_FAVORITES = "hide_sidebar_my_favorites";
    public static final String KEY_HIDE_SIDEBAR_MY_PHOTOS = "hide_sidebar_my_photos";
    public static final String KEY_HIDE_SIDEBAR_MY_FILES = "hide_sidebar_my_files";
    public static final String KEY_HIDE_SIDEBAR_MY_VIDEOS = "hide_sidebar_my_videos";
    public static final String KEY_HIDE_SIDEBAR_MY_CARDS = "hide_sidebar_my_cards";
    public static final String KEY_HIDE_SIDEBAR_FREE_FLOW = "hide_sidebar_free_flow";
    public static final String KEY_HIDE_SIDEBAR_THEME_NIGHT = "hide_sidebar_theme_night";
    public static final String KEY_HIDE_SIDEBAR_CITY_WEATHER = "hide_sidebar_city_weather";
    public static final String KEY_HIDE_SIDEBAR_MY_CITY = "hide_sidebar_my_city";

    public static final String KEY_HIDE_AVATAR_PENDANT = "hide_avatar_pendant";
    public static final String KEY_HIDE_CHAT_BUBBLE = "hide_chat_bubble";
    public static final String KEY_HIDE_FONT_EFFECTS = "hide_font_effects";
    public static final String KEY_HIDE_EXPRESSION_ASSOCIATION = "hide_expression_association";
    public static final String KEY_HIDE_RECOMMENDED_EXPRESSION = "hide_recommended_expression";
    public static final String KEY_HIDE_CHAT_CALL = "hide_chat_call";
    public static final String KEY_HIDE_GET_NEW_BADGE = "hide_get_new_badge";
    public static final String KEY_HIDE_NEW_FEED = "hide_new_feed";
    public static final String KEY_HIDE_CHAT_UNUSUAL_CONTACTS = "hide_chat_unusual_contacts";
    public static final String KEY_HIDE_CHAT_SOUGOU_AD = "hide_chat_sougou_ad";
    public static final String KEY_HIDE_CHAT_TOOLBAR_VOICE = "hide_chat_toolbar_voice";
    public static final String KEY_HIDE_CHAT_TOOLBAR_PIC = "hide_chat_toolbar_pic";
    public static final String KEY_HIDE_CHAT_TOOLBAR_CAMERA = "hide_chat_toolbar_camera";
    public static final String KEY_HIDE_CHAT_TOOLBAR_POKE = "hide_chat_toolbar_poke";
    public static final String KEY_HIDE_CHAT_TOOLBAR_RED_ENVELOPE = "hide_chat_toolbar_red_envelope";
    public static final String KEY_HIDE_CHAT_TOOLBAR_GIF = "hide_chat_toolbar_gif";

    public static final String KEY_HIDE_GROUP_MEMBER_LEVEL = "hide_group_member_level";
    public static final String KEY_HIDE_GROUP_MEMBER_GLAMOUR_LEVEL = "hide_group_member_glamour_level";
    public static final String KEY_HIDE_GROUP_GIFT_ANIM = "hide_group_gift_anim";
    public static final String KEY_HIDE_GROUP_CHAT_ADMISSIONS = "hide_group_chat_admissions";
    public static final String KEY_HIDE_GROUP_SMALL_VIDEO = "hide_group_small_video";
    public static final String KEY_HIDE_GROUP_HELPER_REMOVE_TIPS = "hide_group_helper_remove_tips";
    public static final String KEY_HIDE_PIC_SMILE = "hide_pic_smile";

    public static final String KEY_CLOSE_ALL_ANIMATION = "close_all_animation";
    public static final String KEY_HIDE_SOME_RED_DOT = "hide_some_red_dot";
    public static final String KEY_HIDE_TIM_IN_MY_FILE = "hide_tim_in_my_file";
    public static final String KEY_HIDE_QZONE_AD = "hide_qzone_ad";
    public static final String KEY_HIDE_SETTING_PHONE_NUMBER = "hide_setting_phone_number";
    public static final String KEY_HIDE_SETTING_QQ_EXPERT = "hide_setting_qq_expert";
    public static final String KEY_HIDE_SETTING_CLEAR = "hide_setting_clear";
    public static final String KEY_HIDE_SETTING_FREE_FLOW = "hide_setting_free_flow";

    public static final String KEY_PREVENT_FLASH_DISAPPEAR = "prevent_flash_disappear";
    public static final String KEY_PREVENT_MESSAGES_WITHDRAWN = "prevent_messages_withdrawn";
    public static final String KEY_SIMULATE_MENU = "simulate_menu";
    public static final String KEY_CLOSE_ALL = "close_all";
    public static final String KEY_DESKTOP_ICON = "desktop_icon";
    public static final String KEY_SETTING_QQ = "setting_qq";

    public static final String KEY_INSTRUCTIONS = "instructions";
    public static final String KEY_GITHUB_SOURCE = "github_source";

    public static boolean isModuleActive() {
        return false;
    }

    public static String getQQVersion(Context context) {
        if (context != null) {
            try {
                return context.getPackageManager().getPackageInfo(PACKAGE_NAME_QQ, 0).versionName;
            } catch (Exception e) {
                XposedBridge.log(e);
            }
        }
        return "0.0";
    }
}
