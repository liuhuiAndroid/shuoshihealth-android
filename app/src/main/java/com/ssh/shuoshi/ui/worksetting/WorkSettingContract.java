package com.ssh.shuoshi.ui.worksetting;

import com.ssh.shuoshi.bean.DoctorConsultationInfo;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.List;

/**
 * created by hwt on 2020/12/9
 */
public interface WorkSettingContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void getDoctorConsultationSuccess(DoctorConsultationInfo info);

        void getDoctorConsultationInfoSuccess(DoctorConsultationInfo doctorConsultationInfo);

        void onEditDoctorConsultationSuccess();
    }

    interface Presenter extends BasePresenter<View> {

        void getDoctorConsultation(int id);

        void getDoctorConsultationInfo();

        void editDoctorConsultation(int id, Integer consultationTypeId, Double price, Integer enableFlag);
    }
}
