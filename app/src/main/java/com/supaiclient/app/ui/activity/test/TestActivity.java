package com.supaiclient.app.ui.activity.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.supaiclient.app.R;

/**
 * 测试视图
 * Created by Administrator on 2016/5/31.
 */

public class TestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.submitorder_chose);
    }
}
