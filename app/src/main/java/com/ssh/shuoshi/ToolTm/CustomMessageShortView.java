package com.ssh.shuoshi.ToolTm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssh.shuoshi.MyApplication;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.ConsultationSummaryBean;
import com.ssh.shuoshi.bean.IMTipMessage;
import com.ssh.shuoshi.bean.IMVideoMessage;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.im.RecipeCardBean;
import com.ssh.shuoshi.bean.im.SummaryCardBean;
import com.ssh.shuoshi.ui.consultationsummary.ConsultationSummaryActivity;
import com.ssh.shuoshi.ui.graphicinquiry.GraphicInquiryActivity;
import com.ssh.shuoshi.ui.myprescription.detail.MyPrescriptionDetailActivity;
import com.ssh.shuoshi.util.DateUtil;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.MessageCustomHolder;

public class CustomMessageShortView {

    /**
     * 电子处方卡片
     */
    public static void onDrawPrescriptionCard(GraphicInquiryActivity activity, ICustomMessageViewGroup parent,
                                              final RecipeCardBean data, boolean isShow, boolean self) {
        refresh(parent);
        // 把自定义消息view添加到TUIKit内部的父容器里
        View view = LayoutInflater.from(activity).inflate(R.layout.item_message_card_short, null, false);
        if (self) {
            view.findViewById(R.id.linearContainer)
                    .setBackground(ContextCompat.getDrawable(activity, R.drawable.bg_im_message_right_white));
        } else {
            view.findViewById(R.id.linearContainer)
                    .setBackground(ContextCompat.getDrawable(activity, R.drawable.bg_im_message_left));
        }

        ImageView img = view.findViewById(R.id.iconImg);
        img.setImageResource(R.mipmap.icon_graphic_inquiry_card);
        // 标题
        TextView tvTitle = view.findViewById(R.id.message_short_title);
        tvTitle.setText("Rp.电子处方");
        parent.addMessageContentView(view);
        // 就诊人
        TextView tvName = view.findViewById(R.id.tv_name);
        tvName.setText(data.getContent().getPatientName());
        // 医师
        TextView tvDiagdesc = view.findViewById(R.id.tv_diagdesc);
        tvDiagdesc.setText(data.getContent().getDoctorName());
        TextView tv_detail = view.findViewById(R.id.message_short_detail);
        tv_detail.setOnClickListener(v -> {
            Intent intent = new Intent(activity, MyPrescriptionDetailActivity.class);
            intent.putExtra("prescriptionId", data.getContent().getId());
            activity.startActivityForResult(intent, 300);
        });
    }

    /**
     * 问诊小结卡片
     */
    public static void onDrawCard(Activity context, ICustomMessageViewGroup parent, final SummaryCardBean data,
                                  ImageDiagnoseBean.RowsBean rowsBean, boolean isShow, boolean self) {
        refresh(parent);
        // 把自定义消息view添加到TUIKit内部的父容器里
        View view = LayoutInflater.from(context).inflate(R.layout.item_message_card_short, null, false);
        if (self) {
            view.findViewById(R.id.linearContainer)
                    .setBackground(ContextCompat.getDrawable(context, R.drawable.bg_im_message_right_white));
        } else {
            view.findViewById(R.id.linearContainer)
                    .setBackground(ContextCompat.getDrawable(context, R.drawable.bg_im_message_left));
        }
        parent.addMessageContentView(view);
        // 标题
        TextView tvTitle = view.findViewById(R.id.message_short_title);
        tvTitle.setText("问诊小结");
        // 就诊人
        TextView tvName = view.findViewById(R.id.tv_name);
        tvName.setText(data.getContent().getPatientName());
        // 医师
        TextView tvDiagdesc = view.findViewById(R.id.tv_diagdesc);
        tvDiagdesc.setText(data.getContent().getDoctorName());
        // 查看详情
        TextView detail = view.findViewById(R.id.message_short_detail);
        detail.setOnClickListener(v -> {
            Integer emrDoctorId = rowsBean.getEmrDoctorId();
            if (emrDoctorId == null) {
                emrDoctorId = -1;
            }
            Intent intent = new Intent(context, ConsultationSummaryActivity.class);
            ConsultationSummaryBean cBean = new ConsultationSummaryBean(rowsBean.getId(),
                    rowsBean.getPatientName(), rowsBean.getSexName(), rowsBean.getPatientAge());
            intent.putExtra("rowsBean", cBean);
            // 问诊小结ID 没用，卡片需要填问诊id
            intent.putExtra("emrId", data.getContent().getId());
            intent.putExtra("emrDoctorId", emrDoctorId);
            context.startActivityForResult(intent, 101);
        });
        if (!isShow) {
            ViewGroup.LayoutParams layoutParams = ((MessageCustomHolder) parent).itemView.getLayoutParams();
            layoutParams.height = 0;
            ((MessageCustomHolder) parent).itemView.setLayoutParams(layoutParams);
            ((MessageCustomHolder) parent).itemView.setVisibility(View.GONE);
        }
    }

    /**
     * 普通的Tips消息
     */
    public static void onDrawTipsMessage(Context context, ICustomMessageViewGroup parent, final IMTipMessage data) {
        refresh(parent);
        // 把自定义消息view添加到TUIKit内部的父容器里
        View view = LayoutInflater.from(context).inflate(R.layout.item_text_short, null, false);
        TextView textView = view.findViewById(R.id.tv_text);
        textView.setText(data.getContent().getContent());
        parent.addMessageContentView(view);
        ((MessageCustomHolder) parent).rightUserIcon.setVisibility(View.GONE);
        ((MessageCustomHolder) parent).leftUserIcon.setVisibility(View.GONE);
        ((MessageCustomHolder) parent).usernameText.setVisibility(View.GONE);
    }

    /**
     * 退诊单独写一个View
     */
    public static void onDrawTipsMessageTZ(Context context, ICustomMessageViewGroup parent, final IMTipMessage data) {
        refresh(parent);
        // 把自定义消息view添加到TUIKit内部的父容器里
        View view = LayoutInflater.from(context).inflate(R.layout.item_text_short_tz, null, false);
        TextView textView = view.findViewById(R.id.tv_text);
        textView.setText(data.getContent().getContent());
        parent.addMessageContentView(view);
        ((MessageCustomHolder) parent).rightUserIcon.setVisibility(View.GONE);
        ((MessageCustomHolder) parent).leftUserIcon.setVisibility(View.GONE);
        ((MessageCustomHolder) parent).usernameText.setVisibility(View.GONE);
    }

    /**
     * 单独处理温馨提示
     */
    public static void onDrawTipsMessgaeWXTS(Context context, ICustomMessageViewGroup parent,
                                             final String tips, boolean isCenter) {
        refresh(parent);
        // 把自定义消息view添加到TUIKit内部的父容器里
        View view = LayoutInflater.from(context).inflate(R.layout.item_text_for_wxts, null, false);
        TextView textView = view.findViewById(R.id.tv_text);
        if (isCenter) {
            textView.setGravity(Gravity.CENTER);
        } else {
            textView.setGravity(Gravity.LEFT);
        }
        textView.setText(tips);
        parent.addMessageContentView(view);
        ((MessageCustomHolder) parent).rightUserIcon.setVisibility(View.GONE);
        ((MessageCustomHolder) parent).leftUserIcon.setVisibility(View.GONE);
        ((MessageCustomHolder) parent).usernameText.setVisibility(View.GONE);
    }


    public static void onDrawTipsMessgaeCenter(Context context, ICustomMessageViewGroup parent,
                                               final String tips, boolean isShow) {
        refresh(parent);
        if (isShow) {
            // 把自定义消息view添加到TUIKit内部的父容器里
            View view = LayoutInflater.from(context).inflate(R.layout.item_text_center, null, false);
            TextView textView = view.findViewById(R.id.tv_text);
            textView.setText(tips);
            parent.addMessageContentView(view);
            ((MessageCustomHolder) parent).rightUserIcon.setVisibility(View.GONE);
            ((MessageCustomHolder) parent).leftUserIcon.setVisibility(View.GONE);
            ((MessageCustomHolder) parent).usernameText.setVisibility(View.GONE);
        } else {
            ViewGroup.LayoutParams layoutParams = ((MessageCustomHolder) parent).itemView.getLayoutParams();
            layoutParams.height = 0;
            ((MessageCustomHolder) parent).itemView.setLayoutParams(layoutParams);
            ((MessageCustomHolder) parent).itemView.setVisibility(View.GONE);
            ((MessageCustomHolder) parent).usernameText.setVisibility(View.GONE);
        }
    }

    /**
     * 绘制两边带分割线的提示文本消息
     */
    public static void onDrawEndMessgae(Context context, ICustomMessageViewGroup parent, final String tips) {
        refresh(parent);
        // 把自定义消息view添加到TUIKit内部的父容器里
        View view = LayoutInflater.from(context).inflate(R.layout.item_text_short_with_line, null, false);
        TextView textView = view.findViewById(R.id.tv_text);
        textView.setText(tips);
        parent.addMessageContentView(view);
        ((MessageCustomHolder) parent).rightUserIcon.setVisibility(View.GONE);
        ((MessageCustomHolder) parent).leftUserIcon.setVisibility(View.GONE);
        ((MessageCustomHolder) parent).usernameText.setVisibility(View.GONE);
    }

    // 视频消息
    public static void onDrawVideoMessgae(Context context, ICustomMessageViewGroup parent, IMVideoMessage videoMessage, boolean self) {
        refresh(parent);
        if (videoMessage.getContent().getAction() == 5 || videoMessage.getContent().getAction() == 1
                || videoMessage.getContent().getAction() == 2) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_im_video, null, false);
            if (self) {
                view.findViewById(R.id.linearContainer)
                        .setBackground(ContextCompat.getDrawable(context, R.drawable.bg_im_message_right_white));
            } else {
                view.findViewById(R.id.linearContainer)
                        .setBackground(ContextCompat.getDrawable(context, R.drawable.bg_im_message_left));
            }
            TextView textView = view.findViewById(R.id.tv_text);
            if (videoMessage.getContent().getAction() == 5) {
                textView.setText("视频聊天时长 " + DateUtil.secToTime(videoMessage.getContent().getDuration().intValue()));
            } else if (videoMessage.getContent().getAction() == 1) {
                textView.setText("取消视频");
            } else if (videoMessage.getContent().getAction() == 2) {
                textView.setText("已拒绝");
            }
            parent.addMessageContentView(view);
        } else {
            ViewGroup.LayoutParams layoutParams = ((MessageCustomHolder) parent).itemView.getLayoutParams();
            layoutParams.height = 0;
            ((MessageCustomHolder) parent).itemView.setLayoutParams(layoutParams);
            ((MessageCustomHolder) parent).itemView.setVisibility(View.GONE);
        }
    }

    public static void refresh(ICustomMessageViewGroup parent) {
        ViewGroup.LayoutParams layoutParams = ((MessageCustomHolder) parent).itemView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        ((MessageCustomHolder) parent).itemView.setLayoutParams(layoutParams);
        ((MessageCustomHolder) parent).itemView.setVisibility(View.VISIBLE);
    }

    public static void refreshNone(ICustomMessageViewGroup parent) {
        ViewGroup.LayoutParams layoutParams = ((MessageCustomHolder) parent).itemView.getLayoutParams();
        layoutParams.height = 0;
        ((MessageCustomHolder) parent).itemView.setLayoutParams(layoutParams);
        ((MessageCustomHolder) parent).itemView.setVisibility(View.GONE);
    }
}
