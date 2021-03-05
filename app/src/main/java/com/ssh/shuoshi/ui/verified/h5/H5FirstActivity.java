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
import com.ssh.shuoshi.bean.ca.CAPhoneBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.verified.result.VerifiedResultActivity;
import com.ssh.shuoshi.ui.verified.tencent.WBH5FaceVerifySDK;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.inject.Inject;

import static android.webkit.WebSettings.LOAD_NO_CACHE;

/**
 * created by hwt on 2020/12/30
 */
public class H5FirstActivity extends BaseActivity implements H5FirstContract.View {

    @Inject
    H5FirstPresenter mPresenter;

    @Inject
    UserStorage mUserStorage;

    //处方ID
    private int prescriptionId;
    //医生姓名
    private String doctorName;
    //医生身份证
    private String idCard;
    //意愿认证返回编号
    private String bizId;
    //认证方式
    private int authentication;

    public final static String SCHEMA_REAL = "esign://shuoshi/realBack";
    public final static String SCHEMA_SIGN = "esign://shuoshi/signBack";
    private WebView mWebView;
    String curUrl = null;

    long time = System.currentTimeMillis();

    ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;
    private LoadingDialog mLoadingDialog;
    private HisDoctorBean doctorInfo;

    private int num = 0;

    @Override
    public int initContentView() {
        return R.layout.activity_h5_first;
    }

    @Override
    protected void initInjector() {
        DaggerH5FirstComponent.builder()
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
        prescriptionId = getIntent().getIntExtra("prescriptionId", -1);
        doctorName = getIntent().getStringExtra("doctorName");
        idCard = getIntent().getStringExtra("idCard");
        bizId = getIntent().getStringExtra("bizId");
        authentication = getIntent().getIntExtra("authentication", -1);

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
            String callbackUrl = uri.getQueryParameter("realnameUrl");
            if (!TextUtils.isEmpty(callbackUrl)) {
                if (callbackUrl.equals("esign://shuoshi/realBack")) {
                    showLoading();
                    mPresenter.getDoctorInfo();
                } else {
                    try {
                        mWebView.loadUrl(URLDecoder.decode(callbackUrl, "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            String url = intent.getStringExtra("url");
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
            if (uri.getScheme().equals("http") || uri.getScheme().equals("https")) {
                if (uri.toString().contains("https://www.baidu.com")) {
                    showLoading();
                    mPresenter.getDoctorInfo();
                } else {
                    view.loadUrl(url);
                }

                return true;
            } else if (uri.getScheme().equals("js") || uri.getScheme().equals("jsbridge")) {
                if (uri.getAuthority().equals("signCallback")) {
                    finish();
                }
                //实名结果
                if (uri.getAuthority().equals("tsignRealBack")) {
                    //实名结果字段
                    if (uri.getQueryParameter("verifycode") != null) {
                        String realVerifyCode = uri.getQueryParameter("verifycode");
                    }
                    // 实名认证结束 返回按钮/倒计时返回/暂不认证
                    boolean status = uri.getBooleanQueryParameter("status", false);
                    if (status) {
                        //认证成功返回
                        Toast.makeText(H5FirstActivity.this, "认证成功", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
                return true;
            } else if (url.startsWith(SCHEMA_REAL)) {
                //实名结果
                if (uri.getQueryParameter("verifycode") != null) {
                    String realVerifyCode = uri.getQueryParameter("verifycode");
                }
                // 实名认证结束 返回按钮/倒计时返回/暂不认证
                boolean status = uri.getBooleanQueryParameter("status", false);
                if (status) {
                    //认证成功返回
                    Toast.makeText(H5FirstActivity.this, "认证成功", Toast.LENGTH_LONG).show();
                    finish();
                }
                return true;
            } else if (url.startsWith(SCHEMA_SIGN)) {
                if (url.contains("signResult")) {
                    boolean signResult = uri.getBooleanQueryParameter("signResult", false);
                    Toast.makeText(H5FirstActivity.this, "签署结果： " + " signResult = " + signResult, Toast.LENGTH_LONG).show();
                } else {
                    String tsignCode = uri.getQueryParameter("tsignCode");
                    if ("0".equals(tsignCode)) {
                        tsignCode = "签署成功";
                    } else {
                        tsignCode = "签署失败";
                    }
                    Toast.makeText(H5FirstActivity.this, "签署结果： " + tsignCode, Toast.LENGTH_LONG).show();
                }
                finish();
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
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            if (WBH5FaceVerifySDK.getInstance().recordVideoForApi21(webView, filePathCallback, activity, fileChooserParams)) {
                return true;
            }
            uploadMessageAboveL = filePathCallback;
            recordVideo(H5FirstActivity.this);
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

        if (resultCode == RESULT_OK && requestCode == 300) {
            setResult(RESULT_OK, getIntent());
            finish();
        }

        if (resultCode == 200 && requestCode == 300) {
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
        mLoadingDialog = LoadingDialog.show(H5FirstActivity.this, "");
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
    public void getDoctorInfoSuccess(HisDoctorBean bean) {
        Integer authState = bean.getAuthState();
        num++;
        if (authState == null || authState == 0 && num < 3) {
            mPresenter.task();
        } else {
            if (authState == 1) {
                hideLoading();
                Intent intent = new Intent(H5FirstActivity.this, VerifiedResultActivity.class);
                intent.putExtra("result", true);
                intent.putExtra("prescriptionId", prescriptionId);
                intent.putExtra("authentication", authentication);
                intent.putExtra("doctorName", doctorName);
                intent.putExtra("idCard", idCard);
                startActivityForResult(intent, 300);
            } else {
                mPresenter.queryFaceOauthResult();
            }
        }
    }

    @Override
    public void queryFaceOauthResultSuccess(CAPhoneBean bean) {
        hideLoading();
        Intent intent = new Intent(H5FirstActivity.this, VerifiedResultActivity.class);
        if (bean.getCode().equals("0")) {
            intent.putExtra("result", true);
        } else {
            intent.putExtra("result", false);
        }
        intent.putExtra("prescriptionId", prescriptionId);
        intent.putExtra("authentication", authentication);
        intent.putExtra("doctorName", doctorName);
        intent.putExtra("idCard", idCard);
        startActivityForResult(intent, 300);
    }

}
