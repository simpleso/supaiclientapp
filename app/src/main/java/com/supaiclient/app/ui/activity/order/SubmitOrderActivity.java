package com.supaiclient.app.ui.activity.order;

import android.Manifest;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.baoyz.actionsheet.ActionSheet;
import com.supaiclient.app.BaseApplication;
import com.supaiclient.app.R;
import com.supaiclient.app.api.ApiHttpClient;
import com.supaiclient.app.api.OrderApi;
import com.supaiclient.app.bean.OrderSubmitBean;
import com.supaiclient.app.bean.PeopleBean;
import com.supaiclient.app.bean.SthType;
import com.supaiclient.app.interf.OnBack;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.adapter.base.BaseAdapterHelper;
import com.supaiclient.app.ui.adapter.base.QuickAdapter;
import com.supaiclient.app.ui.base.BaseActivity;
import com.supaiclient.app.ui.base.TimeDialogActivity;
import com.supaiclient.app.ui.dialog.StdmodeDialog;
import com.supaiclient.app.ui.fragment.MainFragment;
import com.supaiclient.app.util.DateUtils;
import com.supaiclient.app.util.FileUtils;
import com.supaiclient.app.util.ImageUtils;
import com.supaiclient.app.util.JSonUtils;
import com.supaiclient.app.util.L;
import com.supaiclient.app.util.PayUtil;
import com.supaiclient.app.util.T;
import com.supaiclient.app.util.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.supaiclient.app.R.id.et_jiajMoney;
import static com.supaiclient.app.R.id.rb_jc;
import static com.supaiclient.app.R.id.tv_ordertype;

/**
 * Created by Administrator on 2015/12/28.
 * 提交订单
 */
public class SubmitOrderActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    private static final String TAG = "SubmitOrderActivity";

    @Bind(R.id.cb_mo)
    RadioButton cb_mo;
    //    @Bind(R.id.cb_mt)
//    RadioButton cbMt;
    @Bind(R.id.cb_hc)
    RadioButton cb_hc;


    @Bind(rb_jc)
    CheckBox rbJc;
    @Bind(R.id.rb_mt)
    CheckBox rbMt;
    @Bind(R.id.rb_bx)
    CheckBox rbBx;


    //    @Bind(R.id.seekBar)
//    SeekBar seekBar;
    @Bind(et_jiajMoney)
    EditText etJiajMoney;
    @Bind(R.id.tv_zongjia)
    TextView tv_zongjia;
    @Bind(R.id.tv_yejianjia)
    TextView tv_yejianjia;
    @Bind(R.id.iv_photo)
    ImageView ivPhoto;
    @Bind(R.id.btn_next2)
    Button btnNext;
    @Bind(R.id.iv_jijian_se)
    ImageView ivJijianSe;
    @Bind(R.id.iv_shoujian_se)
    ImageView iv_shoujian_se;
    @Bind(R.id.tv_qujianadd)
    TextView tvQujianadd;
    @Bind(R.id.tv_jijianadd)
    TextView tvJijianadd;
    @Bind(R.id.et_quname)
    EditText etQuname;
    @Bind(R.id.et_quphone)
    EditText etQuphone;
    @Bind(R.id.et_shouname)
    EditText etShouname;
    @Bind(R.id.et_shouphone)
    EditText etShouphone;
    @Bind(tv_ordertype)
    TextView tvOrdertype;
    //    @Bind(R.id.et_ordername)
//    TextView etOrdername;
    @Bind(R.id.et_beizhu)
    EditText etBeizhu;
    //    @Bind(R.id.et_zhuname)
//    EditText etZhuname;
    @Bind(R.id.tv_jiajian)
    TextView tv_jiajian;
    @Bind(R.id.tv_qijia)
    TextView tv_qijia;
    @Bind(R.id.et_gongjin)
    EditText et_gongjin;
    @Bind(R.id.tv_gongli)
    TextView tv_gongli;
    @Bind(R.id.rl_yejian)
    RelativeLayout rl_yejian;
    @Bind(R.id.lv_root)
    LinearLayout lv_root;
    @Bind(R.id.seekBar1)
    SeekBar mSeekBar1;
    @Bind(R.id.tv_dikou)
    TextView tv_dikou;
    @Bind(R.id.tv_jifen)
    TextView tv_jifen;
    @Bind(R.id.ll_jifenlayout)
    LinearLayout ll_jifenlayout;
    @Bind(R.id.ll_jifen)
    LinearLayout ll_jifen;

    @Bind(R.id.view_height)
    View viewHeight;
    @Bind(R.id.tv_showyuyue)
    TextView tvShowyuyue;
    @Bind(R.id.sp_type)
    TextView sp_type;
    @Bind(R.id.tv_service)
    TextView tv_service;
    @Bind(R.id.tv_describe)
    TextView tv_describe;
    @Bind(R.id.tv_zongjia1)
    TextView tv_zongjia1;

    //    @Bind(R.id.rg_otherServise)
//    RadioGroup rg_otherServise;
    @Bind(R.id.rg_paisong)
    RadioGroup rg_paisong;

    @Bind(R.id.cb_cb1)
    CheckBox cb_cb1;
    @Bind(R.id.cb_cb2)
    CheckBox cb_cb2;
    @Bind(R.id.cb_cb3)
    CheckBox cb_cb3;
    @Bind(R.id.cb_cb4)
    CheckBox cb_cb4;
    @Bind(R.id.cb_cb5)
    CheckBox cb_cb5;
    @Bind(R.id.cb_cb6)
    CheckBox cb_cb6;
    @Bind(R.id.cb_cb7)
    CheckBox cb_cb7;
    @Bind(R.id.cb_cb8)
    CheckBox cb_cb8;

    @Bind(R.id.iv_gjjia)
    ImageView iv_gjjia;
    @Bind(R.id.iv_gjjian)
    ImageView iv_gjjian;

    @Bind(R.id.iv_jgjia)
    ImageView iv_jgjia;
    @Bind(R.id.iv_jgjian)
    ImageView iv_jgjian;

    @Bind(R.id.tv_pstype)
    TextView tv_pstype;

    List<SthType> list;


    private Target target;
    private double yuanjia;// 原来的  价格
    private OrderSubmitBean osb;
    private int type;// 1: 寄件人 选择 联系人  2：收件人 选择 联系人
    private String theLarge = "";
    private String theThumbnail = "";
    private double DiKou = 0;
    private String taketime = "0";
    private String price = "0";
    private String night = "0";
    private String points = "0";
    private float percent = 0;
    private int weight = 0;
    private int distance = 0;
    private double addPice = 0;
    private double servise = 0;
    private int serviseType = 0;
    private double goodstype = 0;
    private PeopleBean peopleBean_sj; //收件人地址 对象
    private PeopleBean peopleBean_jj; // 取件人 地址 对象
    private PeopleBean peopleBean_dw; // 定位 地址 对象
    private String city;
    private int paisongtype = 0;
    private int goodstypeposition;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_submitorder;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        ButterKnife.bind(this);

//        //不显示输入法 0511
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        getSupportActionBar().hide();

        TextView title_content_tv = (TextView) findViewById(R.id.title_content_tv);
        title_content_tv.setText("提交订单");

//        setActionBarTitle("提交订单");

        findViewById(R.id.tv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SubmitOrderActivity.this.finish();
            }
        });

        ll_jifenlayout.setVisibility(View.GONE);

//        setActionBarTitle("提交订单");
        osb = (OrderSubmitBean) getIntent().getSerializableExtra("orderSubmitBean");
        peopleBean_dw = (PeopleBean) getIntent().getSerializableExtra("peopleBean_dw");
        city = getIntent().getStringExtra("city");

        peopleBean_jj = osb.getPeopleBean_jj();
        peopleBean_sj = osb.getPeopleBean_sj();
        init();
        showWaitDialog("请稍后...");

        KJHttp kjHttp = new KJHttp();
        kjHttp.get("https://raw.githubusercontent.com/simpleso/supaiclientapp/master/price", new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                Log.d(TAG, "onSuccess() called with: s = " + s);
                list = JSonUtils.toList(SthType.class, s);

                List<String> strings = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {
                    strings.add(list.get(i).getName());
                }

                L.e(list.toString());

                //默认使用其他
                goodstypeposition = list.size() - 1;

                hideWaitDialog();
                //  ArrayAdapter<String> adapter = new ArrayAdapter<>(SubmitOrderActivity.this, android.R.layout.simple_spinner_item, strings);

//                sp_type.setAdapter(new QuickAdapter<String>(SubmitOrderActivity.this, R.layout.spinner_item, strings) {
//                    @Override
//                    protected void convert(BaseAdapterHelper helper, String item) {
//                        helper.setText(R.id.tv_spinner, item);
//                    }
//                });
//
//                sp_type.setSelection(list.size() - 1);


                sp_type.setText(list.get(goodstypeposition).getName());

                String s1 = list.get(goodstypeposition).getDescribe();

                if (!TextUtils.isEmpty(s1)) {
                    tv_describe.setVisibility(View.VISIBLE);
                    tv_describe.setText(s1);
                } else {
                    tv_describe.setVisibility(View.GONE);
                }

                sumPrice();

           /*     sp_type.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (list.get(position) != null) {

                            goodstype = list.get(position).getPrice();
                            goodstypeposition = position;
                            String s1 = list.get(position).getDescribe();
                            if (!TextUtils.isEmpty(s1)) {
                                tv_describe.setVisibility(View.VISIBLE);
                                tv_describe.setText(s1);
                            } else {
                                tv_describe.setVisibility(View.GONE);
                            }
                        }

                        sumPrice();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });*/

                sp_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String s[] = new String[list.size()];

                        for (int i = 0; i < list.size(); i++) {
                            s[i] = list.get(i).getName();
                        }

                       /* AlertDialog.Builder builder = new AlertDialog.Builder(SubmitOrderActivity.this);
                        builder.setItems(s, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                goodstype = list.get(which).getPrice();
                                goodstypeposition = which;
                                String s1 = list.get(which).getDescribe();
                                if (!TextUtils.isEmpty(s1)) {
                                    tv_describe.setVisibility(View.VISIBLE);
                                    tv_describe.setText(s1);
                                } else {
                                    tv_describe.setVisibility(View.GONE);
                                }
                                sp_type.setText(list.get(which).getName());
                                sumPrice();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                        params.height = DensityUtil.getPhoneHeight(SubmitOrderActivity.this) >> 1;
                        dialog.getWindow().setAttributes(params);
                        dialog.show();*/

                        final StdmodeDialog mStdmodeDialog = new StdmodeDialog(SubmitOrderActivity.this);

                        mStdmodeDialog.setAdapter(new QuickAdapter<SthType>(SubmitOrderActivity.this, R.layout.spinner_item, list) {
                            @Override
                            protected void convert(BaseAdapterHelper helper, SthType item) {

                                helper.setText(R.id.tv_name, item.getName());
                                helper.setText(R.id.tv_price, item.getPrice() + "");
//                                if (item.getPrice() == 0) {
//                                    helper.setVisible(R.id.tv_price, false);
//                                } else {
//                                    helper.setVisible(R.id.tv_price, true);
//                                }
                            }
                        });
                        mStdmodeDialog.setSelectedListener(new StdmodeDialog.SelectedListener() {
                            @Override
                            public void onSelected(int position) {
                                Log.e(TAG, "onItemSelected() called with: position = [" + position + "]");

                                mStdmodeDialog.dismiss();
                                goodstype = list.get(position).getPrice();
                                goodstypeposition = position;
                                String s1 = list.get(position).getDescribe();
                                if (!TextUtils.isEmpty(s1)) {
                                    tv_describe.setVisibility(View.VISIBLE);
                                    tv_describe.setText(s1);
                                } else {
                                    tv_describe.setVisibility(View.GONE);
                                }
                                sp_type.setText(list.get(position).getName());
                                sumPrice();
                            }
                        });
                        mStdmodeDialog.show();
                    }
                });
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);

                hideWaitDialog();
                T.s(R.string.networf_errot);
                Log.d(TAG, "onFailure() called with: errorNo = [" + errorNo + "], strMsg = [" + strMsg + "]");

            }
        });


    }

    private void init() {

        if (osb != null) {

            if (peopleBean_jj != null) {
                tvQujianadd.setText(peopleBean_jj.getAdd());

                String jiname = peopleBean_jj.getName();
                if (!TextUtils.isEmpty(jiname)) {
                    etQuname.setText(jiname);
                }

                String jiphone = peopleBean_jj.getPhone();
                if (!TextUtils.isEmpty(jiphone)) {
                    etQuphone.setText(jiphone);
                }
            }

            if (peopleBean_jj != null) {

                tvJijianadd.setText(peopleBean_sj.getAdd());
                String quname = peopleBean_sj.getName();
                if (!TextUtils.isEmpty(quname)) {
                    etShouname.setText(quname);
                }

                String quphone = peopleBean_sj.getPhone();
                if (!TextUtils.isEmpty(quphone)) {
                    etShouphone.setText(quphone);
                }
            }

            et_gongjin.setText(osb.getWeight());
            tv_gongli.setText(osb.getDistance());

            String taketype = osb.getTaketype();
            if (taketype.equals("1")) {

                viewHeight.setVisibility(View.GONE);
                tvShowyuyue.setVisibility(View.GONE);
                tvOrdertype.setText("立即取件");

            } else {

                viewHeight.setVisibility(View.VISIBLE);
                tvShowyuyue.setVisibility(View.VISIBLE);
                tvOrdertype.setText(DateUtils.timestampToDate(osb.getTaketime()));
            }

            tvOrdertype.setOnClickListener(this);

            DecimalFormat df = new DecimalFormat("############0.00");


            tv_zongjia.setText("￥" + df.format(yuanjia));

//            if (osb.getNightnight().equals("5")) {
//                rl_yejian.setVisibility(View.VISIBLE);
//            } else if (osb.getNightnight().equals("0")) {
//                rl_yejian.setVisibility(View.GONE);
//            }

            if (osb.getPoints().equals("0")) {
                ll_jifen.setVisibility(View.GONE);
            } else {
                ll_jifen.setVisibility(View.VISIBLE);
            }

            //   L.e(osb.getNightnight());

            if (osb.getNightnight().equals("0")) {
                rl_yejian.setVisibility(View.GONE);
            } else {
                rl_yejian.setVisibility(View.VISIBLE);
                tv_yejianjia.setText("￥" + df.format(Double.valueOf(osb.getNightnight())));
            }

            tv_qijia.setText("￥" + df.format(yuanjia - Double.valueOf(osb.getNightnight())));

            //得到传递过来的数据
            distance = Integer.valueOf(osb.getDistance());
            night = osb.getNightnight();
            taketime = osb.getTaketime();
            //运价里包含了夜间家 所以减去
            yuanjia = Double.valueOf(getIntent().getStringExtra("price")) - Double.valueOf(osb.getNightnight());
            points = osb.getPoints();
            percent = osb.getPercent();
            weight = Integer.valueOf(osb.getWeight());

        }
//        seekBar.setMax(30);
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//                tvJiajMoney.setText(progress + "");
//                addPice = progress;
//                sumPrice();
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

        if (osb.getPoints().equals("0")) {

            OrderApi.sendordergetprice(this, osb.getWeight(), osb.getDistance(), osb.getTaketime(), new RequestBasetListener() {
                @Override
                public void onSuccess(String responseStr) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseStr);
                        if (jsonObject.has("points")) {
                            osb.setPoints(jsonObject.getString("points"));
                            mSeekBar1.setMax(Integer.valueOf(osb.getPoints()));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode) {

                }

                @Override
                public void onSendError(int statusCode, String message) {

                }
            });

        } else {
            mSeekBar1.setMax(Integer.valueOf(osb.getPoints()));
        }
        mSeekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_jifen.setText(progress + "");
                DecimalFormat df = new DecimalFormat("############0.00");
                tv_dikou.setText(df.format(progress * osb.getPercent()));
                DiKou = seekBar.getProgress();
                sumPrice();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        rg_paisong.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //右移一位 是应为布局中有TextView 影响到了下标
                int index = group.indexOfChild(group.findViewById(checkedId)) >> 1; //就可以得到index

                L.e(index + "==========");

                switch (index) {

                    case 0: {
                        tv_pstype.setText("平台默认使用摩托车或步行配送物品");
                        paisongtype = 0;
                    }
                    break;
                    case 1: {
                        tv_pstype.setText("使用货车配送物品需加8元服务费");
                        paisongtype = 8;
                    }
                    break;
                }

                sumPrice();
            }
        });

        setOnClick();
    }
/*
*           etBeizhu.setCursorVisible(false);
            etOrdername.setCursorVisible(false);
            etZhuname.setCursorVisible(false);
            etShouname.setCursorVisible(false);
            etQuname.setCursorVisible(false);
            etShouphone.setCursorVisible(false);
* */

    private void setOnClick() {

        ivPhoto.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        ivJijianSe.setOnClickListener(this);
        iv_shoujian_se.setOnClickListener(this);

        cb_hc.setOnClickListener(this);
        cb_mo.setOnClickListener(this);
        // cbMt.setOnClickListener(this);

        rbBx.setOnClickListener(this);
        rbJc.setOnClickListener(this);
        rbMt.setOnClickListener(this);


        tvQujianadd.setOnClickListener(this);
        tvJijianadd.setOnClickListener(this);


        cb_mo.setOnCheckedChangeListener(this);
        //cbMt.setOnCheckedChangeListener(this);
        cb_hc.setOnCheckedChangeListener(this);

        rbJc.setOnCheckedChangeListener(this);
        rbMt.setOnCheckedChangeListener(this);
        rbBx.setOnCheckedChangeListener(this);


//        etShouphone.setOnClickListener(this);
//        etOrdername.setOnClickListener(this);
//        etQuname.setOnClickListener(this);
//        etShouname.setOnClickListener(this);
//        etBeizhu.setOnClickListener(this);
//        etQuphone.setOnClickListener(this);
//        etZhuname.setOnClickListener(this);

        cb_cb1.setOnClickListener(this);
        cb_cb2.setOnClickListener(this);
        cb_cb3.setOnClickListener(this);
        cb_cb4.setOnClickListener(this);
        cb_cb5.setOnClickListener(this);
        cb_cb6.setOnClickListener(this);
        cb_cb7.setOnClickListener(this);
        cb_cb8.setOnClickListener(this);

        iv_gjjia.setOnClickListener(this);
        iv_gjjian.setOnClickListener(this);
        iv_jgjia.setOnClickListener(this);
        iv_jgjian.setOnClickListener(this);

        et_gongjin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!TextUtils.isEmpty(s.toString())) {
                    try {
                        weight = Integer.valueOf(s.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    weight = 1;
                }
                getPrice();
            }
        });

        et_gongjin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(et_gongjin.getText().toString())) {
                        weight = 1;
                        et_gongjin.setText(weight + "");
                        et_gongjin.setSelection(et_gongjin.getText().toString().length());
                        getPrice();
                    }
                }
            }
        });

        etJiajMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    try {
                        addPice = Integer.valueOf(s.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    addPice = 0;
                }
                getPrice();
            }
        });

        etJiajMoney.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(etJiajMoney.getText().toString())) {
                        addPice = 0;
                        etJiajMoney.setText((int) addPice + "");
                        etJiajMoney.setSelection(etJiajMoney.getText().toString().length());
                        getPrice();
                    }
                }
            }
        });

//

        //   lv_root.setListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_photo:// 上传图片
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                ActionSheet.createBuilder(this, getSupportFragmentManager())
                        .setCancelButtonTitle("取消")
                        .setOtherButtonTitles("拍照上传", "选择相册")
                        .setCancelableOnTouchOutside(true)
                        .setListener(new ActionSheet.ActionSheetListener() {
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                            }

                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int index) {

                                //      T.s(index + "");

                                switch (index) {
                                    case 0:

//                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                        startActivityForResult(intent, 1);

                                        TakePhoto();

                                        break;
                                    case 1:

                                        Intent picture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(picture, 3);

                                        break;
                                }
                            }
                        }).show();
                break;
            case R.id.btn_next2:// 立即 支付
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (!isVerOk()) {
                    return;
                }

                //未加 加价 0418

                //osb.setAddprice(Double.valueOf(tvJiajMoney.getText().toString())+"");

                osb.setPoints(DiKou + "");
                osb.setTaketime(taketime);

                BaseApplication.getInstance().setIsCreatOrder(true);

                if (TextUtils.isEmpty(BaseApplication.getInstance().onumber)) {
                    new PayUtil(SubmitOrderActivity.this, osb, osb.getOname()).payStart();
                    //   T.s("提交");
                } else {

                    //     T.s("支付");
                    new PayUtil(this, BaseApplication.getInstance().onumber, "商品名称", new OnBack() {
                        @Override
                        public void onBack() {
                            UIHelper.openHomeAvtivity(SubmitOrderActivity.this);
                        }
                    }).payStart();
                }

                break;

            case R.id.iv_jijian_se:// 选择 寄件人

                type = 1;
                UIHelper.openContacts(SubmitOrderActivity.this, type);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                break;
            case R.id.iv_shoujian_se:// 选择 收件人

                type = 2;
                UIHelper.openContacts(SubmitOrderActivity.this, type);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                break;

            case R.id.cb_mo:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                break;
//            case R.id.cb_mt:
//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                break;
            case R.id.cb_jc:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                break;


            case R.id.rb_bx:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                break;
            case R.id.rb_mt:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                break;
            case rb_jc:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                break;


            case R.id.tv_ordertype:

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                TimeDialogActivity.create(this).setListener(new TimeDialogActivity.BaseDialogListener() {
                    @Override
                    public void onClickBack(TimeDialogActivity actionSheet, long time) {

                        if (time == 0) {// 立即 取件
                            tvShowyuyue.setVisibility(View.GONE);
                            viewHeight.setVisibility(View.GONE);
                            taketime = "0";
                            tvOrdertype.setText("立即取件");
                            osb.setTaketype("1");
                            getPrice();
                            return;
                        }

                        viewHeight.setVisibility(View.VISIBLE);
                        tvShowyuyue.setVisibility(View.VISIBLE);
                        taketime = time + "";
                        osb.setTaketype("2");
                        tvOrdertype.setText(DateUtils.timestampToDate(time + ""));
                        getPrice();
                    }
                }).show();

                break;

            case R.id.tv_qujianadd:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                UIHelper.openAddressHistory2(this, peopleBean_dw, city, 0, 300);
                break;

            case R.id.tv_jijianadd:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                UIHelper.openAddressHistory2(this, peopleBean_dw, city, 1, 300);
                break;


            case R.id.cb_cb1:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (TextUtils.isEmpty(etBeizhu.getText().toString())) {
                    etBeizhu.append(cb_cb1.getText().toString());
                } else {
                    etBeizhu.append("," + cb_cb1.getText().toString());
                }
                break;

            case R.id.cb_cb2:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (TextUtils.isEmpty(etBeizhu.getText().toString())) {
                    etBeizhu.append(cb_cb1.getText().toString());
                } else {
                    etBeizhu.append("," + cb_cb2.getText().toString());
                }
                break;

            case R.id.cb_cb3:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (TextUtils.isEmpty(etBeizhu.getText().toString())) {
                    etBeizhu.append(cb_cb3.getText().toString());
                } else {
                    etBeizhu.append("," + cb_cb3.getText().toString());
                }
                break;

            case R.id.cb_cb4:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (TextUtils.isEmpty(etBeizhu.getText().toString())) {
                    etBeizhu.append(cb_cb4.getText().toString());
                } else {
                    etBeizhu.append("," + cb_cb4.getText().toString());
                }
                break;

            case R.id.cb_cb5:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (TextUtils.isEmpty(etBeizhu.getText().toString())) {
                    etBeizhu.append(cb_cb5.getText().toString());
                } else {
                    etBeizhu.append("," + cb_cb5.getText().toString());
                }
                break;


            case R.id.cb_cb6:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (TextUtils.isEmpty(etBeizhu.getText().toString())) {
                    etBeizhu.append(cb_cb6.getText().toString());
                } else {
                    etBeizhu.append("," + cb_cb6.getText().toString());
                }
                break;

            case R.id.cb_cb7:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (TextUtils.isEmpty(etBeizhu.getText().toString())) {
                    etBeizhu.append(cb_cb7.getText().toString());
                } else {
                    etBeizhu.append("," + cb_cb7.getText().toString());
                }
                break;

            case R.id.cb_cb8:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (TextUtils.isEmpty(etBeizhu.getText().toString())) {
                    etBeizhu.append(cb_cb8.getText().toString());
                } else {
                    etBeizhu.append("," + cb_cb8.getText().toString());
                }
                break;


            case R.id.iv_gjjian://减

                if (weight == 2) {
                    iv_gjjian.setBackgroundResource(R.mipmap.icon_jian);
                }

                if (weight > 1) {
                    weight--;
                    et_gongjin.setText(weight + "");
                    et_gongjin.setSelection(et_gongjin.getText().toString().length());

                }

                getPrice();
                break;
            case R.id.iv_gjjia://加

                weight++;
                if (weight > 1) {
                    iv_gjjian.setBackgroundResource(R.drawable.jianhao_btn_bg);
                }
                et_gongjin.setText(weight + "");
                et_gongjin.setSelection(et_gongjin.getText().toString().length());

                getPrice();
                break;

            case R.id.iv_jgjian://减

                if (addPice == 1) {
                    iv_jgjian.setBackgroundResource(R.mipmap.icon_jian);
                }

                if (addPice > 0) {
                    addPice--;
                    etJiajMoney.setText((int) addPice + "");
                    etJiajMoney.setSelection(etJiajMoney.getText().toString().length());
                }

                getPrice();
                break;
            case R.id.iv_jgjia://加

                addPice++;
                if (addPice > 0) {
                    iv_jgjian.setBackgroundResource(R.drawable.jianhao_btn_bg);
                }
                etJiajMoney.setText((int) addPice + "");
                etJiajMoney.setSelection(etJiajMoney.getText().toString().length());
                getPrice();
                break;


//            case R.id.et_zhuname:
//                etBeizhu.setCursorVisible(false);
//                break;
//
//            case R.id.et_beizhu:
//                etBeizhu.setCursorVisible(false);
//                break;
//
//
//            case R.id.et_quname:
//                etQuname.setCursorVisible(false);
//                break;
//
//
//            case R.id.et_quphone:
//                etQuphone.setCursorVisible(false);
//                break;
//
//            case R.id.et_shouname:
//                etShouname.setCursorVisible(false);
//                break;
//
//            case R.id.et_shouphone:
//                etShouphone.setCursorVisible(false);
//                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BaseApplication.getInstance().isWxPay()) {
            Intent intent = new Intent(this, MainFragment.class);
            intent.putExtra("data", "data");
            this.setResult(200, intent);
            this.finish();
        }
    }


    // 拍照
    private void photo() {

        // 判断是否挂载了SD卡
        String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/supai/";
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        }

        // 没有挂载SD卡，无法保存文件
        if (TextUtils.isEmpty(savePath)) {
            T.s("无法保存照片，请检查SD卡是否挂载");
            return;
        }

        String fileNamelage = "lagerImage.jpg";// 照片命名
        String fileNamesmall = "smallImage.jpg";// 照片命名
        File out = new File(savePath, fileNamelage);
        Uri uri = Uri.fromFile(out);

        theLarge = savePath + fileNamelage;// 该照片的绝对路径
        theThumbnail = savePath + fileNamesmall;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 2);
    }

    public void TakePhoto() {

        if (Build.VERSION.SDK_INT >= 23) {

            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    new PermissionsResultAction() {

                        @Override
                        public void onGranted() {

                            photo();
                        }

                        @Override
                        public void onDenied(String permission) {

                        }
                    });
        } else {
            photo();
        }

    }


    // 验证是否 可 提交 订单
    private boolean isVerOk() {

        if (list == null) {
            T.s(R.string.networf_errot);
            return false;
        }

        String orname = sp_type.getText().toString();
        //etZhuname.getText().toString();
        if (TextUtils.isEmpty(orname)) {

            T.s("请选择物品名称");
            return false;
        }
        osb.setOname(orname);

        //移除第一个字符
        String s = tv_service.getText().toString().substring(1, tv_service.getText().toString().length());

        osb.setAddprice(Double.valueOf(etJiajMoney.getText().toString())
                + Double.valueOf(s) + "");

        // osb.setMessage(etBeizhu.getText().toString());

        osb.setWeight(et_gongjin.getText().toString());

        int max = 0;
        if (cb_mo.isChecked()) {// 默认
            max = 0;
        }
        if (cb_hc.isChecked()) {//货车
            max = 1;
        }

        //假数据
        osb.setCpstyle(max);
        String jjOname = etQuname.getText().toString();
        if (TextUtils.isEmpty(jjOname)) {

            T.s("请输入寄件人的名称");
            return false;
        }
        osb.getPeopleBean_jj().setName(jjOname);

        String uphone = etQuphone.getText().toString();
        if (TextUtils.isEmpty(uphone)) {

            T.s("请输入寄件人的联系方式");
            return false;
        }
        osb.getPeopleBean_jj().setPhone(uphone);


        String houname = etShouname.getText().toString();
        if (TextUtils.isEmpty(houname)) {

            T.s("请输入收件人的名称");
            return false;
        }
        osb.getPeopleBean_sj().setName(houname);


        String shouphone = etShouphone.getText().toString();
        if (TextUtils.isEmpty(shouphone)) {

            T.s("请输入收件人的联系方式");
            return false;
        }

        osb.getPeopleBean_sj().setPhone(shouphone);

        String str = etBeizhu.getText().toString();

//        //判断最后一个
//        if (goodstypeposition != (list.size() - 1)) {
//            str = "[ " + list.get(goodstypeposition).getName() + " ]  " + str;
//        }
        osb.setMessage(str);

        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {
            if (data != null) {
                PeopleBean peopleBean = (PeopleBean) data.getSerializableExtra("peopleBean");
                if (peopleBean != null) {

                    if (type == 1) {
                        etQuphone.setText(peopleBean.getPhone());
                        etQuphone.setSelection(peopleBean.getPhone().length());
                        etQuname.setText(peopleBean.getName());
                        etQuname.setSelection(peopleBean.getName().length());
                    } else {
                        etShouphone.setText(peopleBean.getPhone());
                        etShouphone.setSelection(peopleBean.getPhone().length());
                        etShouname.setText(peopleBean.getName());
                        etShouname.setSelection(peopleBean.getName().length());
                    }
                }
            }
        }

//        if (requestCode == 1) {
//            Bundle extras = data.getExtras();
//            if (extras != null) {
//                Bitmap bmp = (Bitmap) extras.get("data");
//                ivPhoto.setImageBitmap(bmp);
//          //      ImageUtils.saveBitmap(bmp, "ivPhoto.jpg");
//                osb.setgImg(Environment.getExternalStorageDirectory() + "/supai/ivPhoto.jpg");
//            }
//        }
//
//        if (requestCode == 2) {
//            if (data != null) {
//                Uri selectedImage = data.getData();
//                String[] filePathColumns = {MediaStore.Images.Media.DATA};
//                Cursor c = this.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
//                c.moveToFirst();
//                int columnIndex = c.getColumnIndex(filePathColumns[0]);
//                String picturePath = c.getString(columnIndex);
//                Picasso.with(this).load(data.getData()).into(ivPhoto);
//        //        ImageUtils.saveBitmap(ImageUtils.compImage(BitmapFactory.decodeFile(picturePath)), "ivPhoto.jpg");
//                osb.setgImg(Environment.getExternalStorageDirectory() + "/supai/ivPhoto.jpg");
//            }
//        }

        switch (requestCode) {
            case 2: {
                if (!TextUtils.isEmpty(theLarge) && resultCode == RESULT_OK) {
                    try {

                        if (Build.VERSION.SDK_INT >= 23) {
                            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(SubmitOrderActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    new PermissionsResultAction() {
                                        @Override
                                        public void onGranted() {

                                            try {
                                                ImageUtils.createImageThumbnail(SubmitOrderActivity.this,
                                                        theLarge, theThumbnail, 200, 100);

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            if (!TextUtils.isEmpty(theThumbnail)) {
                                                ivPhoto.setImageBitmap(BitmapFactory.decodeFile(theThumbnail));
                                                osb.setgImg(theThumbnail);
                                            }
                                        }

                                        @Override
                                        public void onDenied(String permission) {

                                        }
                                    });
                        } else {
                            ImageUtils.createImageThumbnail(this,
                                    theLarge, theThumbnail, 200, 100);
                            if (!TextUtils.isEmpty(theThumbnail)) {
                                ivPhoto.setImageBitmap(BitmapFactory.decodeFile(theThumbnail));
                                osb.setgImg(theThumbnail);
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }
            break;

            case 3: {

                if (data == null) {
                    return;
                }

                if (data.getData() != null && resultCode == RESULT_OK) {
                    final String path = ImageUtils.getAbsoluteImagePath(this, data.getData());
                    if (!TextUtils.isEmpty(path)) {
                        try {
                            if (Build.VERSION.SDK_INT >= 23) {
                                PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(SubmitOrderActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        new PermissionsResultAction() {
                                            @Override
                                            public void onGranted() {
                                                String savePath = "";
                                                String storageState = Environment.getExternalStorageState();
                                                if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                                                    savePath = Environment.getExternalStorageDirectory()
                                                            .getAbsolutePath() + "/supai/";
                                                    File savedir = new File(savePath);
                                                    if (!savedir.exists()) {
                                                        savedir.mkdirs();
                                                    }
                                                }

                                                // 没有挂载SD卡，无法保存文件
                                                if (TextUtils.isEmpty(savePath)) {
                                                    T.s("无法保存照片，请检查SD卡是否挂载");
                                                    return;
                                                }

                                                String fileNamesmall = "smallHeader.jpg";// 照片命名
                                                theThumbnail = savePath + fileNamesmall;

                                                try {
                                                    ImageUtils.createImageThumbnail(SubmitOrderActivity.this,
                                                            path, theThumbnail, 200, 100);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                if (!TextUtils.isEmpty(theThumbnail)) {
                                                    ivPhoto.setImageBitmap(BitmapFactory.decodeFile(theThumbnail));
                                                    osb.setgImg(theThumbnail);
                                                }
                                            }

                                            @Override
                                            public void onDenied(String permission) {

                                            }
                                        });
                            } else {
                                String savePath = "";
                                String storageState = Environment.getExternalStorageState();
                                if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                                    savePath = Environment.getExternalStorageDirectory()
                                            .getAbsolutePath() + "/supai/";
                                    File savedir = new File(savePath);
                                    if (!savedir.exists()) {
                                        savedir.mkdirs();
                                    }
                                }

                                // 没有挂载SD卡，无法保存文件
                                if (TextUtils.isEmpty(savePath)) {
                                    T.s("无法保存照片，请检查SD卡是否挂载");
                                    return;
                                }

                                String fileNamesmall = "smallHeader.jpg";// 照片命名
                                theThumbnail = savePath + fileNamesmall;

                                ImageUtils.createImageThumbnail(this,
                                        path, theThumbnail, 200, 100);
                                if (!TextUtils.isEmpty(theThumbnail)) {
                                    ivPhoto.setImageBitmap(BitmapFactory.decodeFile(theThumbnail));
                                    osb.setgImg(theThumbnail);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            break;

            case 300: {

                if (data != null) {

                    PeopleBean peopleBean = (PeopleBean) data.getSerializableExtra("peopleBean");

                    if (peopleBean != null) {
                        int type = data.getIntExtra("type", 0);

                        String add = peopleBean.getAdd();

                        if (!TextUtils.isEmpty(add)) {// 确定的 地址

                            if (type == 0) {

                                tvQujianadd.setText(add);
                                peopleBean_jj.setLat(peopleBean.getLat());
                                peopleBean_jj.setLng(peopleBean.getLng());
                                peopleBean_jj.setAdd(add);

                                if (!TextUtils.isEmpty(peopleBean.getName())) {
                                    etQuname.setText(peopleBean.getName());
                                    peopleBean_jj.setName(peopleBean.getName());
                                }
                                if (!TextUtils.isEmpty(peopleBean.getPhone())) {
                                    etQuphone.setText(peopleBean.getPhone());
                                    peopleBean_jj.setPhone(peopleBean.getPhone());
                                }
                                setDistance();
                            } else if (type == 1) {

                                tvJijianadd.setText(add);
                                peopleBean_sj.setLat(peopleBean.getLat());
                                peopleBean_sj.setLng(peopleBean.getLng());
                                peopleBean_sj.setAdd(add);

                                if (!TextUtils.isEmpty(peopleBean.getName())) {
                                    etShouname.setText(peopleBean.getName());
                                    peopleBean_sj.setName(peopleBean.getName());
                                }
                                if (!TextUtils.isEmpty(peopleBean.getPhone())) {
                                    etShouphone.setText(peopleBean.getPhone());
                                    peopleBean_sj.setPhone(peopleBean.getPhone());
                                }
                                setDistance();
                            }

                        }
                    }
                    String back = data.getStringExtra("data");
                    if (!TextUtils.isEmpty(back)) {// 支付 返回

                        L.d("你好 支付返回了");
                    }
                }
            }
            break;
        }
    }


    //设置距离
    public void setDistance() {
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

        final RouteSearch routeSearch = new RouteSearch(this);
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
                float distan = 0;
                for (int i = 0; i < totalLine; i++) {
                    L.e("第" + i + "段距离为：" + driveRouteResult.getPaths().get(i).getDistance() + "m");
                    distan += driveRouteResult.getPaths().get(i).getDistance();
                    float tem = driveRouteResult.getPaths().get(i).getDistance();
                    min = Math.min(min, tem);
                }
                distan = min;
                if (distan == 0.0) {
                    distan = 1;
                }
                double dou = distan / 1000 + 1;//千米
                int jul = (int) (dou);// 默认 单位 为 分
                if (jul < 1) {
                    distan = 1;
                }
                distan = jul;
                L.e("距离-------------" + distan + "km");
                distance = (int) distan;
                L.e(distance + "=================");
                tv_gongli.setText(distance + "");
                getPrice();
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }
        });

        routeSearch.calculateDriveRouteAsyn(query);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.getInstance().onumber = "";
        FileUtils.deleteFile(Environment.getExternalStorageDirectory() + "/supai/ivPhoto.jpg");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.cb_mo: {
                if (isChecked) {
                    cb_mo.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
                } else
                    cb_mo.setTextColor(Color.argb(0xFF, 0x77, 0x77, 0x77));
            }
            break;

//            case R.id.cb_mt: {
//                if (isChecked) {
//                    paisongtype = 0;
//                    cbMt.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
//                } else
//                    cbMt.setTextColor(Color.argb(0xFF, 0x77, 0x77, 0x77));
//            }
//            break;

            case R.id.cb_hc: {
                if (isChecked) {
                    cb_hc.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
                } else
                    cb_hc.setTextColor(Color.argb(0xFF, 0x77, 0x77, 0x77));
            }
            break;

            case rb_jc: {
                if (isChecked) {
                    serviseType += 1;
                    rbJc.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
                } else {
                    serviseType -= 1;
                    rbJc.setTextColor(Color.argb(0xFF, 0x77, 0x77, 0x77));
                }
                sumPrice();
            }
            break;

            case R.id.rb_mt: {
                if (isChecked) {
                    serviseType += 2;
                    rbMt.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
                } else {
                    serviseType -= 2;
                    rbMt.setTextColor(Color.argb(0xFF, 0x77, 0x77, 0x77));
                }
                sumPrice();
            }
            break;

            case R.id.rb_bx: {
                if (isChecked) {
                    serviseType += 4;
                    rbBx.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
                } else {
                    serviseType -= 4;
                    rbBx.setTextColor(Color.argb(0xFF, 0x77, 0x77, 0x77));
                }
                sumPrice();
            }
            break;
        }
    }

    //求和价格
    private void sumPrice() {

        DecimalFormat df = new DecimalFormat("############0.00");
        if (night.equals("0")) {
            rl_yejian.setVisibility(View.GONE);
        } else {
            rl_yejian.setVisibility(View.VISIBLE);

            //夜间家
            tv_yejianjia.setText("￥" + df.format(Double.valueOf(night)));
        }

        String s = et_gongjin.getText().toString();

        if (Integer.valueOf(s) < 1) {
            et_gongjin.setText("1");
            et_gongjin.setSelection(et_gongjin.getText().toString().length());
        }

        //加价
        tv_jiajian.setText("￥" + (int) addPice + "");
        //里程家
        tv_qijia.setText(yuanjia + "元");

        switch (serviseType) {
            case 0:
                servise = 0;
                break;
            case 1:
                servise = yuanjia * 0.3;
                break;
            case 2:
                servise = 10;
                break;
            case 3:
                servise = yuanjia * 0.3 + 10;
                break;
            case 4:
                servise = 5;
                break;
            case 5:
                servise = yuanjia * 0.3 + 5;
                break;
            case 6:
                servise = 5 + 8;
                break;
            case 7:
                servise = yuanjia * 0.3 + 8 + 5;
                break;
        }

        if (points.equals("0")) {
            ll_jifen.setVisibility(View.GONE);
        } else {
            ll_jifen.setVisibility(View.VISIBLE);
        }

        tv_service.setText("￥" + df.format(servise + goodstype + paisongtype) + "");

        osb.setDistance(distance + "");
        osb.setTaketime(taketime);
        osb.setNightnight(night);

        osb.setAddprice(addPice + servise + goodstype + paisongtype + "");

        double pic = Double.valueOf(night) + yuanjia - percent * DiKou + addPice + servise + goodstype + paisongtype;
        //总价
        tv_zongjia.setText("￥" + df.format(pic));
        tv_zongjia1.setText("￥" + df.format(pic));
    }

    // 计算价格
    private void getPrice() {

        //判断 是否 开始计算价格
        ApiHttpClient.cancelRequests();

        BigDecimal big = new BigDecimal(distance);

        double f1 = big.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();

        L.d("距离---》" + f1 + "----重量" + weight + "----预约时间" + taketime);

        //距离
        OrderApi.sendordergetprice(this, weight + "", f1 + "", taketime + "", new RequestBasetListener() {
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
//                    if (redbagBean != null) {
//
//                        isSet = true;
//                        double dou0 = Double.parseDouble(night);
//                        double dou1 = Double.parseDouble(redbagBean.getMoney());
//                        double dou2 = Double.parseDouble(price);
//                        double dou3 = dou2 - dou1 + dou0;
//                        price = dou3 + "";
//                    }
                    //  tvShowmo.setText(price + "元");
                    btnNext.setEnabled(true);

                    //清零服务费
                    servise = 0;
                    L.e(price + "----------------");

                    //包含夜间家 要减去
                    yuanjia = Double.valueOf(price) - Double.valueOf(night);

                    sumPrice();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode) {
                //  tvShowmo.setText("网络错误");
                btnNext.setEnabled(false);
                Log.d(TAG, "onFailure() called with: " + "statusCode = [" + statusCode + "]");
                // handler.obtainMessage(0).sendToTarget();
            }

            @Override
            public void onSendError(int statusCode, String message) {
                Log.d(TAG, "onSendError() called with: " + "statusCode = [" + statusCode + "], message = [" + message + "]");
                //  tvShowmo.setText("网络错误");
                btnNext.setEnabled(false);
                //   handler.obtainMessage(0).sendToTarget();
            }
        });
    }
}
