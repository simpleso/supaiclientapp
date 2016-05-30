package com.supaiclient.app.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.supaiclient.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/12/7.
 * 支付 提示框
 */
public class PayDialogActivity extends Activity implements View.OnClickListener {


    private static final int TRANSLATE_DURATION = 200;
    private static final String EXTRA_DISMISSED = "extra_dismissed";
    private static final int ALPHA_DURATION = 300;
    private static boolean cancelable = false;
    private static BaseDialogListener Listener;
    @Bind(R.id.view_coles)
    View viewColes;
    @Bind(R.id.btn_wxpay)
    RelativeLayout btnWxpay;
    @Bind(R.id.btn_zfbpay)
    RelativeLayout btnZfbpay;
    @Bind(R.id.lin)
    LinearLayout lin;
    private boolean mDismissed = true;
    private ViewGroup mGroup;

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
        setContentView(R.layout.dialog_pay);
        ButterKnife.bind(this);


        viewColes.setOnClickListener(this);
        btnWxpay.setOnClickListener(this);
        btnZfbpay.setOnClickListener(this);

        lin.startAnimation(createAlphaInAnimation());
    }

    private Animation createTranslationInAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
                1, type, 0);
        an.setDuration(TRANSLATE_DURATION);
        return an;
    }

    private Animation createAlphaInAnimation() {
        AlphaAnimation an = new AlphaAnimation(0, 1);
        an.setDuration(ALPHA_DURATION);
        return an;
    }

    private Animation createTranslationOutAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
                0, type, 1);
        an.setDuration(TRANSLATE_DURATION);
        an.setFillAfter(true);
        return an;
    }

    private Animation createAlphaOutAnimation() {
        AlphaAnimation an = new AlphaAnimation(1, 0);
        an.setDuration(ALPHA_DURATION);
        an.setFillAfter(true);
        return an;
    }

    public void dismiss() {
        mDismissed = true;
        lin.startAnimation(createAlphaOutAnimation());
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.view_coles:

                dismiss();
                break;
            case R.id.btn_wxpay:

                if (Listener != null) {
                    Listener.onClickBack(this, 0);
                }
                break;
            case R.id.btn_zfbpay:
                if (Listener != null) {
                    Listener.onClickBack(this, 1);
                }
                break;
        }
    }

    public interface BaseDialogListener {

        // type:0 微信支付，1： 支付宝 支付
        void onClickBack(PayDialogActivity actionSheet, int type);

    }

    public static class Builder {

        private Context mContext;

        private String mTag = "baseDialog";

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setListener(BaseDialogListener baseDialogListener) {

            PayDialogActivity.Listener = baseDialogListener;
            return this;
        }

        public void show() {

            mContext.startActivity(new Intent(mContext, PayDialogActivity.class));

        }
    }

}
