package com.ssh.shuoshi.ui.worksetting;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.DoctorConsultationInfo;
import com.ssh.shuoshi.bean.HttpResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * created by hwt on 2020/12/9
 */
public class WorkSettingPresenter implements WorkSettingContract.Presenter {

    private CommonApi mCommonApi;
    private WorkSettingContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public WorkSettingPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void attachView(@NonNull WorkSettingContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void getDoctorConsultation(int id) {
        mView.showLoading();
        disposables.add(mCommonApi.getDoctorConsultation(id)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<DoctorConsultationInfo>, ObservableSource<DoctorConsultationInfo>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mView.hideLoading())
                .subscribe(bean -> {
                    mView.getDoctorConsultationSuccess(bean);
                }, throwable -> mView.onError(throwable)));
    }

    @Override
    public void getDoctorConsultationInfo() {
        mView.showLoading();
        disposables.add(mCommonApi.getDoctorConsultationInfo()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<DoctorConsultationInfo>, ObservableSource<DoctorConsultationInfo>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mView.hideLoading())
                .subscribe(bean -> {
                    mView.getDoctorConsultationInfoSuccess(bean);
                }, throwable -> mView.onError(throwable)));
    }

    @Override
    public void editDoctorConsultation(int id, Integer consultationTypeId, Double price, Integer enableFlag) {
        mView.showLoading();
        disposables.add(mCommonApi.editDoctorConsultation(id, consultationTypeId, price, enableFlag)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<Integer>, ObservableSource<Integer>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mView.hideLoading())
                .subscribe(bean -> {
                    mView.onEditDoctorConsultationSuccess();
                }, throwable -> mView.onError(throwable)));
    }
}
