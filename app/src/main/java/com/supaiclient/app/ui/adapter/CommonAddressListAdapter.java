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
 * FrequentContacts
 * Created by Administrator on 2015/12/29.
 * 历史 记录  适配
 */
public class CommonAddressListAdapter extends ListBaseAdapter<PeopleBean> {

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.listview_historyaddresslist, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        PeopleBean peopleBean = mDatas.get(position);
        vh.tvName.setText(peopleBean.getName());
        vh.tvAdd.setText(peopleBean.getAdd());
        vh.tvPhone.setText(peopleBean.getPhone());

        return convertView;
    }

    class ViewHolder {

        @Bind(R.id.tv_add)
        TextView tvAdd;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_phone)
        TextView tvPhone;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
