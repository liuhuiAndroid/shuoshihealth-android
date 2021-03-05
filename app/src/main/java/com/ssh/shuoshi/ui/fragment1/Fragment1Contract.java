package com.ssh.shuoshi.ui.fragment1;

import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.UserSigBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.List;

/**
 * created by hwt on 2020/12/12
 */
public interface Fragment1Contract {

    interface View extends BaseView {
        void onError(Throwable throwable, int type);

        void getDoctorInfoSuccess(HisDoctorBean bean);

        void onRefreshCompleted(ImageDiagnoseBean beans, boolean isClear);

        void onLoadCompleted(boolean isLoadAll);

        void getUserSigByUserNoSuccess(String bean, HisDoctorBean bean2);

        void imgDownload(List<String> bean);

        void getConsultationInfoSuccess(ImageDiagnoseBean.RowsBean bean);

        void getConsultationInfoJpushSuccess(ImageDiagnoseBean.RowsBean bean, int mId);
    }

    interface Presenter extends BasePresenter<Fragment1Contract.View> {
        void getDoctorInfo();

        void onRefresh();

        void onLoadMore();

        void loadData();

        void getUserSigByUserNo(String userNo, HisDoctorBean bean);

        void getImagePath(String[] photoPath);

        void getConsultationInfo(int mId);

        void getConsultationInfoJpush(int mId);
    }
}
