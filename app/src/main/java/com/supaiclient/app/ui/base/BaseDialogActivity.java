package com.supaiclient.app.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.supaiclient.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/12/7.
 * 提示框
 */
public class BaseDialogActivity extends Activity {


    private static final String EXTRA_DISMISSED = "extra_dismissed";
    private static String message;
    private static String title;
    private static String positiveButton;
    private static String negativeButton;
    private static boolean cancelable = false;
    private static BaseDialogListener leftListener;
    private static BaseDialogListener rightListener;
    @Bind(R.id.mrl_01)
    MaterialRippleLayout mrl01;
    @Bind(R.id.mrl_02)
    MaterialRippleLayout mrl02;
    private boolean mDismissed = true;
    private ViewGroup mGroup;

    private ImageView iv_line;

    public static Builder create(Context context) {
        return new Builder(context);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_DISMISSED, mDismissed);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mDismissed = savedInstanceState.getBoolean(EXTRA_DISMISSED);
        }

        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            View focusView = this.getCurrentFocus();
            if (focusView != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }
        setContentView(R.layout.fragment_basedialog);
        ButterKnife.bind(this);

        init();
    }


//        @Override
//    protected void attachBaseContext(Context newBase) {
//
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//
//    }


    private void init() {

        TextView dialog_title_txt = (TextView) findViewById(R.id.dialog_title_txt);
        TextView dialog_message_txt = (TextView) findViewById(R.id.dialog_message_txt);


        iv_line = (ImageView) findViewById(R.id.iv_line);
        TextView dialog_cancel_txt = (TextView) findViewById(R.id.dialog_cancel_txt);
        TextView dialog_confirm_txt = (TextView) findViewById(R.id.dialog_confirm_txt);

        if (!TextUtils.isEmpty(negativeButton)) {
            dialog_cancel_txt.setText(negativeButton);
        } else {
            dialog_cancel_txt.setVisibility(View.GONE);
            iv_line.setVisibility(View.GONE);
            mrl01.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(positiveButton)) {
            dialog_confirm_txt.setText(positiveButton);
        } else {
            dialog_confirm_txt.setVisibility(View.GONE);
            iv_line.setVisibility(View.GONE);
            mrl02.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(title)) {

            dialog_title_txt.setVisibility(View.GONE);
        } else {
            dialog_title_txt.setText(title);
        }

        if (TextUtils.isEmpty(message)) {

            dialog_message_txt.setVisibility(View.GONE);
        } else {
            dialog_message_txt.setText(message);
        }


        findViewById(R.id.dialog_cancel_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (leftListener != null) {
                    leftListener.onClickBack(BaseDialogActivity.this);
                } else {
                    BaseDialogActivity.this.dismiss();
                }
            }
        });
        findViewById(R.id.dialog_confirm_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightListener != null) {
                    rightListener.onClickBack(BaseDialogActivity.this);
                } else {
                    BaseDialogActivity.this.dismiss();
                }
            }
        });
    }

    public void dismiss() {
        mDismissed = true;
        if (!isFinishing()) {
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {

        if (cancelable == true) {

            this.finish();
        }
    }

    public void show(Context context, String tag) {
        if (!mDismissed || !isFinishing()) {
            return;
        }
        mDismissed = false;
    }

    public interface BaseDialogListener {

        void onClickBack(BaseDialogActivity actionSheet);

    }

    public static class Builder {

        private Context mContext;

        private String mTag = "baseDialog";

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setTitle(String title) {

            if (TextUtils.isEmpty(title)) {
                title = "";
            }
            BaseDialogActivity.title = title;

            return this;
        }

        public Builder setMessage(String message) {

            if (TextUtils.isEmpty(message)) {
                message = "";
            }
            BaseDialogActivity.message = message;

            return this;
        }

        public Builder setCancelable(boolean cancelable) {

            BaseDialogActivity.cancelable = cancelable;
            return this;
        }

        public Builder setPositiveButton(String positiveButton, BaseDialogListener rightListener) {

            if (TextUtils.isEmpty(positiveButton)) {
                positiveButton = "";
            }
            BaseDialogActivity.positiveButton = positiveButton;
            BaseDialogActivity.rightListener = rightListener;


            return this;
        }

        public Builder setNegativeButton(String negativeButton, BaseDialogListener leftListener) {

            if (TextUtils.isEmpty(negativeButton)) {
                negativeButton = "";
            }
            BaseDialogActivity.negativeButton = negativeButton;
            BaseDialogActivity.leftListener = leftListener;
            return this;
        }

        public void show() {

            mContext.startActivity(new Intent(mContext, BaseDialogActivity.class));

        }
    }

}
