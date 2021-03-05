package com.ssh.shuoshi.ui.prescription.template;

import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/22
 */
public interface PrescriptionTemplateContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

    }

    interface Presenter extends BasePresenter<View> {

    }
}
