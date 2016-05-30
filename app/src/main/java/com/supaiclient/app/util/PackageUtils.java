package com.supaiclient.app.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 包管理器
 * Created by Administrator on 2016/5/24.
 */
public class PackageUtils {

    /**
     * 安装APK
     *
     * @param context
     * @param filePath
     */
    public static void install(Context context, String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 卸载APK
     *
     * @param context
     * @param pakageName
     */
    public static void uninstall(Context context, String pakageName) {
        Uri packageURI = Uri.parse("package:" + pakageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        context.startActivity(uninstallIntent);
    }
}
