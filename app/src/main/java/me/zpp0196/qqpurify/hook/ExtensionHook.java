package me.zpp0196.qqpurify.hook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import me.zpp0196.qqpurify.utils.ReflectionUtils;
import me.zpp0196.qqpurify.utils.XPrefUtils;

/**
 * Created by zpp0196 on 2019/2/8.
 */

public class ExtensionHook extends AbstractHook {

    private Context context;
    private String redirectPath;

    public ExtensionHook(Context context) {
        this.context = context;
    }

    @Override
    public void init() throws Throwable {
        // 模拟菜单
        if (getBool("conversation_simulate_menu")) {
            simulateMenu();
        }
        // 隐藏菜单更新和反馈
        if (getBool("conversation_hide_menuUAF", true)) {
            hideMenuUAF();
        }
        // 隐藏底部分组
        hideMainFragmentTab();
        // 防止消息撤回
        if (getBool("chat_prevent_msgRecall", true)) {
            preventMessagesRecall();
        }
        // 防止闪照销毁
        if (getBool("chat_prevent_flashDisappear", true)) {
            preventFlashDisappear();
        }
        // 以图片方式打开表情包
        if (getBool("chat_prevent_dynamicPic")) {
            preventDynamicPic();
        }
        // 查看图片背景透明
        if (getBool("chat_transparent_imgBg", true)) {
            transImageBg();
        }
        // 重命名base.apk
        if (getBool("troop_rename_base", true)) {
            renameBaseApk();
        }
        // 重定向文件下载目录
        if (getBool("extension_redirect_filerec")) {
            redirectFileRec();
        }
        // 自定义字体大小 FIXME taichi
        if (getBool("global_set_fontSize")) {
            setGlobalFontSize();
        }
    }

    private void simulateMenu() {
        findAndHookMethod(Conversation, "D", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                RelativeLayout relativeLayout = getObjectIfExists(param.thisObject, RelativeLayout.class, "b");
                relativeLayout.setOnClickListener(v -> new Thread(() -> {
                    try {
                        Instrumentation inst = new Instrumentation();
                        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start());
            }
        });
    }

    private void hideMenuUAF() {
        findAndHookMethod(MainFragment, "r", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Dialog dialog = getObjectIfExists(param.thisObject, Dialog.class, "b");
                LinearLayout layout = getObjectIfExists(dialog, LinearLayout.class, "a");
                layout.getChildAt(0)
                        .setVisibility(View.GONE);
                layout.getChildAt(1)
                        .setVisibility(View.GONE);
            }
        });
    }

    private View lebaTab;
    private View lebaTabNew;

    private void hideMainFragmentTab() {
        findAndHookMethod(MainFragment, View[].class, "a", new Class<?>[]{View.class}, new XC_MethodHook() {
            @Override
            @SuppressLint("WrongConstant")
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                View[] views = getObjectIfExists(param.thisObject, View[].class, "a");
                // 联系人
                if (!getBool("contacts_display_tab", true)) {
                    hideView(views[2]);
                }
                // 动态
                if (!getBool("leba_display_tab", true)) {
                    hideView(views[3]);
                }
                // 空间直达
                if (getBool("leba_ce_qzoneEntry")) {
                    Activity activity = (Activity) XposedHelpers.callMethod(param.thisObject, "getActivity");
                    View.OnClickListener enterQzone = view -> {
                        Intent intent = new Intent();
                        intent.putExtra("newflag", true);
                        intent.putExtra("refer", "schemeActiveFeeds");
                        XposedHelpers.callStaticMethod(findClass(QzonePluginProxyActivity), "a", intent, "com.qzone.feed.ui.activity.QZoneFriendFeedActivity");
                        intent.addFlags(0x30000000);
                        Object qqAppInterface = getObjectIfExists(param.thisObject, QQAppInterface, "a");
                        String uin = (String) XposedHelpers.callMethod(qqAppInterface, "getCurrentAccountUin");
                        XposedHelpers.callStaticMethod(findClass(QZoneHelper), "b", activity, uin, intent, -1);
                    };
                    if (lebaTab != null) {
                        lebaTab.setOnClickListener(enterQzone);
                    }
                    if (lebaTabNew != null) {
                        lebaTabNew.setOnClickListener(enterQzone);
                    }
                }
            }
        });
        findAndHookMethod(MainFragment, "a", int.class, int.class, int.class, int.class, int.class, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                View view = (View) param.getResult();
                Context context = view.getContext();
                int strId = (int) param.args[4];
                String title = context.getResources().getString(strId);
                if (title.equals("动态")) {
                    if (lebaTab == null) {
                        lebaTab = view;
                    } else {
                        lebaTabNew = view;
                    }
                }
            }
        });
        // 隐藏联系人和动态底部消息数量
        findAndHookMethod(MainFragment, "a", int.class, BusinessInfoCheckUpdate$RedTypeInfo, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                int i = (int) param.args[0];
                if ((i == 33 && (getBool("contacts_hide_tabNum") || getBool("contacts_hide_newFriend"))) ||
                    (i == 34 && getBool("leba_hide_tabNum"))) {
                    param.setResult(null);
                }
            }
        });
        // 隐藏消息列表底部消息数量
        findAndHookMethod(MainFragment, "a", int.class, int.class, Object.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                int i = (int) param.args[0];
                if (i == 32 && getBool("conversation_hide_tabNum")) {
                    param.setResult(null);
                }
            }
        });
    }

    private void preventMessagesRecall() {
        findAndHookMethod(QQMessageFacade, "a", ArrayList.class, boolean.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                ArrayList arrayList = (ArrayList) param.args[0];
                if (arrayList == null || arrayList.isEmpty() ||
                    isCallingFrom("C2CMessageProcessor")) {
                    return null;
                }
                Object revokeMsgInfo = arrayList.get(0);

                String friendUin = getObjectIfExists(revokeMsgInfo, String.class, "a");
                String fromUin = getObjectIfExists(revokeMsgInfo, String.class, "b");
                int isTroop = getObjectIfExists(revokeMsgInfo, int.class, "a");
                long msgUid = getObjectIfExists(revokeMsgInfo, long.class, "b");
                long shmsgseq = getObjectIfExists(revokeMsgInfo, long.class, "a");
                long time = getObjectIfExists(revokeMsgInfo, long.class, "c");

                Object qqAppInterface = getObjectIfExists(param.thisObject, QQAppInterface, "a");
                String selfUin = (String) XposedHelpers.callMethod(qqAppInterface, "getCurrentAccountUin");

                if (selfUin.equals(fromUin)) {
                    return null;
                }

                int msgType = (int) XposedHelpers.getStaticObjectField(findClass(MessageRecord), "MSG_TYPE_REVOKE_GRAY_TIPS");
                List tip = getRevokeTip(qqAppInterface, selfUin, friendUin, fromUin, msgUid, shmsgseq,
                        time + 1, msgType, isTroop);
                if (tip != null && !tip.isEmpty()) {
                    XposedHelpers.callMethod(param.thisObject, "a", tip, selfUin);
                }
                return null;
            }
        });
    }

    private List getRevokeTip(Object qqAppInterface, String selfUin, String friendUin, String fromUin, long msgUid, long shmsgseq, long time, int msgType, int isTroop) {
        Object messageRecord = XposedHelpers.callStaticMethod(findClass(MessageRecordFactory), "a", msgType);

        String name;
        if (isTroop == 0) {
            name = "对方";
        } else {
            name = (String) XposedHelpers.callStaticMethod(findClass(ContactUtils), "a", qqAppInterface, fromUin, friendUin,
                    isTroop == 1 ? 1 : 2, 0);
        }

        XposedHelpers.callMethod(messageRecord, "init", selfUin,
                isTroop == 0 ? fromUin : friendUin, fromUin,
                name + "尝试撤回一条消息", time, msgType, isTroop, time);

        XposedHelpers.setObjectField(messageRecord, "msgUid",
                msgUid == 0 ? 0 : msgUid + new Random().nextInt());
        XposedHelpers.setObjectField(messageRecord, "shmsgseq", shmsgseq);
        XposedHelpers.setObjectField(messageRecord, "isread", true);

        List<Object> list = new ArrayList<>();
        list.add(messageRecord);
        return list;
    }

    private boolean isCallingFrom(String className) {
        StackTraceElement[] stackTraceElements = Thread.currentThread()
                .getStackTrace();
        for (StackTraceElement element : stackTraceElements) {
            if (element.getClassName()
                    .contains(className)) {
                return true;
            }
        }
        return false;
    }

    private void preventFlashDisappear() {
        findAndHookMethod(CountDownProgressBar, "a", XC_MethodReplacement.returnConstant(null));

        findAndHookMethod(HotChatFlashPicActivity, "onTouch", View.class, MotionEvent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.args[1] = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0);
            }
        });
        findAndHookMethod(AIOImageProviderService, void.class, "a", new Class[]{long.class}, XC_MethodReplacement.returnConstant(null));

        // 隐藏倒计时
        findAndHookMethod(CountDownProgressBar, "onDraw", Canvas.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                ((View) param.thisObject).setVisibility(View.GONE);
            }
        });

        // 允许闪照截图
        findAndHookMethod(Window.class, "setFlags", int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (Integer.parseInt(param.args[0].toString()) ==
                    WindowManager.LayoutParams.FLAG_SECURE) {
                    param.setResult(null);
                }
            }
        });
        findAndHookMethod(SurfaceView.class, "setSecure", boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.args[0] = false;
            }
        });
        findAndHookMethod(HotChatFlashPicActivity, "finish", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                ReflectionUtils.setObjectField(param.thisObject, boolean.class, "c", false);
            }
        });
    }

    private void preventDynamicPic() {
        findAndHookMethod(PicItemBuilder, "onClick", View.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Object v0 = ReflectionUtils.findMethodIfExists(findClass("com.tencent.mobileqq.activity.aio.AIOUtils"), Object.class, "a", View.class)
                        .invoke(null, param.args[0]);
                Object chatMessage = getObjectIfExists(v0, ChatMessage, "a");
                Object picMessageExtraData = XposedHelpers.findField(chatMessage.getClass(), "picExtraData")
                        .get(chatMessage);
                ReflectionUtils.setObjectField(chatMessage, int.class, "imageType", 0);
                if (picMessageExtraData != null) {
                    ReflectionUtils.setObjectField(picMessageExtraData, int.class, "imageBizType", 0);
                }
            }
        });
    }

    private void transImageBg() {
        findAndHookMethod(AbstractGalleryScene, "a", boolean.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                int intAlpha = XPrefUtils.getPref()
                        .getInt("chat_imgBg_alphaPercent", 50);
                if (intAlpha >= 0 && intAlpha <= 100) {
                    float floatAlpha = 1 - intAlpha / 100.0f;
                    View d = getObjectIfExists(param.thisObject, View.class, "d");
                    if (d != null) {
                        d.setAlpha(floatAlpha);
                    }
                    RelativeLayout root = getObjectIfExists(param.thisObject, RelativeLayout.class, "a");
                    View view = root.getChildAt(root.getChildCount() - 1);
                    if (view != null) {
                        view.setAlpha(floatAlpha);
                    }
                }
            }
        });
    }

    private void renameBaseApk() {
        final PackageManager packageManager = context.getPackageManager();
        String apkFormat = XPrefUtils.getPref()
                .getString("troop_rename_format", "%l_%n.apk");
        XposedHelpers.findAndHookMethod(File.class, "getName", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (param.getResult()
                        .toString()
                        .equals("base.apk")) {
                    File file = (File) param.thisObject;
                    if (file.getParentFile()
                            .getAbsolutePath()
                            .contains("/data/app/")) {
                        String parentFilePath = file.getParentFile()
                                .getName();
                        String packageName = parentFilePath.substring(0, parentFilePath.indexOf("-"));
                        PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
                        if (!TextUtils.isEmpty(apkFormat)) {
                            param.setResult(apkFormat.replace("%p", packageName)
                                    .replace("%l", packageInfo.applicationInfo.loadLabel(packageManager)
                                            .toString())
                                    .replace("%n", packageInfo.versionName)
                                    .replace("%c", String.valueOf(packageInfo.versionCode)));
                        }
                    }
                }
            }
        });
    }

    private void redirectFileRec() {
        final String srcPath = "/Tencent/QQfile_recv/";
        redirectPath = XPrefUtils.getPref()
                .getString("extension_redirect_path", srcPath);
        if (TextUtils.isEmpty(redirectPath)) {
            return;
        }
        if (!redirectPath.startsWith("/")) {
            redirectPath = "/" + redirectPath;
        }
        if (!redirectPath.endsWith("/")) {
            redirectPath = redirectPath + "/";
        }
        XposedHelpers.findAndHookConstructor(File.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (param.args[0].toString()
                        .contains(srcPath)) {
                    param.args[0] = param.args[0].toString()
                            .replace(srcPath, redirectPath);
                }
            }
        });
    }

    private void setGlobalFontSize() {
        float fontSize = XPrefUtils.getPref()
                                 .getInt("extension_global_fontSize", 16) * 1.0f;
        findAndHookMethod(FontSettingManager, "a", float.class, XC_MethodReplacement.returnConstant(true));
        findAndHookMethod(FontSettingManager, "a", Context.class, float.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.args[1] = fontSize;
            }
        });
    }
}
