package com.ssh.shuoshi.bean.prescription;

import java.io.Serializable;
import java.util.List;

/**
 * 处方	处方明细    第二层    第一层是 HisPrescriptionDtoBean
 * created by hwt on 2021/1/4
 */
public class HisPrescriptionDetailDtosBean implements Serializable {
    /**
     * searchValue : null
     * createBy : null
     * createTime : 2021-01-01 17:16
     * updateBy : null
     * updateTime : null
     * remark : null
     * params : {}
     * id : 302
     * prescriptionId : 224
     * phamId : 2
     * phamName : 硝苯地平缓释片(Ⅰ)(扬子江)
     * phamSpec : 10mg*12粒/盒
     * units : 包
     * amount : 2
     * dosage : 1
     * dosageUnits : 克
     * frequency : 每日1次
     * administration : 口服
     * freqDetail : null
     * price : 0.02
     * totalPrice : 0.04
     * phamTypeId : 1
     * perscriptionNo : PN210101171630035
     * phamOutCode : 2081366.0
     * phamBrand : null
     * phamTypeName : 西药
     * hisPrescriptionTcmDetails : null
     */

    private Object searchValue;
    private Object createBy;
    private String createTime;
    private Object updateBy;
    private Object updateTime;
    private Object remark;
    private ParamsBeanXX params;
    private Integer id;
    private int prescriptionId;
    private int phamId;
    private String phamName;
    private String phamSpec;
    private String units;
    private int amount;
    private double dosage;
    private String dosageUnits;
    private String frequency;
    private String administration;
    private String freqDetail;
    private double price;
    private double totalPrice;
    private int phamTypeId;
    private String perscriptionNo;
    private String phamOutCode;
    private Object phamBrand;
    private String phamTypeName;
    private List<HisPrescriptionTcmDetailsBean> hisPrescriptionTcmDetails;

    public List<HisPrescriptionTcmDetailsBean> getHisPrescriptionTcmDetails() {
        return hisPrescriptionTcmDetails;
    }

    public void setHisPrescriptionTcmDetails(List<HisPrescriptionTcmDetailsBean> hisPrescriptionTcmDetails) {
        this.hisPrescriptionTcmDetails = hisPrescriptionTcmDetails;
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

    public ParamsBeanXX getParams() {
        return params;
    }

    public void setParams(ParamsBeanXX params) {
        this.params = params;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
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

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getAdministration() {
        return administration;
    }

    public void setAdministration(String administration) {
        this.administration = administration;
    }

    public String getFreqDetail() {
        return freqDetail;
    }

    public void setFreqDetail(String freqDetail) {
        this.freqDetail = freqDetail;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getPhamTypeId() {
        return phamTypeId;
    }

    public void setPhamTypeId(int phamTypeId) {
        this.phamTypeId = phamTypeId;
    }

    public String getPerscriptionNo() {
        return perscriptionNo;
    }

    public void setPerscriptionNo(String perscriptionNo) {
        this.perscriptionNo = perscriptionNo;
    }

    public String getPhamOutCode() {
        return phamOutCode;
    }

    public void setPhamOutCode(String phamOutCode) {
        this.phamOutCode = phamOutCode;
    }

    public Object getPhamBrand() {
        return phamBrand;
    }

    public void setPhamBrand(Object phamBrand) {
        this.phamBrand = phamBrand;
    }

    public String getPhamTypeName() {
        return phamTypeName;
    }

    public void setPhamTypeName(String phamTypeName) {
        this.phamTypeName = phamTypeName;
    }


    public static class ParamsBeanXX implements Serializable {
    }
}

