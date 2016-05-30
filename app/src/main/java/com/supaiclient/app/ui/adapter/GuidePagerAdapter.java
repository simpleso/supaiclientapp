package com.supaiclient.app.ui.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.supaiclient.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zgc on 2015/10/15.
 * 引导 适配器
 */
public class GuidePagerAdapter extends PagerAdapter {

    private List<Integer> arrayList;

    private Activity context;
    private HashMap<Integer, View> hashmap;

    public GuidePagerAdapter(Activity context) {
        this.context = context;

        arrayList = new ArrayList<>();
        arrayList.add(R.mipmap.welcome2);
        arrayList.add(R.mipmap.welcome3);
        arrayList.add(R.mipmap.welcome4);

        hashmap = new HashMap<>();
    }

    @Override
    public int getCount() {
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View v, Object o) {
        return v == o;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {

        View view = hashmap.get(position);
        if (view != null) {
            ((ViewPager) container).removeView(view);
        }
    }

    @Override
    public Object instantiateItem(View container, final int position) {
        ImageView simpleDraweeView = new ImageView(context);

        int mipid = arrayList.get(position);

        simpleDraweeView.setImageResource(mipid);
        simpleDraweeView.setScaleType(ImageView.ScaleType.FIT_XY);

        ViewPager pViewPager = ((ViewPager) container);
        pViewPager.addView(simpleDraweeView);

        if (hashmap.get(position) == null) {
            hashmap.put(position, simpleDraweeView);
        }
        return simpleDraweeView;
    }
}
