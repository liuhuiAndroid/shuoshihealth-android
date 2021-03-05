package com.ssh.shuoshi.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ssh.shuoshi.Constants;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.about.AboutActivity;
import com.ssh.shuoshi.ui.web.WebActivity;

public class AgreementTipsDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private ItemClickListener mItemClickListener;

    public AgreementTipsDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public AgreementTipsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected AgreementTipsDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agreement_tips_dialog);

        Button tvCancel = findViewById(R.id.btn_cancel);
        Button tvSave = findViewById(R.id.btn_ok);
        TextView tvDesc = findViewById(R.id.tv_desc);
        StringBuilder descBuilder = new StringBuilder();
        descBuilder.append("欢迎您使用悠安医生！\n1. 悠安医生是银川硕世互联网医院官方服务平台，我们非常重视保护您的隐私和个人信息。\n2. 我们会根据您使用服务的具体功能需要，收集必要的用户信息（如申请设备信息、存储、相机等相关权限）。\n3. 您可阅读完整版");
        int feetStart = descBuilder.length();
        descBuilder.append("《服务协议及隐私政策》");
        int feetEnd = descBuilder.length();
        descBuilder.append("了解我们申请使用相关权限的情况，以及对您个人隐私的保护措施。");
        SpannableString descSpannable = new SpannableString(descBuilder);
        descSpannable.setSpan(
                new ClickableSpan() {
                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.parseColor("#FFFF824D"));
                    }

                    @Override
                    public void onClick(@NonNull View widget) {
                        Intent intent2 = new Intent(mContext, WebActivity.class);
                        intent2.putExtra("url", Constants.WEB_002);
                        mContext.startActivity(intent2);
                    }
                }, feetStart, feetEnd,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        tvDesc.setMovementMethod(LinkMovementMethod.getInstance());
        tvDesc.setText(descSpannable);
        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                if (mItemClickListener != null) {
                    mItemClickListener.cancel();
                }
                break;
            case R.id.btn_ok:
                if (mItemClickListener != null) {
                    mItemClickListener.choose();
                }
                break;
        }
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void cancel();

        void choose();

    }

}
