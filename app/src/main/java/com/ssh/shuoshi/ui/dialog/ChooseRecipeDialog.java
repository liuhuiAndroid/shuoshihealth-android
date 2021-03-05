package com.ssh.shuoshi.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.library.util.ToastUtil;

/**
 * created by hwt on 2020/12/20
 */
public class ChooseRecipeDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private ItemClickListener mItemClickListener;

    private Integer choose = 2;
    private TextView tvXi;
    private TextView tvZhong;

    public ChooseRecipeDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public ChooseRecipeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected ChooseRecipeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_recipe_dialog);

        TextView tvCancel = findViewById(R.id.tv_cancel);
        TextView tvSave = findViewById(R.id.tv_save);
        tvXi = findViewById(R.id.tv_xi);
        tvZhong = findViewById(R.id.tv_zhong);

        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        tvXi.setOnClickListener(this);
        tvZhong.setOnClickListener(this);
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
                if (choose == null) {
                    ToastUtil.showToast("请选择处方类型");
                    return;
                }
                if (mItemClickListener != null) {
                    mItemClickListener.choose(choose);
                }
                break;

            case R.id.tv_xi:
                tvXi.setTextColor(mContext.getResources().getColor(R.color.orange));
                tvZhong.setTextColor(mContext.getResources().getColor(R.color.black));
                tvXi.setBackgroundResource(R.drawable.tv_orange_round);
                tvZhong.setBackgroundResource(R.drawable.tv_grey_round);
                choose = 1;
                break;

            case R.id.tv_zhong:
                tvXi.setTextColor(mContext.getResources().getColor(R.color.black));
                tvZhong.setTextColor(mContext.getResources().getColor(R.color.orange));
                tvXi.setBackgroundResource(R.drawable.tv_grey_round);
                tvZhong.setBackgroundResource(R.drawable.tv_orange_round);
                choose = 2;
                break;
        }
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void cancel();

        void choose(Integer choose);

    }

}
