package com.ssh.shuoshi.ui.worksetting.timesetting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.DoctorWeekScheduleBean;
import com.ssh.shuoshi.bean.Week;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.CommonDialog;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.worksetting.addtime.ServiceAddTimeActivity;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.haibin.calendarview.CalendarUtil.compareTo;
import static com.ssh.shuoshi.util.WeekUtil.getWeek;
import static com.ssh.shuoshi.util.WeekUtil.stringToDate;

/**
 * 医生排班设置
 */
public class ServiceTimeSettingActivity extends BaseActivity implements ServiceTimeSettingContract.View, CalendarView.OnCalendarSelectListener, View.OnClickListener {

    @Inject
    ServiceTimeSettingPresenter mPresenter;

    @BindView(R.id.title)
    UniteTitle title;
    @BindView(R.id.img_pre)
    ImageView imgPre;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.img_next)
    ImageView imgNext;
    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    @BindView(R.id.tv_week)
    TextView tvWeek;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.btn_copy)
    Button btnCopy;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    private LoadingDialog mLoadingDialog;
    private CommonAdapter commonAdapter;
    private CommonAdapter childAdapter;
    private boolean isEdit = false;
    private List<DoctorWeekScheduleBean> mDoctorCurrentWeekScheduleDate = new ArrayList<>();  //医生当前周排班信息
    private String time = "";  //拼接的时间字符串
    private Week week;
    private int mFirstYear;
    private int mFirstMonth;
    private int mFirstDay;

    @Override
    public int initContentView() {
        return R.layout.activity_service_time_setting;
    }

    @Override
    protected void initInjector() {
        DaggerServiceTimeSettingComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);

        title.setBackListener(-1, v -> finish());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(ServiceTimeSettingActivity.this, LinearLayout.VERTICAL, false));
        mCalendarView.setOnCalendarSelectListener(this);
        imgPre.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        btnCopy.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        tvEdit.setOnClickListener(this);

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        //当前年月日
        int mCurrentYear = calendar.get(java.util.Calendar.YEAR);
        int mCurrentMonth = calendar.get(java.util.Calendar.MONTH) + 1;
        int mCurrentDay = calendar.get(java.util.Calendar.DAY_OF_MONTH);


        tvTime.setText(mCurrentYear + "年" + mCurrentMonth + "月");
        week = getWeek(new Date());

        String monday = week.getMonday();
        String[] split = monday.split("-");
        if (split.length == 3) {
            mFirstYear = Integer.parseInt(split[0]);
            mFirstMonth = Integer.parseInt(split[1]);
            mFirstDay = Integer.parseInt(split[2]);
        }

        tvDate.setText(split(monday) + "-" + split(week.getSunday()));
        time = mCurrentYear + "-" + mCurrentMonth + "-" + mCurrentDay;
        //根据所选日期获取当前周的排班
        mPresenter.getDoctorCurrentWeekScheduleListDate(time);


        // -----------------------------------------------------------------------------------------
        // -----------------------------------------------------------------------------------------
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 获取前月的第一天
        java.util.Calendar cale = java.util.Calendar.getInstance();
        cale.add(java.util.Calendar.MONTH, 0);
        cale.set(java.util.Calendar.DAY_OF_MONTH, 1);
        String firstday001 = format.format(cale.getTime());
        cale.add(java.util.Calendar.MONTH, 1);
        cale.set(java.util.Calendar.DAY_OF_MONTH, 0);
        String lastday001 = format.format(cale.getTime());
        mPresenter.getScheduleListByMonth(firstday001, lastday001);
        // -----------------------------------------------------------------------------------------
        // -----------------------------------------------------------------------------------------

        initData();

        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        java.util.Calendar jcCurrent = java.util.Calendar.getInstance();
        Calendar calendar2 = new Calendar();
        calendar2.setYear(jcCurrent.get(java.util.Calendar.YEAR));
        calendar2.setMonth(jcCurrent.get(java.util.Calendar.MONTH) + 1);
        calendar2.setDay(jcCurrent.get(java.util.Calendar.DAY_OF_MONTH));

        mCalendarView.getDelegate().setClickCalendar(calendar2);
        Calendar calendarCopy = new Calendar();
        calendarCopy.setYear(jcCurrent.get(java.util.Calendar.YEAR));
        calendarCopy.setMonth(jcCurrent.get(java.util.Calendar.MONTH) + 1);
        calendarCopy.setDay(jcCurrent.get(java.util.Calendar.DAY_OF_MONTH));

        java.util.Calendar jc = java.util.Calendar.getInstance();
        jc.setTimeInMillis(calendarCopy.getTimeInMillis());
        int day_of_week = jc.get(java.util.Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        jc.add(java.util.Calendar.DATE, -day_of_week + 1);

        calendarCopy.setYear(jc.get(java.util.Calendar.YEAR));
        calendarCopy.setMonth(jc.get(java.util.Calendar.MONTH) + 1);
        calendarCopy.setDay(jc.get(java.util.Calendar.DAY_OF_MONTH));
        mCalendarView.getDelegate().setSelectedStartRangeCalendar(calendarCopy);

        Calendar calendarCopy2 = new Calendar();
        jc.add(java.util.Calendar.DATE, 6);
        calendarCopy2.setYear(jc.get(java.util.Calendar.YEAR));
        calendarCopy2.setMonth(jc.get(java.util.Calendar.MONTH) + 1);
        calendarCopy2.setDay(jc.get(java.util.Calendar.DAY_OF_MONTH));
        mCalendarView.getDelegate().setSelectedEndRangeCalendar(calendarCopy2);
    }

    public String split(String time) {
        String[] times = time.split("-");
        if (times.length < 3) {
            return time;
        }
        return times[1] + "月" + times[2] + "日";
    }

    private void initData() {
        commonAdapter = new CommonAdapter<DoctorWeekScheduleBean>(this, R.layout.service_time_setting_item, mDoctorCurrentWeekScheduleDate) {
            @Override
            protected void convert(ViewHolder holder, final DoctorWeekScheduleBean weekScheduleBean, final int pos) {
                holder.setText(R.id.tv_date, weekScheduleBean.getDate());
                holder.setText(R.id.tv_week, weekScheduleBean.getDayOfWeek());

                RecyclerView recycler = holder.getView(R.id.recyclerViewChild);
                recycler.setLayoutManager(new GridLayoutManager(ServiceTimeSettingActivity.this, 3));
                childAdapter = new CommonAdapter<DoctorWeekScheduleBean.HisDoctorSchedulesBean>(ServiceTimeSettingActivity.this, R.layout.service_time_setting_item_item, weekScheduleBean.getHisDoctorSchedules()) {
                    @Override
                    protected void convert(ViewHolder holder, DoctorWeekScheduleBean.HisDoctorSchedulesBean bean, int position) {
                        holder.setText(R.id.tv_detail, bean.getStartTime() + "-" + bean.getEndTime());
                        ImageView imgDelete = holder.getView(R.id.img_delete);
                        if (isEdit) {
                            imgDelete.setVisibility(View.VISIBLE);
                            btnCopy.setEnabled(false);
                        } else {
                            imgDelete.setVisibility(View.GONE);
                            btnCopy.setEnabled(true);
                        }
                        imgDelete.setOnClickListener(v -> {
                            CommonDialog commonDialog = new CommonDialog(ServiceTimeSettingActivity.this,
                                    bean.getAppointmentNum() > 0 ? "当前时间段已有患者预约视频问诊，是否确认删除？" : "确认删除该时间段排班吗？",
                                    R.style.dialog_physician_certification);
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
                                }

                                @Override
                                public void save() {
                                    showLoading();
                                    int id = mDoctorCurrentWeekScheduleDate.get(pos).getHisDoctorSchedules().get(position).getId();
                                    mPresenter.deleteDoctorSchedule(id);
                                    commonDialog.dismiss();
                                }
                            });
                            commonDialog.show();
                        });
                    }
                };
                recycler.setAdapter(childAdapter);
            }
        };
        mRecyclerView.setAdapter(commonAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_pre:
                mCalendarView.scrollToPre(true);
                break;
            case R.id.img_next:
                mCalendarView.scrollToNext(true);
                break;
            case R.id.btn_copy:
                CommonDialog commonDialog = new CommonDialog(ServiceTimeSettingActivity.this, "是否确认复制上周排班", R.style.dialog_physician_certification);
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
                    }

                    @Override
                    public void save() {
                        showLoading();
                        mPresenter.copyDoctorLastWeekScheduleListDate(time);
                        commonDialog.dismiss();
                    }
                });
                commonDialog.show();
                break;
            case R.id.btn_add:
                // 添加接诊时间
                Intent intent = new Intent(ServiceTimeSettingActivity.this, ServiceAddTimeActivity.class);
                intent.putExtra("week", week);
                startActivityForResult(intent, 200);
                break;
            case R.id.tv_edit:
                isEdit = !isEdit;
                commonAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200:
                    //重新请求
                    mPresenter.getDoctorCurrentWeekScheduleListDate(time);

                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = format.parse(time);
                        // 获取前月的第一天
                        java.util.Calendar cale = java.util.Calendar.getInstance();
                        cale.setTime(date);
                        cale.add(java.util.Calendar.MONTH, 0);
                        cale.set(java.util.Calendar.DAY_OF_MONTH, 1);
                        String firstday001 = format.format(cale.getTime());
                        cale.add(java.util.Calendar.MONTH, 1);
                        cale.set(java.util.Calendar.DAY_OF_MONTH, 0);
                        String lastday001 = format.format(cale.getTime());
                        mPresenter.getScheduleListByMonth(firstday001, lastday001);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(ServiceTimeSettingActivity.this, "");
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
        hideLoading();
    }

    @Override
    public void getDoctorCurrentWeekScheduleDate(List<DoctorWeekScheduleBean> bean) {
        this.mDoctorCurrentWeekScheduleDate = bean;
        commonAdapter.setDatas(bean);
        if (mDoctorCurrentWeekScheduleDate == null || mDoctorCurrentWeekScheduleDate.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            tvHint.setVisibility(View.VISIBLE);
            btnCopy.setEnabled(true);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvHint.setVisibility(View.GONE);
            btnCopy.setEnabled(false);
        }

    }

    @Override
    public void copyDoctorLastWeekScheduleDate(String bean) {
        hideLoading();
        mPresenter.getDoctorCurrentWeekScheduleListDate(time);
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(time);
            // 获取前月的第一天
            java.util.Calendar cale = java.util.Calendar.getInstance();
            cale.setTime(date);
            cale.add(java.util.Calendar.MONTH, 0);
            cale.set(java.util.Calendar.DAY_OF_MONTH, 1);
            String firstday001 = format.format(cale.getTime());
            cale.add(java.util.Calendar.MONTH, 1);
            cale.set(java.util.Calendar.DAY_OF_MONTH, 0);
            String lastday001 = format.format(cale.getTime());
            mPresenter.getScheduleListByMonth(firstday001, lastday001);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteScheduleSuccess(String bean) {
        mPresenter.getDoctorCurrentWeekScheduleListDate(time);
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(time);
            // 获取前月的第一天
            java.util.Calendar cale = java.util.Calendar.getInstance();
            cale.setTime(date);
            cale.add(java.util.Calendar.MONTH, 0);
            cale.set(java.util.Calendar.DAY_OF_MONTH, 1);
            String firstday001 = format.format(cale.getTime());
            cale.add(java.util.Calendar.MONTH, 1);
            cale.set(java.util.Calendar.DAY_OF_MONTH, 0);
            String lastday001 = format.format(cale.getTime());
            mPresenter.getScheduleListByMonth(firstday001, lastday001);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 获取前月的第一天
        java.util.Calendar cale = java.util.Calendar.getInstance();
        cale.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());
        cale.add(java.util.Calendar.MONTH, 0);
        cale.set(java.util.Calendar.DAY_OF_MONTH, 1);
        String firstday001 = format.format(cale.getTime());
        cale.add(java.util.Calendar.MONTH, 1);
        cale.set(java.util.Calendar.DAY_OF_MONTH, 0);
        String lastday001 = format.format(cale.getTime());
        mPresenter.getScheduleListByMonth(firstday001, lastday001);


        tvTime.setText(calendar.getYear() + "年" + calendar.getMonth() + "月");
        time = calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay();
        //根据所选日期获取当前周的排班
        mPresenter.getDoctorCurrentWeekScheduleListDate(time);
        //根据所选日期获取上周的排班
        Date date = stringToDate(time);
        week = getWeek(date);
        tvDate.setText(split(week.getMonday()) + "-" + split(week.getSunday()));

        int llSize = compareTo(calendar.getYear(), calendar.getMonth(), calendar.getDay(),
                mFirstYear, mFirstMonth, mFirstDay);
        if (llSize < 0) {
            llBottom.setVisibility(View.GONE);
            tvEdit.setVisibility(View.GONE);
        } else {
            llBottom.setVisibility(View.VISIBLE);
            tvEdit.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void getScheduleListByMonthSuccess(List<DoctorWeekScheduleBean> bean) {
        mCalendarView.clearSchemeDate();
        for (int i = 0; i < bean.size(); i++) {
            DoctorWeekScheduleBean doctorWeekScheduleBean = bean.get(i);
            String date = doctorWeekScheduleBean.getDate();
            String[] split = date.split("-");

            Calendar calendar1 = new Calendar();
            calendar1.setYear(Integer.parseInt(split[0]));
            calendar1.setMonth(Integer.parseInt(split[1]));
            calendar1.setDay(Integer.parseInt(split[2]));
            mCalendarView.addSchemeDate(calendar1);
        }
    }

}
