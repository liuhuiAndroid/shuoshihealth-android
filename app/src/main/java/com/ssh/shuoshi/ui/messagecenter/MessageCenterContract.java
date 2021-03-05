package com.ssh.shuoshi.ui.messagecenter;

import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.JPushSysMsgRecordBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

public interface MessageCenterContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void onRefreshCompleted(JPushSysMsgRecordBean beans, boolean isClear);

        void onLoadCompleted(boolean isLoadAll);

        void deleteJpushSysMsgRecordSuccess(String bean);

        void getConsultationInfoSuccess(ImageDiagnoseBean.RowsBean bean, int mId);

        void getConsultationEditFollowTimeSuccess(String bean);
    }

    interface Presenter extends BasePresenter<View> {

        void onRefresh();

        void loadData();

        void onLoadMore();

        void deleteJpushSysMsgRecord(String id);

        void getConsultationInfo(int mId, int id2);

        void consultationEditFollowTime(int days, int id);
    }
}
