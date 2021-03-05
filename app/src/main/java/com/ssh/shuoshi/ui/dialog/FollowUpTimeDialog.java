package com.ssh.shuoshi.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.library.util.ToastUtil;

public class FollowUpTimeDialog extends Dialog implements View.OnClickListener {

    private FollowUpTimeDialog.ItemClickListener mItemClickListener;
    private ImageDiagnoseBean.RowsBean bean;

    private EditText et_number;

    public FollowUpTimeDialog(@NonNull Context context, ImageDiagnoseBean.RowsBean bean) {
        super(context);
        this.bean = bean;
    }

    public FollowUpTimeDialog(@NonNull Context context, int themeResId, ImageDiagnoseBean.RowsBean bean) {
        super(context, themeResId);
        this.bean = bean;
    }

    protected FollowUpTimeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_follow_up_time);

        TextView info = findViewById(R.id.info);
        info.setText(bean.getPatientName() + " " + bean.getSexName() + " " + bean.getPatientAge() + "岁");

        TextView tvCancel = findViewById(R.id.tv_cancel);
        TextView tvConfirm = findViewById(R.id.tv_confirm);
        et_number = findViewById(R.id.et_number);

        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);

        final Window window = getWindow();
        assert window != null;
        window.setLayout(-1, -2);//这2行,和上面的一样,注意顺序就行;
//        //设置背景颜色,只有设置了这个属性,宽度才能全屏MATCH_PARENT
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        WindowManager.LayoutParams mWindowAttributes = window.getAttributes();
//        mWindowAttributes.height = WindowManager.LayoutParams.WRAP_CONTENT;

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
                String trim = et_number.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    ToastUtil.showToast("请输入天数");
                    return;
                }

                int dayNum = Integer.parseInt(trim);
                if (dayNum == 0) {
                    ToastUtil.showToast("随访天数应大于0");
                    return;
                }

                if (mItemClickListener != null) {
                    mItemClickListener.confirm(trim);
                }
                break;
        }
    }

    public void setOnItemClickListener(FollowUpTimeDialog.ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void cancel();

        void confirm(String name);

    }

}
