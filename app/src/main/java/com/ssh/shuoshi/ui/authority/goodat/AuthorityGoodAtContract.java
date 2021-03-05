package com.ssh.shuoshi.ui.authority.goodat;

import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.Map;

/**
 * created by hwt on 2020/12/14
 */
public interface AuthorityGoodAtContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void uploadInfoSuccess(String bean);
    }

    interface Presenter extends BasePresenter<View> {

        void putDoctorInfo(Map<String, Object> mData);
    }
}
