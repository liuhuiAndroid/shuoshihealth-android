package com.ssh.shuoshi.eventbus;

/**
 * created by hwt on 2020/12/10
 */
public class CustomEvent {
    private String mText;

    public CustomEvent(String text) {
        this.mText = text;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }
}
