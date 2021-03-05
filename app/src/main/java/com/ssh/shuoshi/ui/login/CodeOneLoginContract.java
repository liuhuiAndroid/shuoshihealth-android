package com.ssh.shuoshi.ui.login;

import android.text.Editable;

import com.ssh.shuoshi.bean.LoginInfoBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/9
 */
public interface CodeOneLoginContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void refreshSmsCodeUi();

        void SmsCodeSuccess(String uuid);

        void loginSuccess(LoginInfoBean bean);
    }

    interface Presenter extends BasePresenter<View> {

        void getPhoneCode(String phone);

        void login(String phone, String code, String mUuid);
    }
}
