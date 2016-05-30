package com.supaiclient.app.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * 积分的适配器
 * Created by Administrator on 2016/5/3.
 */
public class InregralPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mList;
    private List<String> mTitle;

    public InregralPagerAdapter(FragmentManager fm, List<Fragment> mList, List<String> mTitle) {
        super(fm);
        this.mList = mList;
        this.mTitle = mTitle;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle.get(position);
    }
}
