package com.supaiclient.app.ui.adapter;

import android.text.TextUtils;
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
 * 电话薄
 */
public class PhoneBookLetterListAdapter extends ListBaseAdapter<PeopleBean> {


    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.listview_phonebookletterlist, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        PeopleBean peopleBean = mDatas.get(position);

        vh.firstChar.setText(peopleBean.getFirst());
        vh.tvName.setText(peopleBean.getName());
        vh.tvPhone.setText(peopleBean.getPhone());


        String firstletters = peopleBean.getFirst();

        if (TextUtils.isEmpty(firstletters)) {
            firstletters = "#";
        }
        String previewStr = (position - 1) >= 0 ? mDatas.get(position - 1).getFirst() : " ";

        if (TextUtils.isEmpty(previewStr)) {
            previewStr = "#";
        }
        if (!previewStr.equals(firstletters)) {

            vh.firstChar.setText(firstletters.toUpperCase());
            vh.firstChar.setVisibility(View.VISIBLE);
        } else {
            vh.firstChar.setVisibility(View.GONE);
        }


        return convertView;
    }

    class ViewHolder {

        @Bind(R.id.first_char)
        TextView firstChar;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_phone)
        TextView tvPhone;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
