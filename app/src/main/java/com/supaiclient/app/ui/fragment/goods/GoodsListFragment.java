package com.supaiclient.app.ui.fragment.goods;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.supaiclient.app.api.OrderApi;
import com.supaiclient.app.bean.GoodsBean;
import com.supaiclient.app.interf.OnBack;
import com.supaiclient.app.ui.adapter.GoodsAdapter;
import com.supaiclient.app.ui.adapter.base.ListBaseAdapter;
import com.supaiclient.app.ui.base.BaseListFragment;
import com.supaiclient.app.util.AppConfig;
import com.supaiclient.app.util.JSonUtils;
import com.supaiclient.app.util.UIHelper;

import java.util.ArrayList;

/**
 * Created by zgc on 16/2/4.
 * 商品 列表
 */
public class GoodsListFragment extends BaseListFragment<GoodsBean> {


    @Override
    protected ListBaseAdapter<GoodsBean> getListAdapter() {
        return new GoodsAdapter(new OnBack() {
            @Override
            public void onBack() {
                sendRequestData();
            }
        });
    }

    @Override
    protected int getPageSize() {
        return AppConfig.PAGE_SIZE;
    }

    @Override
    protected void sendRequestData() {


        String state = "0,1,2,3,4,5,6";
        int type = getArguments().getInt("type", -1);
        if (type != -1) {
            state = type + "";
        }
        OrderApi.getOrderList(getActivity(), state, getPageSize(), mCurrentPage * getPageSize(), requestBasetListener);
    }

    @Override
    protected ArrayList<GoodsBean> parseList(String json) throws Exception {

        return (ArrayList<GoodsBean>) JSonUtils.toList(GoodsBean.class, json);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        GoodsBean goodsBean = (GoodsBean) adapterView.getAdapter().getItem(i);
//        Intent intent = new Intent(getActivity(), MyOrderHistoryActivity.class);
//        intent.putExtra("goodsBean", goodsBean);
//        startActivityForResult(intent, 200);

        if (goodsBean != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("goodsBean", goodsBean);
            UIHelper.MyOrderHistoryFragment(getActivity(), bundle);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.requestData(true);
    }
}
