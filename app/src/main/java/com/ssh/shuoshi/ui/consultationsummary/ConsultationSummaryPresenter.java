package com.ssh.shuoshi.ui.consultationsummary;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.EmrBean;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.squareup.otto.Bus;

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
 * created by hwt on 2020/12/9
 */
public class ConsultationSummaryPresenter implements ConsultationSummaryContract.Presenter {

    private ConsultationSummaryContract.View mView;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;

    @Inject
    public ConsultationSummaryPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void getEmr(int mId) {
        disposables.add(mCommonApi.getEmr(mId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<EmrBean>, ObservableSource<EmrBean>>() {
                    @Override
                    public ObservableSource<EmrBean> apply(@io.reactivex.annotations.NonNull HttpResult<EmrBean> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatNullResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mView.hideLoading())
                .subscribe(new Consumer<EmrBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull EmrBean bean) throws Exception {
                        mView.getEmrSuccess(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void addEmr(Map<String, Object> mData) {
        disposables.add(mCommonApi.addEmr(mData)
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
                        mView.addEmrSuccess(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void changeEmr(Map<String, Object> mData) {
        disposables.add(mCommonApi.changeEmr(mData)
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
                        mView.changeEmrSuccess(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void attachView(@NonNull ConsultationSummaryContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

}
