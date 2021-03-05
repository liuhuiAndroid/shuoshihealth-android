package com.ssh.shuoshi.ui.team.doctorteam;

import com.ssh.shuoshi.bean.team.DoctorTeamDetailBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.Map;

/**
 * created by hwt on 2021/1/3
 */
public interface MyDoctorTeamContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void getDoctorTeamDetailSuccess(DoctorTeamDetailBean bean);

        void putDoctorTeamSuccess(Integer bean);

        void exitDoctorTeamSuccess(Integer bean);
    }

    interface Presenter extends BasePresenter<View> {

        void getDoctorTeamDetail(int teamId);

        void putDoctorTeam(Map<String, Object> map);

        void exitDoctorTeam(int teamId);
    }
}
