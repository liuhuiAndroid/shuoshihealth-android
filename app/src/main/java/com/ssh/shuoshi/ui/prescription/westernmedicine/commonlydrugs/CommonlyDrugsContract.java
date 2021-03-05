package com.ssh.shuoshi.ui.prescription.westernmedicine.commonlydrugs;

import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.bean.OftenDrugBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/9
 */
public interface CommonlyDrugsContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void onRefreshCompleted(OftenDrugBean beans, boolean isClear);

        void onLoadCompleted(boolean isLoadAll);

    }

    interface Presenter extends BasePresenter<View> {

        void getMyOftenList(int type, Integer phamVendorId);

        void loadData();

        void onLoadMore();
    }
}
