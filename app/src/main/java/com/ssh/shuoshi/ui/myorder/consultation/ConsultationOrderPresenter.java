package com.ssh.shuoshi.ui.myorder.consultation;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.ConsultationBillBean;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ConsultationOrderPresenter implements ConsultationOrderContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private ConsultationOrderContract.View mView;
    private int page = 1;
    private String data = "2021-01";

    public void setData(String data) {
        this.data = data;
    }

    @Inject
    public ConsultationOrderPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void onRefresh() {
        page = 1;
        loadData();
    }

    @Override
    public void loadData() {
        disposables.add(mCommonApi.getConsultationBillList(page, data)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap((Function<HttpResult<ConsultationBillBean>, ObservableSource<ConsultationBillBean>>) listHttpResult -> CommonApi.flatResponse(listHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ConsultationBillBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull ConsultationBillBean beans) {
                        mView.onRefreshCompleted(beans, page == 1);
                        mView.onLoadCompleted(page < beans.getHisConsultationBillDtos().getTotalPage());
                    }
                }, throwable -> {
                    mView.onError(throwable);
                })
        );
    }

    @Override
    public void onLoadMore() {
        page++;
        loadData();
    }


    @Override
    public void attachView(@NonNull ConsultationOrderContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

}
