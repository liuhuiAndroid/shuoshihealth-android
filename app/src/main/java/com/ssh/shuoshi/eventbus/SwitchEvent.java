package com.ssh.shuoshi.eventbus;

/**
 * created by hwt on 2021/1/8
 */
public class SwitchEvent {

    private String mType;

    public SwitchEvent(String type) {
        this.mType = type;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }
}
