package com.ssh.shuoshi.bean.request;

import java.util.List;

public class ChineseAddPrescriptionTemplateDrugs {

    private List<ChineseAddPrescriptionDrugsTcmDetails> hisPrescriptionTemplateTcmDetails;

    public ChineseAddPrescriptionTemplateDrugs(List<ChineseAddPrescriptionDrugsTcmDetails> hisPrescriptionTcmDetails) {
        this.hisPrescriptionTemplateTcmDetails = hisPrescriptionTcmDetails;
    }


    public List<ChineseAddPrescriptionDrugsTcmDetails> getHisPrescriptionTcmDetails() {
        return hisPrescriptionTemplateTcmDetails;
    }

    public void setHisPrescriptionTcmDetails(List<ChineseAddPrescriptionDrugsTcmDetails> hisPrescriptionTcmDetails) {
        this.hisPrescriptionTemplateTcmDetails = hisPrescriptionTcmDetails;
    }
}
