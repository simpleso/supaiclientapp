package com.supaiclient.app.interf;

import android.app.ProgressDialog;

/**
 * Created by Administrator on 2015/11/26.
 */
public interface WaitDialogControl {

    void hideWaitDialog();

    // 显示默认  加载中…
    ProgressDialog showWaitDialog();

    ProgressDialog showWaitDialog(int resid);

    ProgressDialog showWaitDialog(String text);

}
