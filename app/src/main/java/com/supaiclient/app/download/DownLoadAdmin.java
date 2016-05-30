package com.supaiclient.app.download;

import android.content.Context;
import android.content.Intent;

/**
 * 下载管理员
 * Created by Administrator on 2016/5/14.
 */
public class DownLoadAdmin {

    public static void start(Context mContext, FileInfo fileInfo) {
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.setAction(DownloadService.ACTION_START);
        intent.putExtra("fileInfo", fileInfo);
        mContext.startService(intent);
    }

    public static void stop(Context mContext, FileInfo fileInfo) {
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.setAction(DownloadService.ACTION_STOP);
        intent.putExtra("fileInfo", fileInfo);
        mContext.startService(intent);
    }
}
