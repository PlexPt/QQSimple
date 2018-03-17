package me.zpp0196.qqsimple.hook;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static me.zpp0196.qqsimple.Common.getQQVersion;

/**
 * Created by zpp0196 on 2018/3/18.
 */

public class BaseHook {

    private ClassLoader classLoader;
    private Class<?> id;
    private Class<?> drawable;

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setId(Class<?> id) {
        this.id = id;
    }

    public void setDrawable(Class<?> drawable) {
        this.drawable = drawable;
    }

    protected Class<?> getClass(String className) {
        if(classLoader != null && !className.equals("")) {
            try {
                return classLoader.loadClass(className);
            } catch (ClassNotFoundException e) {
                XposedBridge.log(String.format("%s can not get className: %s", getQQ_Version(), className));
            }
        }
        return null;
    }

    protected int getId(String idName) {
        if (id != null && !idName.equals("")) {
            try {
                return XposedHelpers.getStaticIntField(id, idName);
            } catch (Exception e) {
                XposedBridge.log(String.format("%s not found field: %s", getQQ_Version(), idName));
            }
        }
        return 0;
    }

    protected int getDrawableId(String drawableName) {
        if (drawable != null && !drawableName.equals("")) {
            try {
                return XposedHelpers.getStaticIntField(drawable, drawableName);
            } catch (Exception e) {
                XposedBridge.log(String.format("%s not found field: %s", getQQ_Version(), drawableName));
            }
        }
        return 0;
    }

    protected <T> T findField(Class<?> clazz, String type, String name, XC_MethodHook.MethodHookParam param){
        if(clazz != null && !type.equals("") && !name.equals("")) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getGenericType().toString().contains(type) && field.getName().equals(name)) {
                    try {
                        return (T) field.get(param.thisObject);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    protected void remove(int id, boolean isHide) {
        if(id != 0) {
            XposedHelpers.findAndHookMethod(View.class, "setLayoutParams", ViewGroup.LayoutParams.class, new XC_MethodHook() {
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

    protected void removeDrawable(int id, boolean isHide) {
        if(id != 0) {
            XposedHelpers.findAndHookMethod(ImageView.class, "setImageResource", int.class, new XC_MethodHook() {
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

    protected void findAndHookMethod(Class<?> clazz, String methodName, Object... parameterTypesAndCallback) {
        if (clazz != null) {
            try {
                XposedHelpers.findAndHookMethod(clazz, methodName, parameterTypesAndCallback);
            } catch (Exception e) {
                XposedBridge.log(e);
            }
        }
    }

    /**
     * 获取 QQ 版本
     *
     * @return
     */
    public static String getQQ_Version() {
        Context context = (Context) XposedHelpers.callMethod(XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread", new Object[0]), "getSystemContext", new Object[0]);
        return getQQVersion(context);
    }
}
