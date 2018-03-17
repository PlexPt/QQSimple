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

    private int adviewlayout;
    private int search_container;
    private int search_box;
    private int unchecked_msg_num;
    private int newFriendEntry;
    private int createTroopEntry;
    private int unusual_contacts_footerview;
    private int qzone_feed_entry;
    private int near_people_entry;
    private int xingqu_buluo_entry;
    private int qzone_feed_entry_sub_iv;
    private int nearby_people_entry_sub_iv;
    private int buluo_entry_sub_iv;
    private int ivTitleBtnRightCall;
    private int mydaily;
    private int qr_code_icon;
    private int nickname_area;
    private int richstatus_txt;
    private int sig_layout;
    private int myvip;
    private int mypocket;
    private int myDressup;
    private int myfavorites;
    private int myphotos;
    private int myfiles;
    private int myvideos;
    private int mycards;
    private int cuKingCard;
    private int nightmode;
    private int weather_layout;
    private int weather_area;
    private int qq_aio_panel_ptt;
    private int qq_aio_panel_ptv;
    private int qq_aio_panel_hot_pic;
    private int qq_aio_panel_hongbao;
    private int qq_aio_panel_poke;
    private int chat_item_troop_member_level;
    private int chat_item_troop_member_glamour_level;
    private int troop_assistant_feeds_title_small_video;
    private int troop_assistant_feeds_title_super_owner;
    private int chat_top_bar;
    private int find_reddot;
    private int item_right_reddot;
    private int iv_reddot;
    private int qzone_feed_reddot;
    private int qzone_mood_reddot;
    private int qzone_super_font_tab_reddot;
    private int qzone_uploadphoto_item_reddot;
    private int tv_reddot;
    private int xingqu_buluo_reddot;
    private int timtips;
    private int qqsetting2_phone_unity_info;
    private int qqsetting2_newXmanLayout;
    private int qqsetting2_msg_qqclean;
    private int shuoshuo_ad_upload_quality;
    private int quality_hd_ad;
    private int quality_ad;
    private int pic_light_emoj;
    private int skin_tips_dot;
    private int skin_tips_dot_small;
    private int skin_tips_new;
    private int shortvideo_redbag_outicon;
    private int skin_searchbar_button_pressed_theme_version2;

    public RemoveImagine(Class<?> id, Class<?> drawable) {
        this.id = id;
        this.drawable = drawable;
        initId();
    }

    private void initId(){
        adviewlayout = getId("adviewlayout");
        search_container = getId("search_container");
        search_box = getId("search_box");
        unchecked_msg_num = getId("unchecked_msg_num");
        newFriendEntry = getId("newFriendEntry");
        createTroopEntry = getId("createTroopEntry");
        unusual_contacts_footerview = getId("unusual_contacts_footerview");
        qzone_feed_entry = getId("qzone_feed_entry");
        near_people_entry = getId("near_people_entry");
        xingqu_buluo_entry = getId("xingqu_buluo_entry");
        qzone_feed_entry_sub_iv = getId("qzone_feed_entry_sub_iv");
        nearby_people_entry_sub_iv = getId("nearby_people_entry_sub_iv");
        buluo_entry_sub_iv = getId("buluo_entry_sub_iv");
        ivTitleBtnRightCall = getId("qzone_feed_entry_sub_iv");
        mydaily = getId("mydaily");
        qr_code_icon = getId("qr_code_icon");
        nickname_area = getId("nickname_area");
        sig_layout = getId("richstatus_txt");
        richstatus_txt = getId("richstatus_txt");
        myvip = getId("myvip");
        mypocket = getId("mypocket");
        myDressup = getId("myDressup");
        myfavorites = getId("myfavorites");
        myphotos = getId("myphotos");
        myfiles = getId("myfiles");
        myvideos = getId("myvideos");
        mycards = getId("mycards");
        cuKingCard = getId("cuKingCard");
        nightmode = getId("nightmode");
        weather_layout = getId("weather_layout");
        weather_area = getId("weather_area");
        qq_aio_panel_ptt = getId("qq_aio_panel_ptt");
        qq_aio_panel_ptv = getId("qq_aio_panel_ptv");
        qq_aio_panel_hot_pic = getId("qq_aio_panel_hot_pic");
        qq_aio_panel_hongbao = getId("qq_aio_panel_hongbao");
        qq_aio_panel_poke = getId("qq_aio_panel_poke");
        chat_item_troop_member_level = getId("chat_item_troop_member_level");
        chat_item_troop_member_glamour_level = getId("chat_item_troop_member_glamour_level");
        troop_assistant_feeds_title_small_video = getId("troop_assistant_feeds_title_small_video");
        troop_assistant_feeds_title_super_owner = getId("troop_assistant_feeds_title_super_owner");
        chat_top_bar = getId("chat_top_bar");
        find_reddot = getId("find_reddot");
        item_right_reddot = getId("item_right_reddot");
        iv_reddot = getId("iv_reddot");
        qzone_feed_reddot = getId("qzone_feed_reddot");
        qzone_mood_reddot = getId("qzone_mood_reddot");
        qzone_super_font_tab_reddot = getId("qzone_super_font_tab_reddot");
        qzone_uploadphoto_item_reddot = getId("qzone_uploadphoto_item_reddot");
        tv_reddot = getId("tv_reddot");
        xingqu_buluo_reddot = getId("xingqu_buluo_reddot");
        timtips = getId("timtips");
        qqsetting2_phone_unity_info = getId("qqsetting2_phone_unity_info");
        qqsetting2_newXmanLayout = getId("qqsetting2_newXmanLayout");
        qqsetting2_msg_qqclean = getId("qqsetting2_msg_qqclean");
        shuoshuo_ad_upload_quality = getId("shuoshuo_ad_upload_quality");
        quality_hd_ad = getId("quality_ad");
        quality_ad = getId("quality_ad");
        pic_light_emoj = getId("pic_light_emoj");
        skin_tips_dot = getDrawableId("skin_tips_dot");
        skin_tips_dot_small = getDrawableId("skin_tips_dot_small");
        skin_tips_new = getDrawableId("skin_tips_new");
        shortvideo_redbag_outicon = getDrawableId("shortvideo_redbag_outicon");
        skin_searchbar_button_pressed_theme_version2 = getDrawableId("skin_searchbar_button_pressed_theme_version2");
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
        if (id == adviewlayout) {
            // 隐藏消息列表界面横幅广告
            isHide = SettingUtils.getValueHideChatListHeadAd();
        } else if (id == search_container || id == search_box) {
            // 隐藏搜索框
            isHide = SettingUtils.getValueHideSearchBox();
        } else if (id == unchecked_msg_num) {
            // 隐藏消息列表未读消息数量
            isHide = SettingUtils.getValueHideUncheckedMsgNum();
        } else if (id == newFriendEntry) {
            // 隐藏联系人界面新朋友
            isHide = SettingUtils.getValueHideNewFriendEntry();
        } else if (id == createTroopEntry) {
            // 隐藏联系人界面创建群聊
            isHide = SettingUtils.getValueHideCreateTroopEntry();
        } else if (id == unusual_contacts_footerview) {
            // 隐藏联系人界面不常用联系人
            isHide = SettingUtils.getValueHideUnusualContacts();
        } else if (id == qzone_feed_entry) {
            // 隐藏动态界面空间入口
            isHide = SettingUtils.getValueHideQzoneEntry();
        } else if (id == near_people_entry) {
            // 隐藏动态界面附近的人入口
            isHide = SettingUtils.getValueHideNearEntry();
        } else if (id == xingqu_buluo_entry) {
            // 隐藏动态界面兴趣部落入口
            isHide = SettingUtils.getValueHideTribalEntry();
        } else if (id == qzone_feed_entry_sub_iv) {
            // 隐藏动态界面空间头像提醒
            isHide = SettingUtils.getValueHideQzoneAvatarRemind();
        } else if (id == nearby_people_entry_sub_iv) {
            // 隐藏动态界面附近的人头像提醒
            isHide = SettingUtils.getValueHideNearAvatarRemind();
        } else if (id == buluo_entry_sub_iv) {
            // 隐藏动态界面兴趣部落头像提醒
            isHide = SettingUtils.getValueHideTribalAvatarRemind();
        } else if (id == ivTitleBtnRightCall) {
            // 隐藏聊天界面右上角的 QQ 电话
            isHide = SettingUtils.getValueHideChatCall();
        } else if (id == mydaily) {
            // 隐藏侧滑栏打卡
            isHide = SettingUtils.getValueHideSidebarMyDaily();
        } else if (id == qr_code_icon) {
            // 隐藏侧滑栏我的二维码
            isHide = SettingUtils.getValueHideSidebarMyQrCode();
        } else if (id == nickname_area || id == richstatus_txt || id == sig_layout) {
            // 隐藏侧滑栏 QQ 信息
            isHide = SettingUtils.getValueHideSidebarQQInfo();
        } else if (id == myvip) {
            // 隐藏侧滑栏会员栏
            isHide = SettingUtils.getValueHideSidebarVip();
        } else if (id == mypocket) {
            // 隐藏侧滑栏 QQ 钱包
            isHide = SettingUtils.getValueHideSidebarPocket();
        } else if (id == myDressup) {
            // 隐藏侧滑栏个性装扮
            isHide = SettingUtils.getValueHideSidebarDressUp();
        } else if (id == myfavorites) {
            // 隐藏侧滑栏我的收藏
            isHide = SettingUtils.getValueHideSidebarMyFavorites();
        } else if (id == myphotos) {
            // 隐藏侧滑栏我的相册
            isHide = SettingUtils.getValueHideSidebarMyPhotos();
        } else if (id == myfiles) {
            // 隐藏侧滑栏我的文件
            isHide = SettingUtils.getValueHideSidebarMyFiles();
        } else if (id == myvideos) {
            // 隐藏侧滑栏我的视频
            isHide = SettingUtils.getValueHideSidebarMyVideos();
        } else if (id == mycards) {
            // 隐藏侧滑栏我的名片夹
            isHide = SettingUtils.getValueHideSidebarMyCards();
        } else if (id == cuKingCard) {
            // 隐藏侧滑栏免流量特权
            isHide = SettingUtils.getValueHideSidebarFreeFlow();
        } else if (id == nightmode) {
            // 隐藏侧滑栏夜间模式
            isHide = SettingUtils.getValueHideSidebarThemeNight();
        } else if (id == weather_layout) {
            // 隐藏侧滑栏城市天气
            isHide = SettingUtils.getValueHideSidebarCityWeather();
        } else if (id == weather_area) {
            // 隐藏侧滑栏我的城市
            isHide = SettingUtils.getValueHideSidebarMyCity();
        } else if (id == qq_aio_panel_ptt) {
            // 隐藏聊天界面底部工具栏语音按钮
            isHide = SettingUtils.getValueHideChatToolbarVoice();
        } else if (id == qq_aio_panel_ptv) {
            // 隐藏聊天界面底部工具栏视频按钮
            isHide = SettingUtils.getValueHideChatToolbarCamera();
        } else if (id == qq_aio_panel_hot_pic) {
            // 隐藏聊天界面底部工具栏红包按钮
            isHide = SettingUtils.getValueHideChatToolbarGif();
        } else if (id == qq_aio_panel_hongbao) {
            // 隐藏聊天界面底部工具栏戳一戳按钮
            isHide = SettingUtils.getValueHideChatToolbarRedEnvelope();
        } else if (id == qq_aio_panel_poke) {
            // 隐藏聊天界面底部工具栏 GIF 按钮
            isHide = SettingUtils.getValueHideChatToolbarPoke();
        } else if (id == chat_item_troop_member_level) {
            // 隐藏群头衔
            isHide = SettingUtils.getValueHideGroupMemberLevel();
        } else if (id == chat_item_troop_member_glamour_level) {
            // 隐藏魅力等级
            isHide = SettingUtils.getValueHideGroupMemberGlamourLevel();
        } else if (id == troop_assistant_feeds_title_small_video || id == troop_assistant_feeds_title_super_owner) {
            // 隐藏群消息里的小视频
            isHide = SettingUtils.getValueHideGroupSmallVideo();
        } else if (id == chat_top_bar) {
            // 隐藏移出群助手提示
            isHide = SettingUtils.getValueHideGroupHelperRemoveTips();
        } else if (id == find_reddot || id == item_right_reddot || id == iv_reddot || id == qzone_feed_reddot || id == qzone_mood_reddot || id == qzone_super_font_tab_reddot || id == qzone_uploadphoto_item_reddot || id == tv_reddot || id == xingqu_buluo_reddot) {
            // 隐藏部分小红点
            isHide = SettingUtils.getValueHideSomeRedDot();
        } else if (id == timtips) {
            // 隐藏我的文件里的 TIM 推广
            isHide = SettingUtils.getValueHideTimInMyFile();
        } else if (id == qqsetting2_phone_unity_info) {
            // 隐藏设置电话号码
            isHide = SettingUtils.getValueHideSettingPhoneNumber();
        } else if (id == qqsetting2_newXmanLayout) {
            // 隐藏设置 QQ 达人
            isHide = SettingUtils.getValueHideSettingQQExpert();
        } else if (id == qqsetting2_msg_qqclean) {
            // 隐藏设置空间清理
            isHide = SettingUtils.getValueHideSettingClear();
        } else if (id == shuoshuo_ad_upload_quality || id == quality_hd_ad || id == quality_ad) {
            // 隐藏空间绿厂广告
            isHide = SettingUtils.getValueHideQzoneAd();
        }
        if (getQQ_Version().compareTo("7.3.5") >= 0) {
            // 隐藏聊天图片旁的笑脸按钮
            if (id == pic_light_emoj) {
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
        if (i == skin_tips_dot || i == skin_tips_dot_small || i == skin_tips_new || (getQQ_Version().compareTo("7.3.5") > 0) && i == shortvideo_redbag_outicon) {
            // 去除部分小红点
            if (SettingUtils.getValueHideSomeRedDot()) {
                param.args[0] = skin_searchbar_button_pressed_theme_version2;
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
