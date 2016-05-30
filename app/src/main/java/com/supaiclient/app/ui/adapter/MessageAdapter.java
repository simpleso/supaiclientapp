package com.supaiclient.app.ui.adapter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.supaiclient.app.R;
import com.supaiclient.app.bean.MessageBean;
import com.supaiclient.app.interf.OnBack;
import com.supaiclient.app.ui.adapter.base.ListBaseAdapter;
import com.supaiclient.app.util.DateUtils;
import com.supaiclient.app.util.UIHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 消息适配器
 * Created by zgc on 16/2/4.
 */
public class MessageAdapter extends ListBaseAdapter<MessageBean> {

    private OnBack onBack;

    public MessageAdapter(OnBack onBack) {
        this.onBack = onBack;
    }

    @Override
    protected View getRealView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_message, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final MessageBean mMessageBean = mDatas.get(position);

        //清除之前的数据
        vh.clear();

        vh.tv_datetime.setText(DateUtils.timeSimpleToDate(mMessageBean.getAddtime() + ""));
        vh.tv_message.setText(mMessageBean.getGmcontent() + "");
        if (!TextUtils.isEmpty(mMessageBean.getUrl())) {
            vh.tv_details.setVisibility(View.VISIBLE);
            vh.tv_details.setText(mMessageBean.getUrlname() + "");
            vh.tv_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("MessageBean", mMessageBean);
                    UIHelper.openWebView(context, mMessageBean.getUrlname(), bundle);
                }
            });
        } else {
            vh.tv_details.setVisibility(View.GONE);
        }
        return convertView;
    }


    class ViewHolder {

        @Bind(R.id.tv_datetime)
        TextView tv_datetime;

        @Bind(R.id.tv_message)
        TextView tv_message;

        @Bind(R.id.tv_details)
        TextView tv_details;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        void clear() {
            tv_datetime.setText("");
            tv_message.setText("");
            tv_details.setText("");
        }
    }

}
