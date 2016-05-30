package com.supaiclient.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fan on 2014-12-03.
 */
public class MyLog {


    public static String exceptionPath = "/supai/exceptionlog/";// 异常 信息文件保存地址
    public static String cachePath = "/supai/cachelog/";  // 保存 缓存 日记
    public static String CACHEPATHNAMEXML = "cachpathlogname";

    public static void initMyLog(Context context) {

        //初始�? 日志记录
        SharedPreferences spf = context.getSharedPreferences(CACHEPATHNAMEXML, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd�? HH时mm分ss�?");
        String fileName = sdf.format(new Date());
        editor.putString("fileName", fileName);
        editor.commit();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + cachePath;
        FileUtils.createFile(path, fileName + ".txt");
    }

    /**
     * 得到 记录 日记 file name
     *
     * @param context
     * @return
     */
    private static String getFileName(Context context) {

        SharedPreferences spf = context.getSharedPreferences(CACHEPATHNAMEXML, Context.MODE_PRIVATE);
        return spf.getString("fileName", "");
    }

    /**
     * 保存日记 文件信息
     *
     * @param path     地址
     * @param content  内容
     * @param fileName 文件�?
     */
    public static void add(String content, String path, String fileName) {

        if (!FileUtils.checkSaveLocationExists()) {

            Log.e("MyLog", "无SD卡，无法保存 日记信息");
            return;
        }
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + path;
        FileUtils.createFile(path, fileName);
        FileUtils.writeToFile(content, path + fileName);
    }

    /**
     * 添加 日志
     *
     * @param context
     * @param content
     */
    public static void savelog(Context context, String content) {

        if (!FileUtils.checkSaveLocationExists()) {

            Log.e("MyLog", "无SD卡，无法保存 日记信息");
            return;
        }
        if (TextUtils.isEmpty(content)) {

            Log.e("MyLog", "添加记录日志失败，记录日志为�?");
            return;
        }
        String filename = getFileName(context);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + cachePath + filename + ".txt";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd�? HH时mm分ss�?");
        String time = sdf.format(new Date());
        content = "\r\n" + time + ":" + content;
        FileUtils.writeAdd(path, content);
    }

}
