package com.ssh.shuoshi.ui.prescription.template.commonlywesternmedicine;

import com.ssh.shuoshi.bean.OftenDrugBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

public interface CommonlyWesternMedicineContract {

    interface View extends BaseView {
        void onError(Throwable throwable);

        void onRefreshCompleted(OftenDrugBean beans, boolean isClear);

        void onLoadCompleted(boolean isLoadAll);

        void showLoading();

        void hideLoading();

        void deleteMyOftenListSuccess(String beans);
    }

    interface Presenter extends BasePresenter<View> {

        void onRefresh(int type, Integer phamVendorId);

        void loadData();

        void onLoadMore();

        void deleteMyOftenList(int id);
    }
}
