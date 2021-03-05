package com.ssh.shuoshi.ui.verified.phoneaspiration;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.ca.CAPhoneBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.verified.submit.PrescriptionSubmitCheckActivity;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 手机认证  有意愿
 * created by hwt on 2020/12/27
 */
public class PhoneAspirationActivity extends BaseActivity implements PhoneAspirationContract.View,
        View.OnClickListener {

    @Inject
    PhoneAspirationPresenter mPresenter;
    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.textPhone)
    EditText textPhone;
    @BindView(R.id.editCode)
    EditText editCode;
    @BindView(R.id.textCode)
    TextView textCode;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;

    @Inject
    UserStorage mUserStorage;

    private LoadingDialog mLoadingDialog;

    //获取处方的数据,处方ID
    private int prescriptionId;
//    //展示西，中界面
//    private String perscriptionTypeId;
    //    private String mobileNo;
    private String accountId;
    private HisPrescriptionDtoBean mBean;

    @Override
    public int initContentView() {
        return R.layout.activity_phone_aspiration;
    }

    @Override
    protected void initInjector() {
        DaggerPhoneAspirationComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);

        uniteTitle.setBackListener(-1, v -> finish());
        HisDoctorBean doctorInfo = mUserStorage.getDoctorInfo();
        accountId = doctorInfo.getAccountId();
        textCode.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);

        Intent intent = getIntent();
        prescriptionId = intent.getIntExtra("prescriptionId", -1);
//        perscriptionTypeId = intent.getStringExtra("perscriptionTypeId");
        mBean = (HisPrescriptionDtoBean) intent.getSerializableExtra("historyDrugsBean");
//        mobileNo = intent.getStringExtra("mobileNo");
//        if (!TextUtils.isEmpty(mobileNo)) {
//
//        }
        textPhone.setText(doctorInfo.getAuthPhone());
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(PhoneAspirationActivity.this, "");
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
        if (throwable.getMessage().equals("认证码无效")){
            ToastUtil.showToast("验证码无效");
        } else {
            loadError(throwable);
        }
        hideLoading();
    }

    @Override
    public void caPhoneSecondCodeSuccess(CAPhoneBean bean) {
        refreshSmsCodeUi();
        ToastUtil.showToast("短信已发送");
    }

    //验证通过
    @Override
    public void caPhoneSecondVerifySuccess(CAPhoneBean bean) {
        mPresenter.getDoctorInfo();
    }

    @Override
    public void setDoctorInfoSuccess() {
        Intent intent = new Intent(PhoneAspirationActivity.this, PrescriptionSubmitCheckActivity.class);
//        intent.putExtra("perscriptionTypeId", perscriptionTypeId);
        intent.putExtra("historyDrugsBean", mBean);
        intent.putExtra("prescriptionId", prescriptionId);
        startActivityForResult(intent, 300);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textCode:
                String phone = textPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showToast("手机号码为空");
                    return;
                }

                if (!phone.startsWith("1") || phone.length() != 11) {
                    ToastUtil.showToast("手机号码格式错误");
                    return;
                }

                showLoading();
                Map<String, Object> map = new HashMap<>();
                map.put("accountId", accountId);
                map.put("mobile", phone);
                mPresenter.caPhoneSecondCode(map);
                break;

            case R.id.buttonSubmit:
                String phone2 = textPhone.getText().toString().trim();
                String code = editCode.getText().toString().trim();

                if (TextUtils.isEmpty(phone2)) {
                    ToastUtil.showToast("手机号码为空");
                    return;
                }

                if (!phone2.startsWith("1") || phone2.length() != 11) {
                    ToastUtil.showToast("手机号码格式错误");
                    return;
                }

                if (TextUtils.isEmpty(code)) {
                    ToastUtil.showToast("请输入验证码");
                    return;
                }

                showLoading();
                Map<String, Object> map2 = new HashMap<>();
                map2.put("accountId", accountId);
                map2.put("mobileNo", phone2);
                map2.put("authcode", code);
                map2.put("prescriptionId", prescriptionId);
                mPresenter.caPhoneSecondVerify(map2);
                break;
        }
    }

    public void refreshSmsCodeUi() {
        int time = 60;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(time + 1)
                .map(aLong -> 60 - aLong)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    textCode.setEnabled(false);
                    textCode.setClickable(false);
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        textCode.setText(aLong + " S");
                        textCode.setTextColor(Color.parseColor("#FF824D"));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        textCode.setEnabled(true);
                        textCode.setClickable(true);
                        textCode.setText("获取验证码");
                        textCode.setTextColor(Color.parseColor("#191919"));
                    }
                });
    }
}
