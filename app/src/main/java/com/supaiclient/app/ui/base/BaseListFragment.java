package com.supaiclient.app.ui.base;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.supaiclient.app.R;
import com.supaiclient.app.api.ApiHttpClient;
import com.supaiclient.app.bean.BaseResponseBodyBean;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.adapter.base.ListBaseAdapter;
import com.supaiclient.app.util.AppConfig;
import com.supaiclient.app.util.L;
import com.supaiclient.app.widget.EmptyLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zgc on 2015/10/8.
 */
public abstract class BaseListFragment<T extends BaseResponseBodyBean> extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener,
        AbsListView.OnScrollListener {

    @Bind(R.id.swiperefreshlayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.listview)
    protected ListView mListView;

    @Bind(R.id.error_layout)
    protected EmptyLayout mErrorLayout;

    protected int mCurrentPage = 0;// 当前页


    protected int mStoreEmptyState = -1;// 错误状态码
    protected ListBaseAdapter<T> mAdapter;
    @Bind(R.id.baseList_head_lin)
    LinearLayout baseListHeadLin;
    @Bind(R.id.baseList_head_dialog)
    LinearLayout baseListHeadDialog;
    private ParserTask mParserTask;
    private OnLodingFinsh onLodingFinsh;
    protected RequestBasetListener requestBasetListener = new RequestBasetListener() {
        @Override
        public void onSuccess(String responseStr) {

            String sss = responseStr;

            if (isAdded()) {// 判断当前 fragment 是否正在活动
                if (mState == STATE_REFRESH) {
                    onRefreshNetworkSuccess();
                }
                if (TextUtils.isEmpty(responseStr)) {// 数据为空

                    executeOnLoadDataSuccess(new ArrayList<T>());
                    executeOnLoadFinish();
                } else {
                    executeParserTask(responseStr);
                }
            }
        }

        @Override
        public void onFailure(int statusCode) {

            if (isAdded()) {
                executeOnLoadDataError();
                executeOnLoadFinish();
            }
        }

        @Override
        public void onSendError(int statusCode, String message) {

            if (!TextUtils.isEmpty(message)) {
                L.d(message);
//                com.supaisend.app.util.T.s(message);
            }
            executeOnLoadDataError();
            executeOnLoadFinish();
        }
    };

    public void setOnLodingFinsh(OnLodingFinsh onLodingFinsh) {
        this.onLodingFinsh = onLodingFinsh;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pull_refresh_listview;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    // 添加 头部 布局
    public View addListHeadView() {
        return null;
    }

    //添加 头部 筛选框
    public void addListHeadViewDialog(View headViewDialog) {

        if (headViewDialog != null) {
            baseListHeadDialog.removeAllViews();
            baseListHeadDialog.addView(headViewDialog);
        }
    }

    // 显示  头部 筛选框
    public void showListHeadViewDialog() {

        if (!baseListHeadDialog.isShown()) {

//            baseListHeadDialog.startAnimation(createTranslationOutAnimation());
            baseListHeadDialog.setVisibility(View.VISIBLE);
        } else {
            hideListHeadViewDialog();
        }
    }

    @OnClick(R.id.baseList_head_dialog)
    public void headViewDialogClick() {

        hideListHeadViewDialog();
    }

    // 显示  头部 筛选框
    public void hideListHeadViewDialog() {

        if (baseListHeadDialog.isShown()) {
//            baseListHeadDialog.startAnimation(createTranslationInAnimation());
            baseListHeadDialog.setVisibility(View.GONE);
        }
    }

    // dialog 显示 动画
    private Animation createTranslationOutAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation an = new TranslateAnimation(0, type, 0, type, 0,
                type, type, 1);

        an.setDuration(200);
        an.setFillAfter(true);
        return an;
    }

    // dialog 隐藏动画
    private Animation createTranslationInAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
//        TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
//                1, type, 0);
        TranslateAnimation an = new TranslateAnimation(0, type, 0, type, 0,
                type, 1, type);
        an.setDuration(200);
        return an;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initView(view);
    }

    public void initView(View view) {

        View headView = addListHeadView();
        if (headView != null) {

            baseListHeadLin.removeAllViews();
            baseListHeadLin.addView(headView);
        }
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);

        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCurrentPage = 0;
                mState = STATE_REFRESH;
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                // 刷新 请求 数据
                requestData(true);
            }
        });

        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);

        // 直接网络加载，写完毕后，需要看 相应代码 完成相应的工作


        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        } else {
            mAdapter = getListAdapter();
            mListView.setAdapter(mAdapter);

            mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
            mState = STATE_NONE;
//            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        }

        if (mStoreEmptyState != -1) {
            mErrorLayout.setErrorType(mStoreEmptyState);
        }
    }

    @Override
    public void onDestroyView() {
        mStoreEmptyState = mErrorLayout.getErrorState();
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {

        // 清除任务
        // 结束任务
//        cancelReadCacheTask();
//        cancelParserTask();
        ApiHttpClient.cancelRequests(getActivity());
        super.onDestroy();
    }

    @Override
    public void onResume() {

        super.onResume();
        onRefresh();
    }

    @Override
    public void onRefresh() {// 下拉刷新数据

        if (mState == STATE_REFRESH) {
            return;
        }
        // 设置顶部正在刷新
        mListView.setSelection(0);
        setSwipeRefreshLoadingState();
        mCurrentPage = 0;
        mState = STATE_REFRESH;
        requestData(true);
    }

    /**
     * 设置顶部正在加载的状态
     */
    protected void setSwipeRefreshLoadingState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            // 防止多次重复刷新
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    // 完成刷新
    protected void executeOnLoadFinish() {
        setSwipeRefreshLoadedState();
        mState = STATE_NONE;
    }

    /**
     * 设置顶部加载完毕的状态
     */
    protected void setSwipeRefreshLoadedState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int i) {

        if (mAdapter == null || mAdapter.getCount() == 0) {
            return;
        }
        // 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
        if (mState == STATE_LOADMORE || mState == STATE_REFRESH) {
            return;
        }
        // 判断是否滚动到底部
        boolean scrollEnd = false;
        try {
            if (view.getPositionForView(mAdapter.getFooterView()) == view
                    .getLastVisiblePosition())
                scrollEnd = true;
        } catch (Exception e) {
            scrollEnd = false;
        }

        if (mState == STATE_NONE && scrollEnd) {
            if (mAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE
                    || mAdapter.getState() == ListBaseAdapter.STATE_NETWORK_ERROR) {
                mCurrentPage++;
                mState = STATE_LOADMORE;
                requestData(false);
                mAdapter.setFooterViewLoading();
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    // 解析 数据
    private void executeParserTask(String data) {
        cancelParserTask();
        mParserTask = new ParserTask(data);
        mParserTask.execute();
    }

    private void cancelParserTask() {
        if (mParserTask != null) {
            mParserTask.cancel(true);
            mParserTask = null;
        }
    }

    protected void executeOnLoadDataSuccess(List<T> data) {
        if (data == null) {
            data = new ArrayList<T>();
        }

        if (mErrorLayout != null) {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            if (mCurrentPage == 0) {
                mAdapter.clear();
            }
        }

        int adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
        if ((mAdapter.getCount() + data.size()) == 0) {
            adapterState = ListBaseAdapter.STATE_EMPTY_ITEM;
        } else if (data.size() == 0
                || (data.size() < getPageSize() && mCurrentPage == 0)) {
            adapterState = ListBaseAdapter.STATE_NO_MORE;
            mAdapter.notifyDataSetChanged();
        } else {
            adapterState = ListBaseAdapter.STATE_LOAD_MORE;
        }
        mAdapter.setState(adapterState);
        mAdapter.addData(data);

        // 判断等于是因为最后有一项是listview的状态
        if (mAdapter.getCount() == 1) {

            if (needShowEmptyNoData()) {
                if (mErrorLayout != null) {
                    mErrorLayout.setErrorType(EmptyLayout.NODATA);
                }

            } else {
                mAdapter.setState(ListBaseAdapter.STATE_EMPTY_ITEM);
                mAdapter.notifyDataSetChanged();
            }
        }

        if (onLodingFinsh != null)
            onLodingFinsh.finish(data);
    }

    protected void executeOnLoadDataError() {
        if (mCurrentPage == 0) {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        } else {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mAdapter.setState(ListBaseAdapter.STATE_NETWORK_ERROR);
            mAdapter.notifyDataSetChanged();
        }
    }

    // 数据 解析
    protected ArrayList<T> parseList(String json) throws Exception {
        return null;
    }

    /**
     * 获取 列表 数据
     *
     * @param refresh 是否 刷新 数据
     */
    protected void requestData(boolean refresh) {

        // 这里 需 判断 是否存在缓存 通过抽象方法 来设置 是否添加缓存

        sendRequestData();

    }

    // 请求数据
    protected void sendRequestData() {
    }

    // 刷新成功
    protected void onRefreshNetworkSuccess() {
    }

    protected abstract ListBaseAdapter<T> getListAdapter();

    // 分页 大小
    protected int getPageSize() {
        return AppConfig.PAGE_SIZE;
    }

    /**
     * 是否需要隐藏listview，显示无数据状态
     *
     * @return
     */
    protected boolean needShowEmptyNoData() {
        return true;
    }

    //加载完成的回调接口
    public interface OnLodingFinsh<T extends BaseResponseBodyBean> {

        void finish(List<T> data);
    }

    class ParserTask extends AsyncTask<Void, Void, String> {

        private final String reponseData;
        private boolean parserError;// 解析是否 出现 异常
        private ArrayList<T> list;

        private String msg;// 服务端 提示异常

        public ParserTask(String data) {
            this.reponseData = data;
            parserError = false;
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                list = parseList(reponseData);

/*               int ret = data.getRet();
               if (ret == 0) {//成功
                   list = data.getList();
               } else {
                   msg = data.getMsg();
               }*/

            } catch (Exception e) {
                e.printStackTrace();
                parserError = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (parserError == true) {
                executeOnLoadDataError();
                executeOnLoadFinish();
            } else {
                executeOnLoadDataSuccess(list);
                executeOnLoadFinish();
            }
        }
    }

}
