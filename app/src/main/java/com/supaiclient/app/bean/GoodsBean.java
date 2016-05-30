package com.supaiclient.app.bean;

/**
 * 订单
 * Created by zgc on 16/2/4.
 */
public class GoodsBean extends BaseResponseBodyBean {


    private String needprice;//价格
    private String onumber;//订单号
    private String weight;//重量
    private String getcode;//取件码
    private String otime;//订单时间
    private String takecode;//收件码
    private String status;//状态码//0:未付款，1:代速派 2:代取件  3:代收获 4:代 评价 5: 已取消  6:完成
    private String distance;//距离
    private String sendaddress;//取件地址
    private String tadd;//收件地址

    public String getNeedprice() {
        return needprice;
    }

    public void setNeedprice(String needprice) {
        this.needprice = needprice;
    }

    public String getOnumber() {
        return onumber;
    }

    public void setOnumber(String onumber) {
        this.onumber = onumber;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getGetcode() {
        return getcode;
    }

    public void setGetcode(String getcode) {
        this.getcode = getcode;
    }

    public String getOtime() {
        return otime;
    }

    public void setOtime(String otime) {
        this.otime = otime;
    }

    public String getTakecode() {
        return takecode;
    }

    public void setTakecode(String takecode) {
        this.takecode = takecode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSendaddress() {
        return sendaddress;
    }

    public void setSendaddress(String sendaddress) {
        this.sendaddress = sendaddress;
    }

    public String getTadd() {
        return tadd;
    }

    public void setTadd(String tadd) {
        this.tadd = tadd;
    }

    @Override
    public String toString() {
        return "GoodsBean{" +
                "needprice='" + needprice + '\'' +
                ", onumber='" + onumber + '\'' +
                ", weight='" + weight + '\'' +
                ", getcode='" + getcode + '\'' +
                ", otime='" + otime + '\'' +
                ", takecode='" + takecode + '\'' +
                ", status='" + status + '\'' +
                ", distance='" + distance + '\'' +
                ", sendaddress='" + sendaddress + '\'' +
                ", tadd='" + tadd + '\'' +
                '}';
    }
}
