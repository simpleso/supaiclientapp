package com.supaiclient.app.ui.activity.user;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.supaiclient.app.BaseApplication;
import com.supaiclient.app.R;
import com.supaiclient.app.ui.base.BaseActivity;
import com.supaiclient.app.util.UiTool;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/12/11.
 * 关于 页面
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.tv_versions)
    TextView tvVersions;
    @Bind(R.id.lin_call)
    LinearLayout lin_call;
    @Bind(R.id.tv_versname)
    TextView tv_versname;
    @Bind(R.id.lin_versions)
    LinearLayout lin_versions;
    @Bind(R.id.tv_versnew)
    TextView tv_versnew;

    @Override
    protected boolean hasMessageRightButton() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        setActionBarTitle("关于我们");
        tvVersions.setText(BaseApplication.getVersionName());

        UmengUpdateAgent.setUpdateAutoPopup(false);

        if (Build.VERSION.SDK_INT >= 23) {

            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(AboutActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionsResultAction() {
                        @Override
                        public void onGranted() {

                            UmengUpdateAgent.update(AboutActivity.this);
                        }

                        @Override
                        public void onDenied(String permission) {

                        }
                    });
        } else {

            UmengUpdateAgent.update(this);
        }

        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {

                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update

                        tv_versname.setText("立即更新");
                        tv_versnew.setVisibility(View.VISIBLE);
                        break;
                    case UpdateStatus.No: // has no update
                        tv_versname.setText("最新版本");
                        tv_versnew.setVisibility(View.GONE);
                        lin_versions.setEnabled(false);

                        break;
                }
            }
        });

        setOnClick();
    }

    private void setOnClick() {

        lin_versions.setOnClickListener(this);
        lin_call.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lin_versions://检查更新
                if (Build.VERSION.SDK_INT >= 23) {
                    PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(AboutActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionsResultAction() {
                                @Override
                                public void onGranted() {
                                    UmengUpdateAgent.update(AboutActivity.this);
                                    UmengUpdateAgent.forceUpdate(AboutActivity.this);
                                }

                                @Override
                                public void onDenied(String permission) {

                                }
                            });
                } else {
                    UmengUpdateAgent.update(this);
                    UmengUpdateAgent.forceUpdate(this);
                }
                UmengUpdateAgent.setUpdateAutoPopup(true);
                break;

            case R.id.lin_call:

                UiTool.callKehur(this);

                break;
        }
    }
}
