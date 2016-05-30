package com.supaiclient.app.bean;

/**
 * Created by Administrator on 2015/12/28.
 * 人对象
 */
public class PeopleBean extends BaseResponseBodyBean {

    private String name; // 人 名字
    private String phone;// 手机号
    private String first;// 首字母
    private String lat;  // 经纬度
    private String lng;  // 经纬度
    private String add;  // 地址

    public PeopleBean() {
        super();
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    @Override
    public String toString() {
        return "PeopleBean{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", first='" + first + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", add='" + add + '\'' +
                '}';
    }
}
