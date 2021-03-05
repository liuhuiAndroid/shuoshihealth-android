package com.ssh.shuoshi.ui.worksetting.addtime;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.ui.worksetting.WorkSettingContract;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;

/**
 * created by hwt on 2020/12/11
 */
public class ServiceAddTimePresenter implements ServiceAddTimeContract.Presenter {

    private CommonApi mCommonApi;
    private ServiceAddTimeContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public ServiceAddTimePresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void attachView(@NonNull ServiceAddTimeContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void addDoctorSchedule(List<Map<String, Object>> list) {
        mView.showLoading();
        disposables.add(mCommonApi.addDoctorSchedule(list)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<String>, ObservableSource<String>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mView.hideLoading())
                .subscribe(bean -> {
                    mView.addDoctorScheduleSuccess();
                }, throwable -> mView.onError(throwable)));
    }

}
