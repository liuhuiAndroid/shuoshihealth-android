package com.ssh.shuoshi.ui.verified.result;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.verified.choose.AspirationVerifiedChooseActivity;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 认证结果
 */
public class VerifiedResultActivity extends BaseActivity {

    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    private boolean result;

    //处方ID
    private int prescriptionId;
    private String doctorName;
    private String idCard;
    //认证方式
    private int authentication;

    private Handler handler;

    int time = 3;

    @Override
    public int initContentView() {
        return R.layout.activity_verified_result;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initUiAndListener() {
        uniteTitle.setBackListener(-1, v -> finish());

        result = getIntent().getBooleanExtra("result", false);
        prescriptionId = getIntent().getIntExtra("prescriptionId", -1);
        doctorName = getIntent().getStringExtra("doctorName");
        idCard = getIntent().getStringExtra("idCard");
        authentication = getIntent().getIntExtra("authentication", -1);
        if (result) {
            imageView.setImageResource(R.drawable.verified_success);
            textView.setText("恭喜您，完成个人实名认证");
        } else {
            imageView.setImageResource(R.drawable.verified_fail);
            textView.setText("抱歉，实名认证失败");
        }

        handler = new MyHandler(this);
        handler.sendEmptyMessageDelayed(0, 1000);

        buttonSubmit.setOnClickListener(v -> {
            if (null != handler) {
                handler.removeCallbacksAndMessages(null);
            }

            if (result) {
                Intent intent = new Intent(VerifiedResultActivity.this, AspirationVerifiedChooseActivity.class);
                intent.putExtra("prescriptionId", prescriptionId);
                intent.putExtra("authentication", authentication);
                intent.putExtra("doctorName", doctorName);
                intent.putExtra("idCard", idCard);
                startActivityForResult(intent, 300);
            } else {
                setResult(200, getIntent());
                finish();
            }
        });
    }

    private class MyHandler extends Handler {
        private final WeakReference<VerifiedResultActivity> mActivity;

        MyHandler(VerifiedResultActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                //倒计时
                change();
            }
        }
    }

    private void change() {
        buttonSubmit.setText("返回（" + getCount() + "s）");
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    private int getCount() {
        time--;
        if (time == 0) {
            if (result) {
                Intent intent = new Intent(VerifiedResultActivity.this, AspirationVerifiedChooseActivity.class);
                intent.putExtra("prescriptionId", prescriptionId);
                intent.putExtra("authentication", authentication);
                intent.putExtra("doctorName", doctorName);
                intent.putExtra("idCard", idCard);
                startActivityForResult(intent, 300);
            } else {
                setResult(200, getIntent());
                finish();
            }
        }
        return time--;
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
