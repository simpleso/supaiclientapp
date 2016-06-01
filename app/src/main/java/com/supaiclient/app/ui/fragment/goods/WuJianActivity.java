package com.supaiclient.app.ui.fragment.goods;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.supaiclient.app.R;
import com.supaiclient.app.api.UserApi;
import com.supaiclient.app.bean.FindspmanBean;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.base.BaseActivity;
import com.supaiclient.app.util.DistanceComputeUtil;
import com.supaiclient.app.util.JSonUtils;
import com.supaiclient.app.util.L;
import com.supaiclient.app.util.Util;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.Bind;


/**
 * Created by Administrator on 2016/3/2.
 * 物件 最终
 */
public class WuJianActivity extends BaseActivity {


    @Bind(R.id.tv_left)
    ImageView tvLeft;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.title_right_tv)
    TextView titleRightTv;
    @Bind(R.id.map)
    MapView mapView;
    @Bind(R.id.tv_number)
    TextView tvNumber;
    @Bind(R.id.shoujian)
    TextView shoujian;
    @Bind(R.id.julizhongdian)
    TextView julizhongdian;
    @Bind(R.id.daodashijan)
    TextView daodashijan;
    @Bind(R.id.iv_boda)
    ImageView ivBoda;
    @Bind(R.id.iv_right_title)
    ImageView iv_right_title;

    @Bind(R.id.mingzi)
    TextView mingzi;

    private PopupWindow mPopupWindow;

    static boolean saveBitmap2file(Bitmap bmp) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream("/sdcard/test.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmp.compress(format, quality, stream);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_jijian;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        mapView.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        final String onumber = getIntent().getStringExtra("onumber");

        L.e(onumber);

        showWaitDialog("请稍后...");

        //  L.e(findspmanBean.toString());

        UserApi.getodloca(this, onumber, new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

                L.e("------" + responseStr);

                try {
                    JSONArray jsonArray = new JSONArray(responseStr);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (onumber.equals(jsonObject.getString("onumber"))) {
//                            findspmanBean.setCplat(jsonObject.getDouble("cplat") + "");
//                            findspmanBean.setCplng(jsonObject.getDouble("cplng") + "");

                            FindspmanBean findspmanBean = JSonUtils.toBean(FindspmanBean.class, jsonObject.toString());

                            initSView(findspmanBean);
                            //    L.e(findspmanBean.toString());
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    hideWaitDialog();
                }
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

    private void initSView(final FindspmanBean findspmanBean) {
        TextView title_content_tv = (TextView) findViewById(R.id.title_content_tv);
        title_content_tv.setText("物件追踪");

        findViewById(R.id.tv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WuJianActivity.this.finish();
            }
        });

        final AMap aMap = mapView.getMap();
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.getUiSettings().setZoomControlsEnabled(false);

        //     float f = aMap.getMaxZoomLevel();//19.0 最小比例尺

        LatLng la = new LatLng(Double.parseDouble(findspmanBean.getCplat()), Double.parseDouble(findspmanBean.getCplng()));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(la, 17);//设置缩放比例
        aMap.animateCamera(cameraUpdate);

        View view = getLayoutInflater().inflate(R.layout.view_baidu, null);

        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromView(view);

        MarkerOptions markerOption = new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .position(la)
                .icon(bitmap)
                .draggable(true).period(50);

        //添加之前先清理
        aMap.clear();
        aMap.addMarker(markerOption);

        tvNumber.setText(findspmanBean.getOnumber());
        mingzi.setText(findspmanBean.getSuname());
        shoujian.setText(findspmanBean.getTadd());

        ivBoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(findspmanBean.getSuphone())) {
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + findspmanBean.getSuphone()));
                if (intent.resolveActivity(WuJianActivity.this.getPackageManager()) != null) {
                    WuJianActivity.this.startActivity(intent);
                }
            }
        });

        double dou1 = Double.parseDouble(findspmanBean.getCplat());
        double dou2 = Double.parseDouble(findspmanBean.getCplng());

        double dou3 = Double.parseDouble(findspmanBean.getTlat());
        double dou4 = Double.parseDouble(findspmanBean.getTlng());

        double dis = DistanceComputeUtil.getDistance2(
                dou1, dou2,
                dou3, dou4
        );
        julizhongdian.setText(dis + "KM");

        int dd = (int) dis;
        if (dd == 0) {
            dd = 3;
        } else {
            dd = dd * 3;
        }
        daodashijan.setText(dd + "分钟后");

        iv_right_title.setVisibility(View.VISIBLE);
        titleRightTv.setVisibility(View.GONE);
        iv_right_title.setImageResource(R.mipmap.iconfont_zuihouyibanfenxiang);
        View view1 = LayoutInflater.from(this).inflate(R.layout.fragment_fenxbasedialog, null);
        mPopupWindow = new PopupWindow(view1, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setOutsideTouchable(false);
        view1.findViewById(R.id.iv_qqfenx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                L.e("点击QQ");
                QQshare(findspmanBean);
                if (mPopupWindow != null) mPopupWindow.dismiss();
            }
        });

        view1.findViewById(R.id.iv_weixinfenx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                L.e("点击微信");
                weixinFen(findspmanBean);
                if (mPopupWindow != null) mPopupWindow.dismiss();
            }
        });

        view1.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow != null) mPopupWindow.dismiss();
            }
        });

        iv_right_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 转发
                mPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
            }
        });
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private void weixinFen(final FindspmanBean findspmanBean) {

        mapView.getMap().getMapScreenShot(new AMap.OnMapScreenShotListener() {

            @Override
            public void onMapScreenShot(Bitmap bitmap) {
                IWXAPI iwxapi = WXAPIFactory.createWXAPI(WuJianActivity.this, "wx89d9ac2c82d7353f", true);
                iwxapi.registerApp("wx89d9ac2c82d7353f");

                WXWebpageObject wxWebpageObject = new WXWebpageObject();
                String urlsupai = "http://www.supaichaoren.com/send/shareadd/?lat=" + findspmanBean.getCplat() + "&lng=" + findspmanBean.getCplng();
                wxWebpageObject.webpageUrl = urlsupai;

                //wxWebpageObject.webpageUrl = "http://m.amap.com/?q="+findspmanBean.getCplat()+","+findspmanBean.getCplng()+"&name=分享位置";

                WXMediaMessage wxMediaMessage = new WXMediaMessage(wxWebpageObject);
                wxMediaMessage.title = "速派超人位置分享";
                wxMediaMessage.description = "我刚让速派超人给你送了包裹，快来看看包裹到哪啦，我只想任性地请你体验一次超人服务。";

                Bitmap bit = BitmapFactory.decodeResource(WuJianActivity.this.getResources(), R.mipmap.ic_launcher);

                wxMediaMessage.thumbData = Util.bmpToByteArray(bit, true);// 设置缩略图

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = wxMediaMessage;
                req.scene = SendMessageToWX.Req.WXSceneSession;
                iwxapi.sendReq(req);

            }

            @Override
            public void onMapScreenShot(Bitmap bitmap, int i) {

            }

        });
    }

    //qq分享
    public void QQshare(final FindspmanBean findspmanBean) {

        mapView.getMap().getMapScreenShot(new AMap.OnMapScreenShotListener() {
            @Override
            public void onMapScreenShot(Bitmap bitmap) {

                File file = new File("/mnt/sdcard/test.png");
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                    //    Bitmap bit = BitmapFactory.decodeResource(WuJianActivity.this.getResources(), R.mipmap.ic_launcher);
                    if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                        out.flush();
                        out.close();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Tencent mTencent = Tencent.createInstance("1105152901", WuJianActivity.this);
                Bundle params = new Bundle();
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);

                params.putString(QQShare.SHARE_TO_QQ_TITLE, "速派超人");
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "我刚让速派超人给你送了包裹， 快来看看包裹到哪啦，我只想任性地请你体验一次超人服务。");

                //                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");

                //                String str = "E3:27:07:49:B7:29:46:F2:0A:7F:5D:16:66:2D:58:3E:BA:3A:41:07;com.supaiclient.app";
                //                String url="http://api.map.baidu.com/staticimage/v2?ak=oWdqNYeNRXwX1tqNvzr3FBlX&mcode="+str+"&center="+findspmanBean.getCplng()+","+findspmanBean.getCplat()
                //                        +"&zoom=18&markerStyles=-1,http://api.map.baidu.com/images/marker_red.png,-1,23,25";

                //                String url3 ="http://api.map.baidu.com/marker?location="+findspmanBean.getCplat()+","+findspmanBean.getCplng()+"&title=超人位置&content=超人位置&output=html&src=速派超人";
                String url3 = "http://api.map.baidu.com/place/detail?location=" + findspmanBean.getCplat() + "," + findspmanBean.getCplng() + "&title=超人位置&content=超人位置" +
                        "&src=yourCompanyName|yourAppName&output=html";
                double tx_lat;
                double tx_lon;
                double x_pi = 3.14159265358979324;
                double x = Double.parseDouble(findspmanBean.getCplng()) - 0.0065, y = Double.parseDouble(findspmanBean.getCplat()) - 0.006;
                double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
                double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
                tx_lon = z * Math.cos(theta);
                tx_lat = z * Math.sin(theta);

                String key = "OQCBZ-RY264-O57UC-D7DGK-MDOHF-W3BEO";
                //                String urll = "http://map.qq.com/?type=0&marker=coord:39.96554,116.26719;title:超人位置;addr:超人位置&referer=myapp&isOpenInfowin=1&zoomLevel=17&pointy="+tx_lon+"&pointx="+tx_lat +"&key="+key;
                //                http://apis.map.qq.com/uri/v1/marker?marker=coord:39.892326,116.342763;title:超好吃冰激凌;addr:手帕口桥北铁路道口&referer=myapp
                //                String url5 ="QQhttp://apis.map.qq.com/uri/v1/marker?marker=coord:39.892326,116.342763;title:超好吃冰激凌;addr:手帕口桥北铁路道口&referer=myapp";
                //                String url6 ="http://apis.map.qq.com/uri/1/geocoder?latlng=29.569225,106.534637&referer=pcqq";

                String urlsupai = "http://www.supaichaoren.com/send/shareadd/?lat=" + findspmanBean.getCplat() + "&lng=" + findspmanBean.getCplng();
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, urlsupai);

                //                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://m.amap.com/?q="+findspmanBean.getCplat()+","+findspmanBean.getCplng()+"&name=分享位置");
                //                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");

                params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, "" + file.toString());
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "速派超人");

                mTencent.shareToQQ(WuJianActivity.this, params, new IUiListener() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onComplete(Object response) {
                    }

                    @Override
                    public void onError(UiError e) {
                    }
                });
            }

            @Override
            public void onMapScreenShot(Bitmap bitmap, int i) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null)
            mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null)
            mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null)
            mapView.onDestroy();
    }
}