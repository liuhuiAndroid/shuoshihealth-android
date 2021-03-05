package com.ssh.shuoshi.ui.worksetting.timesetting;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.DoctorWeekScheduleBean;
import com.ssh.shuoshi.bean.HttpResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 医生排班设置
 */
public class ServiceTimeSettingPresenter implements ServiceTimeSettingContract.Presenter {

    private CommonApi mCommonApi;
    private ServiceTimeSettingContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public ServiceTimeSettingPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void attachView(@NonNull ServiceTimeSettingContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void getScheduleListByMonth(String date1, String date2) {
        disposables.add(mCommonApi.getScheduleListByMonth(date1,date2)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<List<DoctorWeekScheduleBean>>, ObservableSource<List<DoctorWeekScheduleBean>>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> mView.getScheduleListByMonthSuccess(bean),
                        throwable -> mView.onError(throwable)));
    }

    @Override
    public void getDoctorCurrentWeekScheduleListDate(String date) {
        disposables.add(mCommonApi.getDoctorCurrentWeekScheduleListDate(date)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<DoctorWeekScheduleBean>>, ObservableSource<List<DoctorWeekScheduleBean>>>() {
                    @Override
                    public ObservableSource<List<DoctorWeekScheduleBean>> apply(@io.reactivex.annotations.NonNull HttpResult<List<DoctorWeekScheduleBean>> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DoctorWeekScheduleBean>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<DoctorWeekScheduleBean> bean) throws Exception {
                        mView.getDoctorCurrentWeekScheduleDate(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void copyDoctorLastWeekScheduleListDate(String date) {
        disposables.add(mCommonApi.copyDoctorLastWeekScheduleListDate(date)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String bean) throws Exception {
                        mView.copyDoctorLastWeekScheduleDate(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void deleteDoctorSchedule(int id) {
        disposables.add(mCommonApi.deleteDoctorSchedule(id)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String bean) throws Exception {
                        mView.hideLoading();
                        mView.deleteScheduleSuccess(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }
}
