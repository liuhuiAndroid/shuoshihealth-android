package com.ssh.shuoshi.ui.login;

import com.ssh.shuoshi.bean.LoginEntity;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/8
 */
public interface LoginContract {

    interface View extends BaseView {

        void loginSuccess(LoginEntity loginEntity);

        void showUserNameError(String message);

        void showPassWordError(String message);

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<View> {

    }
}
