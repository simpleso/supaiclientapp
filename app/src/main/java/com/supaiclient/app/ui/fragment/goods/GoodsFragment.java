package com.supaiclient.app.ui.fragment.goods;

import android.os.Bundle;

import com.supaiclient.app.ui.adapter.ViewPageFragmentAdapter;
import com.supaiclient.app.ui.base.BaseViewPagerFragment;

/**
 * Created by zgc on 16/2/4.
 * 我的订单
 */
public class GoodsFragment extends BaseViewPagerFragment {

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {


//        0:未付款，1:代速派 2:代取件  3:代收获 4:代 评价 5: 已取消  6:完成
        Bundle bundle = new Bundle();
        Bundle bundle2 = new Bundle();
        Bundle bundle3 = new Bundle();
        Bundle bundle4 = new Bundle();
        Bundle bundle5 = new Bundle();
        bundle.putInt("type", -1);

        adapter.addTab("全部", "全部", GoodsListFragment.class, bundle);

        bundle2.putInt("type", 1);
        adapter.addTab("待速派", "待速派", GoodsListFragment.class, bundle2);

        bundle3.putInt("type", 2);
        adapter.addTab("待取件", "待取件", GoodsListFragment.class, bundle3);

        bundle4.putInt("type", 3);
        adapter.addTab("待收货", "待收货", GoodsListFragment.class, bundle4);

        bundle5.putInt("type", 4);
        adapter.addTab("待评价", "待评价", GoodsListFragment.class, bundle5);

    }

    @Override
    protected void setScreenPageLimit() {
//        mViewPager.setOffscreenPageLimit(4);
    }
}
