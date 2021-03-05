package com.ssh.shuoshi.ui.medicalhistory;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.patient.FollowUpListBean;
import com.ssh.shuoshi.bean.patient.MedicalHistoryBean;

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
 * created by hwt on 2020/12/27
 */
public class MedicalHistoryPresenter implements MedicalHistoryContract.Presenter {

    private MedicalHistoryContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private int mPatientId;
    private int page = 1;

    @Inject
    public MedicalHistoryPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }


    @Override
    public void attachView(@NonNull MedicalHistoryContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

    @Override
    public void onRefresh(int patientId) {
        this.mPatientId = patientId;
        this.page = 1;
        loadData();
    }

    @Override
    public void loadData() {
        disposables.add(mCommonApi.getMyPatientListByPatientId(page, mPatientId)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap(new Function<HttpResult<MedicalHistoryBean>, ObservableSource<MedicalHistoryBean>>() {
                    @Override
                    public ObservableSource<MedicalHistoryBean> apply(@io.reactivex.annotations.NonNull HttpResult<MedicalHistoryBean> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MedicalHistoryBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull MedicalHistoryBean beans) throws Exception {
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
    public void getImagePath(String[] photoPath, int position) {
        disposables.add(mCommonApi.imgDownload(photoPath)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<String>>, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(@io.reactivex.annotations.NonNull HttpResult<List<String>> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading();
                    }
                })
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<String> bean) throws Exception {
                        mView.imgDownload(bean,position);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }
}
