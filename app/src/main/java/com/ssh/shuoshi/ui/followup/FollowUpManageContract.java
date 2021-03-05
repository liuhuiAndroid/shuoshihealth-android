package com.ssh.shuoshi.ui.followup;

import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.bean.patient.FollowUpListBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/27
 */
public interface FollowUpManageContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void onRefreshCompleted(FollowUpListBean beans, boolean isClear);

        void onLoadCompleted(boolean isLoadAll);

    }

    interface Presenter extends BasePresenter<View> {

        void onRefresh();

        void loadData();

        void onLoadMore();
    }
}
