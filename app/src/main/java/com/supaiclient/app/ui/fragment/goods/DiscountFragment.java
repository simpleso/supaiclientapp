package com.supaiclient.app.ui.fragment.goods;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.supaiclient.app.R;
import com.supaiclient.app.api.ApiHttpClient;
import com.supaiclient.app.api.UrlUtil;
import com.supaiclient.app.bean.RedbagBean;
import com.supaiclient.app.db.PropertyUtil;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.adapter.base.BaseAdapterHelper;
import com.supaiclient.app.ui.adapter.base.QuickAdapter;
import com.supaiclient.app.ui.base.BaseFragment;
import com.supaiclient.app.util.DateUtils;
import com.supaiclient.app.util.JSonUtils;


import org.kymjs.kjframe.http.HttpParams;

import java.util.List;

/**
 * Created by Administrator on 2016/2/28.
 * 优惠券
 */
public class DiscountFragment extends BaseFragment {

    ListView listView;
    QuickAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listView = new ListView(getActivity());
        init();
        return listView;
    }

    private void init() {

        showWaitDialog("加载中..");

        final String phone = PropertyUtil.getPhone();
        HttpParams params = new HttpParams();
        ApiHttpClient.postList(getActivity(), UrlUtil.redbaggetared, params, new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {
                hideWaitDialog();

                List<RedbagBean> redbagBeanList = JSonUtils.toList(RedbagBean.class, responseStr);
                listView.setAdapter(mAdapter = new QuickAdapter<RedbagBean>(
                        getActivity(), R.layout.fragment_discount, redbagBeanList) {

                    @Override
                    protected void convert(BaseAdapterHelper helper, RedbagBean item) {

                        helper.setText(R.id.tv_jiage, item.getMoney());
                        if (!TextUtils.isEmpty(phone)) {
                            helper.setText(R.id.tv_xianshi, "限制尾号" + phone.substring(phone.length() - 4, phone.length()) + "手机使用");
                        }

                        helper.setText(R.id.tv_riqitime, "有效期至" + DateUtils.timestampToDate(item.getOuttime()));

                        helper.setViewVisibility(R.id.iv, View.GONE);
                        helper.setBackgroundRes(R.id.main_lin, R.mipmap.bg_blue);
                        if (!TextUtils.isEmpty(item.getOnumber())) {// 已经 使用

                            helper.setViewVisibility(R.id.iv, View.VISIBLE);
                            helper.setImageResource(R.id.iv, R.mipmap.iconfont_yishiyong1);
                            helper.setBackgroundRes(R.id.main_lin, R.mipmap.bg_hui);
                        } else {

                            long indexTime = DateUtils.getIndexTimeMillis();
                            long outtime = Long.parseLong(item.getOuttime());
                            if (indexTime > outtime) {
                                helper.setViewVisibility(R.id.iv, View.VISIBLE);
                                helper.setImageResource(R.id.iv, R.mipmap.iconfont_yiguoqi1);
                                helper.setBackgroundRes(R.id.main_lin, R.mipmap.bg_hui);
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(int statusCode) {
                hideWaitDialog();
            }

            @Override
            public void onSendError(int statusCode, String message) {

                hideWaitDialog();
            }
        });

    }


}
