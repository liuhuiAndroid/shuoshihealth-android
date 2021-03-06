package com.ssh.shuoshi.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.gson.JsonParseException;
import com.ssh.shuoshi.Constants;
import com.ssh.shuoshi.api.CommonApi;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.injector.HasComponent;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.model.TRTCCalling;
import com.ssh.shuoshi.model.impl.TRTCCallingImpl;
import com.ssh.shuoshi.ui.login.LoginActivity;
import com.ssh.shuoshi.util.SPUtil;
import com.tencent.mmkv.MMKV;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import me.yokeyword.fragmentation.SupportFragment;
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
public abstract class BaseFragment extends SupportFragment {

    protected BaseActivity baseActivity;
    protected View rootView;

    @Inject
    UserStorage mUserStorage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(initContentView(), null);
        ButterKnife.bind(this, rootView);
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        if (getActivity() instanceof BaseActivity) {
            baseActivity = (BaseActivity) getActivity();
        }
        //???????????????
        baseActivity.getApplicationComponent().inject(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initInjector();
        getBundle(getArguments());
        initUI(view);
        initData();
        super.onViewCreated(view, savedInstanceState);
    }

    public void openActivity(Class<?> cls) {
        startActivity(new Intent(baseActivity, cls));
    }

    public abstract void initInjector();

    public abstract int initContentView();

    /**
     * ??????Activity???????????????
     */
    public abstract void getBundle(Bundle bundle);

    /**
     * ???????????????
     */
    public abstract void initUI(View view);

    /**
     * ????????????????????????????????????
     */
    public abstract void initData();

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    public void loadError(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof CommonApi.APIException) {
            if (((CommonApi.APIException) throwable).code == Constants.TOKEN_INVALID) {
            } else if (((CommonApi.APIException) throwable).code == Constants.NO_USER) {
                // ???????????????
            } else if (((CommonApi.APIException) throwable).code == Constants.TOKEN_EXIST) {
                // ????????????????????????
                SPUtil spUtil = new SPUtil(baseActivity);
                spUtil.setIS_LOGIN(0);
                spUtil.setTOKNE("");
                UserStorage userStorage = new UserStorage(baseActivity, spUtil);
                userStorage.setToken("");
                openActivity(LoginActivity.class);
            } else if (((CommonApi.APIException) throwable).code == Constants.TOKEN_FREEZE) {
                // ???????????????
                SPUtil spUtil = new SPUtil(baseActivity);
                spUtil.setIS_LOGIN(0);
                spUtil.setTOKNE("");
                UserStorage userStorage = new UserStorage(baseActivity, spUtil);
                userStorage.setToken("");
                openActivity(LoginActivity.class);
            } else if (((CommonApi.APIException) throwable).code == Constants.USER_SHOULD_RELOGIN) {
                TRTCCallingImpl.sharedInstance(getActivity()).logout(new TRTCCalling.ActionCallBack() {

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
                JPushInterface.deleteAlias(getActivity(),
                        Integer.parseInt((System.currentTimeMillis() + "").substring(5)));
                AlertDialog.Builder builder = new AlertDialog.Builder(baseActivity);
                builder.setMessage("????????????????????????????????????")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(baseActivity, LoginActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                builder.create();
                builder.show();
//                startActivity(new Intent(getActivity(), LoginActivity.class));
            } else {
                ToastUtil.showToast(throwable.getMessage());
            }
        } else if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                    ToastUtil.showToast("????????????");
                    break;
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    //?????????????????????
                    ToastUtil.showToast("???????????? ????????????: " + httpException.code());
                    break;
            }
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException) {
            ToastUtil.showToast("????????????");
        } else if (throwable instanceof UnknownHostException) {
            ToastUtil.showToast("????????????: ????????????");
        } else if (throwable instanceof SocketTimeoutException) {    //??????
//            ToastUtil.showToast("????????????: " + getResources().getString(R.string.error_overtime));
        } else if (throwable instanceof ConnectException) {
            ToastUtil.showToast("??????????????????????????????????????????");
        } else {
            //            ToastUtil.showToast(getResources().getString(R.string.error_unknow));
        }
    }
}
