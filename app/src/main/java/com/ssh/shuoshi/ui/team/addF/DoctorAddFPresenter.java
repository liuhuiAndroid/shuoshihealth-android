package com.ssh.shuoshi.ui.team.addF;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.DiagnBean;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.team.DistinctDoctorBean;
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
 * created by hwt on 2021/1/3
 */
public class DoctorAddFPresenter implements DoctorAddFContract.Presenter {

    private DoctorAddFContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;

    private int page = 1;
    private String name = "";

    @Inject
    public DoctorAddFPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }


    @Override
    public void attachView(@NonNull DoctorAddFContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

    @Override
    public void onRefresh(String name) {
        this.name = name;
        this.page = 1;
        loadData();
    }

    @Override
    public void loadData() {
        disposables.add(mCommonApi.getDistinctDoctorName(page,name)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap(new Function<HttpResult<DistinctDoctorBean>, ObservableSource<DistinctDoctorBean>>() {
                    @Override
                    public ObservableSource<DistinctDoctorBean> apply(@io.reactivex.annotations.NonNull HttpResult<DistinctDoctorBean> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DistinctDoctorBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull DistinctDoctorBean beans) throws Exception {
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
}
