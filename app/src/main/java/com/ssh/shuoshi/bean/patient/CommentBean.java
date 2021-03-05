package com.ssh.shuoshi.bean.patient;

import java.util.List;

/**
 * 患者评价
 * created by hwt on 2021/1/6
 */
public class CommentBean {

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
         * createTime : 2020-12-23
         * updateBy : null
         * updateTime : null
         * remark : null
         * params : {}
         * id : 16
         * patientId : null
         * doctorId : 61
         * star : 3
         * content : 很好啊 啊 啊啊
         * reviewFlag : 1
         * consultationId : 18
         * doctorName : dtb
         * patientName : null
         * patientUserId : null
         */

        private Object searchValue;
        private Object createBy;
        private String createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private ParamsBean params;
        private int id;
        private Object patientId;
        private int doctorId;
        private int star;
        private String content;
        private int reviewFlag;
        private int consultationId;
        private String doctorName;
        private String patientName;
        private Object patientUserId;

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

        public Object getPatientId() {
            return patientId;
        }

        public void setPatientId(Object patientId) {
            this.patientId = patientId;
        }

        public int getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(int doctorId) {
            this.doctorId = doctorId;
        }

        public int getStar() {
            return star;
        }

        public void setStar(int star) {
            this.star = star;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getReviewFlag() {
            return reviewFlag;
        }

        public void setReviewFlag(int reviewFlag) {
            this.reviewFlag = reviewFlag;
        }

        public int getConsultationId() {
            return consultationId;
        }

        public void setConsultationId(int consultationId) {
            this.consultationId = consultationId;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public Object getPatientUserId() {
            return patientUserId;
        }

        public void setPatientUserId(Object patientUserId) {
            this.patientUserId = patientUserId;
        }

        public static class ParamsBean {
        }
    }
}
