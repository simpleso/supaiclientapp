package com.supaiclient.app.interf;

import com.amap.api.location.AMapLocation;

/**
 * Created by Administrator on 2015/12/25.
 * 定位 完毕 监听
 */
public interface LocationUtilLinstenner {

    void onLocationChanged(AMapLocation location);
}
