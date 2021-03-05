package com.ssh.shuoshi.ui.myprescription.main;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.ui.imagediagnose.main.ImageDiagnoseContract;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * created by hwt on 2021/1/2
 */
public class MyPrescriptionPresenter implements MyPrescriptionContract.Presenter{

    private CommonApi mCommonApi;
    private MyPrescriptionContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public MyPrescriptionPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void attachView(@NonNull MyPrescriptionContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }
}
