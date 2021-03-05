package com.ssh.shuoshi.ui.prescription.chinesemedicine.edit;

import com.ssh.shuoshi.bean.common.SystemTypeBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.Map;

/**
 * created by hwt on 2020/12/9
 */
public interface EditChineseMedicinePrescriptionContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void addPrescriptionSuccess(Integer id);

        void addPrescriptionTemplateSuccess();

        void getFrequencyList(SystemTypeBean bean);

        void getPrescriptionFromIdSuccess(HisPrescriptionDtoBean bean);
    }

    interface Presenter extends BasePresenter<View> {

        void getFrequencyList();

        void addPrescription(Map<String, Object> mMap);

        void addPrescriptionTemplate(Map<String, Object> mMap);

        void getPrescriptionFromId(Integer prescriptionId);

        void putPrescription(Map<String, Object> mMap);
    }
}
