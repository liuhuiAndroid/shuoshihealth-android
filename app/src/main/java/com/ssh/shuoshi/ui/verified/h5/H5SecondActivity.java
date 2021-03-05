package com.ssh.shuoshi.ui.verified.h5;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.permissionx.guolindev.PermissionX;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.verified.result.VerifiedResultActivity;
import com.ssh.shuoshi.ui.verified.submit.PrescriptionSubmitCheckActivity;
import com.ssh.shuoshi.ui.verified.tencent.WBH5FaceVerifySDK;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import static android.webkit.WebSettings.LOAD_NO_CACHE;

/**
 * created by hwt on 2020/12/30
 */
public class H5SecondActivity extends BaseActivity implements H5SecondContract.View {

    @Inject
    H5SecondPresenter mPresenter;

    @Inject
    UserStorage mUserStorage;

    //处方ID
    private int prescriptionId;
    //意愿认证返回编号
    private String bizId;
    //实名认证
    public final static String SCHEMA_REAL = "esign://demo/realBack";
    //签署意愿
    public final static String SCHEMA_SIGN = "esign://demo/signBack";
    private WebView mWebView;
    String curUrl = null;

    long time = System.currentTimeMillis();

    ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;
    private LoadingDialog mLoadingDialog;
    private HisDoctorBean doctorInfo;
    private String accountId;
    //处方、处方订单详细信息
    private HisPrescriptionDtoBean mBean;

    @Override
    public int initContentView() {
        return R.layout.activity_h5_first;
    }

    @Override
    protected void initInjector() {
        DaggerH5SecondComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);

        mWebView = findViewById(R.id.webView);
        UniteTitle title = findViewById(R.id.title);
        title.setBackListener(-1, v -> finish());
        doctorInfo = mUserStorage.getDoctorInfo();
        accountId = doctorInfo.getAccountId();
        prescriptionId = getIntent().getIntExtra("prescriptionId", -1);
        bizId = getIntent().getStringExtra("bizId");
        mBean = (HisPrescriptionDtoBean) getIntent().getSerializableExtra("historyDrugsBean");

        WebSettings webSetting = mWebView.getSettings();

        webSetting.setJavaScriptEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setAppCacheMaxSize(1024 * 1024 * 8);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setAppCacheEnabled(true);
        String appCachePath = getApplication().getCacheDir().getAbsolutePath();
        webSetting.setAppCachePath(appCachePath);
        webSetting.setDatabaseEnabled(true);
        webSetting.setCacheMode(LOAD_NO_CACHE);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= 21) {
            cookieManager.setAcceptThirdPartyCookies(mWebView, true);
        }
        cookieManager.setAcceptCookie(true);


        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new H5FaceWebChromeClient(this));

        WBH5FaceVerifySDK.getInstance().setWebViewSettings(mWebView, getApplicationContext());

        PermissionX.init(this)
                .permissions(Manifest.permission.CAMERA).request((allGranted, grantedList, deniedList) -> {
            if (allGranted) {
                processExtraData();
            } else {
                Toast.makeText(this, "没有摄像头权限，无法进行人脸识别", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWebView.stopLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mWebView != null) {
                mWebView.removeAllViews();
                mWebView.destroy();
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processExtraData();
    }

    private void processExtraData() {
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            // 芝麻认证刷脸结束返回获取后续操作页面地址
//            String callbackUrl = uri.getQueryParameter("realnameUrl");
            String callbackUrl = uri.getQueryParameter("realnameUrl");
//            Log.i("hhhh", "callbackUrl == " + callbackUrl);
            if (!TextUtils.isEmpty(callbackUrl)) {
                try {
                    mWebView.loadUrl(URLDecoder.decode(callbackUrl, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } else {
            String url = intent.getStringExtra("url");
//            Log.i("hhhh", "url===  " + url);
            if (TextUtils.isEmpty(url)) {
                finish();
            }
            if (url.startsWith("alipay")) {
                try {
                    Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent2);
                    return;
                } catch (Exception e) {
                }
            }
            if (curUrl == null) {
                curUrl = url;
            }
            mWebView.loadUrl(url);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url == null) {
                return false;
            }
            Uri uri = Uri.parse(url);
            Log.i("dtb", "url == " + url);
            if (uri.getScheme().equals("http") || uri.getScheme().equals("https")) {
                view.loadUrl(url);
                return true;
            } else if (uri.getScheme().equals("js") || uri.getScheme().equals("jsbridge")) {

                // js://signCallback?signResult=true  签署结果
                if (uri.getAuthority().equals("signCallback")) {
                    Log.i("hwt", "执行到  signCallback 中了");
                    finish();
                }

                //js://tsignRealBack?esignAppScheme=esign://app/callback&serviceId=854677892133554052&verifycode=4a52e2af0d0abfb7b285c4f05b5af133&status=true&passed=true
                //实名结果
                if (uri.getAuthority().equals("tsignRealBack")) {

                    Log.i("hwt", "执行到  tsignRealBack 中了");
                    //实名结果字段
                    if (uri.getQueryParameter("verifycode") != null) {
                        String realVerifyCode = uri.getQueryParameter("verifycode");
                    }
                    // 实名认证结束 返回按钮/倒计时返回/暂不认证
                    boolean status = uri.getBooleanQueryParameter("status", false);
                    if (status) {
                        //认证成功返回
                        Toast.makeText(H5SecondActivity.this, "认证成功", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
                return true;
            } else if (url.startsWith(SCHEMA_REAL)) {
                Log.i("hwt", "真的执行到  SCHEMA_REAL 中了");
                //esign://app/realBack&serviceId=854677892133554052&verifycode=4a52e2af0d0abfb7b285c4f05b5af133&status=true&passed=true

                //实名结果
                if (uri.getQueryParameter("verifycode") != null) {
                    String realVerifyCode = uri.getQueryParameter("verifycode");
                }
                // 实名认证结束 返回按钮/倒计时返回/暂不认证
                boolean status = uri.getBooleanQueryParameter("status", false);
                if (status) {
                    //认证成功返回
                    Toast.makeText(H5SecondActivity.this, "认证成功", Toast.LENGTH_LONG).show();
                    finish();
                }
                return true;
            } else if (url.startsWith(SCHEMA_SIGN)) {
                // js://signCallback?signResult=true  签署结果
                Log.i("hwt", "执行到  签署结果 中了");
                Map<String, Object> map = new HashMap<>();
                map.put("bizId", bizId);
                map.put("accountId", accountId);
                map.put("prescriptionId", prescriptionId);
                mPresenter.caFaceThirdCode(map);
                return true;
            } else if (uri.getScheme().equals("alipays")) {
                // 跳转到支付宝刷脸
                // alipays://platformapi/startapp?appId=20000067&pd=NO&url=https%3A%2F%2Fzmcustprod.zmxy.com.cn%2Fcertify%2Fbegin.htm%3Ftoken%3DZM201811133000000050500431389414
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            time = System.currentTimeMillis();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }
    }


    public class H5FaceWebChromeClient extends WebChromeClient {
        private Activity activity;

        public H5FaceWebChromeClient(Activity mActivity) {
            this.activity = mActivity;
        }


        @Override
        public void onReceivedTitle(WebView view, String title) {
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return super.onJsConfirm(view, url, message, result);
        }

        @TargetApi(8)
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            return super.onConsoleMessage(consoleMessage);
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            if (WBH5FaceVerifySDK.getInstance().recordVideoForApiBelow21(uploadMsg, acceptType, activity)) {
                return;
            }
            uploadMessage = uploadMsg;
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            if (WBH5FaceVerifySDK.getInstance().recordVideoForApiBelow21(uploadMsg, acceptType, activity)) {
                return;
            }
            uploadMessage = uploadMessage;
        }

        @TargetApi(21)
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (WBH5FaceVerifySDK.getInstance().recordVideoForApi21(webView, filePathCallback, activity, fileChooserParams)) {
                return true;
            }
            uploadMessageAboveL = filePathCallback;
            recordVideo(H5SecondActivity.this);
            return true;
        }
    }

    public void recordVideo(Activity activity) {
        // TODO: 2020/12/30 需要判断权限
        PermissionX.init(this)
                .permissions(Manifest.permission.CAMERA).request((allGranted, grantedList, deniedList) -> {
            if (allGranted) {
                try {
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra("android.intent.extras.CAMERA_FACING", 1); // 调用前置摄像头
                    activity.startActivityForResult(intent, FILE_CHOOSER_RESULT_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "没有摄像头权限，无法进行人脸识别", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (WBH5FaceVerifySDK.getInstance().receiveH5FaceVerifyResult(requestCode, resultCode, data)) {
            return;
        }

        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) {
                return;
            }
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (uploadMessageAboveL != null) {
                if (resultCode == RESULT_OK) {
                    uploadMessageAboveL.onReceiveValue(new Uri[]{result});
                    uploadMessageAboveL = null;
                } else {
                    uploadMessageAboveL.onReceiveValue(new Uri[]{});
                    uploadMessageAboveL = null;
                }
            } else if (uploadMessage != null) {
                if (resultCode == RESULT_OK) {
                    uploadMessage.onReceiveValue(result);
                    uploadMessage = null;
                } else {
                    uploadMessage.onReceiveValue(Uri.EMPTY);
                    uploadMessage = null;
                }
            }
        }

        if (requestCode == 300) {
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        WBH5FaceVerifySDK.getInstance().recordVideo(this);
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(H5SecondActivity.this, "");
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
    public void caFaceThirdCodeSuccess(String bean) {
        Intent intent = new Intent(H5SecondActivity.this, PrescriptionSubmitCheckActivity.class);
        intent.putExtra("historyDrugsBean", mBean);
        intent.putExtra("prescriptionId", prescriptionId);
        startActivityForResult(intent, 300);
//        finish();
    }

}
