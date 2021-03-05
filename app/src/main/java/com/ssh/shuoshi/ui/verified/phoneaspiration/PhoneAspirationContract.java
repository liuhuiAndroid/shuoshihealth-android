package com.ssh.shuoshi.ui.verified.phoneaspiration;

import com.ssh.shuoshi.bean.ca.CAPhoneBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.Map;

/**
 * created by hwt on 2020/12/27
 */
public interface PhoneAspirationContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void caPhoneSecondCodeSuccess(CAPhoneBean bean);

        void caPhoneSecondVerifySuccess(CAPhoneBean bean);

        void setDoctorInfoSuccess();
    }

    interface Presenter extends BasePresenter<View> {

        void caPhoneSecondCode(Map<String, Object> map);

        void caPhoneSecondVerify(Map<String, Object> map2);

        void getDoctorInfo();

    }
}
