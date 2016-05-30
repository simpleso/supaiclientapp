package com.supaiclient.app.util;

import android.content.Context;
import android.net.ConnectivityManager;

import com.supaiclient.app.BaseApplication;

/**
 * Created by Administrator on 2015/12/3.
 */
public class NetWorkUtil {

    // 检查 是否有 网络
    public static boolean hasInternet() {

        boolean flag;
        flag = ((ConnectivityManager) BaseApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE
        )).getActiveNetworkInfo() != null;
        return flag;
    }

}
