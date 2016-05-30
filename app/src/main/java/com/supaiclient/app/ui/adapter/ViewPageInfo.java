package com.supaiclient.app.ui.adapter;

import android.os.Bundle;

/**
 * Created by zgc on 2015/10/8.
 */
public final class ViewPageInfo {

    public final String tag;
    public final Class<?> clss;
    public final Bundle args;
    public final String title;

    public ViewPageInfo(String _title, String _tag, Class<?> _class, Bundle _args) {
        title = _title;
        tag = _tag;
        clss = _class;
        args = _args;
    }
}