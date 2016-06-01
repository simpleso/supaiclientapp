package com.supaiclient.app.api;

import android.content.Context;

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
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.x;


/**
 * XUtils的网络模块
 * 准备改网络请求框架
 * Created by Administrator on 2016/5/30.
 */

public class ApiXutils {


    public ApiXutils() {


    }

    public static void postLogin(Context context, SuPaiParams params,
                                 final RequestBasetListener handler) {
        postSend(context, true, false, params, handler);
    }

    public static void post(Context context, SuPaiParams params,
                            final RequestBasetListener handler) {

        postSend(context, false, true, params, handler);
    }

    public static void postNotShow(Context context, SuPaiParams params,
                                   final RequestBasetListener handler) {

        postSend(context, false, false, params, handler);
    }

    public static void postList(Context context, SuPaiParams params,
                                final RequestBasetListener handler) {

        postSend(context, false, false, params, handler);
    }

    public static void postSend(final Context context, final boolean islogin, final boolean isShowMessage, final SuPaiParams params,
                                final RequestBasetListener handler) {

        params.addBodyParameter("type", "android");

        if (!params.getUri().equals(SuPaiParams.API_URL + UrlUtil.LOGIN)) {

            params.addBodyParameter("uinfo", PropertyUtil.getUinfo());
        }
        params.addBodyParameter("version", BaseApplication.getVersionCode() + "");

        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String reStr) {
                try {
                    L.e("responseBody = " + reStr);

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
                    L.e("请求返回数据错误", params.getUri());
                    if (isShowMessage == true) {

                        T.s(R.string.networf_errot);
                    }
                    handler.onFailure(100);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                if (ex instanceof HttpException) {
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    handler.onFailure(responseCode);
                }
                if (isShowMessage == true) {

                    T.s(R.string.networf_errot);
                }

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
