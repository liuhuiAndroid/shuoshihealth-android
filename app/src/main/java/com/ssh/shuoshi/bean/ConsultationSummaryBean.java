package com.ssh.shuoshi.bean;

import java.io.Serializable;

public class ConsultationSummaryBean implements Serializable {

    private int id;

    private String patientName;

    private String sexName;

    private int patientAge;

    public ConsultationSummaryBean(int id, String patientName, String sexName, int patientAge) {
        this.id = id;
        this.patientName = patientName;
        this.sexName = sexName;
        this.patientAge = patientAge;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public int getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(int patientAge) {
        this.patientAge = patientAge;
    }
}
