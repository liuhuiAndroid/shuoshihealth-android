package com.ssh.shuoshi.bean.request;

import java.util.List;

public class ChineseAddPrescriptionDrugs {

    private Integer id;
    // 数量
    private String amount;
    // 一次用量
    private String dosage;
    // 用药途径
    private String administration;
    // 用药频次
//    private int frequencyId;
    private String frequency;
    // 医嘱说明
    private String freqDetail;
    private List<ChineseAddPrescriptionDrugsTcmDetails> hisPrescriptionTcmDetails;

    public ChineseAddPrescriptionDrugs(List<ChineseAddPrescriptionDrugsTcmDetails> hisPrescriptionTcmDetails) {
        this.hisPrescriptionTcmDetails = hisPrescriptionTcmDetails;
    }

    public ChineseAddPrescriptionDrugs(String amount, String dosage, String administration, String frequency,
                                       String freqDetail,Integer secondId,
                                       List<ChineseAddPrescriptionDrugsTcmDetails> hisPrescriptionTcmDetails) {
        this.amount = amount;
        this.dosage = dosage;
        this.administration = administration;
        this.frequency = frequency;
        this.freqDetail = freqDetail;
        this.id = secondId;
        this.hisPrescriptionTcmDetails = hisPrescriptionTcmDetails;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getAdministration() {
        return administration;
    }

    public void setAdministration(String administration) {
        this.administration = administration;
    }

//    public int getFrequencyId() {
//        return frequencyId;
//    }
//
//    public void setFrequencyId(int frequencyId) {
//        this.frequencyId = frequencyId;
//    }

    public String getFreqDetail() {
        return freqDetail;
    }

    public void setFreqDetail(String freqDetail) {
        this.freqDetail = freqDetail;
    }

    public List<ChineseAddPrescriptionDrugsTcmDetails> getHisPrescriptionTcmDetails() {
        return hisPrescriptionTcmDetails;
    }

    public void setHisPrescriptionTcmDetails(List<ChineseAddPrescriptionDrugsTcmDetails> hisPrescriptionTcmDetails) {
        this.hisPrescriptionTcmDetails = hisPrescriptionTcmDetails;
    }
}
