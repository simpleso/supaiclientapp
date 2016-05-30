package com.supaiclient.app.bean;

/**
 * Created by Administrator on 2015/12/28.
 */
public class TestBean extends BaseResponseBodyBean {

    private String name;

    public TestBean(String msg) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "name='" + name + '\'' +
                '}';
    }
}
