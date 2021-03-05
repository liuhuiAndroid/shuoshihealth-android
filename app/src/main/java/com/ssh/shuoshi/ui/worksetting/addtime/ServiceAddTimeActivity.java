package com.ssh.shuoshi.ui.worksetting.addtime;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.Week;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.CommonDialog;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.view.pickview.builder.TimePickerBuilder;
import com.ssh.shuoshi.view.pickview.listener.OnTimeSelectListener;
import com.ssh.shuoshi.view.pickview.view.TimePickerView;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

import static com.ssh.shuoshi.library.util.TimeUtils.getTime;

/**
 * 添加接诊时间
 */
public class ServiceAddTimeActivity extends BaseActivity implements ServiceAddTimeContract.View, View.OnClickListener {

    @Inject
    ServiceAddTimePresenter mPresenter;

    @BindView(R.id.title)
    UniteTitle title;
    @BindView(R.id.cb_1)
    CheckBox cb1;
    @BindView(R.id.cb_2)
    CheckBox cb2;
    @BindView(R.id.cb_3)
    CheckBox cb3;
    @BindView(R.id.cb_4)
    CheckBox cb4;
    @BindView(R.id.cb_5)
    CheckBox cb5;
    @BindView(R.id.cb_6)
    CheckBox cb6;
    @BindView(R.id.cb_7)
    CheckBox cb7;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.img_start_time)
    ImageView imgStartTime;
    @BindView(R.id.rl_start_time)
    RelativeLayout rlStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.img_end_time)
    ImageView imgEndTime;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.rl_1)
    RelativeLayout rl1;
    @BindView(R.id.rl_2)
    RelativeLayout rl2;
    @BindView(R.id.rl_3)
    RelativeLayout rl3;
    @BindView(R.id.rl_4)
    RelativeLayout rl4;
    @BindView(R.id.rl_5)
    RelativeLayout rl5;
    @BindView(R.id.rl_6)
    RelativeLayout rl6;
    @BindView(R.id.rl_7)
    RelativeLayout rl7;
    private LoadingDialog mLoadingDialog;
    private Calendar mCalendar;
    private Week week;
    private String startTime;
    private String endTime;
    List<Map<String, Object>> list = new ArrayList<>();

    @Override
    public int initContentView() {
        return R.layout.activity_service_add_time;
    }

    @Override
    protected void initInjector() {
        DaggerServiceAddTimeComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        mCalendar = Calendar.getInstance();
        title.setBackListener(-1, v -> judgeBack());

        week = (Week) getIntent().getSerializableExtra("week");

        rl1.setOnClickListener(this);
        rl2.setOnClickListener(this);
        rl3.setOnClickListener(this);
        rl4.setOnClickListener(this);
        rl5.setOnClickListener(this);
        rl6.setOnClickListener(this);
        rl7.setOnClickListener(this);
        rlStartTime.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_1:
                cb1.setChecked(!cb1.isChecked());
                break;

            case R.id.rl_2:
                cb2.setChecked(!cb2.isChecked());
                break;

            case R.id.rl_3:
                cb3.setChecked(!cb3.isChecked());
                break;

            case R.id.rl_4:
                cb4.setChecked(!cb4.isChecked());
                break;

            case R.id.rl_5:
                cb5.setChecked(!cb5.isChecked());
                break;

            case R.id.rl_6:
                cb6.setChecked(!cb6.isChecked());
                break;

            case R.id.rl_7:
                cb7.setChecked(!cb7.isChecked());
                break;

            case R.id.rl_start_time:
                TimePickerView pickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String time = getTime(date);
                        String startHour = time.substring(11, 13);
                        String startMinute = time.substring(14, 16);
                        int startHourI = Integer.parseInt(startHour);
                        if (startHourI >= 23) {
                            ToastUtil.showToast("开始时间：不超过23:00");
                            return;
                        }
                        int endHourI = startHourI + 1;
                        tvStartTime.setText(startHour + ":" + startMinute);
                        tvEndTime.setText(endHourI + ":" + startMinute);
                    }
                }).setTitleText("选择开始时间")
                        .setContentTextSize(16)
                        .setType(new boolean[]{false, false, false, true, true, false})
                        .build();
                pickerView.show();
                break;

            case R.id.btn_save:
                //请求网络
                if (!check()) {
                    return;
                }
                uploadDate();
                showLoading();
                mPresenter.addDoctorSchedule(list);
                break;
        }
    }

    private boolean check() {
        if (!cb1.isChecked() && !cb2.isChecked() && !cb3.isChecked()
                && !cb4.isChecked() && !cb5.isChecked() && !cb6.isChecked() && !cb7.isChecked()) {
            ToastUtil.showToast("至少选择一天");
            return false;
        }

        startTime = tvStartTime.getText().toString().trim();
        endTime = tvEndTime.getText().toString().trim();
        if (TextUtils.isEmpty(startTime)) {
            ToastUtil.showToast("开始时间不能为空");
            return false;
        }
        if (TextUtils.isEmpty(endTime)) {
            ToastUtil.showToast("结束时间不能为空");
            return false;
        }

        if (compTime(startTime, endTime)) {
            ToastUtil.showToast("结束时间不能大于开始时间");
            return false;
        }

        //获取时间与当前时间比较
        if (cb1.isChecked() && getTimeCompareSize(week.getMonday() + " " + startTime + ":00")) {
            ToastUtil.showToast("排期时间小于当前时间");
            return false;
        }

        if (cb2.isChecked() && getTimeCompareSize(week.getTuesday() + " " + startTime + ":00")) {
            ToastUtil.showToast("排期时间小于当前时间");
            return false;
        }

        if (cb3.isChecked() && getTimeCompareSize(week.getWednesday() + " " + startTime + ":00")) {
            ToastUtil.showToast("排期时间小于当前时间");
            return false;
        }

        if (cb4.isChecked() && getTimeCompareSize(week.getThursday() + " " + startTime + ":00")) {
            ToastUtil.showToast("排期时间小于当前时间");
            return false;
        }

        if (cb5.isChecked() && getTimeCompareSize(week.getFriday() + " " + startTime + ":00")) {
            ToastUtil.showToast("排期时间小于当前时间");
            return false;
        }

        if (cb6.isChecked() && getTimeCompareSize(week.getSaturday() + " " + startTime + ":00")) {
            ToastUtil.showToast("排期时间小于当前时间");
            return false;
        }

        if (cb7.isChecked() && getTimeCompareSize(week.getSunday() + " " + startTime + ":00")) {
            ToastUtil.showToast("排期时间小于当前时间");
            return false;
        }


        return true;
    }

    //获取时间与当前时间比较
    public static boolean getTimeCompareSize(String startTime) {
        boolean isCompare = true;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//年-月-日 时-分
        try {
            Date date1 = dateFormat.parse(startTime);//开始时间
            Date date2 = new Date();//结束时间
            // 1 结束时间小于开始时间 2 开始时间与结束时间相同 3 结束时间大于开始时间
            if (date2.getTime() < date1.getTime()) {
                isCompare = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isCompare;
    }

    private void uploadDate() {
        list.clear();
        if (cb1.isChecked()) {
            Map<String, Object> mDate = new HashMap<>();
            mDate.put("workDate", week.getMonday());
            mDate.put("startTime", tvStartTime.getText().toString().trim());
            mDate.put("endTime", tvEndTime.getText().toString().trim());
            list.add(mDate);
        }

        if (cb2.isChecked()) {
            Map<String, Object> mDate = new HashMap<>();
            mDate.put("workDate", week.getTuesday());
            mDate.put("startTime", tvStartTime.getText().toString().trim());
            mDate.put("endTime", tvEndTime.getText().toString().trim());
            list.add(mDate);
        }

        if (cb3.isChecked()) {
            Map<String, Object> mDate = new HashMap<>();
            mDate.put("workDate", week.getWednesday());
            mDate.put("startTime", tvStartTime.getText().toString().trim());
            mDate.put("endTime", tvEndTime.getText().toString().trim());
            list.add(mDate);
        }

        if (cb4.isChecked()) {
            Map<String, Object> mDate = new HashMap<>();
            mDate.put("workDate", week.getThursday());
            mDate.put("startTime", tvStartTime.getText().toString().trim());
            mDate.put("endTime", tvEndTime.getText().toString().trim());
            list.add(mDate);
        }

        if (cb5.isChecked()) {
            Map<String, Object> mDate = new HashMap<>();
            mDate.put("workDate", week.getFriday());
            mDate.put("startTime", tvStartTime.getText().toString().trim());
            mDate.put("endTime", tvEndTime.getText().toString().trim());
            list.add(mDate);
        }

        if (cb6.isChecked()) {
            Map<String, Object> mDate = new HashMap<>();
            mDate.put("workDate", week.getSaturday());
            mDate.put("startTime", tvStartTime.getText().toString().trim());
            mDate.put("endTime", tvEndTime.getText().toString().trim());
            list.add(mDate);
        }

        if (cb7.isChecked()) {
            Map<String, Object> mDate = new HashMap<>();
            mDate.put("workDate", week.getSunday());
            mDate.put("startTime", tvStartTime.getText().toString().trim());
            mDate.put("endTime", tvEndTime.getText().toString().trim());
            list.add(mDate);
        }
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(ServiceAddTimeActivity.this, "");
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            judgeBack();
            return true;
        }
        //继续执行父类其他点击事件
        return super.onKeyUp(keyCode, event);
    }

    //返回判断
    private void judgeBack() {
        uploadDate();
        if (list.size() == 0) {
            finish();
        } else {
            CommonDialog commonDialog = new CommonDialog(ServiceAddTimeActivity.this, "是否保存更改",R.style.dialog_physician_certification);
            commonDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            commonDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            //布局位于状态栏下方
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            //全屏
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            //隐藏导航栏
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                    uiOptions |= 0x00001000;
                    commonDialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
                }
            });
            commonDialog.setOnItemClickListener(new CommonDialog.ItemClickListener() {
                @Override
                public void cancel() {
                    commonDialog.dismiss();
                    finish();
                }

                @Override
                public void save() {
                    commonDialog.dismiss();
                    if (!check()) {
                        return;
                    }
                    showLoading();
                    mPresenter.addDoctorSchedule(list);

                }
            });
            commonDialog.show();
        }
    }

    public static boolean compTime(String startTime, String endTime) {
        String[] array1 = startTime.split(":");
        int total1 = Integer.valueOf(array1[0]) * 3600 + Integer.valueOf(array1[1]) * 60;
        String[] array2 = endTime.split(":");
        int total2 = Integer.valueOf(array2[0]) * 3600 + Integer.valueOf(array2[1]) * 60;
        return total1 - total2 > 0 ? true : false;
    }


    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
    }

    @Override
    public void addDoctorScheduleSuccess() {
        ToastUtil.showToast("添加成功");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}
