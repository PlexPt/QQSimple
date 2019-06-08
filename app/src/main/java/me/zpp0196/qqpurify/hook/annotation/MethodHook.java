package me.zpp0196.qqpurify.hook.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zpp0196 on 2019/5/21.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodHook {

    /**
     * @return 对应的key值，默认为方法名
     */
    String key() default "";

    /**
     * @return 方法描述
     */
    String desc();
}
