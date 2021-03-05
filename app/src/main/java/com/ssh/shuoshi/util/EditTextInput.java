package com.ssh.shuoshi.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by hwt on 2020/12/11
 */
public class EditTextInput {

    /**
     * 传入区间值   假设:1-100   1-10
     *
     * @param edit    控件
     * @param context
     * @param max     最大数
     * @param min     最小数
     */
    public static void InputWatch(final EditText edit, final Context context, final int max, final int min) {

        edit.addTextChangedListener(new TextWatcher() {
            int l = 0;
            int location = 0;//记录光标的位置
            String data;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
                l = s.length();
                location = edit.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())) {
                    Pattern p = Pattern.compile("[0-9]*");
                    Matcher m = p.matcher(s.toString());
                    if (m.matches()) {
                        int number = Integer.parseInt(s.toString());
                        //如果起始位置为0,直接转成第二位
                        if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                            edit.setText(s.subSequence(1, 2));
                            edit.setSelection(1);
                            return;
                        }
                        if (number <= max && number >= min) {
                            data = s.toString();
                        } else {
                            edit.setText(data);
                            if (null != data) {
                                edit.setSelection(data.length());
                            }
                        }
                    } else {
                        edit.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }

}
