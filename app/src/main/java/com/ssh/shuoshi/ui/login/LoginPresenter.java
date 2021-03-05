package com.ssh.shuoshi.ui.login;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.JPushSysMsgRecordPushNewDoctorBean;
import com.ssh.shuoshi.bean.LoginEntity;
import com.ssh.shuoshi.bean.SmsCodeBean;
import com.ssh.shuoshi.bean.TestBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.squareup.otto.Bus;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * created by hwt on 2020/12/8
 */
public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mLoginView;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;

    @Inject
    public LoginPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    public void jpushSysMsgRecordPushNewDoctor() {

    }

    @Override
    public void attachView(@NonNull LoginContract.View view) {
        mLoginView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mLoginView = null;
    }


}
