package com.ssh.shuoshi.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by hwt on 2020/12/9
 */
public class CodeTwoLoginActivity extends BaseActivity implements CodeTwoLoginContract.View {

    @Inject
    CodeTwoLoginPresenter mPresenter;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.iv_eye)
    ImageView ivEye;
    @BindView(R.id.btn_save)
    Button btnSave;
    private LoadingDialog mLoadingDialog;

    @Override
    public int initContentView() {
        return R.layout.activity_codetwo_login;
    }

    @Override
    protected void initInjector() {
        DaggerCodeTwoLoginComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void setStatusBar() {
        ImmersionBar.with(this).init();
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString().trim();

//                if (TextUtils.isEmpty(password)) {
//                    ToastUtil.showToast("手机号码为空");
//                    return;
//                }
//
//                if (!isPassword(password)) {
//                    ToastUtil.showToast("密码必须是6-16位数字和字母的组合");
//                    return;
//                }

                //开始保存
                Intent intent = new Intent(CodeTwoLoginActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(CodeTwoLoginActivity.this, "");
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
