package com.ssh.shuoshi.ui.myorder.prescriptiondetail;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * created by hwt on 2020/12/9
 */
public class PrescriptionDetailPresenter implements PrescriptionDetailContract.Presenter {

    private CommonApi mCommonApi;
    private PrescriptionDetailContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public PrescriptionDetailPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void attachView(@NonNull PrescriptionDetailContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }
}
