package com.ssh.shuoshi.ui.team.team;

import com.ssh.shuoshi.bean.team.DoctorTeamBean;
import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

/**
 * created by hwt on 2021/1/3
 */
public interface MyTeamContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void getDoctorListTeamSuccess(DoctorTeamBean bean);
    }

    interface Presenter extends BasePresenter<View> {

        void getDoctorListTeam();
    }
}
