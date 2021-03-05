package com.ssh.shuoshi.ui.team.team;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.team.DoctorTeamBean;
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
public class MyTeamPresenter implements MyTeamContract.Presenter {

    private MyTeamContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;

    @Inject
    public MyTeamPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void attachView(@NonNull MyTeamContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

    @Override
    public void getDoctorListTeam() {
        disposables.add(mCommonApi.getDoctorListTeam()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<DoctorTeamBean>, ObservableSource<DoctorTeamBean>>() {
                    @Override
                    public ObservableSource<DoctorTeamBean> apply(@io.reactivex.annotations.NonNull HttpResult<DoctorTeamBean> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mView.hideLoading())
                .subscribe(new Consumer<DoctorTeamBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull DoctorTeamBean bean) throws Exception {
                        mView.getDoctorListTeamSuccess(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }
}
