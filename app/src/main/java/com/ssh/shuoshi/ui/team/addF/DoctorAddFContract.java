package com.ssh.shuoshi.ui.team.addF;

import com.ssh.shuoshi.bean.team.DistinctDoctorBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2021/1/3
 */
public interface DoctorAddFContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void onRefreshCompleted(DistinctDoctorBean beans, boolean isClear);

        void onLoadCompleted(boolean isLoadAll);
    }

    interface Presenter extends BasePresenter<View> {

        void onRefresh(String name);

        void loadData();

        void onLoadMore();
    }
}
