package com.supaiclient.app.ui.activity.pop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.supaiclient.app.BaseApplication;
import com.supaiclient.app.R;
import com.supaiclient.app.bean.AdvertBean;
import com.supaiclient.app.ui.activity.web.GeneralWebView;

import org.kymjs.kjframe.KJBitmap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 弹出广告
 * Created by Administrator on 2016/5/9.
 */
public class PopWinAd extends Activity implements View.OnClickListener {

    @Bind(R.id.img_ad)
    ImageView img_ad;
    @Bind(R.id.tv_cancel)
    TextView tv_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pop_ad_bomb_screen);
        ButterKnife.bind(this);
        tv_cancel.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            final AdvertBean advertBean = (AdvertBean) bundle.getSerializable("advertBean");

            KJBitmap kjBitmap = new KJBitmap();
            kjBitmap.display(img_ad, advertBean.getBphoto());

            if (!TextUtils.isEmpty(advertBean.getBurl())) {
                img_ad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(PopWinAd.this, GeneralWebView.class).putExtra("url", advertBean.getBurl()), 200);
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        BaseApplication.set("bphoto", "");
        BaseApplication.set("burl", "");
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            BaseApplication.set("bphoto", "");
            BaseApplication.set("burl", "");
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BaseApplication.set("bphoto", "");
        BaseApplication.set("burl", "");
        finish();
    }
}
