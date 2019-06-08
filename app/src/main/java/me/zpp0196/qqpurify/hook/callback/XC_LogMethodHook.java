package me.zpp0196.qqpurify.hook.callback;

import me.zpp0196.library.xposed.XLog;
import me.zpp0196.library.xposed.XMethodHook;

/**
 * Created by zpp0196 on 2019/5/04.
 */
public abstract class XC_LogMethodHook extends XMethodHook.Callback {

    public XC_LogMethodHook() {
    }

    public XC_LogMethodHook(int priority) {
        super(priority);
    }

    @Override
    protected void before(XMethodHook.MethodParam param) {
        String clazzName = param.method.getDeclaringClass().getSimpleName();
        XLog.v(clazzName, "enter: " + param.method);
    }

    @Override
    protected void after(XMethodHook.MethodParam param) {
        String clazzName = param.method.getDeclaringClass().getSimpleName();
        XLog.v(clazzName, "exit: " + param.method);
    }

    public static XMethodHook.Callback intercept() {
        return replace(null);
    }

    public static XMethodHook.Callback replace(final Object constant) {
        return new XMethodHook.Callback() {
            @Override
            protected void before(XMethodHook.MethodParam param) {
                String clazzName = param.method.getDeclaringClass().getSimpleName();
                XLog.v(clazzName, "intercept: " + param.method);
                param.setResult(constant);
            }
        };
    }
}
