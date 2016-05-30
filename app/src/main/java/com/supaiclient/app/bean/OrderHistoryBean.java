package com.supaiclient.app.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/13.
 */
public class OrderHistoryBean implements Serializable {

    private String otime;// int 1450751630 下单时间
    private String paytime;// int 0 支付时间
    private String battletime;// int 0 抢单时间
    private String gettime;// int 0 取货时间
    private String completetime;// int 0 收货时间
    private String replaytime;// int 0 评价时间
    private String deltime;//int 0 取消时间
    private String status;// int 1 订单状态，0未付款，1付款，2抢单，3取货，4收货，5取消，6完成评价
    private String onumber;// string IAL2210335273004 订单号
    private SenduserBean senduser;// array 见示例 送货员信息
    private String getcode;// int 见示例 取件码
    private String takecode;//int 见示例 收件码

    public String getOtime() {
        return otime;
    }

    public void setOtime(String otime) {
        this.otime = otime;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getBattletime() {
        return battletime;
    }

    public void setBattletime(String battletime) {
        this.battletime = battletime;
    }

    public String getGettime() {
        return gettime;
    }

    public void setGettime(String gettime) {
        this.gettime = gettime;
    }

    public String getCompletetime() {
        return completetime;
    }

    public void setCompletetime(String completetime) {
        this.completetime = completetime;
    }

    public String getReplaytime() {
        return replaytime;
    }

    public void setReplaytime(String replaytime) {
        this.replaytime = replaytime;
    }

    public String getDeltime() {
        return deltime;
    }

    public void setDeltime(String deltime) {
        this.deltime = deltime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOnumber() {
        return onumber;
    }

    public void setOnumber(String onumber) {
        this.onumber = onumber;
    }

    public SenduserBean getSenduser() {
        return senduser;
    }

    public void setSenduser(SenduserBean senduser) {
        this.senduser = senduser;
    }

    public String getGetcode() {
        return getcode;
    }

    public void setGetcode(String getcode) {
        this.getcode = getcode;
    }

    public String getTakecode() {
        return takecode;
    }

    public void setTakecode(String takecode) {
        this.takecode = takecode;
    }

    @Override
    public String toString() {
        return "OrderHistoryBean{" +
                "otime='" + otime + '\'' +
                ", paytime='" + paytime + '\'' +
                ", battletime='" + battletime + '\'' +
                ", gettime='" + gettime + '\'' +
                ", completetime='" + completetime + '\'' +
                ", replaytime='" + replaytime + '\'' +
                ", deltime='" + deltime + '\'' +
                ", status='" + status + '\'' +
                ", onumber='" + onumber + '\'' +
                ", senduser=" + senduser +
                ", getcode='" + getcode + '\'' +
                ", takecode='" + takecode + '\'' +
                '}';
    }
}
