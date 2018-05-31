package me.zpp0196.qqsimple.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;

import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.activity.MainActivity;
import me.zpp0196.qqsimple.activity.item.CardItem;
import me.zpp0196.qqsimple.activity.item.CardItem.Item;
import me.zpp0196.qqsimple.fragment.base.BaseFragment;
import me.zpp0196.qqsimple.util.ShellUtil;
import me.zpp0196.qqsimple.util.ShellUtil.Result;
import me.zpp0196.qqsimple.util.UpdateUtil;

import static android.os.Build.BRAND;
import static android.os.Build.MANUFACTURER;
import static me.zpp0196.qqsimple.BuildConfig.APPLICATION_ID;
import static me.zpp0196.qqsimple.BuildConfig.VERSION_CODE;
import static me.zpp0196.qqsimple.BuildConfig.VERSION_NAME;
import static me.zpp0196.qqsimple.Common.PACKAGE_NAME_QQ;
import static me.zpp0196.qqsimple.Common.PACKAGE_NAME_VXP;
import static me.zpp0196.qqsimple.Common.PACKAGE_NAME_XPOSED_INSTALLER;
import static me.zpp0196.qqsimple.Common.PREFS_KEY_APP_VERSION_CODE;
import static me.zpp0196.qqsimple.Common.PREFS_KEY_CHECK_UPDATE_AUTO;
import static me.zpp0196.qqsimple.Common.PREFS_KEY_SWITCH_ALL;
import static me.zpp0196.qqsimple.hook.comm.Ids.isSupport;
import static me.zpp0196.qqsimple.util.CommUtil.getQQVersionCode;
import static me.zpp0196.qqsimple.util.CommUtil.getQQVersionName;
import static me.zpp0196.qqsimple.util.CommUtil.isInVxp;
import static me.zpp0196.qqsimple.util.CommUtil.isInstalled;
import static me.zpp0196.qqsimple.util.ShellUtil.CMD_LAUNCH;
import static me.zpp0196.qqsimple.util.ShellUtil.CMD_REBOOT;
import static me.zpp0196.qqsimple.util.ShellUtil.CMD_UPDATE;
import static me.zpp0196.qqsimple.util.ShellUtil.Result.DEFAULT;
import static me.zpp0196.qqsimple.util.ShellUtil.Result.LAUNCH;
import static me.zpp0196.qqsimple.util.ShellUtil.Result.VXP;

/**
 * Created by zpp0196 on 2018/5/25 0025.
 */

public class MainFragment extends BaseFragment {

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public int getTitleId() {
        return R.string.app_name;
    }

    @Override
    protected void init() {
        addItems(getMainActivity());
    }

    @SuppressLint ("HandlerLeak")
    private void addItems(MainActivity mainActivity) {
        CardItem device = findViewById(R.id.card_device_layout);
        CardItem qq = findViewById(R.id.card_qq_layout);
        CardItem module = findViewById(R.id.card_module_layout);

        Item manufacturer = new Item(mainActivity, R.drawable.ic_item_card_device_phone, getUIFramework());
        Item android = new Item(mainActivity, R.drawable.ic_item_card_device_android, String.format(getString(R.string.item_card_device_android), Build.VERSION.RELEASE, getAndroidVersion(), Build.VERSION.SDK_INT));
        Item qqVersion = new Item(mainActivity, R.drawable.ic_item_card_qq, String.format(getString(R.string.item_card_comm_version), getQQVersionName(mainActivity), getQQVersionCode(mainActivity)));
        Item stopQQ = new Item(mainActivity, R.drawable.ic_item_card_qq_stop, R.string.item_card_qq_stop);
        Item stopVxpQQ = new Item(mainActivity, R.drawable.ic_item_card_qq_stop, R.string.item_card_qq_stop_vxp);
        Item moduleVersion = new Item(mainActivity, R.drawable.ic_item_comm_info, String.format(getString(R.string.item_card_comm_version), VERSION_NAME, VERSION_CODE));
        Item checkUpdate = new Item(mainActivity, R.drawable.ic_item_card_module_check_update, R.string.item_card_module_update_check);
        Item updateVxp = new Item(mainActivity, R.drawable.ic_item_card_module_update_vxp, R.string.item_card_module_update_vxp);

        ShellUtil shellUtil = new ShellUtil(mainActivity);
        shellUtil.setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Result result = (Result) msg.obj;
                Snackbar snackbar = Snackbar.make(getRootView(), result.getResult(), Snackbar.LENGTH_LONG);
                String open = getString(R.string.title_open);
                String actionTitle = open + result.getProgressName();
                switch (msg.what) {
                    case DEFAULT:
                        snackbar.setAction(actionTitle, v -> mainActivity.launchApp(result.getPackageName()));
                        snackbar.show();
                        break;
                    case VXP:
                        if (result.isVxpRunning()) {
                            snackbar.setAction(actionTitle, v -> shellUtil.executeVxpCmd(CMD_LAUNCH, result.getPackageName(), result.getProgressName()));
                            snackbar.show();
                        }
                    case LAUNCH:
                        if (!result.isVxpRunning()) {
                            Snackbar.make(getRootView(), result.getResult(), Snackbar.LENGTH_LONG)
                                    .setAction(getString(R.string.title_open_vxp), v -> mainActivity.launchApp(PACKAGE_NAME_VXP))
                                    .show();
                        }
                        break;
                }
            }
        });

        // 结束运行 QQ
        stopQQ.setOnClickListener(v -> shellUtil.forceStop(PACKAGE_NAME_QQ, getString(R.string.qq_name)));
        // 打开 QQ
        stopQQ.setOnLongClickListener(v -> {
            mainActivity.launchApp(PACKAGE_NAME_QQ);
            return false;
        });
        // 结束运行 QQ(Vxp)
        stopVxpQQ.setOnClickListener(v -> shellUtil.executeVxpCmd(CMD_REBOOT, PACKAGE_NAME_QQ, getString(R.string.qq_name)));
        // 打开 QQ(Vxp)
        stopVxpQQ.setOnLongClickListener(v -> {
            shellUtil.executeVxpCmd(CMD_LAUNCH, PACKAGE_NAME_QQ, getString(R.string.qq_name));
            return false;
        });
        // 打开酷安
        moduleVersion.setOnClickListener(v -> mainActivity.openCoolApk());
        // 检查更新
        checkUpdate.setOnClickListener(v -> checkUpdate(checkUpdate.getTitleView()));
        // 更新模块(Vxp)
        updateVxp.setOnClickListener(v -> shellUtil.executeVxpCmd(CMD_UPDATE, APPLICATION_ID, getString(R.string.app_name)));
        // 进入模块(Vxp)
        updateVxp.setOnLongClickListener(v -> {
            shellUtil.executeVxpCmd(CMD_LAUNCH, APPLICATION_ID, getString(R.string.item_card_module));
            return false;
        });

        device.addItem(manufacturer, android);

        if (isInVxp(mainActivity)) {
            qq.addItem(qqVersion);
        } else {
            qq.addItem(qqVersion, stopQQ, stopVxpQQ);
        }

        module.addItem(moduleVersion, checkUpdate, updateVxp);

        boolean isAutoUpdate = getPrefs().getBoolean(PREFS_KEY_CHECK_UPDATE_AUTO, false);
        if (isAutoUpdate) {
            checkUpdate(checkUpdate.getTitleView());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshInstallStatus(getMainActivity());
    }

    private void refreshInstallStatus(MainActivity mainActivity) {
        int versionCode = getPrefs().getInt(PREFS_KEY_APP_VERSION_CODE, 0);
        if (versionCode < VERSION_CODE) {
            mainActivity.showUpdateLog();
            getEditor().putInt(PREFS_KEY_APP_VERSION_CODE, VERSION_CODE)
                    .apply();
        }

        TextView txtInstallError = findViewById(R.id.module_active_errors);
        View txtInstallContainer = findViewById(R.id.status_container);
        ImageView txtInstallIcon = findViewById(R.id.status_icon);
        View disableWrapper = findViewById(R.id.disableView);
        SwitchCompat closeAll = disableWrapper.findViewById(R.id.enableSwitch);

        closeAll.setChecked(getPrefs().getBoolean(PREFS_KEY_SWITCH_ALL, true));
        closeAll.setOnCheckedChangeListener((buttonView, isChecked) -> getEditor().putBoolean(PREFS_KEY_SWITCH_ALL, isChecked)
                .apply());
        txtInstallContainer.setOnClickListener(v -> openXposed(txtInstallError));

        if (!mainActivity.isModuleActive()) {
            txtInstallError.setText(R.string.module_not_active);
            txtInstallError.setTextColor(getResources().getColor(R.color.warning));
            txtInstallContainer.setBackgroundColor(getResources().getColor(R.color.warning));
            txtInstallIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_error));
            disableWrapper.setVisibility(View.GONE);
        } else if (!isSupport(getQQVersionCode(mainActivity))) {
            txtInstallError.setText(R.string.module_not_support);
            txtInstallError.setTextColor(getResources().getColor(R.color.amber_500));
            txtInstallContainer.setBackgroundColor(getResources().getColor(R.color.amber_500));
            txtInstallIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_warning));
        } else {
            txtInstallError.setText(R.string.module_active);
            txtInstallError.setTextColor(getResources().getColor(R.color.darker_green));
            txtInstallContainer.setBackgroundColor(getResources().getColor(R.color.darker_green));
            txtInstallIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_circle));
        }
    }

    private String getUIFramework() {
        String manufacturer =
                Character.toUpperCase(MANUFACTURER.charAt(0)) + MANUFACTURER.substring(1);
        if (!BRAND.equals(MANUFACTURER)) {
            manufacturer += " " + Character.toUpperCase(BRAND.charAt(0)) + BRAND.substring(1);
        }
        manufacturer += " " + Build.MODEL + " ";
        if (manufacturer.contains("Samsung")) {
            manufacturer += new File("/system/framework/twframework.jar").exists() ? "(TouchWiz)" :
                    "(AOSP-based ROM)";
        } else if (manufacturer.contains("Xioami")) {
            manufacturer +=
                    new File("/system/framework/framework-miui-res.apk").exists() ? "(MIUI)" :
                            "(AOSP-based ROM)";
        }
        return manufacturer;
    }

    private String getAndroidVersion() {
        switch (Build.VERSION.SDK_INT) {
            case 16:
            case 17:
            case 18:
                return "Jelly Bean";
            case 19:
                return "KitKat";
            case 21:
            case 22:
                return "Lollipop";
            case 23:
                return "Marshmallow";
            case 24:
            case 25:
                return "Nougat";
            case 26:
            case 27:
                return "Oreo";
            default:
                return "unknown";
        }
    }

    private void openXposed(TextView txtInstallError) {
        try {
            if (isInstalled(getMainActivity(), PACKAGE_NAME_XPOSED_INSTALLER)) {
                Intent intent = new Intent(PACKAGE_NAME_XPOSED_INSTALLER + ".OPEN_SECTION");
                PackageManager packageManager = getMainActivity().getPackageManager();
                if (packageManager.queryIntentActivities(intent, 0)
                        .isEmpty()) {
                    intent = packageManager.getLaunchIntentForPackage(PACKAGE_NAME_XPOSED_INSTALLER);
                }
                if (intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("section", "modules")
                            .putExtra("fragment", 1)
                            .putExtra("module", APPLICATION_ID);
                }
                startActivity(intent);
            } else {
                txtInstallError.setText(R.string.tip_xposed_not_install);
            }
        } catch (Exception e) {
            txtInstallError.setText(e.getMessage());
        }
    }

    @SuppressLint ("HandlerLeak")
    public synchronized void checkUpdate(TextView textView) {
        if (textView != null) {
            textView.setText(R.string.item_card_module_update_check_ing);
        } else {
            Snackbar.make(getRootView(), R.string.item_card_module_update_check_ing, Snackbar.LENGTH_SHORT)
                    .show();
        }
        UpdateUtil.CheckUpdate(new Handler() {
            @SuppressLint ("LogConditional")
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UpdateUtil.FINISHED:
                        UpdateUtil.UpdateInfo updateInfo = (UpdateUtil.UpdateInfo) msg.obj;
                        Log.d(MainActivity.class.getName(), String.format("isUpdate: %s, versionName: %s, versionCode: %s", updateInfo.isUpdate(), updateInfo.getVersionName(), updateInfo.getVersionCode()));
                        if (updateInfo.isUpdate()) {
                            showUpdateDialog(updateInfo);
                        }
                        if (textView != null) {
                            textView.setText(R.string.item_card_module_update_check_latest);
                        } else {
                            Snackbar.make(getRootView(), R.string.item_card_module_update_check_latest, Snackbar.LENGTH_SHORT)
                                    .show();
                        }
                        break;
                    case UpdateUtil.ERR:
                        Exception exception = (Exception) msg.obj;
                        Snackbar.make(getRootView(), exception.getMessage(), Snackbar.LENGTH_LONG)
                                .show();
                        break;
                }
            }
        });
    }

    private void showUpdateDialog(UpdateUtil.UpdateInfo updateInfo) {
        MainActivity mainActivity = getMainActivity();
        new MaterialDialog.Builder(mainActivity).cancelable(false)
                .title(R.string.item_card_module_update_check_new)
                .customView(mainActivity.getWebView(updateInfo.getUpdateLog()), true)
                .positiveText(R.string.item_card_module_update_check_update)
                .negativeText(R.string.button_releases)
                .neutralText(R.string.button_close)
                .onPositive((dialog, which) -> mainActivity.openCoolApk())
                .onNegative((dialog, which) -> mainActivity.openReleases())
                .build()
                .show();
    }
}
