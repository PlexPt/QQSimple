package me.zpp0196.qqsimple.util;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.activity.MainActivity;

import static me.zpp0196.qqsimple.Common.PACKAGE_NAME_VXP;
import static me.zpp0196.qqsimple.util.CommUtil.isAppRunning;

/**
 * Created by zpp0196 on 2018/5/27 0027.
 */

public class ShellUtil {

    private MainActivity mainActivity;
    private Handler handler;

    @SuppressLint ("HandlerLeak")
    public ShellUtil(MainActivity context) {
        this.mainActivity = context;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Result result = (Result) msg.obj;
                Toast.makeText(mainActivity, result.getResult(), Toast.LENGTH_SHORT)
                        .show();
            }
        };
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    /**
     * 强行停止应用
     *
     * @param packageName 包名
     * @param progressName 程序名
     */
    public void forceStop(String packageName, String progressName) {
        new Thread(() -> {
            Result result = new Result(packageName, progressName);
            result.setResult(executeCmd("am force-stop " + packageName));
            if (result.isEmpty()) {
                result.setResult(getResultStr(R.string.shell_stop_finish, progressName));
            }
            Message msg = new Message();
            msg.obj = result;
            msg.what = Result.DEFAULT;
            handler.sendMessage(msg);
        }).start();
    }

    private String getResultStr(@StringRes int strId, String progressName) {
        return String.format(mainActivity.getString(strId), progressName);
    }

    /**
     * 执行 Vxp 中的命令
     */
    public void executeVxpCmd(VXP_CMD_TYPE type, String packageName, String progressName) {
        if (!checkVxpOperatingStatus(mainActivity)) {
            return;
        }
        new Thread(() -> {
            Result result = new Result(packageName, progressName + "(Vxp)");
            result.setResult(executeCmd(String.format("am broadcast -a io.va.exposed.CMD -e cmd %s -e pkg %s", getVxpCmdType(type), packageName)));
            if (result.isEmpty()) {
                result.setResult(getVxpResultStr(type, progressName));
            }
            Message msg = new Message();
            msg.obj = result;
            msg.what = type == VXP_CMD_TYPE.LAUNCH ? Result.LAUNCH : Result.VXP;
            handler.sendMessage(msg);
        }).start();
    }

    private boolean checkVxpOperatingStatus(MainActivity mainActivity) {
        if (isAppRunning(mainActivity, PACKAGE_NAME_VXP)) {
            return true;
        }
        new MaterialDialog.Builder(mainActivity).title(R.string.title_tip)
                .content(R.string.tip_vxp_must_be_running)
                .negativeText(R.string.title_open_vxp)
                .positiveText(R.string.button_close)
                .onNegative((dialog, which) -> mainActivity.launchApp(PACKAGE_NAME_VXP))
                .show();
        return false;
    }

    private String getVxpResultStr(VXP_CMD_TYPE type, String progressName) {
        String result;
        String format = "%s";
        switch (type) {
            case REBOOT:
                format = mainActivity.getString(R.string.shell_vxp_reboot_finish);
                break;
            case UPDATE:
                format = mainActivity.getString(R.string.shell_vxp_update_finish);
                break;
        }
        result = String.format(format, progressName);
        return result;
    }

    private String getVxpCmdType(VXP_CMD_TYPE type) {
        switch (type) {
            case LAUNCH:
            default:
                return "launch";
            case REBOOT:
                return "reboot";
            case UPDATE:
                return "update";
        }
    }

    /**
     * 执行命令
     *
     * @param cmd 需要执行的命令
     */
    private String executeCmd(String cmd) {
        DataOutputStream os = null;
        Process process = null;
        try {
            process = Runtime.getRuntime()
                    .exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuilder output = new StringBuilder();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();
            return output.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
    }

    public enum VXP_CMD_TYPE {
        UPDATE, REBOOT, LAUNCH
    }

    public static class Result {
        public final static int DEFAULT = 0;
        public final static int VXP = 1;
        public final static int LAUNCH = 2;

        private String progressName;
        private String packageName;
        private String result;

        public Result(String packageName, String progressName) {
            this.packageName = packageName;
            this.progressName = progressName;
        }

        public String getProgressName() {
            return progressName;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public boolean isEmpty() {
            return getResult().isEmpty();
        }
    }
}
