package com.ssh.shuoshi.bean.template;

import java.io.Serializable;

/**
 * 中药明细实体类，抽出来通用,最底层实体类
 * created by hwt on 2020/12/24
 */
public class HisPrescriptionTemplateTcmDetailsBean implements Serializable {

    /**
     * searchValue : null
     * createBy : null
     * createTime : 2020-12-24 17:00
     * updateBy : null
     * updateTime : null
     * remark : null
     * params : {}
     * id : 1
     * templateDetailId : 18
     * phamId : 9
     * phamName : 999感冒灵
     * phamSpec : null
     * units : g
     * amount : 1
     */

    private Object searchValue;
    private Object createBy;
    private String createTime;
    private Object updateBy;
    private Object updateTime;
    private Object remark;
    private ParamsBeanXX params;
    private int id;
    private int templateDetailId;
    private int phamId;
    private String phamName;
    private Object phamSpec;
    private String units;
    private int amount;

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

    public int getTemplateDetailId() {
        return templateDetailId;
    }

    public void setTemplateDetailId(int templateDetailId) {
        this.templateDetailId = templateDetailId;
    }

    public int getPhamId() {
        return phamId;
    }

    public void setPhamId(int phamId) {
        this.phamId = phamId;
    }

    public String getPhamName() {
        return phamName;
    }

    public void setPhamName(String phamName) {
        this.phamName = phamName;
    }

    public Object getPhamSpec() {
        return phamSpec;
    }

    public void setPhamSpec(Object phamSpec) {
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

    public static class ParamsBeanXX implements Serializable {
    }
}

