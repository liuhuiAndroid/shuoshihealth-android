package com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.mmkv.MMKV;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.component.gatherimage.UserIconView;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.modules.chat.TeamBean;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;

import java.util.ArrayList;
import java.util.List;

public abstract class MessageContentHolder extends MessageEmptyHolder {

    public UserIconView leftUserIcon;
    public UserIconView rightUserIcon;
    public TextView usernameText;
    public LinearLayout msgContentLinear;
    public ProgressBar sendingProgress;
    public ImageView statusImage;
    public TextView isReadText;
    public TextView unreadAudioText;
    public TextView chatHint;

    public MessageContentHolder(View itemView) {
        super(itemView);
        rootView = itemView;

        chatHint = itemView.findViewById(R.id.chat_hint);
        leftUserIcon = itemView.findViewById(R.id.left_user_icon_view);
        rightUserIcon = itemView.findViewById(R.id.right_user_icon_view);
        usernameText = itemView.findViewById(R.id.user_name_tv);
        msgContentLinear = itemView.findViewById(R.id.msg_content_ll);
        statusImage = itemView.findViewById(R.id.message_status_iv);
        sendingProgress = itemView.findViewById(R.id.message_sending_pb);
        isReadText = itemView.findViewById(R.id.is_read_tv);
        unreadAudioText = itemView.findViewById(R.id.audio_unread);
    }

    public void setChatHint(String ass) {
        chatHint.setText(ass);
    }

    @Override
    public void layoutViews(final MessageInfo msg, final int position) {
        super.layoutViews(msg, position);

        if (TextUtils.isEmpty(chatHint.getText().toString().trim())) {
            chatHint.setVisibility(View.GONE);
        } else {
            chatHint.setVisibility(View.VISIBLE);
        }

        String imType = MMKV.defaultMMKV().getString("imType", "");
        TeamBean mBean = new Gson().fromJson(imType, TeamBean.class);

        //// ????????????
        if (msg.isSelf()) {
            leftUserIcon.setVisibility(View.GONE);
            rightUserIcon.setVisibility(View.VISIBLE);
        } else {
            leftUserIcon.setVisibility(View.VISIBLE);
            rightUserIcon.setVisibility(View.GONE);
        }
        if (properties.getAvatar() != 0) {
            leftUserIcon.setDefaultImageResId(properties.getAvatar());
            rightUserIcon.setDefaultImageResId(properties.getAvatar());
        } else {
            leftUserIcon.setDefaultImageResId(R.drawable.default_head);
            rightUserIcon.setDefaultImageResId(R.drawable.default_img);
        }
        if (properties.getAvatarRadius() != 0) {
            leftUserIcon.setRadius(properties.getAvatarRadius());
            rightUserIcon.setRadius(properties.getAvatarRadius());
        } else {
            leftUserIcon.setRadius(5);
            rightUserIcon.setRadius(5);
        }
        if (properties.getAvatarSize() != null && properties.getAvatarSize().length == 2) {
            ViewGroup.LayoutParams params = leftUserIcon.getLayoutParams();
            params.width = properties.getAvatarSize()[0];
            params.height = properties.getAvatarSize()[1];
            leftUserIcon.setLayoutParams(params);

            params = rightUserIcon.getLayoutParams();
            params.width = properties.getAvatarSize()[0];
            params.height = properties.getAvatarSize()[1];
            rightUserIcon.setLayoutParams(params);
        }
        leftUserIcon.invokeInformation(msg);
        rightUserIcon.invokeInformation(msg);

        //// ??????????????????
        if (msg.isSelf()) { // ??????????????????????????????
            if (properties.getRightNameVisibility() == 0) {
                usernameText.setVisibility(View.GONE);
            } else {
                usernameText.setVisibility(properties.getRightNameVisibility());
            }
        } else {
            if (properties.getLeftNameVisibility() == 0) {
                if (msg.isGroup()) { // ?????????????????????????????????
                    usernameText.setVisibility(View.VISIBLE);
                } else { // ?????????????????????????????????
                    usernameText.setVisibility(View.GONE);
                }
            } else {
                usernameText.setVisibility(properties.getLeftNameVisibility());
            }
        }
        if (properties.getNameFontColor() != 0) {
            usernameText.setTextColor(properties.getNameFontColor());
        }
        if (properties.getNameFontSize() != 0) {
            usernameText.setTextSize(properties.getNameFontSize());
        }
        // ?????????????????????????????????
        V2TIMMessage timMessage = msg.getTimMessage();
        List<TeamBean.Team> info = mBean.getInfo();
        String key = mBean.getKey();
        if (key.equals("image") || key.equals("video")) {
            usernameText.setText(info.get(0).getName());
        } else {
            for (int i = 0; i < info.size(); i++) {
                if (TextUtils.isEmpty(info.get(i).getTitleName())) {
                    usernameText.setText(info.get(i).getName());
                }

                if (info.get(i).getGroupId().equals(timMessage.getSender())) {
                    usernameText.setText(info.get(i).getName() + " " + info.get(i).getTitleName());
                }
            }
        }
//        if (!TextUtils.isEmpty(timMessage.getNameCard())) {
//            usernameText.setText(timMessage.getNameCard());
//        } else if (!TextUtils.isEmpty(timMessage.getFriendRemark())) {
//            usernameText.setText(timMessage.getFriendRemark());
//        } else if (!TextUtils.isEmpty(timMessage.getNickName())) {
//            usernameText.setText(timMessage.getNickName());
//        } else {
//            usernameText.setText(timMessage.getSender());
//        }

        if (!timMessage.getSender().startsWith("IM")) {
            if (!TextUtils.isEmpty(timMessage.getFaceUrl())) {
                if (!timMessage.getFaceUrl().contains("path=null")) {
                    List<Object> urllist = new ArrayList<>();
                    urllist.add(timMessage.getFaceUrl());
                    if (msg.isSelf()) {
                        rightUserIcon.setIconUrls(urllist);
                    } else {
                        for (int i = 0; i < info.size(); i++) {
                            if (info.get(i).getGroupId().equals(timMessage.getSender())) {
                                leftUserIcon.setIconUrls(urllist);
                            }
                        }
                    }
                } else {
                    if (msg.isSelf()) {
                        rightUserIcon.mIconView.setImageResource(R.drawable.default_img);
                    } else {
                        leftUserIcon.mIconView.setImageResource(R.drawable.default_head);
                    }
                }

            }
        } else {
            leftUserIcon.mIconView.setImageResource(R.drawable.default_head);
            rightUserIcon.mIconView.setImageResource(R.drawable.default_img);
        }

        if (msg.isSelf()) {
            if (msg.getStatus() == MessageInfo.MSG_STATUS_SEND_FAIL
                    || msg.getStatus() == MessageInfo.MSG_STATUS_SEND_SUCCESS
                    || msg.isPeerRead()) {
                sendingProgress.setVisibility(View.GONE);
            } else {
                sendingProgress.setVisibility(View.GONE);
            }
        } else {
            sendingProgress.setVisibility(View.GONE);
        }

        //// ??????????????????
        if (msg.isSelf()) {
            if (properties.getRightBubble() != null && properties.getRightBubble().getConstantState() != null) {
                msgContentFrame.setBackground(properties.getRightBubble().getConstantState().newDrawable());
            } else {
                msgContentFrame.setBackgroundResource(R.drawable.chat_bubble_myself);
            }
        } else {
            if (properties.getLeftBubble() != null && properties.getLeftBubble().getConstantState() != null) {
                msgContentFrame.setBackground(properties.getLeftBubble().getConstantState().newDrawable());
                msgContentFrame.setLayoutParams(msgContentFrame.getLayoutParams());
            } else {
                msgContentFrame.setBackgroundResource(R.drawable.chat_other_bg);
            }
        }

        //// ?????????????????????????????????
        if (onItemClickListener != null) {
            msgContentFrame.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onMessageLongClick(v, position, msg);
                    return true;
                }
            });
            leftUserIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onUserIconClick(view, position, msg);
                }
            });
            rightUserIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onUserIconClick(view, position, msg);
                }
            });
        }

        //// ?????????????????????
        if (msg.getStatus() == MessageInfo.MSG_STATUS_SEND_FAIL) {
            statusImage.setVisibility(View.VISIBLE);
            msgContentFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onMessageLongClick(msgContentFrame, position, msg);
                    }
                }
            });
        } else {
            msgContentFrame.setOnClickListener(null);
            statusImage.setVisibility(View.GONE);
        }

        //// ???????????????????????????????????????????????????
        if (msg.isSelf()) {
            msgContentLinear.removeView(msgContentFrame);
            msgContentLinear.addView(msgContentFrame);
        } else {
            msgContentLinear.removeView(msgContentFrame);
            msgContentLinear.addView(msgContentFrame, 0);
        }
        msgContentLinear.setVisibility(View.VISIBLE);

        //// ???????????????????????????
        if (TUIKitConfigs.getConfigs().getGeneralConfig().isShowRead()) {
            if (msg.isSelf()) {
                if (msg.isGroup()) {
                    isReadText.setVisibility(View.GONE);
                } else {
                    isReadText.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) isReadText.getLayoutParams();
                    params.gravity = Gravity.CENTER_VERTICAL;
                    isReadText.setLayoutParams(params);
                    if (msg.isPeerRead()) {
                        isReadText.setText(R.string.has_read);
                    } else {
                        isReadText.setText(R.string.unread);
                    }
                }
            } else {
                isReadText.setVisibility(View.GONE);
            }
        }

        //// ????????????
        unreadAudioText.setVisibility(View.GONE);

        //// ????????????????????????????????????views
        layoutVariableViews(msg, position);
    }

    public abstract boolean layoutVariableViews(final MessageInfo msg, final int position);
}
