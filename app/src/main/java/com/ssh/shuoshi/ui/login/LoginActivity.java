package com.ssh.shuoshi.ui.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.ssh.shuoshi.Constants;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.LoginEntity;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.inter.MyTextWatcher;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.main.MainActivity;
import com.ssh.shuoshi.ui.web.WebActivity;
import com.ssh.shuoshi.util.ClickUtils;
import com.ssh.shuoshi.util.SPUtil;

import javax.inject.Inject;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

/**
 * created by hwt on 2020/12/8
 */
public class LoginActivity extends BaseActivity implements LoginContract.View, View.OnClickListener {

    @Inject
    LoginPresenter mPresenter;
    @Inject
    SPUtil mSPUtil;
    @Inject
    UserStorage mUserStorage;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.cb_agree)
    CheckBox cbAgree;
    @BindView(R.id.tv_agree)
    TextView tvAgree;
    @BindView(R.id.btn_code_login)
    Button btnCodeLogin;
    @BindView(R.id.tv_law)
    TextView tvLaw;
    @BindView(R.id.img_back)
    ImageView imgBack;
    private LoadingDialog mLoadingDialog;

    @Override
    public int initContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initInjector() {
        DaggerLoginComponent.builder()
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
        tvAgree.setOnClickListener(v -> {
            boolean checked = cbAgree.isChecked();
            cbAgree.setChecked(!checked);
        });
        btnCodeLogin.setOnClickListener(this);
        tvLaw.setOnClickListener(this);
        imgBack.setOnClickListener(v -> finish());

        etPhone.addTextChangedListener((MyTextWatcher) s -> {
            if (TextUtils.isEmpty(s.toString())) {
                btnCodeLogin.setEnabled(false);
                return;
            }

            if (s.toString().length() >= 11) {
                btnCodeLogin.setEnabled(true);
            } else {
                btnCodeLogin.setEnabled(false);
            }
        });
    }

    @Override
    public void loginSuccess(LoginEntity loginEntity) {
        if (loginEntity.getDoctorBean().getNewFlag() != null && loginEntity.getDoctorBean().getNewFlag()) {
            JPushInterface.setAlias(LoginActivity.this,
                    Integer.parseInt((System.currentTimeMillis() + "").substring(5)),
                    loginEntity.getDoctorBean().getId() + "");
        } else {
        }
        openActivity(MainActivity.class);
        finish();
    }

    @Override
    public void showUserNameError(String message) {

    }

    @Override
    public void showPassWordError(String message) {

    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(LoginActivity.this, "");
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
//        loadError(throwable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_law:
                Intent intent2 = new Intent(LoginActivity.this, WebActivity.class);
                intent2.putExtra("url", Constants.WEB_002);
                startActivity(intent2);
                break;
            case R.id.btn_code_login:
                if (ClickUtils.isFastClick()) {
                    // 进行点击事件后的逻辑操作
                    String tel = etPhone.getText().toString().trim();
                    if (TextUtils.isEmpty(tel)) {
                        ToastUtil.showToast("手机号码为空");
                        return;
                    }

                    if (!tel.startsWith("1") || tel.length() != 11) {
                        ToastUtil.showToast("手机号码格式错误");
                        return;
                    }

                    if (!cbAgree.isChecked()) {
                        ToastUtil.showToast("请仔细阅读并勾选相关协议与政策");
                        return;
                    }

                    Intent intent = new Intent(LoginActivity.this, CodeOneLoginActivity.class);
                    intent.putExtra("phone", tel);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                }
                break;
        }
    }

}
