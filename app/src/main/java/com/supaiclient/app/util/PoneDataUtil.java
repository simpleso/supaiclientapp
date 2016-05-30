package com.supaiclient.app.util;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.supaiclient.app.bean.PeopleBean;
import com.supaiclient.app.interf.OnGetPeopleListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/28.
 * 手机通讯录
 */
public class PoneDataUtil {


    List<PeopleBean> peopleBeanList = new ArrayList<PeopleBean>();
    private AsyncQueryHandler asyncQuery;
    private Context context;
    private OnGetPeopleListener onGetPeopleListener;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 400:// 无 数据

                    break;
                case 200:// 有数据

                    final Cursor cursor = (Cursor) msg.obj;
                    tasks(cursor);
                    break;
                case 202:// 处理完毕

                    onGetPeopleListener.OnBack(peopleBeanList);
                    break;
            }
        }
    };

    public PoneDataUtil(Context context, OnGetPeopleListener onGetPeopleListener) {

        this.context = context;
        this.onGetPeopleListener = onGetPeopleListener;
        asyncQuery = new MyAsyncQueryHandler(context.getContentResolver());
    }

    public void load() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人的Uri
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1,
                "sort_key",
                ContactsContract.RawContacts.VERSION,
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
        }; // 查询的列
        asyncQuery.startQuery(0, null, uri, projection, null, null,
                "sort_key COLLATE LOCALIZED asc"); // 按照sort_key升序查询
    }

    // 处理 联系人
    private void tasks(final Cursor cursor) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    String phone = cursor.getString(2);
                    String name = cursor.getString(1);
                    PeopleBean peopleBean = new PeopleBean();

                    if (TextUtils.isEmpty(name)) {

                        peopleBean.setFirst("#");
                    } else {
                        peopleBean.setFirst(PingYinUtil.getPingyinOne(name));
                    }

                    peopleBean.setName(name);
                    peopleBean.setPhone(phone);
                    peopleBeanList.add(peopleBean);
                }
                handler.sendEmptyMessage(202);

            }
        }).start();

    }

    private class MyAsyncQueryHandler extends AsyncQueryHandler {

        public MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {

                cursor.moveToFirst();

                Message msg = new Message();
                msg.obj = cursor;
                msg.what = 200;
                handler.sendMessage(msg);
            } else {
                handler.sendEmptyMessage(400);
            }
        }
    }
}
