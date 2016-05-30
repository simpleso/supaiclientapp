package com.supaiclient.app.bean;

import android.graphics.Bitmap;

/**
 * 附近快递员
 * Created by Administrator on 2016/2/15.
 */
public class FindspmanBean extends BaseResponseBodyBean {

    private String senduserlogo;//string url logourl
    private String onumber;//string AAL2118445693643 订单号
    private String battleuser;//int 0 快递员id
    private String cplat;//float 0 快递员当前位置维度
    private String cplng;//float 0 快递员当前维度
    private String tadd;//string 收件地址 收件地址
    private String tlat;//float 0 收件经度
    private String tlng;//float 0 收件维度
    private String suphone;//string 15032212001 配送员电话
    private String suname;//string 傻逼 配送员姓名
    private Bitmap bitmap;
    private String addr = "";

    public String getSenduserlogo() {
        return senduserlogo;
    }

    public void setSenduserlogo(String senduserlogo) {
        this.senduserlogo = senduserlogo;
    }

    public String getOnumber() {
        return onumber;
    }

    public void setOnumber(String onumber) {
        this.onumber = onumber;
    }

    public String getBattleuser() {
        return battleuser;
    }

    public void setBattleuser(String battleuser) {
        this.battleuser = battleuser;
    }

    public String getCplat() {
        return cplat;
    }

    public void setCplat(String cplat) {
        this.cplat = cplat;
    }

    public String getCplng() {
        return cplng;
    }

    public void setCplng(String cplng) {
        this.cplng = cplng;
    }

    public String getTadd() {
        return tadd;
    }

    public void setTadd(String tadd) {
        this.tadd = tadd;
    }

    public String getTlat() {
        return tlat;
    }

    public void setTlat(String tlat) {
        this.tlat = tlat;
    }

    public String getTlng() {
        return tlng;
    }

    public void setTlng(String tlng) {
        this.tlng = tlng;
    }

    public String getSuphone() {
        return suphone;
    }

    public void setSuphone(String suphone) {
        this.suphone = suphone;
    }

    public String getSuname() {
        return suname;
    }

    public void setSuname(String suname) {
        this.suname = suname;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public String toString() {
        return "FindspmanBean{" +
                "senduserlogo='" + senduserlogo + '\'' +
                ", onumber='" + onumber + '\'' +
                ", battleuser='" + battleuser + '\'' +
                ", cplat='" + cplat + '\'' +
                ", cplng='" + cplng + '\'' +
                ", tadd='" + tadd + '\'' +
                ", tlat='" + tlat + '\'' +
                ", tlng='" + tlng + '\'' +
                ", suphone='" + suphone + '\'' +
                ", suname='" + suname + '\'' +
                ", bitmap=" + bitmap +
                ", addr='" + addr + '\'' +
                '}';
    }
}
