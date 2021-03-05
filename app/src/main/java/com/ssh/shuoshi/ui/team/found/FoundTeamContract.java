package com.ssh.shuoshi.ui.team.found;

import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.Map;

/**
 * created by hwt on 2021/1/3
 */
public interface FoundTeamContract {
    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void addDoctorTeamSuccess(String bean);
    }

    interface Presenter extends BasePresenter<View> {


        void addDoctorTeam(Map<String, Object> map);
    }
}
