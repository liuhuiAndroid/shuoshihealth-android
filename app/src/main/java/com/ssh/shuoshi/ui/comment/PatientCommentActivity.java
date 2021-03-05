package com.ssh.shuoshi.ui.comment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.patient.CommentBean;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.adapter.wrapper.LoadMoreWrapper;
import com.ssh.shuoshi.library.widget.MyPtrClassicFrameLayout;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
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
 * 患者评价
 * created by hwt on 2021/1/6
 */
public class PatientCommentActivity extends BaseActivity implements PatientCommentContract.View,
        PtrHandler, LoadMoreWrapper.OnLoadMoreListener, View.OnClickListener {

    @Inject
    PatientCommentPresenter mPresenter;
    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.textEvaluate)
    TextView textEvaluate;
    @BindView(R.id.miss_tu)
    LinearLayout missTu;

    private CommonAdapter mCommonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private List<CommentBean.RowsBean> mData = new ArrayList<>();
    private LoadingDialog mLoadingDialog;

    @Override
    public int initContentView() {
        return R.layout.activity_patient_comment;
    }

    @Override
    protected void initInjector() {
        DaggerPatientCommentComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void initUiAndListener() {
        uniteTitle.setBackListener(-1, v -> finish());

        mPresenter.attachView(this);

        mPtrLayout.setPtrHandler(this);
        //显示时间
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        //禁止下拉
        mPtrLayout.disableWhenHorizontalMove(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));

        mPresenter.onRefresh();
        textEvaluate.setText("暂无患者评价");
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefreshCompleted(CommentBean beans, boolean isClear) {
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
            mCommonAdapter = new CommonAdapter<CommentBean.RowsBean>(this, R.layout.item_patient_comment, mData) {
                @Override
                protected void convert(ViewHolder holder, final CommentBean.RowsBean bean, int pos) {
                    ImageView img1 = holder.getView(R.id.img1);
                    ImageView img2 = holder.getView(R.id.img2);
                    ImageView img3 = holder.getView(R.id.img3);
                    ImageView img4 = holder.getView(R.id.img4);
                    ImageView img5 = holder.getView(R.id.img5);
                    TextView textName = holder.getView(R.id.textName);
                    TextView textTime = holder.getView(R.id.textTime);
                    TextView textContent = holder.getView(R.id.textContent);

                    int star = bean.getStar();
                    switch (star) {
                        case 1:
                            img1.setImageResource(R.drawable.good_star);
                            break;
                        case 2:
                            img1.setImageResource(R.drawable.good_star);
                            img2.setImageResource(R.drawable.good_star);
                            break;
                        case 3:
                            img1.setImageResource(R.drawable.good_star);
                            img2.setImageResource(R.drawable.good_star);
                            img3.setImageResource(R.drawable.good_star);
                            break;
                        case 4:
                            img1.setImageResource(R.drawable.good_star);
                            img2.setImageResource(R.drawable.good_star);
                            img3.setImageResource(R.drawable.good_star);
                            img4.setImageResource(R.drawable.good_star);
                            break;
                        case 5:
                            img1.setImageResource(R.drawable.good_star);
                            img2.setImageResource(R.drawable.good_star);
                            img3.setImageResource(R.drawable.good_star);
                            img4.setImageResource(R.drawable.good_star);
                            img5.setImageResource(R.drawable.good_star);
                            break;
                    }

                    String patientName = bean.getPatientName();
                    if (!TextUtils.isEmpty(patientName)) {
                        patientName = patientName.substring(0, 1);
                        textName.setText(patientName + "**");
                    }
                    textTime.setText(bean.getCreateTime());
                    textContent.setText(bean.getContent());
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
        mLoadingDialog = LoadingDialog.show(PatientCommentActivity.this, "");
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
