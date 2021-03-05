package com.ssh.shuoshi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 常用药品
 * created by hwt on 2020/12/20
 */
public class OftenDrugBean implements Serializable {

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
         * createTime : 2020-12-14 19:49:54
         * updateBy : null
         * updateTime : null
         * remark : null
         * params : {}
         * id : 1
         * phamId : 1
         * doctorId : 19
         * pharmacyName : 缬沙坦胶囊(代文)
         * pharmacyAliasName : null
         * phamSpec : 80mg*7粒/盒
         * retailPrice : 100
         * unitName : null
         * phamTypeName : 西药
         */

        private Object searchValue;
        private Object createBy;
        private String createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private ParamsBean params;
        private int id;
        private int phamId;
        private int doctorId;
        private String pharmacyName;
        private String pharmacyAliasName;
        private String phamSpec;
        private double retailPrice;
        private Object unitName;
        private String phamTypeName;
        private boolean enableFlag;
        private Integer phamVendorId;
        private String brand;

        //每次剂量单位
        private String dosageUnit;

        // 剩余可用库存
        private Integer surplusUsableStock;

        public Integer getSurplusUsableStock() {
            return surplusUsableStock;
        }

        public void setSurplusUsableStock(Integer surplusUsableStock) {
            this.surplusUsableStock = surplusUsableStock;
        }

        public String getDosageUnit() {
            return dosageUnit;
        }

        public void setDosageUnit(String dosageUnit) {
            this.dosageUnit = dosageUnit;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public boolean isEnableFlag() {
            return enableFlag;
        }

        public void setEnableFlag(boolean enableFlag) {
            this.enableFlag = enableFlag;
        }

        public Integer getPhamVendorId() {
            return phamVendorId;
        }

        public void setPhamVendorId(Integer phamVendorId) {
            this.phamVendorId = phamVendorId;
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

        public int getPhamId() {
            return phamId;
        }

        public void setPhamId(int phamId) {
            this.phamId = phamId;
        }

        public int getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(int doctorId) {
            this.doctorId = doctorId;
        }

        public String getPharmacyName() {
            return pharmacyName;
        }

        public void setPharmacyName(String pharmacyName) {
            this.pharmacyName = pharmacyName;
        }

        public String getPharmacyAliasName() {
            return pharmacyAliasName;
        }

        public void setPharmacyAliasName(String pharmacyAliasName) {
            this.pharmacyAliasName = pharmacyAliasName;
        }

        public String getPhamSpec() {
            return phamSpec;
        }

        public void setPhamSpec(String phamSpec) {
            this.phamSpec = phamSpec;
        }

        public double getRetailPrice() {
            return retailPrice;
        }

        public void setRetailPrice(double retailPrice) {
            this.retailPrice = retailPrice;
        }

        public Object getUnitName() {
            return unitName;
        }

        public void setUnitName(Object unitName) {
            this.unitName = unitName;
        }

        public String getPhamTypeName() {
            return phamTypeName;
        }

        public void setPhamTypeName(String phamTypeName) {
            this.phamTypeName = phamTypeName;
        }

        public static class ParamsBean implements Serializable {
        }
    }
}
