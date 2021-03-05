package com.ssh.shuoshi.ui.doctorauthentication;

import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/13
 */
public interface DoctorAuthenticationContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

    }

    interface Presenter extends BasePresenter<View> {

        void getDoctorInfo();
    }
}
