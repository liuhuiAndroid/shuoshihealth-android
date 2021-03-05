package com.ssh.shuoshi.ui.myorder.prescription;

import com.ssh.shuoshi.bean.ConsultationBillBean;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/9
 */
public interface PrescriptionOrderContract {

    interface View extends BaseView {
        void onError(Throwable throwable);

        void onRefreshCompleted(ConsultationBillBean beans, boolean isClear);

        void onLoadCompleted(boolean isLoadAll);
    }

    interface Presenter extends BasePresenter<View> {

        void onRefresh();

        void loadData();

        void onLoadMore();
    }

}
