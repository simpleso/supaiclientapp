package com.supaiclient.app.model;

import android.content.Context;
import android.text.TextUtils;

import com.supaiclient.app.db.PropertyUtil;
import com.supaiclient.app.util.T;
import com.supaiclient.app.util.VerifyUtil;

/**
 * Created by Administrator on 2015/12/7.
 */
public class UserModel {


    public Context context;

    public UserModel(Context context) {
        this.context = context;
    }

    /**
     * 验证 账号 和 密码
     *
     * @param usernmae
     * @param passwrd
     * @return
     */
    public boolean loginVerify(String usernmae, String passwrd) {

        return !(!VerifyUtil.isPhone(usernmae) || !VerifyUtil.isPassword(passwrd, 0));
    }

    // 判断 是否 自动登录
    public boolean isAutoLogin() {

        String uinfo = PropertyUtil.getUinfo();
        return !TextUtils.isEmpty(uinfo);
    }

    /**
     * 注册 忘记密码 验证
     *
     * @param usernmae
     * @param code
     * @param passwrd
     * @param nextPassword
     * @param type         0:注册  1：忘记密码验证
     * @return
     */
    public boolean rightVerify(int type, String usernmae, String code, String passwrd, String nextPassword) {

        if (!getPassword(usernmae, code)) {
            return false;
        }
        return setPasswword(passwrd, nextPassword, type);

    }

    /**
     * 重置 验证
     *
     * @param usernmae
     * @param code
     * @param passwrd
     * @param nextPassword
     * @return
     */
    public boolean getPassword(String usernmae, String code, String passwrd, String nextPassword) {

        if (!getPassword(usernmae, code)) {
            return false;
        }
        return setPasswword(passwrd, nextPassword, 1);

    }

    /**
     * 密码验证
     *
     * @param usernmae
     * @param code
     * @return
     */
    public boolean getPassword(String usernmae, String code) {

        if (!VerifyUtil.isPhone(usernmae)) {
            return false;
        }
        return VerifyUtil.isCode(code);
    }

    /**
     * 设置密码验证
     *
     * @param passwrd
     * @param nextPassword
     * @return
     */
    public boolean setPasswword(String passwrd, String nextPassword, int type) {

        if (!VerifyUtil.isPassword(passwrd, type)) {
            return false;
        }
        if (TextUtils.isEmpty(nextPassword)) {
            if (type == 1) {
                T.s("请再次输入新密码");
            } else {
                T.s("请再次输入密码");
            }

            return false;
        }

        if (!passwrd.equals(nextPassword)) {
            T.s("两次密码输入不一致");
            return false;
        }
        return true;
    }


}
