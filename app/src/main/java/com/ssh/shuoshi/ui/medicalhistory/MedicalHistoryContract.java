package com.ssh.shuoshi.ui.medicalhistory;

import com.ssh.shuoshi.bean.patient.MedicalHistoryBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.List;

/**
 * created by hwt on 2020/12/27
 */
public interface MedicalHistoryContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void onRefreshCompleted(MedicalHistoryBean beans, boolean isClear);

        void onLoadCompleted(boolean isLoadAll);

        void imgDownload(List<String> bean, int position);
    }

    interface Presenter extends BasePresenter<View> {

        void onRefresh(int mPatientId);

        void loadData();

        void onLoadMore();

        void getImagePath(String[] photoPath, int position);
    }
}
