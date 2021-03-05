package com.ssh.shuoshi.ui.addpatient;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.permissionx.guolindev.PermissionX;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.util.SaveNetPhotoUtils;
import com.ssh.shuoshi.util.image.RoundTransform;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 添加患者
 * created by hwt on 2021/1/5
 */
public class AddPatientActivity extends BaseActivity implements AddPatientContract.View {

    @Inject
    AddPatientPresenter mPresenter;
    @BindView(R.id.title)
    UniteTitle title;
    @BindView(R.id.imgHead)
    ImageView imgHead;
    @BindView(R.id.textName)
    TextView textName;
    @BindView(R.id.textJob)
    TextView textJob;
    @BindView(R.id.textAddress)
    TextView textAddress;
    @BindView(R.id.imgCode)
    ImageView imgCode;

    @Inject
    UserStorage mUserStorage;
    @BindView(R.id.rlALl)
    RelativeLayout rlALl;

    private LoadingDialog mLoadingDialog;

    @Override
    public int initContentView() {
        return R.layout.activity_add_patient;
    }

    @Override
    protected void initInjector() {
        DaggerAddPatientComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);

        HisDoctorBean doctorInfo = mUserStorage.getDoctorInfo();

        textName.setText(doctorInfo.getName());
        textJob.setText(doctorInfo.getDoctorTitleName());
        textAddress.setText(doctorInfo.getHospitalName());
        title.setBackListener(-1, v -> finish());
        title.setRightButton("保存图片", v -> {
            PermissionX.init(this)
                    .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).request((allGranted, grantedList, deniedList) -> {
                if (allGranted) {
                    if (imgCode.getDrawable() == null) {
                        ToastUtil.showToast("暂无图片");
                        return;
                    }
                    Bitmap viewBitmap = createViewBitmap(rlALl);
                    boolean isSuccess = SaveNetPhotoUtils.saveBitmap(this, viewBitmap, "code.jpeg");
                    if (isSuccess) {
                        ToastUtil.showToast("保存成功");
                    } else {
                        ToastUtil.showToast("保存失败");
                    }
                } else {
                    Toast.makeText(this, "没有存储权限，无法进行图片保存", Toast.LENGTH_SHORT).show();
                }
            });
        });

        if (doctorInfo.getHeadImg() != null) {
            String[] photoPath = new String[]{doctorInfo.getHeadImg()};
            mPresenter.getImagePath(photoPath);
        }

        mPresenter.getDoctorCodePhoto();
    }

    public static Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(AddPatientActivity.this, "");
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
    public void getDoctorCodePhotoSuccess(String path) {
        Bitmap qr = generateBitmap(path, 269, 269);
        imgCode.setImageBitmap(qr);
    }

    @Override
    public void imgDownload(List<String> bean) {
        if (bean == null || bean.size() == 0) {
            return;
        }
        RequestOptions options = new RequestOptions().centerCrop().transform(new RoundTransform(this, 5));
        Glide.with(this).load(bean.get(0)).apply(options)
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgHead);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
