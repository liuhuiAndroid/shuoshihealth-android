package com.ssh.shuoshi.ui.verified.choose;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.verified.face.VerifiedFaceActivity;
import com.ssh.shuoshi.ui.verified.phone.VerifiedPhoneActivity;
import com.ssh.shuoshi.view.title.UniteTitle;

import butterknife.BindView;

/**
 * CA签名选择方式
 */
public class VerifiedChooseActivity extends BaseActivity {

    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.verifiedByPhone)
    RelativeLayout verifiedByPhone;
    @BindView(R.id.verifiedByFace)
    RelativeLayout verifiedByFace;
    @BindView(R.id.imagePhoneTag)
    ImageView imagePhoneTag;
    @BindView(R.id.imageFaceTag)
    ImageView imageFaceTag;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;

    private int currentChoose = 1;
    private int prescriptionId;

    @Override
    public int initContentView() {
        return R.layout.activity_verified_choose;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initUiAndListener() {

        uniteTitle.setBackListener(-1, v -> finish());

        prescriptionId = getIntent().getIntExtra("prescriptionId", -1);

        verifiedByPhone.setOnClickListener(v -> {
            imagePhoneTag.setVisibility(View.VISIBLE);
            imageFaceTag.setVisibility(View.INVISIBLE);
            verifiedByPhone.setBackgroundResource(R.drawable.bg_verified_choose_origin);
            verifiedByFace.setBackgroundResource(R.drawable.bg_verified_choose_grey);
            currentChoose = 1;
        });

        verifiedByFace.setOnClickListener(v -> {
            imagePhoneTag.setVisibility(View.INVISIBLE);
            imageFaceTag.setVisibility(View.VISIBLE);
            verifiedByPhone.setBackgroundResource(R.drawable.bg_verified_choose_grey);
            verifiedByFace.setBackgroundResource(R.drawable.bg_verified_choose_origin);
            currentChoose = 2;
        });

        //认证方式，是手机还是人脸
        buttonSubmit.setOnClickListener(v -> {
            if (currentChoose == 1) {
                Intent intent = new Intent(VerifiedChooseActivity.this, VerifiedPhoneActivity.class);
                intent.putExtra("prescriptionId", prescriptionId);
                intent.putExtra("authentication", currentChoose);
                startActivityForResult(intent, 300);

            } else if (currentChoose == 2) {
                Intent intent = new Intent(VerifiedChooseActivity.this, VerifiedFaceActivity.class);
                intent.putExtra("prescriptionId", prescriptionId);
                intent.putExtra("authentication", currentChoose);
                startActivityForResult(intent, 300);
            }
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
}
