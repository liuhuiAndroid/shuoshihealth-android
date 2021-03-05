package com.ssh.shuoshi.ui.authority.editphoto;

import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.List;
import java.util.Map;

/**
 * created by hwt on 2021/1/5
 */
public interface EditPhotoContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void uploadSuccess(List<String> bean);

        void uploadInfoSuccess(String bean);
    }

    interface Presenter extends BasePresenter<View> {

        void uploadNewImg(String path);

        void putDoctorInfo(Map<String, Object> map);
    }
}
