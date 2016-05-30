package com.supaiclient.app.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.loopj.android.http.RequestParams;
import com.supaiclient.app.BaseApplication;
import com.supaiclient.app.R;
import com.supaiclient.app.api.ApiHttpClient;
import com.supaiclient.app.api.OrderApi;
import com.supaiclient.app.api.UrlUtil;
import com.supaiclient.app.bean.LocationBean;
import com.supaiclient.app.bean.OrderSubmitBean;
import com.supaiclient.app.bean.PeopleBean;
import com.supaiclient.app.bean.RedbagBean;
import com.supaiclient.app.interf.OnLoginBackLinstener;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.model.UserModel;
import com.supaiclient.app.ui.activity.order.SubmitOrderActivity;
import com.supaiclient.app.ui.base.TimeDialogActivity;
import com.supaiclient.app.util.DateUtils;
import com.supaiclient.app.util.JSonUtils;
import com.supaiclient.app.util.L;
import com.supaiclient.app.util.T;
import com.supaiclient.app.util.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/12/24.
 * 首页 呼叫超人
 */
public class MainFragment extends Fragment implements OnClickListener, GeocodeSearch.OnGeocodeSearchListener, LocationSource,
        AMapLocationListener {


    private static final String TAG = "MainFragment";

    private static float distance;//距离
    private static AMapLocation location;// 当前 定位 数据
    protected int mCurrentPage = 1;// 当前页
    @Bind(R.id.mapView)
    MapView map;
    @Bind(R.id.tv_showMapIV)
    ImageView tvShowMapIV;
    @Bind(R.id.tv_qujianaddr)
    TextView tvQujianaddr;
    @Bind(R.id.lin_show)
    LinearLayout linShow;
    @Bind(R.id.tv_shoujianaddr)
    TextView tvShoujianaddr;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.lin_showJg)
    LinearLayout linShowJg;
    @Bind(R.id.view_lin2)
    View view_lin2;
    @Bind(R.id.lin_show2)
    LinearLayout lin_show2;
    @Bind(R.id.tv_juli)
    TextView tvJuli;
    @Bind(R.id.iv_jian)
    ImageView ivJian;
    @Bind(R.id.iv_showzl)
    TextView ivShowzl;
    @Bind(R.id.iv_jia)
    ImageView ivJia;
    @Bind(R.id.tv_showmo)
    TextView tvShowmo;
    @Bind(R.id.tv_yuyue_time)
    TextView tvYuyueTime;
    @Bind(R.id.view_height)
    View viewHeight;
    @Bind(R.id.tv_showyuyue)
    TextView tvShowyuyue;
    @Bind(R.id.lin_youhui)
    LinearLayout lin_youhui;
    @Bind(R.id.tv_dihuijiage)
    TextView tv_dihuijiage;
    GeocodeSearch mSearch = null;
    private AMap aMap;
    private String indexAddre;// 当前 定位 地址
    private String city;
    private PeopleBean peopleBean_dw;//当前 定位 对象
    private PeopleBean peopleBean_sj; //收件人地址 对象
    private PeopleBean peopleBean_jj; // 取件人 地址 对象
    private boolean isFast = true;// 是否 第一次 定位 加载,为了 防止  第一次 定位显示数据和 图标 点位 地址不一致
    private int weight = 1;// 重量  默认一公斤

    private long loadTime;


    private String taketime = "0";

    private String price = "";
    private String night = "";
    private String points = "0";
    private float percent = 0;

    private RedbagBean redbagBean;

    private PoiSearch poiSearch;

    private boolean isSet = false;// 是否计算了 优惠

    Handler handler = new Handler() {

        //两秒之后重新获取距离
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            L.e("重新获取价格");
            setDistance();
        }
    };
    private int currentPage = 1; //搜索第几页
    private boolean isFirest = true;
    private boolean isSetUser = true;
    //定位相关
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        map = (MapView) view.findViewById(R.id.mapView);
        map.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = map.getMap();
        } else {
            if (map.getParent() != null) {
                ((ViewGroup) map.getParent()).removeView(view);
            }
        }

        initMapw();
        setOnClickListener();

//        int aa = 0/0;
        view.findViewById(R.id.tv_dingwei_iv).setOnClickListener(this);
        return view;
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {

        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();

//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
//                .fromResource(R.mipmap.ic_launcher));// 设置小蓝点的图标

        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);

        aMap.setLocationSource(this);// 设置定位资源。如果不设置此定位资源则定位按钮不可点击。并且实现activate激活定位,停止定位的回调方法
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

    }

    private void setOnClickListener() {

        linShow.setOnClickListener(this);
        tvQujianaddr.setOnClickListener(this);
        tvShoujianaddr.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        ivJian.setOnClickListener(this);
        ivJia.setOnClickListener(this);
        tvShowmo.setOnClickListener(this);
        tvYuyueTime.setOnClickListener(this);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    // 初始化 地图
    private void initMapw() {

        if (aMap == null) {
            aMap = map.getMap();
        }

        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setRotateGesturesEnabled(false);


        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式，参见类AMap。

//        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

//        MyLocationStyle myLocationStyle = new MyLocationStyle();
////        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
////                .fromResource(R.mipmap.ic_launcher));// 设置小蓝点的图标
//        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
//        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
//        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
//        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
//        aMap.setMyLocationStyle(myLocationStyle);
//        aMap.setLocationSource(this);// 设置定位监听
//        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//

        // 初始化搜索模块，注册事件监听
        mSearch = new GeocodeSearch(getActivity());
        mSearch.setOnGeocodeSearchListener(this);

        //地图加载完毕
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                Log.d("--------------------", "地图加载完成");
                tvShowMapIV.setVisibility(View.VISIBLE);

                mlocationClient = new AMapLocationClient(getActivity());
                mLocationOption = new AMapLocationClientOption();
                //设置定位监听
                mlocationClient.setLocationListener(MainFragment.this);
                //设置为高精度定位模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                mLocationOption.setNeedAddress(true);
                //设置是否只定位一次,默认为false
                mLocationOption.setOnceLocation(false);
                //设置是否强制刷新WIFI，默认为强制刷新
                mLocationOption.setWifiActiveScan(true);
                //设置是否允许模拟位置,默认为false，不允许模拟位置
                mLocationOption.setMockEnable(false);
                //设置定位参数
                mlocationClient.setLocationOption(mLocationOption);
                // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
                // 在定位结束后，在合适的生命周期调用onDestroy()方法
                // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除

                mlocationClient.startLocation();

/*
                //开启定位
                new LocationUtil(getActivity()).setLocationUtilLinstenner(new LocationUtilLinstenner() {
                    @Override
                    public void onLocationChanged(final AMapLocation location) {

                        MainFragment.this.location = location;
                        L.d("定位完成");
                        // 构造定位数据

                        // final float f = baiduMap.getMaxZoomLevel();//19.0 最小比例尺
                        final float f = 17;                         //18 0422修改

                        L.e("======" + location.toString());

                        indexAddre = location.getAoiName() + location.getPoiName();

                        setShowAddr(indexAddre);

                        L.e("indexAddre = " + indexAddre);

                        city = location.getCityCode();

                        peopleBean_dw = new PeopleBean();
                        peopleBean_dw.setAdd(indexAddre);
                        peopleBean_dw.setLat(location.getLatitude() + "");
                        peopleBean_dw.setLng(location.getLongitude() + "");


                        setSendUser(location.getLatitude() + "", location.getLongitude() + "");

                        peopleBean_jj = peopleBean_dw;// 默认 寄件 地址 为 定位 数据
                        LatLng la = new LatLng(location.getLatitude(), location.getLongitude());

                        L.e("latitude = " + la.latitude);
                        L.e("longitude = " + la.longitude);


                        CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngZoom(la, f);
                        if (aMap != null)
                            aMap.animateCamera(mCameraUpdate);

                        new LocationUtil(getActivity()).stop();


//                        PoiSearch.Query query = new PoiSearch.Query("", "地名地址信息", location.getCity());
//
//                        query.setPageSize(10);// 设置每页最多返回多少条poiitem
//                        query.setPageNum(currentPage);//设置查询页码
//
//                        PoiSearch mPoiSearch = new PoiSearch(getActivity(), query);//初始化poiSearch对象
//
//                        mPoiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(location.getLatitude(),
//                                location.getLongitude()), 3000));//设置周边搜索的中心点以及区域
//
//                        mPoiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
//
//                            @Override
//                            public void onPoiSearched(PoiResult poiResult, int errorCode) {
//                                List<PeopleBean> peopleBeanList = new ArrayList<>();
//
//                                L.e("errorCode = " + errorCode);
//
//                                if (errorCode == 1000) {
//
//                                    List<PoiItem> list = poiResult.getPois();
//                                    L.e("PoiItem size = " + list.size());
//
//                                    for (PoiItem pi : list) {
//
//                                        PeopleBean peopleBean = new PeopleBean();
//                                        peopleBean.setAdd(pi.getCityName() + "(" + pi.getDirection() + ")");
//                                        peopleBean.setLat(pi.getLatLonPoint().getLatitude() + "");
//                                        peopleBean.setLng(pi.getLatLonPoint().getLongitude() + "");
//                                        peopleBeanList.add(peopleBean);
//
//                                        L.e(pi.toString());
//                                    }
//
//                                    indexAddre = peopleBeanList.get(0).getAdd();
//                                    setShowAddr(indexAddre);
//
//
//                                    L.e("indexAddre = " + indexAddre);
//
//                                    city = list.get(0).getCityName();
//
//                                    peopleBean_dw = new PeopleBean();
//                                    peopleBean_dw.setAdd(indexAddre);
//                                    peopleBean_dw.setLat(location.getLatitude() + "");
//                                    peopleBean_dw.setLng(location.getLongitude() + "");
//
//                                    setSendUser(location.getLatitude() + "", location.getLongitude() + "");
//
//                                    peopleBean_jj = peopleBean_dw;// 默认 寄件 地址 为 定位 数据
//
//                                    LatLng la = new LatLng(location.getLatitude(), location.getLongitude());
//
////                                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(la, f);//设置缩放比例
////                                    if (baiduMap != null)
////                                        baiduMap.animateMapStatus(u);
//
//                                    L.e("latitude = " + la.latitude);
//                                    L.e("longitude = " + la.longitude);
//
//                                    CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngZoom(la, f);
//                                    //   if (aMap != null)
//                                    aMap.animateCamera(mCameraUpdate);
//
//                                    new LocationUtil(getActivity()).stop();
//                                }
//                            }
//
//                            @Override
//                            public void onPoiItemSearched(PoiItem poiItem, int errorCode) {
//
//                                L.e("onPoiItemSearched", poiItem.getAdCode());
//
//                            }
//                        });//设置回调数据的监听器
//
//                        mPoiSearch.searchPOIAsyn();//开始搜索

                    }
                }).start();
*/
            }
        });

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                if (!isFast) {
                    tvQujianaddr.setText("正在获寄件地址...");
                }
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {

                //L.e("cameraPosition = " + cameraPosition.toString());

                if (!isFast) {
                    updateMapState(cameraPosition);
                } else {
                    isFast = false;
                }
            }
        });
    }

    // 拖动 完毕
    private void updateMapState(CameraPosition status) {
        LatLng mCenterLatLng = status.target;
        startShowAddr(mCenterLatLng);
    }

    private void startShowAddr(LatLng latLng) {

        tvQujianaddr.setText("正在获寄件地址...");
        LatLonPoint mLatLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        mSearch.getFromLocationAsyn(new RegeocodeQuery(mLatLonPoint, 1000, GeocodeSearch.AMAP));

    }

    // 显示 中心点地址
    private void setShowAddr(String showStr) {
        if (!TextUtils.isEmpty(showStr)) {
            tvQujianaddr.setText(showStr);
        }
        setDistance();
    }

    // 显示 中心点地址  失败
    private void setShowAddrError() {
        tvQujianaddr.setText("地址获取失败,请检查网络");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (map != null) {
            map.onDestroy();
        }
        BaseApplication.isZFsucceed = false;
        BaseApplication.getInstance().setIsWxPay(false);
        BaseApplication.getInstance().setIsZFBPay(false);
        BaseApplication.getInstance().setIsCreatOrder(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (map != null) {
            map.onPause();
        }
    }


    @Override
    public void onResume() {

        Log.e(TAG, "onResume() called with: " + "");

        super.onResume();
        if (map != null) {
            map.onResume();
            map.setFocusable(true);
        }

        if (BaseApplication.getInstance().isWxPay() || BaseApplication.getInstance().isZFBPay()) {
            lin_show2.setVisibility(View.GONE);
            linShowJg.setVisibility(View.GONE);
            tvShoujianaddr.setText("");
            tvShoujianaddr.setHint("请输入取件地址");

            BaseApplication.isZFsucceed = false;
            BaseApplication.getInstance().setIsWxPay(false);
            BaseApplication.getInstance().setIsZFBPay(false);

            if (aMap != null) {
                aMap.clear();
            }
            peopleBean_sj = null;

            if (location != null) {
                setSendUser(location.getLatitude() + "", location.getLongitude() + "");
            }
        }

        //重新定位一次 20160524 改需求
        if ((mlocationClient != null) && (BaseApplication.getInstance().getIsBack())) {
            isFirest = true;
            isSetUser = true;
            L.e("后台返回定位");
            BaseApplication.getInstance().setIsBack(false);
            mlocationClient.startLocation();
        }

        redbagBean = null;
        lin_youhui.setVisibility(View.GONE);
        getPrice();
        ApiHttpClient.postNotShow(getActivity(), UrlUtil.getred, new RequestParams(), new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

                if (!TextUtils.isEmpty(responseStr)) {
                    List<RedbagBean> list = JSonUtils.toList(RedbagBean.class, responseStr);
                    if (list.size() > 0) {
                        redbagBean = list.get(0);
                        lin_youhui.setVisibility(View.VISIBLE);
                        tv_dihuijiage.setText("-" + redbagBean.getMoney() + "元");

                        if (isSet == false) {
                            double dou1 = Double.parseDouble(redbagBean.getMoney());
                            double dou2 = Double.parseDouble(price);
                            double dou3 = dou2 - dou1;
                            price = dou3 + "";
                        }
                        tvShowmo.setText(price + "元");
                    }
                }
            }

            @Override
            public void onFailure(int statusCode) {

            }

            @Override
            public void onSendError(int statusCode, String message) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_qujianaddr:// 取件 地址 点击

                UIHelper.openAddressHistory(this, peopleBean_dw, city, 0);
                break;
            case R.id.tv_shoujianaddr:// 寄件人 地址

                UIHelper.openAddressHistory(this, peopleBean_dw, city, 1);
                break;
            case R.id.tv_dingwei_iv://回到 定位 远点

//                try {

                isFirest = true;
                mlocationClient.startLocation();


//                    new LocationUtil(getActivity()).stop();
//                    //定位之前先取消定位
//
//                    new LocationUtil(getActivity()).setLocationUtilLinstenner(new LocationUtilLinstenner() {
//                        @Override
//                        public void onLocationChanged(AMapLocation location) {
//
////                            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(
////                                    new LatLng(location.getLatitude(), location.getLongitude()), f);//设置缩放比例
//
//
//                            ////
//
////                            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
////                            baiduMap.animateMapStatus(u);
////                            MainFragment.this.location = location;
//
//                            setDistance();
//
//                            if (peopleBean_dw == null)
//                                peopleBean_dw = new PeopleBean();
//
//                            peopleBean_dw.setAdd(location.getAddress());
//                            peopleBean_dw.setLat(location.getLatitude() + "");
//                            peopleBean_dw.setLng(location.getLongitude() + "");
//
//                            BaseApplication.set("Latitude", peopleBean_dw.getLat());
//                            BaseApplication.set("Longitude", peopleBean_dw.getLng());
//                            BaseApplication.set("indexAddre", peopleBean_dw.getAdd());
//
//                            new LocationUtil(getActivity()).stop();
//                        }
//                    }).start();
//
//                    //     float f = baiduMap.getMaxZoomLevel();//19.0 最小比例尺
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                break;
            case R.id.btn_next:// 下一步

                final OrderSubmitBean osb = new OrderSubmitBean();

                //L.e("点击了按钮");

                osb.setPeopleBean_jj(peopleBean_jj);
                osb.setPeopleBean_sj(peopleBean_sj);
                osb.setDistance((int) distance + "");
                osb.setWeight(weight + "");
                osb.setTaketime(taketime);
                osb.setAddprice(price);
                osb.setNightnight(night);
                String taketype = "1";
                if (!taketime.equals("0")) {// 有时间  就是 预约单
                    taketype = "2";
                }
                osb.setTaketype(taketype);

                if (redbagBean != null) {
                    osb.setRid(Integer.parseInt(redbagBean.getRid()));
                }

                osb.setPoints(points);
                osb.setPercent(percent);

                //判断 是否 登录
                boolean isLogin = new UserModel(getActivity()).isAutoLogin();

                if (!isLogin) {
                    UIHelper.openKhLoginAvtivity(getActivity(), new OnLoginBackLinstener() {
                        @Override
                        public void onBack() {
                            Intent intent2 = new Intent(getActivity(), SubmitOrderActivity.class);
                            intent2.putExtra("orderSubmitBean", osb);
                            startActivityForResult(intent2, 200);
                        }
                    });
                } else {
                    Intent intent2 = new Intent(getActivity(), SubmitOrderActivity.class);
                    intent2.putExtra("orderSubmitBean", osb);
                    startActivityForResult(intent2, 200);
                }
                break;
            case R.id.iv_jian://减

                if (weight == 2) {
                    ivJian.setBackgroundResource(R.mipmap.icon_jian);
                }

                if (weight > 1) {
                    weight--;
                    ivShowzl.setText(weight + "公斤");
                }

                getPrice();
                break;
            case R.id.iv_jia://加

                weight++;
                if (weight > 1) {
                    ivJian.setBackgroundResource(R.drawable.jianhao_btn_bg);
                }
                ivShowzl.setText(weight + "公斤");

                getPrice();
                break;
            case R.id.tv_showmo:

                getPrice();
                break;
            case R.id.tv_yuyue_time:// 立即 取件

                TimeDialogActivity.create(getActivity()).setListener(new TimeDialogActivity.BaseDialogListener() {
                    @Override
                    public void onClickBack(TimeDialogActivity actionSheet, long time) {

                        if (time == 0) {// 立即 取件
                            tvShowyuyue.setVisibility(View.GONE);
                            viewHeight.setVisibility(View.GONE);
                            taketime = "0";
                            tvYuyueTime.setText("立即取件");
                            return;
                        }
                        viewHeight.setVisibility(View.VISIBLE);
                        tvShowyuyue.setVisibility(View.VISIBLE);
                        taketime = time + "";
                        tvYuyueTime.setText(DateUtils.timestampToDate(time + ""));

                    }
                }).show();

                break;
        }
    }

    //计算 距离

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {

            PeopleBean peopleBean = (PeopleBean) data.getSerializableExtra("peopleBean");

            if (peopleBean != null) {
                int type = data.getIntExtra("type", 0);

                String add = peopleBean.getAdd();

                if (!TextUtils.isEmpty(add)) {// 确定的 地址

                    BaseApplication.getInstance().setIsWxPay(false);
                    BaseApplication.getInstance().setIsZFBPay(false);


                    if (type == 0) {
                        tvQujianaddr.setText(add);
                        peopleBean_jj = peopleBean;

                        setSendUser(peopleBean_jj.getLat() + "", peopleBean_jj.getLng() + "");

//                       peopleBean_jj = peopleBean_dw;// 默认 收件 地址 为 定位 数据
                        //20160418 触发了百度地图的改变 就会影响地图的搜索
//                        LatLng la = new LatLng(Double.parseDouble(peopleBean_sj.getLat()), Double.parseDouble(peopleBean_sj.getLng()));
//                        float f = baiduMap.getMaxZoomLevel();//19.0 最小比例尺
//                        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(la, f);//设置缩放比例
//                        baiduMap.animateMapStatus(u);

                    } else {

                        BaseApplication.getInstance().setIsCreatOrder(false);
                        tvShoujianaddr.setText(add);
                        peopleBean_sj = peopleBean;
                        linShowJg.setVisibility(View.VISIBLE);
                        lin_show2.setVisibility(View.VISIBLE);
                        view_lin2.setVisibility(View.VISIBLE);
                    }

                    setDistance();
                }
            }
            String back = data.getStringExtra("data");
            if (!TextUtils.isEmpty(back)) {// 支付 返回

                L.d("你好 支付返回了");
            }
        }
    }

    private void setDistance() {
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

        aMap.clear();

//        BitmapDescriptor bitmap1 = BitmapDescriptorFactory
//                .fromResource(R.mipmap.qu);
//
//        OverlayOptions option1 = new MarkerOptions()
//                .position(new LatLng(Double.parseDouble(peopleBean_jj.getLat()), Double.parseDouble(peopleBean_jj.getLng())))
//                .icon(bitmap1);
//        bmapView.getMap().addOverlay(option1);
//
//        BitmapDescriptor bitmap3 = BitmapDescriptorFactory
//                .fromResource(R.mipmap.iconfont_dingwei);
//
////        BaseApplication.set("Latitude", peopleBean_dw.getLat());
////        BaseApplication.set("Longitude", peopleBean_dw.getLng());
////        BaseApplication.set("indexAddre", peopleBean_dw.getAdd());
//
//        OverlayOptions option3 = new MarkerOptions()
//                .position(new LatLng(Double.parseDouble(BaseApplication.get("Latitude", "0")),
//                        Double.parseDouble(BaseApplication.get("Longitude", "0"))))
//                .icon(bitmap3);
//        bmapView.getMap().addOverlay(option3);


        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_shou);

        LatLng point = new LatLng(Double.parseDouble(peopleBean_sj.getLat()), Double.parseDouble(peopleBean_sj.getLng()));

        MarkerOptions markerOption = new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .position(point)
                .icon(bitmap)
                .draggable(true).period(50);

        aMap.addMarker(markerOption);

        // distance = DistanceComputeUtil.getDistance(douLasj, douLnsj, douLajj, douLnjj);

        RouteSearch routeSearch = new RouteSearch(getActivity());
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(douLajj, douLnjj), new LatLonPoint(douLasj, douLnsj));

        // fromAndTo包含路径规划的起点和终点，drivingMode表示驾车模式
        // 第三个参数表示途经点（最多支持16个），第四个参数表示避让区域（最多支持32个），第五个参数表示避让道路
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingShortDistance, null, null, "");
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

                L.e("距离" + distance + "km");

                tvJuli.setText((int) distance + "km");
                getPrice();
            }


            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }
        });

        routeSearch.calculateDriveRouteAsyn(query);
    }

    // 计算价格
    private void getPrice() {

        //判断 是否 开始计算价格
        ApiHttpClient.cancelRequests(getActivity());

        BigDecimal big = new BigDecimal(distance);

        double f1 = big.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();

        L.d("距离---》" + f1 + "----重量" + weight + "----预约时间" + taketime);

        //距离
        OrderApi.sendordergetprice(getContext(), weight + "", f1 + "", taketime + "", new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

                try {
                    JSONObject jsonObject = new JSONObject(responseStr);

                    //       L.e(responseStr);

                    price = jsonObject.getString("price");
                    night = jsonObject.getString("night");
                    if (jsonObject.has("points"))
                        points = jsonObject.getString("points");
                    else {
                        points = "0";
                    }
                    if (jsonObject.has("percent")) {
                        percent = Float.valueOf(jsonObject.getString("percent"));
                    } else {
                        percent = 0;
                    }
                    if (redbagBean != null) {

                        isSet = true;
                        double dou0 = Double.parseDouble(night);
                        double dou1 = Double.parseDouble(redbagBean.getMoney());
                        double dou2 = Double.parseDouble(price);
                        double dou3 = dou2 - dou1 + dou0;
                        price = dou3 + "";
                    }
                    tvShowmo.setText(price + "元");
                    btnNext.setEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode) {
                tvShowmo.setText("网络错误");
                btnNext.setEnabled(false);
                Log.d(TAG, "onFailure() called with: " + "statusCode = [" + statusCode + "]");
                handler.obtainMessage(0).sendToTarget();
            }

            @Override
            public void onSendError(int statusCode, String message) {
                Log.d(TAG, "onSendError() called with: " + "statusCode = [" + statusCode + "], message = [" + message + "]");
                tvShowmo.setText("网络错误");
                btnNext.setEnabled(false);
                handler.obtainMessage(0).sendToTarget();
            }
        });
    }

    // 设置 地图 周围 快递员
    private void setSendUser(String lat, String lng) {

        OrderApi.getsenduser(getContext(), lat, lng, new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

                L.e(responseStr);

                if (TextUtils.isEmpty(responseStr)) {
                    return;
                }

                ArrayList<LocationBean> arrayList = (ArrayList<LocationBean>) JSonUtils.toList
                        (LocationBean.class, responseStr);

                for (LocationBean lo : arrayList) {

                    LatLng point = new LatLng(lo.getCplat(), lo.getCplng());
//                    pts.add(point);
                    MarkerOptions markerOption = new MarkerOptions()
                            .anchor(1f, 1f)
                            .position(point)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.motuo))
                            .draggable(true).period(50);
                    aMap.addMarker(markerOption);
                }
//                OverlayOptions option = new MarkerOptions()
//                        .p
//                        .icon(bitmap);

//                OverlayOptions polygonOption = new PolygonOptions()
//                       .points(pts)
//                        .i

            }

            @Override
            public void onFailure(int statusCode) {
            }

            @Override
            public void onSendError(int statusCode, String message) {

            }
        });
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

        if (regeocodeResult == null || i != 1000) {
            setShowAddrError();
            return;
        }

        RegeocodeAddress mRegeocodeAddress = regeocodeResult.getRegeocodeAddress();

//        L.e("1" + mRegeocodeAddress.getFormatAddress());
//        L.e("2" + mRegeocodeAddress.getAdCode());
//        L.e("3" + mRegeocodeAddress.getBuilding());
//        L.e("4" + mRegeocodeAddress.getCity());
//        L.e("5" + mRegeocodeAddress.getDistrict());
//        L.e("6" + mRegeocodeAddress.getFormatAddress());
//        L.e("7" + mRegeocodeAddress.getNeighborhood());
//        L.e("8" + mRegeocodeAddress.getProvince());
//        L.e("9" + mRegeocodeAddress.getTownship());
//
//        L.e(mRegeocodeAddress.getRoads().toString());
//        L.e(mRegeocodeAddress.getBusinessAreas().toString());
//        L.e(mRegeocodeAddress.getCrossroads().toString());

        // if (mRegeocodeAddress.getAois().size() > 0)
        {

            indexAddre = mRegeocodeAddress.getFormatAddress();

//            indexAddre = mRegeocodeAddress.getAois().get(0).getAoiName().
//                    replace("(", "").replace(")", "") + "(" + mRegeocodeAddress.getDistrict()
//                    + mRegeocodeAddress.getTownship() + ")";
            setShowAddr(TextUtils.isEmpty(indexAddre) ? "" : indexAddre);
        }

        //拖动回来完毕
        peopleBean_jj.setAdd(TextUtils.isEmpty(indexAddre) ? "" : indexAddre);
        peopleBean_jj.setLat(mRegeocodeAddress.getPois().get(0).getLatLonPoint().getLatitude() + "");
        peopleBean_jj.setLng(mRegeocodeAddress.getPois().get(0).getLatLonPoint().getLongitude() + "");

        setDistance();
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        if (geocodeResult == null || i != 1000) {
            setShowAddrError();
            return;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        L.d("定位完成");
        // 构造定位数据

        //L.e("======" + aMapLocation.toString());


        if (isFirest) {

            if (aMapLocation.getLatitude() <= 0 || aMapLocation.getLongitude() <= 0) {
                T.s("定位失败!请检查网络");
                return;
            }

            MainFragment.location = aMapLocation;

            indexAddre = aMapLocation.getAddress();

            setShowAddr(indexAddre);
            //L.e("indexAddre = " + indexAddre);
            city = aMapLocation.getCityCode();
            peopleBean_dw = new PeopleBean();
            peopleBean_dw.setAdd(indexAddre);
            peopleBean_dw.setLat(aMapLocation.getLatitude() + "");
            peopleBean_dw.setLng(aMapLocation.getLongitude() + "");

            if (isSetUser) {
                setSendUser(aMapLocation.getLatitude() + "", aMapLocation.getLongitude() + "");
                isSetUser = false;
            }

            peopleBean_jj = peopleBean_dw;// 默认 寄件 地址 为 定位 数据
            LatLng la = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());

            //L.e("latitude = " + la.latitude);
            //L.e("longitude = " + la.longitude);

            CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngZoom(la, 16);
            if (aMap != null)
                aMap.animateCamera(mCameraUpdate);

            mlocationClient.stopLocation();
            isFirest = false;
            setDistance();
        }

        if (aMapLocation.getLatitude() <= 0 || aMapLocation.getLongitude() <= 0) {
            T.s("定位失败!请检查网络");
            isFirest = true;
            return;
        }

        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            } else {
                T.s("定位失败!请检查网络");
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }


    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        mlocationClient.startLocation();
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

}
