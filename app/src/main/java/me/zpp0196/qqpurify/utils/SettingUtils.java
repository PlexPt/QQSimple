package me.zpp0196.qqpurify.utils;

import android.util.Log;

import androidx.annotation.Keep;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.zpp0196.library.xposed.XLog;

/**
 * Created by zpp0196 on 2018/5/17.
 */
public class SettingUtils implements Constants {

    public interface ISetting {

        @Keep
        enum SettingGroup {
            mainui, sidebar, chat, troop, extension, setting, about, earlier, update, empty
        }

        SettingGroup getSettingGroup();
    }

    private static final JSONObject DEFAULT_SETTING = new JSONObject();
    static final JSONObject DEFAULT_GROUPS = new JSONObject();
    static JSONObject mJsonData;

    private static File mDataFile;

    static {
        try {
            JSONObject mainui = new JSONObject();
            mainui.put("simulateMenu", false);
            mainui.put("hideHonestSay", false);
            mainui.put("hideCreateTroop", false);
            mainui.put("hideFriendGroups", new JSONArray().put(0, ""));

            JSONObject sidebar = new JSONObject();
            sidebar.put("addModuleEntry", true);

            JSONObject chat = new JSONObject();
            chat.put(KEY_GRAY_TIP_KEYWORDS, "会员 礼物 送给 豪气 魅力 进场");

            JSONObject troop = new JSONObject();

            JSONObject extension = new JSONObject();
            extension.put(KEY_IMAGE_BG_COLOR, "#80000000");
            extension.put(KEY_RENAME_BASE_FORMAT, "%l_%n.apk");
            extension.put(KEY_REDIRECT_FILE_REC_PATH, "/Tencent/QQfile_recv/");

            JSONObject setting = new JSONObject();
            setting.put(KEY_DISABLE_MODULE, false);
            setting.put(KEY_LOG_LEVEL, XLog.LogLevel.NONE.name());
            setting.put(KEY_LOG_COUNT, 10);

            JSONObject update = new JSONObject();
            update.put(KEY_UPD_AUTO_CHECK, true);
            update.put(KEY_UPD_AUTO_OPEN, false);
            update.put(KEY_UPD_INTERVAL, 1000);

            DEFAULT_GROUPS.put(ISetting.SettingGroup.mainui.name(), mainui);
            DEFAULT_GROUPS.put(ISetting.SettingGroup.sidebar.name(), sidebar);
            DEFAULT_GROUPS.put(ISetting.SettingGroup.chat.name(), chat);
            DEFAULT_GROUPS.put(ISetting.SettingGroup.troop.name(), troop);
            DEFAULT_GROUPS.put(ISetting.SettingGroup.extension.name(), extension);
            DEFAULT_GROUPS.put(ISetting.SettingGroup.setting.name(), setting);
            DEFAULT_GROUPS.put(ISetting.SettingGroup.update.name(), update);
            DEFAULT_SETTING.put(KEY_GROUPS, DEFAULT_GROUPS);
            DEFAULT_SETTING.put(KEY_LAST_MODIFIED, System.currentTimeMillis());
        } catch (JSONException ignore) {
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void init() throws Exception {
        try {
            if (mDataFile == null) {
                mDataFile = new File(Utils.getLocalPath(), "config.json");
            }
            mJsonData = new JSONObject(DEFAULT_SETTING.toString());
            if (!mDataFile.exists()) {
                FileUtils.forceMkdirParent(mDataFile);
                mDataFile.createNewFile();
            }
            String data = FileUtils.readFileToString(mDataFile, StandardCharsets.UTF_8).trim();
            if (data.isEmpty()) {
                write(DEFAULT_SETTING);
            } else {
                mJsonData = new JSONObject(data);
            }
            Log.d("QQPurifySetting", "init: " + mJsonData.toString());
        } catch (Exception e) {
            throw new Exception("读取配置文件失败，使用默认配置");
        }
    }

    public static void restore() throws IOException, JSONException {
        write(DEFAULT_SETTING);
    }

    public static List<String> getGroups(String suffix) {
        List<String> list = new ArrayList<>();
        for (Iterator<String> it = DEFAULT_GROUPS.keys(); it.hasNext(); ) {
            list.add(Utils.initialCapital(it.next()) + suffix);
        }
        return list;
    }

    public static long getLong(String key, long def) {
        try {
            return mJsonData.getLong(key);
        } catch (JSONException e) {
            return def;
        }
    }

    static void write(JSONObject data) throws IOException, JSONException {
        FileUtils.write(mDataFile, data.toString(), StandardCharsets.UTF_8, false);
        mJsonData = new JSONObject(data.toString());
    }
}
