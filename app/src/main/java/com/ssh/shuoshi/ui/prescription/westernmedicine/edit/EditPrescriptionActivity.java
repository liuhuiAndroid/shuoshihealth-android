package com.ssh.shuoshi.ui.prescription.westernmedicine.edit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.common.SystemTypeBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDetailDtosBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.bean.prescription.PrescriptionStateBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.ChineseTemplateSaveDialog;
import com.ssh.shuoshi.ui.dialog.ChooseDictDialog;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.prescription.westernmedicine.add.NewDrugsActivity;
import com.ssh.shuoshi.ui.prescription.westernmedicine.commonlyprescription.CommonlyPrescriptionActivity;
import com.ssh.shuoshi.ui.prescription.westernmedicine.history.HistoryPrescriptionActivity;
import com.ssh.shuoshi.ui.verified.choose.AspirationVerifiedChooseActivity;
import com.ssh.shuoshi.ui.verified.sign.PrescriptionSignActivity;
import com.ssh.shuoshi.ui.verified.start.VerifiedStartActivity;
import com.ssh.shuoshi.view.pickview.builder.OptionsPickerBuilder;
import com.ssh.shuoshi.view.pickview.view.OptionsPickerView;
import com.ssh.shuoshi.view.title.UniteTitle;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * ??????????????????
 */
public class EditPrescriptionActivity extends BaseActivity implements EditPrescriptionContract.View,
        View.OnClickListener {

    @Inject
    EditPrescriptionPresenter mPresenter;
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

    @BindView(R.id.textViewPatientTitle)
    TextView textViewPatientTitle;
    @BindView(R.id.buttonSaveFormwork)
    Button buttonSaveFormwork;
    @BindView(R.id.buttonHistoryPrescription)
    Button buttonHistoryPrescription;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.viewLine01)
    View viewLine01;
    @BindView(R.id.relativeLayoutAddDrugs)
    RelativeLayout relativeLayoutAddDrugs;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    @Inject
    UserStorage mUserStorage;

    private EditPrescriptionAdapter mPrescriptionAdapter;
    private List<DrugBean.RowsBean> mData = new ArrayList<>();
    private int mPhamCategoryId = 1;

    //????????????
    private List<SystemTypeBean.RowsBean> mFrequency = new ArrayList<>();
    private List<SystemTypeBean.RowsBean> mDosageUnitsList = new ArrayList<>();
    private List<SystemTypeBean.RowsBean> mAdministrationRouteList = new ArrayList<>();
    private LoadingDialog mLoadingDialog;

    //?????????ID
    private Integer phamVendorId = null;
    //????????????????????????
    private boolean mIsRequest;
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
    private HisDoctorBean doctorInfo;

    @Override
    public int initContentView() {
        return R.layout.activity_edit_prescription;
    }

    @Override
    public void setStatusBar() {
        super.setStatusBar();
    }

    @Override
    protected void initInjector() {
        DaggerEditPrescriptionComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        mIsRequest = getIntent().getBooleanExtra("isRequest", false);
        doctorInfo = mUserStorage.getDoctorInfo();
        mConsultaionInfoBean = (ImageDiagnoseBean.RowsBean) getIntent().getSerializableExtra("consultaion");
        prescriptionBean = (PrescriptionStateBean.RowsBean) getIntent().getSerializableExtra("prescriptionBean");
        prescriptionDetailBean = (HisPrescriptionDtoBean) getIntent().getSerializableExtra("prescriptionDetailBean");
        uniteTitle.setBackListener(-1, v -> finish());
        initRecycleView();
        //?????????????????????????????????????????????
        if (mConsultaionInfoBean != null) {
            if (mConsultaionInfoBean.getStatus() == 1 || mConsultaionInfoBean.getStatus() == 3) {
                buttonHistoryPrescription.setVisibility(View.VISIBLE);
            } else {
                buttonHistoryPrescription.setVisibility(View.GONE);
            }

            textName.setText("?????????" + mConsultaionInfoBean.getPatientName());
            textSex.setText("?????????" + mConsultaionInfoBean.getSexName());
            textAge.setText("?????????" + mConsultaionInfoBean.getPatientAge());
            textDepartment.setText("?????????" + doctorInfo.getHisSysDeptName());
            textResult.setText(mConsultaionInfoBean.getEmrDiagDesc());
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

        if (mIsRequest) {
            if (mConsultaionInfoBean != null && mConsultaionInfoBean.getPrescriptionState() != null) {
                mPresenter.getPrescriptionFromId(mConsultaionInfoBean.getPrescriptionId());
            } else if (prescriptionBean != null) {
                mPresenter.getPrescriptionFromId(prescriptionBean.getId());
            }
        }

        //??????????????????
        mPresenter.getFrequencyList();
        //??????????????????
        mPresenter.getDosageUnits();
        //??????
        mPresenter.getAdministrationRoute();

        buttonSaveFormwork.setOnClickListener(this);
        relativeLayoutAddDrugs.setOnClickListener(this);
        buttonHistoryPrescription.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);

    }

    //??????????????????????????????????????????
    private void setPrescriptionDetailBean() {
        buttonHistoryPrescription.setVisibility(View.GONE);
        textName.setText("?????????" + prescriptionDetailBean.getPatientName());
        textSex.setText("?????????" + prescriptionDetailBean.getSexName());
        textAge.setText("?????????" + prescriptionDetailBean.getPatientAge());
        textDepartment.setText("?????????" + prescriptionDetailBean.getDeptName());
        textResult.setText(prescriptionDetailBean.getDiagDesc());

//        textNo.setText(prescriptionDetailBean.getPerscriptionNo());
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

    //??????????????????????????????????????????
    private void setPrescriptionBean() {
        buttonHistoryPrescription.setVisibility(View.GONE);
        textName.setText("?????????" + prescriptionBean.getPatientName());
        textSex.setText("?????????" + prescriptionBean.getSexName());
        textAge.setText("?????????" + prescriptionBean.getPatientAge());
        textDepartment.setText("?????????" + prescriptionBean.getDeptName());
        textResult.setText(prescriptionBean.getDiagDesc());
//        textNo.setText(prescriptionBean.getPerscriptionNo());
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

    private void setDetailBena(List<HisPrescriptionDetailDtosBean> detailDtos) {
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
        if (mData.size() != 0) {
            buttonSaveFormwork.setText("??????????????????");
        }

        buttonSubmit.setEnabled(true);

        if (mData.size() >= 5) {
            relativeLayoutAddDrugs.setVisibility(View.GONE);
        }

        mPrescriptionAdapter.notifyDataSetChanged();
    }

    @SuppressLint("WrongConstant")
    private void initRecycleView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        mPrescriptionAdapter = new EditPrescriptionAdapter(this, mData);
        mPrescriptionAdapter.setChangeListener(new EditPrescriptionAdapter.ItemListener() {
            @Override
            public void minusNumber(int position, int type) {
                DrugBean.RowsBean bean = mData.get(position);
                if (type == 1) {
                    if (bean.getNum() > 1) {
                        bean.setNum(bean.getNum() - 1);
                    }
                } else {
                    if (bean.getNum() < 5) {
                        bean.setNum(bean.getNum() + 1);
                    }
                }
//                mPrescriptionAdapter.notifyDataSetChanged();
                mPrescriptionAdapter.notifyItemChanged(position);
            }

            @Override
            public void dictNum(int position) {
                if (mFrequency.size() == 0) {
                    ToastUtil.showToast("??????????????????????????????");
                    return;
                }
                showFrequencyDialog(position);
            }

            @Override
            public void usage(int position) {
                if (mAdministrationRouteList.size() == 0) {
                    ToastUtil.showToast("????????????????????????");
                    return;
                }
                showAdministrationDialog(position);
            }

            @Override
            public void dosageUnits(int position) {
                if (mDosageUnitsList.size() == 0) {
                    ToastUtil.showToast("??????????????????????????????");
                    return;
                }
                showDosageUnitsDialog(position);
            }

            @Override
            public void delete(int position) {
                mData.remove(position);
                mPrescriptionAdapter.notifyDataSetChanged();

                if (mData.size() == 0) {
                    buttonSaveFormwork.setText("????????????");
                    buttonSubmit.setEnabled(false);
                }
                if (mData.size() < 5) {
                    relativeLayoutAddDrugs.setVisibility(View.VISIBLE);
                }

            }
        });
        mRecyclerView.setAdapter(mPrescriptionAdapter);
    }

    //????????????
    private void showDosageUnitsDialog(int position) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            String name = mDosageUnitsList.get(options1).getDictLabel();
            DrugBean.RowsBean bean = mData.get(position);
            bean.setDosageUnits(name);
            mPrescriptionAdapter.notifyItemChanged(position);
        })
                .setTitleText("?????????????????????")
                .setCancelText(" ")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(16)
                .build();
        pvOptions.setPicker(mDosageUnitsList);
        pvOptions.show();
    }

    //??????
    private void showAdministrationDialog(int position) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            String name = mAdministrationRouteList.get(options1).getDictLabel();
            DrugBean.RowsBean bean = mData.get(position);
            bean.setAdministration(name);
            mPrescriptionAdapter.notifyItemChanged(position);
        })
                .setTitleText("?????????????????????")
                .setCancelText(" ")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(16)
                .build();
        pvOptions.setPicker(mAdministrationRouteList);
        pvOptions.show();
    }

    //????????????
    private void showFrequencyDialog(int position) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            String name = mFrequency.get(options1).getDictLabel();
            DrugBean.RowsBean bean = mData.get(position);
            bean.setFrequency(name);
            mPrescriptionAdapter.notifyItemChanged(position);
        })
                .setTitleText("?????????????????????")
                .setCancelText(" ")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(16)
                .build();
        pvOptions.setPicker(mFrequency);
        pvOptions.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativeLayoutAddDrugs:
                Intent intent = new Intent(EditPrescriptionActivity.this, NewDrugsActivity.class);
                intent.putExtra("mPhamCategoryId", mPhamCategoryId);
                intent.putExtra("type", "prescription");
                if (phamVendorId == null) {
                    intent.putExtra("phamVendorId", 0);
                } else {
                    intent.putExtra("phamVendorId", phamVendorId);
                }
                startActivityForResult(intent, 100);
                break;

            case R.id.buttonHistoryPrescription:
                // ?????????????????????
                Intent intent2 = new Intent(EditPrescriptionActivity.this, HistoryPrescriptionActivity.class);
                intent2.putExtra("infoBean", mConsultaionInfoBean);
                intent2.putExtra("mPhamCategoryId", mPhamCategoryId);
                startActivityForResult(intent2, 101);
                break;

            case R.id.buttonSaveFormwork:
                //?????????????????????
                if (mData.size() == 0) {
                    Intent intent3 = new Intent(EditPrescriptionActivity.this, CommonlyPrescriptionActivity.class);
                    startActivityForResult(intent3, 101);
                } else {
                    for (int i = 0; i < mData.size(); i++) {
                        if (mData.get(i).getDosage().equals("0")) {
                            ToastUtil.showToast("?????????????????????????????????0");
                            return;
                        }

                        if (TextUtils.isEmpty(mData.get(i).getDosageUnits())) {
                            ToastUtil.showToast("??????????????????????????????");
                            return;
                        }
                    }

                    ChineseTemplateSaveDialog saveDialog = new ChineseTemplateSaveDialog(EditPrescriptionActivity.this, R.style.dialog_physician_certification);
                    saveDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    saveDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                    //???????????????????????????
                                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                    //??????
                                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                                    //???????????????
                                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                            uiOptions |= 0x00001000;
                            saveDialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
                        }
                    });
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
                }
                break;

            case R.id.buttonSubmit:
                checkData();
                break;
        }
    }

    //?????????
    private void saveCommonTemplate(String name) {
        //??????????????????   ???????????????????????????
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("templateName", name);
        mMap.put("perscriptionTypeId", mPhamCategoryId);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("phamId", mData.get(i).getId());
            map.put("amount", mData.get(i).getNum());
            map.put("dosage", mData.get(i).getDosage());
            map.put("dosageUnits", mData.get(i).getDosageUnits());
            map.put("frequency", mData.get(i).getFrequency());
            map.put("administration", mData.get(i).getAdministration());
            list.add(map);
        }
        mMap.put("hisPrescriptionTemplateDetailDtos", list);
        showLoading();
        mPresenter.addPrescriptionTemplate(mMap);
    }

    //??????????????????
    private void checkData() {
        if (mData != null && mData.size() == 0) {
            ToastUtil.showToast("????????????????????????");
            return;
        }

        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getDosage().equals("0")) {
                ToastUtil.showToast("?????????????????????????????????0");
                return;
            }

            if (TextUtils.isEmpty(mData.get(i).getDosageUnits())) {
                ToastUtil.showToast("??????????????????????????????");
                return;
            }

            if (TextUtils.isEmpty(mData.get(i).getFrequency())) {
                ToastUtil.showToast("??????????????????");
                return;
            }
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

        mMap.put("perscriptionTypeId", mPhamCategoryId);

        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("phamId", mData.get(i).getId());
            map.put("amount", mData.get(i).getNum());
            map.put("dosage", mData.get(i).getDosage());
            map.put("dosageUnits", mData.get(i).getDosageUnits());
            map.put("frequency", mData.get(i).getFrequency());
            map.put("administration", mData.get(i).getAdministration());
            list.add(map);
        }
        mMap.put("hisPrescriptionDetailDtos", list);
        showLoading();
        if (isFirst) {
            mPresenter.addPrescription(mMap);
        } else {
            mMap.put("id", beanId);
            mPresenter.putPrescription(mMap);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    DrugBean.RowsBean rowsBean = (DrugBean.RowsBean) data.getSerializableExtra("rowsBean");
                    phamVendorId = rowsBean.getPhamVendorId();
                    for (int i = 0; i < mData.size(); i++) {
                        //??????????????????????????????
                        if (rowsBean.getId() == mData.get(i).getId()) {
                            if (mData.get(i).getNum() < 5) {
                                mData.get(i).setNum(mData.get(i).getNum() + 1);
                                mPrescriptionAdapter.notifyDataSetChanged();
                            }
                            return;
                        }
                    }
                    rowsBean.setNum(1);
                    if (mFrequency.size() != 0) {
                        rowsBean.setFrequency(mFrequency.get(0).getDictLabel());
                    }
                    rowsBean.setDosage("1");

                    String dosageUnit = rowsBean.getDosageUnit();
                    if (!TextUtils.isEmpty(dosageUnit) && mDosageUnitsList.size() != 0) {
                        for (int i = 0; i < mDosageUnitsList.size(); i++) {
                            if (dosageUnit.equals(mDosageUnitsList.get(i).getDictLabel())) {
                                rowsBean.setDosageUnits(dosageUnit);
                            }
                        }
                    }

                    if (mAdministrationRouteList.size() != 0) {
                        rowsBean.setAdministration(mAdministrationRouteList.get(0).getDictLabel());
                    }
                    mData.add(rowsBean);
                    mPrescriptionAdapter.notifyDataSetChanged();
                    if (mData.size() >= 5) {
                        relativeLayoutAddDrugs.setVisibility(View.GONE);
                    }
                    buttonSaveFormwork.setText("??????????????????");
                    buttonSubmit.setEnabled(true);
                    break;

                case 101:
                    DrugBean drugBean = (DrugBean) data.getSerializableExtra("drugBean");
                    mData.clear();
                    mData.addAll(drugBean.getRows());
                    mPrescriptionAdapter.notifyDataSetChanged();
                    buttonSaveFormwork.setText("??????????????????");
                    buttonSubmit.setEnabled(true);
                    break;

                //??????300
                case 300:
                    Intent intent = new Intent();
                    intent.putExtra("prescriptionId", mPrescriptionId);
                    intent.putExtra("data", new Gson().toJson(mPrescriptionAdapter.getmData()));
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        }
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(EditPrescriptionActivity.this, "");
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

    // TODO: 2020/12/20 ?????????????????????????????????
    @Override
    public void addPrescriptionSuccess(Map<String, Object> map, Integer prescriptionId) {
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
                Intent intent = new Intent(EditPrescriptionActivity.this, AspirationVerifiedChooseActivity.class);
                intent.putExtra("prescriptionId", prescriptionId);
                //????????????
                intent.putExtra("authentication", authType);
                startActivityForResult(intent, 300);
                return;
            }

            //????????????????????????
            Intent intent = new Intent(EditPrescriptionActivity.this, PrescriptionSignActivity.class);
            intent.putExtra("prescriptionId", prescriptionId);
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

    //??????????????????
    private void goNewVerify(Integer prescriptionId) {
        Intent intent = new Intent(EditPrescriptionActivity.this, VerifiedStartActivity.class);
        intent.putExtra("prescriptionId", prescriptionId);
        startActivityForResult(intent, 300);
    }

    @Override
    public void addPrescriptionTemplateSuccess() {
        ToastUtil.showToast("????????????????????????");
    }

    @Override
    public void getFrequencyList(SystemTypeBean bean) {
        this.mFrequency = bean.getRows();
    }

    @Override
    public void getDosageUnitsList(SystemTypeBean bean) {
        this.mDosageUnitsList = bean.getRows();
    }

    @Override
    public void getAdministrationRouteList(SystemTypeBean bean) {
        this.mAdministrationRouteList = bean.getRows();
    }

    //??????ID???????????????????????????????????????
    @Override
    public void getPrescriptionFromIdSuccess(HisPrescriptionDtoBean bean) {
        beanId = bean.getId();
        isFirst = false;
        textNo.setText(bean.getPerscriptionNo());
        textDepartment.setText("?????????" + bean.getDeptName());
        //????????????
        List<HisPrescriptionDetailDtosBean> detailDtos = bean.getHisPrescriptionDetailDtos();
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
        if (mData.size() != 0) {
            buttonSaveFormwork.setText("??????????????????");
        }

        buttonSubmit.setEnabled(true);

        if (mData.size() >= 5) {
            relativeLayoutAddDrugs.setVisibility(View.GONE);
        }

        mPrescriptionAdapter.notifyDataSetChanged();

        if (bean.getDoctorId() != doctorInfo.getId()) {
            buttonSubmit.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}
