package com.supaiclient.app.bean;

/**
 * Created by Administrator on 2016/5/3.
 */
public class IntegralBean {

    //    "gltype":"支出",
    //    "glname":"系统发放",
    //    "points": 20 ,
    //    "glonum":"AB1256145645614",
    //    "addtime":"156131646",

    private String gltype;
    private String glname;
    private String glonum;
    private String addtime;
    private String points;


    public String getGltype() {
        return gltype;
    }

    public void setGltype(String gltype) {
        this.gltype = gltype;
    }

    public String getGlname() {
        return glname;
    }

    public void setGlname(String glname) {
        this.glname = glname;
    }

    public String getGlonum() {
        return glonum;
    }

    public void setGlonum(String glonum) {
        this.glonum = glonum;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "IntegralBean{" +
                "gltype='" + gltype + '\'' +
                ", glname='" + glname + '\'' +
                ", glonum='" + glonum + '\'' +
                ", addtime='" + addtime + '\'' +
                ", points='" + points + '\'' +
                '}';
    }
}
