package me.zpp0196.qqsimple.hook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import me.zpp0196.qqsimple.BuildConfig;

import static me.zpp0196.qqsimple.Common.getQQVersion;

/**
 * Created by zpp0196 on 2018/3/18.
 */

abstract class BaseHook {

    private ClassLoader qqClassLoader;
    private WeakReference<XSharedPreferences> xSharedPreferences = new WeakReference<>(null);

    BaseHook(ClassLoader qqClassLoader) {
        if (qqClassLoader != null) {
            setQQClassLoader(qqClassLoader);
        }
    }

    static String getQQ_Version() {
        Context context = (Context) XposedHelpers.callMethod(XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread", new Object[0]), "getSystemContext", new Object[0]);
        return getQQVersion(context);
    }

    static boolean isMoreThan732() {
        return getQQ_Version().compareTo("7.3.2") >= 0;
    }

    static boolean isMoreThan735() {
        return getQQ_Version().compareTo("7.3.5") >= 0;
    }

    private void setQQClassLoader(ClassLoader qqClassLoader) {
        this.qqClassLoader = qqClassLoader;
    }

    Class<?> findClassInQQ(String className) {
        if (qqClassLoader != null && !className.equals("")) {
            try {
                return qqClassLoader.loadClass(className);
            } catch (ClassNotFoundException e) {
                log("%s Can't find the Class of name: %s", getQQ_Version(), className);
            }
        }
        return null;
    }

    void log(@NonNull Object object) {
        //if(getBool(KEY_DEBUG_MODE)) {
        if (object instanceof Throwable) {
            XposedBridge.log((Throwable) object);
        } else {
            XposedBridge.log(getClass().getSimpleName() + " -> " + object.toString());
        }
        //}
    }

    void log(String str, Object... object) {
        log(String.format(str, object));
    }

    int getId(String name) {
        try {
            return XposedHelpers.getStaticIntField(findClassInQQ("com.tencent.mobileqq.R$id"), name);
        } catch (Exception e) {
            log("%s Can't find the id of name: %s", getQQ_Version(), name);
        }
        return 0;
    }

    int getDrawableId(String name) {
        try {
            return XposedHelpers.getStaticIntField(findClassInQQ("com.tencent.mobileqq.R$drawable"), name);
        } catch (Exception e) {
            log("%s Can't find the drawable of name: %s", getQQ_Version(), name);
        }
        return 0;
    }

    void removeView(int id, boolean isHide) {
        if (id != 0 && isHide) {
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
    }

    void removeDrawable(int id, boolean isHide) {
        if (id != 0 && isHide) {
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
    }

    Field findField(Class<?> clazz, String name) {
        return XposedHelpers.findFieldIfExists(clazz, name);
    }

    Field findFirstFieldByExactType(Class<?> clazz, Class<?> type) {
        Class<?> clz = clazz;
        do {
            for (Field field : clz.getDeclaredFields()) {
                if (field.getType() == type) {
                    field.setAccessible(true);
                    return field;
                }
            }
        } while ((clz = clz.getSuperclass()) != null);
        log("Can't find the field of type: " + type.getName() + " in class " + clazz.getName());
        return null;
    }

    Field findField(Class<?> clazz, Class<?> type, String name) {
        Class<?> clz = clazz;
        do {
            for (Field field : clz.getDeclaredFields()) {
                if (field.getType() == type && field.getName().equals(name)) {
                    field.setAccessible(true);
                    return field;
                }
            }
        } while ((clz = clz.getSuperclass()) != null);
        log("Can't find the field of type: " + type.getName() + " and name: " + name + " in class " + clazz.getName());
        return null;
    }

    Class<?> findClass(String className, ClassLoader classLoader) {
        try {
            return XposedHelpers.findClass(className, classLoader);
        } catch (Exception e) {
            log("Can't find the Class of name: " + className);
            return null;
        }
    }

    void findAndHookMethod(Class<?> clazz, String methodName, Object... parameterTypesAndCallback) {
        if (clazz == null || methodName.equals("") || parameterTypesAndCallback.length == 0 || !(parameterTypesAndCallback[parameterTypesAndCallback.length - 1] instanceof XC_MethodHook)) {
            return;
        }
        for (Object obj : parameterTypesAndCallback) {
            if (obj == null) {
                return;
            }
        }
        try {
            XposedHelpers.findAndHookMethod(clazz, methodName, parameterTypesAndCallback);
        } catch (Exception e) {
            log(e);
        }
    }

    void findAndHookMethod(String className, ClassLoader classLoader, String methodName, Object... parameterTypesAndCallback) {
        findAndHookMethod(findClass(className, classLoader), methodName, parameterTypesAndCallback);
    }

    void findAndHookConstructor(Class<?> clazz, Object... parameterTypesAndCallback) {
        if (clazz == null || parameterTypesAndCallback.length == 0 || !(parameterTypesAndCallback[parameterTypesAndCallback.length - 1] instanceof XC_MethodHook)) {
            return;
        }
        for (Object obj : parameterTypesAndCallback) {
            if (obj == null) {
                return;
            }
        }
        try {
            XposedHelpers.findAndHookConstructor(clazz, parameterTypesAndCallback);
        } catch (Exception e) {
            log(e);
        }
    }

    void findAndHookConstructor(String className, ClassLoader classLoader, Object... parameterTypesAndCallback) {
        findAndHookConstructor(findClass(className, classLoader), parameterTypesAndCallback);
    }

    boolean getBool(String key) {
        return getPref().getBoolean(key, false);
    }

    XSharedPreferences getPref() {
        XSharedPreferences preferences = xSharedPreferences.get();
        if (preferences == null) {
            preferences = new XSharedPreferences(BuildConfig.APPLICATION_ID);
            preferences.makeWorldReadable();
            xSharedPreferences = new WeakReference<>(preferences);
        } else {
            preferences.reload();
        }
        return preferences;
    }
}
