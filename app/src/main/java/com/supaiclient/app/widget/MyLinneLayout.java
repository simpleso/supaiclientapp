package com.supaiclient.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.supaiclient.app.util.L;

/**
 * Created by Administrator on 2016/4/22.
 */
public class MyLinneLayout extends LinearLayout {


    private InputWindowListener listener;

    public MyLinneLayout(Context context) {
        super(context);
    }

    public MyLinneLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinneLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (oldh > h) {
            L.d("input window show");
            if (listener != null)
                listener.show();
        } else {
            L.d("input window hidden");
            if (listener != null)
                listener.hidden();
        }
    }

    public void setListener(InputWindowListener listener) {
        this.listener = listener;
    }
}
