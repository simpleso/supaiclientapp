package com.supaiclient.app.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.supaiclient.app.R;
import com.supaiclient.app.bean.FindspmanBean;
import com.supaiclient.app.ui.adapter.base.ListBaseAdapter;
import com.supaiclient.app.ui.fragment.goods.WuJianActivity;
import com.supaiclient.app.util.DistanceComputeUtil;
import com.supaiclient.app.util.UIHelper;
import com.supaiclient.app.widget.MarqueeTextView;

import org.kymjs.kjframe.KJBitmap;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 物件追中适配器
 * Created by Administrator on 2016/2/15.
 */
public class Main2Adapter extends ListBaseAdapter<FindspmanBean> {

    // KJBitmap kjb = new KJBitmap();

    //GeoCoder geoCoder = GeoCoder.newInstance();

    private double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    // 将 号码 发送到 拨号盘
    public static void dialPhoneNumber(Context context, String phoneNumber) {

        if (TextUtils.isEmpty(phoneNumber)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    //谷歌转百度
    private LatLonPoint bd_encrypt(double gg_lat, double gg_lon) {
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new LatLonPoint(bd_lat, bd_lon);
    }

    //百度转谷歌
    private LatLonPoint bd_decrypt(double bd_lat, double bd_lon) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new LatLonPoint(gg_lat, gg_lon);
    }

    public String getAddressByLatLng(double lat, double lng) {
        String address = "";
        Log.e("##########", "1###########" + lat + "1###########" + lng);
        try {
            Geocoder geoCoder = new Geocoder(context);

            List<Address> addresses = geoCoder.getFromLocation(lat, lng, 5);
            Log.e("##########", "2###########");
            StringBuilder sb = new StringBuilder();

            if (addresses.size() > 0) {
                Address add = addresses.get(0);
                Log.e("##########", "3###########");
//                for (int i = 0; i < add.getMaxAddressLineIndex(); i++) {
//                    sb.append(add.getAddressLine(i)).append("\n");
//                    sb.append(add.getLocality()).append("\n");
//                    sb.append(add.getPostalCode()).append("\n");
//                    sb.append(add.getCountryName()).append("\n");
//                }
                address = add.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }


    private String getLocationAddress(double latitude, double longitude) {
        String add = "";
        try {

            Geocoder geoCoder = new Geocoder(context);
            LatLonPoint mLatLonPoint = bd_decrypt(latitude, longitude);
            List<Address> addresses = geoCoder.getFromLocation(
                    mLatLonPoint.getLatitude(), mLatLonPoint.getLongitude(),
                    10);
            if (addresses.size() > 0) {

//                for (int i = 0; i < addresses.size(); i++) {
//                    L.e(addresses.get(i).toString());
//                }

                Address address = addresses.get(0);
                int maxLine = address.getMaxAddressLineIndex();

//                if (maxLine >= 2) {
//                    add = address.getAddressLine(1) + address.getAddressLine(2);
//                } else {
//                    add = address.getAddressLine(1);
//                }

                add = address.getAddressLine(0);
            }
        } catch (IOException e) {
            add = "";
            e.printStackTrace();
        }
        return add;
    }

    @Override
    protected View getRealView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.listview_main2, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final FindspmanBean findspmanBean = mDatas.get(position);

        vh.tvNumber.setText(findspmanBean.getOnumber());
        vh.tvShoujiandz.setText(findspmanBean.getTadd());
        vh.tvSfname.setText(findspmanBean.getSuname());

//        L.e("----" + getAddressByLatLng(Double.valueOf(findspmanBean.getCplat()),
//                Double.valueOf(findspmanBean.getCplng())));

        //   需要修改
        String string = getAddressByLatLng(Double.valueOf(findspmanBean.getCplat()), Double.valueOf(findspmanBean.getCplng()));
        if (TextUtils.isEmpty(string)) {
            string = "没有获取到地址";
        }
        vh.tvWeizhi.setText(string);

        //服务器给地址 要修改
        String str = "E3:27:07:49:B7:29:46:F2:0A:7F:5D:16:66:2D:58:3E:BA:3A:41:07;com.supaiclient.app";
        String url = "http://api.map.baidu.com/staticimage/v2?ak=oWdqNYeNRXwX1tqNvzr3FBlX&mcode=" + str + "&center=" + findspmanBean.getCplng() + "," + findspmanBean.getCplat()
                + "&zoom=18";

        String urlsupai = "http://www.supaichaoren.com/send/shareadd/?lat=" + findspmanBean.getCplat() + "&lng=" + findspmanBean.getCplng();

        KJBitmap kjBitmap = new KJBitmap();
        kjBitmap.display(vh.iv, url);

        double dou1 = Double.parseDouble(findspmanBean.getCplat());
        double dou2 = Double.parseDouble(findspmanBean.getCplng());

        double dou3 = Double.parseDouble(findspmanBean.getTlat());
        double dou4 = Double.parseDouble(findspmanBean.getTlng());

        double dis = DistanceComputeUtil.getDistance2(
                dou1, dou2,
                dou3, dou4
        );
        vh.tvZdjuli.setText(dis + "KM");

        int dd = (int) dis;
        if (dd == 0) {
            dd = 3;
        } else {
            dd = dd * 3;
        }
        vh.tvTime.setText(dd + "分钟后");

        vh.tvSfname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindspmanBean findspmanBean = mDatas.get(position);

                dialPhoneNumber(context, findspmanBean.getSuphone());
            }
        });


        vh.tv_ckweizhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WuJianActivity.class);
                intent.putExtra("onumber", findspmanBean.getOnumber());
                context.startActivity(intent);
            }
        });

        vh.tv_clxq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("onumber", findspmanBean.getOnumber());
                UIHelper.MyOrderHistoryFragment(context, bundle);
            }
        });

        return convertView;
    }

    class ViewHolder {

        @Bind(R.id.iv)
        ImageView iv;
        @Bind(R.id.tv_weizhi)
        MarqueeTextView tvWeizhi;
        @Bind(R.id.tv_number)
        TextView tvNumber;
        @Bind(R.id.tv_shoujiandz)
        TextView tvShoujiandz;
        @Bind(R.id.tv_sfname)
        TextView tvSfname;
        @Bind(R.id.tv_zdjuli)
        TextView tvZdjuli;
        @Bind(R.id.tv_time)
        TextView tvTime;

        @Bind(R.id.tv_ckweizhi)
        TextView tv_ckweizhi;

        @Bind(R.id.tv_clxq)
        TextView tv_clxq;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
