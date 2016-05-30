package com.supaiclient.app.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Administrator on 2015/11/30.
 */
public class L {

    public static final String LOG_TAG = "TEST";
    public static boolean DEBUG = true;

    public static final void d(String log) {
        if (TextUtils.isEmpty(log)) {
            return;
        }
        if (DEBUG)
            Log.d(LOG_TAG, log);
    }

    public static final void d(String tag, String log) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(log)) {
            return;
        }
        if (DEBUG)
            Log.d(LOG_TAG, log);
    }

    public static final void e(String log) {
        if (TextUtils.isEmpty(log)) {
            return;
        }
        if (DEBUG)
            Log.e(LOG_TAG, log);
    }

    public static final void e(String tag, String log) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(log)) {
            return;
        }
        if (DEBUG)
            Log.e(LOG_TAG, log);
    }


    public static final void i(String log) {
        if (TextUtils.isEmpty(log)) {
            return;
        }
        if (DEBUG)
            Log.i(LOG_TAG, log);
    }

    public static final void i(String tag, String log) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(log)) {
            return;
        }
        if (DEBUG)
            Log.i(LOG_TAG, log);
    }

}
