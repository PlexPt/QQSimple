package me.zpp0196.qqsimple.hook.base;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import me.zpp0196.qqsimple.hook.util.Util;
import me.zpp0196.qqsimple.hook.util.XPrefs;

import static me.zpp0196.qqsimple.hook.comm.Classes.R$drawable;
import static me.zpp0196.qqsimple.hook.comm.Classes.R$id;

/**
 * Created by zpp0196 on 2018/3/18.
 */

public abstract class BaseHook {

    protected void log(String msg){
        Util.log(getClass(), msg);
    }

    protected void log(String format, Object... args){
        Util.log(getClass(), format, args);
    }

    private int getIdInQQ(String name) {
        try {
            return XposedHelpers.getStaticIntField(R$id, name);
        } catch (Throwable e) {
            log("%s Can't find the id of name: %s!", Util.getQQVersion(), name);
        }
        return 0;
    }

    private int getDrawableIdInQQ(String name) {
        try {
            return XposedHelpers.getStaticIntField(R$drawable, name);
        } catch (Throwable e) {
            log("%s Can't find the drawable of name: %s!", Util.getQQVersion(), name);
        }
        return 0;
    }

    protected void hideView(String name, String key) {
        hideView(getIdInQQ(name), getBool(key));
    }

    protected void hideView(String name, boolean isHide) {
        hideView(getIdInQQ(name), isHide);
    }

    protected void hideView(String name) {
        hideView(getIdInQQ(name));
    }

    private void hideView(int id, boolean isHide) {
        if (isHide) hideView(id);
    }

    private void hideView(int id) {
        if (id == 0) return;
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

    protected void hideDrawable(String name) {
        hideDrawable(getDrawableIdInQQ(name));
    }

    @SuppressWarnings("all")
    protected void hideDrawable(String name, boolean isHide) {
        if(isHide) hideDrawable(getDrawableIdInQQ(name));
    }

    private void hideDrawable(int id) {
        if (id == 0) return;
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
        log("%s Can't find the field of type: %s in class: %s!", Util.getQQVersion(), type.getName(), clazz.getName());
        return null;
    }

    @SuppressWarnings("all")
    protected <T> T findObject(@NonNull Object obj, Class<?> type, String name){
        try {
            return (T)findField(obj.getClass(), type, name).get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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
        log("%s Can't find the field of type: %s and name: %s in class: %s!", Util.getQQVersion(), type.getName(), name, clazz.getName());
        return null;
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
            Util.log(e);
        }
    }

    protected boolean getBool(String key) {
        return XPrefs.getPref().getBoolean(key, false);
    }

    protected boolean getBool(String... keys) {
        for (String key : keys) {
            if (getBool(key)) return true;
        }
        return false;
    }
}
