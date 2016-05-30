package com.supaiclient.app.db;

import android.content.Context;
import android.database.SQLException;

import com.supaiclient.app.bean.AdvertBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单 数据库控制
 */
public class AdvertDao {

    private Context context;

    public AdvertDao(Context context) {
        this.context = context;
    }

    // 添加 订单 数据
    public synchronized void addAdvert(AdvertBean order) {

        if (order == null) {
            return;
        }
        AdvertHelper helper = AdvertHelper.getHelper(context);
        try {
            helper.getAdvertDao().create(order);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 得到 数据库 保存的 列表
     *
     * @return
     */
    public synchronized List<AdvertBean> getList() {

        AdvertHelper helper = AdvertHelper.getHelper(context);
        try {
            return helper.getAdvertDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    /**
     * 清空 所有 数据
     */
    public synchronized void deleteAll() {

        AdvertHelper helper = AdvertHelper.getHelper(context);
        try {
            helper.getAdvertDao().delete(getList());

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
}
