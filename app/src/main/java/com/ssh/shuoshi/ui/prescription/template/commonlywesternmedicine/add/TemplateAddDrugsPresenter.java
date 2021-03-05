package com.ssh.shuoshi.ui.prescription.template.commonlywesternmedicine.add;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.squareup.otto.Bus;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;

/**
 * created by hwt on 2020/12/22
 */
public class TemplateAddDrugsPresenter implements TemplateAddDrugsContract.Presenter {

    private TemplateAddDrugsContract.View mView;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;
    private int page = 1;
    private String mName = "";
    private int mType;
    //因为这是找西成药，所以写死 西医 1
    private int mPhamCategoryId = 1;

    private Integer mPhamVendorId;

    @Inject
    public TemplateAddDrugsPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void onRefresh(String name, int type, Integer phamVendorId) {
        this.mName = name;
        this.mType = type;
        this.page = 1;
        this.mPhamVendorId = phamVendorId;
        loadData();
    }

    @Override
    public void loadData() {
        disposables.add(mCommonApi.getMyYaoList(mName, page, mPhamCategoryId, mPhamVendorId)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap((Function<HttpResult<DrugBean>, ObservableSource<DrugBean>>) listHttpResult ->
                        CommonApi.flatResponse(listHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(beans -> {
                    mView.onRefreshCompleted(beans, page == 1);
                    mView.onLoadCompleted(page < beans.getTotalPage());
                }, throwable -> mView.onError(throwable))
        );
    }

    @Override
    public void onLoadMore() {
        page++;
        loadData();
    }

    @Override
    public void addMyOftenList(int id, int pos) {
        disposables.add(mCommonApi.addMyOftenList(id)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap((Function<HttpResult<String>, ObservableSource<String>>) listHttpResult ->
                        CommonApi.flatResponse(listHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mView.hideLoading())
                .subscribe(beans -> mView.addMyOftenListSuccess(beans, pos),
                        throwable -> mView.onError(throwable))
        );
    }

    @Override
    public void attachView(@NonNull TemplateAddDrugsContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }
}
