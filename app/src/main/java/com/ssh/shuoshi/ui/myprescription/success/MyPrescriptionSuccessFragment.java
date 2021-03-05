package com.ssh.shuoshi.ui.myprescription.success;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDetailDtosBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionTcmDetailsBean;
import com.ssh.shuoshi.bean.prescription.PrescriptionStateBean;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.MultiItemTypeAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.adapter.wrapper.LoadMoreWrapper;
import com.ssh.shuoshi.library.widget.MyPtrClassicFrameLayout;
import com.ssh.shuoshi.ui.BaseFragment;
import com.ssh.shuoshi.ui.graphicinquiry.GraphicInquiryActivity;
import com.ssh.shuoshi.ui.myprescription.detail.MyPrescriptionDetailActivity;
import com.ssh.shuoshi.ui.myprescription.main.MyPrescriptionComponent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * created by hwt on 2021/1/2
 */
public class MyPrescriptionSuccessFragment extends BaseFragment implements PtrHandler,
        MyPrescriptionSuccessContract.View, LoadMoreWrapper.OnLoadMoreListener {

    @BindView(R.id.miss_tu)
    LinearLayout missTu;
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;

    @Inject
    MyPrescriptionSuccessPresenter mPresenter;
    private CommonAdapter mCommonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private List<PrescriptionStateBean.RowsBean> mData = new ArrayList<>();

    @Override
    public void initInjector() {
        getComponent(MyPrescriptionComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_prescription;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        mPresenter.onRefresh();
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
    public void onRefreshCompleted(PrescriptionStateBean beans, boolean isClear) {
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
            mCommonAdapter = new CommonAdapter<PrescriptionStateBean.RowsBean>(getActivity(), R.layout.fragment_my_prescription_success_item, mData) {
                @Override
                protected void convert(ViewHolder holder, final PrescriptionStateBean.RowsBean bean, int position) {
                    RelativeLayout mContainer = holder.getView(R.id.mContainer);
                    TextView textName = holder.getView(R.id.textName);
                    TextView textSex = holder.getView(R.id.textSex);
                    TextView textAge = holder.getView(R.id.textAge);
                    TextView textResult = holder.getView(R.id.textResult);
                    TextView textAdvice = holder.getView(R.id.textAdvice);
                    TextView textState = holder.getView(R.id.textState);

                    textName.setText(bean.getPatientName());
                    textSex.setText(bean.getSexName());
                    textAge.setText(bean.getPatientAge() + "岁");
                    textResult.setText("初步诊断：" + bean.getDiagDesc());

                    StringBuffer sb = new StringBuffer();
                    String perscriptionTypeName = bean.getPerscriptionTypeName();
                    List<HisPrescriptionDetailDtosBean> detailDtos = bean.getHisPrescriptionDetailDtos();
                    if (perscriptionTypeName.contains("西")) {
                        if (detailDtos != null) {
                            for (int i = 0; i < detailDtos.size(); i++) {
                                sb.append(detailDtos.get(i).getPhamName());
                                sb.append("、");
                            }
                        }
                    } else {
                        if (detailDtos != null && detailDtos.size() != 0) {
                            List<HisPrescriptionTcmDetailsBean> tcmDetails = detailDtos.get(0).getHisPrescriptionTcmDetails();
                            if (tcmDetails != null) {
                                for (int j = 0; j < tcmDetails.size(); j++) {
                                    sb.append(tcmDetails.get(j).getPhamName());
                                    if (j >= 3) {
                                        sb.append("等");
                                        break;
                                    }
                                    sb.append("、");
                                }
                            }
                        }
                    }

                    textAdvice.setText("建议用药：" + sb.toString());
                    if(bean.getPaymentState() == 0){
                        textState.setText("待支付");
                    } else {
                        textState.setText("已支付");
                    }
                    mContainer.setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity(), MyPrescriptionDetailActivity.class);
                        PrescriptionStateBean.RowsBean rowsBean = mData.get(position);
                        intent.putExtra("prescriptionId", rowsBean.getId());
                        startActivity(intent);
                    });
                }
            };

            mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent(getActivity(), GraphicInquiryActivity.class);
                    PrescriptionStateBean.RowsBean rowsBean = mData.get(position);
                    intent.putExtra("type", GraphicInquiryActivity.TYPE_GRAPHIC);
                    startActivity(intent);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });

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
