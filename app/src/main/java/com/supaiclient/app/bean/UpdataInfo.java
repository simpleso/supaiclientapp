package com.supaiclient.app.bean;

/**
 * 下载最新版本
 * Created by Administrator on 2016/5/13.
 */
public class UpdataInfo extends BaseResponseBodyBean {

//    detail string 修改了bug 更新详情
//    version string 2.1.5 最新版本
//    force int 1 是否强制更新 0为不强制 1为强制
//    url string http://baidu.com 更新的app地址

    private String detail;
    private int version;
    private int force;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getForce() {
        return force;
    }

    public void setForce(int force) {
        this.force = force;
    }

    @Override
    public String toString() {
        return "UpdataInfo{" +
                "detail='" + detail + '\'' +
                ", version=" + version +
                ", force=" + force +
                ", url='" + url + '\'' +
                '}';
    }
}
