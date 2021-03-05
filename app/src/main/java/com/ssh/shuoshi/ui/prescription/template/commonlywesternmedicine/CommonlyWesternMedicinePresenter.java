package com.ssh.shuoshi.ui.prescription.template.commonlywesternmedicine;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.OftenDrugBean;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class CommonlyWesternMedicinePresenter implements CommonlyWesternMedicineContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private CommonlyWesternMedicineContract.View mView;
    private int page = 1;
    private int state = 1;
    private Integer mPhamVendorId;

    @Inject
    public CommonlyWesternMedicinePresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void onRefresh(int type, Integer phamVendorId) {
        page = 1;
        this.mPhamVendorId = phamVendorId;
        loadData();
    }

    @Override
    public void loadData() {
        disposables.add(mCommonApi.getMyOftenList(state, page, mPhamVendorId)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap(new Function<HttpResult<OftenDrugBean>, ObservableSource<OftenDrugBean>>() {
                    @Override
                    public ObservableSource<OftenDrugBean> apply(@io.reactivex.annotations.NonNull HttpResult<OftenDrugBean> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OftenDrugBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull OftenDrugBean beans) throws Exception {
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
    public void deleteMyOftenList(int id) {
        disposables.add(mCommonApi.deleteMyOftenList(id)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mView.hideLoading())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String beans) throws Exception {
                        mView.deleteMyOftenListSuccess(beans);
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
    public void attachView(@NonNull CommonlyWesternMedicineContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }


}
