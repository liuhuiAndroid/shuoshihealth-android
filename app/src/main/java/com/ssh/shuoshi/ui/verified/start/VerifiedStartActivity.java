package com.ssh.shuoshi.ui.verified.start;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ssh.shuoshi.Constants;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.login.LoginActivity;
import com.ssh.shuoshi.ui.verified.choose.VerifiedChooseActivity;
import com.ssh.shuoshi.ui.web.WebActivity;
import com.ssh.shuoshi.view.title.UniteTitle;

import butterknife.BindView;

/**
 * CA签名实名认证
 */
public class VerifiedStartActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    @BindView(R.id.cb_agree)
    CheckBox cbAgree;
    @BindView(R.id.tv_law)
    TextView tvLaw;
    private int prescriptionId;

    @Override
    public int initContentView() {
        return R.layout.activity_verified_start;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initUiAndListener() {

        uniteTitle.setBackListener(-1, v -> finish());
        buttonSubmit.setOnClickListener(this);
        tvLaw.setOnClickListener(this);

        prescriptionId = getIntent().getIntExtra("prescriptionId", -1);

        cbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonSubmit.setEnabled(true);
                } else {
                    buttonSubmit.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSubmit:
                if (!cbAgree.isChecked()) {
                    ToastUtil.showToast("请勾选按钮");
                    return;
                }
                Intent intent = new Intent(VerifiedStartActivity.this, VerifiedChooseActivity.class);
                intent.putExtra("prescriptionId", prescriptionId);
                startActivityForResult(intent, 300);
                break;
            case R.id.tv_law:
                Intent intent2 = new Intent(VerifiedStartActivity.this, WebActivity.class);
                intent2.putExtra("url", Constants.WEB_003);
                startActivity(intent2);
                break;
        }
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

}
