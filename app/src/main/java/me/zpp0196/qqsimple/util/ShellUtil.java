package me.zpp0196.qqsimple.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import me.zpp0196.qqsimple.Common;
import me.zpp0196.qqsimple.R;

import static me.zpp0196.qqsimple.util.CommUtil.isAppRunning;

/**
 * Created by zpp0196 on 2018/5/27 0027.
 */

public class ShellUtil {

    public static final String CMD_UPDATE = "update";
    public static final String CMD_REBOOT = "reboot";
    public static final String CMD_LAUNCH = "launch";
    private static final String ACTION = "io.va.exposed.CMD";
    private static final String KEY_CMD = "cmd";
    private static final String KEY_PKG = "pkg";
    private Context context;
    private Handler handler;

    public ShellUtil(Context context) {
        this.context = context;
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
                result.setResult(progressName + context.getString(R.string.shell_stop_finish));
            }
            Message msg = new Message();
            msg.obj = result;
            msg.what = Result.DEFAULT;
            handler.sendMessage(msg);
        }).start();
    }

    /**
     * 执行 Vxp 中的命令
     */
    public void executeVxpCmd(String type, String packageName, String progressName) {
        new Thread(() -> {
            Result result = new Result(packageName, progressName + "(Vxp)");
            if (isAppRunning(context, Common.PACKAGE_NAME_VXP)) {
                result.setResult(executeCmd(String.format("am broadcast -a %s -e %s %s -e %s %s", ACTION, KEY_CMD, type, KEY_PKG, packageName)));
                if (result.isEmpty()) {
                    result.setResult(context.getString(R.string.shell_execute_finish));
                }
                result.setVxpRunning(true);
            } else {
                result.setResult(context.getString(R.string.tip_vxp_must_be_running));
                result.setVxpRunning(false);
            }
            Message msg = new Message();
            msg.obj = result;
            msg.what = type.equals(CMD_LAUNCH) ? Result.LAUNCH : Result.VXP;
            handler.sendMessage(msg);
        }).start();
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
            return output.toString();
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

    public static class Result {
        public final static int DEFAULT = 0;
        public final static int VXP = 1;
        public final static int LAUNCH = 2;

        private String progressName;
        private String packageName;
        private String result;
        private boolean isVxpRunning;

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

        public boolean isVxpRunning() {
            return isVxpRunning;
        }

        public void setVxpRunning(boolean vxpRunning) {
            isVxpRunning = vxpRunning;
        }
    }
}
