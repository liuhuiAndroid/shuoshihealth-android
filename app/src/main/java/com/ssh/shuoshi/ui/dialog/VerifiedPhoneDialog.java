package com.ssh.shuoshi.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.inter.MyTextWatcher;
import com.ssh.shuoshi.library.util.ToastUtil;

//手机验证
public class VerifiedPhoneDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private String mPhone;
    private ItemClickListener mItemClickListener;
    private EditText etName;
    private TextView textPhone;
    private EditText etCode;

    public VerifiedPhoneDialog(@NonNull Context context, String phone) {
        super(context);
        this.mContext = context;
        this.mPhone = phone;
    }

    public VerifiedPhoneDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected VerifiedPhoneDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_verified_phone);

        ImageView imgClose = findViewById(R.id.img_close);
        textPhone = findViewById(R.id.textPhone);
        etCode = findViewById(R.id.etCode);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        etName = findViewById(R.id.et_name);

        textPhone.setText(mPhone);

        imgClose.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        etCode.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    btnSubmit.setEnabled(false);
                    return;
                }

                if (s.toString().length() >= 4) {
                    btnSubmit.setEnabled(true);
                } else {
                    btnSubmit.setEnabled(false);
                }
            }
        });

        final Window window = getWindow();
        assert window != null;
        window.setLayout(-1, -2);//这2行,和上面的一样,注意顺序就行;
        //设置背景颜色,只有设置了这个属性,宽度才能全屏MATCH_PARENT
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams mWindowAttributes = window.getAttributes();
        mWindowAttributes.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Window dialogWindow = getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.x = 0;
        lp.y = 0;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                if (mItemClickListener != null) {
                    mItemClickListener.cancel();
                }
                break;

            case R.id.btnSubmit:
                String code = etCode.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    ToastUtil.showToast("请输入验证码");
                    return;
                }

                if (mItemClickListener != null) {
                    mItemClickListener.submit(code);
                }
                break;
        }
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void cancel();

        void submit(String code);

    }

}
