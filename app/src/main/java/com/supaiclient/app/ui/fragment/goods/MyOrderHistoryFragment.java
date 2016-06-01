package com.supaiclient.app.ui.fragment.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.supaiclient.app.R;
import com.supaiclient.app.api.ApiHttpClient;
import com.supaiclient.app.api.UrlUtil;
import com.supaiclient.app.bean.OrderHistoryBean;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.activity.order.CancelOrderActivity;
import com.supaiclient.app.ui.adapter.ViewPageFragmentAdapter;
import com.supaiclient.app.ui.base.BaseFragment;
import com.supaiclient.app.util.JSonUtils;
import com.supaiclient.app.util.L;
import com.supaiclient.app.widget.PagerSlidingTabStrip;

/**
 * Created by Administrator on 2016/3/14.
 * 历史
 */
public class MyOrderHistoryFragment extends BaseFragment implements View.OnClickListener {

    protected PagerSlidingTabStrip mTabStrip;
    protected ViewPager mViewPager;
    protected ViewPageFragmentAdapter mTabsAdapter;

    private View view;
    private String onumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_myorderhistory, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mTabStrip = (PagerSlidingTabStrip) view
                .findViewById(R.id.pager_tabstrip);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);

        mTabsAdapter = new ViewPageFragmentAdapter(getChildFragmentManager(),
                mTabStrip, mViewPager);

        onumber = getArguments().getString("onumber");

        L.e(onumber + "------------");
        initViews(onumber);
    }

    private void initViews(String onumber) {

        Bundle bundle = new Bundle();
        bundle.putString("onumber", onumber);
        view.findViewById(R.id.tv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().finish();
            }
        });

        TextView title_content_tv = (TextView) view.findViewById(R.id.title_content_tv);
        final TextView title_right_tv = (TextView) view.findViewById(R.id.title_right_tv);
        title_content_tv.setText("我的订单");
        title_right_tv.setOnClickListener(this);

        RequestParams params = new RequestParams();
        params.put("onumber", onumber);

        ApiHttpClient.postNotShow(getActivity(), UrlUtil.orderhistory, params, new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

                OrderHistoryBean orderHistoryBean = JSonUtils.toBean(OrderHistoryBean.class, responseStr);

                int stats = Integer.parseInt(orderHistoryBean.getStatus());

                //0419 修改为抢单时候都可以取消订单
                if (stats <= 2) {
                    title_right_tv.setText("取消订单");
                }
            }

            @Override
            public void onFailure(int statusCode) {

            }

            @Override
            public void onSendError(int statusCode, String message) {

            }
        });

        mTabsAdapter.addTab("历史记录", "历史记录", HistoryFragment.class, bundle);
        mTabsAdapter.addTab("订单详情", "订单详情", DetailsFragment.class, bundle);
        mViewPager.setOffscreenPageLimit(1);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.title_right_tv:

                Intent intent = new Intent(getActivity(), CancelOrderActivity.class);
                intent.putExtra("onumber", onumber);
                startActivityForResult(intent, 200);

                break;
        }
    }
}
