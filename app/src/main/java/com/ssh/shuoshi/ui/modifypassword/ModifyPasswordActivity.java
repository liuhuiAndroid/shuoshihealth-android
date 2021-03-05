package com.ssh.shuoshi.ui.modifypassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.TextureView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.inter.MyTextWatcher;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 修改手机号
 */
public class ModifyPasswordActivity extends BaseActivity implements ModifyPasswordContract.View {

    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.textViewGetCode)
    TextView textViewGetCode;

    @BindView(R.id.editTextPhone)
    EditText editTextPhone;
    @BindView(R.id.editTextCode)
    EditText editTextCode;

    @BindView(R.id.buttonSave)
    Button buttonSave;
    @BindView(R.id.textViewModifyText)
    TextView textViewModifyText;
    @BindView(R.id.textViewModifyTextPhone)
    TextView textViewModifyTextPhone;

    @Inject
    ModifyPasswordPresenter mPresenter;
    private LoadingDialog mLoadingDialog;
    private String mUuid;
    private String phone;

    @Override
    public int initContentView() {
        return R.layout.activity_modify_password;
    }

    @Override
    protected void initInjector() {
        DaggerModifyPasswordComponent.builder()
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

        String oldPhone = getIntent().getStringExtra("phone");
        textViewModifyTextPhone.setText("当前手机号：" + oldPhone);

        editTextPhone.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    buttonSave.setEnabled(false);
                    return;
                }

                if (s.toString().length() >= 11) {
                    buttonSave.setEnabled(true);
                } else {
                    buttonSave.setEnabled(false);
                }
            }
        });

        textViewGetCode.setOnClickListener(v -> {
            String phone = editTextPhone.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                ToastUtil.showToast("请输入新的手机号");
                return;
            }

            if (!phone.startsWith("1") || phone.length() != 11) {
                ToastUtil.showToast("手机号码格式错误");
                return;
            }

            if (phone.equals(oldPhone)) {
                ToastUtil.showToast("新号码不能和老号码相同");
                return;
            }

            mPresenter.getPhoneCode(phone);
        });

        buttonSave.setOnClickListener(v -> {
            phone = editTextPhone.getText().toString().trim();
            String code = editTextCode.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                ToastUtil.showToast("请输入新的手机号");
                return;
            }
            if (TextUtils.isEmpty(code)) {
                ToastUtil.showToast("请输入验证码");
                return;
            }
            showLoading();
            mPresenter.changeDoctorPhone(phone, code, mUuid);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(ModifyPasswordActivity.this, "");
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
        hideLoading();
        if (TextUtils.isEmpty(mUuid)) {
            ToastUtil.showToast("请先获取验证码");
            return;
        }
        loadError(throwable);

    }

    @Override
    public void refreshSmsCodeUi() {
        ToastUtil.showToast("获取验证码");
        int time = 60;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(time + 1)
                .map(aLong -> 60 - aLong)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    textViewGetCode.setEnabled(false);
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        textViewGetCode.setTextColor(getResources().getColor(R.color.orange));
                        textViewGetCode.setText("剩余" + aLong + "秒");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        textViewGetCode.setEnabled(true);
                        textViewGetCode.setClickable(true);
                        textViewGetCode.setTextColor(getResources().getColor(R.color.baSiGrey));
                        textViewGetCode.setText("获取验证码");
                    }
                });
    }

    @Override
    public void SmsCodeSuccess(String uuid) {
        this.mUuid = uuid;
    }

    @Override
    public void changeSuccess() {
        ToastUtil.showToast("修改成功");
        mPresenter.getDoctorInfo();
    }

    @Override
    public void getDoctorInfoSuccess() {
        Intent intent = new Intent();
        intent.putExtra("phone", phone);
        setResult(RESULT_OK, intent);
        finish();
    }
}
