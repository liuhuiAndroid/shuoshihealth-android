package com.ssh.shuoshi.ui.myprescription.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDetailDtosBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionTcmDetailsBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.prescription.chinesemedicine.edit.EditChineseMedicinePrescriptionActivity;
import com.ssh.shuoshi.ui.prescription.westernmedicine.edit.EditPrescriptionActivity;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * 处方详情
 * created by hwt on 2021/1/2
 */
public class MyPrescriptionDetailActivity extends BaseActivity implements
        MyPrescriptionDetailContract.View, View.OnClickListener {

    @Inject
    MyPrescriptionDetailPresenter mPresenter;
    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.textState)
    TextView textState;
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
    @BindView(R.id.textDiagnose)
    TextView textDiagnose;
    @BindView(R.id.textResult)
    TextView textResult;
    @BindView(R.id.textViewPatientTitle)
    TextView textViewPatientTitle;
    @BindView(R.id.recyclerViewWest)
    RecyclerView recyclerViewWest;
    @BindView(R.id.recyclerViewChinese)
    RecyclerView recyclerViewChinese;
    @BindView(R.id.textDoctorsHint)
    TextView textDoctorsHint;
    @BindView(R.id.textDoctors)
    TextView textDoctors;
    @BindView(R.id.textPharmacistHint)
    TextView textPharmacistHint;
    @BindView(R.id.textPharmacist)
    TextView textPharmacist;
    @BindView(R.id.textNoPassResult)
    TextView textNoPassResult;
    @BindView(R.id.llNoPassResult)
    LinearLayout llNoPassResult;
    @BindView(R.id.textOrderInfo)
    TextView textOrderInfo;
    @BindView(R.id.textOrder1)
    TextView textOrder1;
    @BindView(R.id.textOrderNo)
    TextView textOrderNo;
    @BindView(R.id.textOrder2)
    TextView textOrder2;
    @BindView(R.id.textOrderTime)
    TextView textOrderTime;
    @BindView(R.id.textOrder3)
    TextView textOrder3;
    @BindView(R.id.textOrderState)
    TextView textOrderState;
    @BindView(R.id.textOrder4)
    TextView textOrder4;
    @BindView(R.id.textOrderPrice)
    TextView textOrderPrice;
    @BindView(R.id.llOrderInfo)
    RelativeLayout llOrderInfo;
    @BindView(R.id.buttonRestart)
    Button buttonRestart;
    @BindView(R.id.textMethod)
    TextView textMethod;
    @BindView(R.id.textMethodDesc)
    TextView textMethodDesc;
    @BindView(R.id.textUse)
    TextView textUse;
    @BindView(R.id.textUseDesc)
    TextView textUseDesc;
    @BindView(R.id.textAdvice)
    TextView textAdvice;
    @BindView(R.id.textAdviceDesc)
    TextView textAdviceDesc;
    @BindView(R.id.rl_chinese)
    RelativeLayout rlChinese;

    @Inject
    UserStorage mUserStorage;

    private LoadingDialog mLoadingDialog;
    //获取处方的数据,处方ID
    private int prescriptionId;
    //处方详细信息
    private HisPrescriptionDtoBean mBean;

    private boolean isWest = true;
    private boolean notChange;

    @Override
    public int initContentView() {
        return R.layout.activity_my_prescription_detail;
    }

    @Override
    protected void initInjector() {
        DaggerMyPrescriptionDetailComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        uniteTitle.setBackListener(-1, v -> finish());

        Intent intent = getIntent();
        prescriptionId = intent.getIntExtra("prescriptionId", -1);

        notChange = intent.getBooleanExtra("notChange", false);

        mPresenter.getPrescriptionFromId(prescriptionId);

        buttonRestart.setOnClickListener(this);
    }


    @Override
    public void getPrescriptionFromIdSuccess(HisPrescriptionDtoBean bean) {
        this.mBean = bean;
        fillData();
    }

    //填充数据
    private void fillData() {
        int state = mBean.getState();
        switch (state) {
            case 2:
                textState.setText("审核中");
                break;
            case 3:
                int paymentState = mBean.getPaymentState();
                if (paymentState == 0) {
                    textState.setText("审核通过，待支付");
                    llOrderInfo.setVisibility(View.GONE);
                } else {
                    textState.setText("审核通过，已支付");
                    llOrderInfo.setVisibility(View.VISIBLE);
                    fillOrderData();
                }
                break;

            default:
                textState.setText("审核不通过");
                textNoPassResult.setText("审核不通过：" + mBean.getApprovedOpinion());
                llNoPassResult.setVisibility(View.VISIBLE);
                buttonRestart.setVisibility(View.VISIBLE);
                break;
        }

        String date = mBean.getVisitDate();
        if (!TextUtils.isEmpty(date) && date.length() >= 10) {
            date = date.substring(0, 10);
        }
        textNo.setText(mBean.getPerscriptionNo());
        textName.setText("姓名：" + mBean.getPatientName());
        textSex.setText("性别：" + mBean.getSexName());
        textAge.setText("年龄：" + mBean.getPatientAge() + "岁");
        textDepartment.setText("科室：" + mBean.getDeptName());
        textTime.setText(date);
        textResult.setText(mBean.getDiagDesc());
        textDoctors.setText(mBean.getDoctorName());
        textPharmacist.setText(mBean.getPharmacistName());

        HisDoctorBean doctorInfo = mUserStorage.getDoctorInfo();
        int id = doctorInfo.getId();
        if (id != mBean.getDoctorId()) {
            buttonRestart.setEnabled(false);
        }

        if (notChange){
            buttonRestart.setEnabled(false);
        }

        initAdapter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRestart:
                if (rlChinese.getVisibility() == View.GONE) {
                    Intent intent2 = new Intent(this, EditPrescriptionActivity.class);
                    intent2.putExtra("prescriptionDetailBean", mBean);
                    intent2.putExtra("isRequest", false);
                    startActivityForResult(intent2, 300);
                } else {
                    Intent intent2 = new Intent(this, EditChineseMedicinePrescriptionActivity.class);
                    intent2.putExtra("prescriptionDetailBean", mBean);
                    intent2.putExtra("isRequest", false);
                    startActivityForResult(intent2, 300);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 300) {
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    //订单信息
    private void fillOrderData() {
        HisPrescriptionDtoBean.HisPrescriptionOrderBean order = mBean.getHisPrescriptionOrder();
        if (order == null) {
            return;
        }
        textOrderNo.setText(order.getOrderNo());
        textOrderTime.setText(order.getPayTime());
        Integer orderState = order.getOrderState();
        switch (orderState) {
            case 0:
                textOrderState.setText("未支付");
                break;
            case 1:
            case 2:
            case 3:
                textOrderState.setText("已支付");
                break;
        }
        textOrderPrice.setText("¥" + order.getPrice());
    }

    @SuppressLint("WrongConstant")
    private void initAdapter() {
        //展示界面
        List<HisPrescriptionDetailDtosBean> detailDtos = mBean.getHisPrescriptionDetailDtos();
        if (detailDtos == null && detailDtos.size() == 0) {
            return;
        }
        HisPrescriptionDetailDtosBean detailDtosBean = detailDtos.get(0);
        List<HisPrescriptionTcmDetailsBean> tcmDetails = detailDtosBean.getHisPrescriptionTcmDetails();
        if (tcmDetails == null || tcmDetails.size() == 0) {
            isWest = true;
        } else {
            isWest = false;
        }

        if (isWest) {
            recyclerViewWest.setVisibility(View.VISIBLE);
            rlChinese.setVisibility(View.GONE);
            recyclerViewWest.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
            CommonAdapter commonAdapter1 = new CommonAdapter<HisPrescriptionDetailDtosBean>(this, R.layout.item_my_prescription_detail_west_item, detailDtos) {
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
            recyclerViewWest.setAdapter(commonAdapter1);
        } else {
            recyclerViewWest.setVisibility(View.GONE);
            rlChinese.setVisibility(View.VISIBLE);
            recyclerViewChinese.setLayoutManager(new GridLayoutManager(this, 3));

            CommonAdapter commonAdapter2 = new CommonAdapter<HisPrescriptionTcmDetailsBean>(this,
                    R.layout.item_my_prescription_detail_chinese_item, tcmDetails) {
                @Override
                protected void convert(ViewHolder holder, final HisPrescriptionTcmDetailsBean bean, final int pos) {
                    TextView textName = holder.getView(R.id.textName);
                    textName.setText(bean.getPhamName() + bean.getAmount() + bean.getUnits());
                }
            };
            recyclerViewChinese.setAdapter(commonAdapter2);

            textUseDesc.setText("共" + detailDtosBean.getAmount() + detailDtosBean.getDosageUnits() +
                    ", 每次" + detailDtosBean.getDosage() + detailDtosBean.getDosageUnits() + ", " +
                    detailDtosBean.getFrequency() + "," + detailDtosBean.getAdministration());
            textMethodDesc.setText(detailDtosBean.getPhamName());
            textAdviceDesc.setText(detailDtosBean.getFreqDetail());
        }
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(MyPrescriptionDetailActivity.this, "");
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
        hideLoading();
    }

}
