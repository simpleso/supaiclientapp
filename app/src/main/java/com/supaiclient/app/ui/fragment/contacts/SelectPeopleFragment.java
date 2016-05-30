package com.supaiclient.app.ui.fragment.contacts;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.supaiclient.app.api.OrderApi;
import com.supaiclient.app.bean.PeopleBean;
import com.supaiclient.app.bean.SenduserBean;
import com.supaiclient.app.db.PropertyUtil;
import com.supaiclient.app.ui.activity.order.SubmitOrderActivity;
import com.supaiclient.app.ui.adapter.PhoneBookLetterListAdapter;
import com.supaiclient.app.ui.adapter.base.ListBaseAdapter;
import com.supaiclient.app.ui.base.BaseLetterListFragment;
import com.supaiclient.app.util.JSonUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/28.
 * 人员选择
 */
public class SelectPeopleFragment extends BaseLetterListFragment<PeopleBean> {

    @Override
    protected ListBaseAdapter<PeopleBean> getListAdapter() {
        return new PhoneBookLetterListAdapter();
    }

    @Override
    protected int getPageSize() {
        return 999999999;//禁止 分页
    }

    @Override
    protected void sendRequestData() {

        if (!TextUtils.isEmpty(PropertyUtil.getUinfo())) {// 未登陆 不用加载
            OrderApi.senduserlist(getActivity(), requestBasetListener);
        } else {
            ArrayList<PeopleBean> arrayList1 = new ArrayList<>();
            executeOnLoadDataSuccess(arrayList1);
            executeOnLoadFinish();
        }

    }

    @Override
    protected ArrayList<PeopleBean> parseList(String json) throws Exception {

        ArrayList<SenduserBean> arrayList = (ArrayList<SenduserBean>) JSonUtils.toList(SenduserBean.class, json);
        ArrayList<PeopleBean> arrayList1 = new ArrayList<>();

        for (SenduserBean sb : arrayList) {
            PeopleBean pb = new PeopleBean();
            pb.setName(sb.getJname());
            pb.setPhone(sb.getJphone());
            arrayList1.add(pb);
        }
        initPingying(arrayList1);
        return arrayList1;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        PeopleBean peopleBean = (PeopleBean) adapterView.getAdapter().getItem(i);

        if (peopleBean != null) {

            Intent intent = new Intent(getActivity(), SubmitOrderActivity.class);
            intent.putExtra("peopleBean", peopleBean);
            getActivity().setResult(200, intent);
            getActivity().finish();
        }
    }
}
