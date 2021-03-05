package com.ssh.shuoshi.ui.fragment2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.eventbus.ChangeStateEvent;
import com.ssh.shuoshi.eventbus.RefreshInfoEvent;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseFragment;
import com.ssh.shuoshi.ui.authority.one.AuthorityOneActivity;
import com.ssh.shuoshi.ui.dialog.PhysicianCertificationDialog;
import com.ssh.shuoshi.ui.doctorauthentication.DoctorAuthenticationActivity;
import com.ssh.shuoshi.ui.headimg.DoctorHeadActivity;
import com.ssh.shuoshi.ui.login.LoginActivity;
import com.ssh.shuoshi.ui.main.MainComponent;
import com.ssh.shuoshi.ui.myorder.MyOrderActivity;
import com.ssh.shuoshi.ui.myprescription.main.MyPrescriptionActivity;
import com.ssh.shuoshi.ui.setting.SettingActivity;
import com.ssh.shuoshi.ui.team.team.MyTeamActivity;
import com.tencent.mmkv.MMKV;
import com.tencent.qcloud.tim.uikit.component.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * created by hwt on 2020/12/8
 */
public class Fragment2 extends BaseFragment implements Fragment2Contract.View, View.OnClickListener {

    @BindView(R.id.mImageAvatar)
    CircleImageView mImageAvatar;
    @BindView(R.id.textName)
    TextView textName;
    @BindView(R.id.textDepartment)
    TextView textDepartment;
    @BindView(R.id.textJobTitle)
    TextView textJobTitle;
    @BindView(R.id.textHospitalName)
    TextView textHospitalName;
    @BindView(R.id.constraintLayoutSetting)
    ConstraintLayout constraintLayoutSetting;
    @Inject
    UserStorage mUserStorage;
    @Inject
    Fragment2Presenter mPresenter;
    @BindView(R.id.img_edit)
    ImageView imgEdit;
    @BindView(R.id.rl_have_info)
    RelativeLayout rlHaveInfo;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.rl_no_info)
    RelativeLayout rlNoInfo;
    @BindView(R.id.viewBlank1)
    View viewBlank1;
    @BindView(R.id.constraintLayoutOrder)
    ConstraintLayout constraintLayoutOrder;
    @BindView(R.id.constraintLayoutPrescription)
    ConstraintLayout constraintLayoutPrescription;
    @BindView(R.id.constraintLayoutExpert)
    ConstraintLayout constraintLayoutExpert;
    @BindView(R.id.viewDividingLine2)
    View viewDividingLine2;
    @BindView(R.id.constraintLayoutRecommend)
    ConstraintLayout constraintLayoutRecommend;
    @BindView(R.id.viewDividingLine3)
    View viewDividingLine3;
    @BindView(R.id.tv_team)
    TextView tvTeam;
    @BindView(R.id.tv_apply_state)
    TextView tvApplyState;
    @BindView(R.id.rl_no_state)
    RelativeLayout rlNoState;
    @BindView(R.id.textHint)
    TextView textHint;
    private HisDoctorBean doctorInfo;
    //用户头像
    private String mHeadPath;

    public static BaseFragment newInstance() {
        Fragment2 fragment2 = new Fragment2();
        return fragment2;
    }

    @Override
    public void initInjector() {
        getComponent(MainComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_2;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        doctorInfo = mUserStorage.getDoctorInfo();
        if (TextUtils.isEmpty(mUserStorage.getToken())) {
            rlNoInfo.setVisibility(View.VISIBLE);
            rlHaveInfo.setVisibility(View.GONE);
            rlNoState.setVisibility(View.GONE);
        }
        if (doctorInfo != null) {
            if (doctorInfo.getApprovalState() == 0) {
                tvApplyState.setText("请进行资质认证");
                textHint.setText("立即开启您的线上工作室吧～");
                rlNoState.setVisibility(View.VISIBLE);
                rlNoInfo.setVisibility(View.GONE);
                rlHaveInfo.setVisibility(View.GONE);
            } else if (doctorInfo.getApprovalState() == 1) {
                tvApplyState.setText("认证审核中 ");
                textHint.setText("请耐心等待资质认证审核哦~");
                rlNoState.setVisibility(View.VISIBLE);
                rlNoInfo.setVisibility(View.GONE);
                rlHaveInfo.setVisibility(View.GONE);
            } else if (doctorInfo.getApprovalState() == 3) {
                tvApplyState.setText("认证审核不通过");
                textHint.setText("请修改资料后重新提交");
                rlNoState.setVisibility(View.VISIBLE);
                rlNoInfo.setVisibility(View.GONE);
                rlHaveInfo.setVisibility(View.GONE);
            } else {
                rlNoState.setVisibility(View.GONE);
                rlNoInfo.setVisibility(View.GONE);
                rlHaveInfo.setVisibility(View.VISIBLE);
                init(doctorInfo);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        doctorInfo = mUserStorage.getDoctorInfo();
        if (TextUtils.isEmpty(mUserStorage.getToken())) {
            rlNoInfo.setVisibility(View.VISIBLE);
            rlHaveInfo.setVisibility(View.GONE);
            rlNoState.setVisibility(View.GONE);
        }
        if (doctorInfo != null) {
            if (doctorInfo.getApprovalState() == 0) {
                tvApplyState.setText("请进行资质认证");
                textHint.setText("立即开启您的线上工作室吧～");
                rlNoState.setVisibility(View.VISIBLE);
                rlNoInfo.setVisibility(View.GONE);
                rlHaveInfo.setVisibility(View.GONE);
            } else if (doctorInfo.getApprovalState() == 1) {
                tvApplyState.setText("认证审核中 ");
                textHint.setText("请耐心等待资质认证审核哦~");
                rlNoState.setVisibility(View.VISIBLE);
                rlNoInfo.setVisibility(View.GONE);
                rlHaveInfo.setVisibility(View.GONE);
            } else if (doctorInfo.getApprovalState() == 3) {
                tvApplyState.setText("认证审核不通过");
                textHint.setText("请修改资料后重新提交");
                rlNoState.setVisibility(View.VISIBLE);
                rlNoInfo.setVisibility(View.GONE);
                rlHaveInfo.setVisibility(View.GONE);
            } else {
                rlNoState.setVisibility(View.GONE);
                rlNoInfo.setVisibility(View.GONE);
                rlHaveInfo.setVisibility(View.VISIBLE);
                init(doctorInfo);
            }
        }
    }

    private void init(HisDoctorBean bean) {
        if (bean != null) {
            textName.setText(bean.getName());
            textDepartment.setText(bean.getHisSysDeptName());
            textJobTitle.setText(TextUtils.isEmpty(bean.getDoctorTitleName()) ? "未填写" : bean.getDoctorTitleName());
            textHospitalName.setText(bean.getHospitalName());
        }
    }

    @Override
    public void initUI(View view) {
        mPresenter.attachView(this);
        EventBus.getDefault().register(this);
        if (!TextUtils.isEmpty(mUserStorage.getToken())) {
            mPresenter.getDoctorInfo();
        }
        rlNoState.setOnClickListener(this);
        rlNoInfo.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
        constraintLayoutSetting.setOnClickListener(this);
        constraintLayoutOrder.setOnClickListener(this);
        constraintLayoutPrescription.setOnClickListener(this);
        constraintLayoutExpert.setOnClickListener(this);
        constraintLayoutRecommend.setOnClickListener(this);
        mImageAvatar.setOnClickListener(this);
        rlHaveInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_no_info:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.rl_have_info:
            case R.id.img_edit:
                startActivity(new Intent(getActivity(), DoctorAuthenticationActivity.class));
                break;

            //修改头像
            case R.id.mImageAvatar:
                if (eachHandle()) {
                    return;
                }
                Intent intent1 = new Intent(getActivity(), DoctorHeadActivity.class);
                if (TextUtils.isEmpty(mHeadPath)) {
                    intent1.putExtra("headPath", false);
                } else {
                    intent1.putExtra("headPath", true);
                }
                startActivityForResult(intent1, 200);
                break;

            // 我的订单
            case R.id.constraintLayoutOrder:
                if (eachHandle()) {
                    return;
                }
                startActivity(new Intent(getActivity(), MyOrderActivity.class));
                break;
            //我的处方
            case R.id.constraintLayoutPrescription:
                if (eachHandle()) {
                    return;
                }
                startActivity(new Intent(getActivity(), MyPrescriptionActivity.class));
                break;
            //我的专家团队
            case R.id.constraintLayoutExpert:
                if (eachHandle()) {
                    return;
                }
                startActivity(new Intent(getActivity(), MyTeamActivity.class));
                break;

            //推荐同行
            case R.id.constraintLayoutRecommend:

                break;

            //设置
            case R.id.constraintLayoutSetting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;

            case R.id.rl_no_state:
//                if (doctorInfo.getApprovalState() == 0) {
//                    showPhysicianCertificationDialog();
//                }
                eachHandle();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRate(RefreshInfoEvent event) {
        mPresenter.getDoctorInfo();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200:
                    mPresenter.getDoctorInfo();
                    EventBus.getDefault().post(new RefreshInfoEvent());
                    break;
            }
        }
    }

    //针对不同状态做处理
    private boolean eachHandle() {
        if (TextUtils.isEmpty(mUserStorage.getToken())) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            return true;
        }

        if (doctorInfo.getApprovalState() == 0 || doctorInfo.getApprovalState() == 3) {
            showPhysicianCertificationDialog(doctorInfo.getApprovalState());
            return true;
        }

        if (doctorInfo.getApprovalState() == 1) {
            ToastUtil.showToast("您提交的资质认证材料，正在审核中，请耐心等耐。");
            return true;
        }

        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventChange(ChangeStateEvent event) {
        Glide.with(Objects.requireNonNull(getActivity())).load(getResources().getDrawable(R.drawable.default_img))
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(mImageAvatar);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 显示医师认证弹框
     */
    private void showPhysicianCertificationDialog(int approvalState) {
        PhysicianCertificationDialog dialog = new PhysicianCertificationDialog(
                getActivity(), R.style.dialog_physician_certification);

        dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        dialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
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
                dialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });

        dialog.setBtnGoClick(v -> {
            Intent intent = new Intent(getActivity(), AuthorityOneActivity.class);
            if (approvalState == 3) {
                intent.putExtra("isShow", true);
            }
            startActivity(intent);
            dialog.dismiss();

        });
        dialog.show();
    }

    @Override
    public void initData() {

    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
    }

    @Override
    public void getDoctorInfoSuccess(HisDoctorBean bean) {
        if (bean != null && bean.getHeadImg() != null) {
            String[] photoPath = new String[]{bean.getHeadImg()};
            mPresenter.getImagePath(photoPath);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void imgDownload(List<String> bean) {
        if (bean == null || bean.size() == 0) {
            return;
        }
        mHeadPath = bean.get(0);
        MMKV.defaultMMKV().putString("headImg", mHeadPath);
        Glide.with(Objects.requireNonNull(getActivity())).load(mHeadPath)
                .placeholder(getResources().getDrawable(R.drawable.default_img))
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(mImageAvatar);
    }

}
