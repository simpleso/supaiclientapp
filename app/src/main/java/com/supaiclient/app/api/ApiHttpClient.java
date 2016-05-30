package com.supaiclient.app.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.supaiclient.app.BaseApplication;
import com.supaiclient.app.R;
import com.supaiclient.app.bean.BaseResponseBodyBean;
import com.supaiclient.app.db.PropertyUtil;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.base.BaseDialogActivity;
import com.supaiclient.app.util.JSonUtils;
import com.supaiclient.app.util.L;
import com.supaiclient.app.util.T;
import com.supaiclient.app.util.UIHelper;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ApiHttpClient {

    //    public static String API_URL = "http://192.168.1.200/sendspcr/public/index.php/Uapi";
    public static String API_URL = "http://120.27.150.115/spcr/public/index.php/Uapi";
    //    public static String API_URL = "http://http://120.27.150.115/Uapi";
    public static AsyncHttpClient client;
    private static String appCookie;

    public ApiHttpClient() {
    }

    public static AsyncHttpClient getHttpClient() {

        if (client == null) {

            client = new AsyncHttpClient();
        }
        client.setTimeout(15000);
        return client;
    }

    public static void setHttpClient(AsyncHttpClient c) {
        client = c;
    }

    // 关闭当前 context 所有的 请求
    public static void cancelRequests(Context context) {

        if (client != null) {
            client.cancelRequests(context, true);
        }

    }

    // 关闭 所有请求
    public static void cancelAllRequests() {
        client.cancelAllRequests(true);
    }

    public static void postLogin(Context context, String partUrl, RequestParams params,
                                 final RequestBasetListener handler) {
        postSend(context, partUrl, true, false, params, handler);
    }

    public static void post(Context context, String partUrl, RequestParams params,
                            final RequestBasetListener handler) {

        postSend(context, partUrl, false, true, params, handler);
    }

    public static void postNotShow(Context context, String partUrl, RequestParams params,
                                   final RequestBasetListener handler) {

        postSend(context, partUrl, false, false, params, handler);
    }

    public static void postList(Context context, String partUrl, RequestParams params,
                                final RequestBasetListener handler) {

        postSend(context, partUrl, false, false, params, handler);
    }

    public static void postSend(final Context context, String partUrl, final boolean islogin, final boolean isShowMessage, RequestParams params,
                                final RequestBasetListener handler) {

        if (!hasInternet(context)) {
            handler.onFailure(404);
            return;
        }
        final String url = API_URL + partUrl;
        if (client == null) {

            client = new AsyncHttpClient();
        }
        if (islogin == false) {
            client.setTimeout(10000);
        } else {

            client.setTimeout(5000);
            client.setConnectTimeout(5000);
        }
        params.put("type", "android");

        if (!partUrl.equals(UrlUtil.LOGIN)) {

            params.put("uinfo", PropertyUtil.getUinfo());
        }
        params.put("version", BaseApplication.getVersionCode());

        client.post(context, url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    String reStr = new String(responseBody, "UTF-8");

                    L.d("responseBody = " + reStr);

                    BaseResponseBodyBean baseResponseBodyBean = JSonUtils.toBean(BaseResponseBodyBean.class, reStr);

                    String sta = baseResponseBodyBean.getSta();
                    String msg = baseResponseBodyBean.getMsg();
                    if (sta.equals("suc")) {//成功

                        JSONObject jsonObject = new JSONObject(reStr);
                        String result = jsonObject.getString("result");
                        if (result.equals("[]")) {
                            handler.onSuccess("");
                        } else {
                            handler.onSuccess(result);
                        }

                    } else if (sta.equals("fail")) {// 错误

                        if (isShowMessage == true) {

                            T.s(msg);
                        }
                        handler.onSendError(202, msg);
                        return;

                    } else if (sta.equals("nodata")) {

                        handler.onSuccess("");

                    } else if (sta.equals("nologin")) {// 需要重新登录

                        if (islogin == false) {

                            handler.onSendError(203, msg);

                            // 可能密码已经泄露,

                            BaseDialogActivity.create(context)
                                    .setTitle("温馨提示")
                                    .setMessage("您的账号已在其它设备上登录, 如非本人操作,请及时修改密码或联系工作人员")
                                    .setPositiveButton("重新登录", new BaseDialogActivity.BaseDialogListener() {
                                        @Override
                                        public void onClickBack(BaseDialogActivity actionSheet) {
                                            actionSheet.dismiss();
                                            L.e("重新登录");
                                            UIHelper.openLoginAvtivity(context);
                                        }
                                    })
                                    /*.setNegativeButton("取消", new BaseDialogActivity.BaseDialogListener() {
                                        @Override
                                        public void onClickBack(BaseDialogActivity actionSheet) {
                                            actionSheet.dismiss();
                                            L.e("取消");
                                            AppManager.getAppManager().AppExit(context);
                                        }
                                    })*/
                                    .show();
                        } else {
                            handler.onSendError(203, msg);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    log(url + "请求返回数据错误");
                    if (isShowMessage == true) {

                        T.s(R.string.networf_errot);
                    }
                    handler.onFailure(100);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.onFailure(statusCode);

                if (isShowMessage == true) {

                    T.s(R.string.networf_errot);
                }

                if (error != null) {
                    log(url + "请求失败；》 statusCode = " + statusCode + "*****" + error.getMessage());
                }
            }
        });

        log(new StringBuilder("post ").append(partUrl).toString());
    }

    public static String getApiUrl() {
        return API_URL;
    }

    public static void setApiUrl(String apiUrl) {
        API_URL = apiUrl;
    }

    public static void log(String log) {
        Log.d("BaseApi", log);
    }

    //设置用户代理
    public static void setUserAgent(String userAgent) {
        client.setUserAgent(userAgent);
    }

    public static void setCookie(String cookie) {
        client.addHeader("Cookie", cookie);
    }

    public static void cleanCookie() {
        appCookie = "";
    }

    // 检查 是否有 网络
    public static boolean hasInternet(Context context) {

        if (context == null) {
            return true;
        }
        boolean flag;
        flag = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE
        )).getActiveNetworkInfo() != null;
        return flag;
    }


}
