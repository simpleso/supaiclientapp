package com.supaiclient.app.ui.activity.user;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;

import com.supaiclient.app.R;
import com.supaiclient.app.api.UserApi;
import com.supaiclient.app.bean.SenduserBean;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.adapter.base.BaseAdapterHelper;
import com.supaiclient.app.ui.adapter.base.QuickAdapter;
import com.supaiclient.app.ui.base.BaseActivity;
import com.supaiclient.app.util.L;
import com.supaiclient.app.util.T;
import com.supaiclient.app.widget.SwipeMenu;
import com.supaiclient.app.widget.SwipeMenuCreator;
import com.supaiclient.app.widget.SwipeMenuItem;
import com.supaiclient.app.widget.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 联系人管理
 * Created by Administrator on 2016/4/8.
 */
public class SenderActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.listview_sender)
    SwipeMenuListView mListview_sender;
    private QuickAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sender;
    }

    @Override
    public boolean show_add() {
        return true;
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSenderList();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionBarTitle("联系人管理");

        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.mipmap.iconfont_xiao10);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        mListview_sender.setMenuCreator(mSwipeMenuCreator);
        mListview_sender.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(final int position, SwipeMenu menu, int index) {

                SenduserBean mSenduserBean = (SenduserBean) adapter.getItem(position);

                UserApi.userDelSendUser(SenderActivity.this, mSenduserBean.getJid(), new RequestBasetListener() {

                    @Override
                    public void onSuccess(String responseStr) {

                        if (adapter != null) {
                            T.s("刪除成功");
                            adapter.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode) {
                        T.s("刪除失败");
                    }

                    @Override
                    public void onSendError(int statusCode, String message) {
                        T.s("刪除成功");
                    }
                });
            }
        });
        mListview_sender.setOnItemClickListener(this);
        getSenderList();
    }

    private void getSenderList() {

        if (adapter != null) {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
        showWaitDialog("请稍后...");
        UserApi.userListSendUser(this, new RequestBasetListener() {

            @Override
            public void onSuccess(String responseStr) {

                try {
                    List<SenduserBean> mList = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(responseStr);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        SenduserBean mSenduserBean = new SenduserBean();
                        JSONObject mJSONObject = jsonArray.getJSONObject(i);
                        mSenduserBean.setJid(mJSONObject.getInt("jid"));
                        mSenduserBean.setJname(mJSONObject.getString("jname"));
                        mSenduserBean.setJphone(mJSONObject.getString("jphone"));
                        //L.e(mSenduserBean.toString());
                        mList.add(mSenduserBean);
                    }

                    mListview_sender.setAdapter(adapter = new QuickAdapter<SenduserBean>(SenderActivity.this, R.layout.list_sender, mList) {
                        @Override
                        protected void convert(BaseAdapterHelper helper, SenduserBean item) {
                            helper.setText(R.id.tv_name, item.getJname());
                            helper.setText(R.id.tv_phone, item.getJphone());
                        }
                    });

                } catch (Exception e) {
                    T.s("联系人列表为空");
                } finally {
                    hideWaitDialog();
                }

            }

            @Override
            public void onFailure(int statusCode) {
                T.s("联系人获取失败");
                hideWaitDialog();
            }

            @Override
            public void onSendError(int statusCode, String message) {
                T.s("联系人获取失败");
                hideWaitDialog();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        SenduserBean senduserBean = (SenduserBean) adapter.getItem(position);
        if (senduserBean != null) {
            //dosanthing
        }
    }
}
