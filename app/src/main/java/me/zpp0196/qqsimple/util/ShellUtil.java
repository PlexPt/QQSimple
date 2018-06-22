package me.zpp0196.qqsimple.util;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.StringRes;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import me.zpp0196.qqsimple.R;
import me.zpp0196.qqsimple.activity.MainActivity;

import static me.zpp0196.qqsimple.Common.PACKAGE_NAME_QQ;
import static me.zpp0196.qqsimple.util.ShellUtil.VXP_CMD_TYPE.REBOOT;
import static me.zpp0196.qqsimple.util.ShellUtil.VXP_CMD_TYPE.UPDATE;

/**
 * Created by zpp0196 on 2018/5/27 0027.
 */

public class ShellUtil {

    private MainActivity mainActivity;
    private Handler handler;

    @SuppressLint ("HandlerLeak")
    public ShellUtil(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String) msg.obj;
                Toast.makeText(mainActivity, result, Toast.LENGTH_SHORT)
                        .show();
            }
        };
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    /**
     * 强行停止QQ
     */
    public void forceStopQQ() {
        new Thread(() -> {
            String result = executeCmd("am force-stop " + PACKAGE_NAME_QQ);
            if (result.isEmpty()) {
                result = String.format(getString(R.string.tip_stop_qq_finish), getString(R.string.qq_name));
            }
            Message msg = new Message();
            msg.obj = result;
            handler.sendMessage(msg);
        }).start();
    }

    /**
     * 执行 Vxp 中的命令
     *
     * @param type 命令类型
     * @param packageName 包名
     */
    public void executeVxpCmd(VXP_CMD_TYPE type, String packageName) {
        Intent t = new Intent("io.va.exposed.CMD");
        t.putExtra("cmd", type == REBOOT ? "reboot" : type == UPDATE ? "update" : "launch");
        t.putExtra("pkg", packageName);
        mainActivity.sendBroadcast(t);
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
            return output.toString()
                    .trim();
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

    private String getString(@StringRes int strId) {
        return mainActivity.getString(strId);
    }

    public enum VXP_CMD_TYPE {
        UPDATE, REBOOT, LAUNCH
    }
}