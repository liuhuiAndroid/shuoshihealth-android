package com.ssh.shuoshi.ui.worksetting.timesetting;

import com.ssh.shuoshi.bean.DoctorWeekScheduleBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.List;
import java.util.Map;

/**
 * created by hwt on 2020/12/11
 */
public interface ServiceTimeSettingContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void getScheduleListByMonthSuccess(List<DoctorWeekScheduleBean> bean);

        void getDoctorCurrentWeekScheduleDate(List<DoctorWeekScheduleBean> bean);

        void copyDoctorLastWeekScheduleDate(String bean);

        void deleteScheduleSuccess(String bean);
    }

    interface Presenter extends BasePresenter<View> {

        void getScheduleListByMonth(String date1,String date2);

        void getDoctorCurrentWeekScheduleListDate(String date);

        void copyDoctorLastWeekScheduleListDate(String date);

        void deleteDoctorSchedule(int id);
    }

}
