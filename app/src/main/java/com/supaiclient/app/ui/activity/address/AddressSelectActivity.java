package com.supaiclient.app.ui.activity.address;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.supaiclient.app.R;
import com.supaiclient.app.bean.PeopleBean;
import com.supaiclient.app.ui.adapter.base.BaseAdapterHelper;
import com.supaiclient.app.ui.adapter.base.QuickAdapter;
import com.supaiclient.app.ui.base.BaseActivity;
import com.supaiclient.app.ui.fragment.address.AddressHistoryFragment;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/12/25.
 * 地址 输入 周边城市选择
 */
public class AddressSelectActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener {

    @Bind(R.id.et_inputAddre)
    EditText etInputAddre;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.iv)
    ImageView iv;
    private QuickAdapter mAdapter;

    private int type;
    private String city;
    private int currentPage = 0;

    TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            String content = etInputAddre.getText().toString();
            initSearch(content);
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_addreselect;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        type = getIntent().getIntExtra("type", 0);
        city = getIntent().getStringExtra("city");

        if (type == 1) {
            setActionBarTitle("输入收件地址");
            iv.setImageResource(R.mipmap.icon_shou);
            etInputAddre.setHint("请输入收件人地址");
        } else {
            iv.setImageResource(R.mipmap.qu);
            etInputAddre.setHint("请输入寄件人地址");
            setActionBarTitle("输入寄件地址");
        }

        etInputAddre.addTextChangedListener(mTextWatcher);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PoiItem poiInfo = (PoiItem) parent.getAdapter().getItem(position);
                Intent intent = new Intent(AddressSelectActivity.this, AddressHistoryFragment.class);
                PeopleBean peopleBean = new PeopleBean();

                peopleBean.setAdd(poiInfo.getCityName() + poiInfo.getAdName() + "(" + poiInfo.getSnippet() + ")");
                peopleBean.setLat(poiInfo.getLatLonPoint().getLatitude() + "");
                peopleBean.setLng(poiInfo.getLatLonPoint().getLongitude() + "");

                intent.putExtra("peopleBean", peopleBean);
                intent.putExtra("type", type);
                setResult(200, intent);
                AddressSelectActivity.this.finish();
            }
        });
    }

    private void initSearch(String keyword) {

        if (TextUtils.isEmpty(keyword)) {

            if (mAdapter != null) {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
            }
            return;
        }

        PoiSearch.Query query = new PoiSearch.Query(keyword, "", city != null ? city : "023");
        // keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，
        //POI搜索类型共分为以下20种：汽车服务|汽车销售|
        //汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
        //住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
        //金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
        //cityCode表示POI搜索区域的编码，是必须设置参数
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);//设置查询页码
        PoiSearch poiSearch = new PoiSearch(this, query);//初始化poiSearch对象
        poiSearch.setOnPoiSearchListener(this);//设置回调数据的监听器
        poiSearch.searchPOIAsyn();//开始搜索
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {

        if (poiResult == null || i != 1000) {// 没有找到检索结果

            if (mAdapter != null) {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
            }
            return;
        }

        // 检索结果正常返回
        List<PoiItem> poiInfoList = poiResult.getPois();
        listview.setAdapter(mAdapter = new QuickAdapter<PoiItem>(
                this, R.layout.listview_addressselect, poiInfoList) {

            @Override
            protected void convert(BaseAdapterHelper helper, PoiItem item) {

//                L.e("convert = " + item.toString());
//
//                L.e("--01--" + item.getCityName());
//                L.e("--02--" + item.getDirection());
//                L.e("--03--" + item.getAdCode());
//                L.e("--04--" + item.getAdName());
//                L.e("--05--" + item.getBusinessArea());
//                L.e("--06--" + item.getCityCode());
//                L.e("--07--" + item.getEmail());
//                L.e("--08--" + item.getParkingType());
//                L.e("--09--" + item.getPoiId());
//                L.e("--10--" + item.getProvinceCode());
//                L.e("--11--" + item.getProvinceName());
//                L.e("--12--" + item.getSnippet());
//                L.e("--13--" + item.getTitle());
//                L.e("--14--" + item.getTypeDes());
//                L.e("--15--" + item.getDistance());


                helper.setText(R.id.tv_addas01, item.toString());
                helper.setText(R.id.tv_addas, item.getCityName() + item.getAdName() + "(" + item.getSnippet() + ")");
            }
        });
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
