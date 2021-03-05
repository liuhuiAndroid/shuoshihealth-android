package com.ssh.shuoshi.ui.worksetting.servicenote;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.inter.MyObserver;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * created by hwt on 2020/12/9
 */
public class ServiceNotePresenter implements ServiceNoteContract.Presenter {

    private CommonApi mCommonApi;
    private ServiceNoteContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public ServiceNotePresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void attachView(@NonNull ServiceNoteContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void addDoctorConsultation(int consultationTypeId) {
        mCommonApi.addDoctorConsultation(consultationTypeId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<Integer>, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(@io.reactivex.annotations.NonNull HttpResult<Integer> loginEntityHttpResult) throws Exception {
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
                .subscribe(new MyObserver() {
                    @Override
                    public void onComplete() {
                        mView.onAddDoctorConsultationSuccess();
                    }
                });
    }

}
