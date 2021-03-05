package com.ssh.shuoshi.ui.verified.h5;

import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.Map;

/**
 * created by hwt on 2020/12/30
 */
public interface H5SecondContract {
    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void caFaceThirdCodeSuccess(String bean);
    }

    interface Presenter extends BasePresenter<View> {

        void caFaceThirdCode(Map<String, Object> map);
    }
}
