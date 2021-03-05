package com.ssh.shuoshi.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ssh.shuoshi.R;


/**
 * Created by weiyun on 2019/5/29.
 */

public class LoadingDialog extends Dialog {

    private String mContent;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
    }

    public LoadingDialog(Context context, String content) {
        super(context, R.style.MyDialog);
        this.mContent = content;
    }

    public static LoadingDialog show(Context context, String content) {
        LoadingDialog loadingDialog = new LoadingDialog(context, content);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
        return loadingDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
        TextView mTxtContent = (TextView) findViewById(R.id.dialog_loading_text);
        if (!TextUtils.isEmpty(mContent)) {
            mTxtContent.setText(mContent);
        }
    }
}
