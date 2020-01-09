package proxy.android.app;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Method;

import me.zpp0196.reflectx.proxy.MethodGetter;
import me.zpp0196.reflectx.proxy.SourceClass;

@SourceClass(Application.class)
public interface IApplication {
    @MethodGetter(parameterTypes = Context.class)
    Method attach();
}
