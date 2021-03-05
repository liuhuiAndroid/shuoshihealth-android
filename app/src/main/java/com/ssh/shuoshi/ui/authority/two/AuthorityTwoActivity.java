package com.ssh.shuoshi.ui.authority.two;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.ssh.shuoshi.Constants;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.eventbus.SwitchEvent;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.authority.editphoto.EditPhotoActivity;
import com.ssh.shuoshi.ui.authority.three.AuthorityThreeActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.dialog.SamplePhotoDialog;
import com.ssh.shuoshi.ui.login.LoginActivity;
import com.ssh.shuoshi.ui.web.WebActivity;
import com.ssh.shuoshi.util.GlideEngine;
import com.ssh.shuoshi.util.image.RoundTransform;
import com.ssh.shuoshi.view.title.UniteTitle;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ssh.shuoshi.util.PhotoUtils.isBase64Img;

/**
 * created by hwt on 2020/12/10
 */
public class AuthorityTwoActivity extends BaseActivity implements AuthorityTwoContract.View, View.OnClickListener {

    @Inject
    AuthorityTwoPresenter mPresenter;
    @BindView(R.id.title)
    UniteTitle title;
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.image3)
    ImageView image3;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.img_idf)
    ImageView imgIdf;
    @BindView(R.id.img_add_idf)
    ImageView imgAddIdf;
    @BindView(R.id.tv_idf_upload)
    TextView tvIdfUpload;
    @BindView(R.id.tv_hint_idf)
    TextView tvHintIdf;
    @BindView(R.id.img_idb)
    ImageView imgIdb;
    @BindView(R.id.img_add_idb)
    ImageView imgAddIdb;
    @BindView(R.id.tv_hint_idb)
    TextView tvHintIdb;
    @BindView(R.id.tv_idb_upload)
    TextView tvIdbUpload;
    @BindView(R.id.img_doctorP)
    ImageView imgDoctorP;
    @BindView(R.id.img_add_doctorP)
    ImageView imgAddDoctorP;
    @BindView(R.id.tv_hint_doctorP)
    TextView tvHintDoctorP;
    @BindView(R.id.tv_doctorP_upload)
    TextView tvDoctorPUpload;
    @BindView(R.id.img_doctorZ)
    ImageView imgDoctorZ;
    @BindView(R.id.img_add_doctorZ)
    ImageView imgAddDoctorZ;
    @BindView(R.id.tv_hint_doctorZ)
    TextView tvHintDoctorZ;
    @BindView(R.id.tv_doctorZ_upload)
    TextView tvDoctorZUpload;
    @BindView(R.id.img_doctorT)
    ImageView imgDoctorT;
    @BindView(R.id.img_add_doctorT)
    ImageView imgAddDoctorT;
    @BindView(R.id.tv_hint_doctorT)
    TextView tvHintDoctorT;
    @BindView(R.id.tv_doctorT_upload)
    TextView tvDoctorTUpload;
    @BindView(R.id.imageDoctorP)
    ImageView imageDoctorP;
    @BindView(R.id.imageDoctorZ)
    ImageView imageDoctorZ;
    @BindView(R.id.imageDoctorT)
    ImageView imageDoctorT;
    @BindView(R.id.cb_agree)
    CheckBox cbAgree;
    @BindView(R.id.tv_agree)
    TextView tvAgree;
    @BindView(R.id.tv_law)
    TextView tvLaw;
    private LoadingDialog mLoadingDialog;

    @Inject
    UserStorage mUserStorage;

    private String mType = "";
    private Map<String, String> map = new HashMap<>();
    private int num = 0;
    private Map<String, Object> mData;
    private HisDoctorBean doctorInfo;
    private boolean isShow;
    private RequestOptions options;

    @Override
    public int initContentView() {
        return R.layout.activity_authority_two;
    }

    @Override
    protected void initInjector() {
        DaggerAuthorityTwoComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);

        title.setBackListener(-1, v -> finish());
        tvAgree.setOnClickListener(v -> {
            boolean checked = cbAgree.isChecked();
            cbAgree.setChecked(!checked);
        });
        tvLaw.setOnClickListener(this);
        options = new RequestOptions().centerCrop().transform(new RoundTransform(this, 5));
        Intent intent = getIntent();
        isShow = getIntent().getBooleanExtra("isShow", false);
        mData = new HashMap<>();
        mData.put("name", intent.getStringExtra("name"));
        mData.put("areaId", intent.getIntExtra("areaId", -1));
        mData.put("hospitalName", intent.getStringExtra("hospitalName"));
        int deptId = intent.getIntExtra("deptId", -1);
        if (deptId != -1) {
            mData.put("deptId", intent.getIntExtra("deptId", -1));
        }
        mData.put("titleId", intent.getIntExtra("titleId", -1));
        mData.put("briefIntroduction", intent.getStringExtra("briefIntroduction"));
        mData.put("goodAt", intent.getStringExtra("goodAt"));

        imgAddIdf.setOnClickListener(this);
        tvHintIdf.setOnClickListener(this);
        tvIdfUpload.setOnClickListener(this);
        imgAddIdb.setOnClickListener(this);
        tvHintIdb.setOnClickListener(this);
        tvIdbUpload.setOnClickListener(this);
        imgAddDoctorP.setOnClickListener(this);
        tvHintDoctorP.setOnClickListener(this);
        tvDoctorPUpload.setOnClickListener(this);
        imgAddDoctorZ.setOnClickListener(this);
        tvHintDoctorZ.setOnClickListener(this);
        tvDoctorZUpload.setOnClickListener(this);
        imgAddDoctorT.setOnClickListener(this);
        tvHintDoctorT.setOnClickListener(this);
        tvDoctorTUpload.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        imageDoctorP.setOnClickListener(this);
        imageDoctorZ.setOnClickListener(this);
        imageDoctorT.setOnClickListener(this);

        if (isShow) {
            List<String> photoList = new ArrayList<>();
            doctorInfo = mUserStorage.getDoctorInfo();
            String idCardFront = doctorInfo.getIdCardFront();
            String idCardBack = doctorInfo.getIdCardBack();
            String practiceCertificate = doctorInfo.getPracticeCertificate();
            String qualificationsCertificate = doctorInfo.getQualificationsCertificate();
            String titleCertificate = doctorInfo.getTitleCertificate();
            if (!TextUtils.isEmpty(idCardFront)) {
                photoList.add(idCardFront);
            }
            if (!TextUtils.isEmpty(idCardBack)) {
                photoList.add(idCardBack);
            }
            if (!TextUtils.isEmpty(practiceCertificate)) {
                photoList.add(practiceCertificate);
            }
            if (!TextUtils.isEmpty(qualificationsCertificate)) {
                photoList.add(qualificationsCertificate);
            }
            if (!TextUtils.isEmpty(titleCertificate)) {
                photoList.add(titleCertificate);
            }
            String[] photoPath = photoList.toArray(new String[photoList.size()]);
            mPresenter.getImagePath(photoPath);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_law:
                Intent intent2 = new Intent(AuthorityTwoActivity.this, WebActivity.class);
                intent2.putExtra("url", Constants.WEB_001);
                startActivity(intent2);
                break;
            case R.id.imageDoctorP:
                SamplePhotoDialog samplePhoto1 = new SamplePhotoDialog(AuthorityTwoActivity.this, R.style.dialog_physician_certification, 1);
                samplePhoto1.show();
                break;

            case R.id.imageDoctorZ:
                SamplePhotoDialog samplePhoto2 = new SamplePhotoDialog(AuthorityTwoActivity.this, R.style.dialog_physician_certification, 2);
                samplePhoto2.show();
                break;

            case R.id.imageDoctorT:
                SamplePhotoDialog samplePhoto3 = new SamplePhotoDialog(AuthorityTwoActivity.this, R.style.dialog_physician_certification, 3);
                samplePhoto3.show();
                break;

            case R.id.img_add_idf:
            case R.id.tv_hint_idf:
            case R.id.tv_idf_upload:
                mType = "idCardFront";
                choosePhoto();
                break;

            case R.id.img_add_idb:
            case R.id.tv_hint_idb:
            case R.id.tv_idb_upload:
                mType = "idCardBack";
                choosePhoto();
                break;

            case R.id.img_add_doctorP:
            case R.id.tv_hint_doctorP:
            case R.id.tv_doctorP_upload:
                mType = "practiceCertificate";
                choosePhoto();
                break;

            case R.id.img_add_doctorZ:
            case R.id.tv_hint_doctorZ:
            case R.id.tv_doctorZ_upload:
                mType = "qualificationsCertificate";
                choosePhoto();
                break;

            case R.id.img_add_doctorT:
            case R.id.tv_hint_doctorT:
            case R.id.tv_doctorT_upload:
                mType = "titleCertificate";
                choosePhoto();
                break;

            case R.id.btn_next:
                //判断图片是否存在
                if (imgIdf.getDrawable() == null) {
                    ToastUtil.showToast("需要上传身份证正面照片");
                    return;
                }

                if (imgIdb.getDrawable() == null) {
                    ToastUtil.showToast("需要上传身份证反面照片");
                    return;
                }

                if (imgDoctorP.getDrawable() == null) {
                    ToastUtil.showToast("需要上传医师执业证书");
                    return;
                }

                if (imgDoctorZ.getDrawable() == null) {
                    ToastUtil.showToast("需要上传医师资格证书");
                    return;
                }

                if (imgDoctorT.getDrawable() == null) {
                    ToastUtil.showToast("需要上传职称证书");
                    return;
                }
                if (!cbAgree.isChecked()) {
                    ToastUtil.showToast("请仔细阅读并勾选相关协议与政策");
                    return;
                }

                num = 0;
                showLoading();
                if (isShow) {
                    if (map == null || map.size() == 0) {
                        hideLoading();
                        mPresenter.putDoctorInfo(mData);
                    } else {
                        for (String key : map.keySet()) {
                            String value = map.get(key);
                            mPresenter.uploadNewImg(key, value);
                        }
                    }
                } else {
                    for (String key : map.keySet()) {
                        String value = map.get(key);
                        mPresenter.uploadNewImg(key, value);
                    }
                }
                break;
        }
    }

    @Override
    public void uploadSuccess(List<String> bean, String key) {
        num++;
        mData.put(key, bean.get(0));
        if (num == map.size()) {
            //开始上传基本数据
            mPresenter.putDoctorInfo(mData);
        }
    }

    @Override
    public void uploadInfoSuccess(String bean) {
        hideLoading();
        Intent intent = new Intent(AuthorityTwoActivity.this, AuthorityThreeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void imgDownload(List<String> bean) {
        for (int i = 0; i < bean.size(); i++) {
            showImage(bean.get(i), i);
        }
    }

    private void showImage(String path, int position) {
        if (isBase64Img(path)) {
            path = path.split(",")[1];
        }
        byte[] decode = Base64.decode(path.getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        switch (position) {
            case 0:

                Glide.with(this).load(bitmap).apply(options).into(imgIdf);

//                imgIdf.setImageBitmap(bitmap);
                tvIdfUpload.setVisibility(View.VISIBLE);
                tvHintIdf.setVisibility(View.GONE);
                imgAddIdf.setVisibility(View.GONE);
                break;

            case 1:

                Glide.with(this).load(bitmap).apply(options).into(imgIdb);

//                imgIdb.setImageBitmap(bitmap);
                tvIdbUpload.setVisibility(View.VISIBLE);
                tvHintIdb.setVisibility(View.GONE);
                imgAddIdb.setVisibility(View.GONE);
                break;

            case 2:

                Glide.with(this).load(bitmap).apply(options).into(imgDoctorP);

//                imgDoctorP.setImageBitmap(bitmap);
                tvDoctorPUpload.setVisibility(View.VISIBLE);
                tvHintDoctorP.setVisibility(View.GONE);
                imgAddDoctorP.setVisibility(View.GONE);
                break;

            case 3:

                Glide.with(this).load(bitmap).apply(options).into(imgDoctorZ);

//                imgDoctorZ.setImageBitmap(bitmap);
                tvDoctorZUpload.setVisibility(View.VISIBLE);
                tvHintDoctorZ.setVisibility(View.GONE);
                imgAddDoctorZ.setVisibility(View.GONE);
                break;

            case 4:

                Glide.with(this).load(bitmap).apply(options).into(imgDoctorT);

//                imgDoctorT.setImageBitmap(bitmap);
                imgAddDoctorT.setVisibility(View.GONE);
                tvHintDoctorT.setVisibility(View.GONE);
                tvDoctorTUpload.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void choosePhoto() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage()) //图片
                .theme(R.style.picture_default_style) // 主题样式
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(1) // 最大图片选择数量
                .minSelectNum(1) // 最小选择数量
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .isPreviewImage(true)// 是否可预览图片 true or false
                .isCompress(true)
                .minimumCompressSize(100)// 小于多少kb的图片不压缩
                .isCamera(true)// 是否显示拍照按钮 true or false
                .isEnableCrop(false)// 是否裁剪 true or false
                .isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .isGif(false)// 是否显示gif图片 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(AuthorityTwoActivity.this, "");
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
        hideLoading();
        loadError(throwable);
    }

    /**
     * 选择图片返回的图片信息
     */
    private List<LocalMedia> selectList = new ArrayList<>();
    private String mCompressPath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList != null && selectList.size() > 0) {
                        final LocalMedia localMedia = selectList.get(0);
                        mCompressPath = localMedia.getCompressPath();
                        Intent intent = new Intent(AuthorityTwoActivity.this, EditPhotoActivity.class);
                        intent.putExtra("path", mCompressPath);
                        intent.putExtra("type", mType);
                        startActivityForResult(intent, 200);
                    }
                    break;
                case 200:
                    String photoPath = data.getStringExtra("photoPath");
                    showPhoto(photoPath);
                    break;
            }
        }
    }

    private void showPhoto(String photoPath) {

        map.put(mType, photoPath);

        if (mType.equals("idCardFront")) {
            tvIdfUpload.setVisibility(View.VISIBLE);
            tvHintIdf.setVisibility(View.GONE);
            imgAddIdf.setVisibility(View.GONE);
            Glide.with(this).load(photoPath).apply(options)
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgIdf);
        } else if (mType.equals("idCardBack")) {
            tvIdbUpload.setVisibility(View.VISIBLE);
            tvHintIdb.setVisibility(View.GONE);
            imgAddIdb.setVisibility(View.GONE);
            Glide.with(this).load(photoPath).apply(options)
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgIdb);
        } else if (mType.equals("practiceCertificate")) {
            tvDoctorPUpload.setVisibility(View.VISIBLE);
            tvHintDoctorP.setVisibility(View.GONE);
            imgAddDoctorP.setVisibility(View.GONE);
            Glide.with(this).load(photoPath).apply(options)
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgDoctorP);
        } else if (mType.equals("qualificationsCertificate")) {
            tvDoctorZUpload.setVisibility(View.VISIBLE);
            tvHintDoctorZ.setVisibility(View.GONE);
            imgAddDoctorZ.setVisibility(View.GONE);
            Glide.with(this).load(photoPath).apply(options)
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgDoctorZ);
        } else if (mType.equals("titleCertificate")) {
            tvDoctorTUpload.setVisibility(View.VISIBLE);
            tvHintDoctorT.setVisibility(View.GONE);
            imgAddDoctorT.setVisibility(View.GONE);
            Glide.with(this).load(photoPath).apply(options)
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgDoctorT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
