package com.ssh.shuoshi.ui.worksetting.addtime;

import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.List;
import java.util.Map;

/**
 * created by hwt on 2020/12/11
 */
public interface ServiceAddTimeContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void addDoctorScheduleSuccess();
    }

    interface Presenter extends BasePresenter<View> {

        void addDoctorSchedule(List<Map<String, Object>> list);
    }
}
