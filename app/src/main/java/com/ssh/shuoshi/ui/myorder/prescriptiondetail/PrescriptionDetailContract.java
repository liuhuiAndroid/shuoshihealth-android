package com.ssh.shuoshi.ui.myorder.prescriptiondetail;

import com.ssh.shuoshi.bean.DictListBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.Map;

/**
 * created by hwt on 2020/12/9
 */
public interface PrescriptionDetailContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);


    }

    interface Presenter extends BasePresenter<View> {

    }
}
