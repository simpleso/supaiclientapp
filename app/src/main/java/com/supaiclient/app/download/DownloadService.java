/*
 * @Title DownloadService.java
 * @Copyright Copyright 2010-2015 Yann Software Co,.Ltd All Rights Reserved.
 * @Description��
 * @author Yann
 * @date 2015-8-7 ����10:03:42
 * @version 1.0
 */
package com.supaiclient.app.download;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.supaiclient.app.BaseApplication;
import com.supaiclient.app.R;
import com.supaiclient.app.util.PackageUtils;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * ��ע��
 *
 * @author Yann
 * @date 2015-8-7 ����10:03:42
 */
public class DownloadService extends Service {

    public static final String DOWNLOAD_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/supai/";

    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    public static final String ACTION_FINISHED = "ACTION_FINISHED";
    public static final int MSG_INIT = 0;
    private static final int NOTIFY_ID = 0;
    private static FileInfo fileInfo;
    private String TAG = "DownloadService";
    private Map<Integer, DownloadTask> mTasks =
            new LinkedHashMap<Integer, DownloadTask>();
    private String notify_name = "速派超人 正在下载...";
    private Context mContext = this;
    private Notification mNotification;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: This method is called when the BroadcastReceiver is receiving
            // an Intent broadcast.

            boolean isdownload = BaseApplication.get("isdownload", false);

            //L.e(isdownload + "--------");

            if (!isdownload) {

                mNotification.contentView.setTextViewText(R.id.start_pause, "暂停");

                mNotification.flags &= ~NotificationCompat.FLAG_AUTO_CANCEL;

                Intent intent1 = new Intent(mContext, DownloadService.class);
                intent1.setAction(DownloadService.ACTION_START);
                intent1.putExtra("fileInfo", fileInfo);
                mContext.startService(intent1);
            } else {

                mNotification.contentView.setTextViewText(R.id.start_pause, "开始");
                mNotification.flags = NotificationCompat.FLAG_AUTO_CANCEL;
                Intent intent2 = new Intent(mContext, DownloadService.class);
                intent2.setAction(DownloadService.ACTION_STOP);
                intent2.putExtra("fileInfo", fileInfo);
                mContext.startService(intent2);
            }

            //   throw new UnsupportedOperationException("Not yet implemented");
        }
    };
    private NotificationManager mNotificationManager;
    /**
     * 更新UI的广播接收器
     */
    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadService.ACTION_UPDATE.equals(intent.getAction())) {
                int finised = intent.getIntExtra("finished", 0);
                int id = intent.getIntExtra("id", 0);

                //	mAdapter.updateProgress(id, finised);

                Log.i("mReceiver", id + "-finised = " + finised);
                if (finised < 100) {
                    RemoteViews contentview = mNotification.contentView;
                    contentview.setTextViewText(R.id.tv_progress, finised + "%");
                    contentview.setProgressBar(R.id.progressbar, 100, finised, false);
                } else {
                    // 下载完毕后变换通知形式
                    mNotification.flags = Notification.FLAG_AUTO_CANCEL;
                    mNotification.contentView = null;
                    mNotification = new Notification.Builder(mContext).setContentTitle("下载通知").setContentText("已下载完毕").build();
                    stopSelf();// 停掉服务自身
                }

                PendingIntent contentIntent2 = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
                mNotification.contentIntent = contentIntent2;
                mNotificationManager.notify(NOTIFY_ID, mNotification);

            } else if (DownloadService.ACTION_FINISHED.equals(intent.getAction())) {
                // 下载结束
                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
                //mAdapter.updateProgress(fileInfo.getId(), 0);
                //L.e(fileInfo.getFinished() + "-------");
                mNotificationManager.cancel(NOTIFY_ID);
                BaseApplication.set("isFinish", true);

                //安卓APK
                PackageUtils.install(context, DOWNLOAD_PATH + fileInfo.getFileName());
            }
        }
    };
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_INIT:
                    FileInfo fileInfo = (FileInfo) msg.obj;
                    Log.i(TAG, "Init:" + fileInfo);
                    DownloadTask task = new DownloadTask(DownloadService.this, fileInfo, 1);
                    task.downLoad();
                    mTasks.put(fileInfo.getId(), task);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 创建通知
     */
    private void setUpNotification() {

        int icon = R.mipmap.ic_launcher;
        CharSequence tickerText = "开始下载";
        long when = System.currentTimeMillis();

        mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);

        mNotification = new Notification(icon, tickerText, when);
        // 放置在"正在运行"栏目中
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.update_download_notification_layout);
        contentView.setTextViewText(R.id.name, notify_name);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("com.supaiclent.app.start"),
                PendingIntent.FLAG_UPDATE_CURRENT);

        contentView.setOnClickPendingIntent(R.id.start_pause, pendingIntent);

        // 指定个性化视图
        mNotification.contentView = contentView;

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

        // 指定内容意图
        mNotification.contentIntent = contentIntent;
        mNotificationManager.notify(NOTIFY_ID, mNotification);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.supaiclent.app.start");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * @see Service#onStartCommand(Intent, int, int)
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {

            if (ACTION_START.equals(intent.getAction())) {
                fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
                Log.i(TAG, "Start:" + fileInfo.toString());
                new InitThread(fileInfo).start();
                setUpNotification();

                // 注册广播接收器
                IntentFilter filter = new IntentFilter();
                filter.addAction(DownloadService.ACTION_UPDATE);
                filter.addAction(DownloadService.ACTION_FINISHED);
                registerReceiver(mReceiver, filter);

                mNotification.contentView.setTextViewText(R.id.start_pause, "暂停");
                BaseApplication.set("isdownload", true);

            } else if (ACTION_STOP.equals(intent.getAction())) {
                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
                Log.i(TAG, "Stop:" + fileInfo.toString());

                DownloadTask task = mTasks.get(fileInfo.getId());
                if (task != null) {
                    task.isPause = true;
                    //  mNotificationManager.cancel(NOTIFY_ID);
                    mNotification.contentView.setTextViewText(R.id.start_pause, "开始");
                    BaseApplication.set("isdownload", false);
                }
            }

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
            // 指定内容意图
            mNotification.contentIntent = contentIntent;
            mNotificationManager.notify(NOTIFY_ID, mNotification);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * @see Service#onBind(Intent)
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class InitThread extends Thread {

        private FileInfo mFileInfo = null;

        public InitThread(FileInfo mFileInfo) {
            this.mFileInfo = mFileInfo;
        }

        /**
         * @see Thread#run()
         */
        @Override
        public void run() {
            HttpURLConnection connection = null;
            RandomAccessFile raf = null;

            try {
                URL url = new URL(mFileInfo.getUrl());
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                int length = -1;

                if (connection.getResponseCode() == HttpStatus.SC_OK) {
                    length = connection.getContentLength();
                }

                if (length <= 0) {
                    return;
                }

                File dir = new File(DOWNLOAD_PATH);
                if (!dir.exists()) {
                    dir.mkdir();
                }

                File file = new File(dir, mFileInfo.getFileName());
                raf = new RandomAccessFile(file, "rwd");
                raf.setLength(length);
                mFileInfo.setLength(length);
                mHandler.obtainMessage(MSG_INIT, mFileInfo).sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
