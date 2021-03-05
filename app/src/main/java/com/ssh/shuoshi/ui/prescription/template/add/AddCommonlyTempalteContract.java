package com.ssh.shuoshi.ui.prescription.template.add;

import com.ssh.shuoshi.bean.common.SystemTypeBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.Map;

public interface AddCommonlyTempalteContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void getFrequencyList(SystemTypeBean bean);

        void addPrescriptionTemplateSuccess();

        void getDosageUnitsList(SystemTypeBean bean);

        void getAdministrationRouteList(SystemTypeBean bean);
    }

    interface Presenter extends BasePresenter<View> {

        void getFrequencyList();

        void addPrescriptionTemplate(Map<String, Object> mMap);

        void changePrescriptionTemplate(Map<String, Object> mMap);

        void getDosageUnits();

        void getAdministrationRoute();
    }
}
