package com.ssh.shuoshi.ui.login;

import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/9
 */
public interface PasswordLoginContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<View> {

    }
}
