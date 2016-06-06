package com.supaiclient.app.ui.fragment.goods;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.supaiclient.app.R;
import com.supaiclient.app.api.ApiHttpClient;
import com.supaiclient.app.api.UrlUtil;
import com.supaiclient.app.bean.OrderHistoryBean;
import com.supaiclient.app.interf.OnBack;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.base.BaseFragment;
import com.supaiclient.app.util.DateUtils;
import com.supaiclient.app.util.JSonUtils;
import com.supaiclient.app.util.PayUtil;
import com.supaiclient.app.util.UIHelper;


import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.http.HttpParams;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/13.
 * 历史 记录
 */
public class HistoryFragment extends BaseFragment {

    @Bind(R.id.lin_content)
    LinearLayout linContent;
    String onumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, null);
        ButterKnife.bind(this, view);
        onumber = getArguments().getString("onumber");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setDate();
    }

    private void setDate() {

        linContent.removeAllViews();
        HttpParams params = new HttpParams();
        params.put("onumber", onumber);
        ApiHttpClient.postNotShow(getActivity(), UrlUtil.orderhistory, params, new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

                setViewDate(JSonUtils.toBean(OrderHistoryBean.class, responseStr));
            }

            @Override
            public void onFailure(int statusCode) {

            }

            @Override
            public void onSendError(int statusCode, String message) {

            }
        });

    }

    private void setViewDate(OrderHistoryBean ohb) {

        addXidanOK(ohb);

        int status = Integer.parseInt(ohb.getStatus());

        //调试修改的
        // status = 3;

        switch (status) {
            case 0://订单状态，0未付款，1付款，2抢单，3取货，4收货，5取消，6完成评价
                orderDetail(ohb);
                break;
            case 1:
                dengdai(ohb);
                break;
            case 2:
                dengdai(ohb);
                qiangdan(ohb, false);
                //    setKuaidCx(ohb, true);
                mingp(ohb, true);
                break;
            case 3:// 取货  //订单状态，0未付款，1付款，2抢单，3取货，4收货，5取消，6完成评价
                dengdai(ohb);
                qiangdan(ohb, false);
                qiangdan(ohb, true);
                mingp(ohb, true);
                setKuaidCx(ohb, true);
                break;
            case 4:
                //加入前四个
                dengdai(ohb);
                qiangdan(ohb, false);
                qiangdan(ohb, true);
                mingp(ohb, false);
                setKuaidCx(ohb, false);
                setPingjia(ohb);
                break;
            case 5:
                quxiao(ohb);
                break;
            case 6:
                dengdai(ohb);
                qiangdan(ohb, false);
                qiangdan(ohb, true);
                mingp(ohb, false);
                setKuaidCx(ohb, false);
                pingjiawanc(ohb);
                break;
        }

    }

    /**
     * 订单详情的第一步
     *
     * @param ohb
     */

    private void orderDetail(OrderHistoryBean ohb) {
        View view = getViews();
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        TextView tv_btn = (TextView) view.findViewById(R.id.tv_btn);
        tv_time.setText(DateUtils.timestampToDate(ohb.getOtime()));
        tv_btn.setText("去支付");
        tv_btn.setBackgroundResource(R.drawable.sp_base_blue);
        tv_content.setText("下单成功未支付，请去支付");
        linContent.addView(view);

        tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PayUtil(getActivity(), onumber, "商品名称", new OnBack() {
                    @Override
                    public void onBack() {
                        setDate();
                    }
                }).payStart();
            }
        });
    }

    private View getViews() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.history_view, null);
    }

    //下单成功 view
    private void addXidanOK(OrderHistoryBean ohb) {

        View view = getViews();
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_content.setVisibility(View.VISIBLE);
        TextView tv_btn = (TextView) view.findViewById(R.id.tv_btn);
        tv_time.setText(DateUtils.timestampToDate(ohb.getOtime()));
        tv_btn.setVisibility(View.GONE);
        tv_content.setText("下单成功");
        linContent.addView(view);

    }

    //订单 已经 被取消  view
    private void quxiao(OrderHistoryBean ohb) {

        View view = getViews();
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_content.setVisibility(View.VISIBLE);
        TextView tv_btn = (TextView) view.findViewById(R.id.tv_btn);
        tv_time.setText(DateUtils.timestampToDate(ohb.getDeltime()));
        tv_btn.setVisibility(View.GONE);
        tv_content.setText("您的订单已取消，期待下一次一起和超人拯救世界。");
        linContent.addView(view);
    }

    //订单 已经评价
    private void pingjiawanc(OrderHistoryBean ohb) {

        View view = getViews();
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_content.setVisibility(View.VISIBLE);
        TextView tv_btn = (TextView) view.findViewById(R.id.tv_btn);
        tv_time.setText(DateUtils.timestampToDate(ohb.getReplaytime()));
        tv_btn.setVisibility(View.GONE);
        tv_content.setText("感谢您的评价，超人们会更加努力为你服务");
        linContent.addView(view);
    }

    //抢单 view
    private void qiangdan(OrderHistoryBean ohb, boolean isquhuo) {

        View view = getViews();
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        TextView tv_btn = (TextView) view.findViewById(R.id.tv_btn);
        TextView tv_jijianma = (TextView) view.findViewById(R.id.tv_jijianma);
        tv_content.setVisibility(View.VISIBLE);


        tv_btn.setVisibility(View.GONE);
        if (isquhuo) {// 已经 收货
            tv_time.setText(DateUtils.timestampToDate(ohb.getGettime()));
            tv_content.setText("速派超人已取货，请牢记您的收件码并在收件时出示");
            tv_jijianma.setText("收件码：" + ohb.getTakecode());
        } else {
            tv_time.setText(DateUtils.timestampToDate(ohb.getBattletime()));
            tv_content.setText("速派超人已抢单，请牢记您的寄件码并在寄件时出示");
            tv_jijianma.setText("寄件码：" + ohb.getGetcode());
        }

        tv_jijianma.setVisibility(View.VISIBLE);
        linContent.addView(view);
    }

    //名片 view
    private void mingp(final OrderHistoryBean ohb, boolean showwenzhi) {

        View view = getViews();
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        TextView tv_btn = (TextView) view.findViewById(R.id.tv_btn);
        TextView tv_jijianma = (TextView) view.findViewById(R.id.tv_jijianma);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        LinearLayout Lin_mingp = (LinearLayout) view.findViewById(R.id.Lin_mingp);
        TextView tv_btn_dir = (TextView) view.findViewById(R.id.tv_btn_dir);
        tv_time.setText(DateUtils.timestampToDate(ohb.getBattletime()));

        tv_btn.setVisibility(View.GONE);

        tv_content.setText("快和您的超人联系吧");
        tv_content.setVisibility(View.GONE);
        tv_btn.setText("超人位置");
        tv_jijianma.setVisibility(View.GONE);
//152 1508 0593
        //0408---加载载超人图片
        ImageView img_card = (ImageView) view.findViewById(R.id.img_card);

        if (!TextUtils.isEmpty(ohb.getSenduser().getSuphoto())) {

            KJBitmap kjBitmap = new KJBitmap();
            kjBitmap.display(img_card, ohb.getSenduser().getSuphoto());
        }
        Lin_mingp.setVisibility(View.VISIBLE);
        if (showwenzhi)
            tv_btn_dir.setVisibility(View.VISIBLE);
        else
            tv_btn_dir.setVisibility(View.GONE);
        tv_name.setText(ohb.getSenduser().getSuname());
        tv_phone.setText(ohb.getSenduser().getSuphone());
        tv_btn_dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kuaidCx(ohb);
            }
        });

        linContent.addView(view);
    }

    private void dengdai(final OrderHistoryBean ohb) {

        View view2 = getViews();
        TextView tv_time2 = (TextView) view2.findViewById(R.id.tv_time);
        TextView tv_content = (TextView) view2.findViewById(R.id.tv_content);
        tv_content.setVisibility(View.VISIBLE);
        TextView tv_btn2 = (TextView) view2.findViewById(R.id.tv_btn);
        tv_time2.setText(DateUtils.timestampToDate(ohb.getPaytime()));
        tv_btn2.setVisibility(View.GONE);
        tv_content.setText("等待速派超人们来抢单吧");
        linContent.addView(view2);
    }


    private void setKuaidCx(final OrderHistoryBean ohb, boolean showkuaidi) {

        View view = getViews();
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_content.setVisibility(View.VISIBLE);
        TextView tv_btn = (TextView) view.findViewById(R.id.tv_btn);
        tv_time.setText(DateUtils.timestampToDate(ohb.getOtime()));
        tv_btn.setText("物件追踪");
        tv_btn.setBackgroundResource(R.drawable.sp_base_red);
        if (showkuaidi) {
            tv_btn.setVisibility(View.VISIBLE);
        } else {
            tv_btn.setVisibility(View.GONE);
        }
//        tv_content.setText("看来您已经和您的超人会面了，超人会在最快的时间内将神秘包裹送达，拯救地球。您可以随时在您的快递查询中关心超人动向");

        tv_content.setText("取件成功,超人会为您飞速送达");

        linContent.addView(view);

        tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                kuaidCx(ohb);
            }
        });
    }


    // 快递 查询
    private void kuaidCx(OrderHistoryBean ohb) {


        // showWaitDialog("加载中..");

        Intent intent = new Intent(getActivity(), WuJianActivity.class);
        intent.putExtra("onumber", onumber);
        startActivity(intent);

    /*    RequestParams params = new RequestParams();
        params.put("onumber", onumber);
        ApiHttpClient.post(getActivity(), UrlUtil.orderodloca, params, new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

                hideWaitDialog();
                final ArrayList<FindspmanBean> arrayList = (ArrayList<FindspmanBean>) JSonUtils.toList(FindspmanBean.class, responseStr);
                if (arrayList.size() > 0) {

                    FindspmanBean findspmanBean = arrayList.get(0);
                    Intent intent = new Intent(getActivity(), WuJianActivity.class);
                    intent.putExtra("findspmanBean", findspmanBean);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(int statusCode) {
                hideWaitDialog();
            }

            @Override
            public void onSendError(int statusCode, String message) {
                hideWaitDialog();
            }
        });*/

    }

    private void setPingjia(final OrderHistoryBean ohb) {

        View view = getViews();
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_content.setVisibility(View.VISIBLE);
        TextView tv_btn = (TextView) view.findViewById(R.id.tv_btn);
        tv_time.setText(DateUtils.timestampToDate(ohb.getCompletetime()));
        tv_btn.setText("去评价");
        tv_btn.setBackgroundResource(R.drawable.sp_base_green);
        tv_content.setText("神秘包裹安全送达，去给您的超人一个好评吧");
        linContent.addView(view);

        tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UIHelper.OPENPINGLUN(getActivity(), onumber);

            }
        });
    }

}
