package com.ssh.shuoshi.ui.messagecenter;

import androidx.annotation.NonNull;

import com.squareup.otto.Bus;
import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.JPushSysMsgRecordBean;
import com.ssh.shuoshi.components.storage.UserStorage;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;

public class MessageCenterPresenter implements MessageCenterContract.Presenter {

    private MessageCenterContract.View mView;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;
    private int page = 1;

    @Inject
    public MessageCenterPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void attachView(@NonNull MessageCenterContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

    @Override
    public void onRefresh() {
        this.page = 1;
        loadData();
    }

    @Override
    public void loadData() {
        disposables.add(mCommonApi.jpushSysMsgRecordList(page)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap((Function<HttpResult<JPushSysMsgRecordBean>, ObservableSource<JPushSysMsgRecordBean>>)
                        listHttpResult -> CommonApi.flatResponse(listHttpResult))
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
    public void deleteJpushSysMsgRecord(String id) {
        disposables.add(mCommonApi.deleteJpushSysMsgRecord(id)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap((Function<HttpResult<String>, ObservableSource<String>>)
                        listHttpResult -> CommonApi.flatResponse(listHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(beans ->
                                mView.deleteJpushSysMsgRecordSuccess(beans),
                        throwable -> mView.onError(throwable))
        );
    }

    @Override
    public void getConsultationInfo(int mId,int id2) {
        disposables.add(mCommonApi.getConsultationInfo(mId)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap((Function<HttpResult<ImageDiagnoseBean.RowsBean>, ObservableSource<ImageDiagnoseBean.RowsBean>>)
                        listHttpResult -> CommonApi.flatResponse(listHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(beans ->
                                mView.getConsultationInfoSuccess(beans,id2),
                        throwable -> mView.onError(throwable))
        );
    }

    @Override
    public void consultationEditFollowTime(int days, int id) {
        disposables.add(mCommonApi.consultationEditFollowTime(days, id)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap((Function<HttpResult<String>,
                        ObservableSource<String>>)
                        listHttpResult -> CommonApi.flatResponse(listHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(beans ->
                                mView.getConsultationEditFollowTimeSuccess(beans),
                        throwable -> mView.onError(throwable))
        );
    }
}
