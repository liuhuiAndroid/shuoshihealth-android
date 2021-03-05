package com.ssh.shuoshi.ui.prescription.westernmedicine.edit;

import androidx.annotation.NonNull;

import com.squareup.otto.Bus;
import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.common.SystemTypeBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.components.storage.UserStorage;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * created by hwt on 2020/12/9
 */
public class EditPrescriptionPresenter implements EditPrescriptionContract.Presenter {

    private EditPrescriptionContract.View mView;

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
    public EditPrescriptionPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
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

    //获取处方、处方订单详细信息
    @Override
    public void getPrescriptionFromId(Integer prescriptionId) {
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

    @Override
    public void putPrescription(Map<String, Object> map) {
        disposables.add(mCommonApi.putPrescription(map)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<Integer>, ObservableSource<Integer>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mView.hideLoading())
                .subscribe(bean -> {
                    mView.addPrescriptionSuccess(map, bean);
                }, throwable -> mView.onError(throwable)));
    }

    @Override
    public void addPrescription(Map<String, Object> map) {
        disposables.add(mCommonApi.addPrescription(map)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<Integer>, ObservableSource<Integer>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mView.hideLoading())
                .subscribe(bean -> {
                    mView.addPrescriptionSuccess(map, bean);
                }, throwable -> mView.onError(throwable)));
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
    public void attachView(@NonNull EditPrescriptionContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

}
