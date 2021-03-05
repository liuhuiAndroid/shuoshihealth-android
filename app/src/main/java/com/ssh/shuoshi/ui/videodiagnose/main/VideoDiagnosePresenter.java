package com.ssh.shuoshi.ui.videodiagnose.main;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.count.DiagnoseCountBean;

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
public class VideoDiagnosePresenter implements VideoDiagnoseContract.Presenter {

    private CommonApi mCommonApi;
    private VideoDiagnoseContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private int state = 2;

    @Inject
    public VideoDiagnosePresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void attachView(@NonNull VideoDiagnoseContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void getCountMap() {
        disposables.add(mCommonApi.getCountMap(state)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap(new Function<HttpResult<DiagnoseCountBean>, ObservableSource<DiagnoseCountBean>>() {
                    @Override
                    public ObservableSource<DiagnoseCountBean> apply(@io.reactivex.annotations.NonNull HttpResult<DiagnoseCountBean> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DiagnoseCountBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull DiagnoseCountBean beans) throws Exception {
                        mView.setDiagnoseCount(beans);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                })
        );
    }
}
