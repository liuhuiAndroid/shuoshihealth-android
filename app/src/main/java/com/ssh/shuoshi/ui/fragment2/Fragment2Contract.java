package com.ssh.shuoshi.ui.fragment2;

import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.List;

/**
 * created by hwt on 2020/12/12
 */
public interface Fragment2Contract {

    interface View extends BaseView {
        void onError(Throwable throwable);

        void getDoctorInfoSuccess(HisDoctorBean bean);

        void imgDownload(List<String> bean);
    }

    interface Presenter extends BasePresenter<View> {
        void getDoctorInfo();

        void getImagePath(String[] photoPath);
    }
}
