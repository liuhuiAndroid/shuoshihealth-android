package com.ssh.shuoshi.ui.prescription.chinesemedicine.addherbs;

import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.bean.template.PrescriptionTemplateBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/9
 */
public interface ChineseMedicineMedicalAddHerbsContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void onLoadDataSuccess(DrugBean beans);

        void onRefreshCompleted(PrescriptionTemplateBean beans);
    }

    interface Presenter extends BasePresenter<View> {

        void loadData(String name, Integer phamVendorId);

        void onRefresh();
    }
}
