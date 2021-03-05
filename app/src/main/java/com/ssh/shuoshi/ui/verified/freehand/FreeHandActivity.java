package com.ssh.shuoshi.ui.verified.freehand;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.view.sign.PaintView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 手绘签名
 * created by hwt on 2020/12/29
 */
public class FreeHandActivity extends BaseActivity {

    @BindView(R.id.view)
    PaintView mPaintView;
    @BindView(R.id.textClear)
    TextView textClear;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;

    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }

    @Override
    public void setStatusBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.transparentStatusBar()  //透明状态栏，不写默认透明色
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .init();
    }

    @Override
    public int initContentView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        // 隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        return R.layout.activity_free_hand;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initUiAndListener() {

        imgBack.setOnClickListener(v -> finish());

        textClear.setOnClickListener(v -> mPaintView.clear());

        buttonSubmit.setOnClickListener(v -> {
            if (mPaintView.pathIsEmpty()) {
                ToastUtil.showToast("请签名");
                return;
            }
            try {
                String path = mPaintView.save("doctorSign.png");
                Intent intent = new Intent();
                intent.putExtra("path", path);
                setResult(RESULT_OK, intent);
                finish();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

}
