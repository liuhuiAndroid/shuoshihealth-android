package com.ssh.shuoshi.ui.worksetting.servicenote;

import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/9
 */
public interface ServiceNoteContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void onAddDoctorConsultationSuccess();
    }

    interface Presenter extends BasePresenter<View> {

        void addDoctorConsultation(int id);
    }
}
