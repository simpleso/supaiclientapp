package com.supaiclient.app.ui.fragment.goods;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.supaiclient.app.R;
import com.supaiclient.app.api.UserApi;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.adapter.DiscussAdapter;
import com.supaiclient.app.ui.base.BaseFragment;
import com.supaiclient.app.util.T;
import com.supaiclient.app.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJBitmap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/2/13.
 * 评价
 */
public class DiscussFragment extends BaseFragment {


    @Bind(R.id.tv_number)
    TextView tvNumber;
    @Bind(R.id.draweeview)
    CircleImageView draweeview;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.lin_xinji)
    LinearLayout linXinji;
    @Bind(R.id.lin_xinji2)
    LinearLayout linXinji2;
    @Bind(R.id.tv_sfxinj)
    TextView tvSfxinj;
    @Bind(R.id.tv_yij)
    TextView tvYij;
    @Bind(R.id.tv_yijdan)
    TextView tvYijdan;
    @Bind(R.id.gridview)
    GridView gridview;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.btn_over)
    Button btnOver;

    DiscussAdapter adapter;

    //默认改为5颗星
    private int item = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diascuss, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {

        final String number = getArguments().getString("number");
        tvNumber.setText("订单号：" + number);

        UserApi.ordersuinfo(getActivity(), number, new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {

//                suphone string url 快递员电话
//                suphoto string url 快递员头像
//                suname string AAL2118445693643 订单号
//                totalorder int 0 总完成订单
//                star int 3 快递员星级

                try {
                    JSONObject jsonObject = new JSONObject(responseStr);

                    //修复

                    KJBitmap kjBitmap = new KJBitmap();
                    kjBitmap.display(draweeview, jsonObject.getString("suphoto"));

                    // draweeview.setImageURI(Uri.parse(jsonObject.getString("suphoto")));

                    tvName.setText(jsonObject.getString("suname"));
                    tvYijdan.setText(jsonObject.getString("totalorder"));

                    float xinj = jsonObject.getInt("star");

                    for (int k = 0; k < 5; k++) {
                        ImageView ivyun = new ImageView(getActivity());
                        if (k < xinj) {
                            ivyun.setImageResource(R.mipmap.icon_star_white);
                        }
                         /*else if (k < xinj && k + 1 > xinj) {
                            ivyun.setImageResource(R.mipmap.icon_star_white_half);
                        }*/
                        else {
                            ivyun.setImageResource(R.mipmap.icon_star_white_bian);
                        }
                        ivyun.setPadding(2, 2, 2, 2);
                        RelativeLayout.LayoutParams margin = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        margin.setMargins(10, 0, 0, 0);
                        linXinji.addView(ivyun, margin);
                    }

                    tvSfxinj.setText((int) (xinj) + "分");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode) {

            }

            @Override
            public void onSendError(int statusCode, String message) {

            }
        });


        adapter = new DiscussAdapter(getActivity());
        gridview.setAdapter(adapter);

        initXInj();

        btnOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (item == 0) {

                    T.s("请为师傅打分吧");
                    return;
                }

                String content = etContent.getText().toString();

                if (TextUtils.isEmpty(content)) {

                    //  T.s("请为师傅说点什么吧");
                    content = "";
                }

                showWaitDialog("正在提交");
                UserApi.replay(getContext(), number, item + "", content, new RequestBasetListener() {
                    @Override
                    public void onSuccess(String responseStr) {

                        getActivity().finish();
                        hideWaitDialog();
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
        });
    }


    private void initXInj() {

        linXinji2.removeAllViews();
        for (int k = 1; k <= 5; k++) {
            ImageView ivyun = new ImageView(getActivity());
            if (k <= item) {
                ivyun.setImageResource(R.mipmap.icon_star_orange);
            } else {
                ivyun.setImageResource(R.mipmap.icon_star_orange_bian);
            }
            ivyun.setPadding(10, 10, 10, 10);
            RelativeLayout.LayoutParams margin = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
//            margin.setMargins(30, 0, 0, 0);
            ivyun.setTag(k);
            linXinji2.addView(ivyun, margin);

            ivyun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int item2 = (int) v.getTag();
                    if (item2 <= item) {
                        if (item2 > 1)
                            item2--;
                    }
                    item = item2;
                    initXInj();
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
