package com.ssh.shuoshi.ui.prescription.template.commonlywesternmedicine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.OftenDrugBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.adapter.wrapper.LoadMoreWrapper;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.library.widget.MyPtrClassicFrameLayout;
import com.ssh.shuoshi.ui.BaseFragment;
import com.ssh.shuoshi.ui.dialog.DeleteTemplateDialog;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.prescription.template.PrescriptionTemplateComponent;
import com.ssh.shuoshi.ui.prescription.template.commonlywesternmedicine.add.TemplateAddDrugsActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 处方模版 常用西成药
 */
public class CommonlyWesternMedicineFragment extends BaseFragment implements PtrHandler,
        CommonlyWesternMedicineContract.View, LoadMoreWrapper.OnLoadMoreListener, View.OnClickListener {


    @BindView(R.id.miss_tu)
    LinearLayout missTu;
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;

    @Inject
    CommonlyWesternMedicinePresenter mPresenter;
    @Inject
    UserStorage mUserStorage;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.buttonSave)
    Button buttonSave;
    private CommonAdapter mCommonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private List<OftenDrugBean.RowsBean> mData = new ArrayList<>();
    private int mPhamCategoryId = 1;
    private LoadingDialog mLoadingDialog;

    //供应商ID
    private Integer phamVendorId = null;

    @Override
    public void initInjector() {
        getComponent(PrescriptionTemplateComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_commonly_western_medicine;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @SuppressLint("WrongConstant")
    @Override
    public void initUI(View view) {
        mPresenter.attachView(this);
        btnAdd.setText("新增常用药");
        mPhamCategoryId = mUserStorage.getDoctorInfo().getDeptTypeId();
        mPtrLayout.setPtrHandler(this);
        //显示时间
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        //禁止下拉
        mPtrLayout.disableWhenHorizontalMove(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));

        mPresenter.onRefresh(mPhamCategoryId, phamVendorId);

        btnAdd.setOnClickListener(this);
        buttonSave.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
            case R.id.buttonSave:
                Intent intent = new Intent(getActivity(), TemplateAddDrugsActivity.class);
                intent.putExtra("mPhamCategoryId", mPhamCategoryId);
                if (phamVendorId == null) {
                    intent.putExtra("phamVendorId", 0);
                } else {
                    intent.putExtra("phamVendorId", phamVendorId);
                }
                startActivityForResult(intent, 200);
                break;
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.onLoadMore();
    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
        if (mPtrLayout != null && mPtrLayout.isRefreshing()) {
            mPtrLayout.refreshComplete();
        }
    }

    @Override
    public void onRefreshCompleted(OftenDrugBean beans, boolean isClear) {
        if (beans == null || beans.getRows().size() == 0) {
            missTu.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            buttonSave.setVisibility(View.GONE);
        } else {
            phamVendorId = beans.getRows().get(0).getPhamVendorId();
            missTu.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            buttonSave.setVisibility(View.VISIBLE);
        }
        if (isClear) {
            mData.clear();
        }
        mData.addAll(beans.getRows());

        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<OftenDrugBean.RowsBean>(getActivity(), R.layout.item_commonly_western_medicine, mData) {
                @Override
                protected void convert(ViewHolder holder, final OftenDrugBean.RowsBean bean, int position) {
                    TextView textName = holder.getView(R.id.textName);
                    TextView textSpecification = holder.getView(R.id.textSpecification);
                    TextView textPrice = holder.getView(R.id.textPrice);
                    Button buttonDelete = holder.getView(R.id.buttonDelete);
                    textName.setText(bean.getPharmacyAliasName() + "(" + bean.getBrand() + ")");
                    textSpecification.setText("规格：" + bean.getPhamSpec());
                    textPrice.setText("￥" + bean.getRetailPrice());

                    buttonDelete.setOnClickListener(v -> {
                        DeleteTemplateDialog deleteTemplateDialog =
                                new DeleteTemplateDialog(getActivity(), R.style.dialog_physician_certification,
                                        "确定要删除此模版吗？");
                        deleteTemplateDialog.setOnItemClickListener(new DeleteTemplateDialog.ItemClickListener() {

                            @Override
                            public void cancel() {
                                deleteTemplateDialog.dismiss();
                            }

                            @Override
                            public void confirm() {
                                showLoading();
                                mPresenter.deleteMyOftenList(bean.getId());
                                deleteTemplateDialog.dismiss();
                            }
                        });
                        deleteTemplateDialog.show();
                    });
                }
            };

            mLoadMoreWrapper = new LoadMoreWrapper(mCommonAdapter);
            mLoadMoreWrapper.setLoadMoreView(LayoutInflater.from(getActivity())
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
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(getActivity(), "");
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    public void deleteMyOftenListSuccess(String beans) {
        ToastUtil.showToast("删除成功");
        mPresenter.onRefresh(mPhamCategoryId, phamVendorId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onRefresh(mPhamCategoryId, phamVendorId);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        mPresenter.onRefresh(mPhamCategoryId, phamVendorId);
    }

}
