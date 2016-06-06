package com.supaiclient.app.bean;

/**
 * 服务费
 * Created by Administrator on 2016/6/2.
 */

public class Price {

    /**
     * name : 蛋糕
     * price : 5
     * describe 蛋糕类物品需要支付5元服务费
     */
    private String name;
    private int price;
    private String describe;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public String toString() {
        return "Price{" +
                "name='" + name + '\'' +
                ", describe='" + describe + '\'' +
                ", price=" + price +
                '}';
    }
}
