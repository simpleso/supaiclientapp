package com.supaiclient.app.ui.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.supaiclient.app.R;
import com.supaiclient.app.api.ApiHttpClient;
import com.supaiclient.app.interf.WaitDialogControl;

/**
 * Created by Administrator on 2015/11/26.
 */
public class BaseFragment extends Fragment {

    public static final int STATE_NONE = 0;
    public static final int STATE_REFRESH = 1;// 刷新
    public static final int STATE_LOADMORE = 2;// 正在加载更多
    public static final int STATE_NOMORE = 3;// 加载更多
    public static final int STATE_PRESSNONE = 4;// 正在下拉但还没有到刷新的状态
    public static int mState = STATE_NONE;

    protected LayoutInflater mInflater;

    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  Fresco.initialize(getActivity());
        bundle = getArguments();
    }


    public Bundle getBundle() {
        return bundle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mInflater = inflater;
        View view = super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }


    @Override
    public void onDestroy() {
       ApiHttpClient.cancelRequests();
        super.onDestroy();
    }

    protected int getLayoutId() {
        return 0;
    }

    protected View inflateView(int resId) {
        return this.mInflater.inflate(resId, null);
    }

    public boolean onBackPressed() {
        return false;
    }

    protected ProgressDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }


    protected ProgressDialog showWaitDialog(int resid) {
        FragmentActivity activity = getActivity();
        if (activity instanceof WaitDialogControl) {
            return ((WaitDialogControl) activity).showWaitDialog(resid);
        }
        return null;
    }

    protected ProgressDialog showWaitDialog(String str) {
        FragmentActivity activity = getActivity();
        if (activity instanceof WaitDialogControl) {
            return ((WaitDialogControl) activity).showWaitDialog(str);
        }
        return null;
    }

    protected void hideWaitDialog() {
        FragmentActivity activity = getActivity();
        if (activity instanceof WaitDialogControl) {
            ((WaitDialogControl) activity).hideWaitDialog();
        }
    }


}
