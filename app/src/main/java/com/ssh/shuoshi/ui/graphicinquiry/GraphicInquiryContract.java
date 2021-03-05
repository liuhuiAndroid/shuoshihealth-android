package com.ssh.shuoshi.ui.graphicinquiry;

import android.content.Context;
import android.widget.ImageView;

import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.common.SystemTypeBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.List;

/**
 * created by hwt on 2020/12/18
 */
public interface GraphicInquiryContract {

    interface View extends BaseView {

        void onError(Throwable throwable);

        void consultationSuccess(String bean);

        void exitConsultationSuccess(String bean, String reason);

        void endConsultationSuccess(String bean);

        void getConsultationInfoSuccess(ImageDiagnoseBean.RowsBean bean, boolean init);

        void getConsultationInfoSuccessForExit(ImageDiagnoseBean.RowsBean bean);

        void getConsultationInfoSuccessForPrescriptionCard(ImageDiagnoseBean.RowsBean bean);

        void getImagePathSuccess(List<String> bean, ImageView imageView1, ImageView imageView2,
                                 ImageView imageView3);

        void getReasonList(SystemTypeBean bean);

        void getUserSigByUserNoSuccess(String bean);

        void getTeamImagePathSuccess(List<String> imgList, int position);
    }

    interface Presenter extends BasePresenter<View> {

        void endConsultation(int mId);

        void exitConsultation(int id, String reason);

        void receiveConsultation(int mId);

        void getConsultationInfo(int mId, boolean init);

        void getConsultationInfoForExit(int mId);

        void getConsultationInfoForPrescriptionCard(int mId);

        void getImagePath(String[] photoPath, ImageView imageView1, ImageView imageView2,
                          ImageView imageView3, Context context);

        void getReturnDiagnoseReason();

        void pushVideoConsulationNotice(int id);

        void getUserSigByUserNo(String userNo);

        void getTeamImagePath(String[] path, int position);
    }
}
