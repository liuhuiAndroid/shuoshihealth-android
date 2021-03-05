package com.ssh.shuoshi.ui.doctorauthentication.auth;

import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.List;
import java.util.Map;

/**
 * created by hwt on 2020/12/14
 */
public interface DoctorAuthenticationAuthContract {
    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void uploadSuccess(List<String> bean, String key);

        void uploadInfoSuccess(String bean);

        void imgDownload(List<String> bean);

        void getDoctorInfoSuccess();
    }

    interface Presenter extends BasePresenter<View> {

        void uploadNewImg(String key, String value);

        void putDoctorInfo(Map<String, Object> mData);

        void getImagePath(String[] photoPath);

        void getDoctorInfo();
    }
}
