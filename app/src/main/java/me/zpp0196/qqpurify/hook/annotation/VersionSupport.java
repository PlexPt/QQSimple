package me.zpp0196.qqpurify.hook.annotation;

import android.annotation.SuppressLint;

import androidx.annotation.IntDef;

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

    @SuppressLint("ShiftFlags")
    interface QQVersions {
        int QQ_760 = 832;
        int QQ_776 = 899;
        int QQ_795 = 980;
        int QQ_800 = 1024;
        int QQ_805 = 1186;
    }

    @IntDef(flag = true, value = {
            QQVersions.QQ_760,
            QQVersions.QQ_776,
            QQVersions.QQ_795,
            QQVersions.QQ_800,
            QQVersions.QQ_805
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface QQVersion {
    }

    /**
     * @return 指定key值所在的group
     */
    @SettingUtils.ISetting.SettingGroup
    String group() default SettingUtils.ISetting.SETTING_DEFAULT;

    /**
     * @return 支持的最小版本
     */
    @QQVersion
    long min() default 0;

    /**
     * @return 支持的最大版本
     */
    @QQVersion
    long max() default Long.MAX_VALUE;

    /**
     * @return 不支持的版本
     */
    @QQVersion
    long[] exclude() default -1;
}
