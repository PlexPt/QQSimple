package me.zpp0196.qqpurify.hook.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.zpp0196.qqpurify.utils.SettingUtils;

/**
 * Created by zpp0196 on 2019/5/21.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface VersionSupport {

    /**
     * @return 指定key值所在的group
     */
    SettingUtils.ISetting.SettingGroup group() default SettingUtils.ISetting.SettingGroup.empty;

    /**
     * @return 支持的最小版本
     */
    long min() default 0;

    /**
     * @return 支持的最大版本
     */
    long max() default Long.MAX_VALUE;

    /**
     * @return 不支持的版本
     */
    long[] exclude() default -1;
}
