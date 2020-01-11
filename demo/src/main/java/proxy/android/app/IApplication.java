package proxy.android.app;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Method;

import reflectx.IProxyClass;
import reflectx.annotations.FindMethod;
import reflectx.annotations.SourceClass;

@SourceClass(Application.class)
public interface IApplication extends IProxyClass {
    @FindMethod(parameterTypes = Context.class)
    Method attach();
}
