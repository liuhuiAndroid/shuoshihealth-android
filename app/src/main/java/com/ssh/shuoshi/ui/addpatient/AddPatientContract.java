package com.ssh.shuoshi.ui.addpatient;

import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.List;

/**
 * created by hwt on 2021/1/5
 */
public interface AddPatientContract {
    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void getDoctorCodePhotoSuccess(String bean);

        void imgDownload(List<String> bean);
    }

    interface Presenter extends BasePresenter<View> {

        void getDoctorCodePhoto();

        void getImagePath(String[] photoPath);
    }
}
