package com.supaiclient.app.util;

import android.text.TextUtils;

/**
 * Created by Administrator on 2015/12/7.
 */
public class VerifyUtil {

    /**
     * 验证 手机号码格式
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {

        if (TextUtils.isEmpty(phone)) {

            T.s("手机号码不能够为空");
            return false;
        }

        if (phone.length() != 11) {

            T.s("手机号码不正确");
            return false;
        }
        return true;
    }

    /**
     * 验证 密码 是否正确
     *
     * @param password
     * @return
     */
    public static boolean isPassword(String password, int type) {

        if (TextUtils.isEmpty(password)) {

            if (type == 1) {
                T.s("请输入新密码");
            } else {
                T.s("密码不能够为空");
            }

            return false;
        }
        return true;
    }

    /**
     * 验证码 验证
     *
     * @param code
     * @return
     */
    public static boolean isCode(String code) {
        if (TextUtils.isEmpty(code)) {

            T.s("验证码不能够为空");
            return false;
        }

        if (code.length() != 6) {
            T.s("验证码输入有误");
            return false;
        }
        return true;
    }

}
