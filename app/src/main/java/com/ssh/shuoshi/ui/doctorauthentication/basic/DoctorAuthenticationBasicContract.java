package com.ssh.shuoshi.ui.doctorauthentication.basic;

import com.ssh.shuoshi.bean.AreaBean;
import com.ssh.shuoshi.bean.pickview.SysDeptNameBean;
import com.ssh.shuoshi.bean.pickview.SysTitleNameBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.List;
import java.util.Map;

/**
 * created by hwt on 2020/12/13
 */
public interface DoctorAuthenticationBasicContract {
    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void getSysDeptSuccess(SysDeptNameBean bean);

        void getDoctorTitleDictSuccess(SysTitleNameBean bean);

        void uploadInfoSuccess(String bean);

        void getAddressSuccess(List<AreaBean> bean);
    }

    interface Presenter extends BasePresenter<View> {

        void getProvinceDict();

        void getSysDept();

        void getDoctorTitleDict();

        void putDoctorInfo(Map<String, Object> mData);

    }
}
