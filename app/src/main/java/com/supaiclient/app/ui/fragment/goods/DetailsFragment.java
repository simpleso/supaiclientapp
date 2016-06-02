package com.supaiclient.app.ui.fragment.goods;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.supaiclient.app.R;
import com.supaiclient.app.api.ApiHttpClient;
import com.supaiclient.app.api.UrlUtil;
import com.supaiclient.app.bean.OrderDetailBean;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.base.BaseFragment;
import com.supaiclient.app.util.DateUtils;
import com.supaiclient.app.util.JSonUtils;

import org.kymjs.kjframe.KJBitmap;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 订单详情
 * Created by Administrator on 2016/3/13.
 */
public class DetailsFragment extends BaseFragment {


    @Bind(R.id.tv_left)
    ImageView tvLeft;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.title_right_tv)
    TextView titleRightTv;
    @Bind(R.id.iv_right_title)
    ImageView ivRightTitle;
    @Bind(R.id.lin_title)
    LinearLayout linTitle;
    @Bind(R.id.tv_qujianadd)
    TextView tvQujianadd;
    @Bind(R.id.tv_jijianadd)
    TextView tvJijianadd;
    @Bind(R.id.et_quname)
    EditText etQuname;
    @Bind(R.id.et_quphone)
    EditText etQuphone;
    @Bind(R.id.iv_jijian_se)
    ImageView ivJijianSe;
    @Bind(R.id.et_shouname)
    EditText etShouname;
    @Bind(R.id.et_shouphone)
    EditText etShouphone;
    @Bind(R.id.iv_shoujian_se)
    ImageView ivShoujianSe;
    @Bind(R.id.tv_ordertype)
    TextView tvOrdertype;
    @Bind(R.id.et_ordername)
    TextView etOrdername;
    @Bind(R.id.et_zhuname)
    EditText etZhuname;
    @Bind(R.id.et_beizhu)
    EditText etBeizhu;
    @Bind(R.id.iv_photo)
    ImageView ivPhoto;
    @Bind(R.id.cb_jc)
    RadioButton cbJc;
    @Bind(R.id.cb_mt)
    RadioButton cbMt;
    @Bind(R.id.cb_bx)
    RadioButton cbBx;
    @Bind(R.id.tv_gongli)
    TextView tvGongli;
    @Bind(R.id.seekBar)
    SeekBar seekBar;
    @Bind(R.id.tv_gongjin)
    TextView tvGongjin;
    @Bind(R.id.tv_jiajMoney)
    TextView tvJiajMoney;
    @Bind(R.id.tv_qijia)
    TextView tvQijia;
    @Bind(R.id.tv_jiajian)
    TextView tvJiajian;
    @Bind(R.id.tv_zongjia)
    TextView tvZongjia;
    @Bind(R.id.btn_next2)
    Button btnNext2;

    @Bind(R.id.lin_jiaotong)
    LinearLayout lin_jiaotong;

    @Bind(R.id.Re_jiai)
    RelativeLayout Re_jiai;

    @Bind(R.id.rl_yejian)
    RelativeLayout rl_yejian;

    @Bind(R.id.tv_yejianjia)
    TextView tv_yejianjia;

    @Bind(R.id.ll_jifen)
    LinearLayout ll_jifen;
    @Bind(R.id.ll_jifenlayout)
    LinearLayout ll_jifenlayout;
    @Bind(R.id.tv_jifen1)
    TextView tv_jifen1;
    @Bind(R.id.tv_dikou1)
    TextView tv_dikou1;

    @Bind(R.id.id_rl_add)
    RelativeLayout id_rl_add;

    @Bind(R.id.view_height)
    View viewHeight;
    @Bind(R.id.tv_showyuyue)
    TextView tvShowyuyue;

    @Bind(R.id.tv_service)
    TextView tv_service;
    @Bind(R.id.el_service)
    RelativeLayout el_service;
    @Bind(R.id.lin_otherServise)
    LinearLayout lin_otherServise;
    @Bind(R.id.lin_goods_type)
    LinearLayout lin_goods_type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_submitorder, null);
        ButterKnife.bind(this, view);
        view.findViewById(R.id.lien).setVisibility(View.GONE);
        view.findViewById(R.id.line2).setVisibility(View.GONE);
        init();
        return view;
    }

    private void init() {

        linTitle.setVisibility(View.GONE);
        btnNext2.setVisibility(View.GONE);
        ivJijianSe.setVisibility(View.GONE);
        ivShoujianSe.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);
        Re_jiai.setVisibility(View.GONE);

        String onumber = getArguments().getString("onumber");

        RequestParams params = new RequestParams();
        params.put("onumber", onumber);
        ApiHttpClient.post(getActivity(), UrlUtil.orderdetail, params, new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

                //   L.e(responseStr);

                OrderDetailBean orderDetailBean = JSonUtils.toBean(OrderDetailBean.class, responseStr);
                setView(orderDetailBean);
            }

            @Override
            public void onFailure(int statusCode) {

            }

            @Override
            public void onSendError(int statusCode, String message) {

            }
        });
    }

    private void setView(OrderDetailBean orderDetailBean) {

        tvQujianadd.setText(orderDetailBean.getSendadd().getSendaddress());
        tvJijianadd.setText(orderDetailBean.getTakeadd().getTadd());
        etQuname.setText(orderDetailBean.getSendadd().getSendname());
        etQuname.setEnabled(false);
        etQuphone.setText(orderDetailBean.getSendadd().getSendphone());
        etQuphone.setEnabled(false);

        etShouname.setText(orderDetailBean.getTakeadd().getTname());
        etShouname.setEnabled(false);
        etShouphone.setText(orderDetailBean.getTakeadd().getTphone());
        etShouphone.setEnabled(false);

        DecimalFormat df = new DecimalFormat("############0.00");

        ll_jifen.setVisibility(View.GONE);


        if (orderDetailBean.getPoints() > 0) {
            tv_jifen1.setText(orderDetailBean.getPoints() + "");
            tv_dikou1.setText(df.format(orderDetailBean.getPoints() * orderDetailBean.getPercent()));
        } else {
            ll_jifenlayout.setVisibility(View.GONE);
        }

        if (orderDetailBean.getTaketype().equals("2")) {
            tvOrdertype.setText(DateUtils.timestampToDate(orderDetailBean.getTaketime()));
            viewHeight.setVisibility(View.VISIBLE);
            tvShowyuyue.setVisibility(View.VISIBLE);
        } else if (orderDetailBean.getTaketype().endsWith("1")) {
            tvShowyuyue.setVisibility(View.GONE);
            viewHeight.setVisibility(View.GONE);
            tvOrdertype.setText("立即取件");
        }

        if (TextUtils.isEmpty(orderDetailBean.getGimg())) {
            ivPhoto.setVisibility(View.GONE);
        } else {
            ivPhoto.setVisibility(View.VISIBLE);
            KJBitmap kjBitmap = new KJBitmap();
            kjBitmap.display(ivPhoto, orderDetailBean.getGimg());
        }
        etZhuname.setText(orderDetailBean.getOname());
        etZhuname.setEnabled(false);

        etBeizhu.setText(orderDetailBean.getMessage());
        etBeizhu.setEnabled(false);
//        lin_jiaotong.setVisibility(View.GONE);

        cbBx.setEnabled(false);
        cbMt.setEnabled(false);
        cbJc.setEnabled(false);

        id_rl_add.setVisibility(View.GONE);

        if (orderDetailBean.getAddprice().equals("0.00")) {
            el_service.setVisibility(View.GONE);
        } else {
            el_service.setVisibility(View.VISIBLE);
            tv_service.setText("￥" + orderDetailBean.getAddprice());
        }

        if (orderDetailBean.getCpstyle().equals("1")) {
            cbBx.setChecked(true);
            cbBx.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
            cbMt.setChecked(false);
            cbJc.setChecked(false);
        } else if (orderDetailBean.getCpstyle().equals("2")) {
            cbBx.setChecked(false);
            cbMt.setChecked(true);
            cbMt.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
            cbJc.setChecked(false);
        } else if (orderDetailBean.getCpstyle().equals("3")) {
            cbBx.setChecked(true);
            cbBx.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
            cbMt.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
            cbMt.setChecked(true);
            cbJc.setChecked(false);
        } else if (orderDetailBean.getCpstyle().equals("4")) {
            cbBx.setChecked(false);
            cbMt.setChecked(false);
            cbJc.setChecked(true);
            cbJc.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
        } else if (orderDetailBean.getCpstyle().equals("5")) {
            cbBx.setChecked(true);
            cbBx.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
            cbMt.setChecked(false);
            cbJc.setChecked(true);
            cbJc.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
        } else if (orderDetailBean.getCpstyle().equals("6")) {
            cbBx.setChecked(false);
            cbMt.setChecked(true);
            cbMt.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
            cbJc.setChecked(true);
            cbJc.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
        } else if (orderDetailBean.getCpstyle().equals("7")) {
            cbBx.setChecked(true);
            cbBx.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
            cbMt.setChecked(true);
            cbMt.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
            cbJc.setChecked(true);
            cbJc.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
        }

        if (orderDetailBean.getNight() == 0.0f) {
            rl_yejian.setVisibility(View.GONE);
        } else {
            rl_yejian.setVisibility(View.VISIBLE);
            tv_yejianjia.setText("￥" + df.format(Float.valueOf(orderDetailBean.getNight() + "")));
        }

        tvGongli.setText(orderDetailBean.getDistance());
        tvGongjin.setText(orderDetailBean.getWeight());
//        tvQijia.setText("￥" + orderDetailBean.getWeight());

        tvZongjia.setText("￥" + orderDetailBean.getNeedprice() + "");
        tvQijia.setText("￥" + (Float.valueOf(orderDetailBean.getTotalprice()) - orderDetailBean.getNight()));

        //L.e(orderDetailBean.toString());

        lin_otherServise.setVisibility(View.GONE);
        lin_goods_type.setVisibility(View.GONE);

    }
}
