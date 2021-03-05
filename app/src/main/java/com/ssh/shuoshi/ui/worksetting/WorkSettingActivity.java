package com.ssh.shuoshi.ui.worksetting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.ssh.shuoshi.Constants;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.DoctorConsultationInfo;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.dialog.TuServiceDialog;
import com.ssh.shuoshi.ui.dialog.VideoServiceDialog;
import com.ssh.shuoshi.ui.worksetting.servicenote.ServiceNoteActivity;
import com.ssh.shuoshi.ui.worksetting.timesetting.ServiceTimeSettingActivity;
import com.ssh.shuoshi.util.DoubleUtil;
import com.ssh.shuoshi.util.SoftKeyboardUtil;
import com.ssh.shuoshi.view.title.UniteTitle;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * 工作室设置
 */
public class WorkSettingActivity extends BaseActivity implements WorkSettingContract.View, View.OnClickListener {

    @Inject
    WorkSettingPresenter mPresenter;
    @BindView(R.id.title)
    UniteTitle title;
    @BindView(R.id.img_left_tu)
    ImageView imgLeftTu;
    @BindView(R.id.tv_tu)
    TextView tvTu;
    @BindView(R.id.img_right_tu)
    ImageView imgRightTu;
    @BindView(R.id.rl_tu)
    RelativeLayout rlTu;
    @BindView(R.id.img_left_shi)
    ImageView imgLeftShi;
    @BindView(R.id.tv_video)
    TextView tvVideo;
    @BindView(R.id.img_right_shi)
    ImageView imgRightShi;
    @BindView(R.id.rl_video)
    RelativeLayout rlVideo;

    @BindView(R.id.llTuSetting)
    LinearLayout llTuSetting;
    @BindView(R.id.llVideoSetting)
    LinearLayout llVideoSetting;

    @BindView(R.id.rl_tu_price)
    RelativeLayout rlTuPrice;
    @BindView(R.id.rl_video_price)
    RelativeLayout rlVideoPrice;

    @BindView(R.id.tv_tu_price)
    TextView tvTuPrice;
    @BindView(R.id.tv_video_price)
    TextView tvVideoPrice;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @BindView(R.id.switch_tu)
    Switch switchTu;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @BindView(R.id.switch_video)
    Switch switchVideo;


    @BindView(R.id.tv_video_time)
    TextView textViewVideoTime;

    private LoadingDialog mLoadingDialog;

    @Inject
    UserStorage mUserStorage;

    private int requestCode = 1001;

    private int tuId = 0;
    private int videoId = 0;

    @Override
    public int initContentView() {
        return R.layout.activity_work_setting;
    }

    @Override
    protected void initInjector() {
        DaggerWorkSettingComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        //获取医生问诊列表
        mPresenter.getDoctorConsultationInfo();

        title.setBackListener(-1, v -> finish());

        rlTu.setOnClickListener(this);
        rlVideo.setOnClickListener(this);

        rlTuPrice.setOnClickListener(this);
        rlVideoPrice.setOnClickListener(this);

        textViewVideoTime.setOnClickListener(this);

        //问诊启动状态
        switchTu.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!buttonView.isPressed()) {
                return;
            }
            if (TextUtils.isEmpty(tvTuPrice.getText().toString().trim())) {
                ToastUtil.showToast("请先设置问诊价格");
                switchTu.setChecked(false);
                return;
            }
            mPresenter.editDoctorConsultation(tuId, Constants.CONSULTATION_TYPE_PICTURE, null, isChecked ? 1 : 0);
        });
        switchVideo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!buttonView.isPressed()) {
                return;
            }
            if (TextUtils.isEmpty(tvVideoPrice.getText().toString().trim())) {
                ToastUtil.showToast("请先设置问诊价格");
                switchVideo.setChecked(false);
                return;
            }
            mPresenter.editDoctorConsultation(videoId, Constants.CONSULTATION_TYPE_VIDEO, null, isChecked ? 1 : 0);
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_tu:
                Intent intentPictureInsert = new Intent(WorkSettingActivity.this, ServiceNoteActivity.class);
                intentPictureInsert.putExtra("consultationType", Constants.CONSULTATION_TYPE_PICTURE);
                startActivityForResult(intentPictureInsert, requestCode);
                break;
            case R.id.rl_video:
                Intent intentVideoInsert = new Intent(WorkSettingActivity.this, ServiceNoteActivity.class);
                intentVideoInsert.putExtra("consultationType", Constants.CONSULTATION_TYPE_VIDEO);
                startActivityForResult(intentVideoInsert, requestCode);
                break;
            case R.id.rl_tu_price:
                TuServiceDialog tuServiceDialog = new TuServiceDialog(WorkSettingActivity.this, "图文问诊价格", R.style.dialog_physician_certification);
                tuServiceDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                tuServiceDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
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
                        tuServiceDialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
                    }
                });

                tuServiceDialog.setOnItemClickListener(new TuServiceDialog.ItemClickListener() {
                    @Override
                    public void cancel() {
                        tuServiceDialog.dismiss();
                    }

                    @Override
                    public void save(String number) {
                        SoftKeyboardUtil.hideSoftKeyboard(WorkSettingActivity.this);
                        tvTuPrice.setText(number + "元");
                        tuServiceDialog.dismiss();
                        mPresenter.editDoctorConsultation(tuId, Constants.CONSULTATION_TYPE_PICTURE, DoubleUtil.getDouble(number), null);
                    }
                });
                tuServiceDialog.show();
                break;
            case R.id.rl_video_price:
                VideoServiceDialog videoServiceDialog = new VideoServiceDialog(WorkSettingActivity.this, R.style.dialog_physician_certification);
                videoServiceDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                videoServiceDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
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
                        videoServiceDialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
                    }
                });

                videoServiceDialog.setOnItemClickListener(new VideoServiceDialog.ItemClickListener() {
                    @Override
                    public void cancel() {
                        videoServiceDialog.dismiss();
                    }

                    @Override
                    public void save(String number) {
                        SoftKeyboardUtil.hideSoftKeyboard(WorkSettingActivity.this);
                        tvVideoPrice.setText(number + "元");
                        videoServiceDialog.dismiss();
                        mPresenter.editDoctorConsultation(videoId, Constants.CONSULTATION_TYPE_VIDEO, DoubleUtil.getDouble(number), null);
                    }
                });
                videoServiceDialog.show();
                break;

            case R.id.tv_video_time:
                Intent intentTuTimeInsert = new Intent(WorkSettingActivity.this, ServiceTimeSettingActivity.class);
                intentTuTimeInsert.putExtra("consultationType", Constants.CONSULTATION_TYPE_PICTURE);
                startActivity(intentTuTimeInsert);
                break;
        }
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(WorkSettingActivity.this, "");
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
    }

    @Override
    public void getDoctorConsultationSuccess(DoctorConsultationInfo doctorConsultationInfo) {
        ToastUtil.showToast("infos" + doctorConsultationInfo);
    }

    @Override
    public void getDoctorConsultationInfoSuccess(DoctorConsultationInfo doctorConsultationInfo) {
        if (doctorConsultationInfo != null && doctorConsultationInfo.getRows() != null
                && doctorConsultationInfo.getRows().size() > 0) {
            for (int i = 0; i < doctorConsultationInfo.getRows().size(); i++) {
                DoctorConsultationInfo.RowsBean rowsBean = doctorConsultationInfo.getRows().get(i);
                if (rowsBean.getConsultationTypeId() == Constants.CONSULTATION_TYPE_PICTURE) {
                    // 图文类型
                    switchTu.setVisibility(View.VISIBLE);
                    if (rowsBean.getEnableFlag() == null || rowsBean.getEnableFlag() == 0) {
                        switchTu.setChecked(false);
                    } else {
                        switchTu.setChecked(true);
                    }
                    llTuSetting.setVisibility(View.VISIBLE);
                    rlTu.setVisibility(View.GONE);
                    tuId = rowsBean.getId();
                    if (!TextUtils.isEmpty(rowsBean.getPrice())) {
                        tvTuPrice.setText(rowsBean.getPrice() + "元");
                    }
                } else if (rowsBean.getConsultationTypeId() == Constants.CONSULTATION_TYPE_VIDEO) {
                    // 视频类型
                    switchVideo.setVisibility(View.VISIBLE);
                    if (rowsBean.getEnableFlag() == null || rowsBean.getEnableFlag() == 0) {
                        switchVideo.setChecked(false);
                    } else {
                        switchVideo.setChecked(true);
                    }
                    llVideoSetting.setVisibility(View.VISIBLE);
                    rlVideo.setVisibility(View.GONE);
                    videoId = rowsBean.getId();
                    if (!TextUtils.isEmpty(rowsBean.getPrice())) {
                        tvVideoPrice.setText(rowsBean.getPrice() + "元");
                    }
                }
            }
        } else {
            llTuSetting.setVisibility(View.GONE);
            llVideoSetting.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEditDoctorConsultationSuccess() {
        ToastUtil.showToast("设置成功");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // 重新请求接口
            mPresenter.getDoctorConsultationInfo();
        }
    }

}
