package com.supaiclient.app.ui.activity.order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.supaiclient.app.R;
import com.supaiclient.app.api.ApiHttpClient;
import com.supaiclient.app.api.UrlUtil;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.base.BaseActivity;
import com.supaiclient.app.ui.fragment.goods.MyOrderHistoryFragment;

import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpParams;

import butterknife.Bind;
import butterknife.BindColor;

/**
 * Created by Administrator on 2016/3/13.
 * 取消 订单
 */
public class CancelOrderActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_bianhao)
    TextView tvBianhao;
    @Bind(R.id.tv_biede)
    TextView tvBiede;
    @Bind(R.id.tv_taijiu)
    TextView tvTaijiu;
    @Bind(R.id.tv_taishuai)
    TextView tvTaishuai;
    @Bind(R.id.tv_chongxin)
    TextView tvChongxin;
    @Bind(R.id.content)
    EditText content;
    @Bind(R.id.btn_over)
    Button btnOver;
    @BindColor(R.color.white)
    int white;
    @BindColor(R.color.cancelorder)
    int cancelorder;
    private String yuany;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cancelorder;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        setActionBarTitle("取消订单");
        final String onumber = getIntent().getStringExtra("onumber");
        tvBianhao.setText("订单编号：" + onumber);

        tvBiede.setOnClickListener(this);
        tvTaijiu.setOnClickListener(this);
        tvTaishuai.setOnClickListener(this);
        tvChongxin.setOnClickListener(this);
        yuany = tvChongxin.getText().toString();
        content.setText(yuany);
        content.setSelection(yuany.length());

        btnOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String con = content.getText().toString();
                if (!TextUtils.isEmpty(con)) {
                    yuany = con;
                }

                showWaitDialog("提交中");
                HttpParams params = new HttpParams();
                params.put("onumber", onumber);
                params.put("rfcontent", yuany);
                ApiHttpClient.postNotShow(CancelOrderActivity.this, UrlUtil.orderqxorder, params, new RequestBasetListener() {
                    @Override
                    public void onSuccess(String responseStr) {

                        hideWaitDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(responseStr);

                            String explain = jsonObject.getString("explain");

                            if (!TextUtils.isEmpty(explain)) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(CancelOrderActivity.this);
                                builder.setMessage(explain);
                                builder.setTitle("温馨提示!");
                                builder.setIcon(R.mipmap.ic_launcher);
                                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(CancelOrderActivity.this, MyOrderHistoryFragment.class);
                                        intent.putExtra("back", "back");
                                        setResult(100, intent);
                                        CancelOrderActivity.this.finish();
                                    }
                                });
                                builder.create().show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                            Intent intent = new Intent(CancelOrderActivity.this, MyOrderHistoryFragment.class);
                            intent.putExtra("back", "back");
                            setResult(100, intent);
                            CancelOrderActivity.this.finish();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode) {

                        hideWaitDialog();
                        //      L.e(statusCode + "------");
                    }

                    @Override
                    public void onSendError(int statusCode, String message) {
                        hideWaitDialog();
                        //         L.e(statusCode + "------" + message);
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_biede:

                tvBiede.setTextColor(white);
                tvBiede.setBackgroundResource(R.drawable.sp_base_blue2);
                yuany = tvBiede.getText().toString();


                tvTaijiu.setTextColor(cancelorder);
                tvTaijiu.setBackgroundResource(R.drawable.sp_cancel_bg);
                tvTaishuai.setTextColor(cancelorder);
                tvTaishuai.setBackgroundResource(R.drawable.sp_cancel_bg);

                tvChongxin.setTextColor(cancelorder);
                tvChongxin.setBackgroundResource(R.drawable.sp_cancel_bg);
                break;
            case R.id.tv_taijiu:

                tvTaijiu.setTextColor(white);
                tvTaijiu.setBackgroundResource(R.drawable.sp_base_blue2);
                yuany = tvTaijiu.getText().toString();


                tvBiede.setTextColor(cancelorder);
                tvBiede.setBackgroundResource(R.drawable.sp_cancel_bg);

                tvTaishuai.setTextColor(cancelorder);
                tvTaishuai.setBackgroundResource(R.drawable.sp_cancel_bg);

                tvChongxin.setTextColor(cancelorder);
                tvChongxin.setBackgroundResource(R.drawable.sp_cancel_bg);
                break;
            case R.id.tv_taishuai:
                tvTaishuai.setTextColor(white);
                tvTaishuai.setBackgroundResource(R.drawable.sp_base_blue2);
                yuany = tvTaishuai.getText().toString();

                tvBiede.setTextColor(cancelorder);
                tvBiede.setBackgroundResource(R.drawable.sp_cancel_bg);

                tvTaijiu.setTextColor(cancelorder);
                tvTaijiu.setBackgroundResource(R.drawable.sp_cancel_bg);

                tvChongxin.setTextColor(cancelorder);
                tvChongxin.setBackgroundResource(R.drawable.sp_cancel_bg);
                break;
            case R.id.tv_chongxin:
                tvChongxin.setTextColor(white);
                tvChongxin.setBackgroundResource(R.drawable.sp_base_blue2);
                yuany = tvChongxin.getText().toString();

                tvBiede.setTextColor(cancelorder);
                tvBiede.setBackgroundResource(R.drawable.sp_cancel_bg);

                tvTaishuai.setTextColor(cancelorder);
                tvTaishuai.setBackgroundResource(R.drawable.sp_cancel_bg);

                tvTaijiu.setTextColor(cancelorder);
                tvTaijiu.setBackgroundResource(R.drawable.sp_cancel_bg);
                break;
        }
        content.setText(yuany);
        content.setSelection(yuany.length());
    }
}
