package com.ssh.shuoshi.ui.diagnosissearch;

import com.ssh.shuoshi.bean.DiagnBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/9
 */
public interface DiagnosisSearchContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void onRefreshCompleted(DiagnBean beans, boolean isClear);

        void onLoadCompleted(boolean isLoadAll);
    }

    interface Presenter extends BasePresenter<View> {


        void onRefresh(String diagnName);

        void loadData();

        void onLoadMore();
    }
}
