package com.ssh.shuoshi.ui.modifypassword;

import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/15
 */
public interface ModifyPasswordContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void refreshSmsCodeUi();

        void SmsCodeSuccess(String uuid);

        void changeSuccess();

        void getDoctorInfoSuccess();
    }

    interface Presenter extends BasePresenter<View> {

        void getPhoneCode(String phone);

        void changeDoctorPhone(String phone, String code, String mUuid);

        void getDoctorInfo();
    }
}
