package com.ssh.shuoshi.eventbus;

/**
 * 用于重新更新医生信息
 * type  1更新
 * created by hwt on 2020/12/14
 */
public class ChangeEvent {

    private int mChangeType;

    public ChangeEvent(int changeType) {
        this.mChangeType = changeType;
    }

    public int getChangeType() {
        return mChangeType;
    }

    public void setChangeType(int mChangeType) {
        this.mChangeType = mChangeType;
    }
}
