package com.supaiclient.app.ui.fragment.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.supaiclient.app.R;
import com.supaiclient.app.api.UserApi;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.base.BaseFragment;
import com.supaiclient.app.util.T;
import com.supaiclient.app.util.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/2/22.
 * 发票 首页
 */
public class FapiaoFragment extends BaseFragment {

    @Bind(R.id.tv_left)
    ImageView tvLeft;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.title_right_tv)
    TextView titleRightTv;
    @Bind(R.id.et_input)
    EditText etInput;
    @Bind(R.id.tv_shenqing)
    TextView tvShenqing;
    @Bind(R.id.tv_modey)
    TextView tvModey;

    String model = "0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fapiao, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        UserApi.userGetinfo(getActivity(), new RequestBasetListener() {
            @Override
            public void onSuccess(String responseStr) {
                try {

                    JSONObject jsonObject = new JSONObject(responseStr);
                    model = jsonObject.getString("gutotalmoney");
                    tvModey.setText("" + model);
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
    }

    private void init() {

        //不弹出输入法
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        titleContentTv.setText("发票");
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        titleRightTv.setText("申请记录");

        if (getArguments() != null) {
            model = getArguments().getString("youhui");
            tvModey.setText("" + model);
            //BaseApplication.set("youhui",model);
        }

        titleRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.TransactionList(getActivity());
            }
        });
        tvShenqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 发票 申请

                String con = etInput.getText().toString().trim();
                if (TextUtils.isEmpty(con)) {

                    T.s("请输入申请发票余额");
                    return;
                }
                if (TextUtils.isEmpty(model)) {

                    T.s("可用余额不足");
                    return;
                }

                try {
                    double dou1 = Double.parseDouble(model);
                    double dou2 = Double.parseDouble(con);

                    if (dou2 > dou1) {

                        T.s("申请余额过高");
                        return;
                    }

                    if (dou2 < 100) {
                        T.s("暂不支持100元以下发票申请");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    T.s("请输入正确的余额");
                    return;
                }

                etInput.setText("");
                UIHelper.shenqing(getActivity(), con);

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
