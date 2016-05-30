package com.supaiclient.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.supaiclient.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 更新对话框
 * Created by Administrator on 2016/5/13.
 */
public class UpdataDialog extends Dialog implements View.OnClickListener {

    public UpdataDialogListener updataListener;
    @Bind(R.id.update_content)
    TextView update_content;
    @Bind(R.id.umeng_update_id_ok)
    TextView umeng_update_id_ok;
    @Bind(R.id.update_id_cancel)
    TextView update_id_cancel;
    @Bind(R.id.update_wifi_indicator)
    ImageView update_wifi_indicator;
    private boolean forceUpdate = false;
    private String updateContent;
    private String nowUpdata;


    public UpdataDialog(Context context) {
        super(context, R.style.Translucent_NoTitle);
    }

    public UpdataDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected UpdataDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setUpdataDialogListener(UpdataDialogListener updataDialogListener) {
        updataListener = updataDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_updata);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        umeng_update_id_ok.setOnClickListener(this);
        update_id_cancel.setOnClickListener(this);
    }

    public void setForceUpdate(boolean ForceUpdate) {

        this.forceUpdate = ForceUpdate;
    }

    public void setNowUpdata(String nowUpdata) {

        this.nowUpdata = nowUpdata;
    }

    @Override
    public void show() {
        super.show();
        update_content.setText(updateContent);

        if (forceUpdate) {
            update_id_cancel.setText("退出程序");
        } else {
            update_id_cancel.setText("以后再说");
        }
        if (!TextUtils.isEmpty(nowUpdata)) {
            umeng_update_id_ok.setText(nowUpdata);
        }
    }

    public void setUpdateContent(String updateContent) {

        if (!TextUtils.isEmpty(updateContent)) {
            this.updateContent = updateContent;
        }
    }

    @Override
    public void onBackPressed() {
        //屏蔽返回键
        if (!forceUpdate) {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.umeng_update_id_ok: {
                if (updataListener != null) {
                    if (umeng_update_id_ok.getText().equals("立即更新")) {
                        updataListener.updateNow();
                    } else if (umeng_update_id_ok.getText().equals("立即安装")) {
                        updataListener.nowInstall();
                    }
                }
            }
            break;
            case R.id.update_id_cancel: {

                if (updataListener != null) {

                    if (forceUpdate) {
                        updataListener.exitProgram();

                    } else {
                        updataListener.laterOn();
                    }
                }
            }
            break;

        }
    }

    public interface UpdataDialogListener {

        void nowInstall();  //立即安装

        void updateNow();   //立即更新

        void laterOn();     //以后再说

        void exitProgram(); //退出程序

    }
}
