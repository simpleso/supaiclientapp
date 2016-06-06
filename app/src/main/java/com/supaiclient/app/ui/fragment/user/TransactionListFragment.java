package com.supaiclient.app.ui.fragment.user;

import com.supaiclient.app.api.ApiHttpClient;
import com.supaiclient.app.api.UrlUtil;
import com.supaiclient.app.bean.InvoiceBean;
import com.supaiclient.app.ui.adapter.TransactionListAdapter;
import com.supaiclient.app.ui.adapter.base.ListBaseAdapter;
import com.supaiclient.app.ui.base.BaseListFragment;
import com.supaiclient.app.util.JSonUtils;


import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/2/22.
 * 交易 记录
 */
public class TransactionListFragment extends BaseListFragment<InvoiceBean> {


    @Override
    protected ListBaseAdapter<InvoiceBean> getListAdapter() {
        return new TransactionListAdapter();
    }

    @Override
    protected int getPageSize() {
        return 100;
    }

    @Override
    protected void sendRequestData() {

        ApiHttpClient.postNotShow(getActivity(), UrlUtil.invoicelog, new HttpParams(), requestBasetListener);
    }

    @Override
    protected ArrayList<InvoiceBean> parseList(String json) throws Exception {
        return (ArrayList) JSonUtils.toList(InvoiceBean.class, json);
    }
}
