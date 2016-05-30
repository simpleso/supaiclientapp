package com.supaiclient.app.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/13.
 */
public class Sendadd implements Serializable {

    private String sendname;//": "乐毅",
    private String sendaddress;//": "重庆市渝中区中山三路121号",
    private String sendlat;//": 29.559961,
    private String sendlng;//": 106.555183,
    private String sendphone;//": "1388888888888"

    public String getSendname() {
        return sendname;
    }

    public void setSendname(String sendname) {
        this.sendname = sendname;
    }

    public String getSendaddress() {
        return sendaddress;
    }

    public void setSendaddress(String sendaddress) {
        this.sendaddress = sendaddress;
    }

    public String getSendlat() {
        return sendlat;
    }

    public void setSendlat(String sendlat) {
        this.sendlat = sendlat;
    }

    public String getSendlng() {
        return sendlng;
    }

    public void setSendlng(String sendlng) {
        this.sendlng = sendlng;
    }

    public String getSendphone() {
        return sendphone;
    }

    public void setSendphone(String sendphone) {
        this.sendphone = sendphone;
    }

    @Override
    public String toString() {
        return "Sendadd{" +
                "sendname='" + sendname + '\'' +
                ", sendaddress='" + sendaddress + '\'' +
                ", sendlat='" + sendlat + '\'' +
                ", sendlng='" + sendlng + '\'' +
                ", sendphone='" + sendphone + '\'' +
                '}';
    }
}
