package com.ssh.shuoshi.ui.prescription.westernmedicine.add;

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
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * created by hwt on 2020/12/9
 */
public class NewDrugsPresenter implements NewDrugsContract.Presenter {

    private NewDrugsContract.View mView;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;
    private int page = 1;
    private String mName = "";
    private int mType;
    private Integer mPhamVendorId;

    @Inject
    public NewDrugsPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
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
        disposables.add(mCommonApi.getMyYaoList(mName, page, mType, mPhamVendorId)
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
                        mView.onRefreshCompleted(beans, page == 1);
//                        mView.onLoadCompleted(beans.getRows().size() == 20);
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
    public void attachView(@NonNull NewDrugsContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

}
