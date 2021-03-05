package com.ssh.shuoshi.ui.verified.phone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gyf.barlibrary.ImmersionBar;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.ca.CAPhoneBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.util.PhoneUtil;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.dialog.VerifiedPhoneDialog;
import com.ssh.shuoshi.ui.verified.choose.AspirationVerifiedChooseActivity;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * CA签名 手机认证
 */
public class VerifiedPhoneActivity extends BaseActivity implements VerifiedPhoneContract.View, View.OnClickListener {

    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @Inject
    VerifiedPhonePresenter mPresenter;
    @BindView(R.id.etName)
    TextView etName;
    @BindView(R.id.etIdCard)
    EditText etIdCard;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.buttonNext)
    Button buttonNext;

    @Inject
    UserStorage mUserStorage;

    private LoadingDialog mLoadingDialog;
    private String mFlowId;
    private String name;
    private String idCard;
    private String tel;
    //处方ID
    private int prescriptionId;
    //认证方式
    private int authentication;
    private HisDoctorBean doctorInfo;
    private VerifiedPhoneDialog dialog;

    @Override
    public int initContentView() {
        return R.layout.activity_verified_phone;
    }

    @Override
    protected void initInjector() {
        DaggerVerifiedPhoneComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void setStatusBar() {
        ImmersionBar.with(this).statusBarColor(R.color.white)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true).init();
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        uniteTitle.setBackListener(-1, v -> finish());
        prescriptionId = getIntent().getIntExtra("prescriptionId", -1);
        authentication = getIntent().getIntExtra("authentication", -1);
        buttonNext.setOnClickListener(this);

        doctorInfo = mUserStorage.getDoctorInfo();
        String name = doctorInfo.getName();
        etName.setText(name);

        String idCard = doctorInfo.getIdCard();
        String phone = doctorInfo.getPhone();
        etIdCard.setText(idCard);
        etPhone.setText(phone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNext:
                name = etName.getText().toString().trim();
                idCard = etIdCard.getText().toString().trim();
                tel = etPhone.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    ToastUtil.showToast("姓名不能为空");
                    return;
                }

                if (TextUtils.isEmpty(idCard)) {
                    ToastUtil.showToast("身份证号不能为空");
                    return;
                }

                if (!PhoneUtil.isIdCard(idCard)) {
                    ToastUtil.showToast("身份证格式错误");
                    return;
                }

                if (TextUtils.isEmpty(tel)) {
                    ToastUtil.showToast("手机号码为空");
                    return;
                }

                if (!tel.startsWith("1") || tel.length() != 11) {
                    ToastUtil.showToast("手机号码格式错误");
                    return;
                }

                showLoading();
                String accountId = doctorInfo.getAccountId();
                //如果没有ID，需要先创建个人账号
                if (TextUtils.isEmpty(accountId)) {
                    createUserId();
                } else {
                    getPhoneCode();
                }
                break;
        }
    }

    //先创建个人账号
    private void createUserId() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("idNo", idCard);
        map.put("mobile", tel);
        mPresenter.caCreateUserId(map);
    }

    //获取手机验证码
    public void getPhoneCode() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("idNo", idCard);
        map.put("mobileNo", tel);
        mPresenter.caGetPhoneCode(map);
    }

    @Override
    public void caGetPhoneCodeSuccess(CAPhoneBean bean) {
        if (!bean.getCode().equals("0")) {
            ToastUtil.showToast(bean.getMessage());
            return;
        }
        mFlowId = bean.getData().getFlowId();
        dialog = new VerifiedPhoneDialog(this, etPhone.getText().toString());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnItemClickListener(new VerifiedPhoneDialog.ItemClickListener() {
            @Override
            public void cancel() {
                dialog.dismiss();
            }

            @Override
            public void submit(String code) {
                showLoading();
                caCodeOauth(code);
            }
        });
        dialog.show();
    }

    //创建个人账号成功，请求验证码
    @Override
    public void caCreateUserIdSuccess(CAPhoneBean bean) {
        getPhoneCode();
    }

    @Override
    public void getDoctorInfoSuccess() {
        if (dialog != null) {
            dialog.dismiss();
        }
        Intent intent = new Intent(VerifiedPhoneActivity.this, AspirationVerifiedChooseActivity.class);
        intent.putExtra("prescriptionId", prescriptionId);
        intent.putExtra("mobileNo", tel);
        intent.putExtra("authentication", authentication);
        startActivityForResult(intent, 300);
    }

    @Override
    public void caCodeOauthSuccess(CAPhoneBean bean) {
        if (!bean.getCode().equals("0")) {
            ToastUtil.showToast(bean.getMessage());
            return;
        }

        //更新个人信息
        mPresenter.getDoctorInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 300) {
            Intent intent = new Intent();
            intent.putExtra("prescriptionId", prescriptionId);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    //验证码审核
    private void caCodeOauth(String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("flowId", mFlowId);
        map.put("authcode", code);
        mPresenter.caCodeOauth(map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(VerifiedPhoneActivity.this, "");
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
        hideLoading();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
