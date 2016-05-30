package com.supaiclient.app.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.supaiclient.app.bean.AdvertBean;

/**
 * 广告帮助类
 * Created by Administrator on 2016/5/9.
 */
public class AdvertHelper extends OrmLiteSqliteOpenHelper {


    private static final String TABLE_NAME = "sqlite-advert.db";
    private static AdvertHelper instance;
    private Dao<AdvertBean, Integer> bannerDao;

    private AdvertHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized AdvertHelper getHelper(Context context) {
        if (instance == null) {
            synchronized (AdvertBean.class) {
                if (instance == null)
                    instance = new AdvertHelper(context);
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database,
                         ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, AdvertBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, AdvertBean.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<AdvertBean, Integer> getAdvertDao() throws SQLException {
        if (bannerDao == null) {
            try {
                bannerDao = getDao(AdvertBean.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return bannerDao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
        bannerDao = null;
    }
}
