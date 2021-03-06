package com.ssh.shuoshi.ui.team.addS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.DiagnBean;
import com.ssh.shuoshi.bean.team.DoctorListBean;
import com.ssh.shuoshi.inter.MyTextWatcher;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.MultiItemTypeAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.adapter.wrapper.LoadMoreWrapper;
import com.ssh.shuoshi.library.widget.MyPtrClassicFrameLayout;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * created by hwt on 2021/1/3
 */
public class DoctorAddSActivity extends BaseActivity implements DoctorAddSContract.View, PtrHandler,
        LoadMoreWrapper.OnLoadMoreListener {

    @Inject
    DoctorAddSPresenter mPresenter;

    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.editTextSearch)
    EditText editTextSearch;
    @BindView(R.id.textCancel)
    TextView textCancel;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;

    private CommonAdapter mCommonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private List<DoctorListBean.RowsBean> mData = new ArrayList<>();
    private String name;

    @Override
    public int initContentView() {
        return R.layout.activity_doctor_add;
    }

    @Override
    protected void initInjector() {
        DaggerDoctorAddSComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);

        mPtrLayout.setPtrHandler(this);
        //????????????
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        //????????????
        mPtrLayout.disableWhenHorizontalMove(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        uniteTitle.setBackListener(-1, v -> finish());

        name = getIntent().getStringExtra("name");
        editTextSearch.setText(name);
        mPresenter.onRefresh(name);

        editTextSearch.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {

                } else {
                    mPresenter.onRefresh(s.toString());
                }
            }
        });
        textCancel.setOnClickListener(v -> editTextSearch.setText(""));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
        if (mPtrLayout != null && mPtrLayout.isRefreshing()) {
            mPtrLayout.refreshComplete();
        }
    }

    @Override
    public void onRefreshCompleted(DoctorListBean beans, boolean isClear) {
        if (isClear) {
            mData.clear();
        }
        mData.addAll(beans.getRows());

        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<DoctorListBean.RowsBean>(this, R.layout.item_doctor_adds, mData) {
                @Override
                protected void convert(ViewHolder holder, final DoctorListBean.RowsBean bean, int pos) {
                    ImageView ImageDoctorAvatar = holder.getView(R.id.ImageDoctorAvatar);
                    TextView textDoctorName = holder.getView(R.id.textDoctorName);
                    TextView textOffice = holder.getView(R.id.textOffice);
                    TextView textJob = holder.getView(R.id.textJob);
                    TextView textHospital = holder.getView(R.id.textHospital);
                    TextView textDesc = holder.getView(R.id.textDesc);
                    Button buttonAdd = holder.getView(R.id.buttonAdd);

                    textDoctorName.setText(bean.getName());
                    textOffice.setText(bean.getHisSysDeptName());
                    textJob.setText(bean.getDoctorTitleName());
                    textHospital.setText(bean.getHospitalName());
                    textDesc.setText("?????????" + bean.getGoodAt());
                    buttonAdd.setOnClickListener(v -> {
                        Intent intent = new Intent();
                        intent.putExtra("bean", bean);
                        setResult(RESULT_OK, intent);
                        finish();
                    });

                }
            };

            mLoadMoreWrapper = new LoadMoreWrapper(mCommonAdapter);
            mLoadMoreWrapper.setLoadMoreView(LayoutInflater.from(this)
                    .inflate(R.layout.footer_view_load_more, mRecyclerView, false));
            mLoadMoreWrapper.setOnLoadMoreListener(this);

            mRecyclerView.setAdapter(mLoadMoreWrapper);
        } else {
            if (mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (!mRecyclerView.isComputingLayout())) {
                mLoadMoreWrapper.notifyDataSetChanged();
            }
        }

        if (mPtrLayout != null && mPtrLayout.isRefreshing()) {
            mPtrLayout.refreshComplete();
        }
    }

    @Override
    public void onLoadCompleted(boolean isLoadAll) {
        mLoadMoreWrapper.setLoadAll(isLoadAll);
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.onLoadMore();
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        String content = editTextSearch.getText().toString();
        if (TextUtils.isEmpty(content)) {
            mPtrLayout.refreshComplete();
        } else {
            mPresenter.onRefresh(content);
        }
    }
}
