package me.zpp0196.qqpurify.hook.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.XposedHelpers;
import me.zpp0196.library.xposed.XConstructor;
import me.zpp0196.library.xposed.XField;
import me.zpp0196.library.xposed.XLog;
import me.zpp0196.library.xposed.XMethodHook;
import me.zpp0196.qqpurify.hook.annotation.MethodHook;
import me.zpp0196.qqpurify.hook.annotation.VersionSupport;
import me.zpp0196.qqpurify.hook.callback.XC_LogMethodHook;
import me.zpp0196.qqpurify.hook.utils.QQDialogUtils;
import me.zpp0196.qqpurify.utils.Constants;
import me.zpp0196.qqpurify.hook.utils.QQClasses;
import me.zpp0196.qqpurify.utils.Setting;
import me.zpp0196.qqpurify.utils.SettingUtils;
import me.zpp0196.qqpurify.utils.Utils;

/**
 * Created by zpp0196 on 2018/3/18.
 */
public abstract class BaseHook implements SettingUtils.ISetting, Constants, QQClasses {

    private static final Map<String, Class<?>> mClassMap = new HashMap<>();
    protected static long mQQVersionCode = -1;

    private ClassLoader mClassLoader;
    protected Setting mSetting;
    protected Context mContext;

    public BaseHook(Context context) {
        this.mContext = context;
        this.mClassLoader = context.getClassLoader();
        this.mSetting = Setting.getInstance(this);

        if (mQQVersionCode < 0) {
            mQQVersionCode = Utils.getAppVersionCode(context, PACKAGE_NAME_QQ);
        }
    }

    public void init() {
        invokeHookClassMethod(this);
    }

    private Object getSettingValue(Method method) {
        if (!Modifier.isPublic(method.getModifiers()) ||
                !method.isAnnotationPresent(MethodHook.class)) {
            return null;
        }

        Setting setting = mSetting;
        if (method.isAnnotationPresent(VersionSupport.class)) {
            VersionSupport vs = method.getAnnotation(VersionSupport.class);
            if (vs.group() != SettingGroup.empty) {
                setting = Setting.getInstance(vs.group());
            }

            if (mQQVersionCode < vs.min() || mQQVersionCode >= vs.max()) {
                return null;
            }

            boolean isExclude = false;
            for (long version : vs.exclude()) {
                if (version == mQQVersionCode) {
                    isExclude = true;
                }
            }
            if (isExclude) {
                return null;
            }
        }

        MethodHook methodHook = method.getAnnotation(MethodHook.class);
        String key = methodHook.key();
        if (key.isEmpty()) {
            key = method.getName();
        }

        Object value = setting.get(key);
        XLog.v(getTAG(), String.format("key: %s, value: %s", key, value));
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean && !(boolean) value) {
            return null;
        }
        value = value instanceof JSONArray ? Utils.jArray2SList((JSONArray) value) : value;
        return value;
    }

    private void invokeHookClassMethod(BaseHook impl) {
        Method[] methods = impl.getClass().getMethods();
        for (Method method : methods) {
            Object value = getSettingValue(method);
            if (value == null) {
                continue;
            }
            MethodHook methodHook = method.getAnnotation(MethodHook.class);
            try {
                if (value instanceof Boolean) {
                    method.invoke(impl);
                } else {
                    method.invoke(impl, value);
                }
                XLog.v(impl.getTAG(), "invoke: " + method);
                XLog.d(impl.getTAG(), String.format("Loading %s success", methodHook.desc()));
            } catch (Exception e) {
                String message = String.format("加载\"%s\"功能失败", methodHook.desc());
                QQDialogUtils.addError(new Exception(message));
                XLog.e(impl.getTAG(), message, e);
            }
        }
    }

    protected Class<?> $(String className) {
        if (mClassMap.containsKey(className)) {
            return mClassMap.get(className);
        }
        try {
            Class<?> clazz = XposedHelpers.findClass(className, mClassLoader);
            mClassMap.put(className, clazz);
            return clazz;
        } catch (Throwable th) {
            XLog.e(getTAG(), th);
            return null;
        }
    }

    protected void hideView(View view) {
        if (view == null) {
            return;
        }
        view.setVisibility(View.GONE);
        if (view.getLayoutParams() == null) {
            return;
        }
        Class<?> lpClass = view.getLayoutParams().getClass();
        ViewGroup.LayoutParams lp = XConstructor.create(lpClass).instance(0, 0);
        view.setLayoutParams(lp);
    }

    @SuppressWarnings("SameParameterValue")
    protected void hideViewAfterCreateTab(String frame, Class<?> viewType, String viewName) {
        doSthAfterTabCreated(frame, f -> hideView(XField.create(f).exact(viewType, viewName).get()));
    }

    protected void doSthAfterTabCreated(String frame, OnTabCreated onTabCreated) {
        XMethodHook.create($(FrameFragment)).method("createTabContent").hook(new XC_LogMethodHook() {
            @Override
            protected void after(XMethodHook.MethodParam param) {
                super.after(param);
                if (!param.args(0, String.class).equals(frame)) {
                    return;
                }
                Map map = XField.create(param).exact(Map.class, "a").get();
                onTabCreated.operation(map.get(frame));
            }
        });
    }

    protected String getString(String key) {
        return mSetting.get(key, "");
    }

    protected String getTAG() {
        return APP_NAME + ": " + getClass().getSimpleName();
    }

    protected interface OnTabCreated {
        void operation(Object frame);
    }
}
