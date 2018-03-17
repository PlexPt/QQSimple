package me.zpp0196.qqsimple.util;

import java.lang.ref.WeakReference;

import de.robv.android.xposed.XSharedPreferences;
import me.zpp0196.qqsimple.BuildConfig;

import static me.zpp0196.qqsimple.Common.KEY_CLOSE_ALL;
import static me.zpp0196.qqsimple.Common.KEY_CLOSE_ALL_ANIMATION;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_AVATAR_PENDANT;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_BUBBLE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_CALL;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_LIST_HEAD_AD;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_SOUGOU_AD;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_TOOLBAR_CAMERA;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_TOOLBAR_GIF;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_TOOLBAR_POKE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_TOOLBAR_RED_ENVELOPE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_TOOLBAR_VOICE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CHAT_UNUSUAL_CONTACTS;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CONTACTS_TAB_DEVICE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CONTACTS_TAB_PHONE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CONTACTS_TAB_PUB_ACCOUNT;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CREATE_TROOP_ENTRY;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_DYNAMIC_MORE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_EVERYONE_SEARCHING;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_EXPRESSION_ASSOCIATION;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_FONT_EFFECTS;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_GET_NEW_BADGE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_GROUP_CHAT_ADMISSIONS;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_GROUP_GIFT_ANIM;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_GROUP_HELPER_REMOVE_TIPS;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_GROUP_MEMBER_GLAMOUR_LEVEL;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_GROUP_MEMBER_LEVEL;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_GROUP_SMALL_VIDEO;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_NATIONAL_ENTRANCE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_NEAR_AVATAR_REMIND;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_NEAR_ENTRY;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_NEW_FEED;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_NEW_FRIEND_ENTRY;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_PIC_SMILE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_QZONE_AD;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_QZONE_AVATAR_REMIND;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_QZONE_ENTRY;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_RECOMMENDED_EXPRESSION;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SEARCH_BOX;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SETTING_CLEAR;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SETTING_FREE_FLOW;
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
import static me.zpp0196.qqsimple.Common.KEY_HIDE_TAB_CONTACT;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_TAB_DYNAMIC;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_TIM_IN_MY_FILE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_TRIBAL_AVATAR_REMIND;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_TRIBAL_ENTRY;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_UNCHECKED_MSG_NUM;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_UNUSUAL_CONTACTS;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_UPDATE_REMINDER;
import static me.zpp0196.qqsimple.Common.KEY_PREVENT_FLASH_DISAPPEAR;
import static me.zpp0196.qqsimple.Common.KEY_PREVENT_MESSAGES_WITHDRAWN;

/**
 * Created by Deng on 2018/1/12.
 */

public class SettingUtils {

    private static WeakReference<XSharedPreferences> xSharedPreferences = new WeakReference<>(null);

    private static XSharedPreferences getPref() {
        XSharedPreferences preferences = xSharedPreferences.get();
        if (preferences == null) {
            preferences = new XSharedPreferences(BuildConfig.APPLICATION_ID);
            preferences.makeWorldReadable();
            xSharedPreferences = new WeakReference<>(preferences);
        } else {
            preferences.reload();
        }
        return preferences;
    }

    public static boolean getValueHideSearchBox() {
        return getValue(KEY_HIDE_SEARCH_BOX);
    }

    public static boolean getValueHideTabContact() {
        return getValue(KEY_HIDE_TAB_CONTACT);
    }

    public static boolean getValueHideTabDynamic() {
        return getValue(KEY_HIDE_TAB_DYNAMIC);
    }

    public static boolean getValueHideChatListHeadAd() {
        return getValue(KEY_HIDE_CHAT_LIST_HEAD_AD);
    }

    public static boolean getValueHideUpdateReminder() {
        return getValue(KEY_HIDE_UPDATE_REMINDER);
    }

    public static boolean getValueHideNationalEntrance() {
        return getValue(KEY_HIDE_NATIONAL_ENTRANCE);
    }

    public static boolean getValueHideUncheckedMsgNum() {
        return getValue(KEY_HIDE_UNCHECKED_MSG_NUM);
    }

    public static boolean getValueHideNewFriendEntry() {
        return getValue(KEY_HIDE_NEW_FRIEND_ENTRY);
    }

    public static boolean getValueHideCreateTroopEntry() {
        return getValue(KEY_HIDE_CREATE_TROOP_ENTRY);
    }

    public static boolean getValueHideContactsTabDevice() {
        return getValue(KEY_HIDE_CONTACTS_TAB_DEVICE);
    }

    public static boolean getValueHideContactsTabPhone() {
        return getValue(KEY_HIDE_CONTACTS_TAB_PHONE);
    }

    public static boolean getValueHideContactsTabPubAccount() {
        return getValue(KEY_HIDE_CONTACTS_TAB_PUB_ACCOUNT);
    }

    public static boolean getValueHideUnusualContacts() {
        return getValue(KEY_HIDE_UNUSUAL_CONTACTS);
    }

    public static boolean getValueHideEveryoneSearching() {
        return getValue(KEY_HIDE_EVERYONE_SEARCHING);
    }

    public static boolean getValueHideDynamicMore() {
        return getValue(KEY_HIDE_DYNAMIC_MORE);
    }

    public static boolean getValueHideQzoneEntry() {
        return getValue(KEY_HIDE_QZONE_ENTRY);
    }

    public static boolean getValueHideNearEntry() {
        return getValue(KEY_HIDE_NEAR_ENTRY);
    }

    public static boolean getValueHideTribalEntry() {
        return getValue(KEY_HIDE_TRIBAL_ENTRY);
    }

    public static boolean getValueHideQzoneAvatarRemind() {
        return getValue(KEY_HIDE_QZONE_AVATAR_REMIND);
    }

    public static boolean getValueHideNearAvatarRemind() {
        return getValue(KEY_HIDE_NEAR_AVATAR_REMIND);
    }

    public static boolean getValueHideTribalAvatarRemind() {
        return getValue(KEY_HIDE_TRIBAL_AVATAR_REMIND);
    }

    public static boolean getValueHideSidebarMyDaily() {
        return getValue(KEY_HIDE_SIDEBAR_MY_DAILY);
    }

    public static boolean getValueHideSidebarMyQrCode() {
        return getValue(KEY_HIDE_SIDEBAR_MY_QR_CODE);
    }

    public static boolean getValueHideSidebarQQInfo() {
        return getValue(KEY_HIDE_SIDEBAR_QQ_INFO);
    }

    public static boolean getValueHideSidebarVip() {
        return getValue(KEY_HIDE_SIDEBAR_VIP);
    }

    public static boolean getValueHideSidebarPocket() {
        return getValue(KEY_HIDE_SIDEBAR_POCKET);
    }

    public static boolean getValueHideSidebarDressUp() {
        return getValue(KEY_HIDE_SIDEBAR_DRESS_UP);
    }

    public static boolean getValueHideSidebarMyFavorites() {
        return getValue(KEY_HIDE_SIDEBAR_MY_FAVORITES);
    }

    public static boolean getValueHideSidebarMyPhotos() {
        return getValue(KEY_HIDE_SIDEBAR_MY_PHOTOS);
    }

    public static boolean getValueHideSidebarMyFiles() {
        return getValue(KEY_HIDE_SIDEBAR_MY_FILES);
    }

    public static boolean getValueHideSidebarMyVideos() {
        return getValue(KEY_HIDE_SIDEBAR_MY_VIDEOS);
    }

    public static boolean getValueHideSidebarMyCards() {
        return getValue(KEY_HIDE_SIDEBAR_MY_CARDS);
    }

    public static boolean getValueHideSidebarFreeFlow() {
        return getValue(KEY_HIDE_SIDEBAR_FREE_FLOW);
    }

    public static boolean getValueHideSidebarThemeNight() {
        return getValue(KEY_HIDE_SIDEBAR_THEME_NIGHT);
    }

    public static boolean getValueHideSidebarCityWeather() {
        return getValue(KEY_HIDE_SIDEBAR_CITY_WEATHER);
    }

    public static boolean getValueHideSidebarMyCity() {
        return getValue(KEY_HIDE_SIDEBAR_MY_CITY);
    }

    public static boolean getValueHideAvatarPendant() {
        return getValue(KEY_HIDE_AVATAR_PENDANT);
    }

    public static boolean getValueHideChatBubble() {
        return getValue(KEY_HIDE_CHAT_BUBBLE);
    }

    public static boolean getValueHideFontEffects() {
        return getValue(KEY_HIDE_FONT_EFFECTS);
    }

    public static boolean getValueHideExpressionAssociation() {
        return getValue(KEY_HIDE_EXPRESSION_ASSOCIATION);
    }

    public static boolean getValueHideRecommendedExpression() {
        return getValue(KEY_HIDE_RECOMMENDED_EXPRESSION);
    }

    public static boolean getValueHideChatCall() {
        return getValue(KEY_HIDE_CHAT_CALL);
    }

    public static boolean getValueHideGetNewBadge() {
        return getValue(KEY_HIDE_GET_NEW_BADGE);
    }

    public static boolean getValueHideNewFeed() {
        return getValue(KEY_HIDE_NEW_FEED);
    }

    public static boolean getValueHideChatUnusualContacts() {
        return getValue(KEY_HIDE_CHAT_UNUSUAL_CONTACTS);
    }

    public static boolean getValueHideChatSougouAd() {
        return getValue(KEY_HIDE_CHAT_SOUGOU_AD);
    }

    public static boolean getValueHideChatToolbarVoice() {
        return getValue(KEY_HIDE_CHAT_TOOLBAR_VOICE);
    }

    public static boolean getValueHideChatToolbarCamera() {
        return getValue(KEY_HIDE_CHAT_TOOLBAR_CAMERA);
    }

    public static boolean getValueHideChatToolbarPoke() {
        return getValue(KEY_HIDE_CHAT_TOOLBAR_POKE);
    }

    public static boolean getValueHideChatToolbarRedEnvelope() {
        return getValue(KEY_HIDE_CHAT_TOOLBAR_RED_ENVELOPE);
    }

    public static boolean getValueHideChatToolbarGif() {
        return getValue(KEY_HIDE_CHAT_TOOLBAR_GIF);
    }

    public static boolean getValueHideGroupMemberLevel() {
        return getValue(KEY_HIDE_GROUP_MEMBER_LEVEL);
    }

    public static boolean getValueHideGroupMemberGlamourLevel() {
        return getValue(KEY_HIDE_GROUP_MEMBER_GLAMOUR_LEVEL);
    }

    public static boolean getValueHideGroupGiftAnim() {
        return getValue(KEY_HIDE_GROUP_GIFT_ANIM);
    }

    public static boolean getValueHideGroupChatAdmissions() {
        return getValue(KEY_HIDE_GROUP_CHAT_ADMISSIONS);
    }

    public static boolean getValueHideGroupSmallVideo() {
        return getValue(KEY_HIDE_GROUP_SMALL_VIDEO);
    }

    public static boolean getValueHideGroupHelperRemoveTips() {
        return getValue(KEY_HIDE_GROUP_HELPER_REMOVE_TIPS);
    }

    public static boolean getValueHidePicSmile() {
        return getValue(KEY_HIDE_PIC_SMILE);
    }

    public static boolean getValueCloseAllAnimation() {
        return getValue(KEY_CLOSE_ALL_ANIMATION);
    }

    public static boolean getValueHideSomeRedDot() {
        return getValue(KEY_HIDE_SOME_RED_DOT);
    }

    public static boolean getValueHideTimInMyFile() {
        return getValue(KEY_HIDE_TIM_IN_MY_FILE);
    }

    public static boolean getValueHideQzoneAd() {
        return getValue(KEY_HIDE_QZONE_AD);
    }

    public static boolean getValueHideSettingPhoneNumber() {
        return getValue(KEY_HIDE_SETTING_PHONE_NUMBER);
    }

    public static boolean getValueHideSettingQQExpert() {
        return getValue(KEY_HIDE_SETTING_QQ_EXPERT);
    }

    public static boolean getValueHideSettingClear() {
        return getValue(KEY_HIDE_SETTING_CLEAR);
    }

    public static boolean getValueHideSettingFreeFlow() {
        return getValue(KEY_HIDE_SETTING_FREE_FLOW);
    }

    public static boolean getValuePreventFlashDisappear() {
        return getValue(KEY_PREVENT_FLASH_DISAPPEAR);
    }

    public static boolean getValuePreventMessagesWithdrawn() {
        return getValue(KEY_PREVENT_MESSAGES_WITHDRAWN);
    }

    public static boolean getValueCloseAll() {
        return getValue(KEY_CLOSE_ALL);
    }

    private static boolean getValue(String key) {
        return getPref().getBoolean(key, false);
    }
}
