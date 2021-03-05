package com.ssh.shuoshi.ui.verified.sign;

import androidx.annotation.NonNull;

import com.squareup.otto.Bus;
import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.ca.CAPhoneBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.components.storage.UserStorage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * created by hwt on 2020/12/26
 */
public class PrescriptionSignPresenter implements PrescriptionSignContract.Presenter {

    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;
    private PrescriptionSignContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public PrescriptionSignPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }


    @Override
    public void attachView(@NonNull PrescriptionSignContract.View view) {
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

    @Override
    public void caImgUpload(String mPhotoPath) {
        disposables.add(mCommonApi.caImgUpload(mPhotoPath)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mView.hideLoading())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String bean) throws Exception {
                        mView.caImgUploadSuccess(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
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
    public void caSignPDFNone(Map<String, Object> map) {
        disposables.add(mCommonApi.caSignPDFNone(map)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mView.hideLoading())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String bean) throws Exception {
                        mView.caSignPDFNoneSuccess(bean);
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
    public void getDoctorInfo(boolean init) {
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
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void caFaceSecondCode(Map<String, Object> map) {
        disposables.add(mCommonApi.caFaceSecondCode(map)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<CAPhoneBean>, ObservableSource<CAPhoneBean>>() {
                    @Override
                    public ObservableSource<CAPhoneBean> apply(@io.reactivex.annotations.NonNull HttpResult<CAPhoneBean> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mView.hideLoading())
                .subscribe(new Consumer<CAPhoneBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CAPhoneBean bean) throws Exception {
                        mView.caFaceSecondCodeSuccess(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }
}
