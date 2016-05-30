package com.supaiclient.app.api;

/**
 * Created by Administrator on 2015/12/7.
 */
public class UrlUtil {

    // 登录
    public final static String LOGIN = "/login/index";

    //发送验证码
    public final static String sendmsg = "/login/sendmsg";

    //快捷 登录 获取验证码
    public final static String ordersendmsg = "/order/sendmsg";

    //快捷登录
    public final static String fastlogin = "/login/fastlogin";

    // 注册
    public final static String register = "/login/register";

    // 重置密码提交
    public final static String changepwd = "/login/changepwd";

    //收货 历史地址
    public final static String areasendadd = "/area/sendadd";

    //寄件人 历史地址
    public final static String areagetadd = "/area/getadd";

    //计算价格
    public final static String ordergetprice = "/order/getprice";

    //提交 订单
    public final static String orderaddorder = "/order/addorder";

    // 微信 支付 回调
    public final static String paywxpay = "/pay/wxpay";

    // 寄件人 管理 列表
    public final static String senduserlist = "/senduser/list";
    // 寄件人 管理 添加或删除
    public final static String senduseradd = "/senduser/add";
    // 寄件人 管理 删除
    public final static String senduserdel = "/senduser/del";

    // 意见 反馈
    public final static String viewadd = "/view/add";
    // 常见 问题
    public final static String problemlist = "/problem/list";
    public final static String orderlist = "/order/list";
    // 支付
    public final static String orderpayorder = "/order/payorder";

    // 获取 用户 资料
    public final static String usergetinfo = "/user/getinfo";
    public final static String ordersuinfo = "/order/suinfo";
    public final static String replay = "/order/replay";
    public final static String orderfindspman = "/order/findspman";
    public final static String orderodloca = "/order/odloca";
    public final static String userheader = "/user/addpt";

    // 发票 申请
    public final static String invoicesub = "/invoice/sub";
    public final static String invoicelog = "/invoice/log";
    public final static String redbaggetared = "/redbag/getared";
    public final static String getred = "/redbag/getred";
    public final static String getsenduser = "/user/getsenduser";
    public final static String orderdetail = "/order/detail";
    public final static String orderqxorder = "/order/qxorder";
    public final static String orderhistory = "/order/history";

    //获取邀请码
    public final static String invite = "/user/invite";
    //积分查询
    public final static String pointlist = "/point/pointlist";
    public final static String pointinfo = "/point/pointinfo";


    //推送码
    public final static String clientnum = "/login/clientnum";

    //消息
    public final static String messagelist = "/message/list";
    public final static String messageread = "/message/read";

    //版本更新
    public final static String getversion = "/common/getversion";

    public final static String odloca = "/order/odloca";


}
