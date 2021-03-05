package com.ssh.shuoshi.ui.patient;

import com.ssh.shuoshi.bean.patient.PatientListBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/27
 */
public interface PatientManageContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void onRefreshCompleted(PatientListBean beans, boolean isClear);

        void onLoadCompleted(boolean isLoadAll);

    }

    interface Presenter extends BasePresenter<View> {

        void onRefresh(String name);

        void loadData();

        void onLoadMore();
    }
}
