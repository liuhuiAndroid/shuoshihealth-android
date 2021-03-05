package com.ssh.shuoshi.receive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.WorkSource;
import android.util.Log;

import com.blankj.utilcode.util.BusUtils;
import com.google.gson.Gson;
import com.ssh.shuoshi.bean.NotificationExtrasBean;
import com.ssh.shuoshi.eventbus.AliasOperatorEvent;
import com.ssh.shuoshi.eventbus.AuthSuccessEvent;
import com.ssh.shuoshi.eventbus.ChangeTuNumEvent;
import com.ssh.shuoshi.eventbus.NotificationExtras2Event;
import com.ssh.shuoshi.eventbus.NotificationExtrasEvent;
import com.ssh.shuoshi.ui.authority.one.AuthorityOneActivity;
import com.ssh.shuoshi.ui.messagecenter.MessageCenterActivity;
import com.ssh.shuoshi.ui.myprescription.main.MyPrescriptionActivity;
import com.ssh.shuoshi.ui.worksetting.WorkSettingActivity;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class MyJPushMessageReceiver extends JPushMessageReceiver {

    private static final String TAG = "MyJPushMessageReceiver";

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        Log.e(TAG, "[onMessage] " + customMessage);
        processCustomMessage(context, customMessage);
    }

    /**
     * 点击推送消息
     */
    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageOpened] " + message);
        try {
            String notificationExtras = message.notificationExtras;
            NotificationExtrasBean notificationExtrasBean =
                    new Gson().fromJson(notificationExtras, NotificationExtrasBean.class);
            if (notificationExtrasBean.getExtraBean().getMsgType() == 5 ||
                    notificationExtrasBean.getExtraBean().getMsgType() == 6) {
                EventBus.getDefault().post(new NotificationExtras2Event(
                        notificationExtrasBean.getExtraBean().getMsgType(),
                        notificationExtrasBean.getExtraBean().getBussinessId()
                ));
            } else {
                EventBus.getDefault().post(new NotificationExtrasEvent(
                        notificationExtrasBean.getExtraBean().getMsgType(),
                        notificationExtrasBean.getExtraBean().getBussinessId()
                ));
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        Log.e(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮");
    }

    /**
     * 收到推送消息
     */
    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageArrived] " + message);
    }

    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageDismiss] " + message);
    }

    @Override
    public void onRegister(Context context, String registrationId) {
        // 13065ffa4ea8764d2f9
        Log.e(TAG, "[onRegister] " + registrationId);
    }

    @Override
    public void onConnected(Context context, boolean isConnected) {
        Log.e(TAG, "[onConnected] " + isConnected);
    }

    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        Log.e(TAG, "[onCommandResult] " + cmdMessage);
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, CustomMessage customMessage) {

    }

    @Override
    public void onNotificationSettingsCheck(Context context, boolean isOn, int source) {
        super.onNotificationSettingsCheck(context, isOn, source);
        Log.e(TAG, "[onNotificationSettingsCheck] isOn:" + isOn + ",source:" + source);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);
        Log.e(TAG, "[onNotificationSettingsCheck] isOn:" + jPushMessage + ",source:" + jPushMessage);
        EventBus.getDefault().post(new AliasOperatorEvent());
    }
}
