package com.ssh.shuoshi.ui.myprescription.wait;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDetailDtosBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionTcmDetailsBean;
import com.ssh.shuoshi.bean.prescription.PrescriptionStateBean;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.adapter.wrapper.LoadMoreWrapper;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.library.widget.MyPtrClassicFrameLayout;
import com.ssh.shuoshi.ui.BaseFragment;
import com.ssh.shuoshi.ui.graphicinquiry.GraphicInquiryActivity;
import com.ssh.shuoshi.ui.myprescription.detail.MyPrescriptionDetailActivity;
import com.ssh.shuoshi.ui.myprescription.main.MyPrescriptionComponent;
import com.ssh.shuoshi.ui.prescription.chinesemedicine.edit.EditChineseMedicinePrescriptionActivity;
import com.ssh.shuoshi.ui.prescription.westernmedicine.edit.EditPrescriptionActivity;
import com.ssh.shuoshi.ui.verified.submit.PrescriptionSubmitCheckActivity;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * created by hwt on 2021/1/4
 */
public class MyPrescriptionWaitFragment extends BaseFragment implements PtrHandler,
        MyPrescriptionWaitContract.View, LoadMoreWrapper.OnLoadMoreListener {

    @BindView(R.id.miss_tu)
    LinearLayout missTu;
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;

    @Inject
    MyPrescriptionWaitPresenter mPresenter;
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
        //清空MMKV，防止数据错乱
        MMKV.defaultMMKV().removeValueForKey("prescriptionType");
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
            mCommonAdapter = new CommonAdapter<PrescriptionStateBean.RowsBean>(getActivity(), R.layout.fragment_my_prescription_ing_item, mData) {
                @Override
                protected void convert(ViewHolder holder, final PrescriptionStateBean.RowsBean bean, int position) {
                    RelativeLayout mContainer = holder.getView(R.id.mContainer);
                    TextView textName = holder.getView(R.id.textName);
                    TextView textSex = holder.getView(R.id.textSex);
                    TextView textAge = holder.getView(R.id.textAge);
                    TextView textResult = holder.getView(R.id.textResult);
                    TextView textAdvice = holder.getView(R.id.textAdvice);

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

                    textName.setText(bean.getPatientName());
                    textSex.setText(bean.getSexName());
                    textAge.setText(bean.getPatientAge() + "岁");
                    textResult.setText("初步诊断：" + bean.getDiagDesc());
                    textAdvice.setText("建议用药：" + sb.toString());

                    mContainer.setOnClickListener(v -> {

                        if (bean.getState() == 0) {
                            if (perscriptionTypeName.contains("西")) {
                                Intent intent2 = new Intent(getActivity(), EditPrescriptionActivity.class);
                                intent2.putExtra("prescriptionBean", bean);
                                intent2.putExtra("isRequest", true);
                                startActivityForResult(intent2, 300);
                            } else {
                                Intent intent2 = new Intent(getActivity(), EditChineseMedicinePrescriptionActivity.class);
                                intent2.putExtra("prescriptionBean", bean);
                                intent2.putExtra("isRequest", true);
                                startActivityForResult(intent2, 300);
                            }
                        } else {
                            if (perscriptionTypeName.contains("西")) {
                                MMKV.defaultMMKV().putString("prescriptionType", "西药");
                            } else {
                                MMKV.defaultMMKV().putString("prescriptionType", "中药");
                            }
                            Intent intent = new Intent(getActivity(), PrescriptionSubmitCheckActivity.class);
                            intent.putExtra("prescriptionId", bean.getId());
                            startActivityForResult(intent, 300);
                        }
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 300) {
            mPresenter.onRefresh();
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
