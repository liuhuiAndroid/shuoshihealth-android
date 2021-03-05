package com.ssh.shuoshi.eventbus;

/**
 * 改变图文数量
 * created by hwt on 2020/12/17
 */
public class ChangeTuNumEvent {

    private int mState;
    private String mType;
    private int mNum;

    public ChangeTuNumEvent(String type, int num, int state) {
        this.mType = type;
        this.mNum = num;
        this.mState = state;

    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        this.mState = state;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public int getNum() {
        return mNum;
    }

    public void setNum(int mNum) {
        this.mNum = mNum;
    }
}
