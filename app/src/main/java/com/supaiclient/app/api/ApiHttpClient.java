package com.supaiclient.app.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

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
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;


public class ApiHttpClient {

    //    public static String API_URL = "http://192.168.1.200/sendspcr/public/index.php/Uapi";
    public static String API_URL = "http://120.27.150.115/spcr/public/index.php/Uapi";
    //    public static String API_URL = "http://http://120.27.150.115/Uapi";
    // public static FinalHttp client;
    public static KJHttp client;

    public ApiHttpClient() {

    }

    public static KJHttp getHttpClient() {

        if (client == null) {

            client = new KJHttp();
        }
        HttpConfig httpConfig = new HttpConfig();
        httpConfig.cacheTime = 0;
        client.setConfig(httpConfig);

        return client;
    }

    public static void setHttpClient(KJHttp c) {
        client = c;
    }

    public static void cancelRequests(){
        client.cancelAll();
    }

    public static void postLogin(Context context, String partUrl, HttpParams params,
                                 final RequestBasetListener handler) {
        postSend(context, partUrl, true, false, params, handler);
    }

    public static void post(Context context, String partUrl, HttpParams params,
                            final RequestBasetListener handler) {

        postSend(context, partUrl, false, true, params, handler);
    }

    public static void postNotShow(Context context, String partUrl, HttpParams params,
                                   final RequestBasetListener handler) {

        postSend(context, partUrl, false, false, params, handler);
    }

    public static void postList(Context context, String partUrl, HttpParams params,
                                final RequestBasetListener handler) {

        postSend(context, partUrl, false, false, params, handler);
    }

    public static void postSend(final Context context, String partUrl, final boolean islogin, final boolean isShowMessage, HttpParams params,
                                final RequestBasetListener handler) {

        if (!hasInternet(context)) {
            handler.onFailure(404);
            return;
        }
        final String url = API_URL + partUrl;
        if (client == null) {

            client = new KJHttp();
        }
        if (islogin == false) {
           // client.getConfig().delayTime == 15000;
        } else {

            //client.configTimeout(5000);
        }
        params.put("type", "android");

        if (!partUrl.equals(UrlUtil.LOGIN)) {

            params.put("uinfo", PropertyUtil.getUinfo());
        }
        params.put("version", BaseApplication.getVersionCode() + "");

        client.post(url, params, new HttpCallBack() {

            @Override
            public void onSuccess(String responseBody) {

                try {
                    String reStr = new String(responseBody.getBytes(), "UTF-8");

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
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);

                if (isShowMessage == true) {

                    T.s(R.string.networf_errot);
                }

                if (strMsg != null) {
                    log(url + "请求失败；》 statusCode = " + errorNo + "*****" + strMsg);
                }
            }
            
        });

        log(new StringBuilder("post ").append(partUrl).toString());
    }

    public static void log(String log) {
        Log.d("BaseApi", log);
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
