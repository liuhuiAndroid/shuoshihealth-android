package com.ssh.shuoshi.ui.verified.face;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.ca.CAPhoneBean;
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

public class VerifiedFacePresenter implements VerifiedFaceContract.Presenter {

    private VerifiedFaceContract.View mView;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;

    @Inject
    public VerifiedFacePresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void attachView(@NonNull VerifiedFaceContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

    @Override
    public void caFaceFirstOauth(Map<String, Object> map) {
        disposables.add(mCommonApi.caFaceFirstOauth(map)
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
                        mView.caFaceFirstOauthSuccess(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void caCreateUserId(Map<String, Object> map,int id) {
        disposables.add(mCommonApi.caCreateUserId(map)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<CAPhoneBean>, ObservableSource<CAPhoneBean>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    mView.caCreateUserIdSuccess(bean,id);
                }, throwable -> mView.onError(throwable)));
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
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }
}
