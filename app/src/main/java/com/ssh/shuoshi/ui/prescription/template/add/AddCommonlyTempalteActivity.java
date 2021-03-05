package com.ssh.shuoshi.ui.prescription.template.add;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.bean.common.SystemTypeBean;
import com.ssh.shuoshi.bean.template.HisPrescriptionTemplateDetailDtosBean;
import com.ssh.shuoshi.bean.template.PrescriptionTemplateBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.ChooseDictDialog;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.prescription.westernmedicine.add.NewDrugsActivity;
import com.ssh.shuoshi.ui.prescription.westernmedicine.edit.EditPrescriptionAdapter;
import com.ssh.shuoshi.view.pickview.builder.OptionsPickerBuilder;
import com.ssh.shuoshi.view.pickview.view.OptionsPickerView;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * 新增常用处方
 */
public class AddCommonlyTempalteActivity extends BaseActivity implements AddCommonlyTempalteContract.View, View.OnClickListener {

    @Inject
    AddCommonlyTempaltePresenter mPresenter;
    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.editTextResult)
    EditText editTextResult;
    @BindView(R.id.textViewPatientTitle)
    TextView textViewPatientTitle;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.relativeLayoutAddDrugs)
    RelativeLayout relativeLayoutAddDrugs;
    @BindView(R.id.buttonSave)
    Button buttonSave;

    @Inject
    UserStorage mUserStorage;
    //处方类别ID   1 西医 2 中医
    private int mPhamCategoryId = 1;

    private List<DrugBean.RowsBean> mData = new ArrayList<>();
    private EditPrescriptionAdapter mPrescriptionAdapter;
    private LoadingDialog mLoadingDialog;
    private PrescriptionTemplateBean.RowsBean mBean;
    private String mType;

    //用药频次
    private List<SystemTypeBean.RowsBean> mFrequency = new ArrayList<>();
    private List<SystemTypeBean.RowsBean> mDosageUnitsList = new ArrayList<>();
    private List<SystemTypeBean.RowsBean> mAdministrationRouteList = new ArrayList<>();
    private ChooseDictDialog chooseFrequencyDialog;
    private ChooseDictDialog chooseDosageUnitsList;
    private ChooseDictDialog chooseAdministrationRouteList;

    @Override
    public int initContentView() {
        return R.layout.activity_add_commonly_template;
    }

    @Override
    protected void initInjector() {
        DaggerAddCommonlyTempalteComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        String deptType = mUserStorage.getDoctorInfo().getDeptType();
        if (deptType.equals("西医")) {
            mPhamCategoryId = 1;
        } else {
            mPhamCategoryId = 2;
        }
        initRecycleView();
        uniteTitle.setBackListener(-1, v -> finish());
        //用药频次列表
        mPresenter.getFrequencyList();
        //单次计量单位
        mPresenter.getDosageUnits();
        //用法
        mPresenter.getAdministrationRoute();

        Intent intent = getIntent();
        int preId = intent.getIntExtra("mPhamCategoryId", -1);
        if (preId != -1) {
            mPhamCategoryId = preId;
        }
        mBean = (PrescriptionTemplateBean.RowsBean) intent.getSerializableExtra("template");
        mType = intent.getStringExtra("type");

        if (mBean != null) {
            fillData();
        }

        relativeLayoutAddDrugs.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
    }

    //从详情页进来
    private void fillData() {
        editTextResult.setText(mBean.getTemplateName());
        //赋值adapter
        List<HisPrescriptionTemplateDetailDtosBean> detailDtos = mBean.getHisPrescriptionTemplateDetailDtos();
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
            rowsBean.setTemplateId(detailDtos.get(i).getId());
            mData.add(rowsBean);
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
                mPrescriptionAdapter.notifyDataSetChanged();
            }

            @Override
            public void dictNum(int position) {
                if (mFrequency.size() == 0) {
                    ToastUtil.showToast("用药频次暂时没有信息");
                    return;
                }

                showFrequencyDialog(position);

//                chooseFrequencyDialog = new ChooseDictDialog(AddCommonlyTempalteActivity.this, "请选择用药频次", mFrequency);
//                chooseFrequencyDialog.setCanceledOnTouchOutside(false);
//                chooseFrequencyDialog.setOnItemClickListener(new ChooseDictDialog.ItemClickListener() {
//                    @Override
//                    public void cancel() {
//                        chooseFrequencyDialog.dismiss();
//                    }
//
//                    @Override
//                    public void choose(String name) {
//                        DrugBean.RowsBean bean = mData.get(position);
//                        bean.setFrequency(name);
//                        mPrescriptionAdapter.notifyDataSetChanged();
//                        chooseFrequencyDialog.dismiss();
//                    }
//                });
//                chooseFrequencyDialog.show();
            }

            @Override
            public void usage(int position) {
                if (mAdministrationRouteList.size() == 0) {
                    ToastUtil.showToast("用法暂时没有信息");
                    return;
                }

                showAdministrationDialog(position);

//                chooseAdministrationRouteList = new ChooseDictDialog(AddCommonlyTempalteActivity.this, "请选择用药方法", mAdministrationRouteList);
//                chooseAdministrationRouteList.setCanceledOnTouchOutside(false);
//                chooseAdministrationRouteList.setOnItemClickListener(new ChooseDictDialog.ItemClickListener() {
//                    @Override
//                    public void cancel() {
//                        chooseAdministrationRouteList.dismiss();
//                    }
//
//                    @Override
//                    public void choose(String name) {
//                        DrugBean.RowsBean bean = mData.get(position);
//                        bean.setAdministration(name);
//                        mPrescriptionAdapter.notifyDataSetChanged();
//                        chooseAdministrationRouteList.dismiss();
//                    }
//                });
//                chooseAdministrationRouteList.show();
            }

            @Override
            public void dosageUnits(int position) {
                if (mDosageUnitsList.size() == 0) {
                    ToastUtil.showToast("计量单位暂时没有信息");
                    return;
                }
                showDosageUnitsDialog(position);

//                chooseDosageUnitsList = new ChooseDictDialog(AddCommonlyTempalteActivity.this, "请选择计量单位", mDosageUnitsList);
//                chooseDosageUnitsList.setCanceledOnTouchOutside(false);
//                chooseDosageUnitsList.setOnItemClickListener(new ChooseDictDialog.ItemClickListener() {
//                    @Override
//                    public void cancel() {
//                        chooseDosageUnitsList.dismiss();
//                    }
//
//                    @Override
//                    public void choose(String name) {
//                        DrugBean.RowsBean bean = mData.get(position);
//                        bean.setDosageUnits(name);
//                        mPrescriptionAdapter.notifyDataSetChanged();
//                        chooseDosageUnitsList.dismiss();
//                    }
//                });
//                chooseDosageUnitsList.show();
            }

            @Override
            public void delete(int position) {
                mData.remove(position);
                mPrescriptionAdapter.notifyDataSetChanged();
                if (mData.size() < 5) {
                    relativeLayoutAddDrugs.setVisibility(View.VISIBLE);
                }
            }
        });
        mRecyclerView.setAdapter(mPrescriptionAdapter);
    }

    //计量单位
    private void showDosageUnitsDialog(int position) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            String name = mDosageUnitsList.get(options1).getDictLabel();
            DrugBean.RowsBean bean = mData.get(position);
            bean.setDosageUnits(name);
            mPrescriptionAdapter.notifyItemChanged(position);
        })
                .setTitleText("请选择计量单位")
                .setCancelText(" ")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(mDosageUnitsList);
        pvOptions.show();
    }

    //用法
    private void showAdministrationDialog(int position) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            String name = mAdministrationRouteList.get(options1).getDictLabel();
            DrugBean.RowsBean bean = mData.get(position);
            bean.setAdministration(name);
            mPrescriptionAdapter.notifyItemChanged(position);
        })
                .setTitleText("请选择用药方法")
                .setCancelText(" ")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(mAdministrationRouteList);
        pvOptions.show();
    }

    //用药频次
    private void showFrequencyDialog(int position) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            String name = mFrequency.get(options1).getDictLabel();
            DrugBean.RowsBean bean = mData.get(position);
            bean.setFrequency(name);
            mPrescriptionAdapter.notifyItemChanged(position);
        })
                .setTitleText("请选择用药频次")
                .setCancelText(" ")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(mFrequency);
        pvOptions.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativeLayoutAddDrugs:
                Intent intent = new Intent(AddCommonlyTempalteActivity.this, NewDrugsActivity.class);
                intent.putExtra("mPhamCategoryId", mPhamCategoryId);
                intent.putExtra("type", "template");
                startActivityForResult(intent, 100);
                break;


            case R.id.buttonSave:
                checkData();
                break;
        }
    }

    private void checkData() {
        String result = editTextResult.getText().toString();
        if (TextUtils.isEmpty(result)) {
            ToastUtil.showToast("请输入模版名称");
            return;
        }

        if (mData != null && mData.size() == 0) {
            ToastUtil.showToast("至少添加一个药品");
            return;
        }

        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getDosage().equals("0")) {
                ToastUtil.showToast("单次计量不能为空或者为0");
                return;
            }

            if (TextUtils.isEmpty(mData.get(i).getDosageUnits())) {
                ToastUtil.showToast("每次剂量单位不能为空");
                return;
            }
        }

        Map<String, Object> mMap = new HashMap<>();
        mMap.put("templateName", editTextResult.getText().toString());

        if (mType.equals("1")) {
            mMap.put("perscriptionTypeId", mPhamCategoryId);
        } else if (mType.equals("2") && mBean != null) {
            mMap.put("id", mBean.getId());
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("phamId", mData.get(i).getId());
            map.put("amount", mData.get(i).getNum());
            map.put("dosage", mData.get(i).getDosage());
            map.put("dosageUnits", mData.get(i).getDosageUnits());
            map.put("frequency", mData.get(i).getFrequency());
            map.put("administration", mData.get(i).getAdministration());
            //模版ID不可能为0
            if (mType.equals("2") && mBean != null) {
                if (mData.get(i).getTemplateId() != 0) {
                    map.put("id", mData.get(i).getTemplateId());
                }
            }
            list.add(map);
        }
        mMap.put("hisPrescriptionTemplateDetailDtos", list);
        showLoading();

        if (mType.equals("1")) {
            mPresenter.addPrescriptionTemplate(mMap);
        } else if (mType.equals("2") && mBean != null) {
            mPresenter.changePrescriptionTemplate(mMap);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    DrugBean.RowsBean rowsBean = (DrugBean.RowsBean) data.getSerializableExtra("rowsBean");
                    for (int i = 0; i < mData.size(); i++) {
                        //存在，数量加一
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
                    break;

            }
        }
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(AddCommonlyTempalteActivity.this, "");
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

    @Override
    public void addPrescriptionTemplateSuccess() {
        setResult(RESULT_OK, getIntent());
        finish();
    }
}
