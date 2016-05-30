package com.supaiclient.app.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/13.
 */
public class OrderDetailBean implements Serializable {

    private String oname;// string 商品名称 商品名称
    private String weight;// int 15 重量
    private String distance;// int 5 距离
    private String cpstyle;//string 7 完成方式
    private String totalprice;//float 17.8 总价格
    private String needprice;// float 16.3 需要支付
    private String addprice;//float 15.6 加价
    private String taketype;// int 1 1是立即取件，2是预约取件
    private String taketime;// int 15233165631 预约取件时间
    private String message;// string 测试备注 备注
    private String gimg;//string http://www.baidu.com 图片url
    private float night;
    private int points;

    private Sendadd sendadd;
    private Takeadd takeadd;

    private float percent;

    public String getOname() {
        return oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCpstyle() {
        return cpstyle;
    }

    public void setCpstyle(String cpstyle) {
        this.cpstyle = cpstyle;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getNeedprice() {
        return needprice;
    }

    public void setNeedprice(String needprice) {
        this.needprice = needprice;
    }

    public String getAddprice() {
        return addprice;
    }

    public void setAddprice(String addprice) {
        this.addprice = addprice;
    }

    public String getTaketype() {
        return taketype;
    }

    public void setTaketype(String taketype) {
        this.taketype = taketype;
    }

    public String getTaketime() {
        return taketime;
    }

    public void setTaketime(String taketime) {
        this.taketime = taketime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGimg() {
        return gimg;
    }

    public void setGimg(String gimg) {
        this.gimg = gimg;
    }

    public Sendadd getSendadd() {
        return sendadd;
    }

    public void setSendadd(Sendadd sendadd) {
        this.sendadd = sendadd;
    }

    public Takeadd getTakeadd() {
        return takeadd;
    }

    public void setTakeadd(Takeadd takeadd) {
        this.takeadd = takeadd;
    }

    public float getNight() {
        return night;
    }

    public void setNight(float night) {
        this.night = night;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "OrderDetailBean{" +
                "oname='" + oname + '\'' +
                ", weight='" + weight + '\'' +
                ", distance='" + distance + '\'' +
                ", cpstyle='" + cpstyle + '\'' +
                ", totalprice='" + totalprice + '\'' +
                ", needprice='" + needprice + '\'' +
                ", addprice='" + addprice + '\'' +
                ", taketype='" + taketype + '\'' +
                ", taketime='" + taketime + '\'' +
                ", message='" + message + '\'' +
                ", gimg='" + gimg + '\'' +
                ", night=" + night +
                ", points=" + points +
                ", sendadd=" + sendadd +
                ", takeadd=" + takeadd +
                ", percent=" + percent +
                '}';
    }
}
