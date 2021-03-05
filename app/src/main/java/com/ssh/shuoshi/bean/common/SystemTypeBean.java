package com.ssh.shuoshi.bean.common;

import com.google.gson.annotations.SerializedName;
import com.ssh.shuoshi.view.wheelview.interfaces.IPickerViewData;

import java.io.Serializable;
import java.util.List;

/**
 * 根据字典类型获取字典列表 通用
 * created by hwt on 2020/12/27
 */
public class SystemTypeBean implements Serializable {

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

    public static class RowsBean implements Serializable, IPickerViewData {
        /**
         * searchValue : null
         * createBy : admin
         * createTime : 2020-12-27 14:32
         * updateBy : null
         * updateTime : null
         * remark : null
         * params : {}
         * dictCode : 147
         * dictSort : 1
         * dictLabel : 每日1次
         * dictValue : 1
         * dictType : prescription_frequency
         * cssClass : null
         * listClass : null
         * isDefault : N
         * status : 0
         * default : false
         */

        private Object searchValue;
        private String createBy;
        private String createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private ParamsBean params;
        private int dictCode;
        private int dictSort;
        private String dictLabel;
        private String dictValue;
        private String dictType;
        private Object cssClass;
        private Object listClass;
        private String isDefault;
        private String status;
        @SerializedName("default")
        private boolean defaultX;

        public Object getSearchValue() {
            return searchValue;
        }

        public void setSearchValue(Object searchValue) {
            this.searchValue = searchValue;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
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

        public int getDictCode() {
            return dictCode;
        }

        public void setDictCode(int dictCode) {
            this.dictCode = dictCode;
        }

        public int getDictSort() {
            return dictSort;
        }

        public void setDictSort(int dictSort) {
            this.dictSort = dictSort;
        }

        public String getDictLabel() {
            return dictLabel;
        }

        public void setDictLabel(String dictLabel) {
            this.dictLabel = dictLabel;
        }

        public String getDictValue() {
            return dictValue;
        }

        public void setDictValue(String dictValue) {
            this.dictValue = dictValue;
        }

        public String getDictType() {
            return dictType;
        }

        public void setDictType(String dictType) {
            this.dictType = dictType;
        }

        public Object getCssClass() {
            return cssClass;
        }

        public void setCssClass(Object cssClass) {
            this.cssClass = cssClass;
        }

        public Object getListClass() {
            return listClass;
        }

        public void setListClass(Object listClass) {
            this.listClass = listClass;
        }

        public String getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(String isDefault) {
            this.isDefault = isDefault;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isDefaultX() {
            return defaultX;
        }

        public void setDefaultX(boolean defaultX) {
            this.defaultX = defaultX;
        }

        @Override
        public String getPickerViewText() {
            return this.dictLabel;
        }

        public static class ParamsBean implements Serializable {
        }
    }
}
