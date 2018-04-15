package me.zpp0196.qqsimple.hook.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import me.zpp0196.qqsimple.hook.util.XPrefs;

import static me.zpp0196.qqsimple.Common.getQQVersion;
import static me.zpp0196.qqsimple.Common.qqClassLoader;

/**
 * Created by zpp0196 on 2018/3/18.
 */

public abstract class BaseHook {

    private String QQ_VERSION = "";

    protected String getQQ_Version() {
        if (QQ_VERSION.equals("")) {
            Context context = (Context) XposedHelpers.callMethod(XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread", new Object[0]), "getSystemContext", new Object[0]);
            QQ_VERSION = getQQVersion(context);
        }
        return QQ_VERSION;
    }

    protected boolean isMoreThan732() {
        return getQQ_Version().compareTo("7.3.2") >= 0;
    }

    protected boolean isMoreThan735() {
        return getQQ_Version().compareTo("7.3.5") >= 0;
    }

    protected Class<?> findClassInQQ(String className) {
        if (qqClassLoader == null || className.equals("")) return null;
        try {
            return qqClassLoader.loadClass(className);
        } catch (Throwable e) {
            log("%s Can't find the Class of name: %s!", getQQ_Version(), className);
        }
        return null;
    }

    protected void log(@NonNull Object object) {
        if (!XPrefs.isPrintLog()) return;
        if (object instanceof Throwable) {
            XposedBridge.log((Throwable) object);
        } else {
            XposedBridge.log(getClass().getSimpleName() + " -> " + object.toString());
        }
    }

    protected void log(String str, Object... object) {
        log(String.format(str, object));
    }

    protected int getId(String name) {
        try {
            return XposedHelpers.getStaticIntField(findClassInQQ("com.tencent.mobileqq.R$id"), name);
        } catch (Throwable e) {
            log("%s Can't find the id of name: %s!", getQQ_Version(), name);
        }
        return 0;
    }

    protected int getDrawableId(String name) {
        try {
            return XposedHelpers.getStaticIntField(findClassInQQ("com.tencent.mobileqq.R$drawable"), name);
        } catch (Throwable e) {
            log("%s Can't find the drawable of name: %s!", getQQ_Version(), name);
        }
        return 0;
    }

    protected void removeView(int id, boolean isHide) {
        if (id == 0 || !isHide) return;
        findAndHookMethod(View.class, "setLayoutParams", ViewGroup.LayoutParams.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                View view = (View) param.thisObject;
                if (view.getId() == id) {
                    ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) param.args[0];
                    layoutParams.height = 1;
                    layoutParams.width = 0;
                    view.setVisibility(View.GONE);
                }
            }
        });
    }

    protected void removeDrawable(int id, boolean isHide) {
        if (id == 0 || !isHide) return;
        findAndHookMethod(ImageView.class, "setImageResource", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if ((int) param.args[0] == id) {
                    param.setResult(null);
                }
            }
        });
    }

    protected Field findField(@NonNull Class<?> clazz, String name) {
        Field field = XposedHelpers.findFieldIfExists(clazz, name);
        if (field == null) {
            log("%s Can't find the field of name: %s in class: %s!", getQQ_Version(), name, clazz.getName());
        }
        return field;
    }

    protected Field findFirstFieldByExactType(@NonNull Class<?> clazz, Class<?> type) {
        Class<?> clz = clazz;
        do {
            for (Field field : clz.getDeclaredFields()) {
                if (field.getType() == type) {
                    field.setAccessible(true);
                    return field;
                }
            }
        } while ((clz = clz.getSuperclass()) != null);
        log("%s Can't find the field of type: %s in class: %s!", getQQ_Version(), type.getName(), clazz.getName());
        return null;
    }

    protected Field findField(@NonNull Class<?> clazz, Class<?> type, String name) {
        Class<?> clz = clazz;
        do {
            for (Field field : clz.getDeclaredFields()) {
                if (field.getType() == type && field.getName().equals(name)) {
                    field.setAccessible(true);
                    return field;
                }
            }
        } while ((clz = clz.getSuperclass()) != null);
        log("%s Can't find the field of type: %s and name: %s in class: %s!", getQQ_Version(), type.getName(), name, clazz.getName());
        return null;
    }

    protected Class<?> findClass(String className, ClassLoader classLoader) {
        try {
            return XposedHelpers.findClass(className, classLoader);
        } catch (Throwable e) {
            log("%s Can't find the Class of name: %s!", getQQ_Version(), className);
            return null;
        }
    }

    protected void findAndHookMethod(Class<?> clazz, String methodName, Object... parameterTypesAndCallback) {
        if (clazz == null || methodName.equals("") || parameterTypesAndCallback.length == 0 || !(parameterTypesAndCallback[parameterTypesAndCallback.length - 1] instanceof XC_MethodHook))
            return;
        for (Object obj : parameterTypesAndCallback) {
            if (obj == null) return;
        }
        try {
            XposedHelpers.findAndHookMethod(clazz, methodName, parameterTypesAndCallback);
        } catch (Throwable e) {
            log(e);
        }
    }

    protected void findAndHookMethod(String className, ClassLoader classLoader, String methodName, Object... parameterTypesAndCallback) {
        findAndHookMethod(findClass(className, classLoader), methodName, parameterTypesAndCallback);
    }

    protected void findAndHookConstructor(Class<?> clazz, Object... parameterTypesAndCallback) {
        if (clazz == null || parameterTypesAndCallback.length == 0 || !(parameterTypesAndCallback[parameterTypesAndCallback.length - 1] instanceof XC_MethodHook))
            return;
        for (Object obj : parameterTypesAndCallback) {
            if (obj == null) return;
        }
        try {
            XposedHelpers.findAndHookConstructor(clazz, parameterTypesAndCallback);
        } catch (Throwable e) {
            log(e);
        }
    }

    protected void findAndHookConstructor(String className, ClassLoader classLoader, Object... parameterTypesAndCallback) {
        findAndHookConstructor(findClass(className, classLoader), parameterTypesAndCallback);
    }

    protected boolean getBool(String key) {
        return XPrefs.getPref().getBoolean(key, false);
    }
}
