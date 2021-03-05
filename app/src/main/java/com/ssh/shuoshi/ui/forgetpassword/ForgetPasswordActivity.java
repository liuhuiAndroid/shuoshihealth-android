package com.ssh.shuoshi.ui.forgetpassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.login.CodeOneLoginActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by hwt on 2020/12/9
 */
public class ForgetPasswordActivity extends BaseActivity implements ForgetPasswordContract.View {

    @Inject
    ForgetPasswordPresenter mPresenter;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.btn_next)
    Button btnNext;
    private LoadingDialog mLoadingDialog;

    @Override
    public int initContentView() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initInjector() {
        DaggerForgetPasswordComponent.builder()
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

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel = etPhone.getText().toString().trim();

                if (TextUtils.isEmpty(tel)) {
                    ToastUtil.showToast("手机号码为空");
                    return;
                }

//                if (!isMobile(tel)) {
//                    ToastUtil.showToast("手机号码格式错误");
//                    return;
//                }

                Intent intent = new Intent(ForgetPasswordActivity.this, CodeOneLoginActivity.class);
                intent.putExtra("phone", tel);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(ForgetPasswordActivity.this, "");
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
