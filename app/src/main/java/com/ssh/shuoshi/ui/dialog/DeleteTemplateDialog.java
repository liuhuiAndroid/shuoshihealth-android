package com.ssh.shuoshi.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ssh.shuoshi.R;

public class DeleteTemplateDialog extends Dialog implements View.OnClickListener {

    private ItemClickListener mItemClickListener;
    private String currentTitle;

    public DeleteTemplateDialog(@NonNull Context context) {
        super(context);
    }

    public DeleteTemplateDialog(@NonNull Context context, int themeResId, String title) {
        super(context, themeResId);
        currentTitle = title;
    }

    protected DeleteTemplateDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete_template);

        TextView tvTitle = findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(currentTitle)) {
            tvTitle.setText(currentTitle);
        }

        TextView tvCancel = findViewById(R.id.tv_cancel);
        TextView tvConfirm = findViewById(R.id.tv_confirm);

        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);

//        final Window window = getWindow();
//        assert window != null;
//        window.setLayout(-1, -2);//这2行,和上面的一样,注意顺序就行;
//        //设置背景颜色,只有设置了这个属性,宽度才能全屏MATCH_PARENT
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        WindowManager.LayoutParams mWindowAttributes = window.getAttributes();
//        mWindowAttributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
        Window dialogWindow = getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.x = 0;
        lp.y = 0;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                if (mItemClickListener != null) {
                    mItemClickListener.cancel();
                }
                break;

            case R.id.tv_confirm:
                if (mItemClickListener != null) {
                    mItemClickListener.confirm();
                }
                break;
        }
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void cancel();

        void confirm();

    }

}
