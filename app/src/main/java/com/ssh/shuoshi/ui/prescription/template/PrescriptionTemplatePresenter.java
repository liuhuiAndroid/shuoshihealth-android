package com.ssh.shuoshi.ui.prescription.template;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * created by hwt on 2020/12/22
 */
public class PrescriptionTemplatePresenter implements PrescriptionTemplateContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private PrescriptionTemplateContract.View mView;
    private Bus mBus;
    private UserStorage mUserStorage;

    @Inject
    public PrescriptionTemplatePresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void attachView(@NonNull PrescriptionTemplateContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }
}
