package me.zpp0196.qqpurify.pro;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import me.zpp0196.reflectx.proxy.BaseProxyClass;
import me.zpp0196.reflectx.proxy.ProxyClassImpl;
import me.zpp0196.reflectx.proxy.Source;

@ProxyClassImpl
public class ProguardProxyClass extends BaseProxyClass {

    public static final long TYPE_CLASS = 0;
    public static final long TYPE_FIELD = 1;
    public static final long TYPE_METHOD = 2;

    @Override
    protected Field exactField(Class<?> fieldType, String fieldName) {
        return super.exactField(fieldType, handleProguard(fieldName, TYPE_FIELD));
    }

    @Override
    protected Method exactMethod(Class<?> returnType, String methodName, Class<?>... parameterTypes) {
        return super.exactMethod(returnType, handleProguard(methodName, TYPE_METHOD), parameterTypes);
    }

    private String handleProguard(String original, long type) {
        ProguardMappingFactory mapping = ProguardMappingFactory.Companion.getInstance();
        if (mapping == null) {
            // IApplication
            return original;
        }
        return mapping.getSource(original, getSource(), type);
    }

    private String getSource() {
        Class<?> clazz = getProxyInterface();
        String value = clazz.getName();
        Source source = clazz.getAnnotation(Source.class);
        if (source != null) {
            value = source.value();
        }
        return value;
    }
}
