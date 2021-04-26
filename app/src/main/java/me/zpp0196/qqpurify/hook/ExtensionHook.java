package me.zpp0196.qqpurify.hook;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.zpp0196.library.xposed.XC_MemberHook;
import me.zpp0196.library.xposed.XField;
import me.zpp0196.library.xposed.XLog;
import me.zpp0196.library.xposed.XMethod;
import me.zpp0196.library.xposed.XMethodHook;
import me.zpp0196.qqpurify.hook.annotation.MethodHook;
import me.zpp0196.qqpurify.hook.annotation.VersionSupport;
import me.zpp0196.qqpurify.hook.base.BaseHook;
import me.zpp0196.qqpurify.hook.utils.QQConfigUtils;
import me.zpp0196.qqpurify.utils.Utils;

import static me.zpp0196.qqpurify.utils.Utils.isCallingFrom;

/**
 * Created by zpp0196 on 2019/2/8.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ExtensionHook extends BaseHook {

    public ExtensionHook(Context context) {
        super(context);
    }

    // region 防撤回
    @MethodHook(desc = "防止消息撤回")
    public void preventRecall() {
        XMethodHook.create($(QQMessageFacade)).params(ArrayList.class, boolean.class)
                .method("a").hook(new XC_MemberHook() {
            @Override
            protected void onBeforeHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                ArrayList list = param.args(0);
                if (isCallingFrom(QQConfigUtils.findClass(C2CMessageProcessor)) ||
                        list == null || list.isEmpty()) {
                    param.setResult(null);
                    return;
                }
                Object revokeMsgInfo = list.get(0);

                XField xField = XField.create(revokeMsgInfo);
                String friendUin = xField.exact(String.class, "a").get();
                String fromUin = xField.exact(String.class, "b").get();
                int isTroop = xField.exact(int.class, "a").get();
                long msgUid = xField.exact(long.class, "b").get();
                long shmsgseq = xField.exact(long.class, "a").get();
                long time = xField.exact(long.class, "c").get();

                Object qqApp = XField.create(param).type(QQAppInterface).get();
                String selfUin = XMethod.create(qqApp).name("getCurrentAccountUin").invoke();

                if (selfUin.equals(fromUin)) {
                    param.setResult(null);
                    return;
                }

                int msgType = XField.create($(MessageRecord)).name("MSG_TYPE_REVOKE_GRAY_TIPS").get();
                List tip = getRevokeTip(qqApp, selfUin, friendUin, fromUin, msgUid, shmsgseq,
                        time + 1, msgType, isTroop);
                if (tip != null && !tip.isEmpty()) {
                    XMethod.create(param).name("a").invoke(tip, selfUin);
                }
                param.setResult(null);
            }
        });
    }

    private List getRevokeTip(Object qqAppInterface, String selfUin, String friendUin, String fromUin,
                              long msgUid, long shmsgseq, long time, int msgType, int isTroop) {
        Object messageRecord = XMethod.create($(MessageRecordFactory)).name("a").invoke(msgType);

        String name;
        if (isTroop == 0) {
            name = "对方";
        } else {
            name = XMethod.create($(ContactUtils)).name("a").invoke(qqAppInterface, fromUin,
                    friendUin, isTroop == 1 ? 1 : 2, 0);
        }

        XMethod.create(messageRecord).name("init").invoke(selfUin, isTroop == 0 ? fromUin :
                friendUin, fromUin, name + "尝试撤回一条消息", time, msgType, isTroop, time);

        XField.create(messageRecord).name("msgUid").set(msgUid == 0 ? 0 : msgUid + new Random().nextInt());
        XField.create(messageRecord).name("shmsgseq").set(shmsgseq);
        XField.create(messageRecord).name("isread").set(true);

        List<Object> list = new ArrayList<>();
        list.add(messageRecord);
        return list;
    }

    // endregion

    @MethodHook(desc = "查看图片背景透明")
    public void transparentImgBg() {
        String value = getString(KEY_IMAGE_BG_COLOR);
        if (value.isEmpty()) {
            return;
        }
        int color = Color.parseColor(value);
        XMethodHook.create($(AbstractGalleryScene)).method("a").params(ViewGroup.class)
                .hook(new XC_MemberHook() {
                    @Override
                    protected void onAfterHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                        View d = XField.create(param).exact(View.class, "d").get();
                        d.setBackgroundColor(color);
                    }
                });
    }

    @MethodHook(desc = "以图片方式打开闪照")
    public void openFlashAsPic() {
        XMethodHook.create($(FlashPicHelper)).method(boolean.class, "a")
                .params(MessageRecord).hook(new XC_MemberHook() {
            @Override
            protected void onBeforeHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                if (isCallingFrom(QQConfigUtils.findClass(ItemBuilderFactory)) ||
                        isCallingFrom(QQConfigUtils.findClass(BasePicDownloadProcessor))) {
                    param.setResult(false);
                }
            }
        });
        XMethodHook.create($(PicItemBuilder)).method("a").params(ChatMessage, BaseBubbleBuilder$ViewHolder,
                View.class, BaseChatItemLayout, OnLongClickAndTouchListener).hook(new XC_MemberHook() {
            @Override
            protected void onAfterHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                Object viewHolder = param.args[1];
                if (viewHolder == null) {
                    return;
                }
                boolean isFlash = XMethod.create($(FlashPicHelper))
                        .exact(boolean.class, "a")
                        .types(MessageRecord)
                        .invoke(param.args(0, Object.class));
                XMethod.create(param.args(3, Object.class))
                        .name("setTailMessage")
                        .invoke(isFlash, "闪照", null);
            }
        });
    }

    @MethodHook(desc = "以图片方式打开表情包")
    @VersionSupport(min = QQ_795, exclude = {QQ_799, QQ_800})
    public void openEmoticonAsPic() {
        XMethodHook.create($(PicItemBuilder)).method("onClick").hook(new XC_MemberHook() {
            @Override
            protected void onBeforeHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                Object v0 = XMethod.create($(AIOUtils)).exact(Object.class, "a")
                        .types(View.class).invoke(param.args[0]);
                Object chatMessage = XField.create(v0).exact($(ChatMessage), "a").get();
                Object picMessageExtraData = XField.create(chatMessage).name("picExtraData").get();
                XField.create(chatMessage).exact(int.class, "imageType").set(0);
                if (picMessageExtraData != null) {
                    XField.create(picMessageExtraData).exact(int.class, "imageBizType").set(0);
                }
            }
        });
    }

    @MethodHook(desc = "防止被@")
    public void preventAt() {
        XMethodHook.create($(MessageInfo)).method("a").params(QQAppInterface, String.class,
                MessageInfo, Object.class, MessageRecord, boolean.class).intercept();
    }

    @MethodHook(desc = "签到文本化")
    public void simpleSign() {
        XMethodHook.create($(ItemBuilderFactory)).method("a").params(ChatMessage)
                .hook(new XC_MemberHook() {
                    @Override
                    protected void onAfterHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                        int result = param.getResult();
                        if (result == 71 || result == 84) {
                            param.setResult(-1);
                        }
                    }
                });
    }

    @MethodHook(desc = "重命名base.apk")
    public void renameBase() {
        final PackageManager pm = mContext.getPackageManager();
        String apkFormat = getString(KEY_RENAME_BASE_FORMAT);
        XMethodHook.create(File.class).method("getName").hook(new XC_MemberHook() {
            @Override
            protected void onAfterHooked(@NonNull XC_MemberHook.MemberHookParam param) {
                if (!param.getResult().toString().equals("base.apk")) {
                    return;
                }
                File file = param.thisObject();
                if (!file.getParentFile().getAbsolutePath().contains("/data/app/")) {
                    return;
                }
                String parentFilePath = file.getParentFile().getName();
                String packageName = parentFilePath.substring(0, parentFilePath.indexOf("-"));
                PackageInfo packageInfo = null;
                try {
                    packageInfo = pm.getPackageInfo(packageName, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    onAfterError(param, e);
                }
                if (packageInfo == null || TextUtils.isEmpty(apkFormat)) {
                    return;
                }
                param.setResult(apkFormat.replace("%p", packageName)
                        .replace("%l", packageInfo.applicationInfo.loadLabel(pm).toString())
                        .replace("%n", packageInfo.versionName)
                        .replace("%c", String.valueOf(Utils.getAppVersionCode(mContext, packageName))));
            }
        });
    }

    @MethodHook(desc = "重定向文件下载目录")
    @VersionSupport(min = QQ_760)
    public void redirectFileRec() {
        String redirectPath = getString(KEY_REDIRECT_FILE_REC_PATH);
        if (TextUtils.isEmpty(redirectPath)) {
            return;
        }
        if (!redirectPath.startsWith("/")) {
            redirectPath = "/" + redirectPath;
        }
        if (!redirectPath.endsWith("/")) {
            redirectPath = redirectPath + "/";
        }
        redirectPath = Environment.getExternalStorageDirectory().getAbsolutePath() + redirectPath;
        Field[] fields = $(AppConstants).getFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(null);
                String path = String.valueOf(value);
                if (path.toLowerCase().endsWith("file_recv/")) {
                    field.set(null, redirectPath);
                    return;
                }
            } catch (IllegalAccessException e) {
                XLog.e(e);
            }
        }
    }

    @MethodHook(desc = "隐藏部分小红点")
    public void hideReadTouch() {
        XMethodHook.create($(RedTouch)).method(ImageView.class, "a").params(int.class)
                .afterHooked(param -> hideView(param.getResult())).hook();
    }

    @MethodHook(desc = "隐藏资料卡浮屏")
    @VersionSupport(min = QQ_798)
    public void hideColorScreen() {
        String lcc = QQConfigUtils.getMethod("ext_lcc", "l");
        XMethodHook.create($(FriendProfileCardActivity)).method(lcc).intercept();
    }

    @Override
    public String getSettingGroup() {
        return SETTING_EXTENSION;
    }
}
