package com.supaiclient.app.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/28.
 * 红包 实体类
 */
public class RedbagBean implements Serializable {

    private String rid;// int 1 红包id
    private String money;// float 15 红包金额
    private String outtime;// int 1543434 红包过期时间
    private String onumber;//int 11 红包使用的订单号
    private String createtime;// int 14343434 红包创建时间
    private String usetime;// int 43434 红包使用时间
//    [
//    {
//        "rid": 12,
//            "money": 2,
//            "weight": 0,
//            "distance": 0,
//            "price": 0
//    }
//    ]

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public String getOnumber() {
        return onumber;
    }

    public void setOnumber(String onumber) {
        this.onumber = onumber;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUsetime() {
        return usetime;
    }

    public void setUsetime(String usetime) {
        this.usetime = usetime;
    }

    @Override
    public String toString() {
        return "RedbagBean{" +
                "rid='" + rid + '\'' +
                ", money='" + money + '\'' +
                ", outtime='" + outtime + '\'' +
                ", onumber='" + onumber + '\'' +
                ", createtime='" + createtime + '\'' +
                ", usetime='" + usetime + '\'' +
                '}';
    }
}
