package com.ssh.shuoshi.ui.consultationsummary;

import com.ssh.shuoshi.bean.EmrBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.Map;

public interface ConsultationSummaryContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void getEmrSuccess(EmrBean bean);

        void addEmrSuccess(Integer bean);

        void changeEmrSuccess(Integer bean);
    }

    interface Presenter extends BasePresenter<View> {

        void getEmr(int mId);

        void addEmr(Map<String, Object> mData);

        void changeEmr(Map<String, Object> mData);
    }
}
