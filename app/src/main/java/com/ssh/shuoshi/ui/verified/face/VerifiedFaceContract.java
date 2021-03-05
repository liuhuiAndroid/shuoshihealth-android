package com.ssh.shuoshi.ui.verified.face;

import com.ssh.shuoshi.bean.ca.CAPhoneBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.Map;

public interface VerifiedFaceContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void caFaceFirstOauthSuccess(CAPhoneBean bean);

        void caCreateUserIdSuccess(CAPhoneBean bean, int id);
    }

    interface Presenter extends BasePresenter<View> {

        void caFaceFirstOauth(Map<String, Object> map);

        void caCreateUserId(Map<String, Object> map, int id);

        void getDoctorInfo();
    }
}
