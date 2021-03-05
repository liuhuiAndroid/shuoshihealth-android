package com.ssh.shuoshi.ui.myprescription.detail;

import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2021/1/2
 */
public interface MyPrescriptionDetailContract {
    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void getPrescriptionFromIdSuccess(HisPrescriptionDtoBean bean);
    }

    interface Presenter extends BasePresenter<View> {

        void getPrescriptionFromId(int prescriptionId);
    }
}
