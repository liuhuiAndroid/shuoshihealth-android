package com.ssh.shuoshi.ui.videodiagnose.videoend;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;

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
public class VideoDiagnoseEndPresenter implements VideoDiagnoseEndContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private VideoDiagnoseEndContract.View mView;
    private int page = 1;
    private int state = 2;

    @Inject
    public VideoDiagnoseEndPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void onRefresh() {
        page = 1;
        loadData();
    }

    @Override
    public void loadData() {
        disposables.add(mCommonApi.getTuEndList(page, state)
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
                        mView.onLoadCompleted(beans.getRows().size() == 10);
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
    public void attachView(@NonNull VideoDiagnoseEndContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }
}
