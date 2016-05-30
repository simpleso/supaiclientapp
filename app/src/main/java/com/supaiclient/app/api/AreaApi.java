package com.supaiclient.app.api;

import android.content.Context;

import com.loopj.android.http.RequestParams;
import com.supaiclient.app.interf.RequestBasetListener;

/**
 * Created by Administrator on 2015/12/29.
 */
public class AreaApi {

    /**
     * 获取 收货人 历史 地址
     *
     * @param context
     * @param type                 0 :收货人地址；1：寄件人 地址
     * @param requestBasetListener
     */
    public static final void getoAreasendaddlist(Context context, int type, RequestBasetListener requestBasetListener) {

        RequestParams params = new RequestParams();
        if (type == 0) {
            ApiHttpClient.postList(context, UrlUtil.areasendadd, params, requestBasetListener);
        } else {
            ApiHttpClient.postList(context, UrlUtil.areagetadd, params, requestBasetListener);
        }

    }

}
