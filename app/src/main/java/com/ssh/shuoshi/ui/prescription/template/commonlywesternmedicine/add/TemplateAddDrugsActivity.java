package com.ssh.shuoshi.ui.prescription.template.commonlywesternmedicine.add;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.inter.MyTextWatcher;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.adapter.wrapper.LoadMoreWrapper;
import com.ssh.shuoshi.library.util.ToastUtil;
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
 * 模版 新增药品
 * created by hwt on 2020/12/22
 */
public class TemplateAddDrugsActivity extends BaseActivity implements TemplateAddDrugsContract.View,
        PtrHandler, LoadMoreWrapper.OnLoadMoreListener {

    @Inject
    TemplateAddDrugsPresenter mPresenter;

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
    private LoadingDialog mLoadingDialog;

    //供应商ID
    private Integer phamVendorId = null;

    @Override
    public int initContentView() {
        return R.layout.activity_new_drugs;
    }

    @Override
    protected void initInjector() {
        DaggerTemplateAddDrugsComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);

        mPhamCategoryId = getIntent().getIntExtra("mPhamCategoryId", -1);
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
        uniteTitle.setBackListener(-1, v -> finish());

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
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(TemplateAddDrugsActivity.this, "");
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
    public void onRefreshCompleted(DrugBean beans, boolean isClear) {
        if (isClear) {
            mData.clear();
        }
        mData.addAll(beans.getRows());

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            list.add(mData.get(i).getId());
        }
        Log.i("test","id list:" + new Gson().toJson(list));

        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<DrugBean.RowsBean>(this, R.layout.item_add_drugs_search, mData) {
                @Override
                protected void convert(ViewHolder holder, final DrugBean.RowsBean bean, int pos) {
                    TextView tvName = holder.getView(R.id.textName);
                    TextView tvSpecification = holder.getView(R.id.textSpecification);
                    TextView tvPrice = holder.getView(R.id.textPrice);
                    TextView textHint = holder.getView(R.id.textHint);
                    Button btnAdd = holder.getView(R.id.buttonAdd);

                    if (bean.isOffenFlag()) {
                        textHint.setVisibility(View.VISIBLE);
                        btnAdd.setVisibility(View.GONE);
                    } else {
                        textHint.setVisibility(View.GONE);
                        btnAdd.setVisibility(View.VISIBLE);
                    }

                    tvPrice.setText("¥" + bean.getRetailPrice());
                    tvName.setText(bean.getPhamAliasName() + "(" + bean.getBrand() + ")");
                    tvSpecification.setText("规格：" + bean.getPhamSpec());
                    btnAdd.setOnClickListener(v -> {
                        showLoading();
                        mPresenter.addMyOftenList(bean.getId(), pos);
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
    public void addMyOftenListSuccess(String beans, int pos) {
        mData.get(pos).setOffenFlag(true);
        mLoadMoreWrapper.notifyItemChanged(pos);
//        ToastUtil.showToast("已加入我的常用药");
//        String content = editTextSearch.getText().toString();
//        if (TextUtils.isEmpty(content)) {
//            mPresenter.onRefresh("", mPhamCategoryId, phamVendorId);
//        } else {
//            mPresenter.onRefresh(content, mPhamCategoryId, phamVendorId);
//        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
