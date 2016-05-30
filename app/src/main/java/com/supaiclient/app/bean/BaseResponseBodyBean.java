package com.supaiclient.app.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/7.
 */
public class BaseResponseBodyBean implements Serializable {


//    sta	string	suc	suc表示成功，fail表示失败，nologin表示需要登录(可以直接控制程序跳转到登录)
//    msg	string	成功	返回结果说明文字

    private String sta;
    private String msg;

    public BaseResponseBodyBean(String sta, String msg) {
        this.sta = sta;
        this.msg = msg;
    }

    public BaseResponseBodyBean() {
        super();
    }

    public String getSta() {
        return sta;
    }

    public void setSta(String sta) {
        this.sta = sta;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BaseResponseBodyBean{" +
                "sta='" + sta + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
