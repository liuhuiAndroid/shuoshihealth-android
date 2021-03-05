package com.ssh.shuoshi.ui.historymessage;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.squareup.otto.Bus;
import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.history.ImGetHistoryBean;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.components.storage.UserStorage;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import timber.log.Timber;

public class HistoryMessagePresenter implements HistoryMessageContract.Presenter {

    private HistoryMessageContract.View mView;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;

    private int page = 1;
    private String conversationId;

    @Inject
    public HistoryMessagePresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void attachView(@NonNull HistoryMessageContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

    @Override
    public void onRefresh(String conversationId, int mId, List<String> imgPath) {
        this.conversationId = conversationId;
        this.page = 1;

        disposables.add(mCommonApi.getConsultationInfo(mId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<ImageDiagnoseBean.RowsBean>, ObservableSource<ImageDiagnoseBean.RowsBean>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mView.hideLoading())
                .subscribe(bean -> {
                            mView.getConsultationInfoSuccess(bean);
                            if (imgPath != null && imgPath.size() == 1) {
                                getImagePath((String[]) imgPath.toArray(new String[1]));
                            } else {
                                loadData();
                            }
                        },
                        throwable -> mView.onError(throwable)));

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
                        loadData();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
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
    public void loadData() {
        disposables.add(mCommonApi.imGetHistory(page, conversationId)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap((Function<HttpResult<ImGetHistoryBean>, ObservableSource<ImGetHistoryBean>>)
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
    public void getConsultationInfo(int mId) {

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
                .doFinally(() -> mView.hideLoading())
                .subscribe(imgList -> {
                    Timber.i("getImagePath end --- ");
                    mView.getImagePathSuccess(imgList, imageView1, imageView2, imageView3);
                }, throwable -> mView.onError(throwable)));
    }


    @Override
    public void getImagePathSingle(String[] photoPath, ImageView imageView1) {
        Timber.i("getImagePath start --- ");
        disposables.add(mCommonApi.imgDownload(photoPath)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap((Function<HttpResult<List<String>>, ObservableSource<List<String>>>)
                        loginEntityHttpResult -> CommonApi.flatResponse(loginEntityHttpResult))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> mView.hideLoading())
                .subscribe(imgList -> {
                    Timber.i("getImagePath end --- ");
                    mView.getImageSingleSuccess(imgList, imageView1);
                }, throwable -> mView.onError(throwable)));
    }

}
