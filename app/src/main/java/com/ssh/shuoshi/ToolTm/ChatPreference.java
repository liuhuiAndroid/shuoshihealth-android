package com.ssh.shuoshi.ToolTm;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ssh.shuoshi.ui.videodiagnose.main.VideoDiagnoseActivity;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;

import java.util.ArrayList;
import java.util.List;

//数据转换类
public class ChatPreference {

    public void startChat(Context context, int type) {
        V2TIMManager.getConversationManager().getConversationList(0, 100, new V2TIMValueCallback<V2TIMConversationResult>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.v("TAG", "loadConversation getConversationList error, code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess(V2TIMConversationResult v2TIMConversationResult) {
                ArrayList<ConversationInfo> infos = new ArrayList<>();
                List<V2TIMConversation> v2TIMConversationList = v2TIMConversationResult.getConversationList();

                if (v2TIMConversationList.isEmpty()) {

                } else {
                    V2TIMConversation v2TIMConversation = v2TIMConversationList.get(0);
                    //将 imsdk v2TIMConversation 转换为 UIKit ConversationInfo
                    ConversationInfo conversationInfo = TIMConversation2ConversationInfo(v2TIMConversation);
//                    conversationInfo.setType(1);
                    if (conversationInfo != null) {
                        ChatInfo chatInfo = new ChatInfo();
                        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
                        chatInfo.setId(conversationInfo.getId());
                        chatInfo.setChatName(conversationInfo.getTitle());
                        Intent intent = new Intent(context, VideoDiagnoseActivity.class);
                        intent.putExtra(ImConstants.CHAT_INFO, chatInfo);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            }
        });

    }

    private ConversationInfo TIMConversation2ConversationInfo(final V2TIMConversation conversation) {
        if (conversation == null) {
            return null;
        }
        TUIKitLog.i("TAG", "TIMConversation2ConversationInfo id:" + conversation.getConversationID()
                + "|name:" + conversation.getShowName()
                + "|unreadNum:" + conversation.getUnreadCount());
        V2TIMMessage message = conversation.getLastMessage();
        if (message == null) {
            return null;
        }
        final ConversationInfo info = new ConversationInfo();
        int type = conversation.getType();
        if (type != V2TIMConversation.V2TIM_C2C && type != V2TIMConversation.V2TIM_GROUP) {
            return null;
        }

        boolean isGroup = type == V2TIMConversation.V2TIM_GROUP;
        info.setLastMessageTime(message.getTimestamp());
        List<MessageInfo> list = MessageInfoUtil.TIMMessage2MessageInfo(message);
        if (list != null && list.size() > 0) {
            info.setLastMessage(list.get(list.size() - 1));
        }
        info.setTitle(conversation.getShowName());

        List<Object> faceList = new ArrayList<>();
        if (TextUtils.isEmpty(conversation.getFaceUrl())) {
            faceList.add(com.tencent.qcloud.tim.uikit.R.drawable.default_head);
        } else {
            faceList.add(conversation.getFaceUrl());
        }
        info.setIconUrlList(faceList);
        info.setId(conversation.getUserID());
        info.setConversationId(conversation.getConversationID());
        info.setGroup(isGroup);
        info.setUnRead(conversation.getUnreadCount());
        return info;
    }
}
