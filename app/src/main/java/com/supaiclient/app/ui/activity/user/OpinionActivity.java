package com.supaiclient.app.ui.activity.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.supaiclient.app.R;
import com.supaiclient.app.api.UserApi;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.base.BaseActivity;
import com.supaiclient.app.util.T;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/12/11.
 * 意见反馈
 */
public class OpinionActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;

    @Override
    protected boolean hasMessageRightButton() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_opinion;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        setActionBarTitle("意见反馈");
        setOnClick();
    }

    private void setOnClick() {
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:

                String content = etContent.getText().toString();
                if (TextUtils.isEmpty(content)) {

                    T.s("请给超人提点建议吧");
                    return;
                }

                showWaitDialog("提交中...");
                UserApi.viewAdd(this, content, new RequestBasetListener() {
                    @Override
                    public void onSuccess(String responseStr) {

                        hideWaitDialog();
                        etContent.setText("");
                        T.s("感谢您的支持！");
                        OpinionActivity.this.finish();
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

        }
    }

}
