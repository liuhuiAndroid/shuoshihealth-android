package com.ssh.shuoshi.ui.prescription.chinesemedicine.edit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.common.SystemTypeBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDetailDtosBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionTcmDetailsBean;
import com.ssh.shuoshi.bean.prescription.PrescriptionStateBean;
import com.ssh.shuoshi.bean.request.ChineseAddPrescriptionDrugs;
import com.ssh.shuoshi.bean.request.ChineseAddPrescriptionDrugsTcmDetails;
import com.ssh.shuoshi.bean.request.ChineseAddPrescriptionTemplateDrugs;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.ChineseTemplateSaveDialog;
import com.ssh.shuoshi.ui.dialog.ChooseDictDialog;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.prescription.chinesemedicine.addherbs.ChineseMedicineMedicalAddHerbsActivity;
import com.ssh.shuoshi.ui.prescription.chinesemedicine.advice.ChineseMedicineMedicalAdviceActivity;
import com.ssh.shuoshi.ui.verified.choose.AspirationVerifiedChooseActivity;
import com.ssh.shuoshi.ui.verified.sign.PrescriptionSignActivity;
import com.ssh.shuoshi.ui.verified.start.VerifiedStartActivity;
import com.ssh.shuoshi.view.pickview.builder.OptionsPickerBuilder;
import com.ssh.shuoshi.view.pickview.view.OptionsPickerView;
import com.ssh.shuoshi.view.title.UniteTitle;
import com.tencent.mmkv.MMKV;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ?????? ????????????
 */
public class EditChineseMedicinePrescriptionActivity extends BaseActivity
        implements EditChineseMedicinePrescriptionContract.View, View.OnClickListener {

    @Inject
    EditChineseMedicinePrescriptionPresenter mPresenter;

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

    @BindView(R.id.relativeLayoutMedicationRequirements)
    RelativeLayout relativeLayoutMedicationRequirements;
    @BindView(R.id.textViewMedicationRequirements)
    TextView textViewMedicationRequirements;
    @BindView(R.id.relativeLayoutMedicalAdvice)
    RelativeLayout relativeLayoutMedicalAdvice;
    @BindView(R.id.textViewMedicalAdvice)
    TextView textViewMedicalAdvice;
    @BindView(R.id.buttonSavePrescription)
    Button buttonSavePrescription;

    @BindView(R.id.relativeLayoutEditDrugs)
    RelativeLayout relativeLayoutEditDrugs;
    @BindView(R.id.textViewChooseDict)
    TextView textViewChooseDict;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;

    @BindView(R.id.editTextTotal)
    EditText editTextTotal;
    @BindView(R.id.editTextSub)
    EditText editTextSub;
    @BindView(R.id.tv_add_change)
    TextView tvAddChange;

    private int requestCodeAdvice = 1001;
    private int requestCodeAddHerbs = 1002;
    private ChooseDictDialog chooseDictDialog;
    private List<DrugBean.RowsBean> chooseListResult = new ArrayList<>();
    private EditChineseMedicinePrescriptionHerbsAdapter mAdapterDrugs;
    private LoadingDialog mLoadingDialog;
    //????????????
    private List<SystemTypeBean.RowsBean> mFrequency = new ArrayList<>();
    //????????????????????????
    private boolean mIsRequest;
    @Inject
    UserStorage mUserStorage;
    //?????????????????????ID
    private Integer mPrescriptionId;

    //??????????????????????????????????????????
    private boolean isFirst = true;
    //??????????????????????????????ID
    private int beanId;

    //???????????????????????????????????????
    private ImageDiagnoseBean.RowsBean mConsultaionInfoBean;
    //???????????????????????????????????????
    private PrescriptionStateBean.RowsBean prescriptionBean;
    //???????????????????????????????????????
    private HisPrescriptionDtoBean prescriptionDetailBean;
    private Integer secondId = null;
    private HisDoctorBean doctorInfo;

    @Override
    public int initContentView() {
        return R.layout.activity_chinese_medicine_edit;
    }

    @Override
    protected void initInjector() {
        DaggerEditChineseMedicinePrescriptionComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);

        mConsultaionInfoBean = (ImageDiagnoseBean.RowsBean) getIntent().getSerializableExtra("consultaion");
        prescriptionBean = (PrescriptionStateBean.RowsBean) getIntent().getSerializableExtra("prescriptionBean");
        prescriptionDetailBean = (HisPrescriptionDtoBean) getIntent().getSerializableExtra("prescriptionDetailBean");
        mIsRequest = getIntent().getBooleanExtra("isRequest", false);
        doctorInfo = mUserStorage.getDoctorInfo();
        if (mIsRequest) {
            if (mConsultaionInfoBean != null && mConsultaionInfoBean.getPrescriptionState() != null) {
                mPresenter.getPrescriptionFromId(mConsultaionInfoBean.getPrescriptionId());
            } else if (prescriptionBean != null) {
                mPresenter.getPrescriptionFromId(prescriptionBean.getId());
            }
        }
        if (TextUtils.isEmpty(getIntent().getStringExtra("name"))) {
            uniteTitle.setTitleName(getIntent().getStringExtra("name"));
        }
        uniteTitle.setBackListener(-1, v -> finish());
        initRecycleView();

        if (mConsultaionInfoBean != null) {
            textName.setText("?????????" + mConsultaionInfoBean.getPatientName());
            textSex.setText("?????????" + mConsultaionInfoBean.getSexName());
            textAge.setText("?????????" + mConsultaionInfoBean.getPatientAge());
            textDepartment.setText("?????????" + doctorInfo.getHisSysDeptName());
            textResult.setText("???????????????" + mConsultaionInfoBean.getEmrDiagDesc());
            if (mConsultaionInfoBean != null &&
                    mConsultaionInfoBean.getCreateTime() != null &&
                    mConsultaionInfoBean.getCreateTime().length() > 9) {
                textTime.setText(mConsultaionInfoBean.getCreateTime().substring(0, 10));
            }
        } else if (prescriptionBean != null) {
            //??????????????????????????????
            setPrescriptionBean();
        } else if (prescriptionDetailBean != null) {
            setPrescriptionDetailBean();
        }

        //??????????????????
        mPresenter.getFrequencyList();
        buttonSavePrescription.setOnClickListener(this);
        relativeLayoutMedicalAdvice.setOnClickListener(this);
        relativeLayoutEditDrugs.setOnClickListener(this);
        textViewChooseDict.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);
    }

    //??????????????????????????????????????????
    private void setPrescriptionBean() {
        textName.setText("?????????" + prescriptionBean.getPatientName());
        textSex.setText("?????????" + prescriptionBean.getSexName());
        textAge.setText("?????????" + prescriptionBean.getPatientAge());
        textDepartment.setText("?????????" + prescriptionBean.getDeptName());
        textResult.setText("???????????????" + prescriptionBean.getDiagDesc());
        //????????????
        List<HisPrescriptionDetailDtosBean> detailDtos = prescriptionBean.getHisPrescriptionDetailDtos();
        if (!mIsRequest) {
            setDetailBena(detailDtos);
        }
        if (prescriptionBean != null &&
                prescriptionBean.getCreateTime() != null &&
                prescriptionBean.getCreateTime().length() > 9) {
            textTime.setText(prescriptionBean.getCreateTime().substring(0, 10));
        }
    }

    //??????????????????????????????????????????
    private void setPrescriptionDetailBean() {

        textName.setText("?????????" + prescriptionDetailBean.getPatientName());
        textSex.setText("?????????" + prescriptionDetailBean.getSexName());
        textAge.setText("?????????" + prescriptionDetailBean.getPatientAge());
        textDepartment.setText("?????????" + prescriptionDetailBean.getDeptName());
        textResult.setText("???????????????" + prescriptionDetailBean.getDiagDesc());

        //????????????
        List<HisPrescriptionDetailDtosBean> detailDtos = prescriptionDetailBean.getHisPrescriptionDetailDtos();
        if (!mIsRequest) {
            setDetailBena(detailDtos);
        }
        if (prescriptionDetailBean != null &&
                prescriptionDetailBean.getCreateTime() != null &&
                prescriptionDetailBean.getCreateTime().length() > 9) {
            textTime.setText(prescriptionDetailBean.getCreateTime().substring(0, 10));
        }
    }

    private void setDetailBena(List<HisPrescriptionDetailDtosBean> detailDtos) {
        if (detailDtos == null || detailDtos.size() == 0) {
            return;
        }
        HisPrescriptionDetailDtosBean dtosBean = detailDtos.get(0);
        textViewMedicalAdvice.setText(dtosBean.getFreqDetail());
        textViewMedicationRequirements.setText(dtosBean.getAdministration());
        editTextTotal.setText(dtosBean.getAmount() + "");
        String dosage = dtosBean.getDosage() + "";
        if (dosage.length() > 2 && dosage.substring(dosage.length() - 2).contains(".0")) {
            String substring = dosage.substring(0, dosage.length() - 2);
            editTextSub.setText(substring);
        } else {
            editTextSub.setText(dosage);
        }
        textViewChooseDict.setText(dtosBean.getFrequency());
        List<HisPrescriptionTcmDetailsBean> tcmDetails = dtosBean.getHisPrescriptionTcmDetails();
        if (tcmDetails == null || tcmDetails.size() == 0) {
            return;
        }
        for (int i = 0; i < tcmDetails.size(); i++) {
            DrugBean.RowsBean rowsBean = new DrugBean.RowsBean();
            rowsBean.setPhamAliasName(tcmDetails.get(i).getPhamName());
            rowsBean.setId(tcmDetails.get(i).getPhamId());
            rowsBean.setNumAddDrug(tcmDetails.get(i).getAmount());
            chooseListResult.add(rowsBean);
        }
        tvAddChange.setText("????????????");
        recyclerView.setVisibility(View.VISIBLE);
        mAdapterDrugs.setList(chooseListResult);
        buttonSavePrescription.setVisibility(View.VISIBLE);
    }

    private void initRecycleView() {
        mAdapterDrugs = new EditChineseMedicinePrescriptionHerbsAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(EditChineseMedicinePrescriptionActivity.this, 3));
        recyclerView.setAdapter(mAdapterDrugs);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //???????????????
            case R.id.buttonSavePrescription:
                ChineseTemplateSaveDialog saveDialog = new ChineseTemplateSaveDialog(EditChineseMedicinePrescriptionActivity.this);
                saveDialog.setOnItemClickListener(new ChineseTemplateSaveDialog.ItemClickListener() {

                    @Override
                    public void cancel() {
                        saveDialog.dismiss();
                    }

                    @Override
                    public void confirm(String name) {
                        saveCommonTemplate(name);
                        saveDialog.dismiss();
                    }
                });
                saveDialog.show();
                break;

            //??????
            case R.id.relativeLayoutMedicalAdvice:
                Intent intent22 = new Intent(EditChineseMedicinePrescriptionActivity.this,
                        ChineseMedicineMedicalAdviceActivity.class);
                intent22.putExtra("oldData", textViewMedicalAdvice.getText().toString());
                startActivityForResult(intent22, requestCodeAdvice);
                break;

            //????????????
            case R.id.relativeLayoutEditDrugs:
                if (mAdapterDrugs.getList().size() == 0) {
                    Intent intent = new Intent(EditChineseMedicinePrescriptionActivity.this,
                            ChineseMedicineMedicalAddHerbsActivity.class);
                    startActivityForResult(intent, requestCodeAddHerbs);

                } else {
                    Intent intent = new Intent(EditChineseMedicinePrescriptionActivity.this,
                            ChineseMedicineMedicalAddHerbsActivity.class);
                    intent.putExtra("drugBean", (Serializable) mAdapterDrugs.getList());
                    startActivityForResult(intent, requestCodeAddHerbs);
                }
                break;

            //????????????
            case R.id.textViewChooseDict:
                if (mFrequency.size() == 0) {
                    ToastUtil.showToast("??????????????????????????????");
                    return;
                }

                showFrequencyDialog();

//                chooseDictDialog = new ChooseDictDialog(EditChineseMedicinePrescriptionActivity.this, "?????????????????????", mFrequency);
//                chooseDictDialog.setCanceledOnTouchOutside(false);
//                chooseDictDialog.setOnItemClickListener(new ChooseDictDialog.ItemClickListener() {
//                    @Override
//                    public void cancel() {
//                        chooseDictDialog.dismiss();
//                    }
//
//                    @Override
//                    public void choose(String name) {
//                        textViewChooseDict.setText(name);
//                        chooseDictDialog.dismiss();
//                    }
//                });
//                chooseDictDialog.show();
                break;
            //????????????
            case R.id.buttonSubmit:

                String medicationRequirements = textViewMedicationRequirements.getText().toString().trim();

                String total = editTextTotal.getText().toString().trim();
                String sub = editTextSub.getText().toString().trim();

                if (chooseListResult.size() <= 0) {
                    ToastUtil.showToast("???????????????");
                    return;
                }

                if (TextUtils.isEmpty(total) || total.equals("0")) {
                    ToastUtil.showToast("???????????????????????????");
                    return;
                }

                if (TextUtils.isEmpty(sub) || sub.equals("0")) {
                    ToastUtil.showToast("?????????????????????????????????");
                    return;
                }

                if (Double.parseDouble(sub) > Double.parseDouble(total)) {
                    ToastUtil.showToast("??????????????????????????????");
                    return;
                }

                String medicalAdvice = textViewMedicalAdvice.getText().toString().trim();
                if (medicalAdvice.isEmpty() || medicalAdvice.equals("??????")) {
                    ToastUtil.showToast("???????????????");
                    return;
                }

                String frequency = textViewChooseDict.getText().toString().trim();
                if (TextUtils.isEmpty(frequency)) {
                    ToastUtil.showToast("?????????????????????");
                    return;
                }


                Map<String, Object> mMap = new HashMap<>();

                if (mConsultaionInfoBean != null) {
                    mMap.put("consultationId", mConsultaionInfoBean.getId());
                    mMap.put("patientId", mConsultaionInfoBean.getPatientId());
                } else if (prescriptionBean != null) {
                    mMap.put("consultationId", prescriptionBean.getConsultationId());
                    mMap.put("patientId", prescriptionBean.getPatientId());
                } else {
                    mMap.put("consultationId", prescriptionDetailBean.getConsultationId());
                    mMap.put("patientId", prescriptionDetailBean.getPatientId());
                }
                mMap.put("perscriptionTypeId", 2);

                List<ChineseAddPrescriptionDrugsTcmDetails> cadtds = new ArrayList<>();
                for (int i = 0; i < chooseListResult.size(); i++) {
                    DrugBean.RowsBean rowsBean = chooseListResult.get(i);
                    cadtds.add(new ChineseAddPrescriptionDrugsTcmDetails(rowsBean.getId(), rowsBean.getNumAddDrug().intValue()));
                }
                ChineseAddPrescriptionDrugs capd = new ChineseAddPrescriptionDrugs(
                        total, sub, medicationRequirements, frequency, medicalAdvice, secondId, cadtds);
                List<ChineseAddPrescriptionDrugs> list = new ArrayList<>();
                list.add(capd);
                mMap.put("hisPrescriptionDetailDtos", list);
                showLoading();
                if (isFirst) {
                    mPresenter.addPrescription(mMap);
                } else {
                    mMap.put("id", beanId);
                    mPresenter.putPrescription(mMap);
                }
                break;
        }
    }

    //????????????
    private void showFrequencyDialog() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            String name = mFrequency.get(options1).getDictLabel();
            textViewChooseDict.setText(name);
        })
                .setTitleText("?????????????????????")
                .setCancelText(" ")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(16)
                .build();
        pvOptions.setPicker(mFrequency);
        pvOptions.show();
    }

    //???????????????
    private void saveCommonTemplate(String name) {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("templateName", name);
        mMap.put("perscriptionTypeId", 2);
        List<ChineseAddPrescriptionDrugsTcmDetails> cadtds = new ArrayList<>();
        for (int i = 0; i < chooseListResult.size(); i++) {
            DrugBean.RowsBean rowsBean = chooseListResult.get(i);
            cadtds.add(new ChineseAddPrescriptionDrugsTcmDetails(rowsBean.getId(), rowsBean.getNumAddDrug()));
        }
        ChineseAddPrescriptionTemplateDrugs capd = new ChineseAddPrescriptionTemplateDrugs(cadtds);
        List<ChineseAddPrescriptionTemplateDrugs> list = new ArrayList<>();
        list.add(capd);
        mMap.put("hisPrescriptionTemplateDetailDtos", list);

        showLoading();
        mPresenter.addPrescriptionTemplate(mMap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1001:
                    String advice = data.getStringExtra("advice");
                    if (!advice.isEmpty()) {
                        textViewMedicalAdvice.setText(advice);
                    }
                    break;

                case 1002:
                    List<DrugBean.RowsBean> chooseList = (List<DrugBean.RowsBean>) data.getSerializableExtra("list");
                    tvAddChange.setText("????????????");
                    recyclerView.setVisibility(View.VISIBLE);

                    mAdapterDrugs.setList(chooseList);
                    buttonSavePrescription.setVisibility(View.VISIBLE);

                    chooseListResult.clear();
                    chooseListResult.addAll(chooseList);
                    break;

                //??????300
                case 300:
                    Intent intent = new Intent();
                    intent.putExtra("prescriptionId", mPrescriptionId);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        }
    }

    @Override
    public void getFrequencyList(SystemTypeBean bean) {
        this.mFrequency = bean.getRows();
    }

    @Override
    public void getPrescriptionFromIdSuccess(HisPrescriptionDtoBean bean) {
        beanId = bean.getId();
        isFirst = false;
        textNo.setText(bean.getPerscriptionNo());
        textDepartment.setText("?????????" + bean.getDeptName());
        //????????????
        List<HisPrescriptionDetailDtosBean> detailDtos = bean.getHisPrescriptionDetailDtos();
        if (detailDtos == null || detailDtos.size() == 0) {
            return;
        }
        HisPrescriptionDetailDtosBean dtosBean = detailDtos.get(0);
        secondId = dtosBean.getId();
        Log.i("hwt", "secondId=== " + secondId);
        textViewMedicalAdvice.setText(dtosBean.getFreqDetail());
        textViewMedicationRequirements.setText(dtosBean.getAdministration());
        editTextTotal.setText(dtosBean.getAmount() + "");
        editTextSub.setText(dtosBean.getDosage() + "");
        textViewChooseDict.setText(dtosBean.getFrequency());
        List<HisPrescriptionTcmDetailsBean> tcmDetails = dtosBean.getHisPrescriptionTcmDetails();
        if (tcmDetails == null || tcmDetails.size() == 0) {
            return;
        }
        for (int i = 0; i < tcmDetails.size(); i++) {
            DrugBean.RowsBean rowsBean = new DrugBean.RowsBean();
            rowsBean.setPhamAliasName(tcmDetails.get(i).getPhamName());
            rowsBean.setId(tcmDetails.get(i).getPhamId());
            rowsBean.setNumAddDrug(tcmDetails.get(i).getAmount());
            chooseListResult.add(rowsBean);
        }
        tvAddChange.setText("????????????");
        recyclerView.setVisibility(View.VISIBLE);
        mAdapterDrugs.setList(chooseListResult);
        buttonSavePrescription.setVisibility(View.VISIBLE);

    }

    @Override
    public void addPrescriptionSuccess(Integer prescriptionId) {
        this.mPrescriptionId = prescriptionId;
        ToastUtil.showToast("??????????????????");
        // TODO: 2020/12/29  ????????????????????????????????????????????????
        //1????????????3?????????2???????????????
        //Integer authType;
        //???????????????0????????????1?????????
        //Integer authState;
        //0????????????1??????
        //Integer desireState;

        Integer authType = doctorInfo.getAuthType();
        Integer authState = doctorInfo.getAuthState();
        Integer desireState = doctorInfo.getDesireState();
        MMKV.defaultMMKV().putString("prescriptionType", "??????");
        //????????????????????????
        if (authType == null || authState == null || authState == 0) {
            goNewVerify(prescriptionId);
            return;
        }

        //??????????????????????????????????????????????????????
        if (authState == 1) {
            //??????????????????????????????????????????????????????
            if (desireState == null) {
                Intent intent = new Intent(EditChineseMedicinePrescriptionActivity.this, AspirationVerifiedChooseActivity.class);
                intent.putExtra("prescriptionId", prescriptionId);
//                intent.putExtra("perscriptionTypeId", "2");
                //????????????
                intent.putExtra("authentication", authType);
                startActivityForResult(intent, 300);
                return;
            }

            //????????????????????????
            Intent intent = new Intent(EditChineseMedicinePrescriptionActivity.this, PrescriptionSignActivity.class);
            intent.putExtra("prescriptionId", prescriptionId);
//            intent.putExtra("perscriptionTypeId", "2");
            intent.putExtra("authentication", authType);
            //??????0?????????
            if (desireState == 0) {
                intent.putExtra("type", 1);
            } else if (desireState == 1) {
                //??????1??????
                intent.putExtra("type", 2);
            }
            startActivityForResult(intent, 300);
        }
    }

    private void goNewVerify(Integer prescriptionId) {
        Intent intent = new Intent(EditChineseMedicinePrescriptionActivity.this, VerifiedStartActivity.class);
        intent.putExtra("prescriptionId", prescriptionId);
//        intent.putExtra("perscriptionTypeId", "2");
        startActivityForResult(intent, 300);
    }

    @Override
    public void addPrescriptionTemplateSuccess() {
        ToastUtil.showToast("?????????????????????");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(EditChineseMedicinePrescriptionActivity.this, "");
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