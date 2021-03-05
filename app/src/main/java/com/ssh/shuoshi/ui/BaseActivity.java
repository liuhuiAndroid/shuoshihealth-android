package com.ssh.shuoshi.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.gson.JsonParseException;
import com.gyf.barlibrary.ImmersionBar;
import com.ssh.shuoshi.Constants;
import com.ssh.shuoshi.MyApplication;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.injector.component.ApplicationComponent;
import com.ssh.shuoshi.injector.module.ActivityModule;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.model.TRTCCalling;
import com.ssh.shuoshi.model.impl.TRTCCallingImpl;
import com.ssh.shuoshi.ui.authority.three.AuthorityThreeActivity;
import com.ssh.shuoshi.ui.login.LoginActivity;
import com.ssh.shuoshi.ui.main.MainActivity;
import com.ssh.shuoshi.util.AppManager;
import com.ssh.shuoshi.util.SPUtil;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import me.yokeyword.fragmentation.SupportActivity;
import retrofit2.HttpException;
import timber.log.Timber;

import static com.ssh.shuoshi.Constants.BAD_GATEWAY;
import static com.ssh.shuoshi.Constants.FORBIDDEN;
import static com.ssh.shuoshi.Constants.GATEWAY_TIMEOUT;
import static com.ssh.shuoshi.Constants.INTERNAL_SERVER_ERROR;
import static com.ssh.shuoshi.Constants.NOT_FOUND;
import static com.ssh.shuoshi.Constants.REQUEST_TIMEOUT;
import static com.ssh.shuoshi.Constants.SERVICE_UNAVAILABLE;
import static com.ssh.shuoshi.Constants.UNAUTHORIZED;

/**
 * created by hwt on 2020/12/8
 */
public abstract class BaseActivity extends SupportActivity {

    @Inject
    SPUtil mSPUtil;
    @Inject
    UserStorage mUserStorage;
    public ImmersionBar mImmersionBar;

    private boolean mIsDispatch = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //注入数据库
        getApplicationComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(initContentView());
        setStatusBar();
        ButterKnife.bind(this);
        initInjector();
        initUiAndListener();
        AppManager.getAppManager().addActivity(this);
        Log.i("hwt", "当前的类名: " + getClass().getName());
    }

    public void setStatusBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarColor(R.color.white)
                .statusBarDarkFont(true)
                .keyboardEnable(true)
                .fitsSystemWindows(true).init();
    }

    //设置布局
    public abstract int initContentView();

    //注入injector
    protected abstract void initInjector();

    // init UI and Listener
    protected abstract void initUiAndListener();

    protected ApplicationComponent getApplicationComponent() {
        return ((MyApplication) getApplication()).getApplicationComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }


    protected void setIsDispatch(boolean isDispatch) {
        this.mIsDispatch = isDispatch;
    }

    /**
     * 打开新的activity
     */
    public void openActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    public void loadError(@NonNull Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof CommonApi.APIException) {
            if (((CommonApi.APIException) throwable).code != Constants.USER_SHOULD_RELOGIN){
                ToastUtil.showToast(throwable.getMessage());
            }
            if (((CommonApi.APIException) throwable).code == Constants.NO_USER) {
                // 用户未登录
            } else if (((CommonApi.APIException) throwable).code == Constants.TOKEN_EXPIRED) {
//                mSPUtil.setIS_LOGIN(0);
//                mSPUtil.setTOKNE("");
//                mUserStorage.setToken("");
//                openActivity(LoginActivity.class);
            } else if (((CommonApi.APIException) throwable).code == Constants.TOKEN_EXIST) {
                // 在其他地方登录了
                mSPUtil.setIS_LOGIN(0);
                mSPUtil.setTOKNE("");
                mUserStorage.setToken("");
                // 这里可以用 mUserStorage.logout();替代,需要测试
//                openActivity(LoginActivity.class);
            } else if (((CommonApi.APIException) throwable).code == Constants.TOKEN_FREEZE) {
                // 用户被锁定
                mSPUtil.setIS_LOGIN(0);
                mSPUtil.setTOKNE("");
                mUserStorage.setToken("");
//                openActivity(LoginActivity.class);
            } else if (((CommonApi.APIException) throwable).code == Constants.USER_SHOULD_RELOGIN) {
                TRTCCallingImpl.sharedInstance(BaseActivity.this).logout(new TRTCCalling.ActionCallBack() {

                    @Override
                    public void onError(int code, String msg) {
                        Timber.i("onError code");
                    }

                    @Override
                    public void onSuccess() {
                        Timber.i("onSuccess code");
                    }
                });
                mUserStorage.logout();
                MMKV.defaultMMKV().putBoolean("firstLogin", false);
                JPushInterface.deleteAlias(BaseActivity.this,
                        Integer.parseInt((System.currentTimeMillis() + "").substring(5)));

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("该账号已在其他设备上登录")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(BaseActivity.this, LoginActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                builder.create();
                builder.show();
            }
        } else if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                    //                    onPermissionError(ex);          //权限错误，需要实现
                    ToastUtil.showToast("权限错误");
                    break;
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    //均视为网络错误
                    ToastUtil.showToast("网络异常 错误类型: " + httpException.code());
                    break;
            }
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException) {
            ToastUtil.showToast("解析异常");
        } else if (throwable instanceof UnknownHostException) {
            ToastUtil.showToast("错误类型: 网络异常");
        } else if (throwable instanceof SocketTimeoutException) {    //超时
//            ToastUtil.showToast("错误类型: " + getResources().getString(R.string.error_overtime));
        } else if (throwable instanceof ConnectException) {
            ToastUtil.showToast("无法连接到服务器，请检查网络");
        } else {
            //            ToastUtil.showToast(getResources().getString(R.string.error_unknow));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mIsDispatch && ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }

        // 所有页面跳转和按钮，快速点击应该控制打开重复页面
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isFastDoubleClick()) {
                return true;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 控制打开重复页面
     *
     * @return
     */
    private long lastClickTime = System.currentTimeMillis();

    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD >= 0 && timeD <= 300) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void baseEvent(String event) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
