package com.ssh.shuoshi.ui.patient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.patient.PatientListBean;
import com.ssh.shuoshi.inter.MyTextWatcher;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.adapter.wrapper.LoadMoreWrapper;
import com.ssh.shuoshi.library.widget.MyPtrClassicFrameLayout;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.medicalhistory.MedicalHistoryActivity;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 患者管理
 * created by hwt on 2020/12/27
 */
public class PatientManageActivity extends BaseActivity implements PatientManageContract.View,
        PtrHandler, LoadMoreWrapper.OnLoadMoreListener {

    @Inject
    PatientManagePresenter mPresenter;
    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.editTextSearch)
    EditText editTextSearch;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.miss_tu)
    LinearLayout missTu;
    @BindView(R.id.nestedSc)
    NestedScrollView mNestedSc;

    private LoadingDialog mLoadingDialog;
    private CommonAdapter mCommonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private List<PatientListBean.RowsBean> mData = new ArrayList<>();
    private int mScrollY;

    @Override
    public int initContentView() {
        return R.layout.activity_patient_manage;
    }

    @Override
    protected void initInjector() {
        DaggerPatientManageComponent.builder()
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
        //显示时间
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        //禁止下拉
        mPtrLayout.disableWhenHorizontalMove(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        uniteTitle.setBackListener(-1, v -> finish());

        mPresenter.onRefresh(null);

        editTextSearch.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    mPresenter.onRefresh(null);
                } else {
                    mPresenter.onRefresh(s.toString());
                }
            }
        });

        mNestedSc.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> mScrollY = scrollY);
    }

    @Override
    public void onRefreshCompleted(PatientListBean beans, boolean isClear) {
        if (beans == null || beans.getRows().size() == 0) {
            missTu.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            missTu.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        if (isClear) {
            mData.clear();
        }
        mData.addAll(beans.getRows());

        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<PatientListBean.RowsBean>(this, R.layout.item_patient_manage, mData) {
                @Override
                protected void convert(ViewHolder holder, final PatientListBean.RowsBean bean, int pos) {
                    RelativeLayout rlContainer = holder.getView(R.id.rl_container);
                    TextView tvName = holder.getView(R.id.tv_name);
                    TextView tvSex = holder.getView(R.id.tv_sex);
                    TextView tvAge = holder.getView(R.id.tv_age);
                    TextView tvTime = holder.getView(R.id.tv_time);

                    tvName.setText(bean.getPatientName());
                    tvSex.setText(bean.getSexName());
                    tvAge.setText(bean.getPatientAge() + "岁");
                    String date = bean.getPayTime();
                    if (!TextUtils.isEmpty(date) && date.length() >= 10) {
                        date = date.substring(0, 10);
                    }
                    tvTime.setText("最近问诊时间 " + date);

                    rlContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PatientManageActivity.this, MedicalHistoryActivity.class);
                            intent.putExtra("patientId", bean.getPatientId());
                            startActivity(intent);
                        }
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
        if (mScrollY > 0) {
            return false;
        }
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        String content = editTextSearch.getText().toString();
        if (TextUtils.isEmpty(content)) {
            mPresenter.onRefresh(null);
        } else {
            mPresenter.onRefresh(content);
        }
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(PatientManageActivity.this, "");
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
        if (mPtrLayout != null && mPtrLayout.isRefreshing()) {
            mPtrLayout.refreshComplete();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
