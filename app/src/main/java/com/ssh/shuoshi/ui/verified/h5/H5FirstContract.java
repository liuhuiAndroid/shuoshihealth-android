package com.ssh.shuoshi.ui.verified.h5;

import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.ca.CAPhoneBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/30
 */
public interface H5FirstContract {
    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void getDoctorInfoSuccess(HisDoctorBean bean);

        void queryFaceOauthResultSuccess(CAPhoneBean bean);
    }

    interface Presenter extends BasePresenter<View> {

        void getDoctorInfo();

        void task();

        void queryFaceOauthResult();
    }
}
