package com.ssh.shuoshi;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.hjq.toast.ToastUtils;
import com.hjq.toast.style.ToastQQStyle;
import com.huawei.hms.aaid.HmsInstanceId;
import com.lzf.easyfloat.EasyFloat;
import com.ssh.shuoshi.injector.component.ApplicationComponent;
import com.ssh.shuoshi.injector.component.DaggerApplicationComponent;
import com.ssh.shuoshi.injector.module.ApplicationModule;
import com.ssh.shuoshi.library.util.PhoneUtil;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.library.util.log.CrashlyticsTree;
import com.ssh.shuoshi.library.util.log.Logger;
import com.ssh.shuoshi.library.util.log.Settings;
import com.ssh.shuoshi.ui.videocall.TRTCVideoCallActivity;
import com.ssh.shuoshi.util.SPUtil;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.imsdk.utils.IMFunc;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMOfflinePushConfig;
import com.tencent.imsdk.v2.V2TIMOfflinePushManager;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.mmkv.MMKV;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.config.CustomFaceConfig;
import com.tencent.qcloud.tim.uikit.config.GeneralConfig;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

import static com.ssh.shuoshi.ToolTm.GenerateTestIMUserSig.SDKAPPID;

/**
 * created by hwt on 2020/12/8
 */
public class MyApplication extends MultiDexApplication {

    private static Context mContext;
    private ApplicationComponent mApplicationComponent;
    private ActivityLifecycleCallbacks callbacks;

    @Override
    public void onCreate() {
        super.onCreate();
        initComponent();
        mContext = getApplicationContext();
        new AppError().initUncaught(mContext);
        ToastUtil.register(this);
        ToastUtils.init(this);
        ToastUtils.initStyle(new ToastQQStyle(this));

        String rootDir = MMKV.initialize(this);

        Logger.initialize(new Settings()
                .isShowMethodLink(true)
                .isShowThreadInfo(false)
                .setMethodOffset(0)
                .setLogPriority(BuildConfig.DEBUG ? Log.VERBOSE : Log.ASSERT));

        if (!BuildConfig.DEBUG) {
            Logger.plant(new CrashlyticsTree());
        }

        initPush();
        initJPush();
        initTencentAssembly();
        createChannel();

        EasyFloat.init(this, BuildConfig.DEBUG);
    }

    public static final String CHAT = "chat";

    private void createChannel() {
        String channelId = CHAT;
        String channelName = "聊天消息";
        int importance = NotificationManager.IMPORTANCE_MAX;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(channelId, channelName, importance);
        }
    }

    /**
     * 创建通知渠道
     *
     * @param channelId   渠道ID，需保证全局唯一性
     * @param channelName 渠道名称，是给用户看的，需要能够表达清楚这个渠道的用途
     * @param importance  重要等级，决定通知的不同行为
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    private void initJPush() {
        // 设置调试模式
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        // 初始化推送服务
        JPushInterface.init(this);
    }

    private void initTencentAssembly() {
        TUIKitConfigs configs = TUIKit.getConfigs();
        configs.setSdkConfig(new V2TIMSDKConfig());
        configs.setCustomFaceConfig(new CustomFaceConfig());
        configs.setGeneralConfig(new GeneralConfig());
        TUIKit.init(this, SDKAPPID, configs);

        XGPushConfig.enableDebug(this, BuildConfig.DEBUG);
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                //token在设备卸载重装的时候有可能会变
                Log.d("TPush", "注册成功，设备token为：" + data);
                // 小米9 ：0a6db603c3f79a7cb34fe8779e6e84cd34d9
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });
        // TPNS 启动华为推送
        XGPushConfig.enableOtherPush(getApplicationContext(), true);
        XGPushConfig.setMiPushAppId(getApplicationContext(), Constants.XIAOMI_APPID);
        XGPushConfig.setMiPushAppKey(getApplicationContext(), Constants.XIAOMI_APPKEY);
        XGPushConfig.enableOtherPush(getApplicationContext(), true);
        // TPNS 创建通知渠道
        if (IMFunc.isBrandHuawei()) {
            XGPushManager.createNotificationChannel(getApplicationContext(), "im_chat", "即时通讯消息",
                    true, true, true, null);
            // 华为没啥限制，订单和IM公用一个Channel呗
            JPushInterface.setChannel(this, "im_chat");
        } else if (IMFunc.isBrandXiaoMi()) {
            XGPushManager.createNotificationChannel(getApplicationContext(), "pre84", "即时通讯消息",
                    true, true, true, null);
            XGPushManager.createNotificationChannel(getApplicationContext(), "pre96", "订单状态变化",
                    true, true, true, null);
            JPushInterface.setChannel(this, "pre96");
        }
    }

    private void initPush() {
        String registrationID = PhoneUtil.getDeviceId(mContext);
        SPUtil spUtil = new SPUtil(mContext);
        spUtil.setREGISTRATIONID(registrationID);
    }

    private void initComponent() {
        // 如果Module只有有参构造器，则必须显式传入Module实例。
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        //现在没有需要在MyApplication注入的对象，所以这句代码可写可不写
        mApplicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }


    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    /**
     * 判断某个界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     */
    public static boolean isActivityRunning(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                ComponentName cpn = list.get(i).topActivity;
                if (className.equals(cpn.getClassName())) {
                    return true;
                }
            }
        }

        return false;
    }
}
