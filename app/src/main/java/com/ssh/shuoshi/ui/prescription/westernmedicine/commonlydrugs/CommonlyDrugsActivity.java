package com.ssh.shuoshi.ui.prescription.westernmedicine.commonlydrugs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.OftenDrugBean;
import com.ssh.shuoshi.components.storage.UserStorage;
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
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 常用药品
 */
public class CommonlyDrugsActivity extends BaseActivity implements CommonlyDrugsContract.View,
        PtrHandler, LoadMoreWrapper.OnLoadMoreListener {

    @Inject
    CommonlyDrugsPresenter mPresenter;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;
    List<OftenDrugBean.RowsBean> mData = new ArrayList<>();

    @Inject
    UserStorage mUserStorage;
    private int mPhamCategoryId = 1;

    private LoadingDialog mLoadingDialog;
    private CommonAdapter mCommonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private String mType;
    //供应商ID
    private Integer phamVendorId = null;

    @Override
    public int initContentView() {
        return R.layout.activity_commonly_drugs;
    }

    @Override
    protected void initInjector() {
        DaggerCommonlyDrugsComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        uniteTitle.setBackListener(-1, v -> finish());
        mType = getIntent().getStringExtra("type");

        int phamId = getIntent().getIntExtra("phamVendorId", 0);
        if (phamId == 0) {
            phamVendorId = null;
        } else {
            phamVendorId = phamId;
        }

        mPtrLayout.setPtrHandler(this);
        //显示时间
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        //禁止下拉
        mPtrLayout.disableWhenHorizontalMove(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));

        String deptType = mUserStorage.getDoctorInfo().getDeptType();
        if (deptType.equals("西医")) {
            mPhamCategoryId = 1;
        } else {
            mPhamCategoryId = 2;
        }

        int preId = getIntent().getIntExtra("mPhamCategoryId", -1);
        if (preId != -1) {
            mPhamCategoryId = preId;
        }

        mPresenter.getMyOftenList(mPhamCategoryId, phamVendorId);
    }

    @Override
    public void onRefreshCompleted(OftenDrugBean beans, boolean isClear) {
        if (isClear) {
            mData.clear();
        }
        mData.addAll(beans.getRows());

        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<OftenDrugBean.RowsBean>(this, R.layout.item_new_drugs_search, mData) {
                @Override
                protected void convert(ViewHolder holder, final OftenDrugBean.RowsBean bean, int pos) {
                    TextView tvName = holder.getView(R.id.textName);
                    TextView tvSpecification = holder.getView(R.id.textSpecification);
                    TextView tvPrice = holder.getView(R.id.textPrice);
                    Button btnAdd = holder.getView(R.id.buttonAdd);

                    if (mType.equals("template")) {
                        btnAdd.setText("加入常用处方");
                        btnAdd.setEnabled(true);
                        btnAdd.setBackground(getResources().getDrawable(R.drawable.bg_btn_save_origin));
                    } else {
                        if (bean.getSurplusUsableStock() <= 0) {
                            btnAdd.setText("库存不足");
                            btnAdd.setEnabled(false);
                            btnAdd.setBackground(getResources().getDrawable(R.drawable.bg_btn_save_gray));
                        } else {
                            btnAdd.setText("加入处方");
                            btnAdd.setEnabled(true);
                            btnAdd.setBackground(getResources().getDrawable(R.drawable.bg_btn_save_origin));
                        }
                    }

                    tvPrice.setText("¥" + bean.getRetailPrice());
                    tvName.setText(bean.getPharmacyAliasName() + "(" + bean.getBrand() + ")");
                    tvSpecification.setText("规格：" + bean.getPhamSpec());
                    btnAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OftenDrugBean.RowsBean rowsBean = mData.get(pos);
                            Intent data = new Intent();
                            data.putExtra("rowsBean", rowsBean);
                            setResult(RESULT_OK, data);
                            finish();
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
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(CommonlyDrugsActivity.this, "");
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
    public void onLoadMoreRequested() {
        mPresenter.onLoadMore();
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        mPresenter.getMyOftenList(mPhamCategoryId, phamVendorId);
    }
}
