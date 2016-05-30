package com.supaiclient.app.db;

import com.supaiclient.app.BaseApplication;

/**
 * Created by Administrator on 2015/12/29.
 */
public class PropertyUtil {

    // 得到 唯一码
    public final static String getUinfo() {
        return BaseApplication.get("uinfo", "");
    }

    // 保存 唯一码
    public final static void setUinfo(String uinfo) {
        BaseApplication.set("uinfo", uinfo);
    }

    public final static String getPhone() {
        return BaseApplication.get("phone", "");
    }

    // 保存 上次登录账号
    public final static void setPhone(String phone) {
        BaseApplication.set("phone", phone);
    }

    public static void setGuide(int v) {
        BaseApplication.set("isNewGuide", v);
    }

    public final static int getIsGuide() {
        return BaseApplication.get("isNewGuide", 0);
    }

}
