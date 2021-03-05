package com.ssh.shuoshi.ui.prescription.westernmedicine.history;

import androidx.annotation.NonNull;

import com.squareup.otto.Bus;
import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.components.storage.UserStorage;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class HistoryPrescriptionPresenter implements HistoryPrescriptionContract.Presenter {

    private HistoryPrescriptionContract.View mView;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;

    @Inject
    public HistoryPrescriptionPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void attachView(@NonNull HistoryPrescriptionContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

    @Override
    public void getPrescriptionByPatientId(int patientId, int perscriptionTypeId) {
        disposables.add(mCommonApi.getPrescriptionByPatientId(patientId, perscriptionTypeId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<HisPrescriptionDtoBean>, ObservableSource<HisPrescriptionDtoBean>>() {
                    @Override
                    public ObservableSource<HisPrescriptionDtoBean> apply(@io.reactivex.annotations.NonNull HttpResult<HisPrescriptionDtoBean> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HisPrescriptionDtoBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull HisPrescriptionDtoBean bean) throws Exception {
                        mView.getPrescription(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }
}
