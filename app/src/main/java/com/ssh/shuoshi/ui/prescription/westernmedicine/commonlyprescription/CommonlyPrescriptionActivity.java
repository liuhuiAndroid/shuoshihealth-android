package com.ssh.shuoshi.ui.prescription.westernmedicine.commonlyprescription;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.bean.template.HisPrescriptionTemplateDetailDtosBean;
import com.ssh.shuoshi.bean.template.PrescriptionTemplateBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.adapter.wrapper.LoadMoreWrapper;
import com.ssh.shuoshi.library.widget.MyPtrClassicFrameLayout;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.prescription.template.commonlyprescriptionwestern.templatedetail.PrescriptionTemplateDetailActivity;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 常用处方/电子处方
 */
public class CommonlyPrescriptionActivity extends BaseActivity implements CommonlyPrescriptionContract.View,
        PtrHandler, LoadMoreWrapper.OnLoadMoreListener {

    @Inject
    CommonlyPrescriptionPresenter mPresenter;
    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.miss_tu)
    LinearLayout missTu;
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;

    @Inject
    UserStorage mUserStorage;
    private int mPhamCategoryId = 1;
    private CommonAdapter mCommonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private List<PrescriptionTemplateBean.RowsBean> mData = new ArrayList<>();
    private LoadingDialog mLoadingDialog;

    @Override
    public int initContentView() {
        return R.layout.activity_commonly_prescription;
    }

    @Override
    protected void initInjector() {
        DaggerCommonlyPrescriptionComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        String deptType = mUserStorage.getDoctorInfo().getDeptType();
        if (deptType.equals("西医")) {
            mPhamCategoryId = 1;
        } else {
            mPhamCategoryId = 2;
        }
        uniteTitle.setBackListener(-1, v -> finish());
        mPtrLayout.setPtrHandler(this);
        //显示时间
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        //禁止下拉
        mPtrLayout.disableWhenHorizontalMove(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));

        mPresenter.onRefresh(mPhamCategoryId);

    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(this, "");
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
    public void onRefreshCompleted(PrescriptionTemplateBean beans, boolean isClear) {
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
            mCommonAdapter = new CommonAdapter<PrescriptionTemplateBean.RowsBean>(CommonlyPrescriptionActivity.this,
                    R.layout.item_commonly_prescription_use, mData) {
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

                    Button buttonUse = holder.getView(R.id.buttonUse);

                    textView01.setText(bean.getTemplateName());
                    textView02.setText(sb.toString());

                    mContainer.setOnClickListener(v -> {
                        Intent intent = new Intent(CommonlyPrescriptionActivity.this, PrescriptionTemplateDetailActivity.class);
                        intent.putExtra("template", bean);
                        intent.putExtra("isShow", false);
                        startActivityForResult(intent, 101);
                    });

                    buttonUse.setOnClickListener(v -> {
                        //转换
                        List<HisPrescriptionTemplateDetailDtosBean> detailDtos = bean.getHisPrescriptionTemplateDetailDtos();
                        String templateName = bean.getTemplateName();
                        DrugBean drugBean = new DrugBean();
                        List<DrugBean.RowsBean> mData = new ArrayList<>();
                        for (int i = 0; i < detailDtos.size(); i++) {
                            DrugBean.RowsBean rowsBean = new DrugBean.RowsBean();
                            rowsBean.setDosage(detailDtos.get(i).getDosage() + "");
                            rowsBean.setDosageUnits(detailDtos.get(i).getDosageUnits());
                            rowsBean.setNum(detailDtos.get(i).getAmount());
                            rowsBean.setId(detailDtos.get(i).getPhamId());
                            rowsBean.setAdministration(detailDtos.get(i).getAdministration());
                            rowsBean.setFrequency(detailDtos.get(i).getFrequency());
                            rowsBean.setPhamAliasName(detailDtos.get(i).getPhamName());
                            rowsBean.setPhamSpec(detailDtos.get(i).getPhamSpec());
                            rowsBean.setTemplateName(templateName);
                            mData.add(rowsBean);
                        }
                        drugBean.setRows(mData);
                        Intent intent = new Intent();
                        intent.putExtra("drugBean", drugBean);
                        setResult(RESULT_OK, intent);
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
        mPresenter.onRefresh(mPhamCategoryId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
