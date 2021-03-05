package com.ssh.shuoshi.ui.verified.submit;

import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.List;

/**
 * created by hwt on 2020/12/27
 */
public interface PrescriptionSubmitCheckContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void imgDownload(List<String> bean);

        void changePrescriptionStateSuccess(Integer bean);

        void setDoctorInfoSuccess();

        void getPrescriptionFromIdSuccess(HisPrescriptionDtoBean bean);

        void getPrescriptionFromIdSuccessForFinish(HisPrescriptionDtoBean bean);
    }

    interface Presenter extends BasePresenter<View> {

        void getImagePath(String[] photoPath);

        void changePrescriptionState(int prescriptionId);

        void getDoctorInfo();

        void getPrescriptionFromId(int prescriptionId);

        void getPrescriptionFromIdForFinish(int prescriptionId);
    }
}
