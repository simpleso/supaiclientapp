package com.supaiclient.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.supaiclient.app.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;

/**
 * 评价的适配器
 * Created by Administrator on 2016/2/14.
 */
public class DiscussAdapter extends BaseAdapter {


    private Context context;

    private String[] str;
    private LayoutInflater mInflater;

    private Map<Integer, String> map = new HashMap<>();

    public DiscussAdapter(Context context) {
        this.context = context;
        str = context.getResources().getStringArray(R.array.pinl);
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return str.length;
    }

    @Override
    public Object getItem(int position) {
        return str[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = mInflater.inflate(
                    R.layout.gridview_diacuss, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        String name = str[position];
        vh.tv_name.setText(name);

        if (map.get(position) == null) {

            vh.tv_name.setTextColor(vh.nor);
            vh.tv_name.setBackgroundResource(R.drawable.sp_pinlun);
        } else {
            vh.tv_name.setTextColor(vh.per);
            vh.tv_name.setBackgroundResource(R.drawable.sp_pinlun_per);
        }
        vh.tv_name.setTag(name);
        vh.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (map.get(position) == null) {
                    map.put(position, v.getTag().toString());
                } else {
                    map.remove(position);
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    class ViewHolder {

        @Bind(R.id.tv_name)
        TextView tv_name;
        @BindColor(android.R.color.white)
        int per;
        @BindColor(R.color.pingjianor)
        int nor;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
