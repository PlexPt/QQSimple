package me.zpp0196.qqsimple.hook;

import android.app.Instrumentation;
import android.os.Build;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import me.zpp0196.qqsimple.hook.base.BaseHook;
import me.zpp0196.qqsimple.hook.util.Util;
import me.zpp0196.qqsimple.hook.util.XPrefs;

import static me.zpp0196.qqsimple.hook.comm.Classes.AIOImageProviderService;
import static me.zpp0196.qqsimple.hook.comm.Classes.ContactUtils;
import static me.zpp0196.qqsimple.hook.comm.Classes.Conversation;
import static me.zpp0196.qqsimple.hook.comm.Classes.CoreService;
import static me.zpp0196.qqsimple.hook.comm.Classes.CoreService$KernelService;
import static me.zpp0196.qqsimple.hook.comm.Classes.CountDownProgressBar;
import static me.zpp0196.qqsimple.hook.comm.Classes.HotChatFlashPicActivity;
import static me.zpp0196.qqsimple.hook.comm.Classes.MessageRecord;
import static me.zpp0196.qqsimple.hook.comm.Classes.MessageRecordFactory;
import static me.zpp0196.qqsimple.hook.comm.Classes.QQAppInterface;
import static me.zpp0196.qqsimple.hook.comm.Classes.QQMessageFacade;

/**
 * Created by zpp0196 on 2018/3/11.
 */

class MoreHook extends BaseHook {

    MoreHook() {
        preventFlashDisappear();
        preventMessagesWithdrawn();
        simulateMenu();
        disableCoreService();
    }

    /**
     * 禁用 CoreService
     */
    private void disableCoreService() {
        findAndHookMethod(CoreService, "startCoreService", boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (getBool("disable_coreservice")) param.setResult(null);
            }
        });
        findAndHookMethod(CoreService, "startTempService", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (getBool("disable_coreservice")) param.setResult(null);
            }
        });
        findAndHookMethod(CoreService$KernelService, "onCreate", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (getBool("disable_coreservice")) param.setResult(null);
            }
        });
    }

    /**
     * 防止闪照消失
     */
    private void preventFlashDisappear() {
        findAndHookMethod(CountDownProgressBar, "a", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (getBool("prevent_flash_disappear")) param.setResult(null);
            }
        });
        findAndHookMethod(HotChatFlashPicActivity, "onTouch", View.class, MotionEvent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (getBool("prevent_flash_disappear"))
                    param.args[1] = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0);
            }
        });
        if (AIOImageProviderService != null) {
            Method[] methods = AIOImageProviderService.getDeclaredMethods();
            for (Method method : methods) {
                Class<?>[] name = method.getParameterTypes();
                if (method.getName().equals("a") && method.getGenericReturnType().toString().equals("void") && name.length == 1 && name[0].getName().equals("long")) {
                    XposedBridge.hookMethod(method, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            if (getBool("prevent_flash_disappear")) param.setResult(null);
                        }
                    });
                }
            }
        }
        blockSecureFlag();
    }

    /**
     * 允许闪照截图
     */
    private void blockSecureFlag() {
        findAndHookMethod(Window.class, "setFlags", int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (!getBool("prevent_flash_disappear")) return;
                if (Integer.parseInt(param.args[0].toString()) == WindowManager.LayoutParams.FLAG_SECURE) {
                    param.args[0] = WindowManager.LayoutParams.FLAG_FULLSCREEN;
                }
            }
        });
        if (Build.VERSION.SDK_INT >= 17) {
            findAndHookMethod(SurfaceView.class, "setSecure", boolean.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    if (getBool("prevent_flash_disappear")) param.args[0] = false;
                }
            });
        }
    }

    /**
     * 防止消息撤回
     */
    private void preventMessagesWithdrawn() {
        findAndHookMethod(QQMessageFacade, "a", ArrayList.class, boolean.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                ArrayList arrayList = (ArrayList) param.args[0];
                if(arrayList == null || arrayList.isEmpty() || Util.isCallingFrom("C2CMessageProcessor")) return null;
                Object revokeMsgInfo = arrayList.get(0);

                String friendUin = getObject(revokeMsgInfo, String.class, "a");
                String fromUin = getObject(revokeMsgInfo, String.class, "b");
                int isTroop = getObject(revokeMsgInfo, int.class, "a");
                int isAdmin = 0;
                long msgUid = getObject(revokeMsgInfo, long.class, "b");
                long shmsgseq = getObject(revokeMsgInfo, long.class, "a");
                long time = getObject(revokeMsgInfo, long.class, "c");

                Object qqAppInterface = findField(QQMessageFacade, QQAppInterface, "a").get(param.thisObject);
                String selfUin = (String) XposedHelpers.callMethod(qqAppInterface, "getCurrentAccountUin");

                int msgType = (int)XposedHelpers.getStaticObjectField(MessageRecord, "MSG_TYPE_REVOKE_GRAY_TIPS");
                List tip = getRevokeTip(qqAppInterface, selfUin, friendUin, fromUin, msgUid, shmsgseq, time + 1, msgType, isTroop, isAdmin);
                if(tip != null && !tip.isEmpty()) {
                    try {
                        XposedHelpers.callMethod(param.thisObject, "a", tip, selfUin);
                    } catch (Throwable t) {
                        //
                    }
                }
                return null;
            }
        });
    }

    /**
     * 撤回提示
     * @param selfUin
     * @param friendUin
     * @param fromUin
     * @param time
     * @param msgType
     * @param isTroop
     * @param isAdmin
     */
    private List getRevokeTip(Object qqAppInterface, String selfUin, String friendUin, String fromUin, long msgUid, long shmsgseq, long time, int msgType, int isTroop, int isAdmin){
        Object messageRecord = XposedHelpers.callStaticMethod(MessageRecordFactory, "a", msgType);

        String name;
        if(isTroop == 0) {
            name = "对方";
        }else {
            name = (String) XposedHelpers.callStaticMethod(ContactUtils, "a",qqAppInterface, fromUin, friendUin, isTroop == 1 ? 1 : 2, 0);
        }

        XposedHelpers.callMethod(messageRecord, "init", selfUin, isTroop == 0 ? fromUin : friendUin, fromUin, name + "尝试撤回一条消息", time, msgType, isTroop, time);

        XposedHelpers.setObjectField(messageRecord, "msgUid", msgUid == 0 ? 0 : msgUid + new Random().nextInt());
        XposedHelpers.setObjectField(messageRecord, "shmsgseq", shmsgseq);
        XposedHelpers.setObjectField(messageRecord, "isread", true);

        List<Object> list = new ArrayList<>();
        list.add(messageRecord);
        return list;
    }

    /**
     * 模拟菜单
     */
    private void simulateMenu() {
        findAndHookMethod(Conversation, "D", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if (!getBool("simulate_menu")) return;
                Field field = findField(Conversation, RelativeLayout.class, "b");
                if (field == null) return;
                ViewGroup viewGroup = (ViewGroup) field.get(param.thisObject);
                if (viewGroup == null) return;
                viewGroup.setOnClickListener(v -> new Thread(() -> {
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
}