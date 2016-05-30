package com.supaiclient.app.bean;

import java.io.Serializable;

/**
 * 个人信息
 * Created by Administrator on 2016/2/13.
 */
public class GetinfoBean implements Serializable {

    private String gutotalmoney;// float 15.62 消费总金额
    private String guleavmoney;// float 196.56 可开发票金额
    private String gutotalorder;// int 196 总订单数
    private String gupoints;// int 196 积分
    private String guphone;//string 15032212001 电话号码
    private String guphoto;//string http:/xxx.xxx.com/xx.png 头像
    private String dqj;// int 12 待抢件
    private String dsh;//int 10 待收货

    public GetinfoBean() {
        super();
    }

    public GetinfoBean(String gutotalmoney, String guleavmoney, String gutotalorder, String gupoints, String guphone, String guphoto, String dqj, String dsh) {
        this.gutotalmoney = gutotalmoney;
        this.guleavmoney = guleavmoney;
        this.gutotalorder = gutotalorder;
        this.gupoints = gupoints;
        this.guphone = guphone;
        this.guphoto = guphoto;
        this.dqj = dqj;
        this.dsh = dsh;
    }

    public String getGutotalmoney() {
        return gutotalmoney;
    }

    public void setGutotalmoney(String gutotalmoney) {
        this.gutotalmoney = gutotalmoney;
    }

    public String getGuleavmoney() {
        return guleavmoney;
    }

    public void setGuleavmoney(String guleavmoney) {
        this.guleavmoney = guleavmoney;
    }

    public String getGutotalorder() {
        return gutotalorder;
    }

    public void setGutotalorder(String gutotalorder) {
        this.gutotalorder = gutotalorder;
    }

    public String getGupoints() {
        return gupoints;
    }

    public void setGupoints(String gupoints) {
        this.gupoints = gupoints;
    }

    public String getGuphone() {
        return guphone;
    }

    public void setGuphone(String guphone) {
        this.guphone = guphone;
    }

    public String getGuphoto() {
        return guphoto;
    }

    public void setGuphoto(String guphoto) {
        this.guphoto = guphoto;
    }

    public String getDqj() {
        return dqj;
    }

    public void setDqj(String dqj) {
        this.dqj = dqj;
    }

    public String getDsh() {
        return dsh;
    }

    public void setDsh(String dsh) {
        this.dsh = dsh;
    }

    @Override
    public String toString() {
        return "GetinfoBean{" +
                "gutotalmoney='" + gutotalmoney + '\'' +
                ", guleavmoney='" + guleavmoney + '\'' +
                ", gutotalorder='" + gutotalorder + '\'' +
                ", gupoints='" + gupoints + '\'' +
                ", guphone='" + guphone + '\'' +
                ", guphoto='" + guphoto + '\'' +
                ", dqj='" + dqj + '\'' +
                ", dsh='" + dsh + '\'' +
                '}';
    }
}
