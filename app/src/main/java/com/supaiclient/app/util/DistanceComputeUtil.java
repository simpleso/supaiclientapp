package com.supaiclient.app.util;


import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

import java.math.BigDecimal;

/**
 * 距离计算工具
 * Created by Administrator on 2015/12/30.
 */
public class DistanceComputeUtil {


    public final static int getDistance(double lat1, double lng1, double lat2, double lng2) {

        try {
            LatLng p1LL = new LatLng(lat1, lng1);
            LatLng p2LL = new LatLng(lat2, lng2);

            double distance = AMapUtils.calculateLineDistance(p1LL, p2LL);

            if (distance == 0.0) {
                return 1;
            }
            double dou = distance / 1000 + 0.9;//千米
            int jul = (int) (dou);// 默认 单位 为 分
            if (jul < 1) {
                return 1;
            }
            return jul;
        } catch (Exception e) {
            return 1;
        }
    }

    public static double getDistance2(double lat1, double lng1, double lat2, double lng2) {
//        GeoPoint p1LL = new GeoPoint((int) (lat1*1e6), (int) (lng1*1e6));
//        GeoPoint p2LL = new GeoPoint((int) (lat2*1e6), (int) (lng2*1e6));

        LatLng p1LL = new LatLng(lat1, lng1);
        LatLng p2LL = new LatLng(lat2, lng2);

        double distance = AMapUtils.calculateLineDistance(p1LL, p2LL);

        double dou = distance / 1000;
        BigDecimal b = new BigDecimal(dou);
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;//千米
    }

    public static double getDistance3(double lat1, double lng1, double lat2, double lng2) {
//        GeoPoint p1LL = new GeoPoint((int) (lat1*1e6), (int) (lng1*1e6));
//        GeoPoint p2LL = new GeoPoint((int) (lat2*1e6), (int) (lng2*1e6));

        LatLng p1LL = new LatLng(lat1, lng1);
        LatLng p2LL = new LatLng(lat2, lng2);

        double distance = AMapUtils.calculateLineDistance(p1LL, p2LL);

        double dou = distance / 1000;
        BigDecimal b = new BigDecimal(dou);
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;//千米
    }
}
