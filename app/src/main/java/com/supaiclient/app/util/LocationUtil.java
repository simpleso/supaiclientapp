package com.supaiclient.app.util;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.supaiclient.app.interf.LocationUtilLinstenner;

/**
 * Created by Administrator on 2015/12/10.
 * 定位
 */
public class LocationUtil implements AMapLocationListener {

    private static final int span = 2000000000;  //定位时间间隔
    public AMapLocationClient mLocationClient = null;
    private LocationUtilLinstenner locationUtilLinstenner;

    public LocationUtil(Context context) {

        //声明LocationClient类
        mLocationClient = new AMapLocationClient(context.getApplicationContext());
        mLocationClient.setLocationListener(this);    //注册监听函数
        init();
    }


    public LocationUtil setLocationUtilLinstenner(LocationUtilLinstenner locationUtilLinstenner) {
        this.locationUtilLinstenner = locationUtilLinstenner;
        return this;
    }

    private void init() {
        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(span);

        //优先返回GPS数据
        mLocationOption.setGpsFirst(true);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }


    // 开启 定位
    public void start() {

        mLocationClient.startLocation();
    }

    // 取消 定位
    public void stop() {

        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            locationUtilLinstenner = null;
            mLocationClient = null;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {

                if (locationUtilLinstenner != null) {
                    locationUtilLinstenner.onLocationChanged(aMapLocation);
                }
                L.d(Util.getLocationStr(aMapLocation));

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }
}
