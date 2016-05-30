package com.supaiclient.app.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.supaiclient.app.BaseApplication;
import com.supaiclient.app.R;
import com.supaiclient.app.api.UserApi;
import com.supaiclient.app.bean.UpdataInfo;
import com.supaiclient.app.download.DownloadService;
import com.supaiclient.app.download.FileInfo;
import com.supaiclient.app.interf.RequestBasetListener;
import com.supaiclient.app.ui.dialog.UpdataDialog;


/**
 * 更新管理员
 * Created by Administrator on 2016/5/13.
 */
public class UpdataManeger implements RequestBasetListener {

    private static UpdataManeger mUpdataManeger;
    private Context context;
    private boolean auto = true;

    private UpdataManeger() {
    }

    //采用单列模式
    public static UpdataManeger getInstens() {

        if (mUpdataManeger == null) {
            mUpdataManeger = new UpdataManeger();
        }
        return mUpdataManeger;
    }

    public void setAutoInstall(boolean auto) {
        this.auto = auto;
    }

    public void update(Context context) {

        this.context = context;
        UserApi.getversion(context, this);
    }


    @Override
    public void onSuccess(String responseStr) {

        L.e("===========" + responseStr);

        if (!TextUtils.isEmpty(responseStr)) {

            try {
                final UpdataInfo updataInfo = JSonUtils.toBean(UpdataInfo.class, responseStr);
                if (updataInfo != null) {

                    if (updataInfo.getVersion() > BaseApplication.getVersionCode()) {

                        final UpdataDialog updataDialog = new UpdataDialog(context);

                        String s = updataInfo.getDetail();

                        //做换行处理
                        s = s.replace("{", "");
                        s = s.replace("HH", "\n");
                        s = s.replace("}", "");

                        //测试
                        updataInfo.setForce(0);

                        updataDialog.setUpdateContent(s);

                        updataDialog.setForceUpdate(updataInfo.getForce() == 1);

                        if (BaseApplication.get("isFinish", false)) {
                            updataDialog.setNowUpdata("立即安装");
                        } else {
                            updataDialog.setNowUpdata("立即更新");
                        }

                        updataDialog.setUpdataDialogListener(new UpdataDialog.UpdataDialogListener() {

                            @Override
                            public void nowInstall() {
                                //安装apk
                                PackageUtils.install(context, DownloadService.DOWNLOAD_PATH + "/" + "速派超人.apk");
                            }

                            @Override
                            public void updateNow() {
                                //L.e("现在更新");
                                updataDialog.dismiss();
                                try {

                                    //以下为测试
                                    //updataInfo.setUrl("http://www.apk3.com/uploads/soft/20160511/at4g.apk");

                                    //  download("http://www.apk3.com/uploads/soft/20160511/at4g.apk");

                                    Intent intent = new Intent(context, DownloadService.class);
                                    intent.setAction(DownloadService.ACTION_START);
                                    intent.putExtra("fileInfo", new FileInfo(1, updataInfo.getUrl(), "速派超人.apk", 0, 0));
                                    context.startService(intent);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void laterOn() {
                                //L.e("以后再说");
                                updataDialog.dismiss();
                            }

                            @Override
                            public void exitProgram() {
                                //L.e("退出程序");
                                AppManager.getAppManager().finishAllActivity();
                                updataDialog.dismiss();
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(0);
                            }
                        });

                        //测试使用  上线时改回来
                        //updataDialog.show();

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(int statusCode) {

        T.s(R.string.networf_errot);
    }

    @Override
    public void onSendError(int statusCode, String message) {
        T.s(R.string.networf_errot);
    }
//
//    private void download(String dl) throws Exception {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
//            Intent service = new Intent(context, DownloadService.class);
//            service.putExtra(DownloadService.INTENT_URL, dl);
//            context.startService(service);
//
//        } else {
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(dl));
//            context.startActivity(intent);
//        }
//    }
}
