package com.supaiclient.app.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.supaiclient.app.BaseApplication;
import com.supaiclient.app.util.AppConfig;
import com.supaiclient.app.util.L;
import com.supaiclient.app.util.T;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2016/1/4.
 * 支付成功 回调
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, AppConfig.appId);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

        BaseReq base = baseReq;
    }

    @Override
    public void onResp(BaseResp resp) {

        String message = "";
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            switch (resp.errCode) {
                case 0:
                    message = "支付成功！";
                    BaseApplication.getInstance().setIsWxPay(true);
                    T.sOrderOver();
                    break;
                case -1:
                    message = "支付失败！";
                    T.s(message);
                    break;
                case -2:
                    message = "取消支付！";
                    T.s(message);
                    break;
                default:
                    break;
            }
            this.finish();
        }

        L.d(message + "微信支付结果, errCode = " + resp.errCode);
    }
}
