package com.ssh.shuoshi.bean.template;

import java.io.Serializable;
import java.util.List;

/**
 * 处方明细，第二层
 * created by hwt on 2020/12/24
 */
public class HisPrescriptionTemplateDetailDtosBean implements Serializable {

    /**
     * searchValue : null
     * createBy : null
     * createTime : 2020-12-24 17:00
     * updateBy : null
     * updateTime : null
     * remark : null
     * params : {}
     * id : 18
     * templateId : 13
     * phamId : null
     * phamName : 颗粒剂
     * phamSpec : null
     * units : 副
     * amount : null
     * dosage : null
     * dosageUnits : 副
     * frequencyId : null
     * administration : null
     * frequencyName : null
     * hisPrescriptionTemplateTcmDetails : [{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:00","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":1,"templateDetailId":18,"phamId":9,"phamName":"999感冒灵","phamSpec":null,"units":"g","amount":1},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:00","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":2,"templateDetailId":18,"phamId":5,"phamName":"丹参","phamSpec":null,"units":"g","amount":2},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:00","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":3,"templateDetailId":18,"phamId":6,"phamName":"炙甘草","phamSpec":null,"units":"g","amount":3},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:00","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":4,"templateDetailId":18,"phamId":3,"phamName":"桂枝","phamSpec":null,"units":"g","amount":4},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:03","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":5,"templateDetailId":19,"phamId":9,"phamName":"999感冒灵","phamSpec":null,"units":"g","amount":1},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:03","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":6,"templateDetailId":19,"phamId":5,"phamName":"丹参","phamSpec":null,"units":"g","amount":2},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:03","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":7,"templateDetailId":19,"phamId":6,"phamName":"炙甘草","phamSpec":null,"units":"g","amount":3},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:03","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":8,"templateDetailId":19,"phamId":3,"phamName":"桂枝","phamSpec":null,"units":"g","amount":14},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:03","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":9,"templateDetailId":19,"phamId":9,"phamName":"999感冒灵","phamSpec":null,"units":"g","amount":19},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:03","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":10,"templateDetailId":19,"phamId":9,"phamName":"999感冒灵","phamSpec":null,"units":"g","amount":19},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:03","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":11,"templateDetailId":19,"phamId":9,"phamName":"999感冒灵","phamSpec":null,"units":"g","amount":19}]
     */

    private Object searchValue;
    private Object createBy;
    private String createTime;
    private Object updateBy;
    private Object updateTime;
    private Object remark;
    private ParamsBeanX params;
    private int id;
    private int templateId;
    private int phamId;
    private String phamName;
    private String phamSpec;
    private String units;
    private int amount;
    private double dosage;
    private String dosageUnits;
    private int frequencyId;
    private String administration;
    private String frequencyName;
    private String frequency;

    private List<HisPrescriptionTemplateTcmDetailsBean> hisPrescriptionTemplateTcmDetails;

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
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

    public ParamsBeanX getParams() {
        return params;
    }

    public void setParams(ParamsBeanX params) {
        this.params = params;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
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

    public double getDosage() {
        return dosage;
    }

    public void setDosage(double dosage) {
        this.dosage = dosage;
    }

    public String getDosageUnits() {
        return dosageUnits;
    }

    public void setDosageUnits(String dosageUnits) {
        this.dosageUnits = dosageUnits;
    }

    public int getFrequencyId() {
        return frequencyId;
    }

    public void setFrequencyId(int frequencyId) {
        this.frequencyId = frequencyId;
    }

    public String getAdministration() {
        return administration;
    }

    public void setAdministration(String administration) {
        this.administration = administration;
    }

    public String getFrequencyName() {
        return frequencyName;
    }

    public void setFrequencyName(String frequencyName) {
        this.frequencyName = frequencyName;
    }

    public List<HisPrescriptionTemplateTcmDetailsBean> getHisPrescriptionTemplateTcmDetails() {
        return hisPrescriptionTemplateTcmDetails;
    }

    public void setHisPrescriptionTemplateTcmDetails(List<HisPrescriptionTemplateTcmDetailsBean> hisPrescriptionTemplateTcmDetails) {
        this.hisPrescriptionTemplateTcmDetails = hisPrescriptionTemplateTcmDetails;
    }

    public static class ParamsBeanX implements Serializable {
    }
}
