package com.ssh.shuoshi;

import android.view.View;

import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.view.pickview.builder.TimePickerBuilder;
import com.ssh.shuoshi.view.pickview.listener.OnTimeSelectListener;
import com.ssh.shuoshi.view.pickview.view.TimePickerView;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;

import static com.ssh.shuoshi.library.util.TimeUtils.getTime;

/**
 * created by hwt on 2021/1/14
 */
public class TestActivity extends BaseActivity {
    @BindView(R.id.title)
    UniteTitle title;

    @Override
    public int initContentView() {
        return R.layout.activity_test;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initUiAndListener() {
        title.setOnClickListener(v -> chooseTime());
    }

    private void chooseTime() {

        Calendar startDate = Calendar.getInstance();
        startDate.set(2018, 1, 23);
        Calendar endDate = Calendar.getInstance();

        TimePickerView pickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {

            }
        }).setTitleText("选择时间")
                .setContentTextSize(16)
                .setDate(endDate)
                .setRangDate(startDate, endDate)
                .setType(new boolean[]{true, true, false, false, false, false})
                .build();
        pickerView.show();
    }

}
