package com.ssh.shuoshi.ui.diagnosissearch;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.DiagnBean;
import com.ssh.shuoshi.bean.HttpResult;
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
 * created by hwt on 2020/12/9
 */
public class DiagnosisSearchPresenter implements DiagnosisSearchContract.Presenter {

    private DiagnosisSearchContract.View mView;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;
    private int page = 1;
    private String mDiagnName = "";

    @Inject
    public DiagnosisSearchPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void onRefresh(String diagnName) {
        this.mDiagnName = diagnName;
        this.page = 1;
        loadData();
    }

    @Override
    public void loadData() {
        disposables.add(mCommonApi.getDeptDiagnosis(mDiagnName, page)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap(new Function<HttpResult<DiagnBean>, ObservableSource<DiagnBean>>() {
                    @Override
                    public ObservableSource<DiagnBean> apply(@io.reactivex.annotations.NonNull HttpResult<DiagnBean> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DiagnBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull DiagnBean beans) throws Exception {
                        mView.onRefreshCompleted(beans, page == 1);
                        mView.onLoadCompleted(beans.getRows().size() == 20);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                })
        );
    }

    @Override
    public void onLoadMore() {
        page++;
        loadData();
    }

    @Override
    public void attachView(@NonNull DiagnosisSearchContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }
}
