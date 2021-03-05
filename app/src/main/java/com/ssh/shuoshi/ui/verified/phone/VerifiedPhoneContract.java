package com.ssh.shuoshi.ui.verified.phone;

import com.ssh.shuoshi.bean.ca.CAPhoneBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.Map;

public interface VerifiedPhoneContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void caGetPhoneCodeSuccess(CAPhoneBean bean);

        void caCodeOauthSuccess(CAPhoneBean bean);

        void caCreateUserIdSuccess(CAPhoneBean bean);

        void getDoctorInfoSuccess();

    }

    interface Presenter extends BasePresenter<View> {

        void caGetPhoneCode(Map<String, Object> map);

        void caCodeOauth(Map<String, Object> map);

        void caCreateUserId(Map<String, Object> map);

        void getDoctorInfo();
    }
}
