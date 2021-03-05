package com.ssh.shuoshi.ui.team.introduce;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by hwt on 2021/1/3
 */
public class TeamIntroduceActivity extends BaseActivity implements TeamIntroduceContract.View {

    @Inject
    TeamIntroducePresenter mPresenter;
    @BindView(R.id.title)
    UniteTitle title;
    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.btn_save)
    Button btnSave;

    private LoadingDialog mLoadingDialog;
    private boolean isUpdate;
    private int id;

    @Override
    public int initContentView() {
        return R.layout.activity_team_introduce;
    }

    @Override
    protected void initInjector() {
        DaggerTeamIntroduceComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);

        title.setBackListener(-1, v -> finish());

        Intent data = getIntent();
        String introduction = data.getStringExtra("introduction");
        id = data.getIntExtra("id", -1);
        isUpdate = data.getBooleanExtra("isUpdate", false);

        if (!TextUtils.isEmpty(introduction)) {
            etInfo.setText(introduction);
            tvHint.setText("还可输入" + (200 - etInfo.getText().toString().length()) + "字");
        }

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
            String info = etInfo.getText().toString().trim();
            if (TextUtils.isEmpty(info)) {
                ToastUtil.showToast("请输入内容");
                return;
            }
            if (isUpdate) {
                showLoading();
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("introduction", info);
                mPresenter.putDoctorTeam(map);
            } else {
                Intent intent = new Intent();
                intent.putExtra("info", info);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void putDoctorTeamSuccess(Integer bean) {
        setResult(RESULT_OK, getIntent());
        finish();
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(TeamIntroduceActivity.this, "");
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
