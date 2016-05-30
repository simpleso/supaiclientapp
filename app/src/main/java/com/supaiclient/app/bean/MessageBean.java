package com.supaiclient.app.bean;

/**
 * 消息实体对象
 * Created by Administrator on 2016/5/5.
 */
public class MessageBean extends BaseResponseBodyBean {

//    gmid int 1 消息id
//    gmcontent string 这是消息内容 消息内容
//    url string baidu.com 外链地址
//    urlname string 查看详情 外链地址名称
//    isread int 1 是否已读，0表示未读，1表示已读
//    addtime int 16546496565 消息添加时间


    private int gmid;
    private String gmcontent;
    private String url;
    private String urlname;
    private int guid;
    private int isread;
    private String addtime;

    public int getGmid() {
        return gmid;
    }

    public void setGmid(int gmid) {
        this.gmid = gmid;
    }

    public String getGmcontent() {
        return gmcontent;
    }

    public void setGmcontent(String gmcontent) {
        this.gmcontent = gmcontent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlname() {
        return urlname;
    }

    public void setUrlname(String urlname) {
        this.urlname = urlname;
    }

    public int getGuid() {
        return guid;
    }

    public void setGuid(int guid) {
        this.guid = guid;
    }

    public int getIsread() {
        return isread;
    }

    public void setIsread(int isread) {
        this.isread = isread;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "gmid=" + gmid +
                ", gmcontent='" + gmcontent + '\'' +
                ", url='" + url + '\'' +
                ", urlname='" + urlname + '\'' +
                ", guid=" + guid +
                ", isread=" + isread +
                ", addtime='" + addtime + '\'' +
                '}';
    }
}
