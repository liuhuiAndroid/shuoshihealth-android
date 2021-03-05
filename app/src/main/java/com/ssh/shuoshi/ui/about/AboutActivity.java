package com.ssh.shuoshi.ui.about;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ssh.shuoshi.Constants;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.login.LoginActivity;
import com.ssh.shuoshi.ui.web.WebActivity;
import com.ssh.shuoshi.view.title.UniteTitle;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.title)
    UniteTitle title;
    @BindView(R.id.tvVersionCode)
    TextView tvVersionCode;
    @BindView(R.id.tv_law)
    TextView tvLaw;

    @Override
    public int initContentView() {
        return R.layout.activity_about;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initUiAndListener() {
        title.setBackListener(-1, v -> finish());
        tvVersionCode.setText("版本号：" + getAppVersionName(AboutActivity.this));
        tvLaw.setOnClickListener(v -> {
            Intent intent2 = new Intent(AboutActivity.this, WebActivity.class);
            intent2.putExtra("url", Constants.WEB_002);
            startActivity(intent2);
        });
    }


    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }
}
