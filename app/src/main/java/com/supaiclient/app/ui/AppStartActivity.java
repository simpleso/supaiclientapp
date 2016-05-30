package com.supaiclient.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.igexin.sdk.PushManager;
import com.loopj.android.http.RequestParams;
import com.supaiclient.app.BaseApplication;
import com.supaiclient.app.R;
import com.supaiclient.app.api.ApiHttpClient;
import com.supaiclient.app.api.UrlUtil;
import com.supaiclient.app.db.PropertyUtil;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.model.UserModel;
import com.supaiclient.app.ui.activity.GuideActivity;
import com.supaiclient.app.util.AppConfig;
import com.supaiclient.app.util.L;
import com.supaiclient.app.util.MD5;
import com.supaiclient.app.util.UIHelper;
import com.testin.agent.TestinAgent;
import com.testin.agent.TestinAgentConfig;
import com.umeng.analytics.MobclickAgent;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * APP 启动页
 */
public class AppStartActivity extends Activity {

    private View view;

    private boolean isautologin;

    private AlphaAnimation alphaAnimation;

    /**
     * 微信支付签名算法sign
     *
     * @param characterEncoding
     * @param parameters
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String createSign(String characterEncoding, SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + "yinakeaiduo198500103198506194447");
        String sign = MD5.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = View.inflate(this, R.layout.activity_appstart, null);
        setContentView(view);

        // 是否 启动 日志 记录
        MobclickAgent.setCatchUncaughtExceptions(true);

//        UIHelper.openHomeAvtivity(this);
        // 是否自动登录

//        new OrderDao(this).updateNumber("123123");

        if (AppConfig.isUseTestinCrash) {

            TestinAgentConfig config = new TestinAgentConfig.Builder(this)
                    .withUserInfo(PropertyUtil.getUinfo())// 用户信息-崩溃分析根据用户记录崩溃信息
                    .withDebugModel(true)        // 输出更多SDK的debug信息
                    .withErrorActivity(true)     // 发生崩溃时采集Activity信息
                    .withCollectNDKCrash(true)   // 收集NDK崩溃信息
                    .withOpenCrash(true)         // 收集崩溃信息开关
                    .withOpenEx(true)            // 是否收集异常信息
                    .withReportOnlyWifi(true)    // 仅在 WiFi 下上报崩溃信息
                    .withReportOnBack(true)      // 当APP在后台运行时,是否采集信息
                    .withQAMaster(true)          // 是否收集摇一摇反馈
                    .withCloseOption(true)       // 是否在摇一摇菜单展示‘关闭摇一摇选项’
                    .build();
            TestinAgent.init(config);
        }

        isautologin = new UserModel(this).isAutoLogin();

        if (BaseApplication.getVersionCode() > PropertyUtil.getIsGuide()) {
            startActivity(new Intent(AppStartActivity.this, GuideActivity.class));
        } else {
            if (isautologin) {
                autoLogin();
            } else {
                goStartAnimation();
            }
        }
//        test();
    }

    // 自动 登录
    private void autoLogin() {

        RequestParams params = new RequestParams();
        params.put("uinfo", PropertyUtil.getUinfo());

        ApiHttpClient.postLogin(this, UrlUtil.LOGIN, params, new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

                L.d("自动登录 成功！");
                PushManager.getInstance().initialize(AppStartActivity.this);
                PushManager.getInstance().turnOnPush(AppStartActivity.this);
                startHomeActivity();
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

    private void startLoginActivity() {

        if (alphaAnimation != null) {
            if (!alphaAnimation.isFillEnabled()) {
                alphaAnimation.cancel();
            }
        }
        UIHelper.openLoginAvtivity(AppStartActivity.this);
        AppStartActivity.this.finish();
    }

    private void goStartAnimation() {
        // 渐变展示启动屏
        alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
        alphaAnimation.setDuration(800);
        view.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {

                if (!isautologin) {

                    // startLoginActivity();
                    startHomeActivity();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
    }

    private void startHomeActivity() {

        if (alphaAnimation != null) {
            if (!alphaAnimation.isFillEnabled()) {
                alphaAnimation.cancel();
            }
        }

        UIHelper.openHomeAvtivity(AppStartActivity.this);
        AppStartActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        // 禁止返回按钮
        this.finish();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
