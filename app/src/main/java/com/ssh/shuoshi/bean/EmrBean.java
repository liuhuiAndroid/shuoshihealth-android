package com.ssh.shuoshi.bean;

/**
 * created by hwt on 2020/12/18
 */
public class EmrBean {


    /**
     * searchValue : null
     * createBy : null
     * createTime : 2020-12-14 15:57:39
     * updateBy : null
     * updateTime : null
     * remark : null
     * params : {}
     * id : 1
     * emrNo : MR201210133500123
     * consultationId : 1
     * patientId : 1
     * visitDate : 2020-12-14
     * age : 40
     * doctorId : 1
     * doctor : 医生123
     * illnessDesc : 病例摘要？
     * nowIllness : null
     * anamnesis : null
     * familyIll : null
     * marrital : null
     * individual : null
     * menses : null
     * drugAllergy : null
     * diagDesc : 西医诊断qwe
     * cdiagDesc : 中医诊断asdf
     * orders : 多多多多少少少
     * visitedFlag : 0
     * visitHospitalName : null
     * visitHospitalDiag : null
     * checkImg : null
     * memo : 测试
     * consultationNo : null
     * patientName : patient1
     * sexName : 女
     * patientAge : 21
     * doctorDeptName : null
     */

    private Object searchValue;
    private Object createBy;
    //问诊日期，修改不能超过当天
    private String createTime;
    private Object updateBy;
    private Object updateTime;
    private Object remark;
    private ParamsBean params;
    private int id;
    private String emrNo;
    private int consultationId;
    private int patientId;
    private String visitDate;
    private int age;
    private int doctorId;
    private String doctor;
    private String illnessDesc;
    private Object nowIllness;
    private Object anamnesis;
    private Object familyIll;
    private Object marrital;
    private Object individual;
    private Object menses;
    private Object drugAllergy;
    private String diagDesc;
    private String cdiagDesc;
    private String orders;
    private int visitedFlag;
    private Object visitHospitalName;
    private Object visitHospitalDiag;
    private Object checkImg;
    private String memo;
    private String consultationNo;
    private String patientName;
    private String sexName;
    private int patientAge;
    private String doctorDeptName;

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

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmrNo() {
        return emrNo;
    }

    public void setEmrNo(String emrNo) {
        this.emrNo = emrNo;
    }

    public int getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(int consultationId) {
        this.consultationId = consultationId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getIllnessDesc() {
        return illnessDesc;
    }

    public void setIllnessDesc(String illnessDesc) {
        this.illnessDesc = illnessDesc;
    }

    public Object getNowIllness() {
        return nowIllness;
    }

    public void setNowIllness(Object nowIllness) {
        this.nowIllness = nowIllness;
    }

    public Object getAnamnesis() {
        return anamnesis;
    }

    public void setAnamnesis(Object anamnesis) {
        this.anamnesis = anamnesis;
    }

    public Object getFamilyIll() {
        return familyIll;
    }

    public void setFamilyIll(Object familyIll) {
        this.familyIll = familyIll;
    }

    public Object getMarrital() {
        return marrital;
    }

    public void setMarrital(Object marrital) {
        this.marrital = marrital;
    }

    public Object getIndividual() {
        return individual;
    }

    public void setIndividual(Object individual) {
        this.individual = individual;
    }

    public Object getMenses() {
        return menses;
    }

    public void setMenses(Object menses) {
        this.menses = menses;
    }

    public Object getDrugAllergy() {
        return drugAllergy;
    }

    public void setDrugAllergy(Object drugAllergy) {
        this.drugAllergy = drugAllergy;
    }

    public String getDiagDesc() {
        return diagDesc;
    }

    public void setDiagDesc(String diagDesc) {
        this.diagDesc = diagDesc;
    }

    public String getCdiagDesc() {
        return cdiagDesc;
    }

    public void setCdiagDesc(String cdiagDesc) {
        this.cdiagDesc = cdiagDesc;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public int getVisitedFlag() {
        return visitedFlag;
    }

    public void setVisitedFlag(int visitedFlag) {
        this.visitedFlag = visitedFlag;
    }

    public Object getVisitHospitalName() {
        return visitHospitalName;
    }

    public void setVisitHospitalName(Object visitHospitalName) {
        this.visitHospitalName = visitHospitalName;
    }

    public Object getVisitHospitalDiag() {
        return visitHospitalDiag;
    }

    public void setVisitHospitalDiag(Object visitHospitalDiag) {
        this.visitHospitalDiag = visitHospitalDiag;
    }

    public Object getCheckImg() {
        return checkImg;
    }

    public void setCheckImg(Object checkImg) {
        this.checkImg = checkImg;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getConsultationNo() {
        return consultationNo;
    }

    public void setConsultationNo(String consultationNo) {
        this.consultationNo = consultationNo;
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

    public String getDoctorDeptName() {
        return doctorDeptName;
    }

    public void setDoctorDeptName(String doctorDeptName) {
        this.doctorDeptName = doctorDeptName;
    }

    public static class ParamsBean {
    }
}
