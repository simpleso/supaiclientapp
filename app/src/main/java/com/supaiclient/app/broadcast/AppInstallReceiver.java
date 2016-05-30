package com.supaiclient.app.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.supaiclient.app.BaseApplication;
import com.supaiclient.app.util.FileUtils;

/**
 * 安装程序的广播接收
 * Created by Administrator on 2016/5/16.
 */
public class AppInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager manager = context.getPackageManager();
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {

            String packageName = intent.getData().getSchemeSpecificPart();
            if (context.getPackageName().equals(packageName)) {
                BaseApplication.set("isFinish", false);
                FileUtils.deleteDirectory("supai");
            }
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {

            String packageName = intent.getData().getSchemeSpecificPart();
            if (context.getPackageName().equals(packageName)) {
                BaseApplication.set("isFinish", false);
                FileUtils.deleteDirectory("supai");
            }
        }
    }
}
