package com.supaiclient.app.ui.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.supaiclient.app.R;
import com.supaiclient.app.bean.InvoiceBean;
import com.supaiclient.app.ui.adapter.base.ListBaseAdapter;
import com.supaiclient.app.util.DateUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/12/11.
 * 交易 记录
 */
public class TransactionListAdapter extends ListBaseAdapter<InvoiceBean> {


    Map<String, String> map = new HashMap<>();


    @Override
    public void clear() {
        super.clear();
        map.clear();
    }

    @SuppressLint("InflateParams")
    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.listview_tran, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        InvoiceBean news = mDatas.get(position);

        String yueTime = DateUtils.showIndexTime(news.getAddtime());

        if (map.get(yueTime) == null) {
            vh.tvYue.setVisibility(View.VISIBLE);
            vh.line.setVisibility(View.VISIBLE);
            vh.tvYue.setText(yueTime);
            map.put(yueTime, yueTime);
        } else {
            vh.tvYue.setVisibility(View.GONE);
            vh.line.setVisibility(View.GONE);
        }
        String time = DateUtils.timestampToDate(news.getAddtime());
        vh.tvTime.setText(time);
        vh.tvMoney.setText("￥" + news.getMoney());

        return convertView;
    }


    class ViewHolder {
        @Bind(R.id.tv_yue)
        TextView tvYue;
        @Bind(R.id.line)
        View line;
        @Bind(R.id.tv_ordershouru)
        TextView tvOrdershouru;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_money)
        TextView tvMoney;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
