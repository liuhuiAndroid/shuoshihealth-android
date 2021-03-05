package com.ssh.shuoshi.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.ssh.shuoshi.R;

public class PhysicianCertificationDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private LinearLayout llHead;

    public PhysicianCertificationDialog(@NonNull Context context) {
        super(context);
    }

    public PhysicianCertificationDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected PhysicianCertificationDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_physician_certification);

        ImageView imageViewClose = findViewById(R.id.imageViewClose);
        llHead = findViewById(R.id.ll_view);
        imageViewClose.setOnClickListener(v -> dismiss());
        Button buttonGo = findViewById(R.id.buttonGo);
        if (btnClick != null) {
            buttonGo.setOnClickListener(btnClick);
        }

//        final Window window = getWindow();
//        assert window != null;
//        window.setLayout(-1, -2);//这2行,和上面的一样,注意顺序就行;
//        window.getDecorView().setClipToOutline(false);
//        //设置背景颜色,只有设置了这个属性,宽度才能全屏MATCH_PARENT
////        window.setBackgroundDrawable(new ColorDrawable(R.color.transparent_half));
//        WindowManager.LayoutParams mWindowAttributes = window.getAttributes();
//        mWindowAttributes.height = WindowManager.LayoutParams.MATCH_PARENT;

        Window dialogWindow = getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        dialogWindow.getDecorView().setClipToOutline(false);
        lp.x = 0;
        lp.y = 0;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void show() {
        super.show();
//        Window dialogWindow = getWindow();
//        assert dialogWindow != null;
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llHead.getLayoutParams();
//        DisplayMetrics dm = new DisplayMetrics();
//        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//        manager.getDefaultDisplay().getMetrics(dm);
//        layoutParams.width = dm.widthPixels;
//        layoutParams.height = dm.heightPixels;
//        llHead.setLayoutParams(layoutParams);
    }

    private View.OnClickListener btnClick;

    public void setBtnGoClick(View.OnClickListener btnClick) {
        this.btnClick = btnClick;
    }

    @Override
    public void onClick(View v) {

    }
}
