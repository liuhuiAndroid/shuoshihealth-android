package com.ssh.shuoshi.receive;

import android.content.Context;
import android.util.Log;

import com.ssh.shuoshi.Constants;
import com.tencent.android.mipush.XMPushMessageReceiver;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMOfflinePushConfig;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;

import cn.jpush.android.service.PluginXiaomiPlatformsReceiver;

public class MyXMPushMessageReceiver extends XMPushMessageReceiver {


    final PluginXiaomiPlatformsReceiver receiver = new PluginXiaomiPlatformsReceiver();

    @Override
    public void onReceivePassThroughMessage(final Context context, final MiPushMessage message) {
        receiver.onReceivePassThroughMessage(context, message);
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        receiver.onNotificationMessageClicked(context, message);

        Log.i("a","onNotificationMessageClicked");
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        receiver.onNotificationMessageArrived(context, message);
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        receiver.onCommandResult(context, message);
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        receiver.onReceiveRegisterResult(context, message);
        if (message != null && message.getCommandArguments() != null && message.getCommandArguments().size() > 0) {
            String token = message.getCommandArguments().get(0);
            V2TIMManager.getOfflinePushManager()
                    .setOfflinePushConfig(new V2TIMOfflinePushConfig(Constants.XIAOMI_BUSINESSID, token),
                            new V2TIMCallback() {
                                @Override
                                public void onError(int code, String desc) {
                                    Log.d("TPush", "注册即时通讯IM 小米推送 onError");
                                }

                                @Override
                                public void onSuccess() {
                                    Log.d("TPush", "注册即时通讯IM 小米推送 onSuccess");
                                }
                            });
        }
    }


}
