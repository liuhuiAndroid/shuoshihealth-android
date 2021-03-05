package com.ssh.shuoshi.bean;

import java.util.List;

//获取医生问诊列表
public class DoctorConsultationInfo {

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

        private Object searchValue;
        private Object createBy;
        private String createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private ParamsBean params;
        private int id;
        private int doctorId;
        private int consultationTypeId;
        private String price;

        // 	0未开启，1开启
        private Integer enableFlag;

        private Object doctorName;
        private Object consultationTypeName;

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

        public int getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(int doctorId) {
            this.doctorId = doctorId;
        }

        public int getConsultationTypeId() {
            return consultationTypeId;
        }

        public void setConsultationTypeId(int consultationTypeId) {
            this.consultationTypeId = consultationTypeId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public Integer getEnableFlag() {
            return enableFlag;
        }

        public void setEnableFlag(Integer enableFlag) {
            this.enableFlag = enableFlag;
        }

        public Object getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(Object doctorName) {
            this.doctorName = doctorName;
        }

        public Object getConsultationTypeName() {
            return consultationTypeName;
        }

        public void setConsultationTypeName(Object consultationTypeName) {
            this.consultationTypeName = consultationTypeName;
        }

        public static class ParamsBean {
        }
    }
}
