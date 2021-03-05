package com.ssh.shuoshi.ui.team.addS;

import com.ssh.shuoshi.bean.team.DoctorListBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2021/1/3
 */
public interface DoctorAddSContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void onRefreshCompleted(DoctorListBean beans, boolean isClear);

        void onLoadCompleted(boolean isLoadAll);

    }

    interface Presenter extends BasePresenter<View> {

        void onRefresh(String name);

        void loadData();

        void onLoadMore();

    }
}
