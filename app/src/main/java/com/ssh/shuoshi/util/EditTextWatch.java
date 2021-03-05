package com.ssh.shuoshi.util;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 默认限制小数点2位
 * 默认第一位输入小数点时，转换为0.
 * 如果起始位置为0,且第二位跟的不是".",则无法后续输入
 * created by hwt on 2020/12/31
 */
public class EditTextWatch implements TextWatcher {

    private int MAX = 0;
    private int MIN = 0;
    private EditText editText;
    private int digits = 2;
    private String data;

    public EditTextWatch(EditText et) {
        editText = et;
    }

    public EditTextWatch(EditText et, int max, final int min) {
        editText = et;
        this.MAX = max;
        this.MIN = min;
    }

    //自定义位数
    public EditTextWatch setDigits(int d) {
        digits = d;
        return this;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!TextUtils.isEmpty(s.toString())) {
            Pattern p = Pattern.compile("[0-9]*");
            Matcher m = p.matcher(s.toString());
            if (m.matches()) {
                int number = Integer.parseInt(s.toString());
                if (number <= MAX && number >= MIN) {
                    data = s.toString();
                } else {
                    editText.setText(data);
                    editText.setSelection(data.length());
                }
            }
        }

        //删除“.”后面超过2位后的数据
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > digits) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(".") + digits + 1);
                editText.setText(s);
                editText.setSelection(s.length()); //光标移到最后
            }
        }
        //如果"."在起始位置,则起始位置自动补0
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            editText.setText(s);
            editText.setSelection(2);
        }

        //如果起始位置为0,且第二位跟的不是".",则无法后续输入
        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                editText.setText(s.subSequence(0, 1));
                editText.setSelection(1);
                return;
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
