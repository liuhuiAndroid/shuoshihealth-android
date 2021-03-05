package com.ssh.shuoshi.ui.login;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * created by hwt on 2020/12/9
 */
public class PasswordLoginPresenter implements PasswordLoginContract.Presenter {

    private PasswordLoginContract.View mLoginView;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;

    @Inject
    public PasswordLoginPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }


    @Override
    public void attachView(@NonNull PasswordLoginContract.View view) {
        mLoginView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mLoginView = null;
    }
}
