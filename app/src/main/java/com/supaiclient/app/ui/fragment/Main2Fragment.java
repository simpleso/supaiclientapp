package com.supaiclient.app.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import com.supaiclient.app.api.OrderApi;
import com.supaiclient.app.bean.FindspmanBean;
import com.supaiclient.app.ui.activity.home.MainActivity;
import com.supaiclient.app.ui.adapter.Main2Adapter;
import com.supaiclient.app.ui.adapter.base.ListBaseAdapter;
import com.supaiclient.app.ui.base.BaseListFragment;
import com.supaiclient.app.ui.fragment.goods.WuJianActivity;
import com.supaiclient.app.util.JSonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/15.
 * 物件 追踪
 */
public class Main2Fragment extends BaseListFragment<FindspmanBean> implements BaseListFragment.OnLodingFinsh {


    protected ArrayList<FindspmanBean> arrayList;
    Handler myHandler = new Handler() {

        public void handleMessage(Message msg) {

            executeOnLoadDataSuccess(arrayList);
            executeOnLoadFinish();
        }
    };

    private String url = "http://api.map.baidu.com/geocoder/v2/&output=json";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        setOnLodingFinsh(this);
    }

    @Override
    protected ListBaseAdapter<FindspmanBean> getListAdapter() {
        return new Main2Adapter();
    }

    @Override
    protected void sendRequestData() {

        OrderApi.orderfindspman(getActivity(), mCurrentPage * getPageSize(), getPageSize(), requestBasetListener);

          /*  arrayList = (ArrayList<FindspmanBean>) JSonUtils.toList(FindspmanBean.class, responseStrnseStr);

            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.motuo);

            MapView mapView = new MapView(getActivity(), new BaiduMapOptions());
            BaiduMap baiduMap = mapView.getMap();
            float f = baiduMap.getMaxZoomLevel();//19.0 最小比例尺

            for (final FindspmanBean list : arrayList) {

                LatLng point = new LatLng(Double.parseDouble(list.getCplat()), Double.parseDouble(list.getCplng()));
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .icon(bitmap);
                baiduMap.addOverlay(option);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(point, f);//设置缩放比例
                baiduMap.animateMapStatus(u);
                mapView.onResume();
                baiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {

                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {

                       list.setBitmap(bitmap);
                        myHandler.sendEmptyMessage(1);
                   }
                });
                return;
            }*/
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            requestData(true);
        }
    }

    @Override
    protected ArrayList<FindspmanBean> parseList(String json) throws Exception {


        arrayList = (ArrayList<FindspmanBean>) JSonUtils.toList(FindspmanBean.class, json);

//        for (int i = 0;i<arrayList.size();i++) {
//
//            ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
//            reverseGeoCodeOption.location(new LatLng(Double.valueOf(arrayList.get(i).getCplat()),
//                    Double.valueOf(arrayList.get(i).getCplng())));
//
//            GeoCoder.newInstance().setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
//                @Override
//                public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
//
//                }
//
//                @Override
//                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
//
//                    L.e(reverseGeoCodeResult.getAddress());
//
//                    arrayList.get(i).setAddr(reverseGeoCodeResult.getAddress());
//                }
//            });
//
//            GeoCoder.newInstance().reverseGeoCode(reverseGeoCodeOption);

//        }
        return arrayList;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        FindspmanBean findspmanBean = (FindspmanBean) adapterView.getAdapter().getItem(i);
        if (findspmanBean != null) {
            Intent intent = new Intent(getActivity(), WuJianActivity.class);
            intent.putExtra("findspmanBean", findspmanBean);
            startActivity(intent);
        }
    }

    @Override
    public void finish(List data) {

        if (!data.isEmpty()) {
            getActivity().sendBroadcast(new Intent(MainActivity.ACTION_CG).putExtra("STA", true).putExtra("NUM", data.size()));
        } else {
            getActivity().sendBroadcast(new Intent(MainActivity.ACTION_CG).putExtra("STA", false).putExtra("NUM", 0));
        }

    }
}
