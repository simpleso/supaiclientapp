package com.supaiclient.app.bean;

/**
 * Created by Administrator on 2016/3/6.
 */
public class LocationBean {

    private double cplat;
    private double cplng;

    public double getCplat() {
        return cplat;
    }

    public void setCplat(double cplat) {
        this.cplat = cplat;
    }

    public double getCplng() {
        return cplng;
    }

    public void setCplng(double cplng) {
        this.cplng = cplng;
    }

    @Override
    public String toString() {
        return "LocationBean{" +
                "cplat=" + cplat +
                ", cplng=" + cplng +
                '}';
    }
}
