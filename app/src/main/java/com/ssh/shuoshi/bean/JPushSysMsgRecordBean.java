package com.ssh.shuoshi.bean;

import java.util.List;

public class JPushSysMsgRecordBean {


    /**
     * total : 3
     * rows : [{"searchValue":null,"createBy":null,"createTime":"2021-01-06 20:24","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":1,"msgType":1,"pushTime":"2021-01-06","pushTitle":"资质认证提醒","pushContent":"你好，请先完成资质认证，认证即拥有您的线上工作室，随时为患者线上复诊调方","pushContentId":null,"receiveId":57,"result":null,"msgId":null,"code":null,"messsage":null,"dealFlag":null},{"searchValue":null,"createBy":null,"createTime":"2021-01-06 20:25","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":2,"msgType":1,"pushTime":"2021-01-06","pushTitle":"资质认证提醒","pushContent":"你好，请先完成资质认证，认证即拥有您的线上工作室，随时为患者线上复诊调方","pushContentId":null,"receiveId":57,"result":null,"msgId":null,"code":null,"messsage":null,"dealFlag":null},{"searchValue":null,"createBy":null,"createTime":"2021-01-06 20:29","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":3,"msgType":1,"pushTime":"2021-01-06","pushTitle":"资质认证提醒","pushContent":"你好，请先完成资质认证，认证即拥有您的线上工作室，随时为患者线上复诊调方","pushContentId":null,"receiveId":57,"result":1,"msgId":47287904911792946,"code":200,"messsage":null,"dealFlag":null}]
     * totalPage : 1
     */

    private int total;
    private int totalPage;
    private List<RowsBean> rows;

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

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * searchValue : null
         * createBy : null
         * createTime : 2021-01-06 20:24
         * updateBy : null
         * updateTime : null
         * remark : null
         * params : {}
         * id : 1
         * msgType : 1
         * pushTime : 2021-01-06
         * pushTitle : 资质认证提醒
         * pushContent : 你好，请先完成资质认证，认证即拥有您的线上工作室，随时为患者线上复诊调方
         * pushContentId : null
         * receiveId : 57
         * result : null
         * msgId : null
         * code : null
         * messsage : null
         * dealFlag : null
         */

        private Boolean isChecked;

        public Boolean isChecked() {
            return isChecked;
        }

        public void setChecked(Boolean checked) {
            isChecked = checked;
        }

        private Object searchValue;
        private Object createBy;
        private String createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private ParamsBean params;
        private int id;
        private int msgType;
        private String pushTime;
        private String pushTitle;
        private String pushContent;
        private Integer pushContentId;
        private int receiveId;
        private Object result;
        private Object msgId;
        private Object code;
        private Object messsage;
        private Boolean dealFlag;

        public Object getSearchValue() {
            return searchValue;
        }

        public void setSearchValue(Object searchValue) {
            this.searchValue = searchValue;
        }

        public Object getCreateBy() {
            return createBy;
        }

        public void setCreateBy(Object createBy) {
            this.createBy = createBy;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(Object updateBy) {
            this.updateBy = updateBy;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public ParamsBean getParams() {
            return params;
        }

        public void setParams(ParamsBean params) {
            this.params = params;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMsgType() {
            return msgType;
        }

        public void setMsgType(int msgType) {
            this.msgType = msgType;
        }

        public String getPushTime() {
            return pushTime;
        }

        public void setPushTime(String pushTime) {
            this.pushTime = pushTime;
        }

        public String getPushTitle() {
            return pushTitle;
        }

        public void setPushTitle(String pushTitle) {
            this.pushTitle = pushTitle;
        }

        public String getPushContent() {
            return pushContent;
        }

        public void setPushContent(String pushContent) {
            this.pushContent = pushContent;
        }

        public Integer getPushContentId() {
            return pushContentId;
        }

        public void setPushContentId(Integer pushContentId) {
            this.pushContentId = pushContentId;
        }

        public int getReceiveId() {
            return receiveId;
        }

        public void setReceiveId(int receiveId) {
            this.receiveId = receiveId;
        }

        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }

        public Object getMsgId() {
            return msgId;
        }

        public void setMsgId(Object msgId) {
            this.msgId = msgId;
        }

        public Object getCode() {
            return code;
        }

        public void setCode(Object code) {
            this.code = code;
        }

        public Object getMesssage() {
            return messsage;
        }

        public void setMesssage(Object messsage) {
            this.messsage = messsage;
        }

        public Boolean isDealFlag() {
            return dealFlag;
        }

        public void setDealFlag(Boolean dealFlag) {
            this.dealFlag = dealFlag;
        }

        public static class ParamsBean {
        }
    }
}
