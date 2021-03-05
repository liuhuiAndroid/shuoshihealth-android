package com.ssh.shuoshi.ui.prescription.template.commonlyprescriptionwestern;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.template.PrescriptionTemplateBean;
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
import com.ssh.shuoshi.ui.prescription.template.add.AddCommonlyTempalteActivity;
import com.ssh.shuoshi.ui.prescription.template.commonlyprescriptionwestern.templatedetail.PrescriptionTemplateDetailActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 处方模版 常用处方 西药
 */
public class CommonlyPrescriptionWesternFragment extends BaseFragment implements CommonlyPrescriptionWesternContract.View,
        PtrHandler, LoadMoreWrapper.OnLoadMoreListener, View.OnClickListener {

    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.miss_tu)
    LinearLayout missTu;
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.buttonSave)
    Button buttonSave;

    @Inject
    CommonlyPrescriptionWesternPresenter mPresenter;
    @Inject
    UserStorage mUserStorage;

    private CommonAdapter mCommonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private List<PrescriptionTemplateBean.RowsBean> mData = new ArrayList<>();
    private int mPhamCategoryId = 1;
    private LoadingDialog mLoadingDialog;

    @Override
    public void initInjector() {
        getComponent(PrescriptionTemplateComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_commonly_prescription_western;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @SuppressLint("WrongConstant")
    @Override
    public void initUI(View view) {
        mPresenter.attachView(this);
        mPtrLayout.setPtrHandler(this);
        //显示时间
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        //禁止下拉
        mPtrLayout.disableWhenHorizontalMove(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));

        mPresenter.onRefresh();

        btnAdd.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
        if (mPtrLayout != null && mPtrLayout.isRefreshing()) {
            mPtrLayout.refreshComplete();
        }
    }

    @Override
    public void onRefreshCompleted(PrescriptionTemplateBean beans, boolean isClear) {
        if (beans == null || beans.getRows().size() == 0) {
            missTu.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            buttonSave.setVisibility(View.GONE);
        } else {
            missTu.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            buttonSave.setVisibility(View.VISIBLE);
        }
        if (isClear) {
            mData.clear();
        }
        mData.addAll(beans.getRows());

        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<PrescriptionTemplateBean.RowsBean>(getActivity(), R.layout.item_commonly_prescription_western, mData) {
                @Override
                protected void convert(ViewHolder holder, final PrescriptionTemplateBean.RowsBean bean, int position) {
                    int mSize = bean.getHisPrescriptionTemplateDetailDtos().size();
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < mSize; i++) {
                        if (i == mSize - 1) {
                            sb.append(bean.getHisPrescriptionTemplateDetailDtos().get(i).getPhamName());
                        } else {
                            sb.append(bean.getHisPrescriptionTemplateDetailDtos().get(i).getPhamName());
                            sb.append(",");
                        }
                    }
                    RelativeLayout mContainer = holder.getView(R.id.mContainer);
                    TextView textView01 = holder.getView(R.id.textView01);
                    TextView textView02 = holder.getView(R.id.textView02);

                    Button buttonDelete = holder.getView(R.id.buttonDelete);
                    Button buttonEdit = holder.getView(R.id.buttonEdit);

                    textView01.setText(bean.getTemplateName());
                    textView02.setText(sb.toString());

                    mContainer.setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), PrescriptionTemplateDetailActivity.class);
                        intent.putExtra("isShow", true);
                        intent.putExtra("mPhamCategoryId", mPhamCategoryId);
                        intent.putExtra("template", bean);
                        startActivityForResult(intent, 101);
                    });

                    buttonEdit.setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), AddCommonlyTempalteActivity.class);
                        intent.putExtra("type", "2");
                        intent.putExtra("mPhamCategoryId", mPhamCategoryId);
                        intent.putExtra("template", bean);
                        startActivityForResult(intent, 102);
                    });

                    buttonDelete.setOnClickListener(v -> {
                        DeleteTemplateDialog deleteTemplateDialog = new DeleteTemplateDialog(getActivity(),
                                R.style.dialog_physician_certification, "");
                        deleteTemplateDialog.setOnItemClickListener(new DeleteTemplateDialog.ItemClickListener() {

                            @Override
                            public void cancel() {
                                deleteTemplateDialog.dismiss();
                            }

                            @Override
                            public void confirm() {
                                showLoading();
                                mPresenter.deletePrescriptionTemplate(bean.getId());
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
    public void deletePrescriptionTemplateSuccess(Integer beans) {
        ToastUtil.showToast("删除成功");
        mPresenter.onRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
            case R.id.buttonSave:
                Intent intent = new Intent(getActivity(), AddCommonlyTempalteActivity.class);
                intent.putExtra("type", "1");
                intent.putExtra("mPhamCategoryId", mPhamCategoryId);
                startActivityForResult(intent, 200);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onRefresh();
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
}
