package com.ssh.shuoshi.ui.prescription.chinesemedicine.addherbs;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.bean.template.HisPrescriptionTemplateDetailDtosBean;
import com.ssh.shuoshi.bean.template.HisPrescriptionTemplateTcmDetailsBean;
import com.ssh.shuoshi.bean.template.PrescriptionTemplateBean;
import com.ssh.shuoshi.inter.MyTextWatcher;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.ChooseCommonSideDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 中药 添加药材
 */
public class ChineseMedicineMedicalAddHerbsActivity extends BaseActivity implements ChineseMedicineMedicalAddHerbsContract.View, View.OnClickListener {

    @Inject
    ChineseMedicineMedicalAddHerbsPresenter mPresenter;

    @BindView(R.id.recyclerViewHerbsChoose)
    RecyclerView recyclerViewHerbsChoose;
    @BindView(R.id.recyclerViewHerbsSearch)
    RecyclerView recyclerViewHerbsSearch;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.editTextSearch)
    EditText editTextSearch;
    @BindView(R.id.textViewCommonPrescription)
    TextView textViewCommonPrescription;

    private Integer phamVendorId;

    private ChineseMedicineMedicalAddHerbsSearchAdapter mAdapterSearch =
            new ChineseMedicineMedicalAddHerbsSearchAdapter();

    private ChineseMedicineMedicalAddHerbsChooseAdapter mAdapterChoose =
            new ChineseMedicineMedicalAddHerbsChooseAdapter();

    private List<DrugBean.RowsBean> chooseList = new ArrayList<>();
    private List<PrescriptionTemplateBean.RowsBean> mData = new ArrayList<>();

    @Override
    public int initContentView() {
        return R.layout.activity_chinese_medicine_add_herbs;
    }

    @Override
    protected void initInjector() {
        DaggerChineseMedicineMedicalAddHerbsComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }


    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        mPresenter.loadData(null, phamVendorId);
        setIsDispatch(false);
        tvBack.setOnClickListener(v -> finish());
        tvSave.setOnClickListener(v -> {
            if (chooseList.size() == 0) {
                ToastUtil.showToast("至少添加一种药材");
                return;
            }

            for (int i = 0; i < chooseList.size(); i++) {
                if (chooseList.get(i).getNumAddDrug() == null || chooseList.get(i).getNumAddDrug() == 0) {
                    ToastUtil.showToast("药材重量不能为0");
                    return;
                }
            }

            Intent intent = new Intent();
            intent.putExtra("list", (Serializable) chooseList);
            setResult(RESULT_OK, intent);
            finish();
        });


        initRecyclerViewHerbsChoose();
        initRecyclerViewHerbsSearch();

        List<DrugBean.RowsBean> list = (List<DrugBean.RowsBean>) getIntent().getSerializableExtra("drugBean");
        if (list != null && list.size() != 0) {
            chooseList = list;
            mAdapterChoose.setList(chooseList);
        }
        mPresenter.onRefresh();
        initData();

        textViewCommonPrescription.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewCommonPrescription:
                if (mData == null || mData.size() == 0) {
                    ToastUtil.showToast("暂时没有常用方");
                    return;
                }
                ChooseCommonSideDialog sideDialog = new ChooseCommonSideDialog(this, "选择常用方", mData);
                sideDialog.setOnItemClickListener(new ChooseCommonSideDialog.ItemClickListener() {

                    @Override
                    public void cancel() {
                        sideDialog.dismiss();
                    }

                    @Override
                    public void choose(PrescriptionTemplateBean.RowsBean bean) {
                        //赋值，刷新adapter
                        transition(bean);
                        sideDialog.dismiss();
                    }

                });
                sideDialog.show();
                break;
        }
    }

    //转换实体类
    private void transition(PrescriptionTemplateBean.RowsBean bean) {
        chooseList.clear();
        List<HisPrescriptionTemplateDetailDtosBean> dtos = bean.getHisPrescriptionTemplateDetailDtos();
        for (int i = 0; i < dtos.size(); i++) {
            List<HisPrescriptionTemplateTcmDetailsBean> tcmDetails = dtos.get(i).getHisPrescriptionTemplateTcmDetails();
            for (int j = 0; j < tcmDetails.size(); j++) {
                DrugBean.RowsBean rowsBean = new DrugBean.RowsBean();
                rowsBean.setPhamAliasName(tcmDetails.get(j).getPhamName());
                rowsBean.setNumAddDrug(tcmDetails.get(j).getAmount());
                rowsBean.setId(tcmDetails.get(j).getPhamId());
                chooseList.add(rowsBean);
            }
        }
        mAdapterChoose.setList(chooseList);
    }

    private void initData() {
        editTextSearch.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.loadData(s.toString(), phamVendorId);
            }
        });
    }

    private void initRecyclerViewHerbsChoose() {
        recyclerViewHerbsChoose.setLayoutManager(new GridLayoutManager(ChineseMedicineMedicalAddHerbsActivity.this, 2));
        recyclerViewHerbsChoose.setAdapter(mAdapterChoose);
    }

    private void initRecyclerViewHerbsSearch() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChineseMedicineMedicalAddHerbsActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewHerbsSearch.setLayoutManager(linearLayoutManager);
        mAdapterSearch.setOnItemClickListener((view, position) -> {
            DrugBean.RowsBean bean = mAdapterSearch.getList().get(position);
            for (int i = 0; i < chooseList.size(); i++) {
                if (bean.getId() == chooseList.get(i).getId()) {
                    ToastUtil.showToast("药材已添加");
                    return;
                }
            }
            phamVendorId = bean.getPhamVendorId();
            chooseList.add(bean);
            mAdapterChoose.setList(chooseList);

            editTextSearch.setText("");
        });
        recyclerViewHerbsSearch.setAdapter(mAdapterSearch);
    }

    @Override
    public void onLoadDataSuccess(DrugBean beans) {
        if (beans != null && beans.getRows() != null) {
            mAdapterSearch.setList(beans.getRows());
        }
    }

    @Override
    public void onRefreshCompleted(PrescriptionTemplateBean beans) {
        mData.clear();
        mData.addAll(beans.getRows());
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


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}