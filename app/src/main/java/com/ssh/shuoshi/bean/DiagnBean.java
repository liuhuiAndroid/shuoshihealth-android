package com.ssh.shuoshi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * created by hwt on 2020/12/18
 */
public class DiagnBean implements Serializable{

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

    public static class RowsBean implements Serializable {
        /**
         * searchValue : null
         * createBy : null
         * createTime : null
         * updateBy : null
         * updateTime : null
         * remark : null
         * params : {}
         * id : 2492
         * diagnName : 子宫肌层恶性肿瘤
         * inputCode : ZGJCEXZL
         * operator : null
         * diagnType : 1
         */

        private Object searchValue;
        private Object createBy;
        private Object createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private ParamsBean params;
        private int id;
        private String diagnName;
        private String inputCode;
        private Object operator;
        private int diagnType;

        private String phamOutCode;
        private String phamName;
        private Object phamAliasName;
        private Object phamStdCode;
        private String phamSpec;
        private Object phamUnit;
        private Object phamForm;
        private int tradePrice;
        private int retailPrice;
        private Object phamAmount;
        private Object manufactor;
        private Object phamCategoryId;
        private Object enableFlag;
        private Object phamTypeId;
        private Object unitName;
        private Object phamTypeName;
        private boolean offenFlag;

        public String getPhamOutCode() {
            return phamOutCode;
        }

        public void setPhamOutCode(String phamOutCode) {
            this.phamOutCode = phamOutCode;
        }

        public String getPhamName() {
            return phamName;
        }

        public void setPhamName(String phamName) {
            this.phamName = phamName;
        }

        public Object getPhamAliasName() {
            return phamAliasName;
        }

        public void setPhamAliasName(Object phamAliasName) {
            this.phamAliasName = phamAliasName;
        }

        public Object getPhamStdCode() {
            return phamStdCode;
        }

        public void setPhamStdCode(Object phamStdCode) {
            this.phamStdCode = phamStdCode;
        }

        public String getPhamSpec() {
            return phamSpec;
        }

        public void setPhamSpec(String phamSpec) {
            this.phamSpec = phamSpec;
        }

        public Object getPhamUnit() {
            return phamUnit;
        }

        public void setPhamUnit(Object phamUnit) {
            this.phamUnit = phamUnit;
        }

        public Object getPhamForm() {
            return phamForm;
        }

        public void setPhamForm(Object phamForm) {
            this.phamForm = phamForm;
        }

        public int getTradePrice() {
            return tradePrice;
        }

        public void setTradePrice(int tradePrice) {
            this.tradePrice = tradePrice;
        }

        public int getRetailPrice() {
            return retailPrice;
        }

        public void setRetailPrice(int retailPrice) {
            this.retailPrice = retailPrice;
        }

        public Object getPhamAmount() {
            return phamAmount;
        }

        public void setPhamAmount(Object phamAmount) {
            this.phamAmount = phamAmount;
        }

        public Object getManufactor() {
            return manufactor;
        }

        public void setManufactor(Object manufactor) {
            this.manufactor = manufactor;
        }

        public Object getPhamCategoryId() {
            return phamCategoryId;
        }

        public void setPhamCategoryId(Object phamCategoryId) {
            this.phamCategoryId = phamCategoryId;
        }

        public Object getEnableFlag() {
            return enableFlag;
        }

        public void setEnableFlag(Object enableFlag) {
            this.enableFlag = enableFlag;
        }

        public Object getPhamTypeId() {
            return phamTypeId;
        }

        public void setPhamTypeId(Object phamTypeId) {
            this.phamTypeId = phamTypeId;
        }

        public Object getUnitName() {
            return unitName;
        }

        public void setUnitName(Object unitName) {
            this.unitName = unitName;
        }

        public Object getPhamTypeName() {
            return phamTypeName;
        }

        public void setPhamTypeName(Object phamTypeName) {
            this.phamTypeName = phamTypeName;
        }

        public boolean isOffenFlag() {
            return offenFlag;
        }

        public void setOffenFlag(boolean offenFlag) {
            this.offenFlag = offenFlag;
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

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
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

        public String getDiagnName() {
            return diagnName;
        }

        public void setDiagnName(String diagnName) {
            this.diagnName = diagnName;
        }

        public String getInputCode() {
            return inputCode;
        }

        public void setInputCode(String inputCode) {
            this.inputCode = inputCode;
        }

        public Object getOperator() {
            return operator;
        }

        public void setOperator(Object operator) {
            this.operator = operator;
        }

        public int getDiagnType() {
            return diagnType;
        }

        public void setDiagnType(int diagnType) {
            this.diagnType = diagnType;
        }

        public static class ParamsBean implements Serializable{
        }
    }
}
