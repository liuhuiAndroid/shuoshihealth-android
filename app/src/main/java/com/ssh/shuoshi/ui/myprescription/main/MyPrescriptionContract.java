package com.ssh.shuoshi.ui.myprescription.main;

import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2021/1/2
 */
public interface MyPrescriptionContract {
    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<View> {

    }
}
