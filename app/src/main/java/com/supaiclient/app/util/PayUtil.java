package com.supaiclient.app.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;

import com.alipay.sdk.app.PayTask;
import com.supaiclient.app.BaseApplication;
import com.supaiclient.app.api.ApiHttpClient;
import com.supaiclient.app.api.OrderApi;
import com.supaiclient.app.api.UrlUtil;
import com.supaiclient.app.bean.OrderSubmitBean;
import com.supaiclient.app.bean.PayResult;
import com.supaiclient.app.interf.OnBack;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.model.UserModel;
import com.supaiclient.app.ui.base.PayDialogActivity;
import com.supaiclient.app.ui.fragment.MainFragment;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/1/4.
 */
public class PayUtil {


    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    private ProgressDialog _waitDialog;
    private Activity activity;
    private Context context;
    private boolean _isVisible = true;
    private int type;//type  1： 提交订单 去支付；2： 我的订单中 去 支付
    private OrderSubmitBean osb;
    private String shopName;//商品名称
    private String onumber, needpay;// 订单号 和 价格
    private String snedSign;
    private OnBack onBack;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {

                        BaseApplication.getInstance().setIsZFBPay(true);

                        if (activity == null) {
                            activity = (Activity) context;
                        }
                        if (type == 2) {

                            if (onBack != null) {
                                onBack.onBack();
                            }
                        } else {
                            Intent intent = new Intent(activity, MainFragment.class);
                            intent.putExtra("data", "data");
                            activity.setResult(200, intent);
                            activity.finish();
                            T.sOrderOver();
                        }

                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            T.s("支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误

                            T.s("支付失败");

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    T.s("检查结果为：" + msg.obj);
                    break;
                }
                default:
                    break;
            }
        }
    };

    public PayUtil(Activity context, OrderSubmitBean osb, String shopName) {

        this.activity = context;
        this.context = context;
        type = 1;
        this.osb = osb;
        this.shopName = shopName;
    }

    public PayUtil(Context context, String onumber, String shopName, OnBack onBack) {
        this.context = context;
        this.onumber = onumber;
        this.shopName = shopName;
        this.onBack = onBack;
        type = 2;
    }

    /**
     * create the order info. 创建订单信息
     */
    private final static String getOrderInfo(String subject, String body, String price, String onumber) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + AppConfig.PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + AppConfig.SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + onumber + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + ApiHttpClient.API_URL + "/pay/zfbpay"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//        orderInfo += "&return_url=\"\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    public void hideWaitDialog() {
        if (_isVisible && _waitDialog != null) {
            try {
                _waitDialog.dismiss();
                _waitDialog = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public ProgressDialog showWaitDialog(String message) {

        if (_isVisible) {
            if (_waitDialog == null) {
                _waitDialog = DialogHelp.getWaitDialog(context, message);
            }
            _waitDialog.setCancelable(false);
            _waitDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

                        L.d("加载旋转框关闭--关闭提示框--关闭网络");
                        //ApiHttpClient.getHttpClient().cancelRequests(activity, true);
                        hideWaitDialog();
                    }
                    return false;
                }
            });
            if (_waitDialog != null) {
                _waitDialog.setMessage(message);
                _waitDialog.show();
            }
            return _waitDialog;
        }
        return null;
    }

    // 支付
    public final void payStart() {

        //判断 是否 登录
        boolean isLogin = new UserModel(context).isAutoLogin();
        if (!isLogin) {

            UIHelper.openKhLoginAvtivity(context, null);
            return;
        }

        BaseApplication.isZFsucceed = true;

        PayDialogActivity.create(context).setListener(new PayDialogActivity.BaseDialogListener() {
            @Override
            public void onClickBack(PayDialogActivity actionSheet, int types) {
                // type:0 微信支付，1： 支付宝 支付
                actionSheet.dismiss();

                if (types == 0) {// 微信 支付
                    final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
                    if (!msgApi.isWXAppInstalled()) {

                        T.s("请先安装微信后，在支付");
                        return;
                    }

                    boolean isPaySupported = msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;

                    if (isPaySupported == false) {
                        T.s("当前微信版本不支持支付，请升级后再支付");
                        return;
                    }
                }
                sendDate(types);

            }
        }).show();
    }

    // 支付前 调用
    private void sendDate(final int types) {

        showWaitDialog("正在创建支付...");

        if (!TextUtils.isEmpty(needpay)) {// 判断 是否 调用 过 支付订单

            if (types == 0) {
                new GetPrepayIdTask().execute();
            } else {
                zhifubaoPay();
            }
            return;
        }
        if (type == 1) {

            OrderApi.sendorderaddorder(context, osb, new RequestBasetListener() {
                @Override
                public void onSuccess(String responseStr) {

                    //     L.e(responseStr);
                    try {
                        JSONObject jsonObject = new JSONObject(responseStr);

                        onumber = jsonObject.getString("onumber");
                        needpay = jsonObject.getString("needpay");
                        snedSign = jsonObject.getString("sign");

                        BaseApplication.getInstance().onumber = onumber;


                        // 默认 数据
                       // needpay = "0.01";

                        //   L.e(needpay + "----------");

                        if (types == 0) {
                            new GetPrepayIdTask().execute();
                        } else {
                            zhifubaoPay();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode) {
                    hideWaitDialog();
                    T.s("网络连接失败！");
                }

                @Override
                public void onSendError(int statusCode, String message) {
                    hideWaitDialog();
                    T.s("支付失败！请重新登录");
                }
            });
        } else {

            OrderApi.orderpayorder(context, onumber, new RequestBasetListener() {
                @Override
                public void onSuccess(String responseStr) {
                    try {

                        JSONObject jsonObject = new JSONObject(responseStr);

                        if (type == 2) {
//                            DateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
//                            String time=format.format(new Date());
//                            onumber = jsonObject.getString("onumber")+time;
                            onumber = jsonObject.getString("onumber");
                        } else {
                            onumber = jsonObject.getString("onumber");
                        }

                        needpay = jsonObject.getString("needprice");
                        snedSign = jsonObject.getString("sign");

                        // 默认 数据
                       // needpay = "0.01";

                        L.e(needpay + "----------");

                        if (types == 0) {
                            new GetPrepayIdTask().execute();
                        } else {
                            zhifubaoPay();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode) {
                    hideWaitDialog();
                    T.s("网络连接失败！");
                }

                @Override
                public void onSendError(int statusCode, String message) {
                    hideWaitDialog();
                    T.s("支付失败！请重新登录");
                }
            });
        }

    }

    // 支付宝
    private void zhifubaoPay() {

        String getOrderInfo = PayUtil.getOrderInfo(shopName, shopName, needpay, onumber);

        String sign = SignUtils.sign(getOrderInfo);

        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String singntype = "sign_type=\"RSA\"";
        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = getOrderInfo + "&sign=\"" + sign + "\"&"
                + singntype;

        hideWaitDialog();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                if (activity == null) {
                    activity = (Activity) context;
                }
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    // 预支付 后路 调用 微信
    private void sendPayReq(PayReq req) {

        IWXAPI msgApi = null;
        if (activity != null) {
            msgApi = WXAPIFactory.createWXAPI(activity, null);
        } else {
            msgApi = WXAPIFactory.createWXAPI(context, null);
        }

        boolean registerApp = msgApi.registerApp(AppConfig.appId);
        L.i("registerApp:" + registerApp);
        boolean sendReq = msgApi.sendReq(req);
        L.i("sendReq:" + sendReq);
    }

    private PayReq genPayReq(Map<String, String> result) {
        PayReq req = new PayReq();
        req.appId = AppConfig.appId;
        req.partnerId = AppConfig.MCH_ID;
        req.prepayId = result.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        Log.e("orion", signParams.toString());
        return req;
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    @SuppressLint("DefaultLocale")
    private String genAppSign(List<NameValuePair> params) {


        StringBuilder sb = new StringBuilder();


        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();

        for (NameValuePair list : params) {

            parameters.put(list.getName(), list.getValue());
        }

        String characterEncoding = "UTF-8";
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + AppConfig.API_KEY);

        // this.sb.append("sign str\n"+sb.toString()+"\n\n");
//        String appSign = MD5.getMessageDigest(sb.toString().getBytes())
//                .toUpperCase();
        String appSign = MD5.MD5Encode(sb.toString(), characterEncoding)
                .toUpperCase();
        Log.e("orion", appSign);
        return appSign;
    }

    private String genProductArgs() {


        StringBuffer xml = new StringBuffer();
        xml.append("</xml>");
        List<NameValuePair> packageParams = new LinkedList<>();
        packageParams
                .add(new BasicNameValuePair("appid", AppConfig.appId));

        //    L.e(shopName);

//        try {
//            shopName = URLEncoder.encode(shopName, "GBK");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        packageParams.add(new BasicNameValuePair("body", shopName));
        packageParams.add(new BasicNameValuePair("body", "商品名称"));
        packageParams.add(new BasicNameValuePair("detail", "商品名称"));
        packageParams
                .add(new BasicNameValuePair("mch_id", AppConfig.MCH_ID));
        packageParams.add(new BasicNameValuePair("nonce_str", genNonceStr()));
        packageParams.add(new BasicNameValuePair("notify_url",
                ApiHttpClient.API_URL + UrlUtil.paywxpay));
        packageParams.add(new BasicNameValuePair("out_trade_no", onumber));
        packageParams.add(new BasicNameValuePair("spbill_create_ip",
                "127.0.0.1"));

//        L.e(Float.valueOf(needpay) + "");

        int price = (int) (Double.valueOf(needpay) * 100.0f);// 默认 单位 为 分

//        L.e("price = " + price);

        packageParams.add(new BasicNameValuePair("total_fee", price + ""));
        packageParams.add(new BasicNameValuePair("trade_type", "APP"));

//        String str = genPackageSign(packageParams);
//
//        packageParams.add(new BasicNameValuePair("sign",str ));

        return toXml(genPackageSign(packageParams));
    }

    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion", e.toString());
        }
        return null;

    }

    @SuppressLint("DefaultLocale")
    private List<NameValuePair> genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();


        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();

        for (NameValuePair list : params) {

            parameters.put(list.getName(), list.getValue());
        }

        List<NameValuePair> params2 = new ArrayList<>();
        params.clear();
        String characterEncoding = "UTF-8";
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
            params2.add(new BasicNameValuePair(k, v.toString()));
        }
        sb.append("key=" + AppConfig.API_KEY);

        String sing = MD5.MD5Encode(sb.toString(), characterEncoding).toUpperCase();

        params2.add(new BasicNameValuePair("sign", sing));
        return params2;
    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");

            sb.append(params.get(i).getValue());
//            sb.append("<![CDATA["+params.get(i).getValue()+"]]>");
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        Log.e("orion", sb.toString());
        String content = "";
        try {
            content = new String(sb.toString().getBytes(), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return content;
    }

    public class GetPrepayIdTask extends
            AsyncTask<Void, Void, Map<String, String>> {

        @Override
        protected void onPreExecute() {
            L.i("onpreexecute");
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            L.i("onpostexecute");
//            if (dialog != null) {
//                dialog.dismiss();
//            }
            hideWaitDialog();
            if (result.get("return_code").equals("SUCCESS")) {
                Log.i("pay", "开始微信支付");
                result.get("开始微信支付");
                sendPayReq(genPayReq(result));
            } else {
                T.s(result.get("return_msg"));
                Log.i("pay", result.get("return_msg"));
            }
        }

        @Override
        protected void onCancelled() {
            L.i("oncancelled");
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {

            String url = String
                    .format("https://api.mch.weixin.qq.com/pay/unifiedorder");

            String entity = genProductArgs();
            Map<String, String> xml = null;
//            try {
            // 仅需对sign 做URL编码
            byte[] buf = PayHttpUtil.httpPost(url, entity);
            String content = new String(buf);
            Log.i("orion", "content" + content);
            xml = decodeXml(content);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }

            return xml;
        }
    }


}
