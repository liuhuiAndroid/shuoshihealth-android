package com.ssh.shuoshi.ui.prescription.template.commonlyprescriptionchinese;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.template.HisPrescriptionTemplateDetailDtosBean;
import com.ssh.shuoshi.bean.template.HisPrescriptionTemplateTcmDetailsBean;
import com.ssh.shuoshi.bean.template.PrescriptionTemplateBean;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.adapter.wrapper.LoadMoreWrapper;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.library.widget.MyPtrClassicFrameLayout;
import com.ssh.shuoshi.ui.BaseFragment;
import com.ssh.shuoshi.ui.dialog.DeleteTemplateDialog;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.prescription.template.PrescriptionTemplateComponent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 处方模版 常用处方 中药, 必传2
 */
public class CommonlyPrescriptionChineseFragment extends BaseFragment implements CommonlyPrescriptionChineseContract.View,
        PtrHandler, LoadMoreWrapper.OnLoadMoreListener {


    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;

    @Inject
    CommonlyPrescriptionChinesePresenter mPresenter;
    @BindView(R.id.miss_tu)
    LinearLayout missTu;

    private LoadingDialog mLoadingDialog;
    private CommonAdapter mCommonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private List<PrescriptionTemplateBean.RowsBean> mData = new ArrayList<>();

    @Override
    public void initInjector() {
        getComponent(PrescriptionTemplateComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_commonly_prescription_chinese;
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
            mRecyclerView.setVisibility(View.GONE);
            missTu.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            missTu.setVisibility(View.GONE);
        }
        if (isClear) {
            mData.clear();
        }
        mData.addAll(beans.getRows());

        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<PrescriptionTemplateBean.RowsBean>(getActivity(), R.layout.item_commonly_prescription_chinese, mData) {
                @Override
                protected void convert(ViewHolder holder, final PrescriptionTemplateBean.RowsBean bean, int position) {
                    StringBuffer sb = new StringBuffer();
                    TextView textName = holder.getView(R.id.textName);
                    TextView tvContent = holder.getView(R.id.tv_content);
                    Button buttonDelete = holder.getView(R.id.buttonDelete);

                    List<HisPrescriptionTemplateDetailDtosBean> dtos = bean.getHisPrescriptionTemplateDetailDtos();
                    for (int i = 0; i < dtos.size(); i++) {
                        List<HisPrescriptionTemplateTcmDetailsBean> tcmDetails = dtos.get(i).getHisPrescriptionTemplateTcmDetails();
                        for (int j = 0; j < tcmDetails.size(); j++) {
                            sb.append(tcmDetails.get(j).getPhamName() + " ");
                            sb.append(tcmDetails.get(j).getAmount());
                            sb.append(tcmDetails.get(j).getUnits());
                            sb.append("; ");
                        }
                    }
                    textName.setText(bean.getTemplateName());
                    tvContent.setText(sb.toString());
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
