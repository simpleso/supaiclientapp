package com.supaiclient.app.bean;

import com.supaiclient.app.ui.fragment.address.AddressHistoryFragment;
import com.supaiclient.app.ui.fragment.contacts.ContactsFragmemt;
import com.supaiclient.app.ui.fragment.goods.DetailsFragment;
import com.supaiclient.app.ui.fragment.goods.DiscountFragment;
import com.supaiclient.app.ui.fragment.goods.DiscussFragment;
import com.supaiclient.app.ui.fragment.goods.GoodsFragment;
import com.supaiclient.app.ui.fragment.goods.JiaGeFragment;
import com.supaiclient.app.ui.fragment.goods.MyOrderHistoryFragment;
import com.supaiclient.app.ui.fragment.integral.IntegralFragment;
import com.supaiclient.app.ui.fragment.user.CommonQuestionFragment;
import com.supaiclient.app.ui.fragment.user.FapiaoAddFragment;
import com.supaiclient.app.ui.fragment.user.FapiaoFragment;
import com.supaiclient.app.ui.fragment.user.TransactionListFragment;
import com.supaiclient.app.ui.fragment.message.MessageFragment;
import com.supaiclient.app.ui.fragment.webview.WebViewFragment;

/**
 * Created by Administrator on 2015/12/11.
 */
public enum SimpleBackPage {

    OPENCONTACTS(1, "选择联系人", ContactsFragmemt.class),
    // OPENCONTACTS(1, "选择联系人", PhoneBookFragment.class),
    OPENADDRESSHISTORY(2, "选择地址", AddressHistoryFragment.class),
    OPENCommonQuestion(3, "常见问题", CommonQuestionFragment.class),
    OPENCGOODS(4, "我的订单", GoodsFragment.class),
    OPENPINGLUN(5, "评论", DiscussFragment.class),
    openfapiao(6, "发票申请", FapiaoFragment.class),
    openfapiaoadd(7, "发票申请", FapiaoAddFragment.class),
    TransactionList(8, "申请记录", TransactionListFragment.class),
    JIage(9, "价格表", JiaGeFragment.class),
    youhui(10, "优惠券", DiscountFragment.class),
    xiangqing(11, "订单详情", DetailsFragment.class),
    MyOrderHistoryFragment(12, "notitle", MyOrderHistoryFragment.class),
    IntegralFragment(13, "我的积分", IntegralFragment.class),
    MessageFragment(14, "消息中心", MessageFragment.class),
    WebViewFragment(15, "消息详情", WebViewFragment.class);

    private String title;
    private Class<?> clz;
    private int value;

    SimpleBackPage(int value, String title, Class<?> clz) {
        this.value = value;
        this.title = title;
        this.clz = clz;
    }

    public static SimpleBackPage getPageByValue(int val) {
        for (SimpleBackPage p : values()) {
            if (p.getValue() == val)
                return p;
        }
        return null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
