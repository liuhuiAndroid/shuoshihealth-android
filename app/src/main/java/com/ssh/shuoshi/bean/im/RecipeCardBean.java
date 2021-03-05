package com.ssh.shuoshi.bean.im;

import com.ssh.shuoshi.bean.IMBaseMessage;

import java.io.Serializable;
import java.util.List;

/**
 * created by hwt on 2020/12/21
 * 开处方卡片
 */
public class RecipeCardBean extends IMBaseMessage implements Serializable {

    private Content content;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public static class Content implements Serializable {

        // 处方id
        private int id;
        // 处方类型
        private int prescriptionTypeId;
        // 医生名称
        private String doctorName;
        // 患者名称
        private String patientName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPrescriptionTypeId() {
            return prescriptionTypeId;
        }

        public void setPrescriptionTypeId(int prescriptionTypeId) {
            this.prescriptionTypeId = prescriptionTypeId;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }
    }
}
