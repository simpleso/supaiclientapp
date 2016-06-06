package com.supaiclient.app.ui.fragment.integral;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.supaiclient.app.R;
import com.supaiclient.app.api.UserApi;
import com.supaiclient.app.bean.IntegralBean;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.adapter.base.BaseAdapterHelper;
import com.supaiclient.app.ui.adapter.base.QuickAdapter;
import com.supaiclient.app.util.DateUtils;
import com.supaiclient.app.util.JSonUtils;
import com.supaiclient.app.util.UIHelper;

import java.util.List;

/**
 * 积分相关
 * Created by Administrator on 2016/5/3.
 */
public class IntegralFragment extends Fragment implements AdapterView.OnItemClickListener {

    private View view;
    private ListView mListView;
    private int gltype = 0;
    private LinearLayout mLinearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.viewpager_view_list, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mListView = (ListView) view.findViewById(R.id.listView);

        mListView.setOnItemClickListener(this);
        Bundle bundle = getArguments();

        if (bundle != null) {
            gltype = bundle.getInt("INT");
        }

        mLinearLayout = (LinearLayout) view.findViewById(R.id.ll_nodate);
        UserApi.getPointList(getContext(), gltype + "", new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

                //        L.e("type : " + gltype + ":" + responseStr);

                //测试数据
                /*responseStr = "[{" +
                        "          \"gltype\":\"支出\"," +
                        "          \"points\": 20 ," +
                        "          \"glname\":\"系统发放\"," +
                        "          \"glonum\":\"ABE0417525494990\"," +
                        "          \"addtime\":\"1462326800\"," +
                        "      }]";*/

                if (TextUtils.isEmpty(responseStr)) {
                    mListView.setVisibility(View.GONE);
                    mLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    try {
                        List<IntegralBean> mList = JSonUtils.toList(IntegralBean.class, responseStr);

                        mListView.setVisibility(View.VISIBLE);
                        mLinearLayout.setVisibility(View.GONE);
                        mListView.setAdapter(new QuickAdapter<IntegralBean>(getContext(), R.layout.integra_item, mList) {
                            @Override
                            protected void convert(BaseAdapterHelper helper, IntegralBean item) {

                                helper.setText(R.id.type, item.getGltype());
                                helper.setText(R.id.glname, item.getGlname());

                                if (item.getGltype().equals("支出")) {
                                    helper.setText(R.id.points, "-" + item.getPoints());
                                    helper.setTextColor(R.id.points, Color.argb(0xFF, 0x0B, 0xEF, 0x5B));
                                } else if (item.getGltype().equals("收入")) {
                                    helper.setText(R.id.points, "+" + item.getPoints());
                                    helper.setTextColor(R.id.points, Color.argb(0xFF, 0xE8, 0x5B, 0x5E));
                                } else {

                                }
                                if (TextUtils.isEmpty(item.getGlonum())) {
                                    helper.setVisible(R.id.glonum, false);
                                } else {
                                    helper.setVisible(R.id.glonum, true);
                                    helper.setText(R.id.glonum, "订单号(" + item.getGlonum() + ")");
                                }
                                helper.setText(R.id.addtime, DateUtils.timeSimpleToDate(item.getAddtime()));
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode) {
            }

            @Override
            public void onSendError(int statusCode, String message) {
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        IntegralBean integralBean = (IntegralBean) adapterView.getAdapter().getItem(position);

        if (integralBean != null) {
            if (!TextUtils.isEmpty(integralBean.getGlonum())) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("onumber", integralBean.getGlonum());
                UIHelper.MyOrderHistoryFragment(getActivity(), bundle);
            }
        }
    }
}
