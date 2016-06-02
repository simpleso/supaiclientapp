package com.supaiclient.app.util;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.supaiclient.app.bean.PeopleBean;

/**
 * 驾车距离计算
 * Created by Administrator on 2016/6/1.
 */

public class SuPaiUtil {

    public void getDistance(Context context, PeopleBean peopleBean_sj, PeopleBean peopleBean_jj, final DistanceListener listener) {
        if (peopleBean_sj == null) {
            return;
        }
        if (peopleBean_jj == null) {
            return;
        }
        double douLasj = 0;
        double douLnsj = 0;
        double douLajj = 0;
        double douLnjj = 0;

        String strLatsj = peopleBean_sj.getLat();
        if (!TextUtils.isEmpty(strLatsj)) {
            douLasj = Double.parseDouble(strLatsj);
        }

        String strLntsj = peopleBean_sj.getLng();
        if (!TextUtils.isEmpty(strLntsj)) {
            douLnsj = Double.parseDouble(strLntsj);
        }

        String strLatJJ = peopleBean_jj.getLat();
        if (!TextUtils.isEmpty(strLatJJ)) {
            douLajj = Double.parseDouble(strLatJJ);
        }
        String strLntJj = peopleBean_jj.getLng();
        if (!TextUtils.isEmpty(strLntJj)) {
            douLnjj = Double.parseDouble(strLntJj);
        }

        final RouteSearch routeSearch = new RouteSearch(context);
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(douLajj, douLnjj), new LatLonPoint(douLasj, douLnsj));

        // fromAndTo包含路径规划的起点和终点，drivingMode表示驾车模式
        // 第三个参数表示途经点（最多支持16个），第四个参数表示避让区域（最多支持32个），第五个参数表示避让道路
        final RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingShortDistance, null, null, "");
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {

            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int errorCode) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int errorCode) {

                //L.e("errorCode = " + errorCode);

                if (driveRouteResult == null || errorCode != 1000) {

                    T.s("连接超时，请检查网络");
                    return;
                }

                int totalLine = driveRouteResult.getPaths().size();

                L.e("共查询出" + totalLine + "条符合条件的线路");

                float min = driveRouteResult.getPaths().get(0).getDistance();

                float distance = 0;

                for (int i = 0; i < totalLine; i++) {
                    L.e("第" + i + "段距离为：" + driveRouteResult.getPaths().get(i).getDistance() + "m");
                    distance += driveRouteResult.getPaths().get(i).getDistance();
                    float tem = driveRouteResult.getPaths().get(i).getDistance();
                    min = Math.min(min, tem);
                }

                distance = min;

                if (distance == 0.0) {
                    distance = 1;
                }
                double dou = distance / 1000 + 1;//千米
                int jul = (int) (dou);// 默认 单位 为 分
                if (jul < 1) {
                    distance = 1;
                }
                distance = jul;

                L.e("距离-------------" + distance + "km");

                if (listener != null) {
                    listener.distance(distance);
                }
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }
        });

        routeSearch.calculateDriveRouteAsyn(query);
    }

    public void getDistance(Context context, double douLasj, double douLnsj, double douLajj, double douLnjj, final DistanceListener listener) {

        final RouteSearch routeSearch = new RouteSearch(context);
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(douLajj, douLnjj), new LatLonPoint(douLasj, douLnsj));

        // fromAndTo包含路径规划的起点和终点，drivingMode表示驾车模式
        // 第三个参数表示途经点（最多支持16个），第四个参数表示避让区域（最多支持32个），第五个参数表示避让道路
        final RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingShortDistance, null, null, "");
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {

            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int errorCode) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int errorCode) {

                //L.e("errorCode = " + errorCode);

                if (driveRouteResult == null || errorCode != 1000) {

                    T.s("连接超时，请检查网络");
                    return;
                }

                int totalLine = driveRouteResult.getPaths().size();

                L.e("共查询出" + totalLine + "条符合条件的线路");

                float min = driveRouteResult.getPaths().get(0).getDistance();

                float distance = 0;

                for (int i = 0; i < totalLine; i++) {
                    L.e("第" + i + "段距离为：" + driveRouteResult.getPaths().get(i).getDistance() + "m");
                    distance += driveRouteResult.getPaths().get(i).getDistance();
                    float tem = driveRouteResult.getPaths().get(i).getDistance();
                    min = Math.min(min, tem);
                }

                distance = min;

                if (distance == 0.0) {
                    distance = 1;
                }
                double dou = distance / 1000 + 1;//千米
                int jul = (int) (dou);// 默认 单位 为 分
                if (jul < 1) {
                    distance = 1;
                }
                distance = jul;

                L.e("距离-------------" + distance + "km");

                if (listener != null) {
                    listener.distance(distance);
                }
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }
        });

        routeSearch.calculateDriveRouteAsyn(query);
    }


    public interface DistanceListener {

        void distance(float distance);
    }
}
