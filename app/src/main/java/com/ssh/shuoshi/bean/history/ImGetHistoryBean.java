package com.ssh.shuoshi.bean.history;

import java.util.List;

public class ImGetHistoryBean {

    private int total;
    private int totalPage;
    private List<ImGetHistoryBean.RowsBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<ImGetHistoryBean.RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<ImGetHistoryBean.RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {

        private VirtualDomBean virtualDom;
        private String conversationID;
        private boolean isRead;
        private String type;
        private String conversationType;
        private String nick;
        private PayloadBean payload;
        private String from;
        private int ID;
        private String time;
        private String to;
        private String flow;
        private boolean isResend;


        public VirtualDomBean getVirtualDom() {
            return virtualDom;
        }

        public void setVirtualDom(VirtualDomBean virtualDom) {
            this.virtualDom = virtualDom;
        }

        public String getConversationID() {
            return conversationID;
        }

        public void setConversationID(String conversationID) {
            this.conversationID = conversationID;
        }

        public boolean isIsRead() {
            return isRead;
        }

        public void setIsRead(boolean isRead) {
            this.isRead = isRead;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getConversationType() {
            return conversationType;
        }

        public void setConversationType(String conversationType) {
            this.conversationType = conversationType;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public PayloadBean getPayload() {
            return payload;
        }

        public void setPayload(PayloadBean payload) {
            this.payload = payload;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getFlow() {
            return flow;
        }

        public void setFlow(String flow) {
            this.flow = flow;
        }

        public boolean isIsResend() {
            return isResend;
        }

        public void setIsResend(boolean isResend) {
            this.isResend = isResend;
        }

    }
}
