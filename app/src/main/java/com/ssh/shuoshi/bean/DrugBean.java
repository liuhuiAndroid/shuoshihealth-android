package com.ssh.shuoshi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * created by hwt on 2020/12/20
 */
public class DrugBean implements Serializable {

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

        //药品数据
        private Integer numAddDrug = 0;

        public Integer getNumAddDrug() {
            return numAddDrug;
        }

        public void setNumAddDrug(Integer numAddDrug) {
            this.numAddDrug = numAddDrug;
        }

        /**
         * searchValue : null
         * createBy : null
         * createTime : null
         * updateBy : null
         * updateTime : null
         * remark : null
         * params : {}
         * id : 5
         * phamOutCode : 202012141727005
         * phamName : 丹参
         * phamAliasName : null
         * phamStdCode : null
         * phamSpec : 90mg*8粒/盒
         * phamUnit : null
         * phamForm : null
         * tradePrice : 9.0
         * retailPrice : 99.0
         * phamAmount : null
         * manufactor : null
         * phamCategoryId : null
         * enableFlag : null
         * phamTypeId : null
         * phamTypeName : null
         * unitName : null
         * offenFlag : false
         */

        private Object searchValue;
        private Object createBy;
        private Object createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private ParamsBean params;
        private int id;
        private String phamOutCode;
        private String phamName;
        private String phamAliasName;
        private Object phamStdCode;
        private String phamSpec;
        private Object phamUnit;
        private Object phamForm;
        private double tradePrice;
        private double retailPrice;
        private Object phamAmount;
        private Object manufactor;
        private Object phamCategoryId;
        private Object enableFlag;
        private Object phamTypeId;
        private Object phamTypeName;
        private Object unitName;
        private boolean offenFlag;

        //药品数据
        private int num;

        //用药频次
        private String frequency;

        //是否显示其他输入框
        private boolean isShowFrequency;

        //其他用药频次
        private String otherFrequency;

        //用法
//        private String usage;
        private String administration;

        //一次用量  一次用量单位
        private String dosage;
        private String dosageUnits;


        //每次剂量单位
        private String dosageUnit;

        //模版明细ID
        private int templateId;

        //模版名称
        private String templateName;
        private Object pinyinFull;
        private Object pinyinShort;
        private String brand;
        private Integer phamVendorId;
        private Object deptList;
        // 剩余可用库存
        private Integer surplusUsableStock;

        public Integer getSurplusUsableStock() {
            return surplusUsableStock;
        }

        public void setSurplusUsableStock(Integer surplusUsableStock) {
            this.surplusUsableStock = surplusUsableStock;
        }

        public String getOtherFrequency() {
            return otherFrequency;
        }

        public void setOtherFrequency(String otherFrequency) {
            this.otherFrequency = otherFrequency;
        }

        public boolean isShowFrequency() {
            return isShowFrequency;
        }

        public void setShowFrequency(boolean showFrequency) {
            isShowFrequency = showFrequency;
        }

        public String getDosageUnit() {
            return dosageUnit;
        }

        public void setDosageUnit(String dosageUnit) {
            this.dosageUnit = dosageUnit;
        }

        public Object getPinyinFull() {
            return pinyinFull;
        }

        public void setPinyinFull(Object pinyinFull) {
            this.pinyinFull = pinyinFull;
        }

        public Object getPinyinShort() {
            return pinyinShort;
        }

        public void setPinyinShort(Object pinyinShort) {
            this.pinyinShort = pinyinShort;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public Integer getPhamVendorId() {
            return phamVendorId;
        }

        public void setPhamVendorId(Integer phamVendorId) {
            this.phamVendorId = phamVendorId;
        }

        public Object getDeptList() {
            return deptList;
        }

        public void setDeptList(Object deptList) {
            this.deptList = deptList;
        }

        public String getAdministration() {
            return administration;
        }

        public void setAdministration(String administration) {
            this.administration = administration;
        }

        public String getFrequency() {
            return frequency;
        }

        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        public String getTemplateName() {
            return templateName;
        }

        public void setTemplateName(String templateName) {
            this.templateName = templateName;
        }

        public int getTemplateId() {
            return templateId;
        }

        public void setTemplateId(int templateId) {
            this.templateId = templateId;
        }

        public String getDosage() {
            return dosage;
        }

        public void setDosage(String dosage) {
            this.dosage = dosage;
        }

        public String getDosageUnits() {
            return dosageUnits;
        }

        public void setDosageUnits(String dosageUnits) {
            this.dosageUnits = dosageUnits;
        }

//        public String getUsage() {
//            return usage;
//        }
//
//        public void setUsage(String usage) {
//            this.usage = usage;
//        }

//        public int getDictId() {
//            return dictId;
//        }
//
//        public void setDictId(int dictId) {
//            this.dictId = dictId;
//        }

//        public String getDictName() {
//            return dictName;
//        }
//
//        public void setDictName(String dictName) {
//            this.dictName = dictName;
//        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
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

        public String getPhamAliasName() {
            return phamAliasName;
        }

        public void setPhamAliasName(String phamAliasName) {
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

        public double getTradePrice() {
            return tradePrice;
        }

        public void setTradePrice(double tradePrice) {
            this.tradePrice = tradePrice;
        }

        public double getRetailPrice() {
            return retailPrice;
        }

        public void setRetailPrice(double retailPrice) {
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

        public Object getPhamTypeName() {
            return phamTypeName;
        }

        public void setPhamTypeName(Object phamTypeName) {
            this.phamTypeName = phamTypeName;
        }

        public Object getUnitName() {
            return unitName;
        }

        public void setUnitName(Object unitName) {
            this.unitName = unitName;
        }

        public boolean isOffenFlag() {
            return offenFlag;
        }

        public void setOffenFlag(boolean offenFlag) {
            this.offenFlag = offenFlag;
        }

        public static class ParamsBean implements Serializable {
        }
    }
}
