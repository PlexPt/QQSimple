package me.zpp0196.qqpurify.hook.utils;

import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import me.zpp0196.library.xposed.XLog;
import me.zpp0196.qqpurify.utils.Constants;
import me.zpp0196.qqpurify.utils.Setting;
import me.zpp0196.qqpurify.utils.SettingUtils;
import me.zpp0196.qqpurify.utils.Utils;

/**
 * Created by zpp0196 on 2019/6/2.
 */
public class XLogUtils implements XLog.Callback, Constants {

    private static final SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);

    private static int mLogCount;
    private static boolean mSwitch;
    private static File mLogFile;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private XLogUtils() {
        try {
            File logPath = new File(Utils.getLocalPath(), "logs");
            if (!logPath.exists()) {
                FileUtils.forceMkdir(logPath);
            }
            File[] logFiles = logPath.listFiles();
            Arrays.sort(logFiles);
            int i = logFiles.length;
            for (File logFile : logFiles) {
                if (i < mLogCount) {
                    break;
                }
                logFile.delete();
                i--;
            }
            String proc = Utils.getProcessName();
            long millis = System.currentTimeMillis();
            mLogFile = new File(logPath, String.format("%s_%s.log", proc, millis));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class SingletonInstance {
        private static final XLogUtils INSTANCE = new XLogUtils();
    }

    public static XLogUtils getInstance() {
        Setting setting = Setting.getInstance(SettingUtils.ISetting.SETTING_SETTING);
        mSwitch = setting.get(Constants.KEY_LOG_SWITCH, false);
        mLogCount = setting.get(KEY_LOG_COUNT, 10);
        return SingletonInstance.INSTANCE;
    }

    public static void log(SettingUtils.ISetting setting, String message) {
        XLog.i(String.format("%s(%s) -> %s", APP_NAME, setting.getSettingGroup(), message));
    }

    @Override
    public synchronized boolean log(Object msg, Throwable th) {
        if (mLogFile == null) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(mSdf.format(new Date())).append("/? ").append(msg).append("\n");
        if (th != null) {
            sb.append(Log.getStackTraceString(th)).append("\n");
        }
        try {
            FileUtils.writeStringToFile(mLogFile, sb.toString(), "utf-8", true);
            return mSwitch;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
