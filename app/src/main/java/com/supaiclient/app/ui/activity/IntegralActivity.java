package com.supaiclient.app.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.TextView;

import com.supaiclient.app.R;
import com.supaiclient.app.api.UserApi;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.adapter.InregralPagerAdapter;
import com.supaiclient.app.ui.base.BaseActivity;
import com.supaiclient.app.ui.fragment.integral.IntegralFragment;
import com.supaiclient.app.util.T;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 积分界面
 * Created by Administrator on 2016/5/3.
 */
public class IntegralActivity extends BaseActivity {

    @Bind(R.id.tab_jifen)
    TabLayout mTabLayout;
    @Bind(R.id.vp_view)
    ViewPager mViewPager;
    @Bind(R.id.tv_zongjifen)
    TextView tv_zongjifen;  //总积分
    @Bind(R.id.tv_shiyongjifen)
    TextView tv_shiyongjifen; //使用积分
    @Bind(R.id.tv_huodejifen)
    TextView tv_huodejifen;  //获得积分

    private InregralPagerAdapter adapter;
    private List<Fragment> mList;
    private List<String> mTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_integral;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();
        ButterKnife.bind(this);
        setActionBarTitle(R.string.myIntegral);

        mTitle = new ArrayList<>();

        mTitle.add("全部");
        mTitle.add("获得积分");
        mTitle.add("消费积分");

        mTabLayout.addTab(mTabLayout.newTab().setText(mTitle.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitle.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitle.get(2)));

        mList = new ArrayList<>();

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {

            IntegralFragment integralFragment = new IntegralFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("INT", i);
            integralFragment.setArguments(bundle);
            mList.add(integralFragment);
        }

        adapter = new InregralPagerAdapter(getSupportFragmentManager(), mList, mTitle);
        mViewPager.setOffscreenPageLimit(2);  //设置缓存个数
        mViewPager.setAdapter(adapter);
        mTabLayout.setTabsFromPagerAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        showWaitDialog();
        UserApi.getPointPnfo(this, new RequestBasetListener() {
                    @Override
                    public void onSuccess(String responseStr) {

                        //         L.e(responseStr);
                        /* {
                                "points": 20 ,
                                "inpoints": 40 ,
                                "outpoints": 20
                           }
                        */
                        if (!TextUtils.isEmpty(responseStr)) {
                            try {
                                JSONObject jsonObject = new JSONObject(responseStr);

                                tv_zongjifen.setText(jsonObject.getInt("points") + "");
                                tv_huodejifen.setText(jsonObject.getInt("inpoints") + "");
                                tv_shiyongjifen.setText(jsonObject.getInt("outpoints") + "");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                hideWaitDialog();
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode) {
                        //                 L.e(statusCode + "========");
                        hideWaitDialog();
                        T.s("请检查网络是否可用");
                    }

                    @Override
                    public void onSendError(int statusCode, String message) {
                        //                 L.e(statusCode + "===" + message);
                        hideWaitDialog();
                        T.s("请检查网络是否可用");
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
