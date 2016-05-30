package com.supaiclient.app.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.supaiclient.app.R;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/12/7.
 * 提示框
 */
public class BaseFenxDialogActivity extends Activity {


    private static final String EXTRA_DISMISSED = "extra_dismissed";
    private static boolean cancelable = false;
    private static BaseDialogListener leftListener;
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

       /* InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            View focusView = this.getCurrentFocus();
            if (focusView != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }*/

        setContentView(R.layout.fragment_fenxbasedialog);
        ButterKnife.bind(this);

        init();
    }

    //    @Override
//    protected void attachBaseContext(Context newBase) {
//
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//
//    }


    private void init() {

        findViewById(R.id.iv_qqfenx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                leftListener.onClickBack(BaseFenxDialogActivity.this, 1);
                dismiss();
            }
        });

        findViewById(R.id.iv_weixinfenx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                leftListener.onClickBack(BaseFenxDialogActivity.this, 2);
                dismiss();
            }
        });

        findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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

        void onClickBack(BaseFenxDialogActivity actionSheet, int type);

    }

    public static class Builder {

        private Context mContext;

        private String mTag = "baseDialog";

        public Builder(Context context) {
            mContext = context;
        }


        public Builder setNegativeButton(BaseDialogListener leftListener) {

            BaseFenxDialogActivity.leftListener = leftListener;
            return this;
        }

        public void show() {

            mContext.startActivity(new Intent(mContext, BaseFenxDialogActivity.class));

        }
    }

}
