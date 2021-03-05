package com.ssh.shuoshi.bean.im;

import com.ssh.shuoshi.bean.IMBaseMessage;

import java.io.Serializable;

/**
 * 问诊小结
 */
public class SummaryCardBean extends IMBaseMessage implements Serializable {

    private Content content;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public static class Content implements Serializable {
        // 问诊id，不是问诊小结id
        private int id;
        // 患者名称
        private String patientName;
        // 医生名称
        private String doctorName;

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

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }
    }

}
