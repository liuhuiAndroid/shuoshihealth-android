package com.ssh.shuoshi.ui.followup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.patient.FollowUpListBean;
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
 * 随访管理
 * created by hwt on 2020/12/27
 */
public class FollowUpManageActivity extends BaseActivity implements FollowUpManageContract.View,
        PtrHandler, LoadMoreWrapper.OnLoadMoreListener {

    @Inject
    FollowUpManagePresenter mPresenter;
    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.miss_tu)
    LinearLayout missTu;

    private LoadingDialog mLoadingDialog;
    private CommonAdapter mCommonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private List<FollowUpListBean.RowsBean> mData = new ArrayList<>();

    @Override
    public int initContentView() {
        return R.layout.activity_follow_up_manage;
    }

    @Override
    protected void initInjector() {
        DaggerFollowUpManageComponent.builder()
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

        mPresenter.onRefresh();
    }

    @Override
    public void onRefreshCompleted(FollowUpListBean beans, boolean isClear) {
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
            mCommonAdapter = new CommonAdapter<FollowUpListBean.RowsBean>(this, R.layout.item_follow_up_manage, mData) {
                @Override
                protected void convert(ViewHolder holder, final FollowUpListBean.RowsBean bean, int pos) {
                    RelativeLayout rlContainer = holder.getView(R.id.rl_container);
                    TextView tvName = holder.getView(R.id.tv_name);
                    TextView tvSex = holder.getView(R.id.tv_sex);
                    TextView tvAge = holder.getView(R.id.tv_age);
                    TextView tvResult = holder.getView(R.id.tv_result);
                    TextView tvDate = holder.getView(R.id.tv_date);
                    TextView tvDay = holder.getView(R.id.tv_day);

                    tvName.setText(bean.getPatientName());
                    tvSex.setText(bean.getSexName());
                    tvAge.setText(bean.getPatientAge() + "岁");
                    tvResult.setText(bean.getEmrDiagDesc());

                    String date = bean.getActualStartTime();
                    if (!TextUtils.isEmpty(date) && date.length() >= 10) {
                        date = date.substring(0, 10);
                    }
                    tvDate.setText(date);
                    tvDay.setText(bean.getFollowUpTime());

                    rlContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(FollowUpManageActivity.this, MedicalHistoryActivity.class);
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
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        mPresenter.onRefresh();
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(FollowUpManageActivity.this, "");
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
