package com.ssh.shuoshi.ui.videodiagnose.videoing;

import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2020/12/9
 */
public interface VideoDiagnoseIngContract {
    interface View extends BaseView {
        void onError(Throwable throwable);

        void onRefreshCompleted(ImageDiagnoseBean beans, boolean isClear);

        void onLoadCompleted(boolean isLoadAll);
    }

    interface Presenter extends BasePresenter<View> {

        void onRefresh();

        void loadData();

        void onLoadMore();
    }
}
