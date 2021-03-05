package com.ssh.shuoshi.ui.imagediagnose.main;

import com.ssh.shuoshi.bean.count.DiagnoseCountBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/9
 */
public interface ImageDiagnoseContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void setDiagnoseCount(DiagnoseCountBean beans);
    }

    interface Presenter extends BasePresenter<View> {

        void getCountMap();
    }
}
