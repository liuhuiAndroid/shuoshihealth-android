package com.ssh.shuoshi.bean.prescription;

import java.io.Serializable;

/**
 * 处方	中药明细    第三层    第二层是 HisPrescriptionDetailDtosBean
 * created by hwt on 2021/1/4
 */
public class HisPrescriptionTcmDetailsBean implements Serializable {
    /**
     * searchValue : null
     * createBy : null
     * createTime : 2020-12-29 15:39
     * updateBy : null
     * updateTime : null
     * remark : null
     * params : {}
     * id : 139
     * phamId : 5
     * prescriptionDetailId : 111
     * phamName : 丹参
     * phamSpec : null
     * units : g
     * amount : 22.0
     * price : 0.04
     */

    private Object searchValue;
    private Object createBy;
    private String createTime;
    private Object updateBy;
    private Object updateTime;
    private Object remark;
    private ParamsBeanXX params;
    private int id;
    private int phamId;
    private int prescriptionDetailId;
    private String phamName;
    private String phamSpec;
    private String units;
    private int amount;
    private double price;


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

    public ParamsBeanXX getParams() {
        return params;
    }

    public void setParams(ParamsBeanXX params) {
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

    public int getPrescriptionDetailId() {
        return prescriptionDetailId;
    }

    public void setPrescriptionDetailId(int prescriptionDetailId) {
        this.prescriptionDetailId = prescriptionDetailId;
    }

    public String getPhamName() {
        return phamName;
    }

    public void setPhamName(String phamName) {
        this.phamName = phamName;
    }

    public String getPhamSpec() {
        return phamSpec;
    }

    public void setPhamSpec(String phamSpec) {
        this.phamSpec = phamSpec;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public static class ParamsBeanXX implements Serializable {
    }
}
