package com.ssh.shuoshi.ui.main;

import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/8
 */
public interface MainContract {

    interface View extends BaseView {

        void onError(Throwable throwable);

        void getConsultationInfoSuccess(ImageDiagnoseBean.RowsBean bean, int mId);

        void getConsultationInfoJpushSuccess(ImageDiagnoseBean.RowsBean bean, int mId);
    }

    interface Presenter extends BasePresenter<View> {

        void getDoctorInfo();

        void getConsultationInfo(int mId);

        void getConsultationInfoJpush(int mId);
    }
}
