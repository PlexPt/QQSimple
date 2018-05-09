package me.zpp0196.qqsimple.hook.comm;

import java.util.HashMap;

import me.zpp0196.qqsimple.hook.util.Util;

/**
 * Created by zpp0196 on 2018/4/30 0030.
 */

public class Ids {

    private static HashMap<String, Integer> id;

    public static void init(){
        if(id == null) {
            id = new HashMap<>();
        }else {
            id.clear();
        }
        String version = Util.getQQVersion();
        switch (version){
            case "7.5.8":
                init758();
                break;
            case "8.0.0":
                init800();
                break;
            case "7.6.0":
                init760();
                break;
        }
    }

    public static Integer getId(String name){
        return id.get(name);
    }

    @SuppressWarnings("all")
    private static void init758(){
        put("unchecked_msg_num", 0x7f0a0b4f);
        put("adviewlayout", 0x7f0a0e37);
        put("newFriendEntry", 0x7f0a07e1);
        put("createTroopEntry", 0x7f0a07e2);
        put("emotionLayout", 0x7f0a045b);
        put("btn_more_emoticon", 0x7f0a09a6);
        put("unusual_contacts_footerview", 0x7f0a0772);
        put("leb_search_entry", 0x7f0a0b0f);
        put("qzone_feed_entry", 0x7f0a1e35);
        put("near_people_entry", 0x7f0a1e3a);
        put("xingqu_buluo_entry", 0x7f0a1e3f);
        put("qzone_feed_entry_sub_iv", 0x7f0a1e38);
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

    @SuppressWarnings("all")
    private static void init800(){
        put("unchecked_msg_num", 0x7f0a0c34); // c6 .activity.MainFragment a(View view) _num
        put("adviewlayout", 0x7f0a0f1f); // c6 .activity.recent.BannerManager e(View view) relativelayout
        put("newFriendEntry", 0x7f0a0838); // c6 .activity.Contacts o() f1a
        put("createTroopEntry", 0x7f0a0839); // c6 .activity.Contacts o() b
        put("emotionLayout", 0x7f0a0493); // c6 .activity.BaseChatPie EmotionKeywordHorizonListView
        put("btn_more_emoticon", 0x7f0a0a81); // c4 .emoticonview.EmoticonMainPanel a(QQAppInterface int Context int String BaseChatPie boolean) a
        put("unusual_contacts_footerview", 0x7f0a07c7); // c6 .activity.contacts.fragment.FriendFragment onClick UnCommonlyUsedContactsActivity
        put("leb_search_entry", 0x7f0a0bf4); // c6 .activity.Leba u() a LinearLayout
        put("qzone_feed_entry", 0x7f0a20d5); // c6 .activity.Laba OnClick
        put("near_people_entry", 0x7f0a20da); // c6 .activity.Laba OnClick
        put("xingqu_buluo_entry", 0x7f0a20df); // c6 .activity.Laba OnClick
        put("qzone_feed_entry_sub_iv", 0x7f0a20db); // c6 .activity.Leba u() ImageView
        put("nearby_people_entry_sub_iv", 0x7f0a20de); // c6 .activity.Leba u() ImageView OnClick visibility
        put("buluo_entry_sub_iv", 0x7f0a20e2); // c6 .activity.Leba u() a URLImageView
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
        put("myvideos", 0x7f0a24a2); // OnClick DingdongPluginManager
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

    @SuppressWarnings("all")
    private static void init760(){
        put("unchecked_msg_num", 0x7f0a0c35); // c6 .activity.MainFragment a(View view) _num
        put("adviewlayout", 0x7f0a0f20); // c6 .activity.recent.BannerManager e(View view) relativelayout
        put("newFriendEntry", 0x7f0a0839); // c6 .activity.Contacts o() f1a
        put("createTroopEntry", 0x7f0a083a); // c6 .activity.Contacts o() b
        put("emotionLayout", 0x7f0a0494); // c6 .activity.BaseChatPie EmotionKeywordHorizonListView
        put("btn_more_emoticon", 0x7f0a0a82); // c4 .emoticonview.EmoticonMainPanel a(QQAppInterface int Context int String BaseChatPie boolean) a ImageView
        put("unusual_contacts_footerview", 0x7f0a07c8); // c6 .activity.contacts.fragment.FriendFragment onClick UnCommonlyUsedContactsActivity
        put("leb_search_entry", 0x7f0a0bf5); // c6 .activity.Leba u() a LinearLayout
        put("qzone_feed_entry", 0x7f0a20d6); // c6 .activity.Laba OnClick
        put("near_people_entry", 0x7f0a20db); // c6 .activity.Laba OnClick
        put("xingqu_buluo_entry", 0x7f0a20e0); // c6 .activity.Laba OnClick
        put("qzone_feed_entry_sub_iv", 0x7f0a20dc); // c6 .activity.Leba u() ImageView
        put("nearby_people_entry_sub_iv", 0x7f0a20df); // c6 .activity.Leba u() ImageView OnClick visibility
        put("buluo_entry_sub_iv", 0x7f0a20e3); // c6 .activity.Leba u() a URLImageView
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
        put("myvideos", 0x7f0a24a3); // OnClick DingdongPluginManager
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

    private static void put(String key, Integer value){
        id.put(key, value);
    }
}
