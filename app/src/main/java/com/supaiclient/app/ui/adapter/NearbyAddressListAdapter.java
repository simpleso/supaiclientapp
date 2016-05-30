package com.supaiclient.app.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.supaiclient.app.R;
import com.supaiclient.app.bean.PeopleBean;
import com.supaiclient.app.ui.adapter.base.ListBaseAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/12/28.
 * 附近地址  适配器
 */
public class NearbyAddressListAdapter extends ListBaseAdapter<PeopleBean> {


    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.listview_nearbyaddresslist, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        PeopleBean peopleBean = mDatas.get(position);
        vh.tv_name.setText(peopleBean.getAdd());
        return convertView;
    }

    class ViewHolder {

        @Bind(R.id.tv_name)
        TextView tv_name;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
