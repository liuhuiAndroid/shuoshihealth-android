package com.ssh.shuoshi.eventbus;

public class NotificationExtras2Event {

    private int msgType;
    private int bussinessId;

    public NotificationExtras2Event(int msgType, int bussinessId) {
        this.msgType = msgType;
        this.bussinessId = bussinessId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getBussinessId() {
        return bussinessId;
    }

    public void setBussinessId(int bussinessId) {
        this.bussinessId = bussinessId;
    }
}
