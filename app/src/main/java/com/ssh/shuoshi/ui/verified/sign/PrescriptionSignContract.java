package com.ssh.shuoshi.ui.verified.sign;

import com.ssh.shuoshi.bean.ca.CAPhoneBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.List;
import java.util.Map;

/**
 * created by hwt on 2020/12/26
 */
public interface PrescriptionSignContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void getPrescriptionFromIdSuccess(HisPrescriptionDtoBean bean);

        void caImgUploadSuccess(String bean);

        void imgDownload(List<String> bean);

        void caSignPDFNoneSuccess(String bean);

        void setDoctorInfoSuccess();

        void caFaceSecondCodeSuccess(CAPhoneBean bean);
    }

    interface Presenter extends BasePresenter<View> {

        void getPrescriptionFromId(int prescriptionId);

        void caImgUpload(String mPhotoPath);

        void getImagePath(String[] photoPath);

        void caSignPDFNone(Map<String, Object> map);

        void getDoctorInfo();

        void getDoctorInfo(boolean init);

        void caFaceSecondCode(Map<String, Object> map);

    }
}
