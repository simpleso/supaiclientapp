package com.supaiclient.app.util;

import android.content.Context;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/1/11.
 */
public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler defaultUEH;

    private Context context;

    public CustomExceptionHandler(Context context) {

        this.context = context;
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }


    public void uncaughtException(Thread t, Throwable e) {

        Date currentTime = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");

        String currentTimeStamp = sdf.format(currentTime);
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        final String stacktrace = result.toString();
        Log.e("异常", stacktrace);
        printWriter.close();
        String filename = currentTimeStamp + ".txt";

        MobclickAgent.reportError(context, stacktrace);
        MyLog.add(stacktrace, MyLog.exceptionPath, filename);
        defaultUEH.uncaughtException(t, e);
    }
}
