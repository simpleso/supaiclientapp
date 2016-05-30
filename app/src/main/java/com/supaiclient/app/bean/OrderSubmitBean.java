package com.supaiclient.app.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/31.
 * 订单 提交 数据
 */
public class OrderSubmitBean implements Serializable {

    private PeopleBean peopleBean_sj;//收件人地址 对象
    private PeopleBean peopleBean_jj;// 取件人 地址 对象

    private String weight;//重量
    private String distance;// 距离

    private String taketype;//1及时单，2预约单
    private String taketime;// 预约 时间

    private String gImg;   //拍照的图片地址

    private String oname;// 订单 名称

    private int cpstyle;//交通 工具 之和

    private String addprice;//加的 价格
    private String nightnight; //夜间家

    private String message;// 备注

    private int rid;//红包ID

    private String points = "";
    private float percent = 0;

    public PeopleBean getPeopleBean_sj() {
        return peopleBean_sj;
    }

    public void setPeopleBean_sj(PeopleBean peopleBean_sj) {
        this.peopleBean_sj = peopleBean_sj;
    }

    public PeopleBean getPeopleBean_jj() {
        return peopleBean_jj;
    }

    public void setPeopleBean_jj(PeopleBean peopleBean_jj) {
        this.peopleBean_jj = peopleBean_jj;
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

    public String getOname() {
        return oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public int getCpstyle() {
        return cpstyle;
    }

    public void setCpstyle(int cpstyle) {
        this.cpstyle = cpstyle;
    }

    public String getAddprice() {
        return addprice;
    }

    public void setAddprice(String addprice) {
        this.addprice = addprice;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getgImg() {
        return gImg;
    }

    public void setgImg(String gImg) {
        this.gImg = gImg;
    }

    public String getNightnight() {
        return nightnight;
    }

    public void setNightnight(String nightnight) {
        this.nightnight = nightnight;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
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
        return "OrderSubmitBean{" +
                "peopleBean_sj=" + peopleBean_sj +
                ", peopleBean_jj=" + peopleBean_jj +
                ", weight='" + weight + '\'' +
                ", distance='" + distance + '\'' +
                ", taketype='" + taketype + '\'' +
                ", taketime='" + taketime + '\'' +
                ", gImg='" + gImg + '\'' +
                ", oname='" + oname + '\'' +
                ", cpstyle=" + cpstyle +
                ", addprice='" + addprice + '\'' +
                ", nightnight='" + nightnight + '\'' +
                ", message='" + message + '\'' +
                ", rid=" + rid +
                ", points='" + points + '\'' +
                ", percent=" + percent +
                '}';
    }
}
