package com.supaiclient.app.api;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.util.L;


import org.kymjs.kjframe.http.HttpParams;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by zgc on 2015/9/23.
 * 用户API
 */
public class UserApi {

    /**
     * 用户 登录
     *
     * @param username
     * @param password
     */
    public static final void login(Context context, String username, String password, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        params.put("username", username);
        params.put("pwd", password);
        ApiHttpClient.post(context, UrlUtil.LOGIN, params, requestBasetListener);
    }


    /**
     * 获取验证码
     *
     * @param context
     * @param username
     * @param requestBasetListener
     */
    public static final void getRightCode(Context context, String username, int have, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        params.put("phone", username);
        params.put("have", have + "");
        ApiHttpClient.post(context, UrlUtil.sendmsg, params, requestBasetListener);
    }

    /**
     * 快捷 登录 获取验证码
     *
     * @param context
     * @param username
     * @param requestBasetListener
     */
    public static final void orderSendmsg(Context context, String username, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        params.put("phone", username);
        ApiHttpClient.post(context, UrlUtil.ordersendmsg, params, requestBasetListener);
    }

    /**
     * 快捷登录
     *
     * @param context
     * @param username
     * @param requestBasetListener
     */
    public static final void sendFastlogin(Context context, String username, String code, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        params.put("phone", username);
        params.put("code", code);
        ApiHttpClient.post(context, UrlUtil.fastlogin, params, requestBasetListener);
    }

    /**
     * 注册
     *
     * @param context
     * @param code
     * @param passwrd
     * @param requestBasetListener
     */
    public static final void register(Context context, String username, String code, String passwrd, String invitecode, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        params.put("username", username);
        params.put("pwd", passwrd);
        params.put("code", code);
        params.put("invitecode", invitecode);
        ApiHttpClient.post(context, UrlUtil.register, params, requestBasetListener);
    }

    /**
     * 重置密码
     *
     * @param context
     * @param requestBasetListener
     */
    public static final void UPPass(Context context, String username, String code, String passwrd, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        params.put("phone", username);
        params.put("newpwd", passwrd);
        params.put("code", code);
        ApiHttpClient.post(context, UrlUtil.changepwd, params, requestBasetListener);
    }

    /**
     * 设置 新密码
     *
     * @param context
     * @param requestBasetListener
     */
    public static final void setPass(Context context, String password, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        params.put("username", password);
        ApiHttpClient.post(context, UrlUtil.LOGIN, params, requestBasetListener);
    }

    /**
     * 意见 反馈 提交
     *
     * @param context
     * @param message
     * @param requestBasetListener
     */
    public static final void viewAdd(Context context, String message, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        params.put("yjcontent", message);
        ApiHttpClient.post(context, UrlUtil.viewadd, params, requestBasetListener);
    }

    /**
     * 常见 问题
     *
     * @param context
     * @param requestBasetListener
     */
    public static final void problemlist(Context context, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        ApiHttpClient.post(context, UrlUtil.problemlist, params, requestBasetListener);
    }


    /**
     * 获取用户 资料
     */
    public static final void userGetinfo(Context context, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        ApiHttpClient.postNotShow(context, UrlUtil.usergetinfo, params, requestBasetListener);
    }

    /**
     * 添加寄件人
     */
    public static final void userAddSendUser(Context context, String name, String phone, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        params.put("jname", name);
        params.put("jphone", phone);
        ApiHttpClient.postNotShow(context, UrlUtil.senduseradd, params, requestBasetListener);
    }

    /**
     * 修改寄件人
     */
    public static final void userModifySendUser(Context context, int jid, String name, String phone, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        params.put("jid", jid + "");
        params.put("jname", name);
        params.put("jphone", phone);
        ApiHttpClient.postNotShow(context, UrlUtil.senduseradd, params, requestBasetListener);
    }

    /**
     * 删除寄件人
     */
    public static final void userDelSendUser(Context context, int jid, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        params.put("jid", jid + "");
        ApiHttpClient.postNotShow(context, UrlUtil.senduserdel, params, requestBasetListener);
    }

    /**
     * 获取寄件人列表
     */
    public static final void userListSendUser(Context context, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        ApiHttpClient.postNotShow(context, UrlUtil.senduserlist, params, requestBasetListener);
    }


    /**
     * 上传头像
     */

    public static final void upHeader(final Activity context, final File file, final RequestBasetListener requestBasetListener) {

        final HttpParams params = new HttpParams();

        //0425修改关于6.0系统的适配问题

        if (Build.VERSION.SDK_INT >= 23) {
            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionsResultAction() {
                @Override
                public void onGranted() {
                    try {
                        params.put("guphoto", file);
                    } catch (Exception e) {
                        e.printStackTrace();
                        L.e("文件不存在");
                    }
                    ApiHttpClient.postNotShow(context, UrlUtil.userheader, params, requestBasetListener);
                }

                @Override
                public void onDenied(String permission) {
                    L.e("权限未获取到:" + permission);
                }
            });

        } else {
            try {
                params.put("guphoto", file);
            } catch (Exception e) {
                e.printStackTrace();
                L.e("文件不存在");
            }
            ApiHttpClient.postNotShow(context, UrlUtil.userheader, params, requestBasetListener);
        }
    }


    public static final void ordersuinfo(Context context, String onumber, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        params.put("onumber", onumber);
        ApiHttpClient.post(context, UrlUtil.ordersuinfo, params, requestBasetListener);
    }

    public static final void replay(Context context, String onumber, String star, String content, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        params.put("onumber", onumber);
        params.put("star", star);
        params.put("content", content);
        params.put("rplab1", 1 + "");
        params.put("rplab2", 1 + "");
        params.put("rplab3", 1 + "");
        params.put("rplab4", 1 + "");
        params.put("rplab5", 1 + "");
        params.put("rplab6", 1 + "");
        params.put("rplab7", 1 + "");
        params.put("rplab8", 1 + "");
        params.put("rplab9", 1 + "");
        params.put("rplab10", 1 + "");
        params.put("rplab11", 1 + "");
        params.put("rplab12", 1 + "");
        ApiHttpClient.post(context, UrlUtil.replay, params, requestBasetListener);
    }


    /**
     * 获得邀请码
     */

    public static final void getInviteCode(Context context, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        ApiHttpClient.postNotShow(context, UrlUtil.invite, params, requestBasetListener);
    }

    /**
     * 获得积分项
     */
    public static final void getPointList(Context context, String gltype, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        params.put("gltype", gltype);
        ApiHttpClient.postNotShow(context, UrlUtil.pointlist, params, requestBasetListener);
    }

    /**
     * 获得积分项
     */
    public static final void getPointPnfo(Context context, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        ApiHttpClient.postNotShow(context, UrlUtil.pointinfo, params, requestBasetListener);
    }

    /**
     * 得到 消息 列表
     *
     * @param context
     * @param start
     * @param limit
     * @param requestBasetListener
     */
    public static final void getMessageList(Context context, int start, int limit, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        params.put("pagesize", limit + "");
        params.put("str", start + 1 + "");
        ApiHttpClient.postList(context, UrlUtil.messagelist, params, requestBasetListener);
    }

    /**
     * 获得消息列表
     */
    public static final void clientnum(Context context, String clientnum, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        params.put("clientnum", clientnum);
        ApiHttpClient.postNotShow(context, UrlUtil.clientnum, params, requestBasetListener);
    }

    /**
     * 标记已读消息
     */
    public static final void messageRead(Context context, String gmid, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        params.put("gmid", gmid);
        ApiHttpClient.postNotShow(context, UrlUtil.messageread, params, requestBasetListener);
    }

    /**
     * 获得最新版本
     */
    public static final void getversion(Context context, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        ApiHttpClient.postNotShow(context, UrlUtil.getversion, params, requestBasetListener);
    }


    /**
     * 获得最新版本
     */
    public static final void getodloca(Context context, String onumber, RequestBasetListener requestBasetListener) {

        HttpParams params = new HttpParams();
        params.put("onumber", onumber);
        ApiHttpClient.postNotShow(context, UrlUtil.odloca, params, requestBasetListener);
    }

}
