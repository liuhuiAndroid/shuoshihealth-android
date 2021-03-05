package com.ssh.shuoshi.ui.verified.submit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDetailDtosBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionTcmDetailsBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.view.title.UniteTitle;
import com.tencent.mmkv.MMKV;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ssh.shuoshi.util.PhotoUtils.isBase64Img;

/**
 * 处方提交审核
 * created by hwt on 2020/12/27
 */
public class PrescriptionSubmitCheckActivity extends BaseActivity implements
        PrescriptionSubmitCheckContract.View, View.OnClickListener {

    @Inject
    PrescriptionSubmitCheckPresenter mPresenter;
    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.textPrescriptionType)
    TextView textPrescriptionType;
    @BindView(R.id.textNo)
    TextView textNo;
    @BindView(R.id.textName)
    TextView textName;
    @BindView(R.id.textSex)
    TextView textSex;
    @BindView(R.id.textAge)
    TextView textAge;
    @BindView(R.id.textOffice)
    TextView textOffice;
    @BindView(R.id.textDate)
    TextView textDate;
    @BindView(R.id.textMedicalNo)
    TextView textMedicalNo;
    @BindView(R.id.textDiagnose)
    TextView textDiagnose;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.textRp)
    TextView textRp;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.textHint)
    TextView textHint;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.textPhysician)
    TextView textPhysician;
    @BindView(R.id.imgPhysician)
    ImageView imgPhysician;
    @BindView(R.id.textRemark)
    TextView textRemark;
    @BindView(R.id.view4)
    View view4;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;

    @Inject
    UserStorage mUserStorage;
    @BindView(R.id.textUse)
    TextView textUse;
    @BindView(R.id.textUseDesc)
    TextView textUseDesc;
    @BindView(R.id.textMethod)
    TextView textMethod;
    @BindView(R.id.textMethodDesc)
    TextView textMethodDesc;
    @BindView(R.id.textAdvice)
    TextView textAdvice;
    @BindView(R.id.rl_chinese)
    RelativeLayout rlChinese;

    private LoadingDialog mLoadingDialog;
    //获取处方的数据,处方ID
    private int prescriptionId;
    //展示西，中界面
//    private String perscriptionTypeId;
    //处方、处方订单详细信息
    private HisPrescriptionDtoBean mBean;

    //展示西，中界面
    private String prescriptionType;

    @Override
    public int initContentView() {
        return R.layout.activity_prescription_submit_check;
    }

    @Override
    protected void initInjector() {
        DaggerPrescriptionSubmitCheckComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        uniteTitle.setBackListener(-1, v -> finish());

        prescriptionType = MMKV.defaultMMKV().getString("prescriptionType", "");

        Intent intent = getIntent();
//        perscriptionTypeId = intent.getStringExtra("perscriptionTypeId");
        mBean = (HisPrescriptionDtoBean) intent.getSerializableExtra("historyDrugsBean");
        prescriptionId = intent.getIntExtra("prescriptionId", -1);

        HisDoctorBean doctorInfo = mUserStorage.getDoctorInfo();
        String signImg = doctorInfo.getSignImg();
        if (!TextUtils.isEmpty(signImg)) {
            //如果不为空，则取出来
            String[] photoPath = new String[]{signImg};
            mPresenter.getImagePath(photoPath);
        }

        if (mBean != null){
            initData();
        } else {
            mPresenter.getPrescriptionFromId(prescriptionId);
        }


        buttonSubmit.setOnClickListener(this);
    }

    @SuppressLint("WrongConstant")
    private void initData() {
//        if (perscriptionTypeId.equals("1")) {
        if (prescriptionType.equals("西药")) {
            textPrescriptionType.setText("硕世互联网医院 处方笺");
        } else {
            textPrescriptionType.setText("硕世互联网医院 处方笺（中医）");
        }

        String date = mBean.getVisitDate();
        if (!TextUtils.isEmpty(date) && date.length() >= 10) {
            date = date.substring(0, 10);
        }
        textNo.setText("No：" + mBean.getPerscriptionNo());
        textName.setText("姓名：" + mBean.getPatientName());
        textSex.setText("性别：" + mBean.getSexName());
        textAge.setText("年龄：" + mBean.getPatientAge() + "岁");
        textOffice.setText("科室：" + mBean.getDeptName());
        textDate.setText("日期：" + date);
        textMedicalNo.setText("门诊病历号：" + mBean.getConsulationNo());
        textDiagnose.setText("诊断：" + mBean.getDiagDesc());

        //展示界面
        List<HisPrescriptionDetailDtosBean> detailDtos = mBean.getHisPrescriptionDetailDtos();
//        if (perscriptionTypeId.equals("1")) {
        if (prescriptionType.equals("西药")) {
            rlChinese.setVisibility(View.GONE);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
            CommonAdapter commonAdapter1 = new CommonAdapter<HisPrescriptionDetailDtosBean>(this, R.layout.item_prescription_west_item, detailDtos) {
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
            mRecyclerView.setAdapter(commonAdapter1);
        } else {
            rlChinese.setVisibility(View.VISIBLE);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            if (detailDtos == null || detailDtos.size() == 0) {
                return;
            }
            HisPrescriptionDetailDtosBean detailDtosBean = detailDtos.get(0);
            List<HisPrescriptionTcmDetailsBean> tcmDetails = detailDtosBean.getHisPrescriptionTcmDetails();
            CommonAdapter commonAdapter2 = new CommonAdapter<HisPrescriptionTcmDetailsBean>(this, R.layout.item_prescription_chinese_item, tcmDetails) {
                @Override
                protected void convert(ViewHolder holder, final HisPrescriptionTcmDetailsBean bean, final int pos) {
                    TextView textName = holder.getView(R.id.textName);
                    textName.setText(bean.getPhamName() + " " + bean.getAmount() + bean.getUnits());
                }
            };
            mRecyclerView.setAdapter(commonAdapter2);

            textUseDesc.setText("共" + detailDtosBean.getAmount() + detailDtosBean.getDosageUnits() +
                    ", 每次" + detailDtosBean.getDosage() + detailDtosBean.getDosageUnits() + ", " +
                    detailDtosBean.getFrequency() + "," + detailDtosBean.getAdministration());
            textMethodDesc.setText(detailDtosBean.getPhamName());
            textAdvice.setText("医嘱: " + detailDtosBean.getFreqDetail());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSubmit:

//                MMKV.defaultMMKV().putString("prescriptionBean", new Gson().toJson(mBean));
//                Intent intent = new Intent();
//                intent.putExtra("prescriptionId", prescriptionId);
//                setResult(RESULT_OK, intent);
//                finish();

                //提交审核
                showLoading();
                mPresenter.changePrescriptionState(prescriptionId);
                break;
        }
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(PrescriptionSubmitCheckActivity.this, "");
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
        // TODO: 2020/12/31 这边需要做处理，防止状态不需修改
        loadError(throwable);
//        mPresenter.getDoctorInfo();
    }

    @Override
    public void imgDownload(List<String> bean) {
        if (bean == null || bean.size() == 0) {
            return;
        }
        String mUrl = bean.get(0).split(",")[1];
        if (isBase64Img(mUrl)) {
            mUrl = mUrl.split(",")[1];
        }
        byte[] decode = Base64.decode(mUrl.getBytes(), Base64.DEFAULT);
        Bitmap mBitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        imgPhysician.setImageBitmap(mBitmap);
    }

    @Override
    public void changePrescriptionStateSuccess(Integer bean) {
        ToastUtil.showToast("提交审核成功");
        mPresenter.getDoctorInfo();
    }

    @Override
    public void setDoctorInfoSuccess() {
        mPresenter.getPrescriptionFromIdForFinish(prescriptionId);
    }


    @Override
    public void getPrescriptionFromIdSuccessForFinish(HisPrescriptionDtoBean bean) {
        // TODO: 2020/12/30  需要返回聊天界面
        MMKV.defaultMMKV().putString("prescriptionBean", new Gson().toJson(bean));
        MMKV.defaultMMKV().putString("prescriptionType", prescriptionType);
        Intent intent = new Intent();
        intent.putExtra("prescriptionId", prescriptionId);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void getPrescriptionFromIdSuccess(HisPrescriptionDtoBean bean) {
        this.mBean = bean;
        initData();
    }

}
