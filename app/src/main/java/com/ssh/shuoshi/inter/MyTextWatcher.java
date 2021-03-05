package com.ssh.shuoshi.inter;

import android.text.Editable;
import android.text.TextWatcher;

public interface MyTextWatcher extends TextWatcher {

    @Override
    default void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // NOTHING
    }

    @Override
    default void onTextChanged(CharSequence s, int start, int before, int count) {
        // NOTHING
    }

    @Override
    void afterTextChanged(Editable s);
}
