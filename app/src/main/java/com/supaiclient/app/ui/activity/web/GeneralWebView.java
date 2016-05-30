package com.supaiclient.app.ui.activity.web;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.supaiclient.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 通用的浏览器
 * Created by Administrator on 2016/5/9.
 */
public class GeneralWebView extends Activity implements View.OnClickListener {

    @Bind(R.id.title_content_tv)
    TextView title_content_tv;
    @Bind(R.id.tv_left)
    ImageView tv_left;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.webView)
    WebView webView;
    String mUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_general_web);
        Bundle bundle = getIntent().getExtras();
        ButterKnife.bind(this);
        tv_left.setOnClickListener(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");

        if (bundle != null) {
            mUrl = bundle.getString("url");
            webView.loadUrl(bundle.getString("url"));
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //get the newProgress and refresh progress bar
                progressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                title_content_tv.setText(title);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View v) {

        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
