package com.supaiclient.app.ui.fragment.goods;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.supaiclient.app.ui.base.BaseFragment;

/**
 * 价格
 * Created by Administrator on 2016/2/28.
 */
public class JiaGeFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        WebView webview = new WebView(getActivity());
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(true);
        webview.loadUrl("http://120.27.150.115/spcr/public/index.php/price/index");
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDefaultTextEncodingName("utf-8");
        return webview;
    }
}
