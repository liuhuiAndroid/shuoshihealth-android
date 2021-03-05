package com.ssh.shuoshi.ui.prescription.template.commonlyprescriptionchinese;

import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.template.PrescriptionTemplateBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

public interface CommonlyPrescriptionChineseContract {

    interface View extends BaseView {
        void onError(Throwable throwable);

        void onRefreshCompleted(PrescriptionTemplateBean beans, boolean isClear);

        void onLoadCompleted(boolean isLoadAll);

        void showLoading();

        void hideLoading();

        void deletePrescriptionTemplateSuccess(Integer beans);
    }

    interface Presenter extends BasePresenter<View> {

        void onRefresh();

        void loadData();

        void onLoadMore();

        void deletePrescriptionTemplate(int id);
    }
}
