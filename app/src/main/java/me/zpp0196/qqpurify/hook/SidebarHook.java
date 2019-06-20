package me.zpp0196.qqpurify.hook;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.zpp0196.library.xposed.XConstructor;
import me.zpp0196.library.xposed.XConstructorHook;
import me.zpp0196.library.xposed.XField;
import me.zpp0196.library.xposed.XMethod;
import me.zpp0196.library.xposed.XMethodHook;
import me.zpp0196.qqpurify.activity.MainActivity;
import me.zpp0196.qqpurify.hook.annotation.MethodHook;
import me.zpp0196.qqpurify.hook.annotation.VersionSupport;
import me.zpp0196.qqpurify.hook.base.BaseHook;
import me.zpp0196.qqpurify.hook.callback.XC_LogMethodHook;
import me.zpp0196.qqpurify.hook.utils.QQDialogUtils;
import me.zpp0196.qqpurify.utils.Setting;

import static me.zpp0196.qqpurify.BuildConfig.APPLICATION_ID;
import static me.zpp0196.qqpurify.BuildConfig.VERSION_NAME;
import static me.zpp0196.qqpurify.hook.utils.QQConfigUtils.findClass;
import static me.zpp0196.qqpurify.hook.utils.QQConfigUtils.getField;
import static me.zpp0196.qqpurify.utils.Utils.getAppVersionName;

/**
 * Created by zpp0196 on 2019/2/8.
 */
@SuppressWarnings("unused")
public class SidebarHook extends BaseHook {

    private boolean mHideDaily;
    private boolean mHideQrCode;
    private boolean mHideNightTheme;
    private boolean mHideCityWeather;
    private boolean mHideBack;
    private boolean mHideApollo;
    private List<String> mSidebarItems = new ArrayList<>();

    private View mModuleEntry;

    public SidebarHook(Context context) {
        super(context);
    }

    @Override
    public void init() {
        super.init();
        XConstructorHook.create($(QQSettingMe)).hook(new XC_LogMethodHook() {
            @Override
            protected void after(XMethodHook.MethodParam param) {
                super.after(param);
                XField settingMe = XField.create(param);
                // 打卡
                if (mHideDaily) {
                    hideView(settingMe.exact(LinearLayout.class, getField("sidebar_daily", "a")).get());
                }
                // 二维码
                if (mHideQrCode) {
                    hideView(settingMe.exact(ImageView.class, getField("sidebar_qrCode", "d")).get());
                }
                // 夜间模式
                if (mHideNightTheme) {
                    hideView(settingMe.exact(View.class, getField("sidebar_nightTheme", "d")).get());
                }
                // 城市天气
                if (mHideCityWeather) {
                    hideView(settingMe.exact(LinearLayout.class, getField("sidebar_cityWeather", "c")).get());
                }
                // 返回按钮
                if (mHideBack) {
                    hideView(settingMe.exact(ImageView.class, getField("sidebar_backBtn", "e")).get());
                }
                // 厘米秀
                if (mHideApollo) {
                    settingMe.exact(findClass(SettingMeApolloViewController), "a").set(null);
                }
                // 侧滑栏列表
                View[] items = XField.create(param).exact(View[].class, "a").get();
                for (String str : mSidebarItems) {
                    hideView(items[Integer.valueOf(str)]);
                }
            }
        });
    }

    @MethodHook(desc = "隐藏打卡")
    public void hideDaily() {
        this.mHideDaily = true;
    }

    @MethodHook(desc = "隐藏我的二维码")
    public void hideQrCode() {
        this.mHideQrCode = true;
    }

    @MethodHook(desc = "隐藏夜间模式")
    public void hideNightTheme() {
        this.mHideNightTheme = true;
    }

    @MethodHook(desc = "隐藏侧滑栏列表")
    public void hideSidebarItems(List<String> needHideItems) {
        this.mSidebarItems = needHideItems;
    }

    @MethodHook(desc = "隐藏城市天气")
    public void hideCityWeather() {
        this.mHideCityWeather = true;
    }

    @MethodHook(desc = "隐藏返回按钮")
    @VersionSupport(min = 1024)
    public void hideBack() {
        this.mHideBack = true;
    }

    @MethodHook(desc = "隐藏我的状态")
    @VersionSupport(min = 1024)
    public void hideMineStory() {
        XMethodHook.create($(VSConfigManager)).method("a").params(String.class, Object.class)
                .hook(new XC_LogMethodHook() {
                    @Override
                    protected void after(XMethodHook.MethodParam param) {
                        super.after(param);
                        String key = param.args(0);
                        Object value = param.getResult();
                        if ("mine_videostory_entrance".equals(key)) {
                            param.setResult("0");
                        }
                    }
                });
    }

    @MethodHook(desc = "隐藏厘米秀")
    @VersionSupport(min = 1186)
    public void hideApollo() {
        this.mHideApollo = true;
    }

    @MethodHook(desc = "隐藏设置列表")
    public void hideSettingItems(final List<String> list) {
        // 设置列表
        XMethodHook.create($(QQSettingSettingActivity)).method("a").params(int.class, int.class,
                int.class, int.class).hook(new XC_LogMethodHook() {
            @Override
            protected void after(XMethodHook.MethodParam param) {
                super.after(param);
                Activity activity = param.thisObject();
                String str = activity.getString(param.args(1));
                if (list.contains(str)) {
                    hideView(activity.findViewById(param.args(0)));
                }
            }
        });

        // QQ达人
        if (list.contains("QQ达人")) {
            XMethodHook.create($(QQSettingSettingActivity)).method("c").params(Card)
                    .hook(new XC_LogMethodHook() {
                        @Override
                        protected void before(XMethodHook.MethodParam param) {
                            super.before(param);
                            param.args[0] = null;
                        }
                    });
        }

        // 免流量特权
        if (list.contains("免流量特权")) {
            XMethodHook.create($(QQSettingSettingActivity)).method(void.class, "a")
                    .hook(XC_LogMethodHook.intercept());
        }
    }

    @MethodHook(desc = "添加模块入口")
    public void addModuleEntry() {
        // 皮这一下非常开心.jpg
        int requestCode = (int) (Math.random() * 100000);

        Class<?> clz = $(QQSettingSettingActivity);
        XMethodHook.create(clz).method("doOnCreate").hook(new XC_LogMethodHook() {
            @Override
            protected void after(XMethodHook.MethodParam param) {
                super.after(param);
                View formSimpleItem = XField.create(param).exact(findClass(FormSimpleItem), "a").get();
                Context context = formSimpleItem.getContext();
                mModuleEntry = XConstructor.create(formSimpleItem.getClass()).instance(context);
                XMethod.create(mModuleEntry).name("setLeftText").invoke("QQ净化");
                XMethod.create(mModuleEntry).name("setRightText").invoke(VERSION_NAME);
                LinearLayout linearLayout = (LinearLayout) formSimpleItem.getParent();
                linearLayout.addView(mModuleEntry, 0);
                mModuleEntry.setOnClickListener(view -> {
                    String mainActivityClass = MainActivity.class.getName();
                    String buildNum = XField.create($(BaseApplication)).name("buildNum").get();
                    Intent intent = new Intent(APPLICATION_ID);
                    intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    intent.putExtra(INTENT_BUILD_NUM, buildNum);
                    intent.putExtra(INTENT_LAUNCH, true);
                    intent.setComponent(new ComponentName(APPLICATION_ID, mainActivityClass));
                    Activity activity = param.thisObject();
                    try {
                        activity.startActivityForResult(intent, requestCode);
                    } catch (Exception e) {
                        onAfterError(param, e);
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        XMethodHook.create(clz).method("onActivityResult").hook(new XC_LogMethodHook() {
            @Override
            protected void after(XMethodHook.MethodParam param) {
                super.after(param);
                int rc = param.args(0);
                Intent intent;

                if (requestCode != rc || ((intent = param.args(2)) == null)) {
                    return;
                }

                long oldLmt = Setting.getLong(KEY_LAST_MODIFIED, System.currentTimeMillis());
                long newLmt = intent.getLongExtra(KEY_LAST_MODIFIED, oldLmt);
                if (newLmt == oldLmt) {
                    return;
                }
                QQDialogUtils.showRestartDialog(param.thisObject());
            }
        });

        XMethodHook.create(clz).method("onResume").hook(new XC_LogMethodHook() {
            @Override
            protected void after(XMethodHook.MethodParam param) {
                super.after(param);
                String versionName = getAppVersionName(param.thisObject(), APPLICATION_ID);
                if (mModuleEntry != null) {
                    XMethod.create(mModuleEntry).name("setRightText").invoke(versionName);
                }
            }
        });
    }

    @Override
    public SettingGroup getSettingGroup() {
        return SettingGroup.sidebar;
    }
}
