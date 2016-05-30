package com.supaiclient.app.util;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.supaiclient.app.BaseApplication;
import com.supaiclient.app.R;

/**
 * Created by Administrator on 2015/12/7.
 */
public class T {

    public static void s(int message) {
        s(message, Toast.LENGTH_LONG, 0);
    }

    public static void s(String message) {
        s(message, Toast.LENGTH_LONG, 0, Gravity.BOTTOM);
    }

    public static void s(int message, int icon) {
        s(message, Toast.LENGTH_LONG, icon);
    }

    public static void s(String message, int icon) {
        s(message, Toast.LENGTH_LONG, icon, Gravity.BOTTOM);
    }

    public static void ss(int message) {
        s(message, Toast.LENGTH_SHORT, 0);
    }

    public static void ss(String message) {
        s(message, Toast.LENGTH_SHORT, 0, Gravity.BOTTOM);
    }

    public static void ss(int message, Object... args) {
        s(message, Toast.LENGTH_SHORT, 0, Gravity.BOTTOM, args);
    }

    public static void s(int message, int duration, int icon) {
        s(message, duration, icon, Gravity.BOTTOM);
    }

    public static void s(int message, int duration, int icon,
                         int gravity) {
        s(BaseApplication.getContext().getString(message), duration, icon, gravity);
    }

    public static void s(int message, int duration, int icon,
                         int gravity, Object... args) {
        s(BaseApplication.getContext().getString(message, args), duration, icon, gravity);
    }

    public static void s(String message, int duration, int icon,
                         int gravity) {
        if (message != null && !message.equalsIgnoreCase("")) {
            long time = System.currentTimeMillis();
            if (!message.equalsIgnoreCase(BaseApplication.lastToast)
                    || Math.abs(time - BaseApplication.lastToastTime) > 2000) {
                View view = LayoutInflater.from(BaseApplication.getContext()).inflate(
                        R.layout.view_toast, null);
                ((TextView) view.findViewById(R.id.title_tv)).setText(message);
                if (icon != 0) {
                    ((ImageView) view.findViewById(R.id.icon_iv))
                            .setImageResource(icon);
                    view.findViewById(R.id.icon_iv)
                            .setVisibility(View.VISIBLE);
                }
                Toast toast = new Toast(BaseApplication.getContext());
                toast.setView(view);
                if (gravity == Gravity.CENTER) {
                    toast.setGravity(gravity, 0, 0);
                } else {
                    toast.setGravity(gravity, 0, 35);
                }

                toast.setDuration(duration);
                toast.show();
                BaseApplication.lastToast = message;
                BaseApplication.lastToastTime = System.currentTimeMillis();
            }
        }
    }


    // 订单 支付 成功 调用
    public static void sOrderOver() {

        View view = LayoutInflater.from(BaseApplication.getContext()).inflate(
                R.layout.view_orderover_toast, null);
        Toast toast = new Toast(BaseApplication.getContext());
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

}
