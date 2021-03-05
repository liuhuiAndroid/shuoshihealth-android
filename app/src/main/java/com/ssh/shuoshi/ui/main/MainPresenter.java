package com.ssh.shuoshi.ui.main;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.JPushSysMsgRecordPushNewDoctorBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.squareup.otto.Bus;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * created by hwt on 2020/12/8
 */
public class MainPresenter implements MainContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private MainContract.View mView;
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;

    @Inject
    public MainPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void attachView(@NonNull MainContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
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


    public void jpushSysMsgRecordPushNewDoctor() {
        mCommonApi.jpushSysMsgRecordPushNewDoctor()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<String>,
                        ObservableSource<String>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginEntity -> {
                });
    }


    @Override
    public void getConsultationInfo(int mId) {
        disposables.add(mCommonApi.getConsultationInfo(mId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<ImageDiagnoseBean.RowsBean>, ObservableSource<ImageDiagnoseBean.RowsBean>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> mView.getConsultationInfoSuccess(bean, mId),
                        throwable -> mView.onError(throwable)));
    }

    @Override
    public void getConsultationInfoJpush(int mId) {
        disposables.add(mCommonApi.getConsultationInfo(mId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<ImageDiagnoseBean.RowsBean>, ObservableSource<ImageDiagnoseBean.RowsBean>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> mView.getConsultationInfoJpushSuccess(bean, mId),
                        throwable -> mView.onError(throwable)));
    }
}
