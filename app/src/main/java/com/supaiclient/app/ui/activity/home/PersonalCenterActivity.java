package com.supaiclient.app.ui.activity.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.baoyz.actionsheet.ActionSheet;

import com.supaiclient.app.BaseApplication;
import com.supaiclient.app.R;
import com.supaiclient.app.api.UserApi;
import com.supaiclient.app.bean.GetinfoBean;
import com.supaiclient.app.db.PropertyUtil;
import com.supaiclient.app.interf.OnLoginBackLinstener;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.model.UserModel;
import com.supaiclient.app.ui.activity.integral.IntegralActivity;
import com.supaiclient.app.ui.activity.user.AboutActivity;
import com.supaiclient.app.ui.activity.user.OpinionActivity;
import com.supaiclient.app.ui.activity.user.SenderActivity;
import com.supaiclient.app.ui.base.BaseActivity;
import com.supaiclient.app.util.FileUtils;
import com.supaiclient.app.util.ImageUtils;
import com.supaiclient.app.util.JSonUtils;
import com.supaiclient.app.util.T;
import com.supaiclient.app.util.UIHelper;
import com.supaiclient.app.widget.CircleImageView;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapCallBack;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/23.
 * 个人中心
 */
public class PersonalCenterActivity extends BaseActivity implements View.OnClickListener {


    private static final String TAG = "PersonalCenterActivity";

    TextView tuichu;

    private TextView tv_login;// 登录 按钮

    private TextView tv_login_name;//登录 用户名
    private TextView tv_qian,
            tv_lishidingdan,
            tv_daiqudan,
            tv_kuaijizhong;

    private CircleImageView draweeview;
    private TextView tv_jifen;

    private GetinfoBean getinfoBean;

    private String theLarge = "";
    private String theThumbnail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  Fresco.initialize(this);
        setContentView(R.layout.activity_personal_center);

        Log.d(TAG, "onCreate() called with: " + "savedInstanceState = [" + savedInstanceState + "]");

        setActionBarTitle(R.string.user);

        ButterKnife.bind(this);

        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_login.setOnClickListener(this);

        findViewById(R.id.nav_camara).setOnClickListener(this);
        findViewById(R.id.nav_camara2).setOnClickListener(this);
        findViewById(R.id.nav_fapiao).setOnClickListener(this);
        findViewById(R.id.nav_gallery).setOnClickListener(this);
        findViewById(R.id.nav_manage2).setOnClickListener(this);
        findViewById(R.id.nav_slideshow).setOnClickListener(this);
        findViewById(R.id.ll_integral).setOnClickListener(this);

        tuichu = (TextView) findViewById(R.id.tuichu);
        findViewById(R.id.dd).setOnClickListener(this);
        findViewById(R.id.about).setOnClickListener(this);
        findViewById(R.id.tuichu).setOnClickListener(this);

        draweeview = (CircleImageView) findViewById(R.id.draweeview);
        draweeview.setOnClickListener(this);

        tv_jifen = (TextView) findViewById(R.id.tv_jifen);

        tv_qian = (TextView) findViewById(R.id.tv_qian);
        tv_lishidingdan = (TextView) findViewById(R.id.tv_lishidingdan);
        tv_daiqudan = (TextView) findViewById(R.id.tv_daiqudan);
        tv_kuaijizhong = (TextView) findViewById(R.id.tv_kuaijizhong);

        tv_login_name = (TextView) findViewById(R.id.tv_login_name);

        getUserInfo();

    }


    private void getUserInfo() {

        showWaitDialog("请稍后...");
        UserApi.userGetinfo(PersonalCenterActivity.this, new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

                getinfoBean = JSonUtils.toBean(GetinfoBean.class, responseStr);

                KJBitmap kjBitmap = new KJBitmap();
                kjBitmap.display(draweeview, getinfoBean.getGuphoto(), new BitmapCallBack() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        super.onSuccess(bitmap);

                        tv_login_name.setText(getinfoBean.getGuphone());
                        tv_jifen.setText(getinfoBean.getGupoints());
                        tv_qian.setText(getinfoBean.getGutotalmoney());
                        tv_lishidingdan.setText(getinfoBean.getGutotalorder());

                        tv_daiqudan.setText(getinfoBean.getDqj());
                        tv_kuaijizhong.setText(getinfoBean.getDsh());

                        tv_login.setVisibility(View.GONE);
                        tv_login_name.setVisibility(View.VISIBLE);
                        tuichu.setVisibility(View.VISIBLE);
                        draweeview.setEnabled(true);

                        hideWaitDialog();
                    }
                });

                //draweeview.setImageURI(Uri.parse(getinfoBean.getGuphoto()));
                //Picasso.with(MainActivity.this).load(getinfoBean.getGuphoto()).error(R.mipmap.icon_defut).into(draweeview);
            }

            @Override
            public void onFailure(int statusCode) {
                hideWaitDialog();
            }

            @Override
            public void onSendError(int statusCode, String message) {
                hideWaitDialog();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() ");
        getUserInfo();
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (v.getId()) {
            case R.id.tv_login:// 跳转 至 登录
                UIHelper.openLoginAvtivity(this);
                break;
        }

        if (id == R.id.about) {//关于

            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.dd) {// 意见反馈
            startActivity(new Intent(this, OpinionActivity.class));
        } else if (id == R.id.nav_gallery) {

            //判断是否登录
            boolean isLogin = new UserModel(PersonalCenterActivity.this).isAutoLogin();
            if (!isLogin) {

                UIHelper.openLoginAvtivity(this);
            } else {
                startActivity(new Intent(this, SenderActivity.class));
            }

        } else if (id == R.id.ll_integral) {
            //我的积分
            startActivity(new Intent(this, IntegralActivity.class));

        } else if (id == R.id.nav_camara2) {// 常见问题
            UIHelper.OPENCommonQuestion(this);
        } else if (id == R.id.nav_camara) {//我的 订单

            boolean isLogin = new UserModel(PersonalCenterActivity.this).isAutoLogin();
            if (isLogin) {

                UIHelper.OPENCGOODS(this);
            } else {

                UIHelper.openKhLoginAvtivity(this, new OnLoginBackLinstener() {
                    @Override
                    public void onBack() {
                        UIHelper.OPENCGOODS(PersonalCenterActivity.this);

                    }
                });
            }

//            http://120.27.150.115/spcr/public/index.php/price/index

        } else if (id == R.id.nav_manage2) {//价格表

            UIHelper.openJiage(PersonalCenterActivity.this);

        } else if (id == R.id.nav_slideshow) {//优惠券


            boolean isLogin = new UserModel(PersonalCenterActivity.this).isAutoLogin();
            if (isLogin) {

                UIHelper.youhui(this);
            } else {

                UIHelper.openKhLoginAvtivity(this, new OnLoginBackLinstener() {
                    @Override
                    public void onBack() {

                        UIHelper.youhui(PersonalCenterActivity.this);

                    }
                });
            }
        } else if (id == R.id.nav_fapiao) {     //发票 申请

            boolean isLogin = new UserModel(PersonalCenterActivity.this).isAutoLogin();
            if (isLogin) {

                String youhui = "0";
                if (getinfoBean != null) {
                    youhui = getinfoBean.getGutotalmoney();
                }

                UIHelper.fapiao(this, youhui);
            } else {

                UIHelper.openKhLoginAvtivity(this, new OnLoginBackLinstener() {
                    @Override
                    public void onBack() {

                        String youhui = "0";
                        if (getinfoBean != null) {
                            youhui = getinfoBean.getGutotalmoney();
                        }

                        UIHelper.fapiao(PersonalCenterActivity.this, youhui);

                    }
                });
            }
        } else if (id == R.id.tuichu) {
            AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(this);
            alertbBuilder.setTitle("提示").setMessage("你确定要退出登录吗？");
            alertbBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    PropertyUtil.setUinfo("");
//                    tv_login.setVisibility(View.VISIBLE);
//                    tv_login_name.setVisibility(View.GONE);
//
//                    draweeview.setImageResource(R.mipmap.icon_defut);
//                    tv_jifen.setText("0");
//                    tv_qian.setText("0");
//                    tv_lishidingdan.setText("0");
//                    tv_daiqudan.setText("0");
//                    tv_kuaijizhong.setText("0");
                    sendBroadcast(new Intent(MainActivity.ACTION_CG).putExtra("STA", false).putExtra("NUM", 0));
                    PersonalCenterActivity.this.finish();

                }
            });
            alertbBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).create();
            alertbBuilder.show();
        } else if (id == R.id.draweeview) {

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

                            switch (index) {
                                case 0:

                                    PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(PersonalCenterActivity.this, new String[]{Manifest.permission.CAMERA}, new PermissionsResultAction() {

                                        @Override
                                        public void onGranted() {
                                            photo();
                                        }

                                        @Override
                                        public void onDenied(String permission) {
                                            T.s("未获得相机权限");
                                            return;
                                        }
                                    });


                                    break;
                                case 1:
                                    Intent picture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(picture, 2);

//                                    Intent intent = new Intent(Intent.ACTION_PICK);
//                                    intent.setType("image/*");
//                                    startActivityForResult(intent, 2);

                                    break;
                            }
                        }
                    }).show();
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

        String fileNamelage = "lagerHeader.jpg";// 照片命名
        String fileNamesmall = "smallHeader.jpg";// 照片命名
        File out = new File(savePath, fileNamelage);
        final Uri uri = Uri.fromFile(out);

        theLarge = savePath + fileNamelage;// 该照片的绝对路径
        theThumbnail = savePath + fileNamesmall;

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= 23) {
            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(PersonalCenterActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionsResultAction() {
                        @Override
                        public void onGranted() {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        }

                        @Override
                        public void onDenied(String permission) {

                        }
                    });
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case 1: {
                if (!TextUtils.isEmpty(theLarge) && resultCode == RESULT_OK) {
                    try {
                        if (Build.VERSION.SDK_INT >= 23) {
                            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(PersonalCenterActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                    new PermissionsResultAction() {
                                        @Override
                                        public void onGranted() {
                                            try {
                                                ImageUtils.createImageThumbnail(BaseApplication.getContext(),
                                                        theLarge, theThumbnail, 200, 100);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            if (!TextUtils.isEmpty(theThumbnail))

                                            {
                                                //     draweeview.setImageBitmap(BitmapFactory.decodeFile(theThumbnail));
                                                upHeader(theThumbnail);
                                                //    FileUtils.deleteFile(theLarge);
                                            }
                                        }

                                        @Override
                                        public void onDenied(String permission) {
                                            T.s("未获取到读写SD卡权限");
                                        }
                                    });
                        } else {
                            ImageUtils.createImageThumbnail(this,
                                    theLarge, theThumbnail, 200, 100);
                            if (!TextUtils.isEmpty(theThumbnail))

                            {
                                //    draweeview.setImageBitmap(BitmapFactory.decodeFile(theThumbnail));
                                upHeader(theThumbnail);
                                //    FileUtils.deleteFile(theLarge);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            break;

            case 2: {
                if (data == null) {
                    return;
                }
                if (data.getData() != null && resultCode == RESULT_OK) {
                    final String path = ImageUtils.getImagePath(data.getData(), this);

                    if (!TextUtils.isEmpty(path)) {
                        try {

                            if (Build.VERSION.SDK_INT >= 23) {
                                PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(PersonalCenterActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
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
                                                    ImageUtils.createImageThumbnail(BaseApplication.getContext(),
                                                            path, theThumbnail, 200, 100);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                                if (!TextUtils.isEmpty(theThumbnail)) {
                                                    //            draweeview.setImageBitmap(BitmapFactory.decodeFile(theThumbnail));

                                                    upHeader(theThumbnail);
                                                }
                                            }

                                            @Override
                                            public void onDenied(String permission) {
                                                T.s("未获取到读写SD卡权限");
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
                                    //    draweeview.setImageBitmap(BitmapFactory.decodeFile(theThumbnail));

                                    upHeader(theThumbnail);
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

    private void upHeader(final String path) {

        UserApi.upHeader(this, new File(path), new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

                T.s("图片上传成功");
                getUserInfo();
                FileUtils.deleteFile(path);
            }

            @Override
            public void onFailure(int statusCode) {
                T.s("图片上传失败");
            }

            @Override
            public void onSendError(int statusCode, String message) {
                T.s("图片上传失败");
            }
        });
    }
}
