package com.supaiclient.app.ui.fragment.user;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.supaiclient.app.R;
import com.supaiclient.app.api.UserApi;
import com.supaiclient.app.bean.ProblemBean;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.base.BaseFragment;
import com.supaiclient.app.util.JSonUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/2/2.
 * 常见问题
 */
public class CommonQuestionFragment extends BaseFragment {


    @Bind(R.id.expand)
    ExpandableListView expand;
    ExpandableListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_commonquestion;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);

        load();
        return view;
    }

    private void setItemChecked(int groupPosition, int childPosition) {
        if (expand == null) {
            return;
        }
        int numberOfGroupThatIsOpened = 0;
        for (int i = 0; i < groupPosition; i++) {
            if (expand.isGroupExpanded(i)) {
                numberOfGroupThatIsOpened += adapter.getChildrenCount(i);
            }
        }
        int position = numberOfGroupThatIsOpened + groupPosition
                + childPosition + 1;
        if (!expand.isItemChecked(position)) {
            expand.setItemChecked(position, true);
        }
    }


    private void load() {

        expand.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                setItemChecked(groupPosition, childPosition);
                return true;
            }
        });
        showWaitDialog("加载中...");

        UserApi.problemlist(getActivity(), new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {
                hideWaitDialog();


                ArrayList<ProblemBean> list = (ArrayList<ProblemBean>) JSonUtils.toList(ProblemBean.class, responseStr);


                int size = list.size();

                //群组名称
                String[] group = new String[size];
                //好友名称
                String[][] buddy = new String[size][1];

                int k = 0;
                for (ProblemBean li : list) {
                    group[k] = li.getPtitle();

//                    String s = Matcher.quoteReplacement(li.getPcontent());
                    String s = li.getPcontent();

                    s = s.replace("{", "");
                    s = s.replace("}", "");
                    s = s.replace("HH", "\n");

                    buddy[k][0] = s;
                    k++;
                }
                expand.setGroupIndicator(null);
                adapter = new BuddyAdapter(getContext(), group, buddy);
                expand.setAdapter(adapter);

                //分组展开
                expand.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    public void onGroupExpand(int groupPosition) {


                    }
                });
                //分组关闭
                expand.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                    public void onGroupCollapse(int groupPosition) {

                    }
                });

            }

            @Override
            public void onFailure(int statusCode) {
                hideWaitDialog();
            }

            @Override
            public void onSendError(int statusCode, String message) {

                hideWaitDialog();
            }
        });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class BuddyAdapter extends BaseExpandableListAdapter {

        LayoutInflater inflater;
        private String[] group;
        private String[][] buddy;
        private Context context;

        public BuddyAdapter(Context context, String[] group, String[][] buddy) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.group = group;
            this.buddy = buddy;
        }

        public Object getChild(int groupPosition, int childPosition) {
            return buddy[groupPosition][childPosition];
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public View getChildView(int groupPosition, int childPosition, boolean arg2, View convertView,
                                 ViewGroup arg4) {

            convertView = inflater.inflate(R.layout.common_02, null);
            TextView nickTextView = (TextView) convertView.findViewById(R.id.tv_content);

            //0420 不要转成HTML
//            nickTextView.setText(Html.fromHtml(getChild(groupPosition, childPosition).toString()));
            nickTextView.setText(getChild(groupPosition, childPosition).toString());
            return convertView;
        }

        public int getChildrenCount(int groupPosition) {
            return buddy[groupPosition].length;
        }

        public Object getGroup(int groupPosition) {
            return group[groupPosition];
        }

        public int getGroupCount() {
            return group.length;
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup arg3) {
            convertView = inflater.inflate(R.layout.common_01, null);

            convertView.findViewById(R.id.img_yuan).setVisibility(View.GONE);

            TextView groupNameTextView = (TextView) convertView.findViewById(R.id.tv_content);
            groupNameTextView.setText(Html.fromHtml(getGroup(groupPosition).toString()));
            return convertView;
        }

        public boolean hasStableIds() {
            return true;
        }

        // 子选项是否可以选择
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }
    }
}
