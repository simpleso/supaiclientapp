package com.supaiclient.app.api;

import org.xutils.http.RequestParams;

/**
 * 速派请求参数
 * Created by Administrator on 2016/5/31.
 */

public class SuPaiParams extends RequestParams {

    //    public static String API_URL = "http://192.168.1.200/sendspcr/public/index.php/Uapi";
    public static String API_URL = "http://120.27.150.115/spcr/public/index.php/Uapi";

    //    public static String API_URL = "http://http://120.27.150.115/Uapi";
    public SuPaiParams(String partUrl) {
        super(API_URL + partUrl);
    }
}
