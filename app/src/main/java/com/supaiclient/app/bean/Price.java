package com.supaiclient.app.bean;

/**
 * 服务费
 * Created by Administrator on 2016/6/2.
 */

public class Price {

    /**
     * name : 鲜花
     * price : 5
     */
    private String name;
    private int price;

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

    @Override
    public String toString() {
        return "Price{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
