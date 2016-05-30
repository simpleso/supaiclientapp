package com.supaiclient.app.ui.fragment.webview;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.supaiclient.app.bean.MessageBean;
import com.supaiclient.app.ui.base.BaseFragment;

/**
 * 消息浏览器
 * Created by Administrator on 2016/5/5.
 */
public class WebViewFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        WebView webview = new WebView(getActivity());
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(true);

        Bundle bundle = getArguments();

        if (bundle != null) {
            MessageBean mMessageBean = (MessageBean) bundle.getSerializable("MessageBean");
            if (!TextUtils.isEmpty(mMessageBean.getUrlname())) {

            }
            if (!TextUtils.isEmpty(mMessageBean.getUrl())) {
                webview.loadUrl(mMessageBean.getUrl());
            }
        }

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //get the newProgress and refresh progress bar
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });

        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDefaultTextEncodingName("utf-8");
        return webview;
    }
}
