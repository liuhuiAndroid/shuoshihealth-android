package com.ssh.shuoshi.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.LoginInfoBean;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.main.MainActivity;
import com.ssh.shuoshi.util.AppManager;
import com.tencent.mmkv.MMKV;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.local.ActionHelper;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * created by hwt on 2020/12/9
 */
public class CodeOneLoginActivity extends BaseActivity implements CodeOneLoginContract.View {

    @Inject
    CodeOneLoginPresenter mPresenter;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_time)
    TextView tvTime;
    private LoadingDialog mLoadingDialog;
    private int type;
    private String mUuid;

    @Override
    public int initContentView() {
        return R.layout.activity_codeone_login;
    }

    @Override
    protected void initInjector() {
        DaggerCodeOneLoginComponent.builder()
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
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        type = intent.getIntExtra("type", -1);

        tvPhone.setText("验证码已发送到" + phone);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPresenter.getPhoneCode(phone);

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getPhoneCode(phone);
            }
        });

        showSoftInputFromWindow(CodeOneLoginActivity.this, etPhone);

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 4) {
                    showLoading();
                    mPresenter.login(phone, editable.toString(), mUuid);
                }
            }
        });
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(CodeOneLoginActivity.this, "");
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
    public void refreshSmsCodeUi() {
        int time = 60;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(time + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return 60 - aLong;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        tvTime.setEnabled(false);
                        tvTime.setClickable(false);
                    }
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        tvTime.setTextColor(getResources().getColor(R.color.orange));
                        tvTime.setText(aLong + "秒");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        tvTime.setEnabled(true);
                        tvTime.setClickable(true);
                        tvTime.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                        tvTime.setText("重新获取");
                    }
                });
    }

    @Override
    public void SmsCodeSuccess(String uuid) {
        this.mUuid = uuid;
    }

    @Override
    public void loginSuccess(LoginInfoBean bean) {
        if (bean.getHisDoctor().getNewFlag() != null && bean.getHisDoctor().getNewFlag()) {
            MMKV.defaultMMKV().putBoolean("firstLogin", bean.getHisDoctor().getNewFlag());
        } else {
            MMKV.defaultMMKV().putBoolean("firstLogin", false);
        }
        ToastUtil.showToast("登录成功");

        Intent intent = new Intent(CodeOneLoginActivity.this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        AppManager.getAppManager().finishAllActivity();
    }


    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}
