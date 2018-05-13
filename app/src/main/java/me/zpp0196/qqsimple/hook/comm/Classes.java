package me.zpp0196.qqsimple.hook.comm;

import me.zpp0196.qqsimple.hook.util.Util;

import static me.zpp0196.qqsimple.Common.PACKAGE_NAME_QQ;
import static me.zpp0196.qqsimple.hook.util.Util.getQQVersion;

/**
 * Created by zpp0196 on 2018/4/27 0027.
 */

public class Classes {

    private static ClassLoader qqClassLoader;

    public static Class<?> R$drawable;
    public static Class<?> R$id;

    public static Class<?> AbstractChatItemBuilder$ViewHolder;
    public static Class<?> ActionSheet;
    public static Class<?> AioAnimationConfigHelper;
    public static Class<?> AIOImageProviderService;
    public static Class<?> ApolloManager$CheckApolloInfoResult;
    public static Class<?> BaseActivity;
    public static Class<?> BaseBubbleBuilder$ViewHolder;
    public static Class<?> BubbleManager;
    public static Class<?> BusinessInfoCheckUpdate$RedTypeInfo;
    public static Class<?> ChatActivityUtils;
    public static Class<?> ChatMessage;
    public static Class<?> Conversation;
    public static Class<?> ConversationNowController;
    public static Class<?> Contacts;
    public static Class<?> ContactUtils;
    public static Class<?> CoreService;
    public static Class<?> CoreService$KernelService;
    public static Class<?> CountDownProgressBar;
    public static Class<?> EmoticonManager;
    public static Class<?> FontManager;
    public static Class<?> FrameHelperActivity;
    public static Class<?> GatherContactsTips;
    public static Class<?> GrayTipsItemBuilder;
    public static Class<?> HotChatFlashPicActivity;
    public static Class<?> ItemBuilderFactory;
    public static Class<?> Leba;
    public static Class<?> LocalSearchBar;
    public static Class<?> MainFragment;
    public static Class<?> MedalNewsItemBuilder;
    public static Class<?> MessageForDeliverGiftTips;
    public static Class<?> MessageRecord;
    public static Class<?> MessageRecordFactory;
    public static Class<?> OnLongClickAndTouchListener;
    public static Class<?> PopupMenuDialog;
    public static Class<?> PopupMenuDialog$MenuItem;
    public static Class<?> PopupMenuDialog$OnClickActionListener;
    public static Class<?> QQAppInterface;
    public static Class<?> QQMessageFacade;
    public static Class<?> QQSettingMe;
    public static Class<?> QQSettingSettingActivity;
    public static Class<?> QzoneFeedItemBuilder;
    public static Class<?> QzonePluginProxyActivity;
    public static Class<?> RichStatItemBuilder;
    public static Class<?> SimpleSlidingIndicator;
    public static Class<?> SougouInputGrayTips;
    public static Class<?> TextItemBuilder;
    public static Class<?> TextPreviewActivity;
    public static Class<?> TListView;
    public static Class<?> TroopEnterEffectController;
    public static Class<?> TroopGiftAnimationController;
    public static Class<?> VipSpecialCareGrayTips;

    public static void initClass(ClassLoader classLoader){
        qqClassLoader = classLoader;

        if(R$drawable == null) R$drawable = findClassInQQ(".R$drawable");
        if(R$id == null) R$id = findClassInQQ(".R$id");

        if(AbstractChatItemBuilder$ViewHolder == null) AbstractChatItemBuilder$ViewHolder = findClassInQQ(".activity.aio.AbstractChatItemBuilder$ViewHolder");
        if(ActionSheet == null) ActionSheet = findClassInQQ("com.tencent.widget.ActionSheet");
        if(AioAnimationConfigHelper == null) AioAnimationConfigHelper = findClassInQQ(".activity.aio.anim.AioAnimationConfigHelper");
        if(AIOImageProviderService == null) AIOImageProviderService = findClassInQQ(".activity.aio.photo.AIOImageProviderService");
        if(ApolloManager$CheckApolloInfoResult == null) ApolloManager$CheckApolloInfoResult = findClassInQQ(".apollo.ApolloManager$CheckApolloInfoResult");
        if(BaseActivity == null) BaseActivity = findClassInQQ(".app.BaseActivity");
        if(BaseBubbleBuilder$ViewHolder == null) BaseBubbleBuilder$ViewHolder = findClassInQQ(".activity.aio.BaseBubbleBuilder$ViewHolder");
        if(BubbleManager == null) BubbleManager = findClassInQQ(".bubble.BubbleManager");
        if(BusinessInfoCheckUpdate$RedTypeInfo == null) BusinessInfoCheckUpdate$RedTypeInfo = findClassInQQ("com.tencent.pb.getbusiinfo.BusinessInfoCheckUpdate$RedTypeInfo");
        if(ChatActivityUtils == null) ChatActivityUtils = findClassInQQ(".activity.ChatActivityUtils");
        if(ChatMessage == null) ChatMessage = findClassInQQ(".data.ChatMessage");
        if(Conversation == null) Conversation = findClassInQQ(".activity.Conversation");
        if(ConversationNowController == null) ConversationNowController = findClassInQQ(".now.enter.ConversationNowController");
        if(Contacts == null) Contacts = findClassInQQ(".activity.Contacts");
        if(ContactUtils == null) ContactUtils = findClassInQQ(".utils.ContactUtils");
        if(CoreService == null) CoreService = findClassInQQ(".app.CoreService");
        if(CoreService$KernelService == null) CoreService$KernelService = findClassInQQ(".app.CoreService$KernelService");
        if(CountDownProgressBar == null) CountDownProgressBar = findClassInQQ("com.tencent.widget.CountDownProgressBar");
        if(EmoticonManager == null) EmoticonManager = findClassInQQ(".model.EmoticonManager");
        if(FontManager == null) FontManager = findClassInQQ("com.etrump.mixlayout.FontManager");
        if(FrameHelperActivity == null) FrameHelperActivity = findClassInQQ(".app.FrameHelperActivity");
        if(GatherContactsTips == null) GatherContactsTips = findClassInQQ(".activity.aio.tips.GatherContactsTips");
        if(GrayTipsItemBuilder == null) GrayTipsItemBuilder = findClassInQQ(".activity.aio.item.GrayTipsItemBuilder");
        if(HotChatFlashPicActivity == null) HotChatFlashPicActivity = findClassInQQ(".dating.HotChatFlashPicActivity");
        if(ItemBuilderFactory == null) ItemBuilderFactory = findClassInQQ(".activity.aio.item.ItemBuilderFactory");
        if(Leba == null) Leba = findClassInQQ(".activity.Leba");
        if(LocalSearchBar == null) LocalSearchBar = findClassInQQ(".activity.recent.LocalSearchBar");
        if(MainFragment == null) MainFragment = findClassInQQ(".activity.MainFragment");
        if(MedalNewsItemBuilder == null) MedalNewsItemBuilder = findClassInQQ(".activity.aio.item.MedalNewsItemBuilder");
        if(MessageForDeliverGiftTips == null) MessageForDeliverGiftTips = findClassInQQ(".data.MessageForDeliverGiftTips");
        if(MessageRecord == null) MessageRecord = findClassInQQ(".data.MessageRecord");
        if(MessageRecordFactory == null) MessageRecordFactory = findClassInQQ(".service.message.MessageRecordFactory");
        if(OnLongClickAndTouchListener == null) OnLongClickAndTouchListener = findClassInQQ(".activity.aio.OnLongClickAndTouchListener");
        if(PopupMenuDialog == null) PopupMenuDialog = findClassInQQ("com.tencent.widget.PopupMenuDialog");
        if(PopupMenuDialog$MenuItem == null) PopupMenuDialog$MenuItem = findClassInQQ("com.tencent.widget.PopupMenuDialog$MenuItem");
        if(PopupMenuDialog$OnClickActionListener == null) PopupMenuDialog$OnClickActionListener = findClassInQQ("com.tencent.widget.PopupMenuDialog$OnClickActionListener");
        if(QQAppInterface == null) QQAppInterface = findClassInQQ(".app.QQAppInterface");
        if(QQMessageFacade == null) QQMessageFacade = findClassInQQ(".app.message.QQMessageFacade");
        if(QQSettingMe == null) QQSettingMe = findClassInQQ(".activity.QQSettingMe");
        if(QQSettingSettingActivity == null) QQSettingSettingActivity = findClassInQQ(".activity.QQSettingSettingActivity");
        if(QzoneFeedItemBuilder == null) QzoneFeedItemBuilder = findClassInQQ(".activity.aio.item.QzoneFeedItemBuilder");
        if(QzonePluginProxyActivity == null) QzonePluginProxyActivity = findClassInQQ("cooperation.qzone.QzonePluginProxyActivity");
        if(RichStatItemBuilder == null) RichStatItemBuilder = findClassInQQ(".activity.aio.item.RichStatItemBuilder");
        if(SimpleSlidingIndicator == null) SimpleSlidingIndicator = findClassInQQ(".activity.contacts.view.SimpleSlidingIndicator");
        if(SougouInputGrayTips == null) SougouInputGrayTips = findClassInQQ(".activity.aio.tips.SougouInputGrayTips");
        if(TextItemBuilder == null) TextItemBuilder = findClassInQQ(".activity.aio.item.TextItemBuilder");
        if(TextPreviewActivity == null) TextPreviewActivity = findClassInQQ(".activity.TextPreviewActivity");
        if(TListView == null) TListView = findClassInQQ("com.tencent.widget.ListView");
        if(TroopEnterEffectController == null) TroopEnterEffectController = findClassInQQ(".troop.enterEffect.TroopEnterEffectController");
        if(TroopGiftAnimationController == null) TroopGiftAnimationController = findClassInQQ(".troopgift.TroopGiftAnimationController");
        if(VipSpecialCareGrayTips == null) VipSpecialCareGrayTips = findClassInQQ(".activity.aio.tips.VipSpecialCareGrayTips");
    }

    private static Class<?> findClassInQQ(String className) {
        if (qqClassLoader == null || className.equals("")) return null;
        if(className.startsWith(".")) className = PACKAGE_NAME_QQ + className;
        try {
            return qqClassLoader.loadClass(className);
        } catch (Throwable e) {
            if(!className.contains("com.tencent.mobileqq.R$"))
                Util.log("Classes", String.format("%s Can't find the Class of name: %s!", getQQVersion(), className));
        }
        return null;
    }
}