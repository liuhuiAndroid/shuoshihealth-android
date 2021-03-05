package com.ssh.shuoshi.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ssh.shuoshi.R;

/**
 * 通用Dialog
 * created by hwt on 2021/1/27
 */
public class CommonDialog extends Dialog implements View.OnClickListener {

    private String mContent;
    private Context mContext;
    private ItemClickListener mItemClickListener;

    public CommonDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public CommonDialog(@NonNull Context context, String content, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        this.mContent = content;
    }

    public CommonDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected CommonDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_dialog);

        TextView tvCancel = findViewById(R.id.tv_cancel);
        TextView textContent = findViewById(R.id.textContent);
        TextView tvSave = findViewById(R.id.tv_save);

        textContent.setText(mContent);
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
            case R.id.tv_cancel:
                if (mItemClickListener != null) {
                    mItemClickListener.cancel();
                }
                break;

            case R.id.tv_save:
                if (mItemClickListener != null) {
                    mItemClickListener.save();
                }
                break;
        }
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void cancel();

        void save();

    }
}
