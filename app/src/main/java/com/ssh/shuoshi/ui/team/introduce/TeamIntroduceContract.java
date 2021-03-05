package com.ssh.shuoshi.ui.team.introduce;

import com.ssh.shuoshi.ui.BasePresenter;
import com.ssh.shuoshi.ui.BaseView;

import java.util.Map;

/**
 * created by hwt on 2021/1/3
 */
public interface TeamIntroduceContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void onError(Throwable throwable);

        void putDoctorTeamSuccess(Integer bean);
    }

    interface Presenter extends BasePresenter<View> {

        void putDoctorTeam(Map<String, Object> map);
    }
}
