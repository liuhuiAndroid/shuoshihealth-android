package com.ssh.shuoshi.ui.team.addF;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.DiagnBean;
import com.ssh.shuoshi.bean.team.DistinctDoctorBean;
import com.ssh.shuoshi.bean.team.DoctorListBean;
import com.ssh.shuoshi.inter.MyTextWatcher;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.MultiItemTypeAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.adapter.wrapper.LoadMoreWrapper;
import com.ssh.shuoshi.library.widget.MyPtrClassicFrameLayout;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.patient.PatientManageActivity;
import com.ssh.shuoshi.ui.team.addS.DoctorAddSActivity;
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
public class DoctorAddFActivity extends BaseActivity implements DoctorAddFContract.View, PtrHandler,
        LoadMoreWrapper.OnLoadMoreListener {

    @Inject
    DoctorAddFPresenter mPresenter;
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
    private List<String> mData = new ArrayList<>();

    @Override
    public int initContentView() {
        return R.layout.activity_doctor_add;
    }

    @Override
    protected void initInjector() {
        DaggerDoctorAddFComponent.builder()
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
    public void onRefreshCompleted(DistinctDoctorBean beans, boolean isClear) {
        if (isClear) {
            mData.clear();
        }
        mData.addAll(beans.getRows());

        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<String>(this, R.layout.item_diagnosis_search, mData) {
                @Override
                protected void convert(ViewHolder holder, final String bean, int pos) {
                    TextView tvName = holder.getView(R.id.textName);
                    tvName.setText(bean);
                }
            };

            mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    String name = mData.get(position);
                    Intent data = new Intent(DoctorAddFActivity.this, DoctorAddSActivity.class);
                    data.putExtra("name", name);
                    startActivityForResult(data, 200);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 200) {
            DoctorListBean.RowsBean bean = (DoctorListBean.RowsBean) data.getSerializableExtra("bean");
            Intent intent = new Intent();
            intent.putExtra("bean", bean);
            setResult(RESULT_OK, intent);
            finish();
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
