package com.ssh.shuoshi.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.util.EditTextWatch;

/**
 * created by hwt on 2020/12/10
 */
public class VideoServiceDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private ItemClickListener mItemClickListener;
    private EditText etNumber;
    private static final int MIN = 0;
    private static final int MAX = 2000;

    public VideoServiceDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public VideoServiceDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected VideoServiceDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"WrongConstant", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_service_dialog);
        getWindow().setLayout((ViewGroup.LayoutParams.MATCH_PARENT), ViewGroup.LayoutParams.MATCH_PARENT);
        TextView tvCancel = findViewById(R.id.tv_cancel);
        TextView tvSave = findViewById(R.id.tv_save);
        etNumber = findViewById(R.id.et_number);

        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);

//        EditTextInput.InputWatch(etNumber, mContext, MAX, MIN);
        etNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etNumber.addTextChangedListener(new EditTextWatch(etNumber, MAX, MIN));
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
