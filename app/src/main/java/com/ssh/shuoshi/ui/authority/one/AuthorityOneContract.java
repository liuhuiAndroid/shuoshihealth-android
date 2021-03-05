package com.ssh.shuoshi.ui.authority.one;

import com.ssh.shuoshi.bean.AreaBean;
import com.ssh.shuoshi.bean.SysDeptBean;
import com.ssh.shuoshi.bean.pickview.SysDeptNameBean;
import com.ssh.shuoshi.bean.pickview.SysTitleNameBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.List;

/**
 * created by hwt on 2020/12/10
 */
public interface AuthorityOneContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void getSysDeptSuccess(SysDeptNameBean bean);

        void getDoctorTitleDictSuccess(SysTitleNameBean bean);

        void getAddressSuccess(List<AreaBean> bean);
    }

    interface Presenter extends BasePresenter<View> {

        void getSysDept();

        void getDoctorTitleDict();

        void getProvinceDict();
    }
}
