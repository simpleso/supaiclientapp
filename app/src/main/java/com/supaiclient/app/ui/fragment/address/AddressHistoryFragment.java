package com.supaiclient.app.ui.fragment.address;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.supaiclient.app.R;
import com.supaiclient.app.bean.PeopleBean;
import com.supaiclient.app.ui.activity.address.AddressSelectActivity;
import com.supaiclient.app.ui.activity.home.MainActivity;
import com.supaiclient.app.ui.adapter.ViewPageFragmentAdapter;
import com.supaiclient.app.ui.base.BaseFragment;
import com.supaiclient.app.ui.fragment.MainFragment;
import com.supaiclient.app.util.T;
import com.supaiclient.app.widget.EmptyLayout;
import com.supaiclient.app.widget.PagerSlidingTabStrip;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/12/29.
 * 历史地址 和 周围 地址
 */
public class AddressHistoryFragment extends BaseFragment implements OnClickListener {

    protected PagerSlidingTabStrip mTabStrip;
    protected ViewPager mViewPager;
    protected ViewPageFragmentAdapter mTabsAdapter;
    protected EmptyLayout mErrorLayout;
    @Bind(R.id.tv_inputAddre)
    TextView tvInputAddre;
    @Bind(R.id.tv_indexaddar)
    TextView tvIndexaddar;
    @Bind(R.id.pager_tabstrip)
    PagerSlidingTabStrip pagerTabstrip;
    @Bind(R.id.view_pager_line)
    View viewPagerLine;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.error_layout)
    EmptyLayout errorLayout;
    @Bind(R.id.tv_shiyong)
    TextView tvShiyong;
    @Bind(R.id.iv_type)
    ImageView ivType;

    private PeopleBean peopleBean_dw;
    private int type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_addresshistory, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {

        Bundle bundle = getArguments();
        if (bundle != null) {

            peopleBean_dw = (PeopleBean) bundle.getSerializable("peopleBean_dw");
            type = bundle.getInt("type");
            if (peopleBean_dw != null) {
                if (!TextUtils.isEmpty(peopleBean_dw.getAdd())) {//当前的 定位 地址
                    tvIndexaddar.setText(peopleBean_dw.getAdd());
                }
            } else {
                tvIndexaddar.setText("定位失败");
            }

            if (type == 0) {

                ivType.setImageResource(R.mipmap.qu);
                tvInputAddre.setHint("请输入寄件人地址");
                tvInputAddre.setHintTextColor(Color.argb(0x80, 0x80, 0x80, 0x80));
            } else {
                ivType.setImageResource(R.mipmap.icon_shou);
                tvInputAddre.setHint("请输入收件人地址");
                tvInputAddre.setHintTextColor(Color.argb(0x80, 0x80, 0x80, 0x80));
            }
        }
        tvInputAddre.setOnClickListener(this);
        tvShiyong.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mTabStrip = (PagerSlidingTabStrip) view
                .findViewById(R.id.pager_tabstrip);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);

        mErrorLayout = (EmptyLayout) view.findViewById(R.id.error_layout);

        mTabsAdapter = new ViewPageFragmentAdapter(getChildFragmentManager(),
                mTabStrip, mViewPager);

        mTabsAdapter.addTab("附近地址", "附近地址", NearbyAddressFragment.class, getArguments());
        mTabsAdapter.addTab("历史记录", "历史记录", HistoryAddressFragment.class, getArguments());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_shiyong://使用

                if (peopleBean_dw == null) {

                    T.s("定位失败！");
                    return;
                }
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("peopleBean", peopleBean_dw);
                intent.putExtra("type", type);

                getActivity().setResult(200, intent);
                this.getActivity().finish();
                break;
            case R.id.tv_inputAddre:// 去 选择 地址

                Intent intent2 = new Intent(getActivity(), AddressSelectActivity.class);
                intent2.putExtra("type", type);
                startActivityForResult(intent2, 200);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {

            PeopleBean peopleBean = (PeopleBean) data.getSerializableExtra("peopleBean");
            int type = data.getIntExtra("type", 0);

            if (peopleBean != null) {
                // 确定的 地址
                Intent intent = new Intent(getActivity(), MainFragment.class);
                intent.putExtra("peopleBean", peopleBean);
                intent.putExtra("type", type);

                //L.e(type + ":" + peopleBean.toString());

                getActivity().setResult(200, intent);
                this.getActivity().finish();
            }
        }
    }

}
