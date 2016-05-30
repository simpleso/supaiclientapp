package com.supaiclient.app.ui.fragment.address;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.supaiclient.app.bean.PeopleBean;
import com.supaiclient.app.ui.adapter.NearbyAddressListAdapter;
import com.supaiclient.app.ui.adapter.base.ListBaseAdapter;
import com.supaiclient.app.ui.base.BaseListFragment;
import com.supaiclient.app.ui.fragment.MainFragment;
import com.supaiclient.app.util.L;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2015/12/29.
 * 附近地址
 */

public class NearbyAddressFragment extends BaseListFragment<PeopleBean> implements PoiSearch.OnPoiSearchListener {

    private int type;
    private String city;

    @Override
    protected ListBaseAdapter<PeopleBean> getListAdapter() {
        return new NearbyAddressListAdapter();
    }

    @Override
    protected void sendRequestData() {

        Bundle bundle = getBundle();
        if (bundle != null) {

            PeopleBean peopleBean = (PeopleBean) bundle.getSerializable("peopleBean_dw");
            city = bundle.getString("city");

            if (peopleBean == null) {

                //楼盘|商务住宅|公司企业
                PoiSearch.Query query = new PoiSearch.Query("", "", city != null ? city : "023");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
                query.setPageSize(20);// 设置每页最多返回多少条poiitem
                query.setPageNum(mCurrentPage);// 设置查第一页
                PoiSearch poiSearch = new PoiSearch(getActivity(), query);
                poiSearch.setOnPoiSearchListener(this);
                poiSearch.searchPOIAsyn();
            } else {

                type = bundle.getInt("type");
                String lat = peopleBean.getLat();
                String lag = peopleBean.getLng();

                // keyWord表示搜索字符串，
                //第二个参数表示POI搜索类型，二者选填其一，
                //POI搜索类型共分为以下20种：汽车服务|汽车销售|
                //汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
                //住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
                //金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
                //cityCode表示POI搜索区域的编码，是必须设置参数

                //楼盘|商务住宅|公司企业

                if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lag)) {
                    PoiSearch.Query query = new PoiSearch.Query("", "", city != null ? city : "023");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
                    query.setPageSize(20);// 设置每页最多返回多少条poiitem
                    query.setPageNum(mCurrentPage);// 设置查第一页
                    PoiSearch poiSearch = new PoiSearch(getActivity(), query);
                    poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(Double.valueOf(lat),
                            Double.valueOf(lag)), 2000));//设置周边搜索的中心点以及区域
                    poiSearch.setOnPoiSearchListener(this);
                    poiSearch.searchPOIAsyn();
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        PeopleBean peopleBean = (PeopleBean) adapterView.getAdapter().getItem(i);
        if (peopleBean != null) {

            Intent intent = new Intent(getActivity(), MainFragment.class);
            intent.putExtra("peopleBean", peopleBean);
            intent.putExtra("type", type);

            //L.e(type +":" +peopleBean.toString());

            getActivity().setResult(200, intent);
            this.getActivity().finish();
        }
    }

    @Override
    protected int getPageSize() {
        return 20;//禁止 分页
    }


    /**
     * 通过经纬度获取地址
     *
     * @param point
     * @return
     */
    private String getLocationAddress(LatLng point) {
        String add = "";
        Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(
                    point.latitude, point.longitude,
                    1);
            Address address = addresses.get(0);
            int maxLine = address.getMaxAddressLineIndex();
            if (maxLine >= 2) {
                add = address.getAddressLine(1) + address.getAddressLine(2);
            } else {
                add = address.getAddressLine(1);
            }
        } catch (IOException e) {
            add = "";
            e.printStackTrace();
        }
        return add;
    }


    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {

        List<PeopleBean> peopleBeanList = new ArrayList<>();

        if (poiResult == null || i != 1000) {// 没有找到检索结果

            L.i("没有找到检索结果");

        } else {

            List<PoiItem> poiInfoList = poiResult.getPois();
            for (PoiItem pi : poiInfoList) {

//                L.e("--01--" + pi.getCityName());
//                L.e("--02--" + pi.getDirection());
//                L.e("--03--" + pi.getAdCode());
//                L.e("--04--" + pi.getAdName());
//                L.e("--05--" + pi.getBusinessArea());
//                L.e("--06--" + pi.getCityCode());
//                L.e("--07--" + pi.getEmail());
//                L.e("--08--" + pi.getParkingType());
//                L.e("--09--" + pi.getPoiId());
//                L.e("--10--" + pi.getProvinceCode());
//                L.e("--11--" + pi.getProvinceName());
//                L.e("--12--" + pi.getSnippet());
//                L.e("--13--" + pi.getTitle());
//                L.e("--14--" + pi.getTypeDes());
//                L.e("--15--" + pi.getDistance());

                PeopleBean peopleBean = new PeopleBean();
//                peopleBean.setAdd(pi.getTitle().replace("(", "").replace(")", "") + "(" + pi.getSnippet() + ")");
                peopleBean.setAdd(pi.getSnippet());
                peopleBean.setLat(pi.getLatLonPoint().getLatitude() + "");
                peopleBean.setLng(pi.getLatLonPoint().getLongitude() + "");

                peopleBeanList.add(peopleBean);
            }

            executeOnLoadDataSuccess(peopleBeanList);
            executeOnLoadFinish();
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
