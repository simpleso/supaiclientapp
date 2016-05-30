package com.supaiclient.app.ui.fragment.address;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.supaiclient.app.api.UserApi;
import com.supaiclient.app.bean.PeopleBean;
import com.supaiclient.app.model.UserModel;
import com.supaiclient.app.ui.adapter.CommonAddressListAdapter;
import com.supaiclient.app.ui.adapter.base.ListBaseAdapter;
import com.supaiclient.app.ui.base.BaseListFragment;
import com.supaiclient.app.ui.fragment.MainFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/29.
 * 常用地址
 */
public class CommonAddressFragment extends BaseListFragment<PeopleBean> {

    private int type;

    @Override
    protected ListBaseAdapter<PeopleBean> getListAdapter() {
        return new CommonAddressListAdapter();
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
                UserApi.userListSendUser(getActivity(), requestBasetListener);
            } else {
                executeOnLoadDataSuccess(new ArrayList<PeopleBean>());
                executeOnLoadFinish();
            }
        }


    }

    @Override
    protected ArrayList<PeopleBean> parseList(String json) throws Exception {


        //以下是没有给接口是这样处理
        //以后有接口是在修改

        ArrayList<PeopleBean> peopleBeans = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject j = jsonArray.getJSONObject(i);
            PeopleBean p = new PeopleBean();
            String s = j.getString("jname");
            s = s + "./";
            s = s + "重庆市新干线大厦";
            String[] strings = s.split("./");
            p.setName(strings[0]);
            p.setAdd(strings[1]);
            p.setPhone(j.getString("jphone"));
            peopleBeans.add(p);
        }
        return peopleBeans;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        PeopleBean peopleBean = (PeopleBean) adapterView.getAdapter().getItem(i);
        if (peopleBean != null) {
            Intent intent = new Intent(getActivity(), MainFragment.class);
            intent.putExtra("peopleBean", peopleBean);
            intent.putExtra("type", type);
            getActivity().setResult(200, intent);
            this.getActivity().finish();
        }
    }
}
