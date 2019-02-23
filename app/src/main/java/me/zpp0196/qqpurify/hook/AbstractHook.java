package me.zpp0196.qqpurify.hook;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import me.zpp0196.qqpurify.utils.Constants;
import me.zpp0196.qqpurify.utils.ReflectionUtils;
import me.zpp0196.qqpurify.utils.XPrefUtils;

/**
 * Created by zpp0196 on 2018/3/18.
 */

public abstract class AbstractHook extends Constants.Class {

    private static ClassLoader classLoader;
    private final LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(0, 0);
    private final RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(0, 0);

    public static void setClassLoader(ClassLoader classLoader) {
        AbstractHook.classLoader = classLoader;
    }

    public abstract void init() throws Throwable;

    protected String getTAG() {
        return getClass().getSimpleName();
    }

    protected void log(String msg) {
        Log.i(getTAG(), msg);
    }

    protected Class<?> findClass(String className) {
        try {
            return XposedHelpers.findClass(className, classLoader);
        } catch (XposedHelpers.ClassNotFoundError e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void findAndHookMethod(String className, Class<?> returnType, String methodName, XC_MethodHook callback) {
        findAndHookMethod(className, returnType, methodName, new Class<?>[0], callback);
    }

    protected void findAndHookMethod(String className, Class<?> returnType, String methodName, Class<?>[] parameterTypes, XC_MethodHook callback) {
        Class<?> clazz = findClass(className);
        if (clazz != null) {
            try {
                Method method = ReflectionUtils.findMethodIfExists(clazz, returnType, methodName, parameterTypes);
                XposedBridge.hookMethod(method, callback);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    protected void findAndHookMethod(String className, String methodName, Object... parameterTypesAndCallback) {
        findAndHookMethod(findClass(className), methodName, parameterTypesAndCallback);
    }

    protected void findAndHookMethod(Class<?> clazz, String methodName, Object... parameterTypesAndCallback) {
        if (clazz != null) {
            try {
                XposedHelpers.findAndHookMethod(clazz, methodName, parameterTypesAndCallback);
            } catch (NoSuchMethodError e) {
                e.printStackTrace();
            }
        }
    }

    protected void findAndHideView(Object obj, Class<?> viewType, String name, String key) {
        if (getBool(key)) {
            hideView(getObjectIfExists(obj, viewType, name));
        }
    }

    protected XC_MethodHook hideViewAfterMethod(Class<?> viewType, String viewName) {
        return new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                hideView(getObjectIfExists(param.thisObject, viewType, viewName));
            }
        };
    }

    protected void hideView(View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup != null) {
                if (viewGroup instanceof LinearLayout) {
                    view.setLayoutParams(linearParams);
                } else if (viewGroup instanceof RelativeLayout) {
                    view.setLayoutParams(relativeParams);
                }
            }
        }
    }

    protected <T> T getObjectIfExists(Object obj, Class<?> fieldType, String fieldName) {
        return ReflectionUtils.getObjectIfExists(obj.getClass(), fieldType, fieldName, obj);
    }

    protected <T> T getObjectIfExists(Object obj, String typeName, String fieldName) {
        return ReflectionUtils.getObjectIfExists(obj.getClass(), typeName, fieldName, obj);
    }

    protected boolean getBool(String key) {
        return getBool(key, false);
    }

    protected boolean getBool(String key, boolean defValue) {
        return XPrefUtils.getPref()
                .getBoolean(key, defValue);
    }

    @SuppressWarnings ("unused")
    protected void recursiveView(String space, View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            Log.i(getTAG(), space + viewGroup.getClass()
                    .getName() + ", id: " + viewGroup.getId() + ", visible: " +
                            viewGroup.getVisibility());
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                recursiveView(space + space, viewGroup.getChildAt(i));
            }
        } else if (view instanceof TextView) {
            Log.i(getTAG(), space + view.getClass()
                    .getName() + ", id: " + view.getId() + ", text: " +
                            ((TextView) view).getText() + ", visible: " + view.getVisibility());
        } else {
            Log.i(getTAG(), space + view.getClass()
                    .getName() + ", id: " + view.getId() + ", visible: " + view.getVisibility());
        }
    }
}
