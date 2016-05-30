package com.supaiclient.app.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.loopj.android.http.RequestParams;
import com.supaiclient.app.BaseApplication;
import com.supaiclient.app.R;
import com.supaiclient.app.api.ApiHttpClient;
import com.supaiclient.app.api.UrlUtil;
import com.supaiclient.app.db.PropertyUtil;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.model.UserModel;
import com.supaiclient.app.ui.adapter.GuidePagerAdapter;
import com.supaiclient.app.util.AppManager;
import com.supaiclient.app.util.L;
import com.supaiclient.app.util.T;
import com.supaiclient.app.util.UIHelper;

/**
 * Created by Administrator on 2016/1/11.
 * 引导 界面
 */
public class GuideActivity extends Activity {


    int curPage = 0;
    int preState = 0;
    private boolean isautologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        AppManager.getAppManager().addActivity(this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final GuidePagerAdapter adapter = new GuidePagerAdapter(this);
        viewPager.setAdapter(adapter);
        PropertyUtil.setGuide(BaseApplication.getVersionCode());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                curPage = position;
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                L.d("_state_" + state);

                if (preState == 1 && state == 0 && curPage == 0) {
                    T.s("已经是第一页");
                }

                int count = adapter.getCount() - 1;
                if (preState == 1 && state == 0 && curPage == count) {

                    GuideActivity.this.finish();
                    isautologin = new UserModel(BaseApplication.getContext()).isAutoLogin();
                    if (isautologin) {
                        autoLogin();
                    } else {
                        //  startLoginActivity();
                        startHomeActivity();
                    }
                }
                preState = state;

            }
        });

    }

    private void startLoginActivity() {

        UIHelper.openLoginAvtivity(GuideActivity.this);
        GuideActivity.this.finish();
    }

    // 自动 登录
    private void autoLogin() {

        RequestParams params = new RequestParams();
        params.put("uinfo", PropertyUtil.getUinfo());

        ApiHttpClient.postLogin(this, UrlUtil.LOGIN, params, new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

                L.d("自动登录 成功！");
                UIHelper.openHomeAvtivity(BaseApplication.getContext());
            }

            @Override
            public void onFailure(int statusCode) {
                L.d("自动登录 失败！");
                PropertyUtil.setUinfo("");
                startHomeActivity();
            }

            @Override
            public void onSendError(int statusCode, String message) {
                L.d("自动登录 失败！");
                PropertyUtil.setUinfo("");
                startHomeActivity();
            }
        });

    }

    private void startHomeActivity() {

        UIHelper.openHomeAvtivity(GuideActivity.this);
        GuideActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
