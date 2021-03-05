package com.ssh.shuoshi.ui.verified.face;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gyf.barlibrary.ImmersionBar;
import com.permissionx.guolindev.PermissionX;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.VerifiedBean;
import com.ssh.shuoshi.bean.ca.CAPhoneBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.util.PhoneUtil;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.ChooseVerifiedDialog;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.verified.h5.H5FirstActivity;
import com.ssh.shuoshi.view.pickview.builder.OptionsPickerBuilder;
import com.ssh.shuoshi.view.pickview.view.OptionsPickerView;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * CA签名 人脸识别
 */
public class VerifiedFaceActivity extends BaseActivity implements VerifiedFaceContract.View, View.OnClickListener {

    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.editTextName)
    TextView editTextName;
    @BindView(R.id.editTextIdCard)
    TextView editTextIdCard;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;

    @Inject
    VerifiedFacePresenter mPresenter;

    private String editName;
    private String card;
    private LoadingDialog mLoadingDialog;
    //处方ID
    private int prescriptionId;
    //认证方式
    private int authentication;
    @Inject
    UserStorage mUserStorage;
    private HisDoctorBean doctorInfo;

    @Override
    public int initContentView() {
        return R.layout.activity_verified_face;
    }

    @Override
    protected void initInjector() {
        DaggerVerifiedFaceComponent.builder()
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

        doctorInfo = mUserStorage.getDoctorInfo();

        String name = doctorInfo.getName();
        editTextName.setText(name);

        String idCard = doctorInfo.getIdCard();
        editTextIdCard.setText(idCard);
        buttonSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSubmit:
                PermissionX.init(VerifiedFaceActivity.this)
                        .permissions(Manifest.permission.CAMERA)
                        .request((allGranted, grantedList, deniedList) -> {
                            if (allGranted) {
                                chooseDialog();
                            } else {
                                ToastUtil.showToast("没有摄像头权限，无法进行人脸识别");
                            }
                        });
                break;
        }
    }

    private void chooseDialog() {
        editName = editTextName.getText().toString().trim();
        card = editTextIdCard.getText().toString().trim();
        if (TextUtils.isEmpty(editName)) {
            ToastUtil.showToast("请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(card)) {
            ToastUtil.showToast("请输入身份证号");
            return;
        }

        if (!PhoneUtil.isIdCard(card)) {
            ToastUtil.showToast("身份证格式错误");
            return;
        }

        List<VerifiedBean> list = new ArrayList<>();
        VerifiedBean vb1 = new VerifiedBean(1, "支付宝人脸识别");
        VerifiedBean vb2 = new VerifiedBean(2, "腾讯云人脸识别");
        list.add(vb1);
        list.add(vb2);
        showMethodDialog(list);
    }

    private void showMethodDialog(List<VerifiedBean> list) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            VerifiedBean verifiedBean = list.get(options1);
            showLoading();
            String accountId = doctorInfo.getAccountId();
            //没有ID，需要先创建个人账号
            if (TextUtils.isEmpty(accountId)) {
                createUserId(verifiedBean.getCode());
            } else {
                caFaceFirstOauth(verifiedBean.getCode());
            }
        })
                .setTitleText("请选择人脸识别方式")
                .setCancelText(" ")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(list);
        pvOptions.show();
    }

    //先创建个人账号
    private void createUserId(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", editName);
        map.put("idNo", card);
        mPresenter.caCreateUserId(map, id);
    }

    private void caFaceFirstOauth(int id) {
        //callbackUrl  自己定义
        Map<String, Object> map = new HashMap<>();
        map.put("name", editName);
        map.put("idNo", card);
        if (id == 1) {
            map.put("faceauthMode", "ZHIMACREDIT");
            map.put("callbackUrl", "esign://shuoshi/realBack");
        } else {
            map.put("faceauthMode", "TENCENT");
            map.put("callbackUrl", "https://www.baidu.com");
        }

        mPresenter.caFaceFirstOauth(map);
    }

    @Override
    public void caCreateUserIdSuccess(CAPhoneBean bean, int id) {
        mPresenter.getDoctorInfo();
        caFaceFirstOauth(id);
    }


    @Override
    public void caFaceFirstOauthSuccess(CAPhoneBean bean) {
        if (!bean.getCode().equals("0")) {
            ToastUtil.showToast(bean.getMessage());
            return;
        }
        String authUrl = bean.getData().getAuthUrl();
        Intent intent = new Intent(this, H5FirstActivity.class);
        intent.putExtra("url", authUrl);
        intent.putExtra("prescriptionId", prescriptionId);
        intent.putExtra("authentication", authentication);
        intent.putExtra("doctorName", editName);
        intent.putExtra("idCard", card);
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
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(VerifiedFaceActivity.this, "");
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
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}
