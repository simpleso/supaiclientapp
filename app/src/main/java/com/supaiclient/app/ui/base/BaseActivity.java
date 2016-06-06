package com.supaiclient.app.ui.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.supaiclient.app.R;
import com.supaiclient.app.api.ApiHttpClient;
import com.supaiclient.app.interf.WaitDialogControl;
import com.supaiclient.app.ui.activity.user.ActivityAddSender;
import com.supaiclient.app.util.AppConfig;
import com.supaiclient.app.util.AppManager;
import com.supaiclient.app.util.DialogHelp;
import com.supaiclient.app.util.L;
import com.supaiclient.app.util.UIHelper;
import com.supaiclient.app.util.UiTool;
import com.testin.agent.TestinAgent;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/11/26.
 */
public class BaseActivity extends AppCompatActivity implements WaitDialogControl {


    protected ActionBar mActionBar;
    boolean hasdefaultRightOnClick = false;
    private boolean _isVisible = true;
    private ProgressDialog _waitDialog;
    private TextView mTvActionTitle;
    private addOnChick mAddOnChick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }

        AppManager.getAppManager().addActivity(this);
        mActionBar = getSupportActionBar();
        if (hasActionBar()) {
            initActionBar(mActionBar);
        }

        if (AppConfig.isUseTestinCrash) {
            TestinAgent.init(this);
        }
        ButterKnife.bind(this);
        init(savedInstanceState);
        initView();
        initData();
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//
//    }

    protected int getLayoutId() {
        return 0;
    }

    // 是否显示 action Bar
    protected boolean hasActionBar() {
        return true;
    }

    // 是否显示 右边消息 按钮---为 不是默认的---------银行卡 按钮

    // 是否显示 返回按钮
    protected boolean hasBackButton() {
        return true;
    }

    // 是否显示 右边消息 按钮
    protected boolean hasMessageRightButton() {
        return false;
    }

    public void setHasdefaultRightOnClick(boolean hasdefaultRightOnClick) {
        this.hasdefaultRightOnClick = hasdefaultRightOnClick;
    }


    @Override
    protected void onResume() {
        super.onResume();
        //注：回调 1
        if (AppConfig.isUseTestinCrash) {
            TestinAgent.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
        if (AppConfig.isUseTestinCrash) {
            TestinAgent.onPause(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ApiHttpClient.cancelRequests();
        ButterKnife.unbind(this);
    }

    public void initView() {

    }

    public void initData() {

    }

    protected void init(Bundle savedInstanceState) {
    }

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(showRightTitle()){

            getMenuInflater().inflate(R.menu.kjlogon_menu, menu);
            MenuItem search = menu.findItem(R.id.message_content);
            search.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    UIHelper.openKhLoginAvtivity(BaseActivity.this,null);
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }*/

//    @Override
//    public void onOptionsMenuClosed(Menu menu) {
//
//        switch (menu.getItemId()) {
//            case R.id.message_content:
//
//                break;
//        }
//        super.onOptionsMenuClosed(menu);
//    }

    protected boolean showRightTitle() {
        return false;
    }

    public void setAddOnChick(addOnChick mAddOnChick) {
        this.mAddOnChick = mAddOnChick;
    }

    public boolean show_add() {
        return false;
    }

    protected void initActionBar(ActionBar actionBar) {
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.base_title);
        mTvActionTitle = (TextView) mActionBar.getCustomView().findViewById(R.id.title_content_tv);
        mActionBar.setHomeButtonEnabled(true);

        if (showRightTitle()) {

            TextView mTextView = (TextView) mActionBar.getCustomView().findViewById(R.id.message_content);
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    UIHelper.openKhLoginAvtivity(BaseActivity.this, null);
                }
            });
        }
        if (show_add()) {
            ImageView mTextView = (ImageView) mActionBar.getCustomView().findViewById(R.id.content_add);
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(BaseActivity.this, ActivityAddSender.class));
                }
            });
        }

        if (hasBackButton()) {

            //  系统 自带 actionBar
          /*  mActionBar.setDisplayHomeAsUpEnabled(true);// 给左上角图标的左边加上一个返回的图标
            mActionBar.setHomeAsUpIndicator(R.mipmap.iconfont_duduyinleappicon);*/

            ImageView imageView = (ImageView) mActionBar.getCustomView().findViewById(R.id.tv_left);
            imageView.setVisibility(View.VISIBLE);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity.this.finish();
                    if (mAddOnChick != null) mAddOnChick.OnChick(v);
                }
            });


        } else {
//            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
            actionBar.setDisplayUseLogoEnabled(false);
//            int titleRes = getActionBarTitle();
//            if (titleRes != 0) {
//                actionBar.setTitle(titleRes);
//            }

        }
    }

    protected int getActionBarTitle() {
        return R.string.app_name;
    }

    public void setActionBarTitle(int resId) {
        if (resId != 0) {
            setActionBarTitle(getString(resId));
        }
    }

    public void setActionBarTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            title = getString(R.string.app_name);
        }
        if (hasActionBar() && mActionBar != null) {
            if (mTvActionTitle != null) {
                mTvActionTitle.setText(title);
            }
            mActionBar.setTitle(title);
        }
    }

    @Override
    public ProgressDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }

    @Override
    public ProgressDialog showWaitDialog(int resid) {
        return showWaitDialog(getString(resid));
    }

    @Override
    public ProgressDialog showWaitDialog(String message) {
        if (_isVisible) {
            if (_waitDialog == null) {
                _waitDialog = DialogHelp.getWaitDialog(this, message);
            }
            _waitDialog.setCancelable(false);
            _waitDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

                        L.d("加载旋转框关闭--关闭提示框--关闭网络");
                       // ApiHttpClient.getHttpClient().cancelRequests(BaseActivity.this, true);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (AppConfig.isUseTestinCrash) {
            TestinAgent.onDispatchTouchEvent(this, ev);
        }
        // 处理 点击 外部 关闭 键盘
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            View v = getCurrentFocus();
            if (UiTool.isShouldHideInput(v, ev)) {
                UiTool.hideSoftInput(v.getWindowToken(), this);
            }

//            if(hasTwoClick()){
//                long time = System.currentTimeMillis();
//                long timeD = time - lastClickTime;
//                if (0 < timeD && timeD < 1500) {
//                    return true;
//                }
//                lastClickTime = time;
//            }
        }
        return super.dispatchTouchEvent(ev);
    }


    // 是否 开启 双击不响应
//    protected boolean hasTwoClick() {
//        return true;
//    }


    public interface addOnChick {
        void OnChick(View view);
    }


}
