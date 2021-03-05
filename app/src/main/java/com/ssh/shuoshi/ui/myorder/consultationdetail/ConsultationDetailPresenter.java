package com.ssh.shuoshi.ui.myorder.consultationdetail;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * created by hwt on 2020/12/9
 */
public class ConsultationDetailPresenter implements ConsultationDetailContract.Presenter {

    private CommonApi mCommonApi;
    private ConsultationDetailContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public ConsultationDetailPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void attachView(@NonNull ConsultationDetailContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }
}
