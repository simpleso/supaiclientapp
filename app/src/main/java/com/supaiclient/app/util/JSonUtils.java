package com.supaiclient.app.util;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by zgc on 2015/10/12.
 * JSON 解析 公共方法
 */
public class JSonUtils {

    private final static String TAG = JSonUtils.class.getSimpleName();


    // 将 JSON 数据转换为 实体对象
    public static <T> T toBean(Class<T> type, String jsonstr) {

        if (TextUtils.isEmpty(jsonstr)) {
            return null;
        }
        return JSON.parseObject(jsonstr, type);
    }

    // 将 JSON 数据转换为 实体对象 列表

    public static <T> List<T> toList(Class<T> type, String jsonstr) {

        if (TextUtils.isEmpty(jsonstr)) {
            return null;
        }
        return JSON.parseArray(jsonstr, type);
    }

    // 将 JSON 数据转换为 实体对象
    public static String toJson(Object type) {

        if (type != null) {
            return JSON.toJSONString(type);
        } else {
            return null;
        }

    }

}
