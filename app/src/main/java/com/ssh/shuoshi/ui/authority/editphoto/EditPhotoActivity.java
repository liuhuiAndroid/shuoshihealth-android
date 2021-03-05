package com.ssh.shuoshi.ui.authority.editphoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.authority.two.AuthorityTwoActivity;
import com.ssh.shuoshi.ui.authority.two.AuthorityTwoPresenter;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ssh.shuoshi.util.PhotoUtils.saveNewBitmap;

/**
 * created by hwt on 2020/12/10
 */
public class EditPhotoActivity extends BaseActivity implements View.OnClickListener,
        EditPhotoContract.View {
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_rotate)
    TextView tvRotate;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;

    @Inject
    EditPhotoPresenter mPresenter;

    private Bitmap mBitmap;
    private String type;
    private LoadingDialog mLoadingDialog;
    private boolean isUpload;

    @Override
    public int initContentView() {
        return R.layout.activity_edit_photo;
    }

    @Override
    protected void initInjector() {
        DaggerEditPhotoComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void setStatusBar() {
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        isUpload = intent.getBooleanExtra("isUpload", false);
        String path = intent.getStringExtra("path");
        mBitmap = BitmapFactory.decodeFile(path);
//        Glide.with(this).load(path).into(img);
        img.setImageBitmap(mBitmap);
        tvCancel.setOnClickListener(this);
        tvRotate.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                finish();
                break;

            case R.id.tv_rotate:
                mBitmap = rotateBitmap(90, mBitmap);
                img.setImageBitmap(mBitmap);
                break;

            case R.id.tv_confirm:
                //保存到本地
                String photoPath = saveNewBitmap(type + System.currentTimeMillis() + ".png", mBitmap);

                if (isUpload) {
                    showLoading();
                    mPresenter.uploadNewImg(photoPath);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("photoPath", photoPath);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }

    public static Bitmap rotateBitmap(int degree, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(EditPhotoActivity.this, "");
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
    public void uploadSuccess(List<String> bean) {
        if (bean == null || bean.size() == 0) {
            hideLoading();
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("headImg", bean.get(0));
        mPresenter.putDoctorInfo(map);
    }

    @Override
    public void uploadInfoSuccess(String bean) {
        hideLoading();
        setResult(RESULT_OK, getIntent());
        finish();
    }
}
