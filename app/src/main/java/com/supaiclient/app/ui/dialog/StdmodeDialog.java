package com.supaiclient.app.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.supaiclient.app.R;
import com.supaiclient.app.util.DensityUtil;

/**
 * 物品选择对话框
 * Created by Administrator on 2016/6/6.
 */

public class StdmodeDialog extends Dialog implements AdapterView.OnItemClickListener {


    private static final String TAG = "StdmodeDialog";

    private Context context;
    private ListView listView;
    private ListAdapter adapter;
    private SelectedListener selectedListener;


    public interface SelectedListener {

        void onSelected(int position);
    }

    public StdmodeDialog(Context context) {
        super(context, R.style.Translucent_NoTitle);
        this.context = context;
    }

    public StdmodeDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected StdmodeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_stdmode);
        listView = (ListView) findViewById(R.id.listView);
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = (int) (DensityUtil.getPhoneWidth(context) / 1.5);
        params.height = (int) (DensityUtil.getPhoneHeight(context) / 3.0);
        this.getWindow().setAttributes(params);
    }


    public void setAdapter(ListAdapter adapter) {
        this.adapter = adapter;
    }


    public void setSelectedListener(SelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    @Override
    public void show() {
        super.show();
        if (adapter != null)
            listView.setAdapter(adapter);
        if (selectedListener != null)
            listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.e(TAG, "onItemSelected() called with: parent = [" + parent + "], view = [" + view + "], position = [" + position + "], id = [" + id + "]");

        if (selectedListener != null) {
            selectedListener.onSelected(position);
        }
    }

}
