package me.zpp0196.qqsimple.util;

import java.lang.ref.WeakReference;

import de.robv.android.xposed.XSharedPreferences;
import me.zpp0196.qqsimple.BuildConfig;

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
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CONTACTS_TAB_PHONE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CONTACTS_TAB_PUB_ACCOUNT;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_CREATE_TROOP_ENTRY;
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
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SETTING_FREE_FLOW;
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
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SIDEBAR_THEME_NIGHT;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SIDEBAR_VIP;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SOME_RED_DOT;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_SPLASH_AD;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_TIM_IN_MY_FILE;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_TRIBAL_AVATAR_REMIND;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_TRIBAL_ENTRY;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_UNCHECKED_MSG_NUM;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_UNUSUAL_CONTACTS;
import static me.zpp0196.qqsimple.Common.KEY_HIDE_UPDATE_REMINDER;
import static me.zpp0196.qqsimple.Common.KEY_MASTER_SWITCH;
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

    public static boolean getValueMasterSwitch() {
        return returnValue(KEY_MASTER_SWITCH);
    }

    public static boolean getValueHideChatListHeadAd() {
        return returnValue(KEY_HIDE_CHAT_LIST_HEAD_AD);
    }

    public static boolean getValueHideUpdateReminder() {
        return returnValue(KEY_HIDE_UPDATE_REMINDER);
    }

    public static boolean getValueHideNationalEntrance() {
        return returnValue(KEY_HIDE_NATIONAL_ENTRANCE);
    }

    public static boolean getValueHideUncheckedMsgNum() {
        return returnValue(KEY_HIDE_UNCHECKED_MSG_NUM);
    }

    public static boolean getValueHideContactsTabPhone() {
        return returnValue(KEY_HIDE_CONTACTS_TAB_PHONE);
    }

    public static boolean getValueHideContactsTabPubAccount() {
        return returnValue(KEY_HIDE_CONTACTS_TAB_PUB_ACCOUNT);
    }

    public static boolean getValueHideNewFriendEntry() {
        return returnValue(KEY_HIDE_NEW_FRIEND_ENTRY);
    }

    public static boolean getValueHideCreateTroopEntry() {
        return returnValue(KEY_HIDE_CREATE_TROOP_ENTRY);
    }

    public static boolean getValueHideUnusualContacts() {
        return returnValue(KEY_HIDE_UNUSUAL_CONTACTS);
    }

    public static boolean getValueHideEveryoneSearching() {
        return returnValue(KEY_HIDE_EVERYONE_SEARCHING);
    }

    public static boolean getValueHideQzoneEntry() {
        return returnValue(KEY_HIDE_QZONE_ENTRY);
    }

    public static boolean getValueHideQzoneAvatarRemind() {
        return returnValue(KEY_HIDE_QZONE_AVATAR_REMIND);
    }

    public static boolean getValueHideNearEntry() {
        return returnValue(KEY_HIDE_NEAR_ENTRY);
    }

    public static boolean getValueHideNearAvatarRemind() {
        return returnValue(KEY_HIDE_NEAR_AVATAR_REMIND);
    }

    public static boolean getValueHideTribalEntry() {
        return returnValue(KEY_HIDE_TRIBAL_ENTRY);
    }

    public static boolean getValueHideTribalAvatarRemind() {
        return returnValue(KEY_HIDE_TRIBAL_AVATAR_REMIND);
    }

    public static boolean getValueHideAvatarPendant() {
        return returnValue(KEY_HIDE_AVATAR_PENDANT);
    }

    public static boolean getValueHideChatBubble() {
        return returnValue(KEY_HIDE_CHAT_BUBBLE);
    }

    public static boolean getValueHideFontEffects() {
        return returnValue(KEY_HIDE_FONT_EFFECTS);
    }

    public static boolean getValueHideExpressionAssociation() {
        return returnValue(KEY_HIDE_EXPRESSION_ASSOCIATION);
    }

    public static boolean getValueHideRecommendedExpression() {
        return returnValue(KEY_HIDE_RECOMMENDED_EXPRESSION);
    }

    public static boolean getValueHidePicSmile() {
        return returnValue(KEY_HIDE_PIC_SMILE);
    }

    public static boolean getValueHideChatCall() {
        return returnValue(KEY_HIDE_CHAT_CALL);
    }

    public static boolean getValueHideGetNewBadge() {
        return returnValue(KEY_HIDE_GET_NEW_BADGE);
    }

    public static boolean getValueHideNewFeed() {
        return returnValue(KEY_HIDE_NEW_FEED);
    }

    public static boolean getValueHideChatUnusualContacts() {
        return returnValue(KEY_HIDE_CHAT_UNUSUAL_CONTACTS);
    }

    public static boolean getValueHideChatSougouAd() {
        return returnValue(KEY_HIDE_CHAT_SOUGOU_AD);
    }

    public static boolean getValueHideChatToolbarVoice() {
        return returnValue(KEY_HIDE_CHAT_TOOLBAR_VOICE);
    }

    public static boolean getValueHideChatToolbarCamera() {
        return returnValue(KEY_HIDE_CHAT_TOOLBAR_CAMERA);
    }

    public static boolean getValueHideChatToolbarPoke() {
        return returnValue(KEY_HIDE_CHAT_TOOLBAR_POKE);
    }

    public static boolean getValueHideChatToolbarRedEnvelope() {
        return returnValue(KEY_HIDE_CHAT_TOOLBAR_RED_ENVELOPE);
    }

    public static boolean getValueHideChatToolbarGif() {
        return returnValue(KEY_HIDE_CHAT_TOOLBAR_GIF);
    }

    public static boolean getValueHideGroupMemberLevel() {
        return returnValue(KEY_HIDE_GROUP_MEMBER_LEVEL);
    }

    public static boolean getValueHideGroupMemberGlamourLevel() {
        return returnValue(KEY_HIDE_GROUP_MEMBER_GLAMOUR_LEVEL);
    }

    public static boolean getValueHideGroupGiftAnim() {
        return returnValue(KEY_HIDE_GROUP_GIFT_ANIM);
    }

    public static boolean getValueHideGroupChatAdmissions() {
        return returnValue(KEY_HIDE_GROUP_CHAT_ADMISSIONS);
    }

    public static boolean getValueHideGroupSmallVideo() {
        return returnValue(KEY_HIDE_GROUP_SMALL_VIDEO);
    }

    public static boolean getValueHideGroupHelperRemoveTips() {
        return returnValue(KEY_HIDE_GROUP_HELPER_REMOVE_TIPS);
    }

    public static boolean getValueHideSidebarVip() {
        return returnValue(KEY_HIDE_SIDEBAR_VIP);
    }

    public static boolean getValueHideSidebarPocket() {
        return returnValue(KEY_HIDE_SIDEBAR_POCKET);
    }

    public static boolean getValueHideSidebarDressUp() {
        return returnValue(KEY_HIDE_SIDEBAR_DRESS_UP);
    }

    public static boolean getValueHideSidebarMyFavorites() {
        return returnValue(KEY_HIDE_SIDEBAR_MY_FAVORITES);
    }

    public static boolean getValueHideSidebarMyPhotos() {
        return returnValue(KEY_HIDE_SIDEBAR_MY_PHOTOS);
    }

    public static boolean getValueHideSidebarMyFiles() {
        return returnValue(KEY_HIDE_SIDEBAR_MY_FILES);
    }

    public static boolean getValueHideSidebarMyVideos() {
        return returnValue(KEY_HIDE_SIDEBAR_MY_VIDEOS);
    }

    public static boolean getValueHideSidebarMyCards() {
        return returnValue(KEY_HIDE_SIDEBAR_MY_CARDS);
    }

    public static boolean getValueHideSidebarFreeFlow() {
        return returnValue(KEY_HIDE_SIDEBAR_FREE_FLOW);
    }

    public static boolean getValueHideSidebarMyDaily() {
        return returnValue(KEY_HIDE_SIDEBAR_MY_DAILY);
    }

    public static boolean getValueHideSidebarMyQrCode() {
        return returnValue(KEY_HIDE_SIDEBAR_MY_QR_CODE);
    }

    public static boolean getValueHideSidebarThemeNight() {
        return returnValue(KEY_HIDE_SIDEBAR_THEME_NIGHT);
    }

    public static boolean getValueHideSidebarCityWeather() {
        return returnValue(KEY_HIDE_SIDEBAR_CITY_WEATHER);
    }

    public static boolean getValueHideSidebarMyCity() {
        return returnValue(KEY_HIDE_SIDEBAR_MY_CITY);
    }

    public static boolean getValueCloseAllAnimation() {
        return returnValue(KEY_CLOSE_ALL_ANIMATION);
    }

    public static boolean getValueHideSplashAd() {
        return returnValue(KEY_HIDE_SPLASH_AD);
    }

    public static boolean getValueHideSomeRedDot() {
        return returnValue(KEY_HIDE_SOME_RED_DOT);
    }

    public static boolean getValueHideSettingFreeFlow() {
        return returnValue(KEY_HIDE_SETTING_FREE_FLOW);
    }

    public static boolean getValueHideTimInMyFile() {
        return returnValue(KEY_HIDE_TIM_IN_MY_FILE);
    }

    public static boolean getValueHideQzoneAd() {
        return returnValue(KEY_HIDE_QZONE_AD);
    }

    public static boolean getValuePreventFlashDisappear() {
        return returnValue(KEY_PREVENT_FLASH_DISAPPEAR);
    }

    public static boolean getValuePreventMessagesWithdrawn() {
        return returnValue(KEY_PREVENT_MESSAGES_WITHDRAWN);
    }

    private static boolean returnValue(String key) {
        return getPref().getBoolean(key, false);
    }
}
