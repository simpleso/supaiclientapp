package com.supaiclient.app.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * 广告实体
 * Created by Administrator on 2016/5/9.
 */
@DatabaseTable(tableName = "tab_advert")
public class AdvertBean implements Serializable {

//    sta string AD 消息类型
//    stime int 13245656784 推送消息的时间
//    bid int 1 广告id
//    bname string 广告名 广告名称
//    bphoto string http://192.168.1.200/sendspcr/public/upload/test.jpg banner图
//    burl string http://www.baidu.com url地址
//    outtime int 1324564567 结束时间

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "bid")
    private int bid;
    @DatabaseField(columnName = "bname")
    private String bname;
    @DatabaseField(columnName = "bphoto")
    private String bphoto;
    @DatabaseField(columnName = "burl")
    private String burl;
    @DatabaseField(columnName = "outtime")
    private int outtime;
    @DatabaseField(columnName = "btype")
    private String btype;

    public AdvertBean() {
    }

    public AdvertBean(int id, int bid, String bname, String bphoto, String burl, int outtime, String btype) {
        this.id = id;
        this.bid = bid;
        this.bname = bname;
        this.bphoto = bphoto;
        this.burl = burl;
        this.outtime = outtime;
        this.btype = btype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getBphoto() {
        return bphoto;
    }

    public void setBphoto(String bphoto) {
        this.bphoto = bphoto;
    }

    public String getBurl() {
        return burl;
    }

    public void setBurl(String burl) {
        this.burl = burl;
    }

    public int getOuttime() {
        return outtime;
    }

    public void setOuttime(int outtime) {
        this.outtime = outtime;
    }

    public String getBtype() {
        return btype;
    }

    public void setBtype(String btype) {
        this.btype = btype;
    }

    @Override
    public String toString() {
        return "AdvertBean{" +
                "id=" + id +
                ", bid=" + bid +
                ", bname='" + bname + '\'' +
                ", bphoto='" + bphoto + '\'' +
                ", burl='" + burl + '\'' +
                ", outtime=" + outtime +
                ", btype='" + btype + '\'' +
                '}';
    }
}
