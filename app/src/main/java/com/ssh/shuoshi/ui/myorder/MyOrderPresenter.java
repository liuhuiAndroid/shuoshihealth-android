package com.ssh.shuoshi.ui.myorder;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.ui.imagediagnose.main.ImageDiagnoseContract;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * created by hwt on 2020/12/9
 */
public class MyOrderPresenter implements ImageDiagnoseContract.Presenter {

    private CommonApi mCommonApi;
    private ImageDiagnoseContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public MyOrderPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void attachView(@NonNull ImageDiagnoseContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void getCountMap() {

    }
}
