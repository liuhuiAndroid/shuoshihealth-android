package com.ssh.shuoshi.bean;

public class NotificationExtrasBean {
    private ExtraBean extraBean;

    public ExtraBean getExtraBean() {
        return extraBean;
    }

    public void setExtraBean(ExtraBean extraBean) {
        this.extraBean = extraBean;
    }

    public class ExtraBean{
        private int msgType;
        private int bussinessId;

        public int getBussinessId() {
            return bussinessId;
        }

        public void setBussinessId(int bussinessId) {
            this.bussinessId = bussinessId;
        }

        public int getMsgType() {
            return msgType;
        }

        public void setMsgType(int msgType) {
            this.msgType = msgType;
        }
    }
}
