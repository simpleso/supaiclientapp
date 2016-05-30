package com.supaiclient.app.bean;

/**
 * 推送消息
 * Created by Administrator on 2016/5/6.
 */
public class PushMessage {

    private String stime;
    private String data;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    @Override
    public String toString() {
        return "PushMessage{" +
                "stime='" + stime + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
