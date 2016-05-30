package com.supaiclient.app.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/13.
 */
public class Takeadd implements Serializable {

    private String tname;//": "炼陈",
    private String tphone;//": "13996249618",
    private String tadd;//": "轨道交通1号线",
    private String tlat;//": 29.562706,
    private String tlng;//": 106.466728

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTphone() {
        return tphone;
    }

    public void setTphone(String tphone) {
        this.tphone = tphone;
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

    @Override
    public String toString() {
        return "Takeadd{" +
                "tname='" + tname + '\'' +
                ", tphone='" + tphone + '\'' +
                ", tadd='" + tadd + '\'' +
                ", tlat='" + tlat + '\'' +
                ", tlng='" + tlng + '\'' +
                '}';
    }
}
