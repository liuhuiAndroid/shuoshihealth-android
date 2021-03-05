package com.ssh.shuoshi.ui.fragment2;

import androidx.annotation.NonNull;

import com.squareup.otto.Bus;
import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.components.storage.UserStorage;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * created by hwt on 2020/12/12
 */
public class Fragment2Presenter implements Fragment2Contract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;
    private Fragment2Contract.View mView;

    @Inject
    public Fragment2Presenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    //获取用户信息
    @Override
    public void getDoctorInfo() {
        disposables.add(mCommonApi.getDoctorInfo()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<HisDoctorBean>, ObservableSource<HisDoctorBean>>) doctorInfo -> CommonApi.flatResponse(doctorInfo))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    mUserStorage.setDoctorInfo(bean);
                    mView.getDoctorInfoSuccess(bean);
                    mUserStorage.setApprovalState(bean.getApprovalState());
                }, throwable -> mView.onError(throwable)));
    }

    @Override
    public void getImagePath(String[] photoPath) {
        disposables.add(mCommonApi.imgDownload(photoPath)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<String>>, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(@io.reactivex.annotations.NonNull HttpResult<List<String>> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<String> bean) throws Exception {
                        mView.imgDownload(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void attachView(@NonNull Fragment2Contract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

}
