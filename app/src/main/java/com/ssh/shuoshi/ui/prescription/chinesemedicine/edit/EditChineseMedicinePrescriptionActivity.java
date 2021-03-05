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
 * 中药 编辑处方
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
    //用药频次
    private List<SystemTypeBean.RowsBean> mFrequency = new ArrayList<>();
    //是否取回上次数据
    private boolean mIsRequest;
    @Inject
    UserStorage mUserStorage;
    //这是返回处方的ID
    private Integer mPrescriptionId;

    //是否是新开处方，还是编辑处方
    private boolean isFirst = true;
    //编辑处方，要传的处方ID
    private int beanId;

    //从聊天界面跳转带过来的数据
    private ImageDiagnoseBean.RowsBean mConsultaionInfoBean;
    //从我的处方列表带过来的数据
    private PrescriptionStateBean.RowsBean prescriptionBean;
    //从我的处方详情带过来的数据
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
            textName.setText("姓名：" + mConsultaionInfoBean.getPatientName());
            textSex.setText("性别：" + mConsultaionInfoBean.getSexName());
            textAge.setText("年龄：" + mConsultaionInfoBean.getPatientAge());
            textDepartment.setText("科室：" + doctorInfo.getHisSysDeptName());
            textResult.setText("诊断结果：" + mConsultaionInfoBean.getEmrDiagDesc());
            if (mConsultaionInfoBean != null &&
                    mConsultaionInfoBean.getCreateTime() != null &&
                    mConsultaionInfoBean.getCreateTime().length() > 9) {
                textTime.setText(mConsultaionInfoBean.getCreateTime().substring(0, 10));
            }
        } else if (prescriptionBean != null) {
            //从处方中过来进行赋值
            setPrescriptionBean();
        } else if (prescriptionDetailBean != null) {
            setPrescriptionDetailBean();
        }

        //用药频次列表
        mPresenter.getFrequencyList();
        buttonSavePrescription.setOnClickListener(this);
        relativeLayoutMedicalAdvice.setOnClickListener(this);
        relativeLayoutEditDrugs.setOnClickListener(this);
        textViewChooseDict.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);
    }

    //从我的处方列表中过来进行赋值
    private void setPrescriptionBean() {
        textName.setText("姓名：" + prescriptionBean.getPatientName());
        textSex.setText("性别：" + prescriptionBean.getSexName());
        textAge.setText("年龄：" + prescriptionBean.getPatientAge());
        textDepartment.setText("科室：" + prescriptionBean.getDeptName());
        textResult.setText("诊断结果：" + prescriptionBean.getDiagDesc());
        //进行转换
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

    //从我的处方详情中过来进行赋值
    private void setPrescriptionDetailBean() {

        textName.setText("姓名：" + prescriptionDetailBean.getPatientName());
        textSex.setText("性别：" + prescriptionDetailBean.getSexName());
        textAge.setText("年龄：" + prescriptionDetailBean.getPatientAge());
        textDepartment.setText("科室：" + prescriptionDetailBean.getDeptName());
        textResult.setText("诊断结果：" + prescriptionDetailBean.getDiagDesc());

        //进行转换
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
        tvAddChange.setText("编辑药材");
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
            //存为常用方
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

            //医嘱
            case R.id.relativeLayoutMedicalAdvice:
                Intent intent22 = new Intent(EditChineseMedicinePrescriptionActivity.this,
                        ChineseMedicineMedicalAdviceActivity.class);
                intent22.putExtra("oldData", textViewMedicalAdvice.getText().toString());
                startActivityForResult(intent22, requestCodeAdvice);
                break;

            //编辑药材
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

            //选择频次
            case R.id.textViewChooseDict:
                if (mFrequency.size() == 0) {
                    ToastUtil.showToast("用法频次暂时没有信息");
                    return;
                }

                showFrequencyDialog();

//                chooseDictDialog = new ChooseDictDialog(EditChineseMedicinePrescriptionActivity.this, "请选择用药频次", mFrequency);
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
            //生成处方
            case R.id.buttonSubmit:

                String medicationRequirements = textViewMedicationRequirements.getText().toString().trim();

                String total = editTextTotal.getText().toString().trim();
                String sub = editTextSub.getText().toString().trim();

                if (chooseListResult.size() <= 0) {
                    ToastUtil.showToast("请编辑药材");
                    return;
                }

                if (TextUtils.isEmpty(total) || total.equals("0")) {
                    ToastUtil.showToast("总量不能为空或为零");
                    return;
                }

                if (TextUtils.isEmpty(sub) || sub.equals("0")) {
                    ToastUtil.showToast("每次用量不能为空或为零");
                    return;
                }

                if (Double.parseDouble(sub) > Double.parseDouble(total)) {
                    ToastUtil.showToast("每次用量不能超过总量");
                    return;
                }

                String medicalAdvice = textViewMedicalAdvice.getText().toString().trim();
                if (medicalAdvice.isEmpty() || medicalAdvice.equals("必填")) {
                    ToastUtil.showToast("请填写医嘱");
                    return;
                }

                String frequency = textViewChooseDict.getText().toString().trim();
                if (TextUtils.isEmpty(frequency)) {
                    ToastUtil.showToast("请选择用药频次");
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

    //用药频次
    private void showFrequencyDialog() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            String name = mFrequency.get(options1).getDictLabel();
            textViewChooseDict.setText(name);
        })
                .setTitleText("请选择用药频次")
                .setCancelText(" ")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(16)
                .build();
        pvOptions.setPicker(mFrequency);
        pvOptions.show();
    }

    //存为常用方
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
                    tvAddChange.setText("编辑药材");
                    recyclerView.setVisibility(View.VISIBLE);

                    mAdapterDrugs.setList(chooseList);
                    buttonSavePrescription.setVisibility(View.VISIBLE);

                    chooseListResult.clear();
                    chooseListResult.addAll(chooseList);
                    break;

                //关闭300
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
        textDepartment.setText("科室：" + bean.getDeptName());
        //进行转换
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
        tvAddChange.setText("编辑药材");
        recyclerView.setVisibility(View.VISIBLE);
        mAdapterDrugs.setList(chooseListResult);
        buttonSavePrescription.setVisibility(View.VISIBLE);

    }

    @Override
    public void addPrescriptionSuccess(Integer prescriptionId) {
        this.mPrescriptionId = prescriptionId;
        ToastUtil.showToast("生成处方成功");
        // TODO: 2020/12/29  后期修改，只认证一次，下次直接进
        //1、运营商3要素，2、人脸识别
        //Integer authType;
        //认证状态，0未认证，1已认证
        //Integer authState;
        //0非意愿，1意愿
        //Integer desireState;

        Integer authType = doctorInfo.getAuthType();
        Integer authState = doctorInfo.getAuthState();
        Integer desireState = doctorInfo.getDesireState();
        MMKV.defaultMMKV().putString("prescriptionType", "中药");
        //表示没有做过认证
        if (authType == null || authState == null || authState == 0) {
            goNewVerify(prescriptionId);
            return;
        }

        //已经认证成功过一次，但还没有选择意愿
        if (authState == 1) {
            //表示还没有选择意愿，进入到选择意愿中
            if (desireState == null) {
                Intent intent = new Intent(EditChineseMedicinePrescriptionActivity.this, AspirationVerifiedChooseActivity.class);
                intent.putExtra("prescriptionId", prescriptionId);
//                intent.putExtra("perscriptionTypeId", "2");
                //认证方式
                intent.putExtra("authentication", authType);
                startActivityForResult(intent, 300);
                return;
            }

            //直接进入签名环节
            Intent intent = new Intent(EditChineseMedicinePrescriptionActivity.this, PrescriptionSignActivity.class);
            intent.putExtra("prescriptionId", prescriptionId);
//            intent.putExtra("perscriptionTypeId", "2");
            intent.putExtra("authentication", authType);
            //选择0非意愿
            if (desireState == 0) {
                intent.putExtra("type", 1);
            } else if (desireState == 1) {
                //选择1意愿
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
        ToastUtil.showToast("添加常用方成功");
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