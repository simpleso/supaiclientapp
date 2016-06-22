package com.supaiclient.app.ui.activity.home;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.supaiclient.app.BaseApplication;
import com.supaiclient.app.R;
import com.supaiclient.app.api.OrderApi;
import com.supaiclient.app.api.UserApi;
import com.supaiclient.app.bean.AdvertBean;
import com.supaiclient.app.bean.FindspmanBean;
import com.supaiclient.app.bean.MessageBean;
import com.supaiclient.app.interf.OnLoginBackLinstener;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.model.UserModel;
import com.supaiclient.app.ui.activity.web.GeneralWebView;
import com.supaiclient.app.ui.fragment.Main2Fragment;
import com.supaiclient.app.ui.fragment.MainFragment;
import com.supaiclient.app.util.AppConfig;
import com.supaiclient.app.util.AppManager;
import com.supaiclient.app.util.JSonUtils;
import com.supaiclient.app.util.UIHelper;
import com.supaiclient.app.util.UpdataManeger;
import com.supaiclient.app.widget.SegmentControl;
import com.umeng.update.UmengUpdateAgent;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.bitmap.BitmapCallBack;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/12/23.
 * 首页
 */
public class MainActivity extends FragmentActivity {


    public static final String ACTION_MEG = "com.supaiclient.app.message";
    public static final String ACTION_AD = "com.supaiclient.app.ad";
    public static final String ACTION_CG = "com.supaiclient.app.cg";
    public static final String ACTION_CGSTA = "com.supaiclient.app.cgsta";
    private static final String TAG = "MainActivity";

    /*  @Bind(R.id.nav_view)
      NavigationView navView;*/
//    @Bind(R.id.rl_top)
//    RelativeLayout rlTop;
//    @Bind(R.id.drawer_layout)
//    DrawerLayout drawerLayout;
//    @Bind(R.id.content)
//    FrameLayout content;
//    @Bind(R.id.realtabcontent)
//    FrameLayout realtabcontent;
    private static Fragment[] mFragments;
    @Bind(R.id.iv_main)
    ImageView ivMain;
    @Bind(R.id.iv_main2)
    ImageView ivMain2;
    @Bind(R.id.tv_small_red)
    TextView tv_small_red;
    @Bind(R.id.root_layout)
    LinearLayout root_layout;
    @Bind(R.id.segment_control)
    SegmentControl segmentControl;
    @Bind(R.id.tv_noti)
    TextView tv_noti;
    long exitTime = 0;
    //  TextView tuichu;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;


    //    private TextView tv_login;// 登录 按钮
//
//    private TextView tv_login_name;//登录 用户名
//    private TextView tv_qian,
//            tv_lishidingdan,
//            tv_daiqudan,
//            tv_kuaijizhong;
//
//    private SimpleDraweeView draweeview;
//    private TextView tv_jifen;
//
//    private GetinfoBean getinfoBean;
    private PopupWindow popupWindow;
    /* private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;


     private String theLarge = "";
     private String theThumbnail = "";*/
    private AdvertBean advertBean;

    private BroadcastReceiver mMSGReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ACTION_MEG)) {

                if (intent.getBooleanExtra("XX", false)) {
                    tv_small_red.setVisibility(View.VISIBLE);
                } else {
                    tv_small_red.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    private BroadcastReceiver mADReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ACTION_AD)) {
                advertBean = (AdvertBean) intent.getSerializableExtra("advertBean");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (advertBean != null) {
                            BaseApplication.set("bphoto", advertBean.getBphoto());
                            BaseApplication.set("burl", advertBean.getBurl());
                        }
                    }
                }, 0);
            }
        }
    };


    private BroadcastReceiver mCGReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ACTION_CG)) {
                Boolean b = intent.getBooleanExtra("STA", false);
                if (b) {
                    //   L.e("收到消息,显示");
                    tv_noti.setVisibility(View.VISIBLE);
                    int num = intent.getIntExtra("NUM", 0);
                    tv_noti.setText("" + (num > 9 ? "9+" : num));
                } else {
                    //    L.e("收到消息,隐藏");
                    tv_noti.setVisibility(View.GONE);
                }
            }
        }
    };

    private BroadcastReceiver mCGStaReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ACTION_CGSTA)) {

                getfindman();
                //L.e("收到推送的CG");
            }
        }
    };

//    /***
//     * 获取PopupWindow实例
//     */
//    private void getPopupWindow(String bname) {
//        if (null != popupWindow) {
//            popupWindow.dismiss();
//            return;
//        } else {
//            initPopuptWindow(bname);
//        }
//    }
//
//    /**
//     * 创建PopupWindow
//     */
//    protected void initPopuptWindow(final String bname) {
//
//        // TODO Auto-generated method stub
//        final View popupWindow_view = getLayoutInflater().inflate(R.layout.pop_ad_bomb_screen, null,
//                false);
//
//        popupWindow = new PopupWindow(popupWindow_view, LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT, true);
//
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//
//        ImageView imageView = (ImageView) popupWindow_view.findViewById(R.id.img_ad);
//
//        if (!TextUtils.isEmpty(bname)) {
//            KJBitmap kjBitmap = new KJBitmap();
//            kjBitmap.display(imageView, bname, new BitmapCallBack() {
//                @Override
//                public void onFinish() {
//                    super.onFinish();
//                    popupWindow.showAtLocation(root_layout, Gravity.CENTER, 0, 0);
//                }
//            });
//        }
//        final String burl = BaseApplication.get("burl", "");
//        if (!TextUtils.isEmpty(burl)) {
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    popupWindow.dismiss();
//                    startActivity(new Intent(MainActivity.this, GeneralWebView.class).putExtra("url", burl));
//                }
//            });
//        }
//
//        TextView textView = (TextView) popupWindow_view.findViewById(R.id.tv_cancel);
//
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//                BaseApplication.set("bphoto", "");
//                BaseApplication.set("burl", "");
//            }
//        });
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //L.e("onCreate");

        //Fresco.initialize(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //   startService(new Intent(this, Foreground.class));

        UmengUpdateAgent.update(this);
        UpdataManeger.getInstens().update(this);

        setFragmentIndicator();

        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(ACTION_MEG);
        registerReceiver(mMSGReceiver, intentFilter1);

        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(ACTION_AD);
        registerReceiver(mADReceiver, intentFilter2);

        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction(ACTION_CG);
        registerReceiver(mCGReceiver, intentFilter3);

        IntentFilter intentFilter4 = new IntentFilter();
        intentFilter4.addAction(ACTION_CGSTA);
        registerReceiver(mCGStaReceiver, intentFilter4);

        //  NavigationView view = (NavigationView) findViewById(R.id.nav_view);
        //  LinearLayout root  = (LinearLayout) view.getParent();
        //  view.setNavigationItemSelectedListener(this);
        /* View headerView = findViewById(R.id.head);

        tv_login = (TextView) headerView.findViewById(R.id.tv_login);
        tv_login.setOnClickListener(this);

        findViewById(R.id.nav_camara).setOnClickListener(this);
        findViewById(R.id.nav_camara2).setOnClickListener(this);
        findViewById(R.id.nav_fapiao).setOnClickListener(this);
        findViewById(R.id.nav_gallery).setOnClickListener(this);
        findViewById(R.id.nav_manage2).setOnClickListener(this);
        findViewById(R.id.nav_slideshow).setOnClickListener(this);
        tuichu = (TextView) findViewById(R.id.tuichu);
        findViewById(R.id.dd).setOnClickListener(this);
        findViewById(R.id.about).setOnClickListener(this);
        findViewById(R.id.tuichu).setOnClickListener(this);

        draweeview = (SimpleDraweeView) headerView.findViewById(R.id.draweeview);
        draweeview.setOnClickListener(this);

        tv_jifen = (TextView) headerView.findViewById(R.id.tv_jifen);

        tv_qian = (TextView) headerView.findViewById(R.id.tv_qian);
        tv_lishidingdan = (TextView) headerView.findViewById(R.id.tv_lishidingdan);
        tv_daiqudan = (TextView) headerView.findViewById(R.id.tv_daiqudan);
        tv_kuaijizhong = (TextView) headerView.findViewById(R.id.tv_kuaijizhong);

        tv_login_name = (TextView) headerView.findViewById(R.id.tv_login_name);
*/

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //老板说 不要侧滑
//      drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //关闭手势滑动
//      drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);      //打开手势滑动

      /*  drawer.setDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                boolean isLogin = new UserModel(MainActivity.this).isAutoLogin();

                if (isLogin) {// 是否 已经 登录
                    tv_login.setVisibility(View.GONE);
                    tv_login_name.setVisibility(View.VISIBLE);
                    tuichu.setVisibility(View.VISIBLE);
                    draweeview.setEnabled(true);
                } else {
                    tv_login.setVisibility(View.VISIBLE);
                    tuichu.setVisibility(View.GONE);
                    draweeview.setEnabled(false);
                }


            }
        });*/


        ivMain2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                tv_small_red.setVisibility(View.INVISIBLE);
                UIHelper.openMessage(MainActivity.this);

            }
        });


        ivMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                boolean isLogin = new UserModel(MainActivity.this).isAutoLogin();
//
//                if (isLogin) {// 是否 已经 登录
//                    tv_login.setVisibility(View.GONE);
//                    tv_login_name.setVisibility(View.VISIBLE);
//                    tuichu.setVisibility(View.VISIBLE);
//                    draweeview.setEnabled(true);
//                } else {
//                    tv_login.setVisibility(View.VISIBLE);
//                    tv_login_name.setVisibility(View.GONE);
//                    tuichu.setVisibility(View.GONE);
//                    draweeview.setEnabled(false);
//                }
//                getUserInfo();
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.openDrawer(GravityCompat.START);

                boolean isLogin = new UserModel(MainActivity.this).isAutoLogin();

                if (isLogin) {// 是否 已经 登录
                    startActivity(new Intent(MainActivity.this, PersonalCenterActivity.class));
                } else {
                    UIHelper.openLoginAvtivity(MainActivity.this, new OnLoginBackLinstener() {

                        @Override
                        public void onBack() {

                            startActivity(new Intent(MainActivity.this, PersonalCenterActivity.class));
                        }
                    });
                }
            }
        });

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_top);
        relativeLayout.getBackground().setAlpha(450);

        segmentControl.setOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
            @Override
            public void onSegmentControlClick(int index) {

                //     T.s(index+"");

                if (index == 0) {
                    tv_noti.setTextColor(Color.argb(0xff, 0xcc, 0x00, 0x00));
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction
                            .hide(mFragments[1]).show(mFragments[0]).commit();
                } else {

                    //    sendBroadcast(new Intent(MainActivity.ACTION_CG).putExtra("STA", false));

                    tv_noti.setTextColor(Color.WHITE);

                    //判断是否 登录
                    boolean isLogin = new UserModel(MainActivity.this).isAutoLogin();
                    if (isLogin) {

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction
                                .hide(mFragments[0]).show(mFragments[1]).commit();

                    } else {

                        UIHelper.openLoginAvtivity(MainActivity.this
                                , new OnLoginBackLinstener() {
                                    @Override
                                    public void onBack() {

                                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction
                                                .hide(mFragments[0]).show(mFragments[1]).commit();
                                    }
                                }
                        );

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction
                                .hide(mFragments[0]).show(mFragments[1]).commit();

                    }
                }

            }
        });
    }

  /*  private void getUserInfo() {
        UserApi.userGetinfo(MainActivity.this, new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

                getinfoBean = JSonUtils.toBean(GetinfoBean.class, responseStr);

                draweeview.setImageURI(Uri.parse(getinfoBean.getGuphoto()));

                //Picasso.with(MainActivity.this).load(getinfoBean.getGuphoto()).error(R.mipmap.icon_defut).into(draweeview);

                tv_login_name.setText(getinfoBean.getGuphone());
                tv_jifen.setText(getinfoBean.getGupoints());
                tv_qian.setText(getinfoBean.getGutotalmoney());
                tv_lishidingdan.setText(getinfoBean.getGutotalorder());

                tv_daiqudan.setText(getinfoBean.getDqj());
                tv_kuaijizhong.setText(getinfoBean.getDsh());
            }

            @Override
            public void onFailure(int statusCode) {

            }

            @Override
            public void onSendError(int statusCode, String message) {

            }
        });
    }*/

    /**
     * 初始化fragment
     */
    private void setFragmentIndicator() {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        mFragments = new Fragment[2];
        mFragments[0] = new MainFragment();
        mFragments[1] = new Main2Fragment();

        fragmentTransaction.add(R.id.content, mFragments[0]);
        fragmentTransaction.add(R.id.content, mFragments[1]);
        fragmentTransaction.commitAllowingStateLoss();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .hide(mFragments[1]).show(mFragments[0]).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //L.e("onDestroy");
        unregisterReceiver(mMSGReceiver);
        unregisterReceiver(mADReceiver);
        unregisterReceiver(mCGReceiver);
        unregisterReceiver(mCGStaReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
      //  TestinAgent.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
        //TestinAgent.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }

    /*

    @Override
    protected void onResume() {
        super.onResume();

        UserApi.userGetinfo(this, new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

                getinfoBean = JSonUtils.toBean(GetinfoBean.class, responseStr);

                draweeview.setImageURI(Uri.parse(getinfoBean.getGuphoto()));

                //Picasso.with(MainActivity.this).load(getinfoBean.getGuphoto()).error(R.mipmap.icon_defut).into(draweeview);

                tv_jifen.setText(getinfoBean.getGupoints());
                tv_qian.setText(getinfoBean.getGutotalmoney());
                tv_lishidingdan.setText(getinfoBean.getGutotalorder());
                tv_daiqudan.setText(getinfoBean.getDqj());
                tv_kuaijizhong.setText(getinfoBean.getDsh());
            }

            @Override
            public void onFailure(int statusCode) {

            }

            @Override
            public void onSendError(int statusCode, String message) {

            }
        });
    }
*/

    @Override
    protected void onStop() {
        super.onStop();

        //当试图不可见就不在显示了
        BaseApplication.set("bphoto", "");
        BaseApplication.set("burl", "");
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (BaseApplication.getInstance().isZFBPay() || BaseApplication.getInstance().isWxPay()) {
//            img_rad_noti.setVisibility(View.VISIBLE);
//        }

        //L.e("onResume");

      //  TestinAgent.onResume(this);

        UserApi.getMessageList(this, 0, AppConfig.PAGE_SIZE, new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

                ArrayList<MessageBean> messageBeans = (ArrayList<MessageBean>) JSonUtils.toList(MessageBean.class, responseStr);

                for (int i = 0; i < messageBeans.size(); i++) {

                    if (messageBeans.get(i).getSta().equals(0)) {
                        sendBroadcast(new Intent(MainActivity.ACTION_MEG).putExtra("XX", true));
                        break;
                    }
                }
            }

            @Override
            public void onFailure(int statusCode) {
                Log.d(TAG, "onFailure() called with: " + "statusCode = [" + statusCode + "]");
            }

            @Override
            public void onSendError(int statusCode, String message) {
                Log.d(TAG, "onSendError() returned: " + message);

            }
        });


        getfindman();


        String bname = BaseApplication.get("bphoto", "");
        //L.e(bname);
        if (!TextUtils.isEmpty(bname)) {

            root_layout.removeAllViews();
            root_layout.setFocusable(true);

            final View view = LayoutInflater.from(this).inflate(R.layout.pop_ad_bomb_screen, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.img_ad);

            if (!TextUtils.isEmpty(bname)) {
                KJBitmap kjBitmap = new KJBitmap();
                kjBitmap.display(imageView, bname, new BitmapCallBack() {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        root_layout.setVisibility(View.VISIBLE);
                        root_layout.addView(view,
                                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                                LinearLayoutCompat.LayoutParams.MATCH_PARENT);
                        //L.e("添加成功");
                    }
                });
            }
            final String burl = BaseApplication.get("burl", "");
            if (!TextUtils.isEmpty(burl)) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        root_layout.setVisibility(View.GONE);
                        startActivity(new Intent(MainActivity.this, GeneralWebView.class).putExtra("url", burl));
                    }
                });
            }

            TextView textView = (TextView) view.findViewById(R.id.tv_cancel);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    root_layout.setVisibility(View.GONE);
                    BaseApplication.set("bphoto", "");
                    BaseApplication.set("burl", "");
                }
            });
        }
    }

    private void getfindman() {

        OrderApi.orderfindspman(this, 0 * AppConfig.PAGE_SIZE, AppConfig.PAGE_SIZE, new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {
                //L.e("33333333" + responseStr);
                if (!TextUtils.isEmpty(responseStr)) {
                    ArrayList<FindspmanBean> arrayList = (ArrayList<FindspmanBean>) JSonUtils.toList(FindspmanBean.class, responseStr);

                    if (!arrayList.isEmpty())
                        sendBroadcast(new Intent(MainActivity.ACTION_CG).putExtra("STA", true).putExtra("NUM", arrayList.size()));
                } else {
                    sendBroadcast(new Intent(MainActivity.ACTION_CG).putExtra("STA", false).putExtra("NUM", 0));
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


    private void exit() {

        if ((System.currentTimeMillis() - exitTime) > 1500) {
            Toast.makeText(getApplicationContext(), "再按一次退出速派超人",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            AppManager.getAppManager().finishAllActivity();
            this.finish();

            //取消下载通知
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            super.onBackPressed();
            unregisterReceiver(mMSGReceiver);
            unregisterReceiver(mADReceiver);
            unregisterReceiver(mCGReceiver);
            unregisterReceiver(mCGStaReceiver);
        }
    }


    /**
     * 需要权限:android.permission.GET_TASKS
     * * @param context
     * * @return
     */
    public boolean isApplicationBroughtToBackground(Context context) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (tasks != null && !tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }


    @Override
    public void onBackPressed() {
//        LinearLayout drawer = (LinearLayout) findViewById(R.id.root_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
        exit(); //双击退出程序
        //       }
    }



    /*
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {

            item.setCheckable(false);
            int id = item.getItemId();

            if (id == R.id.about) {//关于

                startActivity(new Intent(this, AboutActivity.class));
            }else if(id == R.id.dd){// 意见反馈
                startActivity(new Intent(this, OpinionActivity.class));
            }else if(id == R.id.nav_camara2){// 常见问题
                UIHelper.OPENCommonQuestion(this);
            }else if(id == R.id.nav_camara){//我的 订单

                boolean isLogin = new UserModel(MainActivity.this).isAutoLogin();
                if(isLogin){

                    UIHelper.OPENCGOODS(this);
                }else{

                    UIHelper.openKhLoginAvtivity(this, new OnLoginBackLinstener() {
                        @Override
                        public void onBack() {

                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            drawer.closeDrawer(GravityCompat.START);
                            UIHelper.OPENCGOODS(MainActivity.this);

                        }
                    });
                }

    //            http://120.27.150.115/spcr/public/index.php/price/index

            }
            else if(id == R.id.nav_manage2){//价格表

                UIHelper.openJiage(MainActivity.this);
            }
            else if(id == R.id.nav_slideshow){//优惠券


                boolean isLogin = new UserModel(MainActivity.this).isAutoLogin();
                if(isLogin){

                    UIHelper.youhui(this);
                }else{

                    UIHelper.openKhLoginAvtivity(this, new OnLoginBackLinstener() {
                        @Override
                        public void onBack() {


                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            drawer.closeDrawer(GravityCompat.START);
                            UIHelper.youhui(MainActivity.this);

                        }
                    });
                }
            }
            else if(id == R.id.nav_fapiao){     //发票 申请

                boolean isLogin = new UserModel(MainActivity.this).isAutoLogin();
                if(isLogin){

                    String youhui = "00";
                    if(getinfoBean !=null){
                        youhui = getinfoBean.getGutotalmoney();
                    }

                    UIHelper.fapiao(this,youhui);
                }else{

                    UIHelper.openKhLoginAvtivity(this, new OnLoginBackLinstener() {
                        @Override
                        public void onBack() {

                            String youhui = "00";
                            if(getinfoBean !=null){
                                youhui = getinfoBean.getGutotalmoney();
                            }
                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            drawer.closeDrawer(GravityCompat.START);
                            UIHelper.fapiao(MainActivity.this,youhui);

                        }
                    });
                }
            }else if(id == R.id.tuichu){
                AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(this);
                alertbBuilder.setTitle("提示").setMessage("你确定要退出登录吗？");
                alertbBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PropertyUtil.setUinfo("");
                        tv_login.setVisibility(View.VISIBLE);
                        tv_login_name.setVisibility(View.GONE);

                        draweeview.setImageResource(R.mipmap.icon_defut);
                        tv_jifen.setText("0");
                        tv_qian.setText("0");
                        tv_lishidingdan.setText("0");
                        tv_daiqudan.setText("0");
                        tv_kuaijizhong.setText("0");

                    }
                });
                alertbBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
                alertbBuilder.show();
            }

            else{

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        */
/*
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
            boolean isLogin = new UserModel(MainActivity.this).isAutoLogin();
            if (!isLogin) {

                UIHelper.openLoginAvtivity(this);
            } else {
                startActivity(new Intent(this, SenderActivity.class));
            }
        } else if (id == R.id.nav_camara2) {// 常见问题
            UIHelper.OPENCommonQuestion(this);
        } else if (id == R.id.nav_camara) {//我的 订单

            boolean isLogin = new UserModel(MainActivity.this).isAutoLogin();
            if (isLogin) {

                UIHelper.OPENCGOODS(this);
            } else {

                UIHelper.openKhLoginAvtivity(this, new OnLoginBackLinstener() {
                    @Override
                    public void onBack() {

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        UIHelper.OPENCGOODS(MainActivity.this);

                    }
                });
            }

//            http://120.27.150.115/spcr/public/index.php/price/index

        } else if (id == R.id.nav_manage2) {//价格表

            UIHelper.openJiage(MainActivity.this);

        } else if (id == R.id.nav_slideshow) {//优惠券


            boolean isLogin = new UserModel(MainActivity.this).isAutoLogin();
            if (isLogin) {

                UIHelper.youhui(this);
            } else {

                UIHelper.openKhLoginAvtivity(this, new OnLoginBackLinstener() {
                    @Override
                    public void onBack() {


                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        UIHelper.youhui(MainActivity.this);

                    }
                });
            }
        } else if (id == R.id.nav_fapiao) {     //发票 申请

            boolean isLogin = new UserModel(MainActivity.this).isAutoLogin();
            if (isLogin) {

                String youhui = "00";
                if (getinfoBean != null) {
                    youhui = getinfoBean.getGutotalmoney();
                }

                UIHelper.fapiao(this, youhui);
            } else {

                UIHelper.openKhLoginAvtivity(this, new OnLoginBackLinstener() {
                    @Override
                    public void onBack() {

                        String youhui = "00";
                        if (getinfoBean != null) {
                            youhui = getinfoBean.getGutotalmoney();
                        }
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        UIHelper.fapiao(MainActivity.this, youhui);

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
                    tv_login.setVisibility(View.VISIBLE);
                    tv_login_name.setVisibility(View.GONE);

                    draweeview.setImageResource(R.mipmap.icon_defut);
                    tv_jifen.setText("0");
                    tv_qian.setText("0");
                    tv_lishidingdan.setText("0");
                    tv_daiqudan.setText("0");
                    tv_kuaijizhong.setText("0");

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

                                    PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(MainActivity.this, new String[]{Manifest.permission.CAMERA}, new PermissionsResultAction() {

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionsResultAction() {
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
                            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(MainActivity.this,
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
                                                draweeview.setImageBitmap(BitmapFactory.decodeFile(theThumbnail));
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
                                draweeview.setImageBitmap(BitmapFactory.decodeFile(theThumbnail));
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
                                PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(MainActivity.this,
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
                                                    draweeview.setImageBitmap(BitmapFactory.decodeFile(theThumbnail));

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
                                    draweeview.setImageBitmap(BitmapFactory.decodeFile(theThumbnail));

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
*/
   /* private void upHeader(final String path) {

        UserApi.upHeader(this, new File(path), new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

                T.s("图片上传成功");
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
    }*/

}
