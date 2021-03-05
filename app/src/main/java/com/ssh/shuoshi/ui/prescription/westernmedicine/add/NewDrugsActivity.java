package com.ssh.shuoshi.ui.prescription.westernmedicine.add;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.bean.OftenDrugBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.inter.MyTextWatcher;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.adapter.wrapper.LoadMoreWrapper;
import com.ssh.shuoshi.library.widget.MyPtrClassicFrameLayout;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.prescription.westernmedicine.commonlydrugs.CommonlyDrugsActivity;
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
 * 新增药品
 */
public class NewDrugsActivity extends BaseActivity implements NewDrugsContract.View,
        PtrHandler, LoadMoreWrapper.OnLoadMoreListener {

    @Inject
    NewDrugsPresenter mPresenter;

    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.editTextSearch)
    EditText editTextSearch;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;
    @Inject
    UserStorage mUserStorage;

    private CommonAdapter mCommonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private List<DrugBean.RowsBean> mData = new ArrayList<>();
    private int mPhamCategoryId = 1;
    private String mType;

    //供应商ID
    private Integer phamVendorId = null;

    @Override
    public int initContentView() {
        return R.layout.activity_new_drugs;
    }

    @Override
    protected void initInjector() {
        DaggerNewDrugsComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        Intent intent = getIntent();
        mPhamCategoryId = intent.getIntExtra("mPhamCategoryId", -1);

        mType = intent.getStringExtra("type");
        int phamId = intent.getIntExtra("phamVendorId", 0);
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
        uniteTitle.setBackListener(-1, v -> finish());

        uniteTitle.setRightButton("常用药品", v -> {
            Intent intent2 = new Intent(NewDrugsActivity.this, CommonlyDrugsActivity.class);
            intent2.putExtra("mPhamCategoryId", mPhamCategoryId);
            if (phamVendorId == null) {
                intent.putExtra("phamVendorId", 0);
            } else {
                intent.putExtra("phamVendorId", phamVendorId);
            }
            intent2.putExtra("type", mType);
            startActivityForResult(intent2, 200);
        });

        mPresenter.onRefresh("", mPhamCategoryId, phamVendorId);

        editTextSearch.addTextChangedListener((MyTextWatcher) s -> {
            if (TextUtils.isEmpty(s.toString())) {
                mPresenter.onRefresh("", mPhamCategoryId, phamVendorId);
            } else {
                mPresenter.onRefresh(s.toString(), mPhamCategoryId, phamVendorId);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 200) {
            OftenDrugBean.RowsBean rowsBean = (OftenDrugBean.RowsBean) data.getSerializableExtra("rowsBean");
            DrugBean.RowsBean bean = new DrugBean.RowsBean();
            bean.setId(rowsBean.getPhamId());
            bean.setRetailPrice(rowsBean.getRetailPrice());
            bean.setPhamSpec(rowsBean.getPhamSpec());
            bean.setPhamAliasName(rowsBean.getPharmacyAliasName());
            bean.setDosageUnit(rowsBean.getDosageUnit());
            Intent intent = new Intent();
            intent.putExtra("rowsBean", bean);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onRefreshCompleted(DrugBean beans, boolean isClear) {
        if (isClear) {
            mData.clear();
        }
        mData.addAll(beans.getRows());

        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<DrugBean.RowsBean>(this, R.layout.item_new_drugs_search, mData) {
                @Override
                protected void convert(ViewHolder holder, final DrugBean.RowsBean bean, int pos) {
                    TextView tvName = holder.getView(R.id.textName);
                    TextView tvSpecification = holder.getView(R.id.textSpecification);
                    TextView tvPrice = holder.getView(R.id.textPrice);
                    Button btnAdd = holder.getView(R.id.buttonAdd);

                    if (mType.equals("template")) {
                        btnAdd.setText("加入常用处方");
                        btnAdd.setClickable(true);
                        btnAdd.setBackground(getResources().getDrawable(R.drawable.bg_btn_save_origin));
                    } else {
                        if (bean.getSurplusUsableStock() <= 0) {
                            btnAdd.setText("库存不足");
                            btnAdd.setClickable(false);
                            btnAdd.setBackground(getResources().getDrawable(R.drawable.bg_btn_save_gray));
                        } else {
                            btnAdd.setText("加入处方");
                            btnAdd.setClickable(true);
                            btnAdd.setBackground(getResources().getDrawable(R.drawable.bg_btn_save_origin));
                        }
                    }



                    tvPrice.setText("¥" + bean.getRetailPrice());
                    tvName.setText(bean.getPhamAliasName() + "(" + bean.getBrand() + ")");
                    tvSpecification.setText("规格：" + bean.getPhamSpec());
                    btnAdd.setOnClickListener(v -> {
                        DrugBean.RowsBean rowsBean = mData.get(pos);
                        Intent data = new Intent();
                        data.putExtra("rowsBean", rowsBean);
                        setResult(RESULT_OK, data);
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
            mPresenter.onRefresh("", mPhamCategoryId, phamVendorId);
        } else {
            mPresenter.onRefresh(content, mPhamCategoryId, phamVendorId);
        }
    }

}
