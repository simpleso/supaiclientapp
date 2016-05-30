package com.supaiclient.app.bean;

/**
 * Created by Administrator on 2016/2/22.
 */
public class InvoiceBean extends BaseResponseBodyBean {

    private String money;
    private String addtime;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        return "InvoiceBean{" +
                "money='" + money + '\'' +
                ", addtime='" + addtime + '\'' +
                '}';
    }
}
