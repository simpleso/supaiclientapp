package com.supaiclient.app.bean;

/**
 * Created by Administrator on 2016/1/12.
 * 联系人
 */
public class SenduserBean extends BaseResponseBodyBean {

//    jid	int	1	寄件人id
//    jname	string	tantan	寄件人姓名
//    jphone	string	18725648509	电话号码

    private int jid;
    private String jname;
    private String jphone;

    private String suname;//": "您老",
    private String suphoto;//": "http://192.168.1.200/sendspcr/public/upload/userdata/b25b7306801ec9cf7dcc1b4f9e010fe8.jpg",
    private String suphone;//": "18723182922"

    public int getJid() {
        return jid;
    }

    public void setJid(int jid) {
        this.jid = jid;
    }

    public String getJname() {
        return jname;
    }

    public void setJname(String jname) {
        this.jname = jname;
    }

    public String getJphone() {
        return jphone;
    }

    public void setJphone(String jphone) {
        this.jphone = jphone;
    }

    public String getSuname() {
        return suname;
    }

    public void setSuname(String suname) {
        this.suname = suname;
    }

    public String getSuphoto() {
        return suphoto;
    }

    public void setSuphoto(String suphoto) {
        this.suphoto = suphoto;
    }

    public String getSuphone() {
        return suphone;
    }

    public void setSuphone(String suphone) {
        this.suphone = suphone;
    }

    @Override
    public String toString() {
        return "SenduserBean{" +
                "jid=" + jid +
                ", jname='" + jname + '\'' +
                ", jphone='" + jphone + '\'' +
                ", suname='" + suname + '\'' +
                ", suphoto='" + suphoto + '\'' +
                ", suphone='" + suphone + '\'' +
                '}';
    }
}
