package com.ssh.shuoshi.bean.pickview;

import com.ssh.shuoshi.view.wheelview.interfaces.IPickerViewData;

import java.util.List;

/**
 * created by hwt on 2021/1/17
 */
public class SysDeptNameBean {

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

    public static class RowsBean implements IPickerViewData {
        /**
         * searchValue : null
         * createBy : null
         * createTime : 2020-12-09 17:26:55
         * updateBy : null
         * updateTime : null
         * remark : null
         * params : {}
         * id : 1
         * parentId : 0
         * deptCode : YH00001
         * deptName : 妇科
         * deptTypeId : 1
         * deptAlias : 妇科
         * deptDesc : null
         * enableFlag : 1
         * serialNo : 1
         * hospitalId : 1
         * operator : admin
         */

        private Object searchValue;
        private Object createBy;
        private String createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private ParamsBean params;
        private int id;
        private int parentId;
        private String deptCode;
        private String deptName;
        private int deptTypeId;
        private String deptAlias;
        private Object deptDesc;
        private int enableFlag;
        private int serialNo;
        private int hospitalId;
        private String operator;
        private String titleName;

        private String cityCode;
        private int cityLevel;
        private String cityNameEn;
        private String cityNameZh;

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public int getCityLevel() {
            return cityLevel;
        }

        public void setCityLevel(int cityLevel) {
            this.cityLevel = cityLevel;
        }

        public String getCityNameEn() {
            return cityNameEn;
        }

        public void setCityNameEn(String cityNameEn) {
            this.cityNameEn = cityNameEn;
        }

        public String getCityNameZh() {
            return cityNameZh;
        }

        public void setCityNameZh(String cityNameZh) {
            this.cityNameZh = cityNameZh;
        }

        public String getTitleName() {
            return titleName;
        }

        public void setTitleName(String titleName) {
            this.titleName = titleName;
        }

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

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public String getDeptCode() {
            return deptCode;
        }

        public void setDeptCode(String deptCode) {
            this.deptCode = deptCode;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public int getDeptTypeId() {
            return deptTypeId;
        }

        public void setDeptTypeId(int deptTypeId) {
            this.deptTypeId = deptTypeId;
        }

        public String getDeptAlias() {
            return deptAlias;
        }

        public void setDeptAlias(String deptAlias) {
            this.deptAlias = deptAlias;
        }

        public Object getDeptDesc() {
            return deptDesc;
        }

        public void setDeptDesc(Object deptDesc) {
            this.deptDesc = deptDesc;
        }

        public int getEnableFlag() {
            return enableFlag;
        }

        public void setEnableFlag(int enableFlag) {
            this.enableFlag = enableFlag;
        }

        public int getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(int serialNo) {
            this.serialNo = serialNo;
        }

        public int getHospitalId() {
            return hospitalId;
        }

        public void setHospitalId(int hospitalId) {
            this.hospitalId = hospitalId;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        @Override
        public String getPickerViewText() {
            return this.deptName;
        }

        public static class ParamsBean {
        }
    }
}
