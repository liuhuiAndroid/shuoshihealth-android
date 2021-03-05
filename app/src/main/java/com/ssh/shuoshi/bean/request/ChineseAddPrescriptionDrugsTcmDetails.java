package com.ssh.shuoshi.bean.request;

/**
 * 中药 药材明细
 */
public class ChineseAddPrescriptionDrugsTcmDetails {

    // 药品ID
    private int phamId;
    // 数量
    private int amount;

    public ChineseAddPrescriptionDrugsTcmDetails(int phamId, int amount) {
        this.phamId = phamId;
        this.amount = amount;
    }

    public int getPhamId() {
        return phamId;
    }

    public void setPhamId(int phamId) {
        this.phamId = phamId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
