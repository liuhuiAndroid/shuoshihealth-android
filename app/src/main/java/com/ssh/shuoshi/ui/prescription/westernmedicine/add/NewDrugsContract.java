package com.ssh.shuoshi.ui.prescription.westernmedicine.add;

import com.ssh.shuoshi.bean.DiagnBean;
import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/9
 */
public interface NewDrugsContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void onRefreshCompleted(DrugBean beans, boolean isClear);

        void onLoadCompleted(boolean isLoadAll);
    }

    interface Presenter extends BasePresenter<View> {

        void onRefresh(String name, int type, Integer phamVendorId);

        void loadData();

        void onLoadMore();
    }
}
