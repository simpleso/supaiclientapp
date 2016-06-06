package com.supaiclient.app.ui.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.supaiclient.app.R;
import com.supaiclient.app.api.ApiHttpClient;
import com.supaiclient.app.api.UrlUtil;
import com.supaiclient.app.bean.FindspmanBean;
import com.supaiclient.app.bean.GoodsBean;
import com.supaiclient.app.interf.OnBack;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.adapter.base.ListBaseAdapter;
import com.supaiclient.app.ui.fragment.goods.WuJianActivity;
import com.supaiclient.app.util.DateUtils;
import com.supaiclient.app.util.JSonUtils;
import com.supaiclient.app.util.PayUtil;
import com.supaiclient.app.util.UIHelper;


import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 订单适配器
 * Created by zgc on 16/2/4.
 */
public class GoodsAdapter extends ListBaseAdapter<GoodsBean> {

    private OnBack onBack;

    public GoodsAdapter(OnBack onBack) {
        this.onBack = onBack;
    }

    @Override
    protected View getRealView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.listview_goods, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final GoodsBean goodsBean = mDatas.get(position);

        //清除之前的数据
        vh.clear();

        vh.tv_number.setText(goodsBean.getOnumber());
        vh.tv_jijian.setText(goodsBean.getSendaddress());
        vh.tv_shou.setText(goodsBean.getTadd());
        vh.tv_jiage.setText(goodsBean.getNeedprice() + "");
        vh.tv_time.setText(DateUtils.timestampToDate(goodsBean.getOtime()));
        vh.tv_juli.setText(goodsBean.getWeight() + "kg/" + goodsBean.getDistance() + "km");

        String status = goodsBean.getStatus();

        //0:未付款，1:待速派 2:待取件  3:待收货 4:待评价 5: 已取消  6:完成
        vh.re_ma.setVisibility(View.GONE);
        vh.tv_zhuantai.setVisibility(View.GONE);
        vh.tv_status.setVisibility(View.VISIBLE);

        vh.tv_status.setOnClickListener(null);
        if (status.equals("0")) {

            vh.tv_statusshow.setText("待支付");
            vh.tv_status.setBackgroundResource(R.drawable.sp_base_blue);
            vh.tv_status.setText("去支付");

            vh.tv_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    GoodsBean goodsBean = mDatas.get(position);
                    new PayUtil(context, goodsBean.getOnumber(), "商品名称", onBack).payStart();
                }
            });
        } else if (status.equals("1")) {

            vh.tv_statusshow.setText("待速派");
            vh.tv_status.setBackgroundResource(R.drawable.sp_base_blue);
            vh.tv_status.setVisibility(View.GONE);
        } else if (status.equals("2")) {

            vh.tv_statusshow.setText("待取件");
            vh.tv_status.setBackgroundResource(R.drawable.sp_base_blue);
            vh.tv_status.setVisibility(View.GONE);
            vh.re_ma.setVisibility(View.VISIBLE);
            vh.tv_ma.setText("寄件码");
            vh.tv_ma_show.setText(goodsBean.getGetcode());

//            //加上派送中
//            vh.tv_zhuantai.setVisibility(View.VISIBLE);
//            vh.tv_zhuantai.setImageResource(R.mipmap.iconfont_tubiaopaishongzhong);

        } else if (status.equals("3")) {
            vh.tv_statusshow.setText("待收货");
            vh.tv_status.setBackgroundResource(R.drawable.sp_base_blue);
            vh.tv_status.setVisibility(View.VISIBLE);
            vh.tv_status.setText("物件追踪");
            vh.tv_status.setBackgroundResource(R.drawable.sp_base_red);
            vh.re_ma.setVisibility(View.VISIBLE);
            vh.tv_ma.setText("收件码");
            vh.tv_ma_show.setText(goodsBean.getTakecode());

//            //加上派送中
//            vh.tv_zhuantai.setVisibility(View.VISIBLE);
//            vh.tv_zhuantai.setImageResource(R.mipmap.iconfont_tubiaopaishongzhong);

            vh.tv_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HttpParams params = new HttpParams();
                    params.put("onumber", goodsBean.getOnumber());
                    ApiHttpClient.post(context, UrlUtil.orderodloca, params, new RequestBasetListener() {
                        @Override
                        public void onSuccess(String responseStr) {

                            final ArrayList<FindspmanBean> arrayList = (ArrayList<FindspmanBean>) JSonUtils.toList(FindspmanBean.class, responseStr);
                            if (arrayList.size() > 0) {

                                FindspmanBean findspmanBean = arrayList.get(0);
                                Intent intent = new Intent(context, WuJianActivity.class);
                                intent.putExtra("findspmanBean", findspmanBean);
                                context.startActivity(intent);
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
            });
        } else if (status.equals("4")) {

            vh.tv_statusshow.setText("待评价");
            vh.tv_status.setText("去评价");
            vh.tv_status.setBackgroundResource(R.drawable.sp_base_green);
            vh.tv_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    GoodsBean goodsBean = mDatas.get(position);
                    UIHelper.OPENPINGLUN(context, goodsBean.getOnumber());

//                    new PayUtil(context, goodsBean.getOnumber(), "商品名称", onBack).payStart();
                }
            });

        } else if (status.equals("5")) {

            vh.tv_statusshow.setText("已取消");
            vh.tv_status.setBackgroundResource(R.drawable.sp_base_blue);
            vh.tv_status.setVisibility(View.GONE);
            vh.tv_zhuantai.setVisibility(View.VISIBLE);
            vh.tv_zhuantai.setImageResource(R.mipmap.iconfont_tubiaoyiquxiao01);
        } else if (status.equals("6")) {

            vh.tv_statusshow.setText("已完成");
            vh.tv_status.setVisibility(View.GONE);
            vh.tv_status.setBackgroundResource(R.drawable.sp_base_blue);
            vh.tv_zhuantai.setVisibility(View.VISIBLE);
            vh.tv_zhuantai.setImageResource(R.mipmap.iconfont_yiwancheng);
        }

        return convertView;
    }


    class ViewHolder {

        @Bind(R.id.tv_number)
        TextView tv_number;

        @Bind(R.id.tv_jijian)
        TextView tv_jijian;
        @Bind(R.id.tv_shou)
        TextView tv_shou;

        @Bind(R.id.tv_jiage)
        TextView tv_jiage;

        @Bind(R.id.tv_juli)
        TextView tv_juli;

        @Bind(R.id.tv_time)
        TextView tv_time;

        @Bind(R.id.tv_statusshow)
        TextView tv_statusshow;

        @Bind(R.id.tv_status)
        TextView tv_status;

        @Bind(R.id.re_ma)
        RelativeLayout re_ma;

        @Bind(R.id.tv_ma)
        TextView tv_ma;

        @Bind(R.id.tv_ma_show)
        TextView tv_ma_show;

        @Bind(R.id.tv_zhuantai)
        ImageView tv_zhuantai;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        void clear() {
            tv_number.setText("");
            tv_jijian.setText("");
            tv_shou.setText("");
            tv_jiage.setText("");
            tv_juli.setText("");
            tv_time.setText("");
            tv_statusshow.setText("");
            tv_status.setText("");
            tv_ma.setText("");
            tv_ma_show.setText("");
        }
    }

}
