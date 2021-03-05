package com.ssh.shuoshi.ui.doctorauthentication.auth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.eventbus.SwitchEvent;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseFragment;
import com.ssh.shuoshi.ui.authority.editphoto.EditPhotoActivity;
import com.ssh.shuoshi.ui.authority.two.AuthorityTwoActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.dialog.SamplePhotoDialog;
import com.ssh.shuoshi.ui.doctorauthentication.DoctorAuthenticationComponent;
import com.ssh.shuoshi.util.GlideEngine;
import com.ssh.shuoshi.util.image.RoundTransform;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

import static com.ssh.shuoshi.util.PhotoUtils.isBase64Img;

public class DoctorAuthenticationAuthFragment extends BaseFragment implements View.OnClickListener,
        DoctorAuthenticationAuthContract.View {

    @BindView(R.id.img_idf)
    ImageView imgIdf;
    @BindView(R.id.tv_idf_upload)
    TextView tvIdfUpload;
    @BindView(R.id.img_idb)
    ImageView imgIdb;
    @BindView(R.id.tv_idb_upload)
    TextView tvIdbUpload;
    @BindView(R.id.img_doctorP)
    ImageView imgDoctorP;
    @BindView(R.id.tv_doctorP_upload)
    TextView tvDoctorPUpload;
    @BindView(R.id.img_doctorZ)
    ImageView imgDoctorZ;
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
    @BindView(R.id.buttonSave)
    Button buttonSave;
    @BindView(R.id.scrollViewBasic)
    ScrollView scrollViewBasic;
    @BindView(R.id.imageDoctorP)
    ImageView imageDoctorP;
    @BindView(R.id.imageDoctorZ)
    ImageView imageDoctorZ;
    @BindView(R.id.imageDoctorT)
    ImageView imageDoctorT;

    private LoadingDialog mLoadingDialog;
    private String mType = "";
    private Map<String, String> map = new HashMap<>();
    private Map<String, Object> mData;
    private int num = 0;

    @Inject
    DoctorAuthenticationAuthPresenter mPresenter;
    @Inject
    UserStorage mUserStorage;
    //基本信息
    private Map<String, Object> mInfoData;
    private RequestOptions options;

    @Override
    public void initInjector() {
        getComponent(DoctorAuthenticationComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_doctor_authentication_auth;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void initUI(View view) {
        mPresenter.attachView(this);
        mData = new HashMap<>();
        options = new RequestOptions().centerCrop().transform(new RoundTransform(getActivity(), 5));
        List<String> photoList = new ArrayList<>();
        HisDoctorBean doctorInfo = mUserStorage.getDoctorInfo();
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


        tvIdfUpload.setOnClickListener(this);
        tvIdbUpload.setOnClickListener(this);
        tvDoctorPUpload.setOnClickListener(this);
        tvDoctorZUpload.setOnClickListener(this);
        imgAddDoctorT.setOnClickListener(this);
        tvHintDoctorT.setOnClickListener(this);
        tvDoctorTUpload.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        imageDoctorP.setOnClickListener(this);
        imageDoctorZ.setOnClickListener(this);
        imageDoctorT.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imageDoctorP:
                SamplePhotoDialog samplePhoto1 = new SamplePhotoDialog(getActivity(), R.style.dialog_physician_certification, 1);
                samplePhoto1.show();
                break;

            case R.id.imageDoctorZ:
                SamplePhotoDialog samplePhoto2 = new SamplePhotoDialog(getActivity(), R.style.dialog_physician_certification, 2);
                samplePhoto2.show();
                break;

            case R.id.imageDoctorT:
                SamplePhotoDialog samplePhoto3 = new SamplePhotoDialog(getActivity(), R.style.dialog_physician_certification, 3);
                samplePhoto3.show();
                break;

            case R.id.tv_idf_upload:
                mType = "idCardFront";
                choosePhoto();
                break;

            case R.id.tv_idb_upload:
                mType = "idCardBack";
                choosePhoto();
                break;

            case R.id.tv_doctorP_upload:
                mType = "practiceCertificate";
                choosePhoto();
                break;

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

            case R.id.buttonSave:
                num = 0;
                showLoading();
                if (map == null || map.size() == 0) {
                    if (mInfoData == null || mInfoData.size() == 0) {
                        hideLoading();
                        ToastUtil.showToast("没有更新的信息");
                        return;
                    } else {
                        mData.putAll(mInfoData);
                        mPresenter.putDoctorInfo(mData);
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
                        Intent intent = new Intent(getActivity(), EditPhotoActivity.class);
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
            Glide.with(getActivity()).load(photoPath).apply(options)
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgIdf);
        } else if (mType.equals("idCardBack")) {
            tvIdbUpload.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(photoPath).apply(options)
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgIdb);
        } else if (mType.equals("practiceCertificate")) {
            tvDoctorPUpload.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(photoPath).apply(options)
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgDoctorP);
        } else if (mType.equals("qualificationsCertificate")) {
            tvDoctorZUpload.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(photoPath).apply(options)
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgDoctorZ);
        } else if (mType.equals("titleCertificate")) {
            tvDoctorTUpload.setVisibility(View.VISIBLE);
            tvHintDoctorT.setVisibility(View.GONE);
            imgAddDoctorT.setVisibility(View.GONE);
            Glide.with(getActivity()).load(photoPath).apply(options)
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgDoctorT);
        }
    }


    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(getActivity(), "");
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

    @Override
    public void uploadSuccess(List<String> bean, String key) {
        num++;
        mData.put(key, bean.get(0));
        if (num == map.size()) {
            if (mInfoData != null) {
                mData.putAll(mInfoData);
            }
            //开始上传基本数据
            mPresenter.putDoctorInfo(mData);
        }
    }

    @Override
    public void uploadInfoSuccess(String bean) {
        ToastUtil.showToast("保存成功");
        mPresenter.getDoctorInfo();
    }

    @Override
    public void imgDownload(List<String> bean) {
        for (int i = 0; i < bean.size(); i++) {
            showImage(bean.get(i), i);
        }
    }

    @Override
    public void getDoctorInfoSuccess() {
        EventBus.getDefault().post(new SwitchEvent("change"));
        getActivity().finish();
    }

    private void showImage(String path, int position) {
        if (isBase64Img(path)) {
            path = path.split(",")[1];
        }
        byte[] decode = Base64.decode(path.getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        switch (position) {
            case 0:
                Glide.with(getActivity()).load(bitmap).apply(options).into(imgIdf);
                break;

            case 1:
                Glide.with(getActivity()).load(bitmap).apply(options).into(imgIdb);
//                imgIdb.setImageBitmap(bitmap);
                break;

            case 2:
                Glide.with(getActivity()).load(bitmap).apply(options).into(imgDoctorP);
//                imgDoctorP.setImageBitmap(bitmap);
                break;

            case 3:
                Glide.with(getActivity()).load(bitmap).apply(options).into(imgDoctorZ);
//                imgDoctorZ.setImageBitmap(bitmap);
                break;

            case 4:
                Glide.with(getActivity()).load(bitmap).apply(options).into(imgDoctorT);
//                imgDoctorT.setImageBitmap(bitmap);
                imgAddDoctorT.setVisibility(View.GONE);
                tvHintDoctorT.setVisibility(View.GONE);
                tvDoctorTUpload.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setData(Map<String, Object> map) {
        this.mInfoData = map;
    }
}
