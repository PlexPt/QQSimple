package me.zpp0196.qqsimple.hook.comm;

import me.zpp0196.qqsimple.hook.util.Util;

import static me.zpp0196.qqsimple.hook.util.Util.getQQVersion;

/**
 * Created by zpp0196 on 2018/4/27 0027.
 */

public class Classes {

    private static ClassLoader qqClassLoader;

    public static Class<?> R$id;
    public static Class<?> R$drawable;

    public static Class<?> AbstractChatItemBuilder$ViewHolder;
    public static Class<?> ActionSheet;
    public static Class<?> AIOImageProviderService;
    public static Class<?> ApolloManager$CheckApolloInfoResult;
    public static Class<?> BaseBubbleBuilder$ViewHolder;
    public static Class<?> BubbleManager;
    public static Class<?> BusinessInfoCheckUpdate$RedTypeInfo;
    public static Class<?> ChatMessage;
    public static Class<?> ContactsViewController;
    public static Class<?> Conversation;
    public static Class<?> ConversationNowController;
    public static Class<?> CoreService;
    public static Class<?> CoreService$KernelService;
    public static Class<?> CountDownProgressBar;
    public static Class<?> EmoticonManager;
    public static Class<?> GatherContactsTips;
    public static Class<?> GrayTipsItemBuilder;
    public static Class<?> HotChatFlashPicActivity;
    public static Class<?> ItemBuilderFactory;
    public static Class<?> Leba;
    public static Class<?> MainFragment;
    public static Class<?> MedalNewsItemBuilder;
    public static Class<?> MessageForDeliverGiftTips;
    public static Class<?> MessageRecord;
    public static Class<?> OnLongClickAndTouchListener;
    public static Class<?> PluginStatic;
    public static Class<?> PopupMenuDialog;
    public static Class<?> PopupMenuDialog$MenuItem;
    public static Class<?> PopupMenuDialog$OnClickActionListener;
    public static Class<?> QQMessageFacade;
    public static Class<?> QQSettingMe;
    public static Class<?> QzoneFeedItemBuilder;
    public static Class<?> ReadInJoyHelper;
    public static Class<?> RichStatItemBuilder;
    public static Class<?> SimpleSlidingIndicator;
    public static Class<?> SougouInputGrayTips;
    public static Class<?> TextItemBuilder;
    public static Class<?> TextPreviewActivity;
    public static Class<?> TroopEnterEffectController;
    public static Class<?> TroopGiftAnimationController;
    public static Class<?> VipSpecialCareGrayTips;

    public static void initClass(ClassLoader classLoader){
        qqClassLoader = classLoader;

        if(R$id == null) R$id = findClassInQQ("com.tencent.mobileqq.R$id");
        if(R$drawable == null) R$drawable = findClassInQQ("com.tencent.mobileqq.R$drawable");

        if(AbstractChatItemBuilder$ViewHolder == null) AbstractChatItemBuilder$ViewHolder = findClassInQQ("com.tencent.mobileqq.activity.aio.AbstractChatItemBuilder$ViewHolder");
        if(ActionSheet == null) ActionSheet = findClassInQQ("com.tencent.widget.ActionSheet");
        if(AIOImageProviderService == null) AIOImageProviderService = findClassInQQ("com.tencent.mobileqq.activity.aio.photo.AIOImageProviderService");
        if(ApolloManager$CheckApolloInfoResult == null) ApolloManager$CheckApolloInfoResult = findClassInQQ("com.tencent.mobileqq.apollo.ApolloManager$CheckApolloInfoResult");
        if(BaseBubbleBuilder$ViewHolder == null) BaseBubbleBuilder$ViewHolder = findClassInQQ("com.tencent.mobileqq.activity.aio.BaseBubbleBuilder$ViewHolder");
        if(BubbleManager == null) BubbleManager = findClassInQQ("com.tencent.mobileqq.bubble.BubbleManager");
        if(BusinessInfoCheckUpdate$RedTypeInfo == null) BusinessInfoCheckUpdate$RedTypeInfo = findClassInQQ("com.tencent.pb.getbusiinfo.BusinessInfoCheckUpdate$RedTypeInfo");
        if(ChatMessage == null) ChatMessage = findClassInQQ("com.tencent.mobileqq.data.ChatMessage");
        if(ContactsViewController == null) ContactsViewController = findClassInQQ("com.tencent.mobileqq.activity.contacts.base.ContactsViewController");
        if(Conversation == null) Conversation = findClassInQQ("com.tencent.mobileqq.activity.Conversation");
        if(ConversationNowController == null) ConversationNowController = findClassInQQ("com.tencent.mobileqq.now.enter.ConversationNowController");
        if(CoreService == null) CoreService = findClassInQQ("com.tencent.mobileqq.app.CoreService");
        if(CoreService$KernelService == null) CoreService$KernelService = findClassInQQ("com.tencent.mobileqq.app.CoreService$KernelService");
        if(CountDownProgressBar == null) CountDownProgressBar = findClassInQQ("com.tencent.widget.CountDownProgressBar");
        if(EmoticonManager == null) EmoticonManager = findClassInQQ("com.tencent.mobileqq.model.EmoticonManager");
        if(GatherContactsTips == null) GatherContactsTips = findClassInQQ("com.tencent.mobileqq.activity.aio.tips.GatherContactsTips");
        if(GrayTipsItemBuilder == null) GrayTipsItemBuilder = findClassInQQ("com.tencent.mobileqq.activity.aio.item.GrayTipsItemBuilder");
        if(HotChatFlashPicActivity == null) HotChatFlashPicActivity = findClassInQQ("com.tencent.mobileqq.dating.HotChatFlashPicActivity");
        if(ItemBuilderFactory == null) ItemBuilderFactory = findClassInQQ("com.tencent.mobileqq.activity.aio.item.ItemBuilderFactory");
        if(Leba == null) Leba = findClassInQQ("com.tencent.mobileqq.activity.Leba");
        if(MainFragment == null) MainFragment = findClassInQQ("com.tencent.mobileqq.activity.MainFragment");
        if(MedalNewsItemBuilder == null) MedalNewsItemBuilder = findClassInQQ("com.tencent.mobileqq.activity.aio.item.MedalNewsItemBuilder");
        if(MessageForDeliverGiftTips == null) MessageForDeliverGiftTips = findClassInQQ("com.tencent.mobileqq.data.MessageForDeliverGiftTips");
        if(MessageRecord == null) MessageRecord = findClassInQQ("com.tencent.mobileqq.data.MessageRecord");
        if(OnLongClickAndTouchListener == null) OnLongClickAndTouchListener = findClassInQQ("com.tencent.mobileqq.activity.aio.OnLongClickAndTouchListener");
        if(PluginStatic == null) PluginStatic = findClassInQQ("com.tencent.mobileqq.pluginsdk.PluginStatic");
        if(PopupMenuDialog == null) PopupMenuDialog = findClassInQQ("com.tencent.widget.PopupMenuDialog");
        if(PopupMenuDialog$MenuItem == null) PopupMenuDialog$MenuItem = findClassInQQ("com.tencent.widget.PopupMenuDialog$MenuItem");
        if(PopupMenuDialog$OnClickActionListener == null) PopupMenuDialog$OnClickActionListener = findClassInQQ("com.tencent.widget.PopupMenuDialog$OnClickActionListener");
        if(QQMessageFacade == null) QQMessageFacade = findClassInQQ("com.tencent.mobileqq.app.message.QQMessageFacade");
        if(QQSettingMe == null) QQSettingMe = findClassInQQ("com.tencent.mobileqq.activity.QQSettingMe");
        if(QzoneFeedItemBuilder == null) QzoneFeedItemBuilder = findClassInQQ("com.tencent.mobileqq.activity.aio.item.QzoneFeedItemBuilder");
        if(ReadInJoyHelper == null) ReadInJoyHelper = findClassInQQ("cooperation.readinjoy.ReadInJoyHelper");
        if(RichStatItemBuilder == null) RichStatItemBuilder = findClassInQQ("com.tencent.mobileqq.activity.aio.item.RichStatItemBuilder");
        if(SimpleSlidingIndicator == null) SimpleSlidingIndicator = findClassInQQ("com.tencent.mobileqq.activity.contacts.view.SimpleSlidingIndicator");
        if(SougouInputGrayTips == null) SougouInputGrayTips = findClassInQQ("com.tencent.mobileqq.activity.aio.tips.SougouInputGrayTips");
        if(TextItemBuilder == null) TextItemBuilder = findClassInQQ("com.tencent.mobileqq.activity.aio.item.TextItemBuilder");
        if(TextPreviewActivity == null) TextPreviewActivity = findClassInQQ("com.tencent.mobileqq.activity.TextPreviewActivity");
        if(TroopEnterEffectController == null) TroopEnterEffectController = findClassInQQ("com.tencent.mobileqq.troop.enterEffect.TroopEnterEffectController");
        if(TroopGiftAnimationController == null) TroopGiftAnimationController = findClassInQQ("com.tencent.mobileqq.troopgift.TroopGiftAnimationController");
        if(VipSpecialCareGrayTips == null) VipSpecialCareGrayTips = findClassInQQ("com.tencent.mobileqq.activity.aio.tips.VipSpecialCareGrayTips");
    }

    private static Class<?> findClassInQQ(String className) {
        if (qqClassLoader == null || className.equals("")) return null;
        try {
            return qqClassLoader.loadClass(className);
        } catch (Throwable e) {
            Util.log("Classes", String.format("%s Can't find the Class of name: %s!", getQQVersion(), className));
        }
        return null;
    }
}