/*
 * @Title DownloadTask.java
 * @Copyright Copyright 2010-2015 Yann Software Co,.Ltd All Rights Reserved.
 * @Description��
 * @author Yann
 * @date 2015-8-7 ����10:11:05
 * @version 1.0
 */
package com.supaiclient.app.download;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadTask {
    public boolean isPause = false;
    private Context mContext = null;
    private FileInfo mFileInfo = null;
    private ThreadDAO mDao = null;
    private int mFinised = 0;
    private int mThreadCount = 1;
    private List<DownloadThread> mDownloadThreadList = null;

    public DownloadTask(Context mContext, FileInfo mFileInfo, int count) {
        this.mContext = mContext;
        this.mFileInfo = mFileInfo;
        this.mThreadCount = count;
        mDao = new ThreadDAOImpl(mContext);
    }

    public void downLoad() {

        List<ThreadInfo> threads = mDao.getThreads(mFileInfo.getUrl());
        ThreadInfo threadInfo = null;

        if (0 == threads.size()) {
            int len = mFileInfo.getLength() / mThreadCount;
            for (int i = 0; i < mThreadCount; i++) {
                threadInfo = new ThreadInfo(i, mFileInfo.getUrl(),
                        len * i, (i + 1) * len - 1, 0);

                if (mThreadCount - 1 == i) {
                    threadInfo.setEnd(mFileInfo.getLength());
                }


                threads.add(threadInfo);
                mDao.insertThread(threadInfo);
            }
        }

        mDownloadThreadList = new ArrayList<DownloadThread>();
        for (ThreadInfo info : threads) {
            DownloadThread thread = new DownloadThread(info);
            thread.start();
            mDownloadThreadList.add(thread);
        }
    }

    private synchronized void checkAllThreadFinished() {
        boolean allFinished = true;

        for (DownloadThread thread : mDownloadThreadList) {
            if (!thread.isFinished) {
                allFinished = false;
                break;
            }
        }

        if (allFinished) {
            mDao.deleteThread(mFileInfo.getUrl());
            Intent intent = new Intent(DownloadService.ACTION_FINISHED);
            intent.putExtra("fileInfo", mFileInfo);
            mContext.sendBroadcast(intent);
        }
    }

    private class DownloadThread extends Thread {
        public boolean isFinished = false;
        private ThreadInfo mThreadInfo = null;

        public DownloadThread(ThreadInfo mInfo) {
            this.mThreadInfo = mInfo;
        }


        @Override
        public void run() {
            HttpURLConnection connection = null;
            RandomAccessFile raf = null;
            InputStream inputStream = null;

            try {
                URL url = new URL(mThreadInfo.getUrl());
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                int start = mThreadInfo.getStart() + mThreadInfo.getFinished();
                connection.setRequestProperty("Range",
                        "bytes=" + start + "-" + mThreadInfo.getEnd());

                File file = new File(DownloadService.DOWNLOAD_PATH,
                        mFileInfo.getFileName());

                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);
                Intent intent = new Intent();
                intent.setAction(DownloadService.ACTION_UPDATE);
                mFinised += mThreadInfo.getFinished();
                Log.i("mFinised", mThreadInfo.getId() + "finished = " + mThreadInfo.getFinished());
                // ��ʼ����
                if (connection.getResponseCode() == 206) {
                    inputStream = connection.getInputStream();
                    byte buf[] = new byte[1024 << 2];
                    int len = -1;
                    long time = System.currentTimeMillis();
                    while ((len = inputStream.read(buf)) != -1) {
                        raf.write(buf, 0, len);
                        mFinised += len;
                        mThreadInfo.setFinished(mThreadInfo.getFinished() + len);
                        if (System.currentTimeMillis() - time > 1000) {
                            time = System.currentTimeMillis();
                            int f = mFinised * 100 / mFileInfo.getLength();
                            if (f > mFileInfo.getFinished()) {
                                intent.putExtra("finished", f);
                                intent.putExtra("id", mFileInfo.getId());
                                mContext.sendBroadcast(intent);
                            }
                        }

                        if (isPause) {
                            mDao.updateThread(mThreadInfo.getUrl(),
                                    mThreadInfo.getId(),
                                    mThreadInfo.getFinished());

                            Log.i("mThreadInfo", mThreadInfo.getId() + "finished = " + mThreadInfo.getFinished());

                            return;
                        }
                    }

                    // ��ʶ�߳�ִ�����
                    isFinished = true;
                    checkAllThreadFinished();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (raf != null) {
                        raf.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
}
