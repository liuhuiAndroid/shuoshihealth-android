package com.ssh.shuoshi.ui.prescription.template.add;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.DictListBean;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.common.SystemTypeBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.squareup.otto.Bus;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class AddCommonlyTempaltePresenter implements AddCommonlyTempalteContract.Presenter {

    private AddCommonlyTempalteContract.View mView;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;

    //用药频次
    private String frequency = "prescription_frequency";
    //单次计量单位
    private String dosageUnits = "prescription_dosage_units";
    //用法
    private String administrationRoute = "prescription_administration_route";

    @Inject
    public AddCommonlyTempaltePresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void attachView(@NonNull AddCommonlyTempalteContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

    @Override
    public void getFrequencyList() {
        disposables.add(mCommonApi.getDictionariesList(frequency)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<SystemTypeBean>, ObservableSource<SystemTypeBean>>() {
                    @Override
                    public ObservableSource<SystemTypeBean> apply(@io.reactivex.annotations.NonNull HttpResult<SystemTypeBean> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SystemTypeBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull SystemTypeBean bean) throws Exception {
                        mView.getFrequencyList(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void getDosageUnits() {
        disposables.add(mCommonApi.getDictionariesList(dosageUnits)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<SystemTypeBean>, ObservableSource<SystemTypeBean>>() {
                    @Override
                    public ObservableSource<SystemTypeBean> apply(@io.reactivex.annotations.NonNull HttpResult<SystemTypeBean> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SystemTypeBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull SystemTypeBean bean) throws Exception {
                        mView.getDosageUnitsList(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void getAdministrationRoute() {
        disposables.add(mCommonApi.getDictionariesList(administrationRoute)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<SystemTypeBean>, ObservableSource<SystemTypeBean>>() {
                    @Override
                    public ObservableSource<SystemTypeBean> apply(@io.reactivex.annotations.NonNull HttpResult<SystemTypeBean> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SystemTypeBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull SystemTypeBean bean) throws Exception {
                        mView.getAdministrationRouteList(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void addPrescriptionTemplate(Map<String, Object> mMap) {
        disposables.add(mCommonApi.addPrescriptionTemplate(mMap)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<Integer>, ObservableSource<Integer>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mView.hideLoading())
                .subscribe(bean -> {
                    mView.addPrescriptionTemplateSuccess();
                }, throwable -> mView.onError(throwable)));
    }

    @Override
    public void changePrescriptionTemplate(Map<String, Object> mMap) {
        disposables.add(mCommonApi.changePrescriptionTemplate(mMap)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<Integer>, ObservableSource<Integer>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mView.hideLoading())
                .subscribe(bean -> {
                    mView.addPrescriptionTemplateSuccess();
                }, throwable -> mView.onError(throwable)));
    }

}
