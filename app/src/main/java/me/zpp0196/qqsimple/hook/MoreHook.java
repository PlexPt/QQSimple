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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static me.zpp0196.qqsimple.hook.MainHook.getQQ_Version;

/**
 * Created by zpp0196 on 2018/3/11.
 */

public class MoreHook {

    private ClassLoader classLoader;
    private boolean isRevoke = false;
    private ArrayList messageuin = new ArrayList();

    public MoreHook(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * 防止闪照消失
     */
    public void preventFlashDisappear() {
        Class<?> CountDownProgressBar = getClass("com.tencent.widget.CountDownProgressBar");
        XposedHelpers.findAndHookMethod(CountDownProgressBar, "a", XC_MethodReplacement.returnConstant(null));
        Class<?> HotChatFlashPicActivity = getClass("com.tencent.mobileqq.dating.HotChatFlashPicActivity");
        findAndHookMethod(HotChatFlashPicActivity, "onTouch", View.class, MotionEvent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                param.args[1] = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0);
            }
        });
        Class<?> AIOImageProviderService = getClass("com.tencent.mobileqq.activity.aio.photo.AIOImageProviderService");
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
    public void preventMessagesWithdrawn() {
        Class<?> BaseMessageManager = getClass("com.tencent.mobileqq.app.message.BaseMessageManager");
        findAndHookMethod(BaseMessageManager, "a", ArrayList.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                isRevoke = false;
            }
        });
        findAndHookMethod(BaseMessageManager, "b", ArrayList.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                ArrayList arrayList = (ArrayList) param.args[0];
                if (arrayList == null) {
                    return;
                }
                String a = arrayList.get(0).toString();
                if (!messageuin.contains(a)) {
                    messageuin.add(a);
                    isRevoke = true;
                } else {
                    param.args[0] = null;
                }
            }
        });
        Class<?> QQMessageFacade = getClass("com.tencent.mobileqq.app.message.QQMessageFacade");
        findAndHookMethod(QQMessageFacade, "b", String.class, int.class, long.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (isRevoke) {
                    param.args[2] = 0;
                }

            }
        });
        Class<?> UniteGrayTipParam = getClass("com.tencent.mobileqq.graytip.UniteGrayTipParam");
        findAndHookConstructor(UniteGrayTipParam, String.class, String.class, String.class, int.class, int.class, int.class, long.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                String message = (String) param.args[2];
                if (isRevoke && message.contains("撤回了一条")) {
                    message = message.replace("撤回了", "尝试撤回");
                }
                param.args[2] = message;
            }
        });
    }

    /**
     * 模拟菜单
     */
    public void simulateMenu(){
        Class<?> Conversation = getClass("com.tencent.mobileqq.activity.Conversation");
        findAndHookMethod(Conversation, "D", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if(Conversation != null){
                    Field[] fields = Conversation.getDeclaredFields();
                    for(Field field : fields) {
                        field.setAccessible(true);
                        if (field.getGenericType().toString().contains("RelativeLayout") && field.getName().equals("b")) {
                            ViewGroup viewGroup = (ViewGroup) field.get(param.thisObject);
                            viewGroup.setOnClickListener(v -> {
                                new Thread(() -> {
                                    try {
                                        Instrumentation inst = new Instrumentation();
                                        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }).start();
                            });
                        }
                    }
                }
            }
        });
    }

    private Class<?> getClass(String className) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            XposedBridge.log(String.format("%s can not get className: %s", getQQ_Version(), className));
        }
        return null;
    }

    private void findAndHookMethod(Class<?> clazz, String methodName, Object... parameterTypesAndCallback) {
        if (clazz == null) {
            return;
        }
        try {
            XposedHelpers.findAndHookMethod(clazz, methodName, parameterTypesAndCallback);
        } catch (Exception e) {
            XposedBridge.log(e);
        }
    }
}
