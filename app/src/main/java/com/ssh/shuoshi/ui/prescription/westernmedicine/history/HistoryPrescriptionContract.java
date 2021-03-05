package com.ssh.shuoshi.ui.prescription.westernmedicine.history;

import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

public interface HistoryPrescriptionContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void getPrescription(HisPrescriptionDtoBean bean);
    }

    interface Presenter extends BasePresenter<View> {

        void getPrescriptionByPatientId(int patientId, int perscriptionTypeId);
    }
}
