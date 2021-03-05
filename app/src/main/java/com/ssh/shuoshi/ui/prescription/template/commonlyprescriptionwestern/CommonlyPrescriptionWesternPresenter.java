package com.ssh.shuoshi.ui.prescription.template.commonlyprescriptionwestern;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.OftenDrugBean;
import com.ssh.shuoshi.bean.template.PrescriptionTemplateBean;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class CommonlyPrescriptionWesternPresenter implements CommonlyPrescriptionWesternContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private CommonlyPrescriptionWesternContract.View mView;
    private int page = 1;
    //只获取西医处方
    private int perscriptionTypeId = 1;

    @Inject
    public CommonlyPrescriptionWesternPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void onRefresh() {
        page = 1;
        loadData();
    }

    @Override
    public void loadData() {
        disposables.add(mCommonApi.getPrescriptionTemplate(page, perscriptionTypeId)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap(new Function<HttpResult<PrescriptionTemplateBean>, ObservableSource<PrescriptionTemplateBean>>() {
                    @Override
                    public ObservableSource<PrescriptionTemplateBean> apply(@io.reactivex.annotations.NonNull HttpResult<PrescriptionTemplateBean> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PrescriptionTemplateBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull PrescriptionTemplateBean beans) throws Exception {
                        mView.onRefreshCompleted(beans, page == 1);
                        mView.onLoadCompleted(page < beans.getTotalPage());
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
    public void deletePrescriptionTemplate(int id) {
        disposables.add(mCommonApi.deletePrescriptionTemplate(id)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap(new Function<HttpResult<Integer>, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(@io.reactivex.annotations.NonNull HttpResult<Integer> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mView.hideLoading())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Integer beans) throws Exception {
                        mView.deletePrescriptionTemplateSuccess(beans);
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
    public void attachView(@NonNull CommonlyPrescriptionWesternContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }


}
