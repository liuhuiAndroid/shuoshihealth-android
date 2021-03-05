package com.ssh.shuoshi.ui.historymessage;

import android.content.Context;
import android.widget.ImageView;

import com.ssh.shuoshi.bean.history.ImGetHistoryBean;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.List;

public interface HistoryMessageContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void onRefreshCompleted(ImGetHistoryBean beans, boolean isClear);

        void onLoadCompleted(boolean isLoadAll);

        void getConsultationInfoSuccess(ImageDiagnoseBean.RowsBean bean);

        void getImagePathSuccess(List<String> bean, ImageView imageView1, ImageView imageView2,
                                 ImageView imageView3);

        void getImageSingleSuccess(List<String> bean, ImageView imageView1);

        void imgDownload(List<String> bean);

        void getTeamImagePathSuccess(List<String> imgList, int position);
    }

    interface Presenter extends BasePresenter<View> {

        void onRefresh(String conversationID, int mId, List<String> imgPath);

        void loadData();

        void onLoadMore();

        void getConsultationInfo(int mId);

        void getImagePath(String[] photoPath, ImageView imageView1, ImageView imageView2,
                          ImageView imageView3, Context context);

        void getImagePathSingle(String[] photoPath, ImageView imageView1);

        void getImagePath(String[] photoPath);

        void getTeamImagePath(String[] path, int position);
    }
}
