package me.zpp0196.qqsimple.hook;

import de.robv.android.xposed.XposedHelpers;
import me.zpp0196.qqsimple.util.SettingUtils;

import static me.zpp0196.qqsimple.hook.RemoveImagine.remove;

/**
 * Created by zpp0196 on 2018/3/11.
 */

public class SideBarHook {

    private Class<?> id;

    public SideBarHook(Class id) {
        this.id = id;
    }

    /**
     * 隐藏侧边栏
     */
    public void hideSidebar() {
        if (SettingUtils.getValueHideSidebarVip()) {
            remove(getLayoutId("myvip"));
        }
        if (SettingUtils.getValueHideSidebarPocket()) {
            remove(getLayoutId("mypocket"));
        }
        if (SettingUtils.getValueHideSidebarDressUp()) {
            remove(getLayoutId("myDressup"));
        }
        if (SettingUtils.getValueHideSidebarMyFavorites()) {
            remove(getLayoutId("myfavorites"));
        }
        if (SettingUtils.getValueHideSidebarMyPhotos()) {
            remove(getLayoutId("myphotos"));
        }
        if (SettingUtils.getValueHideSidebarMyFiles()) {
            remove(getLayoutId("myfiles"));
        }
        if (SettingUtils.getValueHideSidebarMyVideos()) {
            remove(getLayoutId("myvideos"));
        }
        if (SettingUtils.getValueHideSidebarMyCards()) {
            remove(getLayoutId("mycards"));
        }
        if (SettingUtils.getValueHideSidebarFreeFlow()) {
            remove(getLayoutId("cuKingCard"));
        }
        if (SettingUtils.getValueHideSidebarMyDaily()) {
            remove(getLayoutId("mydaily"));
        }
        if (SettingUtils.getValueHideSidebarMyQrCode()) {
            remove(getLayoutId("qr_code_icon"));
        }
        if (SettingUtils.getValueHideSidebarThemeNight()) {
            remove(getLayoutId("nightmode"));
        }
        if (SettingUtils.getValueHideSidebarCityWeather()) {
            remove(getLayoutId("weather_layout"));
        }
        if (SettingUtils.getValueHideSidebarMyCity()) {
            remove(getLayoutId("weather_area"));
        }
    }

    private int getLayoutId(String layoutName) {
        return XposedHelpers.getStaticIntField(id, layoutName);
    }
}
