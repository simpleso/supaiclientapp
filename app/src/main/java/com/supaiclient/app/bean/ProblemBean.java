package com.supaiclient.app.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/2.
 * 常见  问题
 */
public class ProblemBean implements Serializable {

    private String ptitle;
    private String pcontent;

    public String getPtitle() {
        return ptitle;
    }

    public void setPtitle(String ptitle) {
        this.ptitle = ptitle;
    }

    public String getPcontent() {
        return pcontent;
    }

    public void setPcontent(String pcontent) {
        this.pcontent = pcontent;
    }

    @Override
    public String toString() {
        return "ProblemBean{" +
                "ptitle='" + ptitle + '\'' +
                ", pcontent='" + pcontent + '\'' +
                '}';
    }
}
