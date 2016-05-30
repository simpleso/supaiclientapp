package com.supaiclient.app.ui.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.supaiclient.app.R;
import com.supaiclient.app.api.UserApi;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.util.T;

/**
 * 添加联系人
 * Created by Administrator on 2016/4/8.
 */
public class ActivityAddSender extends Activity {

    private TextView title_right_tv;
    private EditText mEtName;
    private EditText mEtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        setContentView(R.layout.activity_add_sender);

        title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        mEtName = (EditText) findViewById(R.id.et_input_name);
        mEtPhone = (EditText) findViewById(R.id.et_input_phone);


        findViewById(R.id.tv_left).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ActivityAddSender.this.finish();
            }
        });
        title_right_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = mEtName.getText().toString().trim();
                String phone = mEtPhone.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    T.s("名字不能为空");
                    return;
                }

                if (TextUtils.isEmpty(phone)) {
                    T.s("电话不能为空");
                    return;
                }

                if (phone.length() != 11) {
                    T.s("电话号码不正确");
                    return;
                }

                UserApi.userAddSendUser(ActivityAddSender.this, name, phone, new RequestBasetListener() {

                    @Override
                    public void onSuccess(String responseStr) {
                        T.s("添加成功!");
                        ActivityAddSender.this.finish();
                    }

                    @Override
                    public void onFailure(int statusCode) {
                        T.s("请检查网络!");
                    }

                    @Override
                    public void onSendError(int statusCode, String message) {
                        T.s(message);
                    }
                });
            }
        });
    }
}
