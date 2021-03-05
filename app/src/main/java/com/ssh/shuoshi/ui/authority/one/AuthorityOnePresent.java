package com.ssh.shuoshi.ui.authority.one;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.AreaBean;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.SysDeptBean;
import com.ssh.shuoshi.bean.pickview.SysDeptNameBean;
import com.ssh.shuoshi.bean.pickview.SysTitleNameBean;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * created by hwt on 2020/12/10
 */
public class AuthorityOnePresent implements AuthorityOneContract.Presenter {

    private CommonApi mCommonApi;
    private AuthorityOneContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public AuthorityOnePresent(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void attachView(@NonNull AuthorityOneContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void getSysDept() {
        disposables.add(mCommonApi.getSysDept()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<SysDeptNameBean>, ObservableSource<SysDeptNameBean>>() {
                    @Override
                    public ObservableSource<SysDeptNameBean> apply(@io.reactivex.annotations.NonNull HttpResult<SysDeptNameBean> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SysDeptNameBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull SysDeptNameBean bean) throws Exception {
                        mView.getSysDeptSuccess(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void getDoctorTitleDict() {
        disposables.add(mCommonApi.getDoctorTitleDict()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<SysTitleNameBean>, ObservableSource<SysTitleNameBean>>() {
                    @Override
                    public ObservableSource<SysTitleNameBean> apply(@io.reactivex.annotations.NonNull HttpResult<SysTitleNameBean> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SysTitleNameBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull SysTitleNameBean bean) throws Exception {
                        mView.getDoctorTitleDictSuccess(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void getProvinceDict() {
        disposables.add(mCommonApi.getProvinceDict()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<AreaBean>>, ObservableSource<List<AreaBean>>>() {
                    @Override
                    public ObservableSource<List<AreaBean>> apply(@io.reactivex.annotations.NonNull HttpResult<List<AreaBean>> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AreaBean>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<AreaBean> bean) throws Exception {
                        mView.getAddressSuccess(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

}
