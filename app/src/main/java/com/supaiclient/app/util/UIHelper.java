package com.supaiclient.app.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.supaiclient.app.BaseApplication;
import com.supaiclient.app.bean.PeopleBean;
import com.supaiclient.app.bean.SimpleBackPage;
import com.supaiclient.app.interf.OnLoginBackLinstener;
import com.supaiclient.app.ui.activity.home.MainActivity;
import com.supaiclient.app.ui.activity.user.LoginActivity;
import com.supaiclient.app.ui.activity.user.UserBaseRegisterOrPasswdActivity;
import com.supaiclient.app.ui.base.SimpleBackActivity;

/**
 * Created by Administrator on 2015/12/28.
 */
public class UIHelper {

    public static void showSimpleBack(Context context, SimpleBackPage page,
                                      Bundle args) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivity(intent);
    }

    public static void showSimpleBackForResult(Activity activity, SimpleBackPage page,
                                               Bundle args) {
        Intent intent = new Intent(activity, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        activity.startActivityForResult(intent, 200);
    }

    public static void showSimpleBack(Context context, SimpleBackPage page) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivity(intent);
    }

    /**
     * 打开 选择 联系人
     *
     * @param activity
     */
    public static void openContacts(Activity activity, int type) {

        Intent intent = new Intent(activity, SimpleBackActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, bundle);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, SimpleBackPage.OPENCONTACTS.getValue());
        activity.startActivityForResult(intent, 200);

    }

    /**
     * 周围 地址 或 历史地址
     *
     * @param context
     * @param type    0 :收件 地址；1：寄件 地址
     */
    public static void openAddressHistory(Fragment context, PeopleBean peopleBean_dw, String city, int type) {

        Bundle bundle = new Bundle();
        bundle.putString("city", city);
        bundle.putInt("type", type);
        bundle.putSerializable("peopleBean_dw", peopleBean_dw);
        Intent intent = new Intent(context.getActivity(), SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, bundle);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, SimpleBackPage.OPENADDRESSHISTORY.getValue());
        context.startActivityForResult(intent, 200);
    }


    /**
     * 周围 地址 或 历史地址
     *
     * @param context
     * @param type    0 :收件 地址；1：寄件 地址
     */
    public static void openIntegralFragment(Fragment context, PeopleBean peopleBean_dw, String city, int type) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("peopleBean_dw", peopleBean_dw);
        Intent intent = new Intent(context.getActivity(), SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, bundle);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, SimpleBackPage.OPENADDRESSHISTORY.getValue());
        context.startActivityForResult(intent, 200);
    }


    /**
     * 打开首页
     *
     * @param context
     */
    public static void openHomeAvtivity(Context context) {

        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * 打开登录
     *
     * @param context
     */
    public static void openLoginAvtivity(Context context) {

        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * 打开登录
     *
     * @param context
     */
    public static void openLoginAvtivity(Context context, OnLoginBackLinstener onLoginBackLinstener) {
        BaseApplication.getInstance().setOnLoginBackLinstener(onLoginBackLinstener);
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * 快捷登录
     *
     * @param context
     */
    public static void openKhLoginAvtivity(Context context, OnLoginBackLinstener onLoginBackLinstener) {

        BaseApplication.getInstance().setOnLoginBackLinstener(onLoginBackLinstener);
        Intent intent = new Intent(context, UserBaseRegisterOrPasswdActivity.class);
        intent.putExtra(UserBaseRegisterOrPasswdActivity.OPENKEY, UserBaseRegisterOrPasswdActivity.KUAIJIELOGIN);
        context.startActivity(intent);
    }

    /**
     * 忘记密码
     *
     * @param context
     */
    public static void openForGetPasswordAvtivity(Context context) {

        Intent intent = new Intent(context, UserBaseRegisterOrPasswdActivity.class);
        intent.putExtra(UserBaseRegisterOrPasswdActivity.OPENKEY, UserBaseRegisterOrPasswdActivity.FORGETPASSSWORDVALUE);
        context.startActivity(intent);
    }

    /**
     * 重置密码
     *
     * @param context
     */
    public static void openResetPasswordAvtivity(Context context) {

        Intent intent = new Intent(context, UserBaseRegisterOrPasswdActivity.class);
        intent.putExtra(UserBaseRegisterOrPasswdActivity.OPENKEY, UserBaseRegisterOrPasswdActivity.RESETPASSSWORDVALUE);
        context.startActivity(intent);
    }

    /**
     * 注册
     *
     * @param context
     */
    public static void openRegisterAvtivity(Context context) {

        Intent intent = new Intent(context, UserBaseRegisterOrPasswdActivity.class);
        intent.putExtra(UserBaseRegisterOrPasswdActivity.OPENKEY, UserBaseRegisterOrPasswdActivity.REGISTERVALUE);
        context.startActivity(intent);
    }


    // 常见  问题
    public static void OPENCommonQuestion(Context context) {

        showSimpleBack(context, SimpleBackPage.OPENCommonQuestion);
    }

    // 我的 订单
    public static void OPENCGOODS(Context context) {
        showSimpleBack(context, SimpleBackPage.OPENCGOODS);
    }

    // 评论
    public static void OPENPINGLUN(Context context, String number) {
        Bundle bundle = new Bundle();
        bundle.putString("number", number);

        showSimpleBack(context, SimpleBackPage.OPENPINGLUN, bundle);
    }

    // 优惠券
    public static void youhui(Context context) {

        showSimpleBack(context, SimpleBackPage.youhui);
    }

    // 发票
    public static void fapiao(Context context, String money) {
        Bundle bundle = new Bundle();
        bundle.putString("youhui", money);
        showSimpleBack(context, SimpleBackPage.openfapiao, bundle);
    }

    // 发票 申请
    public static void shenqing(Context context, String money) {
        Bundle bundle = new Bundle();
        bundle.putString("youhui", money);
        showSimpleBack(context, SimpleBackPage.openfapiaoadd, bundle);
    }

    // 交易 记录
    public static void TransactionList(Context context) {
        showSimpleBack(context, SimpleBackPage.TransactionList);
    }

    // 价格表
    public static void openJiage(Context context) {

        showSimpleBack(context, SimpleBackPage.JIage);
    }

    // 订单详情
    public static void xiangqing(Context context, Bundle bundle) {
        showSimpleBack(context, SimpleBackPage.xiangqing, bundle);
    }

    // 订单历史
    public static void MyOrderHistoryFragment(Context context, Bundle bundle) {
        showSimpleBack(context, SimpleBackPage.MyOrderHistoryFragment, bundle);
    }

    // 价格表
    public static void openMessage(Context context) {

        showSimpleBack(context, SimpleBackPage.MessageFragment);
    }

    // 打开浏览器
    public static void openWebView(Context context, String title, Bundle bundle) {

        SimpleBackPage mSimpleBackPage = SimpleBackPage.WebViewFragment;

        //Title跟着改变
        //   mSimpleBackPage.setTitle(title);

        showSimpleBack(context, mSimpleBackPage, bundle);
    }
}
