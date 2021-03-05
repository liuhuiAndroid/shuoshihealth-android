package com.ssh.shuoshi.ui.verified.sign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.VerifiedBean;
import com.ssh.shuoshi.bean.ca.CAPhoneBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDetailDtosBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionTcmDetailsBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.CommonDialog;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.verified.freehand.FreeHandActivity;
import com.ssh.shuoshi.ui.verified.h5.H5SecondActivity;
import com.ssh.shuoshi.ui.verified.phoneaspiration.PhoneAspirationActivity;
import com.ssh.shuoshi.ui.verified.submit.PrescriptionSubmitCheckActivity;
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
import butterknife.ButterKnife;

import static com.ssh.shuoshi.util.PhotoUtils.isBase64Img;

/**
 * 电子处方签名
 * created by hwt on 2020/12/26
 */
public class PrescriptionSignActivity extends BaseActivity implements PrescriptionSignContract.View,
        View.OnClickListener {
    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
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
    @BindView(R.id.imgSignature)
    ImageView imgSignature;
    @BindView(R.id.textSignature)
    TextView textSignature;
    @BindView(R.id.imgSignaturePhoto)
    ImageView imgSignaturePhoto;
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

    //是否有意愿， 1 没有 2 有
    private int mType;

    @Inject
    PrescriptionSignPresenter mPresenter;

    private LoadingDialog mLoadingDialog;
    //获取处方的数据,处方ID
    private int prescriptionId;
    private String doctorName;
    private String mPhotoPath;        //从签名版返回的数据
    private String mUrl;              //从服务器返回的数据
    private String accountId;
    private Bitmap mBitmap;
    private HisPrescriptionDtoBean mBean;  //获取到的处方数据
    //认证方式   1是手机，2是人脸
    private int authentication;

    private String[] ZHIMA = new String[]{"FACE_ZHIMA_XY", "FACE_FACE_LIVENESS_RECOGNITION"};
    private String[] TENCENT = new String[]{"FACE_TECENT_CLOUD_H5", "FACE_FACE_LIVENESS_RECOGNITION"};
    private HisDoctorBean doctorInfo;
    private String shortUrl;
    private String bizId;
    //展示西，中界面
    private String prescriptionType;

    //从医生信息中获取的  1、运营商3要素，2、人脸识别
    private Integer authType;
    //从医生信息中获取的 认证状态，0未认证，1已认证
    private Integer authState;
    //从医生信息中获取的 意愿状态 0非意愿，1意愿
    private Integer desireState;
    //医生表的身份证号
    private String mIdCard;
    //医生表的认证手机号
    private String authPhone;


    @Override
    public int initContentView() {
        return R.layout.activity_prescription_sign;
    }

    @Override
    protected void initInjector() {
        DaggerPrescriptionSignComponent.builder()
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

        prescriptionType = MMKV.defaultMMKV().getString("prescriptionType", "");

        doctorInfo = mUserStorage.getDoctorInfo();

        authType = doctorInfo.getAuthType();
        authState = doctorInfo.getAuthState();
        desireState = doctorInfo.getDesireState();
        accountId = doctorInfo.getAccountId();
        String signImg = doctorInfo.getSignImg();
        if (!TextUtils.isEmpty(signImg)) {
            //如果有过签名，则取出来
            String[] photoPath = new String[]{signImg};
            mPresenter.getImagePath(photoPath);
        }

        Intent intent = getIntent();
        mType = intent.getIntExtra("type", -1);
        prescriptionId = intent.getIntExtra("prescriptionId", -1);
        authentication = intent.getIntExtra("authentication", -1);

        if (authentication == -1) {
            authentication = authType;
        }

        mIdCard = doctorInfo.getIdCard();
        authPhone = doctorInfo.getAuthPhone();
        doctorName = doctorInfo.getName();

        if (mType == -1) {
            mType = desireState;
        }

        imgSignature.setOnClickListener(this);
        textSignature.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);
        imgSignaturePhoto.setOnClickListener(this);

        mPresenter.getPrescriptionFromId(prescriptionId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgSignature:
            case R.id.textSignature:
                if (imgSignaturePhoto.getDrawable() != null) {
                    CommonDialog commonDialog = new CommonDialog(PrescriptionSignActivity.this, "是否替换已有签名？", R.style.dialog_physician_certification);
                    commonDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    commonDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                    //布局位于状态栏下方
                                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                    //全屏
                                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                                    //隐藏导航栏
                                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                            uiOptions |= 0x00001000;
                            commonDialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
                        }
                    });
                    commonDialog.setOnItemClickListener(new CommonDialog.ItemClickListener() {
                        @Override
                        public void cancel() {
                            commonDialog.dismiss();
                        }

                        @Override
                        public void save() {
                            Intent intent = new Intent(PrescriptionSignActivity.this, FreeHandActivity.class);
                            startActivityForResult(intent, 100);
                            commonDialog.dismiss();
                        }
                    });
                    commonDialog.show();
                    return;
                }

                //跳转手绘签名
                Intent intent = new Intent(PrescriptionSignActivity.this, FreeHandActivity.class);
                startActivityForResult(intent, 100);
                break;

            case R.id.imgSignaturePhoto:
                if (imgSignaturePhoto.getDrawable() == null) {
                    return;
                }

                if (!TextUtils.isEmpty(mPhotoPath)) {
                    Glide.with(this).load(mPhotoPath).skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE).into(imgPhysician);
                    return;
                }

                if (!TextUtils.isEmpty(mUrl) && mBitmap != null) {
                    Glide.with(this).load(mBitmap).skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE).into(imgPhysician);
                }

                break;

            case R.id.buttonSubmit:
                if (imgSignaturePhoto.getDrawable() == null) {
                    ToastUtil.showToast("请先签名");
                    return;
                }

                if (imgPhysician.getDrawable() == null) {
                    ToastUtil.showToast("请先点击签名");
                    return;
                }

                showLoading();
                if (TextUtils.isEmpty(mPhotoPath) && !TextUtils.isEmpty(mUrl)) {
                    noUploadPhoto();
                } else {
                    mPresenter.caImgUpload(mPhotoPath);
                }

                break;
        }
    }

    //不用上传图片
    private void noUploadPhoto() {
        //手机认证
        if (authentication == 1) {
            //是否有意愿
            if (mType == 1) {
                showLoading();
                Map<String, Object> map = new HashMap<>();
                map.put("accountId", accountId);
                map.put("prescriptionId", prescriptionId);
                mPresenter.caSignPDFNone(map);
            } else {
                hideLoading();
                startToSignActivity();
            }
        } else {
            //人脸验证
            //是否有意愿

            if (mType == 1) {
                showLoading();
                Map<String, Object> map = new HashMap<>();
                map.put("accountId", accountId);
                map.put("prescriptionId", prescriptionId);
                mPresenter.caSignPDFNone(map);
            } else {
                //再次进行人脸验证
                hideLoading();
                showChooseDialog();
            }
        }
    }

    private void showChooseDialog() {
        List<VerifiedBean> list = new ArrayList<>();
        VerifiedBean vb1 = new VerifiedBean(1, "支付宝人脸识别");
        VerifiedBean vb2 = new VerifiedBean(2, "腾讯云人脸识别");
        list.add(vb1);
        list.add(vb2);

        showMethodDialog(list);
    }

    private void showMethodDialog(List<VerifiedBean> list) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            VerifiedBean verifiedBean = list.get(options1);
            showLoading();
            caFaceSecondCode(verifiedBean.getCode());
        })
                .setTitleText("请选择人脸识别方式")
                .setCancelText(" ")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(list);
        pvOptions.show();
    }


    private void caFaceSecondCode(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("appScheme", "esign://demo/signBack");
        map.put("redirectUrl", "esign://demo/signBack");
        map.put("noticeDeveloperUrl", "esign://demo/signBack");
        map.put("idNumber", mIdCard);
        if (!TextUtils.isEmpty(authPhone)) {
            map.put("mobile", authPhone);
        }
        map.put("name", doctorName);
        if (id == 1) {
            map.put("willTypes", ZHIMA);      //这个是数组
            map.put("willDefaultType", "FACE_ZHIMA_XY");
        } else {
            map.put("willTypes", TENCENT);
            map.put("willDefaultType", "FACE_TECENT_CLOUD_H5");
        }
        mPresenter.caFaceSecondCode(map);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void getPrescriptionFromIdSuccess(HisPrescriptionDtoBean bean) {
        this.mBean = bean;
        String date = bean.getVisitDate();
        if (!TextUtils.isEmpty(date) && date.length() >= 10) {
            date = date.substring(0, 10);
        }
        textNo.setText("No：" + bean.getPerscriptionNo());
        textName.setText("姓名：" + bean.getPatientName());
        textSex.setText("性别：" + bean.getSexName());
        textAge.setText("年龄：" + bean.getPatientAge() + "岁");
        textOffice.setText("科室：" + bean.getDeptName());
        textDate.setText("日期：" + date);
        textMedicalNo.setText("门诊病历号：" + bean.getConsulationNo());
        textDiagnose.setText("诊断：" + bean.getDiagDesc());

        //展示界面
        List<HisPrescriptionDetailDtosBean> detailDtos = bean.getHisPrescriptionDetailDtos();
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
            CommonAdapter commonAdapter2 = new CommonAdapter<HisPrescriptionTcmDetailsBean>(this,
                    R.layout.item_prescription_chinese_item, tcmDetails) {
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
    public void caImgUploadSuccess(String bean) {
        mPresenter.getDoctorInfo(false);
//        ToastUtil.showToast("签名上传成功");
        //上传完图片，开始上传别的
        noUploadPhoto();
    }

    @Override
    public void imgDownload(List<String> bean) {
        if (bean == null || bean.size() == 0) {
            return;
        }
        mUrl = bean.get(0).split(",")[1];
        if (isBase64Img(mUrl)) {
            mUrl = mUrl.split(",")[1];
        }
        byte[] decode = Base64.decode(mUrl.getBytes(), Base64.DEFAULT);
        mBitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        imgSignaturePhoto.setImageBitmap(mBitmap);
    }

    //无意愿提交成功
    @Override
    public void caSignPDFNoneSuccess(String bean) {

        mPresenter.getDoctorInfo();

//        startToCheckActivity();
    }

    @Override
    public void setDoctorInfoSuccess() {
        //手机认证，判断是否进行二次认证
        if (authentication == 1) {
            //是否有意愿， 1 没有 2 有
            if (mType == 1) {
                //直接无意愿
                startToCheckActivity();
            } else {
                startToSignActivity();
            }
        } else {
            if (mType == 1) {
                //直接无意愿
                startToCheckActivity();
            } else {
                doctorInfo.setBizid(bizId);
                //人脸验证
                Intent intent = new Intent(this, H5SecondActivity.class);
                intent.putExtra("url", shortUrl);
                intent.putExtra("bizId", bizId);
                intent.putExtra("historyDrugsBean", mBean);
                intent.putExtra("prescriptionId", prescriptionId);
//                intent.putExtra("authentication", authentication);
                startActivityForResult(intent, 300);
            }
        }
    }

    @Override
    public void caFaceSecondCodeSuccess(CAPhoneBean bean) {
        if (!bean.getCode().equals("0")) {
            ToastUtil.showToast(bean.getMessage());
            return;
        }
        bizId = bean.getData().getBizId();

        shortUrl = bean.getData().getShortUrl();
        setDoctorInfoSuccess();
    }

    //直接进入审核界面
    private void startToCheckActivity() {
        Intent intent = new Intent(PrescriptionSignActivity.this, PrescriptionSubmitCheckActivity.class);
        intent.putExtra("historyDrugsBean", mBean);
        intent.putExtra("prescriptionId", prescriptionId);
        startActivityForResult(intent, 300);
    }

    //进入手机二次验证界面
    private void startToSignActivity() {
        Intent intent = new Intent(PrescriptionSignActivity.this, PhoneAspirationActivity.class);
//        intent.putExtra("mobileNo", mobileNo);
        intent.putExtra("prescriptionId", prescriptionId);
        intent.putExtra("historyDrugsBean", mBean);
        startActivityForResult(intent, 300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                mPhotoPath = data.getStringExtra("path");
                Glide.with(this).load(mPhotoPath).skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE).into(imgSignaturePhoto);
                Glide.with(this).load(mPhotoPath).skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE).into(imgPhysician);
            }

            if (requestCode == 300) {
                Intent intent = new Intent();
                intent.putExtra("prescriptionId", prescriptionId);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(PrescriptionSignActivity.this, "");
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}
