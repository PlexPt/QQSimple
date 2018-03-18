package me.zpp0196.qqsimple.hook;

import android.content.Context;
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

class BaseHook {

    private ClassLoader classLoader;
    private Class<?> id;
    private Class<?> drawable;
    private WeakReference<XSharedPreferences> xSharedPreferences = new WeakReference<>(null);

    /**
     * 获取 QQ 版本
     *
     * @return
     */
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

    void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    void setId(Class<?> id) {
        this.id = id;
    }

    void setDrawable(Class<?> drawable) {
        this.drawable = drawable;
    }

    Class<?> getClass(String className) {
        if (classLoader != null && !className.equals("")) {
            try {
                return classLoader.loadClass(className);
            } catch (ClassNotFoundException e) {
                XposedBridge.log(String.format("%s can not get className: %s", getQQ_Version(), className));
            }
        }
        return null;
    }

    int getId(String idName) {
        if (id != null && !idName.equals("")) {
            try {
                return XposedHelpers.getStaticIntField(id, idName);
            } catch (Exception e) {
                XposedBridge.log(String.format("%s not found field: %s", getQQ_Version(), idName));
            }
        }
        return 0;
    }

    int getDrawableId(String drawableName) {
        if (drawable != null && !drawableName.equals("")) {
            try {
                return XposedHelpers.getStaticIntField(drawable, drawableName);
            } catch (Exception e) {
                XposedBridge.log(String.format("%s not found field: %s", getQQ_Version(), drawableName));
            }
        }
        return 0;
    }

    <T> T findField(Class<?> clazz, String type, String name, XC_MethodHook.MethodHookParam param) {
        return findField(clazz, type, name, param.thisObject);
    }

    <T> T findField(Class<?> clazz, String type, String name, Object object) {
        Field field = findField(clazz, type, name);
        if (field != null) {
            try {
                return (T) field.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    Field findField(Class<?> clazz, String type, String name) {
        if (clazz != null && !type.equals("") && !name.equals("")) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getGenericType().toString().contains(type) && field.getName().equals(name)) {
                    return field;
                }
            }
        }
        return null;
    }

    void remove(int id, boolean isHide) {
        if (id != 0) {
            findAndHookMethod(View.class, "setLayoutParams", ViewGroup.LayoutParams.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    View view = (View) param.thisObject;
                    if (isHide && view.getId() == id) {
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
        if (id != 0) {
            findAndHookMethod(ImageView.class, "setImageResource", int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    if (isHide && (int) param.args[0] == id) {
                        param.setResult(null);
                    }
                }
            });
        }
    }

    void findAndHookMethod(Class<?> clazz, String methodName, Object... parameterTypesAndCallback) {
        if (clazz != null) {
            for (Object object : parameterTypesAndCallback) {
                if (object == null) {
                    return;
                }
            }
            try {
                XposedHelpers.findAndHookMethod(clazz, methodName, parameterTypesAndCallback);
            } catch (Exception e) {
                XposedBridge.log(e);
            }
        }
    }

    boolean getBool(String key) {
        return getPref().getBoolean(key, false);
    }

    String getString(String key, String defValue) {
        return getPref().getString(key, defValue);
    }

    private XSharedPreferences getPref() {
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
