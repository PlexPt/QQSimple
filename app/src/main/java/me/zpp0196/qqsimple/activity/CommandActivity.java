package me.zpp0196.qqsimple.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import me.zpp0196.qqsimple.Common;
import me.zpp0196.qqsimple.R;

/**
 * Created by zpp0196 on 2018/4/25 0025.
 */

public class CommandActivity extends AppCompatActivity {

    private CommandType commandType;
    private String packageName;
    private String progressName;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what != -1) updateView(msg.obj.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cmd);
        Intent intent = getIntent();
        packageName = intent.getStringExtra("packageName");
        progressName = intent.getStringExtra("progressName");
        if (packageName == null || packageName.isEmpty() || progressName == null || progressName.isEmpty())
            return;
        String vxpCmdType = intent.getStringExtra("vxpCmdType");
        if (vxpCmdType != null && !vxpCmdType.isEmpty()) {
            executeVxpCmd(vxpCmdType, packageName);
            commandType = CommandType.VXP;
        } else {
            forceStop(packageName, progressName);
            commandType = CommandType.QQ;
        }
    }

    private void updateView(String msg) {
        ProgressBar progressBar = findViewById(R.id.pb_cmd_wait);
        progressBar.setVisibility(View.GONE);
        TextView textView = findViewById(R.id.txt_cmd_wait);
        textView.setText(msg);
        TextView start = findViewById(R.id.txt_cmd_start);
        start.setText("打开" + progressName);
        start.setVisibility(View.VISIBLE);
        if (msg.contains("denied")) {
            start.setText("未获取 Root 权限！");
            return;
        }
        if (commandType == CommandType.QQ) {
            start.setOnClickListener(v -> openQQ());
            if (getPrefs().getBoolean("auto_start", false)) openQQ();
        } else if (commandType == CommandType.VXP) {
            start.setOnClickListener(v -> launchAppInVxp());
            if (getPrefs().getBoolean("auto_start", false)) launchAppInVxp();
        }
    }

    private SharedPreferences getPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void openQQ() {
        startActivity(getPackageManager().getLaunchIntentForPackage(Common.PACKAGE_NAME_QQ));
    }

    /**
     * 强行停止应用
     *
     * @param packageName  包名
     * @param progressName 程序名
     */
    private void forceStop(String packageName, String progressName) {
        new Thread(() -> {
            String result = executeCmd("am force-stop " + packageName);
            Message msg = new Message();
            msg.obj = result;
            if (result.isEmpty()) {
                msg.obj = "强行停止" + progressName + "完成";
            }
            handler.sendMessage(msg);
        }).start();
    }

    private void launchAppInVxp() {
        executeVxpCmd("launch", packageName);
    }

    /**
     * 执行 Vxp 中的命令
     *
     * @param type
     * @param packageName
     */
    private void executeVxpCmd(String type, String packageName) {
        new Thread(() -> {
            String result = executeCmd(String.format("am broadcast -a io.va.exposed.CMD -e cmd %s -e pkg %s", type, packageName));
            Message msg = new Message();
            msg.obj = result;
            if (result.isEmpty()) {
                msg.obj = "执行完成";
            }
            if (type.equals("launch")) msg.what = -1;
            handler.sendMessage(msg);
        }).start();
    }

    /**
     * 执行命令
     *
     * @param cmd 需要执行的命令
     * @return
     */
    private String executeCmd(String cmd) {
        DataOutputStream os = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
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
            if (os != null)
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (process != null) process.destroy();
        }
    }

    private enum CommandType {
        QQ, VXP
    }
}
