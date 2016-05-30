package com.supaiclient.app.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.supaiclient.app.R;
import com.supaiclient.app.bean.GoodsBean;
import com.supaiclient.app.ui.activity.order.CancelOrderActivity;
import com.supaiclient.app.ui.adapter.ViewPageFragmentAdapter;
import com.supaiclient.app.ui.base.BaseActivity;
import com.supaiclient.app.util.T;
import com.supaiclient.app.widget.PagerSlidingTabStrip;

/**
 * Created by Administrator on 2016/3/13.
 * 历史 记录
 */
public class MyOrderHistoryActivity extends BaseActivity implements View.OnClickListener {


    protected PagerSlidingTabStrip mTabStrip;
    protected ViewPager mViewPager;
    protected ViewPageFragmentAdapter mTabsAdapter;
    Drawable drawable;
    GoodsBean goodsBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myorderhistory;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        getSupportActionBar().hide();


        goodsBean = (GoodsBean) getIntent().getSerializableExtra("goodsBean");

        drawable = getResources().getDrawable(R.mipmap.icon_jianjian_bai);

//        mFragments = new Fragment[2];
//        mFragments[0] = new HistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("onumber", goodsBean.getOnumber().toString());

//        mFragments[1] = new DetailsFragment();
//        mFragments[1].setArguments(bundle);
//        mFragments[0].setArguments(bundle);

        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pager_tabstrip);

        mViewPager = (ViewPager) findViewById(R.id.pager);

//        mTabsAdapter = new ViewPageFragmentAdapter(getChildFragmentManager(),
//                mTabStrip, mViewPager);

        findViewById(R.id.tv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyOrderHistoryActivity.this.finish();
            }
        });


        TextView title_content_tv = (TextView) findViewById(R.id.title_content_tv);
        TextView title_right_tv = (TextView) findViewById(R.id.title_right_tv);
        title_content_tv.setText("我的订单");

        int stats = Integer.parseInt(goodsBean.getStatus());
        if (stats <= 1) {
            title_right_tv.setText("取消订单");
            title_right_tv.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.title_right_tv:

                Intent intent = new Intent(MyOrderHistoryActivity.this, CancelOrderActivity.class);
                intent.putExtra("onumber", goodsBean.getOnumber().toString());
                startActivityForResult(intent, 200);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (!TextUtils.isEmpty(data.getStringExtra("back"))) {

                T.s("取消成功");
                this.finish();

            }
        }
    }
}
