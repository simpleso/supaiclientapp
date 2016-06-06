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
import com.supaiclient.app.api.ApiHttpClient;
import com.supaiclient.app.api.UrlUtil;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.base.BaseFragment;
import com.supaiclient.app.util.T;


import org.kymjs.kjframe.http.HttpParams;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/2/22.
 * 发票 申请
 */
public class FapiaoAddFragment extends BaseFragment {


    @Bind(R.id.tv_left)
    ImageView tvLeft;
    @Bind(R.id.title_content_tv)
    TextView titleContentTv;
    @Bind(R.id.title_right_tv)
    TextView titleRightTv;
    @Bind(R.id.et_quname)
    TextView etQuname;
    @Bind(R.id.et_quphone)
    EditText etQuphone;
    @Bind(R.id.et_beizhu)
    EditText etBeizhu;
    @Bind(R.id.et_youjidiz)
    EditText etYoujidiz;
    @Bind(R.id.et_shoujianren)
    EditText etShoujianren;
    @Bind(R.id.et_jijianren)
    EditText etJijianren;
    @Bind(R.id.tv_shenqing)
    TextView tvShenqing;

    String youhui;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fapiao_add, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {

        titleContentTv.setText("发票申请");
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        if (getArguments() != null) {
            youhui = getArguments().getString("youhui");
        }

        tvShenqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String et_quphone = etQuphone.getText().toString();
                if (TextUtils.isEmpty(et_quphone)) {
                    T.s("请输入发票抬头");
                    return;
                }

                String et_beizhu = etBeizhu.getText().toString();
                if (TextUtils.isEmpty(et_beizhu)) {
                    T.s("请输入备注");
                    return;
                }

                String et_youjidiz = etYoujidiz.getText().toString();
                if (TextUtils.isEmpty(et_youjidiz)) {
                    T.s("请输入邮寄地址");
                    return;
                }

                String et_shoujianren = etShoujianren.getText().toString();
                if (TextUtils.isEmpty(et_shoujianren)) {
                    T.s("请输入收件人姓名");
                    return;
                }
                String et_jijianren = etJijianren.getText().toString();
                if (TextUtils.isEmpty(et_jijianren)) {
                    T.s("请输入收件人电话");
                    return;
                }

                showWaitDialog("申请中..");
                HttpParams params = new HttpParams();
                params.put("money", youhui);
                params.put("intt", et_quphone);
                params.put("inbz", et_beizhu);
                params.put("inadd", et_youjidiz);
                params.put("inname", et_shoujianren);
                params.put("inphone", et_jijianren);
                ApiHttpClient.post(getActivity(), UrlUtil.invoicesub, params, new RequestBasetListener() {
                    @Override
                    public void onSuccess(String responseStr) {

                        T.s("申请成功！");
                        hideWaitDialog();
                        getActivity().finish();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
