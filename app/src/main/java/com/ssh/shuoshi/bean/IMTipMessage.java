package com.ssh.shuoshi.bean;

/**
 * created by hwt on 2020/12/19
 */
public class IMTipMessage extends IMBaseMessage {

    private TipExtra content;

    public TipExtra getContent() {
        return content;
    }

    public void setContent(TipExtra content) {
        this.content = content;
    }
}
