package com.supaiclient.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/12/16.
 */
public class DateUtils {

    /**
     * unix时间戳转换为dateFormat
     *
     * @param beginDate
     * @return
     */
    public static String timestampToDate(String beginDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(new Date(Long.parseLong(beginDate + "000")));
        return sd;
    }


    /**
     * unix时间戳转换为dateFormat
     *
     * @param beginDate
     * @return
     */
    public static String timeSimpleToDate(String beginDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
        String sd = sdf.format(new Date(Long.parseLong(beginDate + "000")));
        return sd;
    }

    /**
     * 和 当前时间 对比 友好 显示
     *
     * @param beginDate
     * @return
     */
    public static String showIndexTime(String beginDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");


        String nianIndex = sdf2.format(new Date());

        String sendNian = sdf2.format(new Date(Long.parseLong(beginDate + "000")));

        // 判断是否 是 同年
        if (nianIndex.equals(sendNian)) {
            SimpleDateFormat sdf3 = new SimpleDateFormat("MM");
            String sendYue = sdf3.format(new Date(Long.parseLong(beginDate + "000")));
            return sendYue;
        } else {
            return sdf.format(new Date(Long.parseLong(beginDate + "000")));
        }
    }

    /**
     * 指定 时间 转换  时间错
     *
     * @param data
     * @return
     */
    public static long timeToDate(String data) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:");
        try {
            Date time = sdf.parse(data);
            return time.getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 判断 预约时间是否到点
     *
     * @param taketime
     * @return
     */
    public static boolean isTimeOrdeEnd(int taketime) {

        long indexTime = getIndexTimeMillis();
        L.d("当前时间戳：" + indexTime);
        return taketime <= indexTime;
    }

    /**
     * 获取当前 时间戳
     *
     * @return
     */
    public final static long getIndexTimeMillis() {

        return System.currentTimeMillis() / 1000L;
    }

}
