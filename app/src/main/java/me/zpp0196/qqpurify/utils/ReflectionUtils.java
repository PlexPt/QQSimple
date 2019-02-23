package me.zpp0196.qqpurify.utils;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import de.robv.android.xposed.XposedHelpers;

/**
 * Created by zpp0196 on 2019/1/24 0024.
 */

public class ReflectionUtils {

    public static <T> T getStaticObjectIfExists(Class<?> clazz, Class<?> fieldType, String fieldName) {
        return getObjectIfExists(clazz, fieldType, fieldName, null);
    }

    @SuppressWarnings ("unchecked")
    public static <T> T getObjectIfExists(Class<?> clazz, Class<?> fieldType, String fieldName, Object obj) {
        return getObjectIfExists(clazz, fieldType.getName(), fieldName, obj);
    }

    @SuppressWarnings ("unchecked")
    public static <T> T getObjectIfExists(Class<?> clazz, String typeName, String fieldName, Object obj) {
        try {
            Field field = findFieldIfExists(clazz, typeName, fieldName);
            return field == null ? null : (T) field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    public static void setStaticObjectField(Class<?> clazz, Class<?> fieldType, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        findFieldIfExists(clazz, fieldType, fieldName).set(null, value);
    }

    public static void setObjectField(Object obj, Class<?> fieldType, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        if (obj != null) {
            Field field = findFieldIfExists(obj.getClass(), fieldType, fieldName);
            if (field != null) {
                field.set(obj, value);
            }
        }
    }

    public static Field findFieldIfExists(Class<?> clazz, Class<?> fieldType, String fieldName)
            throws NoSuchFieldException {
        return findFieldIfExists(clazz, fieldType.getName(), fieldName);
    }

    public static Field findFieldIfExists(Class<?> clazz, String typeName, String fieldName)
            throws NoSuchFieldException {
        if (clazz != null && !TextUtils.isEmpty(typeName) && !TextUtils.isEmpty(fieldName)) {
            Class<?> clz = clazz;
            do {
                for (Field field : clz.getDeclaredFields()) {
                    if (field.getType()
                                .getName()
                                .equals(typeName) && field.getName()
                                .equals(fieldName)) {
                        field.setAccessible(true);
                        return field;
                    }
                }
            } while ((clz = clz.getSuperclass()) != null);
            throw new NoSuchFieldException(clazz.getName() + "#" + typeName + " " + fieldName);
        }
        return null;
    }

    public static Method findMethodIfExists(Class<?> clazz, Class<?> returnType, String methodName, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        if (clazz != null && returnType != null && !TextUtils.isEmpty(methodName)) {
            Class<?> clz = clazz;
            do {
                Method[] methods = XposedHelpers.findMethodsByExactParameters(clazz, returnType, parameterTypes);
                for (Method method : methods) {
                    if (method.getName()
                            .equals(methodName)) {
                        return method;
                    }
                }
            } while ((clz = clz.getSuperclass()) != null);
            throw new NoSuchMethodException(
                    clazz.getName() + "#" + returnType.getName() + " " + methodName + "(" +
                    Arrays.toString(parameterTypes) + ")");
        }
        return null;
    }
}
