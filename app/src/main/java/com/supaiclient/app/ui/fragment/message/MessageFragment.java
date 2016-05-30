package com.supaiclient.app.ui.fragment.message;


import android.content.Intent;
import android.text.TextUtils;

import com.supaiclient.app.api.UserApi;
import com.supaiclient.app.bean.MessageBean;
import com.supaiclient.app.interf.OnBack;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.activity.home.MainActivity;
import com.supaiclient.app.ui.adapter.MessageAdapter;
import com.supaiclient.app.ui.adapter.base.ListBaseAdapter;
import com.supaiclient.app.ui.base.BaseListFragment;
import com.supaiclient.app.util.JSonUtils;
import com.supaiclient.app.util.L;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息页面
 * Created by Administrator on 2016/5/5.
 */

public class MessageFragment extends BaseListFragment<MessageBean> implements BaseListFragment.OnLodingFinsh<MessageBean> {

    @Override
    protected ListBaseAdapter<MessageBean> getListAdapter() {

        return new MessageAdapter(new OnBack() {
            @Override
            public void onBack() {
                sendRequestData();
            }
        });
    }

    @Override
    protected void sendRequestData() {

        setOnLodingFinsh(this);
        UserApi.getMessageList(getActivity(), mCurrentPage * getPageSize(), getPageSize(), requestBasetListener);
    }

    @Override
    protected ArrayList<MessageBean> parseList(String json) throws Exception {

//    gmid int 1 消息id
//    gmcontent string 这是消息内容 消息内容
//    url string baidu.com 外链地址
//    urlname string 查看详情 外链地址名称
//    isread int 1 是否已读，0表示未读，1表示已读
//    addtime int 1462446385 消息添加时间

        return (ArrayList<MessageBean>) JSonUtils.toList(MessageBean.class, json);
    }

   /* @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        MessageBean mMessageBean = (MessageBean) adapterView.getAdapter().getItem(i);

        if ((mMessageBean != null) && (TextUtils.isEmpty(mMessageBean.getUrl()))) {

            UserApi.messageRead(getActivity(), mMessageBean.getGmid() + "", new RequestBasetListener() {
                @Override
                public void onSuccess(String responseStr) {

                    L.e("消息已读成功");
                }

                @Override
                public void onFailure(int statusCode) {

                }

                @Override
                public void onSendError(int statusCode, String message) {

                }
            });
        }
}*/


    @Override
    public void onResume() {
        super.onResume();
        this.requestData(true);
    }

    @Override
    public void finish(List<MessageBean> data) {

        List<MessageBean> messageBean = data;

        StringBuilder mStringBuilder = new StringBuilder();

        getActivity().sendBroadcast(new Intent(MainActivity.ACTION_MEG).putExtra("XX", false));

        for (int i = 0; i < messageBean.size(); i++) {

            //L.e(messageBean.toString());
            MessageBean mMessageBean = messageBean.get(i);

            if (mMessageBean.getIsread() == 0) {
                mStringBuilder.append(mMessageBean.getGmid() + ",");
            }
        }

        if (!TextUtils.isEmpty(mStringBuilder.toString())) {

            UserApi.messageRead(getActivity(), mStringBuilder.substring(0, mStringBuilder.length() - 1).toString() + "", new RequestBasetListener() {
                @Override
                public void onSuccess(String responseStr) {

                    L.d("消息已读成功");
                }

                @Override
                public void onFailure(int statusCode) {

                }

                @Override
                public void onSendError(int statusCode, String message) {

                }
            });
        }
    }
}


