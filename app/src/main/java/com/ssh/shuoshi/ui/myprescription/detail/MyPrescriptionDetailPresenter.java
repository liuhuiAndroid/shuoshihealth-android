package com.ssh.shuoshi.ui.myprescription.detail;

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

/**
 * created by hwt on 2021/1/2
 */
public class MyPrescriptionDetailPresenter implements MyPrescriptionDetailContract.Presenter{

    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;
    private MyPrescriptionDetailContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public MyPrescriptionDetailPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }


    @Override
    public void attachView(@NonNull MyPrescriptionDetailContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void getPrescriptionFromId(int prescriptionId) {
        disposables.add(mCommonApi.getPrescriptionFromId(prescriptionId)
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
                        mView.getPrescriptionFromIdSuccess(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }
}
