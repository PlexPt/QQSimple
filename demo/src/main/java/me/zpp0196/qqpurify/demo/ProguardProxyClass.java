package me.zpp0196.qqpurify.demo;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import proxy.com.tencent.mobileqq.activity.IConversation;

import reflectx.BaseProxyClass;
import reflectx.Reflectx;
import reflectx.annotations.ProxyClassImpl;

@ProxyClassImpl
public class ProguardProxyClass extends BaseProxyClass {

    private enum Type {
        FIELD, METHOD
    }

    @Override
    protected Field field(Class<?> fieldType, @NotNull String fieldName) {
        return super.field(fieldType, handleProguard(fieldName, Type.FIELD));
    }

    @Override
    protected Method method(Class<?> returnType, @NotNull String methodName, Class<?>... parameterTypes) {
        return super.method(returnType, handleProguard(methodName, Type.METHOD), parameterTypes);
    }

    private String handleProguard(String name, Type type) {
        if (getProxyClass() == IConversation.class) {
            if (type == Type.METHOD) {
                if ("initMiniAppEntryLayout".equals(name)) {
                    long proguardVersion = Reflectx.getProguardVersion();
                    if (proguardVersion == 1296) {
                        return "L";
                    } else if (proguardVersion == 1320) {
                        return "M";
                    }
                }
            }
        }
        return name;
    }
}
