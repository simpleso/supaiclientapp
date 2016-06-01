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
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.baoyz.actionsheet.ActionSheet;
import com.supaiclient.app.BaseApplication;
import com.supaiclient.app.R;
import com.supaiclient.app.api.OrderApi;
import com.supaiclient.app.bean.OrderSubmitBean;
import com.supaiclient.app.bean.PeopleBean;
import com.supaiclient.app.interf.OnBack;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.base.BaseActivity;
import com.supaiclient.app.ui.fragment.MainFragment;
import com.supaiclient.app.util.DateUtils;
import com.supaiclient.app.util.FileUtils;
import com.supaiclient.app.util.ImageUtils;
import com.supaiclient.app.util.PayUtil;
import com.supaiclient.app.util.T;
import com.supaiclient.app.util.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Target;
import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/12/28.
 * 提交订单
 */
public class SubmitOrderActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    @Bind(R.id.cb_jc)
    CheckBox cbJc;
    @Bind(R.id.cb_mt)
    CheckBox cbMt;
    @Bind(R.id.cb_bx)
    CheckBox cbBx;
    @Bind(R.id.seekBar)
    SeekBar seekBar;
    @Bind(R.id.tv_jiajMoney)
    TextView tvJiajMoney;
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
    @Bind(R.id.tv_ordertype)
    TextView tvOrdertype;
    @Bind(R.id.et_ordername)
    TextView etOrdername;
    @Bind(R.id.et_beizhu)
    EditText etBeizhu;
    @Bind(R.id.et_zhuname)
    EditText etZhuname;
    @Bind(R.id.tv_jiajian)
    TextView tv_jiajian;
    @Bind(R.id.tv_qijia)
    TextView tv_qijia;
    @Bind(R.id.tv_gongjin)
    TextView tv_gongjin;
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
    private Target target;
    private double yuanjia;// 原来的  价格
    private OrderSubmitBean osb;
    private int type;// 1: 寄件人 选择 联系人  2：收件人 选择 联系人
    private double ou = 0;
    private String theLarge = "";
    private String theThumbnail = "";
    private double DiKou = 0;

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

        init();
    }

    private void init() {

        if (osb != null) {

            PeopleBean peopleBean_jj = osb.getPeopleBean_jj();

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

            PeopleBean peopleBean_sj = osb.getPeopleBean_sj();

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

            tv_gongjin.setText(osb.getWeight());
            tv_gongli.setText(osb.getDistance());

            String taketype = osb.getTaketype();
            if (taketype.equals("1")) {
                tvOrdertype.setText("立即取件");
            } else {
                tvOrdertype.setText(DateUtils.timestampToDate(osb.getTaketime()));
            }

            yuanjia = Double.parseDouble(osb.getAddprice());

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

        }
        seekBar.setMax(30);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                tvJiajMoney.setText(progress + "");
                tv_jiajian.setText("￥" + progress + "");
                double ou = yuanjia + Double.parseDouble(progress + "") - DiKou * osb.getPercent();
                DecimalFormat df = new DecimalFormat("############0.00");
                tv_zongjia.setText("￥" + df.format(ou));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (osb.getPoints().equals("0")) {

            OrderApi.sendordergetprice(this, osb.getWeight(), osb.getDistance(), osb.getTaketime(), new RequestBasetListener() {
                @Override
                public void onSuccess(String responseStr) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseStr);
                        if (jsonObject.has("points"))
                            osb.setPoints(jsonObject.getString("points"));
                        mSeekBar1.setMax(Integer.valueOf(osb.getPoints()));
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
                double ou = Double.valueOf(tvJiajMoney.getText().toString()) + yuanjia - Double.parseDouble((progress * osb.getPercent()) + "");
                tv_zongjia.setText("￥" + df.format(ou));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        cbJc.setOnCheckedChangeListener(this);
        cbMt.setOnCheckedChangeListener(this);
        cbBx.setOnCheckedChangeListener(this);

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

        cbBx.setOnClickListener(this);
        cbJc.setOnClickListener(this);
        cbMt.setOnClickListener(this);

//        etShouphone.setOnClickListener(this);
//        etOrdername.setOnClickListener(this);
//        etQuname.setOnClickListener(this);
//        etShouname.setOnClickListener(this);
//        etBeizhu.setOnClickListener(this);
//        etQuphone.setOnClickListener(this);
//        etZhuname.setOnClickListener(this);

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

                //      osb.setAddprice(Double.valueOf(tvJiajMoney.getText().toString())+"");

                osb.setPoints(DiKou + "");

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

            case R.id.cb_bx:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                break;
            case R.id.cb_mt:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                break;
            case R.id.cb_jc:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

        String orname = etZhuname.getText().toString();
        if (TextUtils.isEmpty(orname)) {

            T.s("请输入物品名称");
            return false;
        }
        osb.setOname(orname);
        osb.setAddprice(tvJiajMoney.getText().toString());
        osb.setMessage(etBeizhu.getText().toString());

        int max = 0;
        if (cbJc.isChecked()) {// 机车
            max = max + 4;
        }
        if (cbBx.isChecked()) {//步行
            max = max + 1;
        }
        if (cbMt.isChecked()) {//// 摩托
            max = max + 2;
        }

        if (max == 0) {
            max = 7;
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

        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
        }
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
            case R.id.cb_jc: {
                if (isChecked)
                    cbJc.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
                else
                    cbJc.setTextColor(Color.argb(0xFF, 0x77, 0x77, 0x77));
            }
            break;

            case R.id.cb_mt: {
                if (isChecked)
                    cbMt.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
                else
                    cbMt.setTextColor(Color.argb(0xFF, 0x77, 0x77, 0x77));
            }
            break;

            case R.id.cb_bx: {
                if (isChecked)
                    cbBx.setTextColor(Color.argb(0xFF, 0xFD, 0xC1, 0x86));
                else
                    cbBx.setTextColor(Color.argb(0xFF, 0x77, 0x77, 0x77));
            }
            break;
        }
    }
}
