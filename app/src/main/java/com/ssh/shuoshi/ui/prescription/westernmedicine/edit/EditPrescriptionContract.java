package com.ssh.shuoshi.ui.prescription.westernmedicine.edit;

import com.ssh.shuoshi.bean.common.SystemTypeBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.Map;

/**
 * created by hwt on 2020/12/9
 */
public interface EditPrescriptionContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void addPrescriptionSuccess(Map<String, Object> map,Integer bean);

        void addPrescriptionTemplateSuccess();

        void getFrequencyList(SystemTypeBean bean);

        void getDosageUnitsList(SystemTypeBean bean);

        void getAdministrationRouteList(SystemTypeBean bean);

        void getPrescriptionFromIdSuccess(HisPrescriptionDtoBean bean);
    }

    interface Presenter extends BasePresenter<View> {

        void addPrescription(Map<String, Object> map);

        void addPrescriptionTemplate(Map<String, Object> mMap);

        void getFrequencyList();

        void getDosageUnits();

        void getAdministrationRoute();

        void getPrescriptionFromId(Integer prescriptionId);

        void putPrescription(Map<String, Object> mMap);
    }
}
