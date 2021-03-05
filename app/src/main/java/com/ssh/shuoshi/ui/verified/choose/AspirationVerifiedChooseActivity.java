package com.ssh.shuoshi.ui.verified.choose;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.verified.sign.PrescriptionSignActivity;
import com.ssh.shuoshi.view.title.UniteTitle;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选择意愿认证服务
 * created by hwt on 2020/12/26
 */
public class AspirationVerifiedChooseActivity extends BaseActivity {
    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.imageNoneTag)
    ImageView imageNoneTag;
    @BindView(R.id.rl_aspiration_none)
    RelativeLayout rlAspirationNone;
    @BindView(R.id.imageHaveTag)
    ImageView imageHaveTag;
    @BindView(R.id.rl_aspiration_have)
    RelativeLayout rlAspirationHave;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;

    private int currentChoose = 1;
    private int prescriptionId;
    private String mobileNo;
    private String doctorName;
    private String idCard;

    //认证方式   1是手机，2是人脸
    private int authentication;

    @Override
    public int initContentView() {
        return R.layout.activity_aspiration_verified_choose;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initUiAndListener() {
        uniteTitle.setBackListener(-1, v -> finish());

        prescriptionId = getIntent().getIntExtra("prescriptionId", -1);
        mobileNo = getIntent().getStringExtra("mobileNo");
        doctorName = getIntent().getStringExtra("doctorName");
        idCard = getIntent().getStringExtra("idCard");
        authentication = getIntent().getIntExtra("authentication", -1);

        rlAspirationNone.setOnClickListener(v -> {
            imageNoneTag.setVisibility(View.VISIBLE);
            imageHaveTag.setVisibility(View.INVISIBLE);
            rlAspirationNone.setBackgroundResource(R.drawable.bg_verified_choose_origin);
            rlAspirationHave.setBackgroundResource(R.drawable.bg_verified_choose_grey);
            currentChoose = 1;
        });

        rlAspirationHave.setOnClickListener(v -> {
            imageNoneTag.setVisibility(View.INVISIBLE);
            imageHaveTag.setVisibility(View.VISIBLE);
            rlAspirationNone.setBackgroundResource(R.drawable.bg_verified_choose_grey);
            rlAspirationHave.setBackgroundResource(R.drawable.bg_verified_choose_origin);
            currentChoose = 2;
        });

        buttonSubmit.setOnClickListener(v -> {
            Intent intent = new Intent(this, PrescriptionSignActivity.class);
            intent.putExtra("type", currentChoose);
            intent.putExtra("prescriptionId", prescriptionId);
            intent.putExtra("mobileNo", mobileNo);
            intent.putExtra("authentication", authentication);
            intent.putExtra("doctorName", doctorName);
            intent.putExtra("idCard", idCard);
            startActivityForResult(intent, 300);
        });
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
