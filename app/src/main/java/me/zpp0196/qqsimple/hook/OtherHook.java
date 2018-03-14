package me.zpp0196.qqsimple.hook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static me.zpp0196.qqsimple.hook.RemoveImagine.remove;

/**
 * Created by zpp0196 on 2018/3/11.
 */

public class OtherHook {

    private ClassLoader classLoader;
    private Class<?> drawable;
    private Class<?> id;

    public OtherHook(ClassLoader classLoader, Class<?> drawable, Class id) {
        this.classLoader = classLoader;
        this.drawable = drawable;
        this.id = id;
    }

    /**
     * 完全关闭动画
     */
    public void closeAllAnimation() {
        XposedHelpers.findAndHookMethod(Activity.class, "startActivityForResult", Intent.class, int.class, Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                ((Intent) param.args[0]).putExtra("open_chatfragment_withanim", false);
            }
        });
        XposedHelpers.findAndHookMethod(Activity.class, "startActivity", Intent.class, Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                ((Intent) param.args[0]).putExtra("open_chatfragment_withanim", false);
            }
        });
    }

    /**
     * 隐藏部分小红点
     */
    public void hideRedDot() {
        remove(getId("find_reddot"));
        remove(getId("item_right_reddot"));
        remove(getId("iv_reddot"));
        remove(getId("qzone_feed_reddot"));
        remove(getId("qzone_mood_reddot"));
        remove(getId("qzone_super_font_tab_reddot"));
        remove(getId("qzone_uploadphoto_item_reddot"));
        remove(getId("tv_reddot"));
        remove(getId("xingqu_buluo_reddot"));
        ArrayList<Integer> RedTouchId = new ArrayList<>();
        RedTouchId.add(getDrawableId("skin_tips_dot"));
        RedTouchId.add(getDrawableId("skin_tips_dot_small"));
        RedTouchId.add(getDrawableId("skin_tips_new"));
        if (MainHook.getQQ_Version().compareTo("7.3.5") > 0) {
            RedTouchId.add(getDrawableId("shortvideo_redbag_outicon"));
        }
        for (int i = 0; i < RedTouchId.size(); i++) {
            RemoveImagine.removeDrawable(Integer.parseInt(RedTouchId.get(i).toString()), getDrawableId("skin_searchbar_button_pressed_theme_version2"));
        }
    }

    /**
     * 隐藏设置免流量特权
     */
    public void hideSettingFreeFlow() {
        Class<?> QQSettingSettingActivity = getClass("com.tencent.mobileqq.activity.QQSettingSettingActivity");
        XposedHelpers.findAndHookMethod(QQSettingSettingActivity, "a", XC_MethodReplacement.returnConstant(null));
    }

    /**
     * 隐藏我的文件里的 TIM 推广
     */
    public void hideTimInMyFile() {
        remove(getId("timtips"));
    }

    /**
     * 隐藏空间绿厂广告
     */
    public void hideQzoneAd() {
        remove(getId("shuoshuo_ad_upload_quality"));
        remove(getId("quality_hd_ad"));
        remove(getId("quality_ad"));
    }

    private Class<?> getClass(String className) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            XposedBridge.log(e);
        }
        return null;
    }

    private int getDrawableId(String drawableName) {
        return XposedHelpers.getStaticIntField(drawable, drawableName);
    }

    private int getId(String idName) {
        return XposedHelpers.getStaticIntField(id, idName);
    }
}
