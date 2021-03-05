package com.ssh.shuoshi.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.forgetpassword.ForgetPasswordActivity;
import com.ssh.shuoshi.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by hwt on 2020/12/9
 */
public class PasswordLoginActivity extends BaseActivity implements PasswordLoginContract.View, View.OnClickListener {

    @Inject
    PasswordLoginPresenter mPresenter;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.cb_agree)
    CheckBox cbAgree;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_phone_login)
    TextView tvPhoneLogin;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.tv_law)
    TextView tvLaw;
    private LoadingDialog mLoadingDialog;

    @Override
    public int initContentView() {
        return R.layout.password_login;
    }

    @Override
    protected void initInjector() {
        DaggerPasswordLoginComponent.builder()
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

        tvPhoneLogin.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
        tvLaw.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_phone_login:
                finish();
                break;
            case R.id.tv_forget_password:
                openActivity(ForgetPasswordActivity.class);
                break;
            case R.id.tv_law:
                ToastUtil.showToast("跳转了新界面");
                break;
            case R.id.btn_login:
                String account = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(account)) {
                    ToastUtil.showToast("手机号码不能为空");
                    return;
                }

//                if (!isMobile(account)) {
//                    ToastUtil.showToast("手机号码格式错误");
//                    return;
//                }

                if (TextUtils.isEmpty(password)) {
                    ToastUtil.showToast("密码不能为空");
                    return;
                }

                if (!cbAgree.isChecked()) {
                    ToastUtil.showToast("请勾选按钮");
                    return;
                }

                Intent intent = new Intent(PasswordLoginActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;
        }
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(PasswordLoginActivity.this, "");
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
