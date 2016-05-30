package com.supaiclient.app.ui.fragment.contacts;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.supaiclient.app.bean.PeopleBean;
import com.supaiclient.app.interf.OnGetPeopleListener;
import com.supaiclient.app.ui.activity.order.SubmitOrderActivity;
import com.supaiclient.app.ui.adapter.PhoneBookLetterListAdapter;
import com.supaiclient.app.ui.adapter.base.ListBaseAdapter;
import com.supaiclient.app.ui.base.BaseLetterListFragment;
import com.supaiclient.app.util.PoneDataUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/12/28.
 * 电话薄 选择
 */
public class PhoneBookFragment extends BaseLetterListFragment<PeopleBean> {


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


        new PoneDataUtil(getContext(), new OnGetPeopleListener() {
            @Override
            public void OnBack(List<PeopleBean> peopleBeanList) {

                executeOnLoadDataSuccess(peopleBeanList);
                executeOnLoadFinish();
                initPingying(peopleBeanList);
            }
        }).load();
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
