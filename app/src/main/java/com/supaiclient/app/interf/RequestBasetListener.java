package com.supaiclient.app.interf;

/**
 * Created by Administrator on 2015/12/3.
 * 请求http 回调
 */
public interface RequestBasetListener {

    // http 请求返回成功
    void onSuccess(String responseStr);

    // http 请求 失败，一般指 网络连接错误
    void onFailure(int statusCode);

    // 数据请求错误
    void onSendError(int statusCode, String message);


}
