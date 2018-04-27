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
import java.util.Date;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import me.zpp0196.qqsimple.hook.base.BaseHook;
import me.zpp0196.qqsimple.hook.util.Util;

import static me.zpp0196.qqsimple.hook.comm.Classes.AIOImageProviderService;
import static me.zpp0196.qqsimple.hook.comm.Classes.Conversation;
import static me.zpp0196.qqsimple.hook.comm.Classes.CoreService;
import static me.zpp0196.qqsimple.hook.comm.Classes.CoreService$KernelService;
import static me.zpp0196.qqsimple.hook.comm.Classes.CountDownProgressBar;
import static me.zpp0196.qqsimple.hook.comm.Classes.HotChatFlashPicActivity;
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
        if (!getBool("disable_coreservice")) return;
        findAndHookMethod(CoreService, "startCoreService", boolean.class, XC_MethodReplacement.returnConstant(null));
        findAndHookMethod(CoreService, "startTempService", XC_MethodReplacement.returnConstant(null));
        findAndHookMethod(CoreService$KernelService, "onCreate", XC_MethodReplacement.returnConstant(null));
    }

    /**
     * 防止闪照消失
     */
    private void preventFlashDisappear() {
        if (!getBool("prevent_flash_disappear")) return;
        findAndHookMethod(CountDownProgressBar, "a", XC_MethodReplacement.returnConstant(null));
        findAndHookMethod(HotChatFlashPicActivity, "onTouch", View.class, MotionEvent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                param.args[1] = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0);
            }
        });
        if (AIOImageProviderService != null) {
            Method[] methods = AIOImageProviderService.getDeclaredMethods();
            for (Method method : methods) {
                Class<?>[] name = method.getParameterTypes();
                if (method.getName().equals("a") && method.getGenericReturnType().toString().equals("void") && name.length == 1 && name[0].getName().equals("long")) {
                    XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(null));
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
                    param.args[0] = false;
                }
            });
        }
    }

    /**
     * 防止消息撤回
     */
    private void preventMessagesWithdrawn() {
        if (getBool("prevent_messages_withdrawn")) findAndHookMethod(QQMessageFacade, "a", ArrayList.class, boolean.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                ArrayList arrayList = (ArrayList) param.args[0];
                if(arrayList == null || arrayList.isEmpty()) return null;
                Object RevokeMsgInfo = arrayList.get(0);
                String fromuin = findObject(RevokeMsgInfo, String.class, "b");
                Util.log("QQ Revoke Log","%s revoke a message at %s", fromuin, String.format("%tc", new Date(System.currentTimeMillis())));
                return null;
            }
        });
    }

    /**
     * 模拟菜单
     */
    private void simulateMenu() {
        if (getBool("simulate_menu")) findAndHookMethod(Conversation, "D", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
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