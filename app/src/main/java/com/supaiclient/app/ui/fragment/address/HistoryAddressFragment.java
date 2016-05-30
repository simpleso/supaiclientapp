package com.supaiclient.app.ui.fragment.address;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.supaiclient.app.api.AreaApi;
import com.supaiclient.app.bean.PeopleBean;
import com.supaiclient.app.model.UserModel;
import com.supaiclient.app.ui.adapter.HistoryAddressListAdapter;
import com.supaiclient.app.ui.adapter.base.ListBaseAdapter;
import com.supaiclient.app.ui.base.BaseListFragment;
import com.supaiclient.app.ui.fragment.MainFragment;
import com.supaiclient.app.util.JSonUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/29.
 * 历史地址
 */
public class HistoryAddressFragment extends BaseListFragment<PeopleBean> {

    private int type;

    @Override
    protected ListBaseAdapter<PeopleBean> getListAdapter() {
        return new HistoryAddressListAdapter();
    }

    @Override
    protected int getPageSize() {
        return 999999999;
    }

    @Override
    protected void sendRequestData() {
        super.sendRequestData();

        Bundle bundle = getBundle();
        if (bundle != null) {

            type = bundle.getInt("type");
            boolean isLogin = new UserModel(getActivity()).isAutoLogin();

            if (isLogin) {
                AreaApi.getoAreasendaddlist(getActivity(), type, requestBasetListener);
            } else {
                executeOnLoadDataSuccess(new ArrayList<PeopleBean>());
                executeOnLoadFinish();
            }
        }
    }

    @Override
    protected ArrayList<PeopleBean> parseList(String json) throws Exception {

        return (ArrayList<PeopleBean>) JSonUtils.toList(PeopleBean.class, json);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        PeopleBean peopleBean = (PeopleBean) adapterView.getAdapter().getItem(i);
        Intent intent = new Intent(getActivity(), MainFragment.class);
        intent.putExtra("peopleBean", peopleBean);
        intent.putExtra("type", type);

        //L.e(type + ":" + peopleBean.toString());

        getActivity().setResult(200, intent);
        this.getActivity().finish();

    }
}
