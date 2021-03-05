package com.ssh.shuoshi.ui.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.eventbus.ChangeStateEvent;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.model.TRTCCalling;
import com.ssh.shuoshi.model.impl.TRTCCallingImpl;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.about.AboutActivity;
import com.ssh.shuoshi.ui.dialog.CommonDialog;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.modifypassword.ModifyPasswordActivity;
import com.ssh.shuoshi.ui.team.doctorteam.MyDoctorTeamActivity;
import com.ssh.shuoshi.view.title.UniteTitle;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import timber.log.Timber;

/**
 * created by hwt on 2020/12/12
 */
public class SettingActivity extends BaseActivity implements SettingContract.View, View.OnClickListener {

    @BindView(R.id.title)
    UniteTitle title;
    @BindView(R.id.tv_tu)
    TextView tvTu;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.rl_tu)
    RelativeLayout rlTu;
    @BindView(R.id.rl_about)
    RelativeLayout rlAbout;
    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.btn_exit)
    TextView btnExit;
    private LoadingDialog mLoadingDialog;
    @Inject
    SettingPresenter mPresenter;

    @Inject
    UserStorage mUserStorage;
    private HisDoctorBean doctorInfo;

    @Override
    public int initContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initInjector() {
        DaggerSettingComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        doctorInfo = mUserStorage.getDoctorInfo();
        if (doctorInfo != null) {
            tvPhone.setText(doctorInfo.getPhone());
        }

        title.setBackListener(-1, v -> finish());
        rlTu.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        rlAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_about:
                Intent intentAbout = new Intent(SettingActivity.this, AboutActivity.class);
                startActivity(intentAbout);
                break;
            case R.id.rl_tu:
                if (!TextUtils.isEmpty(tvPhone.getText().toString())) {
                    Intent intent = new Intent(SettingActivity.this, ModifyPasswordActivity.class);
                    intent.putExtra("phone", tvPhone.getText().toString());
                    startActivityForResult(intent, 200);
                }
                break;

            case R.id.btn_exit:
                if (doctorInfo == null) {
                    ToastUtil.showToast("请先登录");
                    return;
                }
                CommonDialog commonDialog = new CommonDialog(SettingActivity.this, "退出登录后无法接收问诊消息提醒等，确定退出登录吗？",R.style.dialog_physician_certification);
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
                        TRTCCallingImpl.sharedInstance(SettingActivity.this).logout(new TRTCCalling.ActionCallBack() {

                            @Override
                            public void onError(int code, String msg) {
                                Timber.i("onError code");
                            }

                            @Override
                            public void onSuccess() {
                                Timber.i("onSuccess code");
                            }
                        });
                        mUserStorage.logout();
                        MMKV.defaultMMKV().putBoolean("firstLogin", false);
                        EventBus.getDefault().post(new ChangeStateEvent());
                        JPushInterface.deleteAlias(SettingActivity.this,
                                Integer.parseInt((System.currentTimeMillis() + "").substring(5)));
                        finish();
                        commonDialog.dismiss();
                    }
                });
                commonDialog.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {
                String phone = data.getStringExtra("phone");
                tvPhone.setText(phone);
            }
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
