package com.ssh.shuoshi.ui.patient;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.patient.PatientListBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.ui.prescription.template.commonlywesternmedicine.add.TemplateAddDrugsContract;
import com.squareup.otto.Bus;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * created by hwt on 2020/12/27
 */
public class PatientManagePresenter implements PatientManageContract.Presenter {

    private PatientManageContract.View mView;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;
    private int page = 1;
    private String mName = "";
    private int mType = 1;
    //因为这是找西成药，所以写死 西医 1
    private int mPhamCategoryId = 1;

    @Inject
    public PatientManagePresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }


    @Override
    public void onRefresh(String name) {
        this.mName = name;
        this.page = 1;
        loadData();
    }

    @Override
    public void loadData() {
        disposables.add(mCommonApi.getMyPatientListByPatientName(page, mName)
                .debounce(500, TimeUnit.MILLISECONDS)
                .switchMap(new Function<HttpResult<PatientListBean>, ObservableSource<PatientListBean>>() {
                    @Override
                    public ObservableSource<PatientListBean> apply(@io.reactivex.annotations.NonNull HttpResult<PatientListBean> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PatientListBean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull PatientListBean beans) throws Exception {
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
    public void attachView(@NonNull PatientManageContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }
}
