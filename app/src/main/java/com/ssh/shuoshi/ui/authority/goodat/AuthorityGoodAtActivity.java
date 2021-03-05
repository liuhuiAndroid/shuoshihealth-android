package com.ssh.shuoshi.ui.authority.goodat;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.eventbus.ChangeEvent;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.view.title.UniteTitle;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * created by hwt on 2020/12/10
 */
public class AuthorityGoodAtActivity extends BaseActivity implements AuthorityGoodAtContract.View {

    @BindView(R.id.title)
    UniteTitle title;
    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.btn_save)
    Button btnSave;
    @Inject
    AuthorityGoodAtPresenter mPresenter;
    private String type;
    private String info;
    private LoadingDialog mLoadingDialog;

    @Override
    public int initContentView() {
        return R.layout.activity_authority_good;
    }

    @Override
    protected void initInjector() {
        DaggerAuthorityGoodAtComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void setStatusBar() {
        super.setStatusBar();
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        type = getIntent().getStringExtra("type");
        String goodAt = getIntent().getStringExtra("info");
        etInfo.setText(goodAt);

        tvHint.setText("还可输入" + (200 - etInfo.getText().toString().length()) + "字");

        title.setBackListener(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable == null) {
                    tvHint.setText("还可输入200字");
                } else {
                    tvHint.setText("还可输入" + (200 - editable.length()) + "字");
                }

            }
        });

        btnSave.setOnClickListener(v -> {
            info = etInfo.getText().toString().trim();
            if (TextUtils.isEmpty(info)) {
                ToastUtil.showToast("输入内容不能为空");
                return;
            }

            Map<String, Object> mData = new HashMap<>();
            mData.put("goodAt", info);
            if (type.equals("1")) {
                Intent intent = new Intent();
                intent.putExtra("good", info);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                //请求网络，更新状态,重新请求
                showLoading();
                mPresenter.putDoctorInfo(mData);
            }
        });
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(AuthorityGoodAtActivity.this, "");
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
        loadError(throwable);
    }

    @Override
    public void uploadInfoSuccess(String bean) {
        ToastUtil.showToast("保存成功");
        EventBus.getDefault().post(new ChangeEvent(1));
        Intent intent = new Intent();
        intent.putExtra("good", info);
        setResult(RESULT_OK, intent);
        finish();
    }
}
