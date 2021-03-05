package com.ssh.shuoshi.ui.myprescription.wait;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.prescription.PrescriptionStateBean;
import com.ssh.shuoshi.ui.myprescription.fail.MyPrescriptionFailContract;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * created by hwt on 2021/1/4
 */
public class MyPrescriptionWaitPresenter implements MyPrescriptionWaitContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private MyPrescriptionWaitContract.View mView;
    private int page = 1;
    private int state = 2;

    @Inject
    public MyPrescriptionWaitPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void onRefresh() {
        page = 1;
        loadData();
    }

    @Override
    public void loadData() {
        disposables.add(mCommonApi.getByApprovalStatePrescription(page, state)
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .switchMap(new Function<HttpResult<PrescriptionStateBean>, ObservableSource<PrescriptionStateBean>>() {
                            @Override
                            public ObservableSource<PrescriptionStateBean> apply(@io.reactivex.annotations.NonNull HttpResult<PrescriptionStateBean> listHttpResult) throws Exception {
                                return CommonApi.flatResponse(listHttpResult);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<PrescriptionStateBean>() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull PrescriptionStateBean beans) throws Exception {
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
    public void attachView(@NonNull MyPrescriptionWaitContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }
}
