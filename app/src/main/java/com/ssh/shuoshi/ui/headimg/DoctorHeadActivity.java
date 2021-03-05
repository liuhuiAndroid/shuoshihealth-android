package com.ssh.shuoshi.ui.headimg;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.authority.editphoto.EditPhotoActivity;
import com.ssh.shuoshi.util.GlideEngine;
import com.ssh.shuoshi.view.title.UniteTitle;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

/**
 * created by hwt on 2021/1/5
 */
public class DoctorHeadActivity extends BaseActivity {

    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.imgHead)
    ImageView imgHead;

    @Override
    public int initContentView() {
        return R.layout.activity_doctor_head;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initUiAndListener() {

        uniteTitle.setBackListener(-1, v -> finish());

        boolean headPath = getIntent().getBooleanExtra("headPath", false);
        if (headPath) {
            String headImg = MMKV.defaultMMKV().getString("headImg", "");
            if (!TextUtils.isEmpty(headImg)) {
                Glide.with(this).load(headImg)
                        .placeholder(getResources().getDrawable(R.drawable.default_img))
                        .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgHead);
            }
        }
        uniteTitle.setRightButton("从相册选择", v -> {
            choosePhoto();
        });
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
                .compressQuality(50)//图片压缩后输出质量
                .isEnableCrop(true)// 是否裁剪
                .withAspectRatio(1, 1)
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .isCamera(true)// 是否显示拍照按钮 true or false
                .setCropDimmedColor(ContextCompat.getColor(this, R.color.transparent))// 设置裁剪背景色值
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
                        Intent intent = new Intent(this, EditPhotoActivity.class);
                        intent.putExtra("path", mCompressPath);
                        intent.putExtra("type", "doctor");
                        intent.putExtra("isUpload", true);
                        startActivityForResult(intent, 200);
                    }
                    break;
                case 200:
                    setResult(RESULT_OK, getIntent());
                    finish();
                    break;
            }
        }
    }

}
