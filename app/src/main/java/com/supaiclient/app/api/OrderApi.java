package com.supaiclient.app.api;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.loopj.android.http.RequestParams;
import com.supaiclient.app.bean.OrderSubmitBean;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.util.L;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Administrator on 2015/12/30.
 */
public class OrderApi {

    /**
     * 计算价格
     *
     * @param context
     * @param weight
     * @param distance
     * @param requestBasetListener
     */
    public static final void sendordergetprice(Context context, String weight, String distance, String taketime, RequestBasetListener requestBasetListener) {

        RequestParams params = new RequestParams();
        params.put("weight", weight);
        params.put("distance", distance);
        params.put("taketime", taketime);
        ApiHttpClient.post(context, UrlUtil.ordergetprice, params, requestBasetListener);
    }

    /**
     * 提交 订单
     *
     * @param context
     * @param osb
     * @param requestBasetListener
     */
    public static final void sendorderaddorder(final Context context, final OrderSubmitBean osb, final RequestBasetListener requestBasetListener) {

        final RequestParams params = new RequestParams();
        params.put("oname", osb.getOname());
        params.put("weight", osb.getWeight());
        params.put("distance", osb.getDistance());
        params.put("cpstyle", osb.getCpstyle());
        params.put("addprice", osb.getAddprice());
        params.put("taketype", osb.getTaketype());
        params.put("taketime", osb.getTaketime());
        params.put("message", osb.getMessage());
        params.put("rid", osb.getRid());
        params.put("points", osb.getPoints());


        //0425修改关于6.0系统的适配问题
        if (Build.VERSION.SDK_INT >= 23) {
            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionsResultAction() {
                @Override
                public void onGranted() {
                    if (!TextUtils.isEmpty(osb.getgImg())) {
                        try {
                            params.put("gimg", new File(osb.getgImg()));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onDenied(String permission) {
                    L.e("权限未获取到:" + permission);
                }
            });
        } else {
            if (!TextUtils.isEmpty(osb.getgImg())) {
                try {
                    params.put("gimg", new File(osb.getgImg()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        params.put("tname", osb.getPeopleBean_sj().getName());
        params.put("tadd", osb.getPeopleBean_sj().getAdd());
        params.put("tlat", osb.getPeopleBean_sj().getLat());
        params.put("tlng", osb.getPeopleBean_sj().getLng());
        params.put("tphone", osb.getPeopleBean_sj().getPhone());

        params.put("sendname", osb.getPeopleBean_jj().getName());
        params.put("sendaddress", osb.getPeopleBean_jj().getAdd());
        params.put("sendlat", osb.getPeopleBean_jj().getLat());
        params.put("sendlng", osb.getPeopleBean_jj().getLng());
        params.put("sendphone", osb.getPeopleBean_jj().getPhone());


        ApiHttpClient.post(context, UrlUtil.orderaddorder, params, requestBasetListener);
    }

    /**
     * 我的 订单 支付
     *
     * @param context
     * @param onumber
     * @param requestBasetListener
     */
    public static final void orderpayorder(Context context, String onumber, RequestBasetListener requestBasetListener) {

        RequestParams params = new RequestParams();
        params.put("onumber", onumber);

        ApiHttpClient.post(context, UrlUtil.orderpayorder, params, requestBasetListener);
    }

    /**
     * 寄件人列表
     *
     * @param context
     * @param requestBasetListener
     */
    public static final void senduserlist(Context context, RequestBasetListener requestBasetListener) {

        RequestParams params = new RequestParams();
        ApiHttpClient.post(context, UrlUtil.senduserlist, params, requestBasetListener);
    }

    /**
     * 得到 订单 列表
     *
     * @param context
     * @param type                 0:未付款，1:代速派 2:代取件  3:代收获 4:代 评价 5: 已取消  6:完成
     * @param requestBasetListener
     */
    public static final void getOrderList(Context context, String type, int limit, int start, RequestBasetListener requestBasetListener) {

        RequestParams params = new RequestParams();
        params.put("pagesize", limit);
        params.put("str", start + 1);
        params.put("status", type);

        ApiHttpClient.post(context, UrlUtil.orderlist, params, requestBasetListener);
    }

    /**
     * 物件 最终 列表 数据
     *
     * @param context
     * @param limit
     * @param start
     * @param requestBasetListener
     */
    public static final void orderfindspman(Context context, int limit, int start, RequestBasetListener requestBasetListener) {

        RequestParams params = new RequestParams();
        params.put("pagesize", limit);
        params.put("str", start + 1);
        ApiHttpClient.postNotShow(context, UrlUtil.orderfindspman, params, requestBasetListener);
    }


    /**
     * 获取 周围 快递员
     *
     * @param context
     * @param lat
     * @param lng
     * @param requestBasetListener
     */
    public static final void getsenduser(Context context, String lat, String lng, RequestBasetListener
            requestBasetListener) {

        RequestParams params = new RequestParams();
        params.put("lat", lat);
        params.put("lng", lng);
        ApiHttpClient.postNotShow(context, UrlUtil.getsenduser, params, requestBasetListener);
    }

    /**
     * 获取订单详情
     *
     * @param context
     * @param requestBasetListener
     */
    public static final void getorderdetail(Context context, String onumber, RequestBasetListener
            requestBasetListener) {

        RequestParams params = new RequestParams();
        params.put("onumber", onumber);
        ApiHttpClient.postNotShow(context, UrlUtil.orderdetail, params, requestBasetListener);
    }

}
