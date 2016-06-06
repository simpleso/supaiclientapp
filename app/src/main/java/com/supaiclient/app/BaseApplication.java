package com.supaiclient.app;

import android.annotation.TargetApi;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.supaiclient.app.api.ApiHttpClient;
import com.supaiclient.app.interf.OnLoginBackLinstener;
import com.supaiclient.app.util.CustomExceptionHandler;

import org.kymjs.kjframe.KJHttp;

/**
 * 全局应用
 * Created by Administrator on 2015/12/3.
 */
public class BaseApplication extends MultiDexApplication implements ComponentCallbacks2 {

    public static String lastToast = "";
    public static long lastToastTime;
    public static boolean isZFsucceed = false;
    static Context context;
    private static String PREF_NAME = "creativelocker.pref";
    private static BaseApplication instance;
    private static boolean sIsAtLeastGB;
    private static int versionCode = 0;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            sIsAtLeastGB = true;
        }
    }

    public String onumber;
    public String needpay;
    public String snedSign;
    //登录 监听
    public OnLoginBackLinstener onLoginBackLinstener;
    private Boolean isBack = false;
    private boolean isCreatOrder = false;
    // 微信 支付 是否成功
    private boolean isWxPay = false;
    // 支付宝 支付 是否成功
    private boolean isZFBPay = false;

    public static synchronized BaseApplication getContext() {
        return (BaseApplication) context;
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void apply(SharedPreferences.Editor editor) {
        if (sIsAtLeastGB) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public static void set(String key, int value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(key, value);
        apply(editor);
    }

    public static void set(String key, boolean value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        apply(editor);
    }

    public static void set(String key, String value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(key, value);
        apply(editor);
    }

    public static boolean get(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    public static String get(String key, String defValue) {
        return getPreferences().getString(key, defValue);
    }

    public static int get(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    public static long get(String key, long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    public static float get(String key, float defValue) {
        return getPreferences().getFloat(key, defValue);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getPreferences() {
        SharedPreferences pre = getContext().getSharedPreferences(PREF_NAME,
                Context.MODE_MULTI_PROCESS);
        return pre;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getPreferences(String prefName) {
        return getContext().getSharedPreferences(prefName,
                Context.MODE_MULTI_PROCESS);
    }

    //版本名
    public static String getVersionName() {
        return getPackageInfo(getContext()).versionName;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    //获取版本号
    public static int getVersionCode() {

        if (versionCode != 0) {
            return versionCode;
        }
        try {
            versionCode = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    public Boolean getIsBack() {
        return isBack;
    }

    public void setIsBack(Boolean isBack) {
        this.isBack = isBack;
    }

    public boolean isCreatOrder() {
        return isCreatOrder;
    }

    public void setIsCreatOrder(boolean isCreatOrder) {
        this.isCreatOrder = isCreatOrder;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        context = getApplicationContext();

        //DataCleanManager.cleanInternalCache(this);

        // 初始化网络请求
//        AsyncHttpClient client = new AsyncHttpClient();
//        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
//        client.setCookieStore(myCookieStore);
//        client.setMaxConnections(200000);

        ApiHttpClient.setHttpClient(new KJHttp());

        //xUtils 的初始化
        // x.Ext.init(this);
        // x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.

//        MobclickAgent.setCheckDevice(true);

        //修改字体

//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().
//                setDefaultFontPath("fonts/Roboto-Bold.ttf").
//                setFontAttrId(R.attr.fontPath).build());

        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(getApplicationContext()));

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Log.e("siyehua", "background...");
            BaseApplication.getInstance().setIsBack(true);
        }
    }

    public boolean isWxPay() {
        return isWxPay;
    }

    public void setIsWxPay(boolean isWxPay) {
        this.isWxPay = isWxPay;
    }

    public boolean isZFBPay() {
        return isZFBPay;
    }

    public void setIsZFBPay(boolean isZFBPay) {
        this.isZFBPay = isZFBPay;
    }

    public OnLoginBackLinstener getOnLoginBackLinstener() {
        return onLoginBackLinstener;
    }

    public void setOnLoginBackLinstener(OnLoginBackLinstener onLoginBackLinstener) {
        this.onLoginBackLinstener = onLoginBackLinstener;
    }
}
