package com.ssh.shuoshi.ui.graphicinquiry;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.common.SystemTypeBean;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * created by hwt on 2020/12/18
 */
public class GraphicInquiryPresenter implements GraphicInquiryContract.Presenter {

    private CommonApi mCommonApi;
    private GraphicInquiryContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    //退诊理由
    private String reason = "withdrawal_reason";

    @Inject
    public GraphicInquiryPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }


    @Override
    public void attachView(@NonNull GraphicInquiryContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

    @Override
    public void endConsultation(int id) {
        disposables.add(mCommonApi.endConsultation(id)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<String>, ObservableSource<String>>) loginEntityHttpResult
                        -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> mView.endConsultationSuccess(bean),
                        throwable -> mView.onError(throwable)));
    }

    @Override
    public void exitConsultation(int id, String reason) {
        disposables.add(mCommonApi.exitConsultation(id, reason)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String bean) throws Exception {
                        mView.exitConsultationSuccess(bean, reason);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void receiveConsultation(int id) {
        disposables.add(mCommonApi.receiveConsultation(id)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String bean) throws Exception {
                        mView.consultationSuccess(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void getConsultationInfo(int mId, boolean init) {
        disposables.add(mCommonApi.getConsultationInfo(mId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<ImageDiagnoseBean.RowsBean>, ObservableSource<ImageDiagnoseBean.RowsBean>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> mView.getConsultationInfoSuccess(bean, init),
                        throwable -> mView.onError(throwable)));
    }

    @Override
    public void getConsultationInfoForExit(int mId) {
        disposables.add(mCommonApi.getConsultationInfo(mId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<ImageDiagnoseBean.RowsBean>, ObservableSource<ImageDiagnoseBean.RowsBean>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> mView.getConsultationInfoSuccessForExit(bean),
                        throwable -> mView.onError(throwable)));
    }


    @Override
    public void getImagePath(String[] photoPath, ImageView imageView1, ImageView imageView2,
                             ImageView imageView3, Context context) {
        Timber.i("getImagePath start --- ");
        disposables.add(mCommonApi.imgDownload(photoPath)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<List<String>>, ObservableSource<List<String>>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(imgList -> {
                    Timber.i("getImagePath end --- ");
                    mView.getImagePathSuccess(imgList, imageView1, imageView2, imageView3);
                }, throwable -> mView.onError(throwable)));
    }

    @Override
    public void getReturnDiagnoseReason() {
        disposables.add(mCommonApi.getDictionariesList(reason)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<SystemTypeBean>, ObservableSource<SystemTypeBean>>() {
                    @Override
                    public ObservableSource<SystemTypeBean> apply(@io.reactivex.annotations.NonNull HttpResult<SystemTypeBean> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SystemTypeBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull SystemTypeBean bean) throws Exception {
                        mView.getReasonList(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void pushVideoConsulationNotice(int id) {
        disposables.add(mCommonApi.pushVideoConsulationNotice(id)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<String>, ObservableSource<String>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<String>) bean -> { },
                        (Consumer<Throwable>) throwable -> mView.onError(throwable)));
    }

    @Override
    public void getUserSigByUserNo(String userNo) {
        disposables.add(mCommonApi.getUserSigByUserNo(userNo)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap((Function<HttpResult<String>, ObservableSource<String>>)
                        listHttpResult -> CommonApi.flatResponse(listHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(beans -> mView.getUserSigByUserNoSuccess(beans),
                        throwable -> mView.onError(throwable)));
    }

    @Override
    public void getTeamImagePath(String[] path, int position) {
        disposables.add(mCommonApi.imgDownload(path)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<List<String>>, ObservableSource<List<String>>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(imgList -> {
                    mView.getTeamImagePathSuccess(imgList, position);
                }, throwable -> mView.onError(throwable)));
    }

    @Override
    public void getConsultationInfoForPrescriptionCard(int mId) {
        disposables.add(mCommonApi.getConsultationInfo(mId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<ImageDiagnoseBean.RowsBean>, ObservableSource<ImageDiagnoseBean.RowsBean>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> mView.getConsultationInfoSuccessForPrescriptionCard(bean),
                        throwable -> mView.onError(throwable)));
    }

}
