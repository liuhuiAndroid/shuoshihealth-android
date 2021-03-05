package com.ssh.shuoshi.ui.prescription.westernmedicine.history;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDetailDtosBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 历史处方
 */
public class HistoryPrescriptionActivity extends BaseActivity implements HistoryPrescriptionContract.View, View.OnClickListener {

    @Inject
    HistoryPrescriptionPresenter mPresenter;

    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.textNo)
    TextView textNo;
    @BindView(R.id.textTime)
    TextView textTime;
    @BindView(R.id.textName)
    TextView textName;
    @BindView(R.id.textSex)
    TextView textSex;
    @BindView(R.id.textAge)
    TextView textAge;
    @BindView(R.id.textDepartment)
    TextView textDepartment;
    @BindView(R.id.textResult)
    TextView textResult;

    @BindView(R.id.textRemarks)
    TextView textRemarks;

    @BindView(R.id.recyclerViewDrugs)
    RecyclerView mRecyclerView;

    @Inject
    UserStorage mUserStorage;
    @BindView(R.id.textViewPatientTitle)
    TextView textViewPatientTitle;
    @BindView(R.id.textDoctorsHint)
    TextView textDoctorsHint;
    @BindView(R.id.textDoctors)
    TextView textDoctors;
    @BindView(R.id.textPharmacistHint)
    TextView textPharmacistHint;
    @BindView(R.id.textPharmacist)
    TextView textPharmacist;
    @BindView(R.id.viewLine01)
    View viewLine01;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    private int mPhamCategoryId = 1;
    private ImageDiagnoseBean.RowsBean mConsultaionInfoBean;
    private CommonAdapter commonAdapter;
    private List<HisPrescriptionDetailDtosBean> detailDtos = new ArrayList<>();

    @Override
    public int initContentView() {
        return R.layout.activity_history_prescription;
    }

    @Override
    protected void initInjector() {
        DaggerHistoryPrescriptionComponent.builder()
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
        HisDoctorBean doctorInfo = mUserStorage.getDoctorInfo();
        mConsultaionInfoBean = (ImageDiagnoseBean.RowsBean) getIntent().getSerializableExtra("infoBean");
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        textNo.setText(mConsultaionInfoBean.getConsultationNo());
//        textTime.setText(simpleDateFormat.format(new Date()));

        textName.setText("姓名：" + mConsultaionInfoBean.getPatientName());
        textSex.setText("性别：" + mConsultaionInfoBean.getSexName());
        textAge.setText("年龄：" + mConsultaionInfoBean.getPatientAge());
        String doctorDeptName = mConsultaionInfoBean.getDoctorDeptName();
        if (TextUtils.isEmpty(doctorDeptName)) {
            textDepartment.setText("科室：" + doctorInfo.getHisSysDeptName());
        } else {
            textDepartment.setText("科室：" + doctorDeptName);
        }

        textResult.setText("诊断结果：" + mConsultaionInfoBean.getEmrDiagDesc());
        textRemarks.setText("备注：本处方72小时有效");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        String deptType = doctorInfo.getDeptType();
        if (deptType.equals("西医")) {
            mPhamCategoryId = 1;
        } else {
            mPhamCategoryId = 2;
        }

        int categoryId = getIntent().getIntExtra("mPhamCategoryId", -1);
        if (categoryId != -1) {
            mPhamCategoryId = categoryId;
        }

        mPresenter.getPrescriptionByPatientId(mConsultaionInfoBean.getPatientId(), mPhamCategoryId);
        buttonSubmit.setOnClickListener(this);
        initData();
    }

    private void initData() {
        commonAdapter = new CommonAdapter<HisPrescriptionDetailDtosBean>(this, R.layout.item_history_drugs, detailDtos) {
            @Override
            protected void convert(ViewHolder holder, final HisPrescriptionDetailDtosBean bean, final int pos) {
                TextView textName = holder.getView(R.id.textName);
                TextView textSpec = holder.getView(R.id.textSpec);
                TextView textSize = holder.getView(R.id.textSize);
                TextView textSpecification = holder.getView(R.id.textSpecification);
                textName.setText(bean.getPhamName() + "(");
                textSpec.setText(bean.getPhamSpec() + ")");
                textSize.setText("x" + bean.getAmount());
                textSpecification.setText("用法用量：" + bean.getAdministration() + ", 每次" +
                        bean.getDosage() + bean.getDosageUnits() + ", " + bean.getFrequency());
            }
        };
        mRecyclerView.setAdapter(commonAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSubmit:
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
                    mData.add(rowsBean);
                }
                drugBean.setRows(mData);
                Intent intent = new Intent();
                intent.putExtra("drugBean", drugBean);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
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
    }

    @Override
    public void getPrescription(HisPrescriptionDtoBean bean) {
        detailDtos = bean.getHisPrescriptionDetailDtos();
        commonAdapter.setDatas(detailDtos);
        textDoctors.setText(bean.getDoctorName());
        textPharmacist.setText(bean.getPharmacistName());
        textNo.setText(bean.getPerscriptionNo());
        String createTime = bean.getCreateTime();
        if (!TextUtils.isEmpty(createTime) && createTime.length() >= 10) {
            textTime.setText(createTime.substring(0, 10));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}
