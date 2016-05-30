package com.supaiclient.app.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.supaiclient.app.BaseApplication;
import com.supaiclient.app.R;
import com.supaiclient.app.api.UserApi;
import com.supaiclient.app.db.PropertyUtil;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.model.UserModel;
import com.supaiclient.app.ui.base.BaseActivity;
import com.supaiclient.app.util.AppConfig;
import com.supaiclient.app.util.AppManager;
import com.supaiclient.app.util.T;
import com.supaiclient.app.util.UIHelper;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/12/2.
 * 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.et_input_phonenumber)
    EditText etInputPhonenumber;
    @Bind(R.id.et_input_passwrod)
    EditText etInputPasswrod;
    @Bind(R.id.btn_over)
    Button btnOver;
    @Bind(R.id.tv_getpassword)
    TextView tvGetpassword;
    @Bind(R.id.tv_register)
    TextView tvRegister;
    @Bind(R.id.tv_qq_log)
    TextView tv_qq_log;
    @Bind(R.id.tv_wx_log)
    TextView tv_wx_log;
    BaseUiListener mBaseUiListener = new BaseUiListener();
    private long exitTime = 0;
    private UserModel loginModel;
    private Tencent mTencent;

    @Override
    protected boolean showRightTitle() {
        return true;
    }

    @Override
    protected boolean hasMessageRightButton() {
        return true;
    }

    @Override
    protected int getLayoutId() {

        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        //修改为不弹输入法 0511
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        mTencent = Tencent.createInstance(AppConfig.QQ_APP_ID, getApplicationContext());

        setActionBarTitle("登录");
        loginModel = new UserModel(this);
        setOnClick();
        PropertyUtil.setUinfo("");

        etInputPhonenumber.requestFocus();
        etInputPhonenumber.setFocusable(true);

        /*
        String phone = PropertyUtil.getPhone();
        if(!TextUtils.isEmpty(phone)){
            etInputPhonenumber.setText(phone);
        }
        */
    }

    private void setOnClick() {

        tvRegister.setOnClickListener(this);
        tvGetpassword.setOnClickListener(this);
        btnOver.setOnClickListener(this);
        tv_qq_log.setOnClickListener(this);
        tv_wx_log.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_over:// 登录

                String username = etInputPhonenumber.getText().toString().trim();
                String password = etInputPasswrod.getText().toString().trim();

                if (!loginModel.loginVerify(username, password)) {
                    return;
                }
                PropertyUtil.setPhone(username);
                showWaitDialog("登录中...");

                UserApi.login(LoginActivity.this, username, password, new RequestBasetListener() {
                    @Override
                    public void onSuccess(String responseStr) {

                        try {
                            JSONObject jsonObject = new JSONObject(responseStr);
                            String uinfo = jsonObject.getString("uinfo");
                            if (!TextUtils.isEmpty(uinfo)) {
                                PropertyUtil.setUinfo(uinfo);
                            }
                            PushManager.getInstance().initialize(LoginActivity.this);
                            PushManager.getInstance().turnOnPush(LoginActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        hideWaitDialog();
                        LoginActivity.this.finish();
                        if (BaseApplication.getInstance().getOnLoginBackLinstener() != null) {
                            BaseApplication.getInstance().getOnLoginBackLinstener().onBack();
                        } else {
                            UIHelper.openHomeAvtivity(LoginActivity.this);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode) {
                        hideWaitDialog();
                    }

                    @Override
                    public void onSendError(int statusCode, String message) {
                        hideWaitDialog();
                    }
                });

                break;
            case R.id.tv_getpassword:// 找回密码

                UIHelper.openForGetPasswordAvtivity(this);
                break;

            case R.id.tv_register:// 注册

                UIHelper.openRegisterAvtivity(this);
                break;

            case R.id.tv_qq_log:// QQ登录
                qqlonging();
                break;

            case R.id.tv_wx_log:// 微信登录
                wxLonging();
                break;
        }
    }

    private void exit() {

        if ((System.currentTimeMillis() - exitTime) > 1500) {
            Toast.makeText(getApplicationContext(), "再按一次退出速派超人",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            AppManager.getAppManager().finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_API) {

            mTencent.handleLoginData(data, mBaseUiListener);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //0420按键检测
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void qqlonging() {

        mTencent.login(this, "SCOPE = all", mBaseUiListener);
    }

    private void wxLonging() {

        IWXAPI iwxapi = WXAPIFactory.createWXAPI(this, "wx89d9ac2c82d7353f", true);

        if (iwxapi.isWXAppInstalled()) {

            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            iwxapi.sendReq(req);
        } else {
            T.s("对不起，请安装微信！");
        }
    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            //V2.0版本，参数类型由JSONObject 改成了Object,具体类型参考api文档
            //L.e("onComplete", response.toString());
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject response) {
            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
            try {
                //获得的数据是JSON格式的，获得你想获得的内容
                //如果你不知道你能获得什么，看一下下面的LOG
                //L.e("-------------" + response.toString());
//                openidString = ((JSONObject) response).getString("openid");
//                openidTextView.setText(openidString);
//                Log.e(TAG, "-------------" + openidString);
//                access_token= ((JSONObject) response).getString("access_token");
//                expires_in = ((JSONObject) response).getString("expires_in");
//
//
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            /**到此已经获得OpneID以及其他你想获得的内容了
             QQ登录成功了，我们还想获取一些QQ的基本信息，比如昵称，头像什么的，这个时候怎么办？
             sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息
             如何得到这个UserInfo类呢？  */
            QQToken qqToken = mTencent.getQQToken();
            UserInfo info = new UserInfo(getApplicationContext(), qqToken);

            info.getUserInfo(new IUiListener() {

                public void onComplete(final Object response) {
                    // TODO Auto-generated method stub
                    //L.e("---------------111111");
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 0;
                    //    mHandler.sendMessage(msg);
                    //L.e("-----111---" + response.toString());
                    /**由于图片需要下载所以这里使用了线程，如果是想获得其他文字信息直接
                     * 在mHandler里进行操作
                     *
                     */
                    new Thread() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            JSONObject json = (JSONObject) response;
                            try {
                                //     bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
//                            Message msg = new Message();
//                            msg.obj = bitmap;
//                            msg.what = 1;
//                            mHandler.sendMessage(msg);
                        }
                    }.start();
                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            });

        }

        @Override
        public void onError(UiError e) {
            //L.e("onError:", "code:" + e.errorCode + ", msg:"
            //       + e.errorMessage + ", detail:" + e.errorDetail);
        }

        @Override
        public void onCancel() {
            //L.e("onCancel", "");
        }
    }
}
