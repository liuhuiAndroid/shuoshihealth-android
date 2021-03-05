package com.ssh.shuoshi.ui.prescription.westernmedicine.commonlyprescription;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.template.PrescriptionTemplateBean;
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
public class CommonlyPrescriptionPresenter implements CommonlyPrescriptionContract.Presenter {

    private CommonlyPrescriptionContract.View mView;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;
    private int page = 1;
    private int perscriptionTypeId = 1;

    @Inject
    public CommonlyPrescriptionPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void onRefresh(int type) {
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
    public void attachView(@NonNull CommonlyPrescriptionContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

}
