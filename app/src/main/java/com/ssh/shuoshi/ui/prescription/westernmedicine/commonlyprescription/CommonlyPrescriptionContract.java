package com.ssh.shuoshi.ui.prescription.westernmedicine.commonlyprescription;

import com.ssh.shuoshi.bean.template.PrescriptionTemplateBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/9
 */
public interface CommonlyPrescriptionContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void onRefreshCompleted(PrescriptionTemplateBean beans, boolean isClear);

        void onLoadCompleted(boolean isLoadAll);

    }

    interface Presenter extends BasePresenter<View> {

        void onRefresh(int type);

        void loadData();

        void onLoadMore();
    }
}
