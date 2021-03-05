package com.ssh.shuoshi.ui.verified.submit;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.squareup.otto.Bus;

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
 * created by hwt on 2020/12/27
 */
public class PrescriptionSubmitCheckPresenter implements PrescriptionSubmitCheckContract.Presenter {

    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;
    private PrescriptionSubmitCheckContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public PrescriptionSubmitCheckPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void attachView(@NonNull PrescriptionSubmitCheckContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void getImagePath(String[] photoPath) {
        disposables.add(mCommonApi.imgDownload(photoPath)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<String>>, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(@io.reactivex.annotations.NonNull HttpResult<List<String>> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading();
                    }
                })
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<String> bean) throws Exception {
                        mView.imgDownload(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void changePrescriptionState(int prescriptionId) {
        disposables.add(mCommonApi.changePrescriptionState(prescriptionId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<Integer>, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(@io.reactivex.annotations.NonNull HttpResult<Integer> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mView.hideLoading())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Integer bean) throws Exception {
                        mView.changePrescriptionStateSuccess(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void getDoctorInfo() {
        disposables.add(mCommonApi.getDoctorInfo()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<HisDoctorBean>, ObservableSource<HisDoctorBean>>() {
                    @Override
                    public ObservableSource<HisDoctorBean> apply(@io.reactivex.annotations.NonNull HttpResult<HisDoctorBean> doctorInfo) throws Exception {
                        return CommonApi.flatResponse(doctorInfo);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HisDoctorBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull HisDoctorBean bean) throws Exception {
                        mUserStorage.setDoctorInfo(bean);
                        mUserStorage.setApprovalState(bean.getApprovalState());
                        mView.setDoctorInfoSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
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

    @Override
    public void getPrescriptionFromIdForFinish(int prescriptionId) {
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
                        mView.getPrescriptionFromIdSuccessForFinish(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }
}
