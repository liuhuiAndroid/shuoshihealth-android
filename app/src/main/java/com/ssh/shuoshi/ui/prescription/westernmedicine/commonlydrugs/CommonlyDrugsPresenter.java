package com.ssh.shuoshi.ui.prescription.westernmedicine.commonlydrugs;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.OftenDrugBean;
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
public class CommonlyDrugsPresenter implements CommonlyDrugsContract.Presenter {

    private CommonlyDrugsContract.View mView;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;
    private int page = 1;
    private int mType;
    private Integer mPhamVendorId;

    @Inject
    public CommonlyDrugsPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void getMyOftenList(int type, Integer phamVendorId) {
        this.page = 1;
        this.mType = type;
        this.mPhamVendorId = phamVendorId;
        loadData();
    }

    @Override
    public void loadData() {
        disposables.add(mCommonApi.getMyOftenList(mType, page, mPhamVendorId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<OftenDrugBean>, ObservableSource<OftenDrugBean>>() {
                    @Override
                    public ObservableSource<OftenDrugBean> apply(@io.reactivex.annotations.NonNull HttpResult<OftenDrugBean> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OftenDrugBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull OftenDrugBean bean) throws Exception {
                        mView.onRefreshCompleted(bean, page == 1);
                        mView.onLoadCompleted(page < bean.getTotalPage());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void onLoadMore() {
        page++;
        loadData();
    }

    @Override
    public void attachView(@NonNull CommonlyDrugsContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

}
