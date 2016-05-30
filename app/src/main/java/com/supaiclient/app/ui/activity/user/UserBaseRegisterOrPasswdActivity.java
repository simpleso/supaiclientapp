package com.supaiclient.app.ui.activity.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.supaiclient.app.BaseApplication;
import com.supaiclient.app.R;
import com.supaiclient.app.api.UserApi;
import com.supaiclient.app.db.PropertyUtil;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.model.UserModel;
import com.supaiclient.app.ui.base.BaseActivity;
import com.supaiclient.app.util.AppManager;
import com.supaiclient.app.util.T;
import com.supaiclient.app.util.UIHelper;
import com.supaiclient.app.util.VerifyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zgc on 2015/10/15.
 * 注册  或 忘记密码 或 重置 密码
 */
public class UserBaseRegisterOrPasswdActivity extends BaseActivity {


    public final static String OPENKEY = "bundletype";

    // 注册
    public final static int REGISTERVALUE = 1;
    //忘记密码
    public final static int FORGETPASSSWORDVALUE = 2;
    // 重置 密码
    public final static int RESETPASSSWORDVALUE = 3;

    // 快捷登录
    public final static int KUAIJIELOGIN = 4;
    /**
     * 显示 记时
     */
    private final int SHOWTIME = 0x002;
    @Bind(R.id.et_input_phonenumber)
    EditText etInputPhonenumber;
    @Bind(R.id.et_input_code)
    EditText etInputCode;
    @Bind(R.id.btn_get_code_nor)
    Button btnGetCodeNor;
    @Bind(R.id.btn_get_code_per)
    Button btnGetCodePer;
    @Bind(R.id.lin_input_code)
    LinearLayout linInputCode;
    @Bind(R.id.et_input_passwrod)
    EditText etInputPasswrod;
    @Bind(R.id.et_input_passwrod2)
    EditText etInputPasswrod2;
    @Bind(R.id.btn_over)
    Button btnOver;
    @Bind(R.id.tv_login)
    TextView tvLogin;

    @Bind(R.id.ll_yaoqingma)
    LinearLayout ll_yaoqingma;
    @Bind(R.id.et_yaoqingma)
    EditText et_yaoqingma;

    @Bind(R.id.ll_pass1)
    LinearLayout ll_pass1;
    @Bind(R.id.ll_pass2)
    LinearLayout ll_pass2;

    private int type;
    private int openType;
    private int startTime;
    private Timer timer;
    private TimerTask task;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOWTIME:

                    if (btnGetCodePer == null) {
                        return;
                    }
                    if (startTime == 0) {
                        btnGetCodePer.setText("(60秒后)重新发送");
                    } else {
                        btnGetCodePer.setText("(" + startTime + "秒后)重新发送");
                    }

                    if (startTime <= 0) {
                        task.cancel();
                        btnGetCodePer.setText("重新发送");
                        btnGetCodePer.setClickable(true);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private UserModel userModel;
    private int have = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_userbaseregisterorpasswd;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        //修改为不弹输入法 0511
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        openType = getIntent().getIntExtra(OPENKEY, REGISTERVALUE);
        userModel = new UserModel(this);

        type = getIntent().getIntExtra("type", 0);

        if (openType == REGISTERVALUE) {
            have = 0;
        }

    }

    @Override
    public void initView() {

        switch (openType) {
            case REGISTERVALUE:
                ll_yaoqingma.setVisibility(View.VISIBLE);
                setActionBarTitle("注册");
                btnOver.setText("注册");
                break;
            case FORGETPASSSWORDVALUE:

                ll_yaoqingma.setVisibility(View.GONE);
                setActionBarTitle("忘记密码");
                btnOver.setText("重置密码");
                etInputPasswrod.setHint("请输入新密码");
                etInputPasswrod2.setHint("请再次输入新密码");

                break;
            case RESETPASSSWORDVALUE:
                setActionBarTitle("重置密码");
                ll_yaoqingma.setVisibility(View.GONE);
                btnOver.setText("提交");
                etInputPhonenumber.setVisibility(View.GONE);
                linInputCode.setVisibility(View.GONE);
                break;
            case KUAIJIELOGIN:// 快捷登录
                ll_yaoqingma.setVisibility(View.GONE);
                setActionBarTitle("快捷登录");
                ll_pass1.setVisibility(View.GONE);
                ll_pass2.setVisibility(View.GONE);
                tvLogin.setVisibility(View.VISIBLE);
                btnOver.setText("确定");
                break;
        }
    }

    // 跳转登录
    @OnClick(R.id.tv_login)
    public void goLogin() {
        UIHelper.openLoginAvtivity(this);
        this.finish();
    }

    // 获取 验证码
    @OnClick(R.id.btn_get_code_nor)
    public void getCode() {

        String phone = etInputPhonenumber.getText().toString();

        if (!VerifyUtil.isPhone(phone)) {
            return;
        }

        showWaitDialog("获取验证码中..");

        if (openType == KUAIJIELOGIN) {

            UserApi.orderSendmsg(this, phone, new RequestBasetListener() {
                @Override
                public void onSuccess(String responseStr) {

                    hideWaitDialog();
                    //成功后 在显示
                    startTime = 60;
                    btnGetCodePer.setVisibility(View.VISIBLE);
                    btnGetCodeNor.setVisibility(View.GONE);

                    timekeeping();

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
            return;
        }
        UserApi.getRightCode(this, phone, have, new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

                hideWaitDialog();
                //成功后 在显示
                startTime = 60;
                btnGetCodePer.setVisibility(View.VISIBLE);
                btnGetCodeNor.setVisibility(View.GONE);

                timekeeping();

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
    }

    // 重新 获取 验证码
    @OnClick(R.id.btn_get_code_per)
    public void ResetCode() {

        String phone = etInputPhonenumber.getText().toString();

        if (!VerifyUtil.isPhone(phone)) {
            return;
        }

        showWaitDialog("获取验证码中..");

        if (openType == KUAIJIELOGIN) {

            UserApi.orderSendmsg(this, phone, new RequestBasetListener() {

                @Override
                public void onSuccess(String responseStr) {

                    hideWaitDialog();
                    //成功后 在显示
                    startTime = 60;
                    btnGetCodePer.setClickable(false);
                    timekeeping();

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
            return;
        }

        UserApi.getRightCode(this, phone, have, new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

                hideWaitDialog();
                //成功后 在显示
                startTime = 60;
                btnGetCodePer.setClickable(false);
                timekeeping();

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

    }

    // 完成
    @OnClick(R.id.btn_over)
    public void goOver() {

        String phone = etInputPhonenumber.getText().toString();
        String code = etInputCode.getText().toString();
        String password = etInputPasswrod.getText().toString();
        String pass2 = etInputPasswrod2.getText().toString();
        String yaoqingma = et_yaoqingma.getText().toString();   //加入邀请码

        switch (openType) {
            case REGISTERVALUE:// 注册 验证

                if (!userModel.rightVerify(0, phone, code, password, pass2)) {
                    return;
                }

                showWaitDialog("注册中...");

                UserApi.register(UserBaseRegisterOrPasswdActivity.this, phone, code, password, yaoqingma + "", new RequestBasetListener() {
                    @Override
                    public void onSuccess(String responseStr) {

                        hideWaitDialog();
                        T.s("注册成功");
                        try {
                            JSONObject jsonObject = new JSONObject(responseStr);
                            String uinfo = jsonObject.getString("uinfo");
                            if (!TextUtils.isEmpty(uinfo)) {
                                PropertyUtil.setUinfo(uinfo);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        AppManager.getAppManager().finishAllActivity();
                        UIHelper.openHomeAvtivity(UserBaseRegisterOrPasswdActivity.this);
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
            case FORGETPASSSWORDVALUE:// 重置 密码


                if (!userModel.rightVerify(1, phone, code, password, pass2)) {
                    return;
                }
                showWaitDialog("提交中..");
                UserApi.UPPass(UserBaseRegisterOrPasswdActivity.this, phone, code, password, new RequestBasetListener() {
                    @Override
                    public void onSuccess(String responseStr) {

                        hideWaitDialog();
                        T.s("恭喜您，密码找回成功");
                        UserBaseRegisterOrPasswdActivity.this.finish();
//                        UIHelper.openResetPasswordAvtivity(UserBaseRegisterOrPasswdActivity.this);
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
            case RESETPASSSWORDVALUE:// 设置密码 提交

                if (!userModel.setPasswword(password, pass2, 1)) {
                    return;
                }

                showWaitDialog("提交中");


                UserApi.setPass(UserBaseRegisterOrPasswdActivity.this, password, new RequestBasetListener() {
                    @Override
                    public void onSuccess(String responseStr) {

                        T.s("密码修改成功");
                        hideWaitDialog();
                        UserBaseRegisterOrPasswdActivity.this.finish();

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
            case KUAIJIELOGIN://快捷登录

                if (!userModel.getPassword(phone, code)) {
                    return;
                }
                showWaitDialog("登录中...");

                UserApi.sendFastlogin(this, phone, code, new RequestBasetListener() {
                    @Override
                    public void onSuccess(String responseStr) {

                        hideWaitDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(responseStr);
                            String uinfo = jsonObject.getString("uinfo");

                            PushManager.getInstance().initialize(UserBaseRegisterOrPasswdActivity.this);
                            PushManager.getInstance().turnOnPush(UserBaseRegisterOrPasswdActivity.this);

                            if (!TextUtils.isEmpty(uinfo)) {
                                PropertyUtil.setUinfo(uinfo);
                            }
                            UserBaseRegisterOrPasswdActivity.this.finish();
                            if (BaseApplication.getInstance().getOnLoginBackLinstener() != null) {
                                BaseApplication.getInstance().getOnLoginBackLinstener().onBack();
                            } else {
                                UIHelper.openHomeAvtivity(UserBaseRegisterOrPasswdActivity.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                return;
        }

    }

    // 计时
    private void timekeeping() {

        if (timer == null) {
            timer = new Timer();
        }

        if (task != null) {
            task.cancel();
        }
        task = new TimerTask() {
            @Override
            public void run() {
                startTime--;

                Message message = new Message();
                message.what = SHOWTIME;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    @Override
    protected void onDestroy() {

        if (task != null) {
            task.cancel();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
