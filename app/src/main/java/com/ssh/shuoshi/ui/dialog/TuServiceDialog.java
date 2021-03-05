package com.ssh.shuoshi.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.util.EditTextWatch;

/**
 * created by hwt on 2020/12/10
 */
public class TuServiceDialog extends Dialog implements View.OnClickListener {

    private String mTitle;
    private Context mContext;
    private ItemClickListener mItemClickListener;
    private EditText etNumber;
    private static final int MIN = 0;
    private static final int MAX = 2000;

    public TuServiceDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public TuServiceDialog(@NonNull Context context, String title, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        this.mTitle = title;
    }

    public TuServiceDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected TuServiceDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tu_service_dialog);

        TextView tvTile = findViewById(R.id.tv_tile);
        TextView tvCancel = findViewById(R.id.tv_cancel);
        TextView tvSave = findViewById(R.id.tv_save);
        etNumber = findViewById(R.id.et_number);

        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);

        if (!TextUtils.isEmpty(mTitle)) {
            tvTile.setText(mTitle);
        }
        etNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etNumber.addTextChangedListener(new EditTextWatch(etNumber, MAX, MIN));
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height= ViewGroup.LayoutParams.MATCH_PARENT;
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
                String trim = etNumber.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    ToastUtil.showToast("请输入价格");
                    return;
                }
                if (mItemClickListener != null) {
                    mItemClickListener.save(trim);
                }
                break;
        }
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void cancel();

        void save(String number);

    }

}
