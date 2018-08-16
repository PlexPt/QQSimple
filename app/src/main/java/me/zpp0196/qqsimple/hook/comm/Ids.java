package me.zpp0196.qqsimple.hook.comm;

import java.util.HashMap;

import me.zpp0196.qqsimple.hook.util.HookUtil;

/**
 * Created by zpp0196 on 2018/4/30 0030.
 */

@SuppressWarnings ("all")
public class Ids {

    private static HashMap<String, Integer> id;

    private static int[] supportVersion = new int[]{818, 828, 832, 836, 850, 852, 864, 872, 884};

    public static void init() {
        if (id == null) {
            id = new HashMap<>();
        } else {
            id.clear();
        }
        int version = HookUtil.getQQVersionCode();
        switch (version) {
            case 818:
                init758_818();
                break;
            case 828:
                init800_828();
                break;
            case 832:
                init760_832();
                break;
            case 836:
                init763_836();
                break;
            case 850:
                init763_850();
            case 852:
                init763_850();
                break;
            case 864:
                init765_864();
                break;
            case 872:
                init768_872();
                break;
            case 884:
                init770_884();
                break;
        }
    }

    public static boolean isSupport(int versionCode) {
        if (versionCode > supportVersion[supportVersion.length - 1]) {
            return false;
        }
        if (versionCode < supportVersion[0]) {
            return true;
        }

        for (int i : supportVersion) {
            if (versionCode == i) {
                return true;
            }
        }
        return false;
    }

    public static Integer getId(String name) {
        return id.get(name);
    }

    private static void init758_818() {
        put("unchecked_msg_num", 0x7f0a0b4f);
        put("adviewlayout", 0x7f0a0e37);
        put("contactHeader", 0x7f0a07e4);
        put("search_container", 0x7f0a1fa2);
        put("slidcards_container", 0x7f0a07df);
        put("newFriendEntry", 0x7f0a07e1);
        put("createTroopEntry", 0x7f0a07e2);
        put("emotionLayout", 0x7f0a045b);
        put("btn_more_emoticon", 0x7f0a09a6);
        put("unusual_contacts_footerview", 0x7f0a0772);
        put("leb_search_entry", 0x7f0a0b0f);
        put("qzone_feed_entry", 0x7f0a1e35);
        put("near_people_entry", 0x7f0a1e3a);
        put("xingqu_buluo_entry", 0x7f0a1e3f);
        put("nearby_people_entry_sub_iv", 0x7f0a1e3e);
        put("buluo_entry_sub_iv", 0x7f0a1e42);
        put("qr_code_icon", 0x7f0a21ef);
        put("mydaily", 0x7f0a21f0);
        put("nickname_area", 0x7f0a21eb);
        put("sig_layout", 0x7f0a21e6);
        put("myvip", 0x7f0a21f6);
        put("mypocket", 0x7f0a21f7);
        put("myDressup", 0x7f0a21f9);
        put("myfavorites", 0x7f0a21fa);
        put("myphotos", 0x7f0a21fb);
        put("myfiles", 0x7f0a21fc);
        put("myvideos", 0x7f0a21ff);
        put("mycards", 0x7f0a21fe);
        put("cuKingCard", 0x7f0a2201);
        put("settings", 0x7f0a2202);
        put("nightmode", 0x7f0a2206);
        put("weather_layout", 0x7f0a2209);
        put("weather_area", 0x7f0a220d);
        put("chat_item_pendant_image", 0x7f0a0057);
        put("ivTitleBtnRightCall", 0x7f0a0833);
        put("qq_aio_panel_ptt", 0x7f0a01b4);
        put("qq_aio_panel_ptt_gold_msg", 0x7f0a01b6);
        put("qq_aio_panel_image", 0x7f0a01ba);
        put("qq_aio_panel_image_gold_msg", 0x7f0a01bc);
        put("qq_aio_panel_camera", 0x7f0a01bd);
        put("qq_aio_panel_ptv", 0x7f0a01c0);
        put("qq_aio_panel_ptv_gold_msg", 0x7f0a01c1);
        put("qq_aio_panel_hongbao", 0x7f0a01c7);
        put("qq_aio_panel_poke", 0x7f0a01d1);
        put("qq_aio_panel_hot_pic", 0x7f0a01d4);
        put("qq_aio_panel_hot_pic_gold_msg", 0x7f0a01d8);
        put("chat_item_troop_member_level", 0x7f0a0049);
        put("chat_item_troop_member_glamour_level", 0x7f0a004a);
        put("troop_assistant_feeds_title_small_video", 0x7f0a3118);
        put("pic_light_emoj", 0x7f0a00a2);
        put("timtips", 0x7f0a1974);
        put("qqsetting2_phone_unity_info", 0x7f0a2246);
        put("qqsetting2_newXmanLayout", 0x7f0a2247);
        put("qqsetting2_msg_notify", 0x7f0a2248);
        put("qqsetting2_msg_history", 0x7f0a2249);
        put("qqsetting2_msg_qqclean", 0x7f0a224a);
        put("qqsetting2_device_security", 0x7f0a224b);
        put("qqsetting2_permission_privacy", 0x7f0a224c);
        put("qqsetting2_assist", 0x7f0a224e);
        put("cu_open_card_guide_entry", 0x7f0a224f);
        put("about", 0x7f0a2252);
        put("qzone_feed_reddot", 0x7f0a1e39);
        put("xingqu_buluo_reddot", 0x7f0a1e43);

        // drawable
        put("skin_tips_dot", 0x7f021e20);
        // put("skin_tips_dot_small", 0x7f021e21);
        put("skin_tips_new", 0x7f021e23);
        put("shortvideo_redbag_outicon", 0x7f021c4c);

        // Qzone
        put("qzone_cover_avatar_facade", 0x7f0a27c9);
        put("qzone_cover_avatar_vip", 0x7f0a27ca);
        put("qzone_cover_avatar_qboss", 0x7f0a2ad3);
        put("feed_attach_view", 0x7f0a2eab);
        put("operation_like_container", 0x7f0a2f5a);
        put("operation_like_container2", 0x7f0a2f64);
        put("feed_praise_avatars_view", 0x7f0a2f89);
        put("feed_guide_comment_bar", 0x7f0a2f44);
        put("feed_canvas_comment_area_stub", 0x7f0a2f43);
        put("shuoshuo_ad_upload_quality", 0x7f0a2cf8);
        put("quality_hd_ad", 0x7f0a2e05);
        put("quality_ad", 0x7f0a2e24);
        put("quality_normal_ad", 0x7f0a2e00);
        put("quality_original_ad", 0x7f0a2e09);
        put("qzone_feed_commwidget_container", 0x7f0a2ab3);
        put("qzone_feed_commwidget_image", 0x7f0a2ab4);
        put("qzone_feed_commwidget_text", 0x7f0a2ab5);
        put("qzone_feed_commwidget_count", 0x7f0a2ab6);
        put("qzone_feed_commwidget_hide_btn", 0x7f0a2ab7);
        put("qzone_feed_commwidget_stub", 0x7f0a280e);
        put("qz_feed_head_container", 0x7f0a2adc);
    }

    private static void init800_828() {
        put("unchecked_msg_num", 0x7f0a0c34); // c6 .activity.MainFragment a(View view) _num
        put("adviewlayout", 0x7f0a0f1f); // c6 .activity.recent.BannerManager e(View view) relativelayout
        put("contactHeader", 0x7f0a07e3); // c6 .activity.Contact o() findViewById
        put("search_container", 0x7f0a1fa1); // c6 .activity.Contact o() findViewById2
        put("slidcards_container", 0x7f0a0836); // c6 .activity.contacts.base.CardController a(View view) FrameLayout
        put("newFriendEntry", 0x7f0a0838); // c6 .activity.Contacts o() f1a
        put("createTroopEntry", 0x7f0a0839); // c6 .activity.Contacts o() b
        put("emotionLayout", 0x7f0a0493); // c6 .activity.BaseChatPie EmotionKeywordHorizonListView
        put("btn_more_emoticon", 0x7f0a0a81); // c4 .emoticonview.EmoticonMainPanel a(QQAppInterface int Context int String BaseChatPie boolean) a
        put("unusual_contacts_footerview", 0x7f0a07c7); // c6 .activity.contacts.fragment.FriendFragment onClick UnCommonlyUsedContactsActivity
        put("leb_search_entry", 0x7f0a0bf4); // c6 .activity.Leba u() a LinearLayout
        put("qzone_feed_entry", 0x7f0a20d5); // c6 .activity.Laba OnClick
        put("near_people_entry", 0x7f0a20da); // c6 .activity.Laba OnClick
        put("xingqu_buluo_entry", 0x7f0a20df); // c6 .activity.Laba OnClick
        put("nearby_people_entry_sub_iv", 0x7f0a20de); // c6 .activity.Leba u() ImageView d
        put("buluo_entry_sub_iv", 0x7f0a20e2); // c6 .activity.Leba u() URLImageView a
        put("qr_code_icon", 0x7f0a2494); // .activity.QQSettingMe OnClick reportFla
        put("mydaily", 0x7f0a2495); // +1
        put("nickname_area", 0x7f0a2490); // OnClick 0X80072D6
        put("sig_layout", 0x7f0a248b); // OnClick signatureH5Url
        put("myvip", 0x7f0a249b); // OnClick enter vip
        put("mypocket", 0x7f0a249c); // OnClick My_wallet
        put("myDressup", 0x7f0a249e); // OnCick Trends_tab
        put("myfavorites", 0x7f0a249f); // OnClick QfavHelper
        put("myphotos", 0x7f0a24a0); // OnClick QZonePhotoListActivity
        put("myfiles", 0x7f0a24a1); // OnClick QfileFileAssistantActivity
        put("myvideos", 0x7f0a24a4); // OnClick QQStoryMemoriesActivity
        put("mycards", 0x7f0a24a3); // OnClick BusinessCardListActivity
        put("cuKingCard", 0x7f0a24a6); // OnClick cuKingCard url = null
        put("settings", 0x7f0a24a7); // +1
        put("nightmode", 0x7f0a24ab); // OnClick 夜间模式
        put("weather_layout", 0x7f0a24ae); // OnClick https://weather.mp.qq.com/?_wv=5127&asyncMode=1
        put("weather_area", 0x7f0a24b2); // OnClick getText
        put("chat_item_pendant_image", 0x7f0a0059); // c6 .activity.aio.BaseChatItemLayout setPendantImage URLImageView
        put("ivTitleBtnRightCall", 0x7f0a088a); // c6 .actiivty.BaseChatPie d() setContentDescription("通话");
        put("qq_aio_panel_ptt", 0x7f0a01b7); // c6 .activity.aio.panel.AIOPanelUtils  2 0 3
        put("qq_aio_panel_ptt_gold_msg", 0x7f0a01b9); // 2 3 3
        put("qq_aio_panel_image", 0x7f0a01bd); // 4 0 3
        put("qq_aio_panel_image_gold_msg", 0x7f0a01bf); // 4 3 3
        put("qq_aio_panel_camera", 0x7f0a01c0); // 5 0 3
        put("qq_aio_panel_ptv", 0x7f0a01c3); // 6 0 3
        put("qq_aio_panel_ptv_gold_msg", 0x7f0a01c4); // 6 2 3
        put("qq_aio_panel_hongbao", 0x7f0a01ca); // 10 0 3
        put("qq_aio_panel_poke", 0x7f0a01d4); // 23 0 3
        put("qq_aio_panel_hot_pic", 0x7f0a01d7); // 24 0 3
        put("qq_aio_panel_hot_pic_gold_msg", 0x7f0a01db); // 24 3 3
        put("chat_item_troop_member_level", 0x7f0a004b); // c6 .activity.aio.BaseBubbleBuilder Clk_label
        put("chat_item_troop_member_glamour_level", 0x7f0a004c); // level + 1
        put("troop_assistant_feeds_title_small_video", 0x7f0a33f7); // c6 TroopAssistantFeedsJsHandler
        put("pic_light_emoj", 0x7f0a00a4); // c6 .activity.aio.item.PicItemBuilder a(View view, MotionEvent motionEvent)
        put("timtips", 0x7f0a1b11); // c4 .filemanager.activity.fileassistant.QfileFileAssistantActivity doOnCreate ViewStub
        put("qqsetting2_phone_unity_info", 0x7f0a1c8d); // c6 .activity.QQSettingActivity OnClick PhoneUnityBindInfoActivity
        put("qqsetting2_newXmanLayout", 0x7f0a24e9); // OnClick http://ti.qq.com/xman/self.html
        put("qqsetting2_msg_notify", 0x7f0a1c90); // OnClick NotifyPushSettingActivity
        put("qqsetting2_msg_history", 0x7f0a1c91); // OnClick QQSettingMsgHistoryActivity
        put("qqsetting2_msg_qqclean", 0x7f0a24ea); // OnClick QQSettingCleanActivity
        put("qqsetting2_device_security", 0x7f0a24eb); // OnClick My_settab_safe
        put("qqsetting2_permission_privacy", 0x7f0a1c92); // OnClick PermisionPrivacyActivity
        put("qqsetting2_assist", 0x7f0a1c93); // OnClick AssistantSettingActivity
        put("cu_open_card_guide_entry", 0x7f0a24ed); // OnClick click cu_open_card_guide url: %s
        put("about", 0x7f0a1c97); // OnClick AboutActivity
        put("qzone_feed_reddot", 0x7f0a20d9); // c6 .activity.Leba lebaViewItem.a.uiResId == 0
        put("xingqu_buluo_reddot", 0x7f0a20e3); // c6 .activity.Leba lebaViewItem.a.uiResId == 0

        // drawable
        put("skin_tips_dot", 0x7f0220e3); // c3 .redtouch.RedTouch a(int i)
        // put("skin_tips_dot_small", 0x7f0220e4); // +1
        put("skin_tips_new", 0x7f0220e6); // +2
        put("shortvideo_redbag_outicon", 0x7f021f0b); // c3 .redtouch.RedTouch a(boolean z) parseRedBagTouch

        // Qzone
        put("qzone_cover_avatar_facade", 0x7f0a2a44);
        put("qzone_cover_avatar_vip", 0x7f0a2a45);
        put("qzone_cover_avatar_qboss", 0x7f0a2d77);
        put("feed_attach_view", 0x7f0a3188);
        put("operation_like_container", 0x7f0a3238);
        put("operation_like_container2", 0x7f0a3241);
        put("feed_praise_avatars_view", 0x7f0a3266);
        put("feed_guide_comment_bar", 0x7f0a3223);
        put("feed_canvas_comment_area_stub", 0x7f0a3221);
        put("shuoshuo_ad_upload_quality", 0x7f0a2fae);
        put("quality_hd_ad", 0x7f0a30ca);
        put("quality_ad", 0x7f0a30e9);
        put("quality_normal_ad", 0x7f0a30c5);
        put("quality_original_ad", 0x7f0a30ce);
        put("qzone_feed_commwidget_container", 0x7f0a2d57);
        put("qzone_feed_commwidget_image", 0x7f0a2d58);
        put("qzone_feed_commwidget_text", 0x7f0a2d59);
        put("qzone_feed_commwidget_count", 0x7f0a2d5a);
        put("qzone_feed_commwidget_hide_btn", 0x7f0a2d5b);
        put("qzone_feed_commwidget_stub", 0x7f0a2a8a);
        put("qz_feed_head_container", 0x7f0a2d80);
    }

    private static void init760_832() {
        put("unchecked_msg_num", 0x7f0a0c35); // c6 .activity.MainFragment a(View view) _num
        put("adviewlayout", 0x7f0a0f20); // c6 .activity.recent.BannerManager e(View view) relativelayout
        put("contactHeader", 0x7f0a07e4); // c6 .activity.Contact o() findViewById
        put("search_container", 0x7f0a1fa2); // c6 .activity.Contact o() findViewById2
        put("slidcards_container", 0x7f0a0837); // c6 .activity.contacts.base.CardController a(View view) FrameLayout
        put("newFriendEntry", 0x7f0a0839); // c6 .activity.Contacts o() f1a
        put("createTroopEntry", 0x7f0a083a); // c6 .activity.Contacts o() b
        put("emotionLayout", 0x7f0a0494); // c6 .activity.BaseChatPie EmotionKeywordHorizonListView
        put("btn_more_emoticon", 0x7f0a0a82); // c4 .emoticonview.EmoticonMainPanel a(QQAppInterface int Context int String BaseChatPie boolean) a ImageView
        put("unusual_contacts_footerview", 0x7f0a07c8); // c6 .activity.contacts.fragment.FriendFragment onClick UnCommonlyUsedContactsActivity
        put("leb_search_entry", 0x7f0a0bf5); // c6 .activity.Leba u() a LinearLayout
        put("qzone_feed_entry", 0x7f0a20d6); // c6 .activity.Laba OnClick
        put("near_people_entry", 0x7f0a20db); // c6 .activity.Laba OnClick
        put("xingqu_buluo_entry", 0x7f0a20e0); // c6 .activity.Laba OnClick
        put("nearby_people_entry_sub_iv", 0x7f0a20df); // c6 .activity.Leba u() ImageView d
        put("buluo_entry_sub_iv", 0x7f0a20e3); // c6 .activity.Leba u() URLImageView a
        put("qr_code_icon", 0x7f0a2495); // c6 .activity.QQSettingMe OnClick reportFla
        put("mydaily", 0x7f0a2496); // OnClick task_entry_config
        put("nickname_area", 0x7f0a2491); // OnClick 0X80072D6
        put("sig_layout", 0x7f0a248c); // OnClick signatureH5Url
        put("myvip", 0x7f0a249c); // OnClick enter vip
        put("mypocket", 0x7f0a249d); // OnClick My_wallet
        put("myDressup", 0x7f0a249f); // OnCick Trends_tab
        put("myfavorites", 0x7f0a24a0); // OnClick QfavHelper
        put("myphotos", 0x7f0a24a1); // OnClick QZonePhotoListActivity
        put("myfiles", 0x7f0a24a2); // OnClick QfileFileAssistantActivity
        put("myvideos", 0x7f0a24a5); // OnClick QQStoryMemoriesActivity
        put("mycards", 0x7f0a24a4); // OnClick BusinessCardListActivity
        put("cuKingCard", 0x7f0a24a7); // OnClick cuKingCard url = null
        put("settings", 0x7f0a24a8); // +1
        put("nightmode", 0x7f0a24ac); // OnClick 夜间模式
        put("weather_layout", 0x7f0a24af); // OnClick https://weather.mp.qq.com/?_wv=5127&asyncMode=1
        put("weather_area", 0x7f0a24b3); // OnClick getText
        put("chat_item_pendant_image", 0x7f0a0059); // c6 .activity.aio.BaseChatItemLayout setPendantImage URLImageView
        put("ivTitleBtnRightCall", 0x7f0a088b); // c6 .actiivty.BaseChatPie d() setContentDescription("通话");
        put("qq_aio_panel_ptt", 0x7f0a01b7); // c6 .activity.aio.panel.AIOPanelUtils  2 0 3
        put("qq_aio_panel_ptt_gold_msg", 0x7f0a01b9); // 2 3 3
        put("qq_aio_panel_image", 0x7f0a01bd); // 4 0 3
        put("qq_aio_panel_image_gold_msg", 0x7f0a01bf); // 4 3 3
        put("qq_aio_panel_camera", 0x7f0a01c0); // 5 0 3
        put("qq_aio_panel_ptv", 0x7f0a01c3); // 6 0 3
        put("qq_aio_panel_ptv_gold_msg", 0x7f0a01c4); // 6 2 3
        put("qq_aio_panel_hongbao", 0x7f0a01ca); // 10 0 3
        put("qq_aio_panel_poke", 0x7f0a01d4); // 23 0 3
        put("qq_aio_panel_hot_pic", 0x7f0a01d7); // 24 0 3
        put("qq_aio_panel_hot_pic_gold_msg", 0x7f0a01db); // 24 3 3
        put("chat_item_troop_member_level", 0x7f0a004b); // c6 .activity.aio.BaseBubbleBuilder Clk_label
        put("chat_item_troop_member_glamour_level", 0x7f0a004c); // level + 1
        put("troop_assistant_feeds_title_small_video", 0x7f0a33f7); // c6 TroopAssistantFeedsJsHandler
        put("pic_light_emoj", 0x7f0a00a4); // c6 .activity.aio.item.PicItemBuilder a(View view, MotionEvent motionEvent)
        put("timtips", 0x7f0a1b12); // c4 .filemanager.activity.fileassistant.QfileFileAssistantActivity doOnCreate ViewStub
        put("qqsetting2_phone_unity_info", 0x7f0a1c8e); // c6 .activity.QQSettingActivity OnClick PhoneUnityBindInfoActivity
        put("qqsetting2_newXmanLayout", 0x7f0a24ea); // OnClick http://ti.qq.com/xman/self.html
        put("qqsetting2_msg_notify", 0x7f0a1c91); // OnClick NotifyPushSettingActivity
        put("qqsetting2_msg_history", 0x7f0a1c92); // OnClick QQSettingMsgHistoryActivity
        put("qqsetting2_msg_qqclean", 0x7f0a24eb); // OnClick QQSettingCleanActivity
        put("qqsetting2_device_security", 0x7f0a24ec); // OnClick My_settab_safe
        put("qqsetting2_permission_privacy", 0x7f0a1c93); // OnClick PermisionPrivacyActivity
        put("qqsetting2_assist", 0x7f0a1c94); // OnClick AssistantSettingActivity
        put("cu_open_card_guide_entry", 0x7f0a24ee); // OnClick click cu_open_card_guide url: %s
        put("about", 0x7f0a1c98); // OnClick AboutActivity
        put("qzone_feed_reddot", 0x7f0a20da); // c6 .activity.Leba lebaViewItem.a.uiResId == 0
        put("xingqu_buluo_reddot", 0x7f0a20e4); // c6 .activity.Leba lebaViewItem.a.uiResId == 0

        // drawable
        put("skin_tips_dot", 0x7f0220e3); // c3 .redtouch.RedTouch a(int i)
        // put("skin_tips_dot_small", 0x7f0220e4); // +1
        put("skin_tips_new", 0x7f0220e6); // +2
        put("shortvideo_redbag_outicon", 0x7f021f0b); // c3 .redtouch.RedTouch a(boolean z) parseRedBagTouch

        // Qzone
        put("qzone_cover_avatar_facade", 0x7f0a2a44); // QZoneFeedsHeader QzoneFacadeDecorator
        put("qzone_cover_avatar_vip", 0x7f0a2a45); // QZoneFeedsHeader QzoneVipDecorator
        put("qzone_cover_avatar_qboss", 0x7f0a2d77); // QZoneFeedsHeader QzoneQbossDecorator
        put("feed_attach_view", 0x7f0a3188); // @id/feed_attach_view
        put("operation_like_container", 0x7f0a3238); // 2188
        put("operation_like_container2", 0x7f0a3241); // 2669
        put("feed_praise_avatars_view", 0x7f0a3266); // 2192
        put("feed_guide_comment_bar", 0x7f0a3223); // 2413
        put("feed_canvas_comment_area_stub", 0x7f0a3221); // 2787
        put("shuoshuo_ad_upload_quality", 0x7f0a2fae); // restoreShuoshuoPicShow picUrl: AsyncImageView
        put("quality_normal_ad", 0x7f0a30c5); // QZoneUploadQualityActivity AsyncImageView c
        put("quality_original_ad", 0x7f0a30ce); // QZoneUploadQualityActivity AsyncImageView a
        put("quality_hd_ad", 0x7f0a30ca); // QZoneUploadQualityActivity AsyncImageView b
        put("quality_ad", 0x7f0a30e9); // QZoneUploadPhotoActivity AsyncImageView c
        put("qzone_feed_commwidget_container", 0x7f0a2d57); // QZoneCommWidget i() View b
        put("qzone_feed_commwidget_image", 0x7f0a2d58); // AsyncImageView a
        put("qzone_feed_commwidget_text", 0x7f0a2d59); // TextView b
        put("qzone_feed_commwidget_count", 0x7f0a2d5a); // TextView a
        put("qzone_feed_commwidget_hide_btn", 0x7f0a2d5b); // AsyncImageView b
        put("qzone_feed_commwidget_stub", 0x7f0a2a8a); // QZoneFriendFeedFragment new QZoneCommWidget( View c
        put("qz_feed_head_container", 0x7f0a2d80); // HotBannerManager i() LinearLayout
    }

    private static void init763_836() {
        put("unchecked_msg_num", 0x7f0a0c4b); // c6 .activity.MainFragment a(View view) _num
        put("adviewlayout", 0x7f0a0f3b); // c6 .activity.recent.BannerManager e(View view) relativelayout
        put("contactHeader", 0x7f0a07fb); // c6 .activity.Contacts o() findViewById
        put("search_container", 0x7f0a1fed); // c6 .activity.Contacts o() findViewById2
        put("slidcards_container", 0x7f0a084e); // c6 .activity.contacts.base.CardController a(View view) FrameLayout
        put("newFriendEntry", 0x7f0a0850); // c6 .activity.Contacts o() f1a
        put("createTroopEntry", 0x7f0a0851); // c6 .activity.Contacts o() b
        put("emotionLayout", 0x7f0a04a6); // c6 .activity.BaseChatPie EmotionKeywordHorizonListView
        put("btn_more_emoticon", 0x7f0a0a9a); // c4 .emoticonview.EmoticonMainPanel a(QQAppInterface int Context int String BaseChatPie boolean) a ImageView
        put("unusual_contacts_footerview", 0x7f0a07df); // c6 .activity.contacts.fragment.FriendFragment OnClick UnCommonlyUsedContactsActivity
        put("leb_search_entry", 0x7f0a0c0b); // c6 .activity.Leba u() a LinearLayout
        put("qzone_feed_entry", 0x7f0a2148); // c6 .activity.Laba OnClick
        put("near_people_entry", 0x7f0a214d); // c6 .activity.Laba OnClick
        put("xingqu_buluo_entry", 0x7f0a2152); // c6 .activity.Laba OnClick
        put("nearby_people_entry_sub_iv", 0x7f0a2151); // c6 .activity.Leba u() ImageView d
        put("buluo_entry_sub_iv", 0x7f0a2155); // c6 .activity.Leba u() URLImageView a
        put("qr_code_icon", 0x7f0a2536); // c6 .activity.QQSettingMe OnClick reportFla
        put("mydaily", 0x7f0a2537); // OnClick task_entry_config
        put("nickname_area", 0x7f0a2532); // OnClick 0X80072D6
        put("sig_layout", 0x7f0a252d); // OnClick signatureH5Url
        put("myvip", 0x7f0a253d); // OnClick enter vip
        put("mypocket", 0x7f0a253e); // OnClick My_wallet
        put("myDressup", 0x7f0a2540); // OnCick Trends_tab
        put("myfavorites", 0x7f0a2541); // OnClick QfavHelper
        put("myphotos", 0x7f0a2542); // OnClick QZonePhotoListActivity
        put("myfiles", 0x7f0a2543); // OnClick QfileFileAssistantActivity
        put("myvideos", 0x7f0a2546); // OnClick QQStoryMemoriesActivity
        put("mycards", 0x7f0a2545); // OnClick BusinessCardListActivity
        put("cuKingCard", 0x7f0a2548); // OnClick cuKingCard url = null
        put("settings", 0x7f0a2549); // OnClick QQSettingSettingActivity
        put("nightmode", 0x7f0a254d); // OnClick 夜间模式
        put("weather_layout", 0x7f0a2550); // OnClick https://weather.mp.qq.com/?_wv=5127&asyncMode=1
        put("weather_area", 0x7f0a2554); // OnClick getText()).append("，天气：");
        put("chat_item_pendant_image", 0x7f0a0059); // c6 .activity.aio.BaseChatItemLayout setPendantImage URLImageView
        put("ivTitleBtnRightCall", 0x7f0a08a3); // c6 .actiivty.BaseChatPie d() setContentDescription("通话");
        put("qq_aio_panel_ptt", 0x7f0a01b7); // c6 .activity.aio.panel.AIOPanelUtils  a 3
        put("qq_aio_panel_ptt_gold_msg", 0x7f0a01b9); // c
        put("qq_aio_panel_image", 0x7f0a01bd); // g
        put("qq_aio_panel_image_gold_msg", 0x7f0a01bf); // i
        put("qq_aio_panel_camera", 0x7f0a01c0); // j
        put("qq_aio_panel_ptv", 0x7f0a01c3); // m
        put("qq_aio_panel_ptv_gold_msg", 0x7f0a01c4); // n
        put("qq_aio_panel_hongbao", 0x7f0a01ca); // s
        put("qq_aio_panel_poke", 0x7f0a01d4); // z
        put("qq_aio_panel_hot_pic", 0x7f0a01d7); // B
        put("qq_aio_panel_hot_pic_gold_msg", 0x7f0a01db); // F
        put("chat_item_troop_member_level", 0x7f0a004b); // c6 .activity.aio.BaseBubbleBuilder Clk_label
        put("chat_item_troop_member_glamour_level", 0x7f0a004c); // level + 1
        put("troop_assistant_feeds_title_small_video", 0x7f0a34af); // c6 TroopAssistantFeedsJsHandler
        put("pic_light_emoj", 0x7f0a00a4); // c6 .activity.aio.item.PicItemBuilder a(View view, MotionEvent motionEvent)
        put("timtips", 0x7f0a1b54); // c4 .filemanager.activity.fileassistant.QfileFileAssistantActivity doOnCreate ViewStub
        put("qqsetting2_phone_unity_info", 0x7f0a1ccf); // c6 .activity.QQSettingSettingActivity OnClick PhoneUnityBindInfoActivity
        put("qqsetting2_newXmanLayout", 0x7f0a258b); // OnClick http://ti.qq.com/xman/self.html
        put("qqsetting2_msg_notify", 0x7f0a1cd2); // OnClick NotifyPushSettingActivity
        put("qqsetting2_msg_history", 0x7f0a1cd3); // OnClick QQSettingMsgHistoryActivity
        put("qqsetting2_msg_qqclean", 0x7f0a258c); // OnClick QQSettingCleanActivity
        put("qqsetting2_device_security", 0x7f0a258d); // OnClick My_settab_safe
        put("qqsetting2_permission_privacy", 0x7f0a1cd4); // OnClick PermisionPrivacyActivity
        put("qqsetting2_assist", 0x7f0a1cd5); // OnClick AssistantSettingActivity
        put("cu_open_card_guide_entry", 0x7f0a258f); // OnClick click cu_open_card_guide url: %s
        put("about", 0x7f0a1cd9); // OnClick AboutActivity
        put("qzone_feed_reddot", 0x7f0a214c); // c6 .activity.Leba lebaViewItem.a.uiResId == 0
        put("xingqu_buluo_reddot", 0x7f0a2156); // c6 .activity.Leba lebaViewItem.a.uiResId == 0

        // drawable
        put("skin_tips_dot", 0x7f02215d); // c3 .redtouch.RedTouch a(int i)
        // put("skin_tips_dot_small", 0x7f02215e); // +1
        put("skin_tips_new", 0x7f02215f); // +2
        put("shortvideo_redbag_outicon", 0x7f021f85); // c3 .redtouch.RedTouch a(boolean z) parseRedBagTouch

        // Qzone
        put("qzone_cover_avatar_facade", 0x7f0a2af7); // QZoneFeedsHeader QzoneFacadeDecorator
        put("qzone_cover_avatar_vip", 0x7f0a2af8); // QZoneFeedsHeader QzoneVipDecorator
        put("qzone_cover_avatar_qboss", 0x7f0a2e2a); // QZoneFeedsHeader QzoneQbossDecorator
        put("feed_attach_view", 0x7f0a323a); // @id/feed_attach_view
        put("operation_like_container", 0x7f0a32ea); // 2188
        put("operation_like_container2", 0x7f0a32f3); // 2669
        put("feed_praise_avatars_view", 0x7f0a3318); // 2192
        put("feed_guide_comment_bar", 0x7f0a32d5); // 2413
        put("feed_canvas_comment_area_stub", 0x7f0a32d3); // 2787
        put("shuoshuo_ad_upload_quality", 0x7f0a3061); // restoreShuoshuoPicShow picUrl: AsyncImageView
        put("quality_normal_ad", 0x7f0a3178); // QZoneUploadQualityActivity AsyncImageView c
        put("quality_original_ad", 0x7f0a3181); // QZoneUploadQualityActivity AsyncImageView a
        put("quality_hd_ad", 0x7f0a317d); // QZoneUploadQualityActivity AsyncImageView b
        put("quality_ad", 0x7f0a319c); // QZoneUploadPhotoActivity AsyncImageView c
        put("qzone_feed_commwidget_container", 0x7f0a2e0a); // QZoneCommWidget i() View b
        put("qzone_feed_commwidget_image", 0x7f0a2e0b); // AsyncImageView a
        put("qzone_feed_commwidget_text", 0x7f0a2e0c); // TextView b
        put("qzone_feed_commwidget_count", 0x7f0a2e0d); // TextView a
        put("qzone_feed_commwidget_hide_btn", 0x7f0a2e0e); // AsyncImageView b
        put("qzone_feed_commwidget_stub", 0x7f0a2b3d); // QZoneFriendFeedFragment new QZoneCommWidget( View c
        put("qz_feed_head_container", 0x7f0a2e33); // HotBannerManager i() LinearLayout
    }

    private static void init763_850() {
        put("unchecked_msg_num", 0x7f0a0c4b); // c6 .activity.MainFragment a(View view) _num
        put("adviewlayout", 0x7f0a0f3b); // c6 .activity.recent.BannerManager e(View view) relativelayout
        put("contactHeader", 0x7f0a07fb); // c6 .activity.Contacts o() findViewById
        put("search_container", 0x7f0a1fef); // c6 .activity.Contacts o() findViewById2
        put("slidcards_container", 0x7f0a084e); // c6 .activity.contacts.base.CardController a(View view) FrameLayout
        put("newFriendEntry", 0x7f0a0850); // c6 .activity.Contacts o() f1a
        put("createTroopEntry", 0x7f0a0851); // c6 .activity.Contacts o() b
        put("emotionLayout", 0x7f0a04a6); // c6 .activity.BaseChatPie EmotionKeywordHorizonListView
        put("btn_more_emoticon", 0x7f0a0a9a); // c4 .emoticonview.EmoticonMainPanel a(QQAppInterface int Context int String BaseChatPie boolean) a ImageView
        put("unusual_contacts_footerview", 0x7f0a07df); // c6 .activity.contacts.fragment.FriendFragment OnClick UnCommonlyUsedContactsActivity
        put("leb_search_entry", 0x7f0a0c0b); // c6 .activity.Leba u() a LinearLayout
        put("qzone_feed_entry", 0x7f0a214a); // c6 .activity.Laba OnClick
        put("near_people_entry", 0x7f0a214f); // c6 .activity.Laba OnClick
        put("xingqu_buluo_entry", 0x7f0a2154); // c6 .activity.Laba OnClick
        put("nearby_people_entry_sub_iv", 0x7f0a2153); // c6 .activity.Leba u() ImageView d
        put("buluo_entry_sub_iv", 0x7f0a2157); // c6 .activity.Leba u() URLImageView a
        put("qr_code_icon", 0x7f0a2538); // c6 .activity.QQSettingMe OnClick reportFla
        put("mydaily", 0x7f0a2539); // OnClick task_entry_config
        put("nickname_area", 0x7f0a2534); // OnClick 0X80072D6
        put("sig_layout", 0x7f0a252f); // OnClick signatureH5Url
        put("myvip", 0x7f0a253f); // OnClick enter vip
        put("mypocket", 0x7f0a2540); // OnClick My_wallet
        put("myDressup", 0x7f0a2542); // OnCick Trends_tab
        put("myfavorites", 0x7f0a2543); // OnClick QfavHelper
        put("myphotos", 0x7f0a2544); // OnClick QZonePhotoListActivity
        put("myfiles", 0x7f0a2545); // OnClick QfileFileAssistantActivity
        put("myvideos", 0x7f0a2548); // OnClick QQStoryMemoriesActivity
        put("mycards", 0x7f0a2547); // OnClick BusinessCardListActivity
        put("cuKingCard", 0x7f0a254a); // OnClick cuKingCard url = null
        put("settings", 0x7f0a254b); // OnClick QQSettingSettingActivity
        put("nightmode", 0x7f0a254f); // OnClick 夜间模式
        put("weather_layout", 0x7f0a2552); // OnClick https://weather.mp.qq.com/?_wv=5127&asyncMode=1
        put("weather_area", 0x7f0a2556); // OnClick getText()).append("，天气：");
        put("chat_item_pendant_image", 0x7f0a0059); // c6 .activity.aio.BaseChatItemLayout setPendantImage URLImageView
        put("ivTitleBtnRightCall", 0x7f0a08a3); // c6 .actiivty.BaseChatPie d() setContentDescription("通话");
        put("qq_aio_panel_ptt", 0x7f0a01b7); // c6 .activity.aio.panel.AIOPanelUtiles  a 3
        put("qq_aio_panel_ptt_gold_msg", 0x7f0a01b9); // c
        put("qq_aio_panel_image", 0x7f0a01bd); // g
        put("qq_aio_panel_image_gold_msg", 0x7f0a01bf); // i
        put("qq_aio_panel_camera", 0x7f0a01c0); // j
        put("qq_aio_panel_ptv", 0x7f0a01c3); // m
        put("qq_aio_panel_ptv_gold_msg", 0x7f0a01c4); // n
        put("qq_aio_panel_hongbao", 0x7f0a01ca); // s
        put("qq_aio_panel_poke", 0x7f0a01d4); // z
        put("qq_aio_panel_hot_pic", 0x7f0a01d7); // B
        put("qq_aio_panel_hot_pic_gold_msg", 0x7f0a01db); // F
        put("chat_item_troop_member_level", 0x7f0a004b); // c6 .activity.aio.BaseBubbleBuilder Clk_label
        put("chat_item_troop_member_glamour_level", 0x7f0a004c); // level + 1
        put("troop_assistant_feeds_title_small_video", 0x7f0a34b1); // c6 TroopAssistantFeedsJsHandler
        put("pic_light_emoj", 0x7f0a00a4); // c6 .activity.aio.item.PicItemBuilder a(View view, MotionEvent motionEvent)
        put("timtips", 0x7f0a1b55); // c4 .filemanager.activity.fileassistant.QfileFileAssistantActivity doOnCreate ViewStub
        put("qqsetting2_phone_unity_info", 0x7f0a1cd0); // c6 .activity.QQSettingSettingActivity OnClick PhoneUnityBindInfoActivity
        put("qqsetting2_newXmanLayout", 0x7f0a258d); // OnClick http://ti.qq.com/xman/self.html
        put("qqsetting2_msg_notify", 0x7f0a1cd3); // OnClick NotifyPushSettingActivity
        put("qqsetting2_msg_history", 0x7f0a1cd4); // OnClick QQSettingMsgHistoryActivity
        put("qqsetting2_msg_qqclean", 0x7f0a258e); // OnClick QQSettingCleanActivity
        put("qqsetting2_device_security", 0x7f0a258f); // OnClick My_settab_safe
        put("qqsetting2_permission_privacy", 0x7f0a1cd5); // OnClick PermisionPrivacyActivity
        put("qqsetting2_assist", 0x7f0a1cd6); // OnClick AssistantSettingActivity
        put("cu_open_card_guide_entry", 0x7f0a2591); // OnClick click cu_open_card_guide url: %s
        put("about", 0x7f0a1cda); // OnClick AboutActivity
        put("qzone_feed_reddot", 0x7f0a214e); // c6 .activity.Leba lebaViewItem.a.uiResId == 0
        put("xingqu_buluo_reddot", 0x7f0a2158); // c6 .activity.Leba lebaViewItem.a.uiResId == 0

        // drawable
        put("skin_tips_dot", 0x7f022163); // c3 .redtouch.RedTouch a(int i)
        // put("skin_tips_dot_small", 0x7f022164); // +1
        put("skin_tips_new", 0x7f022165); // +2
        put("shortvideo_redbag_outicon", 0x7f021f8b); // c3 .redtouch.RedTouch a(boolean z) parseRedBagTouch

        // Qzone
        put("qzone_cover_avatar_facade", 0x7f0a2af9); // QZoneFeedsHeader QzoneFacadeDecorator
        put("qzone_cover_avatar_vip", 0x7f0a2afa); // QZoneFeedsHeader QzoneVipDecorator
        put("qzone_cover_avatar_qboss", 0x7f0a2e2c); // QZoneFeedsHeader QzoneQbossDecorator
        put("feed_attach_view", 0x7f0a323c); // @id/feed_attach_view
        put("operation_like_container", 0x7f0a32ec); // 2188
        put("operation_like_container2", 0x7f0a32f5); // 2669
        put("feed_praise_avatars_view", 0x7f0a331a); // 2192
        put("feed_guide_comment_bar", 0x7f0a32d7); // 2413
        put("feed_canvas_comment_area_stub", 0x7f0a32d5); // 2787
        put("shuoshuo_ad_upload_quality", 0x7f0a3063); // restoreShuoshuoPicShow picUrl: AsyncImageView
        put("quality_normal_ad", 0x7f0a317a); // QZoneUploadQualityActivity AsyncImageView c
        put("quality_original_ad", 0x7f0a3183); // QZoneUploadQualityActivity AsyncImageView a
        put("quality_hd_ad", 0x7f0a317f); // QZoneUploadQualityActivity AsyncImageView b
        put("quality_ad", 0x7f0a319e); // QZoneUploadPhotoActivity AsyncImageView c
        put("qzone_feed_commwidget_container", 0x7f0a2e10); // QZoneCommWidget i() View b
        put("qzone_feed_commwidget_image", 0x7f0a2e0d); // AsyncImageView a
        put("qzone_feed_commwidget_text", 0x7f0a2e0e); // TextView b
        put("qzone_feed_commwidget_count", 0x7f0a2e0f); // TextView a
        put("qzone_feed_commwidget_hide_btn", 0x7f0a2e10); // AsyncImageView b
        put("qzone_feed_commwidget_stub", 0x7f0a2b3f); // QZoneFriendFeedFragment new QZoneCommWidget( View c
        put("qz_feed_head_container", 0x7f0a2e35); // HotBannerManager i() LinearLayout
    }

    private static void init765_864() {
        put("unchecked_msg_num", 0x7f0a0c66); // c6 .activity.MainFragment a(View view) _num
        put("adviewlayout", 0x7f0a0f40); // c6 .activity.recent.BannerManager e(View view) relativelayout
        put("contactHeader", 0x7f0a0813); // c6 .activity.Contacts o() findViewById
        put("search_container", 0x7f0a1ff8); // c6 .activity.Contacts o() findViewById2
        put("slidcards_container", 0x7f0a0867); // c6 .activity.contacts.base.CardController a(View view) FrameLayout
        put("newFriendEntry", 0x7f0a0869); // c6 .activity.Contacts o() a
        put("createTroopEntry", 0x7f0a086a); // c6 .activity.Contacts o() b
        put("emotionLayout", 0x7f0a04ab); // c6 .activity.BaseChatPie EmotionKeywordHorizonListView
        put("btn_more_emoticon", 0x7f0a0ab2); // c4 .emoticonview.EmoticonMainPanel a(QQAppInterface int Context int String BaseChatPie boolean) ImageView a
        put("unusual_contacts_footerview", 0x7f0a07f7); // c6 .activity.contacts.fragment.FriendFragment OnClick UnCommonlyUsedContactsActivity
        put("leb_search_entry", 0x7f0a0c26); // c6 .activity.Leba u() a LinearLayout
        put("qzone_feed_entry", 0x7f0a2153); // c6 .activity.Laba OnClick
        put("near_people_entry", 0x7f0a2158); // c6 .activity.Laba OnClick
        put("xingqu_buluo_entry", 0x7f0a215d); // c6 .activity.Laba OnClick
        put("nearby_people_entry_sub_iv", 0x7f0a215c); // c6 .activity.Leba u() ImageView d
        put("buluo_entry_sub_iv", 0x7f0a2160); // c6 .activity.Leba u() URLImageView a
        put("qr_code_icon", 0x7f0a2536); // c6 .activity.QQSettingMe OnClick reportFla
        put("mydaily", 0x7f0a2537); // OnClick task_entry_config
        put("nickname_area", 0x7f0a2532); // OnClick 0X80072D6
        put("sig_layout", 0x7f0a252d); // OnClick signatureH5Url
        put("myvip", 0x7f0a253d); // OnClick enter vip
        put("mypocket", 0x7f0a253e); // OnClick My_wallet
        put("myDressup", 0x7f0a2540); // OnCick Trends_tab
        put("myfavorites", 0x7f0a2541); // OnClick QfavHelper
        put("myphotos", 0x7f0a2542); // OnClick QZonePhotoListActivity
        put("myfiles", 0x7f0a2543); // OnClick QfileFileAssistantActivity
        put("myvideos", 0x7f0a2546); // OnClick QQStoryMemoriesActivity
        put("mycards", 0x7f0a2545); // OnClick BusinessCardListActivity
        put("cuKingCard", 0x7f0a2548); // OnClick cuKingCard url = null
        put("settings", 0x7f0a2549); // OnClick QQSettingSettingActivity
        put("nightmode", 0x7f0a254d); // OnClick 夜间模式
        put("weather_layout", 0x7f0a2550); // OnClick https://weather.mp.qq.com/?_wv=5127&asyncMode=1
        put("weather_area", 0x7f0a2554); // OnClick getText()).append("，天气：");
        put("chat_item_pendant_image", 0x7f0a0059); // c6 .activity.aio.BaseChatItemLayout setPendantImage URLImageView
        put("ivTitleBtnRightCall", 0x7f0a08bc); // c6 .actiivty.BaseChatPie d() setContentDescription("通话"); d
        put("qq_aio_panel_ptt", 0x7f0a01b7); // c6 .activity.aio.panel.AIOPanelUtiles  a 3
        put("qq_aio_panel_ptt_gold_msg", 0x7f0a01b9); // c
        put("qq_aio_panel_image", 0x7f0a01bd); // g
        put("qq_aio_panel_image_gold_msg", 0x7f0a01bf); // i
        put("qq_aio_panel_camera", 0x7f0a01c0); // j
        put("qq_aio_panel_ptv", 0x7f0a01c3); // m
        put("qq_aio_panel_ptv_gold_msg", 0x7f0a01c4); // n
        put("qq_aio_panel_hongbao", 0x7f0a01ca); // s
        put("qq_aio_panel_poke", 0x7f0a01d4); // z
        put("qq_aio_panel_hot_pic", 0x7f0a01d7); // B
        put("qq_aio_panel_hot_pic_gold_msg", 0x7f0a01db); // F
        put("chat_item_troop_member_level", 0x7f0a004b); // c6 .activity.aio.BaseBubbleBuilder Clk_label
        put("chat_item_troop_member_glamour_level", 0x7f0a004c); // level + 1
        put("troop_assistant_feeds_title_small_video", 0x7f0a34e5); // c6 TroopAssistantFeedsJsHandler
        put("pic_light_emoj", 0x7f0a00a4); // c6 .activity.aio.item.PicItemBuilder a(View view, MotionEvent motionEvent)
        put("timtips", 0x7f0a1b58); // c4 .filemanager.activity.fileassistant.QfileFileAssistantActivity doOnCreate ViewStub
        put("qqsetting2_phone_unity_info", 0x7f0a1cd1); // c6 .activity.QQSettingSettingActivity OnClick PhoneUnityBindInfoActivity
        put("qqsetting2_newXmanLayout", 0x7f0a258b); // OnClick http://ti.qq.com/xman/self.html
        put("qqsetting2_msg_notify", 0x7f0a1cd4); // OnClick NotifyPushSettingActivity
        put("qqsetting2_msg_history", 0x7f0a1cd5); // OnClick QQSettingMsgHistoryActivity
        put("qqsetting2_msg_qqclean", 0x7f0a258c); // OnClick QQSettingCleanActivity
        put("qqsetting2_device_security", 0x7f0a258d); // OnClick My_settab_safe
        put("qqsetting2_permission_privacy", 0x7f0a1cd6); // OnClick PermisionPrivacyActivity
        put("qqsetting2_assist", 0x7f0a1cd7); // OnClick AssistantSettingActivity
        put("cu_open_card_guide_entry", 0x7f0a258f); // OnClick click cu_open_card_guide url: %s
        put("about", 0x7f0a1cdb); // OnClick AboutActivity
        put("qzone_feed_reddot", 0x7f0a2157); // c6 .activity.Leba TextView b
        put("xingqu_buluo_reddot", 0x7f0a2161); // c6 .activity.Leba u() TextView d

        // drawable
        put("skin_tips_dot", 0x7f0221b3); // c3 .redtouch.RedTouch a(int i)
        // put("skin_tips_dot_small", 0x7f0221b4); // +1
        put("skin_tips_new", 0x7f0221b5); // +2
        put("shortvideo_redbag_outicon", 0x7f021fda); // c3 .redtouch.RedTouch a(boolean z) parseRedBagTouch

        // Qzone
        put("qzone_cover_avatar_facade", 0x7f0a2b04); // QZoneFeedsHeader QzoneFacadeDecorator
        put("qzone_cover_avatar_vip", 0x7f0a2b05); // QZoneFeedsHeader QzoneVipDecorator
        put("qzone_cover_avatar_qboss", 0x7f0a2e47); // QZoneFeedsHeader QzoneQbossDecorator
        put("feed_attach_view", 0x7f0a3261); // @id/feed_attach_view
        put("operation_like_container", 0x7f0a3313); // 2188
        put("operation_like_container2", 0x7f0a331c); // 2669
        put("feed_praise_avatars_view", 0x7f0a3341); // 2192
        put("feed_guide_comment_bar", 0x7f0a32fe); // 2413
        put("feed_canvas_comment_area_stub", 0x7f0a32fc); // 2787
        put("shuoshuo_ad_upload_quality", 0x7f0a3081); // restoreShuoshuoPicShow picUrl: AsyncImageView
        put("quality_normal_ad", 0x7f0a3199); // QZoneUploadQualityActivity AsyncImageView c
        put("quality_original_ad", 0x7f0a31a2); // QZoneUploadQualityActivity AsyncImageView a
        put("quality_hd_ad", 0x7f0a319e); // QZoneUploadQualityActivity AsyncImageView b
        put("quality_ad", 0x7f0a31bd); // QZoneUploadPhotoActivity AsyncImageView c
        put("qzone_feed_commwidget_container", 0x7f0a2e27); // QZoneCommWidget i() View b
        put("qzone_feed_commwidget_image", 0x7f0a2e28); // AsyncImageView a
        put("qzone_feed_commwidget_text", 0x7f0a2e29); // TextView b
        put("qzone_feed_commwidget_count", 0x7f0a2e2a); // TextView a
        put("qzone_feed_commwidget_hide_btn", 0x7f0a2e2b); // AsyncImageView b
        put("qzone_feed_commwidget_stub", 0x7f0a2b49); // QZoneFriendFeedFragment new QZoneCommWidget( View d
        put("qz_feed_head_container", 0x7f0a2e50); // HotBannerManager i() LinearLayout
    }

    private static void init768_872() {
        // drawable
        put("skin_tips_dot", 0x7f0221b5); // c3 .redtouch.RedTouch a(int i)
        // put("skin_tips_dot_small", 0x7f0221b6); // +1
        put("skin_tips_new", 0x7f0221b7); // +2
        put("shortvideo_redbag_outicon", 0x7f021fdc); // c3 .redtouch.RedTouch a(boolean z) parseRedBagTouch
    }

    private static void init770_884() {
        // drawable
        put("skin_tips_dot", 0x7f0221f3); // c3 .redtouch.RedTouch a(int i)
        put("skin_tips_new", 0x7f0221f5); // +2
        put("shortvideo_redbag_outicon", 0x7f02201d); // c3 .redtouch.RedTouch a(boolean z) parseRedBagTouch
    }

    private static void put(String key, Integer value) {
        id.put(key, value);
    }
}
