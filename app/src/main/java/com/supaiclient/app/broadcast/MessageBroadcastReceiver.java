package com.supaiclient.app.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.supaiclient.app.R;
import com.supaiclient.app.api.UserApi;
import com.supaiclient.app.bean.AdvertBean;
import com.supaiclient.app.bean.PushMessage;
import com.supaiclient.app.bean.SimpleBackPage;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.activity.home.MainActivity;
import com.supaiclient.app.ui.base.SimpleBackActivity;
import com.supaiclient.app.util.JSonUtils;
import com.supaiclient.app.util.L;

import org.json.JSONObject;

/**
 * 消息推送
 * Created by Administrator on 2016/5/5.
 */
public class MessageBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "MessageReceiver";
    private Context mContext;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 11: {


                }
                break;

                case 22: {
                    PushMessage message = (PushMessage) msg.obj;
                    getNotification(mContext, message);
                    mContext.sendBroadcast(new Intent(MainActivity.ACTION_MEG).putExtra("XX", true));
                }
                break;
                case 33: {
//                    AdvertDao advertDao = new AdvertDao(mContext);
//                    advertDao.addAdvert((AdvertBean) msg.obj);

                    //现在只需要图片的地址 简单存储

                    mContext.sendBroadcast(new Intent(MainActivity.ACTION_AD).putExtra("advertBean", (AdvertBean) msg.obj));
                }
                break;

                case 44: {

                    mContext.sendBroadcast(new Intent(MainActivity.ACTION_CGSTA));
                    //  LocalBroadcastManager.getInstance(BaseApplication.getContext()).sendBroadcast(new Intent(Main2Fragment.ACTION_CGF).putExtra("STA", true));
                }
                break;
            }
        }
    };

    public static void getNotification(Context context, PushMessage message) {
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder)
                new NotificationCompat.Builder(
                        context).setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("系统通知")
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentText(message.getData());

        mBuilder.setAutoCancel(true);//自己维护通知的消失
        Intent resultIntent = new Intent(context,
                SimpleBackActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        resultIntent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, SimpleBackPage.MessageFragment.getValue());

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        this.mContext = context;

        if (bundle == null) {
            return;
        }
        Log.e("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {

            case PushConsts.GET_CLIENTID:

                String cid = bundle.getString("clientid");
                // TODO:处理cid返回
                UserApi.clientnum(context, cid, new RequestBasetListener() {
                    @Override
                    public void onSuccess(String responseStr) {
                        L.d("推送添加服务端成功");
                    }

                    @Override
                    public void onFailure(int statusCode) {
                        L.d("推送添加服务端失败--数据库错误");
                    }

                    @Override
                    public void onSendError(int statusCode, String message) {
                        L.d("推送添加服务端成功--网络连接失败");
                    }
                });

                break;
            case PushConsts.GET_MSG_DATA:

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);

                L.e("第三方回执接口调用" + (result ? "成功" : "失败"));

                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {

                    String data = new String(payload);
                    // TODO:接收处理透传（payload）数据
                    L.e(data);
                    sendDate(mContext, data);
                }
                break;
            default:
                break;
        }
    }

    // 穿透 数据 过来了
    private void sendDate(final Context context, final String jsonstr) {

        Log.d(TAG, "sendDate-->>有穿透消息过来" + jsonstr);
        new Thread(new Runnable() {
            @Override
            public void run() {
                message(context, jsonstr);
            }
        }).start();
    }

    private void message(Context context, String jsonstr) {
        try {

            L.e(jsonstr);

            JSONObject jsonObject = new JSONObject(jsonstr);
            String sta = jsonObject.getString("sta");

            //消息
            if (sta.equals("XX")) {

                String s = jsonObject.getString("data");
                handler.obtainMessage(11, s).sendToTarget();

            } else if (sta.equals("MSG")) {

                int stime = jsonObject.getInt("stime");
                String data = jsonObject.getString("data");
                PushMessage message = new PushMessage();
                message.setStime(stime + "");
                message.setData(data);
                handler.obtainMessage(22, message).sendToTarget();

            } else if (sta.equals("AD")) {

                String data = jsonObject.getString("data");
                AdvertBean advertBean = JSonUtils.toBean(AdvertBean.class, data);
                handler.obtainMessage(33, advertBean).sendToTarget();
            } else if (sta.equals("CG")) {

                String data = jsonObject.getString("onumber");

                if (!TextUtils.isEmpty(data))
                    handler.obtainMessage(44, "").sendToTarget();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
