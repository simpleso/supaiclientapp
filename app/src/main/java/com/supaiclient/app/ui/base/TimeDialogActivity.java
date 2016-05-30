package com.supaiclient.app.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.supaiclient.app.R;
import com.supaiclient.app.util.DateUtils;
import com.supaiclient.app.widget.PickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/12/7.
 * 时间选择 提示框
 */
public class TimeDialogActivity extends Activity implements View.OnClickListener {


    private static final int TRANSLATE_DURATION = 200;
    private static final String EXTRA_DISMISSED = "extra_dismissed";
    private static final int ALPHA_DURATION = 300;
    private static boolean cancelable = false;
    private static BaseDialogListener Listener;
    @Bind(R.id.view_coles)
    View viewColes;
    @Bind(R.id.lin)
    LinearLayout lin;
    @Bind(R.id.minute_rl)
    PickerView minuteRl;
    @Bind(R.id.minute_pv)
    PickerView minutePv;
    @Bind(R.id.minute_tv)
    TextView minuteTv;
    @Bind(R.id.second_pv)
    PickerView secondPv;
    @Bind(R.id.second_tv)
    TextView secondTv;
    @Bind(R.id.tv_close)
    TextView tvClose;
    @Bind(R.id.tv_ok)
    TextView tvOk;
    private boolean mDismissed = true;
    private ViewGroup mGroup;
    private String timeOne;//时间 01
    private String timeHour;//时间 小时
    private String timeMinute;//时间 分钟

    private List<String> timeOneList;

    public static Builder create(Context context) {
        return new Builder(context);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_DISMISSED, mDismissed);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mDismissed = savedInstanceState.getBoolean(EXTRA_DISMISSED);
        }

        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            View focusView = this.getCurrentFocus();
            if (focusView != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }
        setContentView(R.layout.dialog_time);
        ButterKnife.bind(this);

        viewColes.setOnClickListener(this);
        tvClose.setOnClickListener(this);
        tvOk.setOnClickListener(this);

        lin.startAnimation(createAlphaInAnimation());

        init();
    }

    private void init() {


        timeOne = "今天";
        timeOneList = new ArrayList<String>();
        timeOneList.add("今天");
        timeOneList.add("明天");
        timeOneList.add("后天");
        minuteRl.setData(timeOneList);
        minuteRl.setSelected(0);

        // 默认 数据
        minutePv.setVisibility(View.VISIBLE);
        secondPv.setVisibility(View.VISIBLE);
        minuteTv.setVisibility(View.VISIBLE);
        secondTv.setVisibility(View.VISIBLE);

  /*      addXsAll();
        addFenAll();*/

        addJInrXs();
        addJinrifen();

        findViewById(R.id.tv_lij).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Listener.onClickBack(TimeDialogActivity.this, 0);
                dismiss();
            }
        });
        minuteRl.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {

                timeOne = text;
                if (text.equals("今天")) {
                    minutePv.setVisibility(View.VISIBLE);
                    secondPv.setVisibility(View.VISIBLE);
                    minuteTv.setVisibility(View.VISIBLE);
                    secondTv.setVisibility(View.VISIBLE);
                    addJInrXs();
                    addJinrifen();
                } else if (text.equals("明天")) {
                    minutePv.setVisibility(View.VISIBLE);
                    secondPv.setVisibility(View.VISIBLE);
                    minuteTv.setVisibility(View.VISIBLE);
                    secondTv.setVisibility(View.VISIBLE);

                    addXsAll();
                    addFenAll();

                } else if (text.equals("后天")) {
                    minutePv.setVisibility(View.VISIBLE);
                    secondPv.setVisibility(View.VISIBLE);
                    minuteTv.setVisibility(View.VISIBLE);
                    secondTv.setVisibility(View.VISIBLE);
                    addXsAll();
                    addFenAll();
                }
            }
        });

        // 小时
        minutePv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                timeHour = text;
                int xiaos = Integer.parseInt(text);
                boolean isJ = isXd(xiaos);
                if (isJ) {
                    addJinrifen();
                } else {
                    addFenAll();
                }
            }
        });

        //分钟
        secondPv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {

                timeMinute = text;
            }
        });
    }

    //添加 今天 小时
    private void addJInrXs() {

        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String data = sdf.format(new Date());
        int dataInt = Integer.parseInt(data);

        // 小时
        List<String> dataXs = new ArrayList<String>();

        for (int i = 1; i <= 23; i++) {

            if (i >= dataInt) {
                dataXs.add("" + showInt(i));
            }
        }

        try {
            if (dataXs.size() != 0)
                timeHour = dataXs.get(0);
            else {
                timeHour = "00";
                dataXs.add("23");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        minutePv.setData(dataXs);
        minutePv.setSelected(0);

    }

    // 判断 滑动小时  是否 到达 当前时间 小时
    private boolean isXd(int hh) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String data = sdf.format(new Date());
        int dataInt = Integer.parseInt(data);
        return dataInt == hh;
    }

    // 添加 今天 分钟 数
    private void addJinrifen() {

        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        String data = sdf.format(new Date());
        int dataInt = Integer.parseInt(data);

        List<String> dataXs = new ArrayList<String>();
        for (int i = 0; i <= 11; i++) {

            int tim = i * 5;
            if (tim > dataInt) {
                dataXs.add(showInt(tim) + "");
            }
        }
        try {
            if (dataXs.size() != 0)
                timeMinute = dataXs.get(0);
            else {
                timeMinute = "00";
                dataXs.add("59");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        secondPv.setData(dataXs);
        secondPv.setSelected(0);
    }

    //添加 全部小时
    private void addXsAll() {

        // 小时
        List<String> data = new ArrayList<String>();
        for (int i = 1; i <= 23; i++) {
            data.add("" + showInt(i));
        }
        timeHour = data.get(0);
        minutePv.setData(data);
        minutePv.setSelected(0);
    }

    // 添加 所有分钟数
    private void addFenAll() {

        // //分
        List<String> data3 = new ArrayList<String>();

        for (int i = 0; i <= 11; i++) {
            data3.add(showInt(i * 5) + "");
        }
        timeMinute = data3.get(0);
        secondPv.setData(data3);
        secondPv.setSelected(0);
    }

    // 友好显示
    private String showInt(int shoi) {

        if (shoi < 10) {

            return "0" + shoi;
        }
        return shoi + "";
    }

    private Animation createTranslationInAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
                1, type, 0);
        an.setDuration(TRANSLATE_DURATION);
        return an;
    }

    private Animation createAlphaInAnimation() {
        AlphaAnimation an = new AlphaAnimation(0, 1);
        an.setDuration(ALPHA_DURATION);
        return an;
    }

    private Animation createTranslationOutAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
                0, type, 1);
        an.setDuration(TRANSLATE_DURATION);
        an.setFillAfter(true);
        return an;
    }

    private Animation createAlphaOutAnimation() {
        AlphaAnimation an = new AlphaAnimation(1, 0);
        an.setDuration(ALPHA_DURATION);
        an.setFillAfter(true);
        return an;
    }

    public void dismiss() {
        mDismissed = true;
        lin.startAnimation(createAlphaOutAnimation());
        if (!isFinishing()) {
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {

        if (cancelable == true) {

            this.finish();
        }
    }

    public void show(Context context, String tag) {
        if (!mDismissed || !isFinishing()) {
            return;
        }
        mDismissed = false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.view_coles:

                dismiss();
                break;
            case R.id.tv_close:

                dismiss();
                break;
            case R.id.tv_ok:
                dismiss();
                if (Listener != null) {

                    if (timeOne.equals("今天")) {

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String data = sdf.format(new Date());
                        String time = data + " " + timeHour + ":" + timeMinute + ":00";
                        Listener.onClickBack(this, DateUtils.timeToDate(time));

                    } else if (timeOne.equals("明天")) {

                      /*  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String data = sdf.format(new Date());
                        String time = data + " " + timeHour + ":" + timeMinute + ":00";
                        Listener.onClickBack(this, DateUtils.timeToDate(time));*/

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        Calendar cal = Calendar.getInstance();
                        String data = sdf.format(new Date());

                        try {
                            cal.setTime(sdf.parse(data));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        cal.add(Calendar.DAY_OF_YEAR, +1);
                        String nextDate_1 = sdf.format(cal.getTime());

                        String time = nextDate_1 + " " + timeHour + ":" + timeMinute + ":00";
                        Listener.onClickBack(this, DateUtils.timeToDate(time));

                    } else {// 后天

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        Calendar cal = Calendar.getInstance();
                        String data = sdf.format(new Date());

                        try {
                            cal.setTime(sdf.parse(data));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        cal.add(Calendar.DAY_OF_YEAR, +2);
                        String nextDate_1 = sdf.format(cal.getTime());

                        String time = nextDate_1 + " " + timeHour + ":" + timeMinute + ":00";
                        Listener.onClickBack(this, DateUtils.timeToDate(time));
                    }
                }
                break;
        }
    }

    public interface BaseDialogListener {

        void onClickBack(TimeDialogActivity actionSheet, long time);

    }

    public static class Builder {

        private Context mContext;

        private String mTag = "baseDialog";

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setListener(BaseDialogListener baseDialogListener) {

            TimeDialogActivity.Listener = baseDialogListener;
            return this;
        }

        public void show() {

            mContext.startActivity(new Intent(mContext, TimeDialogActivity.class));

        }
    }

}
