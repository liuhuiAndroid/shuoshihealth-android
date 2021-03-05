package com.ssh.shuoshi.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ssh.shuoshi.R;

public class SamplePhotoDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private int mType;

    public SamplePhotoDialog(@NonNull Context context) {
        super(context);
    }

    public SamplePhotoDialog(@NonNull Context context, int themeResId,int type) {
        super(context, themeResId);
        this.mContext = context;
        this.mType = type;
    }

    protected SamplePhotoDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sample_photo);

        ImageView imageViewClose = findViewById(R.id.imgClose);
        ImageView imgOne = findViewById(R.id.imgOne);
        ImageView imgTwo = findViewById(R.id.imgTwo);
        ImageView imgThree = findViewById(R.id.imgThree);
        TextView title = findViewById(R.id.textOne);
        TextView hint = findViewById(R.id.textTwo);

        switch (mType) {
            case 1:
                imgOne.setImageDrawable(mContext.getResources().getDrawable(R.drawable.zhiye_one));
                imgTwo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.zhiye_two));
                imgThree.setImageDrawable(mContext.getResources().getDrawable(R.drawable.zhiye_three));
                title.setText("执业证书拍摄要求");
                hint.setText("注意：照片中需包含姓名、证书编码、执业地点等");
                break;

            case 2:
                imgOne.setImageDrawable(mContext.getResources().getDrawable(R.drawable.zige_one));
                imgTwo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.zige_two));
                imgThree.setImageDrawable(mContext.getResources().getDrawable(R.drawable.zige_three));
                title.setText("资格证书拍摄要求");
                hint.setText("注意：照片中需包含姓名、证书编码、学历等");
                break;

            case 3:
                imgOne.setImageDrawable(mContext.getResources().getDrawable(R.drawable.zhicheng_one));
                imgTwo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.zhicheng_two));
                imgThree.setImageDrawable(mContext.getResources().getDrawable(R.drawable.zhicheng_three));
                title.setText("职称证书拍摄要求");
                hint.setText("注意：照片中需包含姓名、工作单位、任职资格等");
                break;
        }

        imageViewClose.setOnClickListener(v -> dismiss());


//        final Window window = getWindow();
//        assert window != null;
//        window.getDecorView().setPadding(0, 0, 0, 0);
//        window.setLayout(-1, -1);//这2行,和上面的一样,注意顺序就行;
//        //设置背景颜色,只有设置了这个属性,宽度才能全屏MATCH_PARENT
//        window.setBackgroundDrawable(new ColorDrawable(R.color.transparent_half));
//        WindowManager.LayoutParams mWindowAttributes = window.getAttributes();
//        mWindowAttributes.height = WindowManager.LayoutParams.MATCH_PARENT;
//
//        Window dialogWindow = getWindow();
//        assert dialogWindow != null;
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.CENTER);
//        lp.x = 0;
//        lp.y = 0;
//        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
//        dialogWindow.setAttributes(lp);
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

    private View.OnClickListener btnClick;

    public void setBtnGoClick(View.OnClickListener btnClick) {
        this.btnClick = btnClick;
    }

    @Override
    public void onClick(View v) {

    }
}
