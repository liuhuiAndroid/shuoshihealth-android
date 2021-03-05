package com.ssh.shuoshi.ui.prescription.template.commonlywesternmedicine.add;

import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/22
 */
public interface TemplateAddDrugsContract {
    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void onRefreshCompleted(DrugBean beans, boolean isClear);

        void onLoadCompleted(boolean isLoadAll);

        void addMyOftenListSuccess(String beans,int pos);
    }

    interface Presenter extends BasePresenter<View> {

        void onRefresh(String name, int type, Integer phamVendorId);

        void loadData();

        void onLoadMore();

        void addMyOftenList(int id,int pos);
    }
}
