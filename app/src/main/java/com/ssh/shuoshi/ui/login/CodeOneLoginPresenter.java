package com.ssh.shuoshi.ui.login;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.JPushSysMsgRecordPushNewDoctorBean;
import com.ssh.shuoshi.bean.LoginInfoBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.squareup.otto.Bus;

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
public class CodeOneLoginPresenter implements CodeOneLoginContract.Presenter {

    private CodeOneLoginContract.View mLoginView;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;

    @Inject
    public CodeOneLoginPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void attachView(@NonNull CodeOneLoginContract.View view) {
        mLoginView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mLoginView = null;
    }

    @Override
    public void getPhoneCode(String phone) {
        disposables.add(mCommonApi.sendSmsCode(phone)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mLoginView.hideLoading();
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String uuid) throws Exception {
                        mLoginView.refreshSmsCodeUi();
                        mLoginView.SmsCodeSuccess(uuid);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mLoginView.onError(throwable);
                    }
                }));
    }

    @Override
    public void login(String phone, String code, String mUuid) {
        disposables.add(mCommonApi.login(phone, code, mUuid)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<LoginInfoBean>, ObservableSource<LoginInfoBean>>() {
                    @Override
                    public ObservableSource<LoginInfoBean> apply(@io.reactivex.annotations.NonNull HttpResult<LoginInfoBean> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mLoginView.hideLoading();
                    }
                })
                .subscribe(new Consumer<LoginInfoBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull LoginInfoBean bean) throws Exception {
                        mUserStorage.setDoctorInfo(bean.getHisDoctor());
                        mUserStorage.setToken(bean.getToken());
                        mUserStorage.setApprovalState(bean.getHisDoctor().getApprovalState());
                        mLoginView.loginSuccess(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mLoginView.onError(throwable);
                    }
                }));
    }

    public void jpushSysMsgRecordPushNewDoctor() {

    }
}
