package com.ssh.shuoshi.ui.fragment1;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.UserSigBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.squareup.otto.Bus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * created by hwt on 2020/12/12
 */
public class Fragment1Presenter implements Fragment1Contract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Fragment1Contract.View mView;
    private Bus mBus;
    private UserStorage mUserStorage;
    private int page = 1;

    @Inject
    public Fragment1Presenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
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
                        mView.getDoctorInfoSuccess(bean);
                        mUserStorage.setApprovalState(bean.getApprovalState());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable, 1);
                    }
                }));
    }

    @Override
    public void onRefresh() {
        this.page = 1;
        loadData();
    }

    @Override
    public void onLoadMore() {
        page++;
        loadData();
    }

    @Override
    public void loadData() {
        disposables.add(mCommonApi.getMyNewList(page)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap(new Function<HttpResult<ImageDiagnoseBean>, ObservableSource<ImageDiagnoseBean>>() {
                    @Override
                    public ObservableSource<ImageDiagnoseBean> apply(@io.reactivex.annotations.NonNull HttpResult<ImageDiagnoseBean> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ImageDiagnoseBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull ImageDiagnoseBean beans) throws Exception {
                        mView.onRefreshCompleted(beans, page == 1);
                        mView.onLoadCompleted(page < beans.getTotalPage());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable, 2);
                    }
                })
        );
    }

    @Override
    public void getUserSigByUserNo(String userNo, HisDoctorBean bean) {
        disposables.add(mCommonApi.getUserSigByUserNo(userNo)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap((Function<HttpResult<String>, ObservableSource<String>>)
                        listHttpResult -> CommonApi.flatResponse(listHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(beans -> mView.getUserSigByUserNoSuccess(beans, bean),
                        throwable -> mView.onError(throwable, 3)));
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
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<String> bean) throws Exception {
                        mView.imgDownload(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable, 4);
                    }
                }));
    }

    @Override
    public void attachView(@NonNull Fragment1Contract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void getConsultationInfo(int mId) {
        disposables.add(mCommonApi.getConsultationInfo(mId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<ImageDiagnoseBean.RowsBean>, ObservableSource<ImageDiagnoseBean.RowsBean>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> mView.getConsultationInfoSuccess(bean),
                        throwable -> mView.onError(throwable, 5)));
    }

    @Override
    public void getConsultationInfoJpush(int mId) {
        disposables.add(mCommonApi.getConsultationInfo(mId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<ImageDiagnoseBean.RowsBean>, ObservableSource<ImageDiagnoseBean.RowsBean>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> mView.getConsultationInfoJpushSuccess(bean, mId),
                        throwable -> mView.onError(throwable, 10086)));
    }
}
