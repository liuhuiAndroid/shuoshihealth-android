package com.ssh.shuoshi.bean;

public class CommonlyPrescriptionInfo {

    private String result;
    private String rp;

    public CommonlyPrescriptionInfo(String result, String rp) {
        this.result = result;
        this.rp = rp;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRp() {
        return rp;
    }

    public void setRp(String rp) {
        this.rp = rp;
    }
}
