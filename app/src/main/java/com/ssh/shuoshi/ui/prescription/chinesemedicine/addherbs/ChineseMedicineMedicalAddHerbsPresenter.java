package com.ssh.shuoshi.ui.prescription.chinesemedicine.addherbs;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.DrugBean;
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
public class ChineseMedicineMedicalAddHerbsPresenter implements ChineseMedicineMedicalAddHerbsContract.Presenter {

    private ChineseMedicineMedicalAddHerbsContract.View mView;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;
    private int page = 1;
    private int perscriptionTypeId = 2;

    @Inject
    public ChineseMedicineMedicalAddHerbsPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void attachView(@NonNull ChineseMedicineMedicalAddHerbsContract.View view) {
        mView = view;
    }

    @Override
    public void loadData(String name, Integer phamVendorId) {
        disposables.add(mCommonApi.getChineseDrugList(name, 1, 2, phamVendorId)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap(new Function<HttpResult<DrugBean>, ObservableSource<DrugBean>>() {
                    @Override
                    public ObservableSource<DrugBean> apply(@io.reactivex.annotations.NonNull HttpResult<DrugBean> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DrugBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull DrugBean beans) throws Exception {
                        mView.onLoadDataSuccess(beans);
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
    public void onRefresh() {
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
                        mView.onRefreshCompleted(beans);
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
    public void detachView() {
        disposables.clear();
        mView = null;
    }

}
