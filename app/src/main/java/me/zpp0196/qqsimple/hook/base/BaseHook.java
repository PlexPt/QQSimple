package me.zpp0196.qqsimple.hook.base;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import me.zpp0196.qqsimple.hook.comm.Ids;
import me.zpp0196.qqsimple.hook.util.HookUtil;
import me.zpp0196.qqsimple.hook.util.XPrefs;

import static de.robv.android.xposed.XposedHelpers.getStaticIntField;
import static me.zpp0196.qqsimple.hook.comm.Classes.R$drawable;
import static me.zpp0196.qqsimple.hook.comm.Classes.R$id;
import static me.zpp0196.qqsimple.hook.util.HookUtil.getQQVersionName;

/**
 * Created by zpp0196 on 2018/3/18.
 */

public abstract class BaseHook {

    public abstract void init();

    protected void log(String msg) {
        HookUtil.log(getClass(), msg);
    }

    protected void log(String format, Object... args) {
        HookUtil.log(getClass(), format, args);
    }

    protected int getIdInQQ(String name) {
        Integer id = Ids.getId(name);
        if (id != null && id != 0) {
            return id;
        }
        try {
            return getStaticIntField(R$id, name);
        } catch (Throwable e) {
            log("%s Can't find the id of name: %s!", getQQVersionName(), name);
        }
        return 0;
    }

    protected int getDrawableIdInQQ(String name) {
        Integer id = Ids.getId(name);
        if (id != null && id != 0) {
            return id;
        }
        try {
            return getStaticIntField(R$drawable, name);
        } catch (Throwable e) {
            log("%s Can't find the drawable of name: %s!", getQQVersionName(), name);
        }
        return 0;
    }

    protected void hideView(String name) {
        hideView(getIdInQQ(name));
    }

    protected void hideView(String name, String... key) {
        hideView(getIdInQQ(name), key);
    }

    private void hideView(int id, String... key) {
        if (id == 0) {
            return;
        }
        findAndHookMethod(View.class, "setLayoutParams", ViewGroup.LayoutParams.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                View view = (View) param.thisObject;
                if (view.getId() == id) {
                    if (!getBool(key)) {
                        return;
                    }
                    ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) param.args[0];
                    layoutParams.height = 1;
                    layoutParams.width = 0;
                    view.setVisibility(View.GONE);
                }
            }
        });
    }

    protected void hideDrawable(String name) {
        hideDrawable(getDrawableIdInQQ(name));
    }

    protected void hideDrawable(String name, String... key) {
        hideDrawable(getDrawableIdInQQ(name), key);
    }

    private void hideDrawable(int id, String... key) {
        if (id == 0) {
            return;
        }
        findAndHookMethod(ImageView.class, "setImageResource", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if ((int) param.args[0] == id) {
                    if (!getBool(key)) {
                        return;
                    }
                    param.setResult(null);
                }
            }
        });
    }

    protected Field findFirstFieldByExactType(@NonNull Class<?> clazz, Class<?> type) {
        if (type == null) {
            return null;
        }
        Class<?> clz = clazz;
        do {
            for (Field field : clz.getDeclaredFields()) {
                if (field.getType() == type) {
                    field.setAccessible(true);
                    return field;
                }
            }
        } while ((clz = clz.getSuperclass()) != null);
        log("%s Can't find the field of type: %s in class: %s!", getQQVersionName(), type.getName(), clazz.getName());
        return null;
    }

    @SuppressWarnings ("unchecked")
    protected <T> T getObject(@NonNull Object obj, @NonNull Class<?> type, String name) {
        try {
            return (T) findField(obj.getClass(), type, name).get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Field findField(@NonNull Class<?> clazz, Class<?> type, String name) {
        if (type == null) {
            return null;
        }
        Class<?> clz = clazz;
        do {
            for (Field field : clz.getDeclaredFields()) {
                if (field.getType() == type && field.getName()
                        .equals(name)) {
                    field.setAccessible(true);
                    return field;
                }
            }
        } while ((clz = clz.getSuperclass()) != null);
        log("%s Can't find the field of type: %s and name: %s in class: %s!", getQQVersionName(), type.getName(), name, clazz.getName());
        return null;
    }

    protected void findAndHookMethod(Class<?> clazz, String methodName, Object... parameterTypesAndCallback) {
        if (clazz == null || methodName.equals("") || parameterTypesAndCallback.length == 0 ||
            !(parameterTypesAndCallback[parameterTypesAndCallback.length -
                                        1] instanceof XC_MethodHook)) {
            return;
        }
        for (Object obj : parameterTypesAndCallback) {
            if (obj == null) {
                return;
            }
        }
        try {
            XposedHelpers.findAndHookMethod(clazz, methodName, parameterTypesAndCallback);
        } catch (Throwable e) {
            HookUtil.log(e);
        }
    }

    protected void findAndHookConstructor(Class<?> clazz, Object... parameterTypesAndCallback) {
        if (clazz == null || parameterTypesAndCallback.length == 0 ||
            !(parameterTypesAndCallback[parameterTypesAndCallback.length -
                                        1] instanceof XC_MethodHook)) {
            return;
        }
        for (Object obj : parameterTypesAndCallback) {
            if (obj == null) {
                return;
            }
        }
        try {
            XposedHelpers.findAndHookConstructor(clazz, parameterTypesAndCallback);
        } catch (Throwable e) {
            HookUtil.log(e);
        }
    }

    protected XC_MethodHook replaceNull(String... key) {
        return replaceObj(null, key);
    }

    protected XC_MethodHook replaceFalse(String... key) {
        return replaceObj(false, key);
    }

    protected XC_MethodHook replaceObj(Object result, String... key) {
        return new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                if (getBool(key)) {
                    param.setResult(result);
                }
            }
        };
    }

    protected boolean getBool(String key) {
        return XPrefs.getBoolean(key);
    }

    protected boolean getBool(String... keys) {
        if (keys == null || keys.length == 0) {
            return true;
        }
        for (String key : keys) {
            if (getBool(key)) {
                return true;
            }
        }
        return false;
    }
}
