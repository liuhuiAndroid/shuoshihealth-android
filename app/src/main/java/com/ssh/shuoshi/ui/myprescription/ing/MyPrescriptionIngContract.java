package com.ssh.shuoshi.ui.myprescription.ing;

import com.ssh.shuoshi.bean.prescription.PrescriptionStateBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2021/1/2
 */
public interface MyPrescriptionIngContract {
    interface View extends BaseView {
        void onError(Throwable throwable);

        void onRefreshCompleted(PrescriptionStateBean beans, boolean isClear);

        void onLoadCompleted(boolean isLoadAll);
    }

    interface Presenter extends BasePresenter<View> {

        void onRefresh();

        void loadData();

        void onLoadMore();
    }
}
