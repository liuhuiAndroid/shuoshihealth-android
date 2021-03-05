package com.ssh.shuoshi.ui.setting;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.ui.worksetting.WorkSettingContract;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * created by hwt on 2020/12/12
 */
public class SettingPresenter implements SettingContract.Presenter {

    private CommonApi mCommonApi;
    private SettingContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public SettingPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void attachView(@NonNull SettingContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }
}
