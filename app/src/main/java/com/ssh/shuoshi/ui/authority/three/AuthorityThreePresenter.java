package com.ssh.shuoshi.ui.authority.three;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.ui.authority.one.AuthorityOneContract;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * created by hwt on 2020/12/10
 */
public class AuthorityThreePresenter implements AuthorityThreeContract.Presenter{

    private CommonApi mCommonApi;
    private AuthorityThreeContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public AuthorityThreePresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }


    @Override
    public void attachView(@NonNull AuthorityThreeContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }
}
