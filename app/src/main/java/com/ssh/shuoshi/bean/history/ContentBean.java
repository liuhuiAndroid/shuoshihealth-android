package com.ssh.shuoshi.bean.history;

import java.util.List;

/**
 * created by hwt on 2021/1/15
 */
public class ContentBean {

    private String patientName;
    private String diagDesc;
    private String title;
    private String sexName;
    private Integer patientAge;
    private String content;
    private String consultationTypeName;
    private Integer consultationId;
    private String detail;
    private String duration;
    private Integer prescriptionId;
    private Integer perscriptionTypeId;
    private List<ChildrenPresBean> list;
    private Integer status;
    private int room_id;
    private int version;
    private String call_id;
    private int action;
    private List<String> invited_list;
    private int id;
    private String from;
    private String doctorName;

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getInvited_list() {
        return invited_list;
    }

    public void setInvited_list(List<String> invited_list) {
        this.invited_list = invited_list;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCall_id() {
        return call_id;
    }

    public void setCall_id(String call_id) {
        this.call_id = call_id;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDiagDesc() {
        return diagDesc;
    }

    public void setDiagDesc(String diagDesc) {
        this.diagDesc = diagDesc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public Integer getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(Integer patientAge) {
        this.patientAge = patientAge;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public class ChildrenPresBean {

        private String name;
        private String guige;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGuige() {
            return guige;
        }

        public void setGuige(String guige) {
            this.guige = guige;
        }
    }

    public Integer getPerscriptionTypeId() {
        return perscriptionTypeId;
    }

    public void setPerscriptionTypeId(Integer perscriptionTypeId) {
        this.perscriptionTypeId = perscriptionTypeId;
    }

    public List<ChildrenPresBean> getList() {
        return list;
    }

    public void setList(List<ChildrenPresBean> list) {
        this.list = list;
    }

    public Integer getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getConsultationTypeName() {
        return consultationTypeName;
    }

    public void setConsultationTypeName(String consultationTypeName) {
        this.consultationTypeName = consultationTypeName;
    }

    public Integer getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(Integer consultationId) {
        this.consultationId = consultationId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
